/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dao;

import com.idebsystems.serviciosweb.entities.ArchivoXml;
import com.idebsystems.serviciosweb.entities.DocumentoReembolsos;
import com.idebsystems.serviciosweb.util.Persistencia;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author jorge
 */
public class DocumentoReembolsosDAO extends Persistencia {
    
    private static final Logger LOGGER = Logger.getLogger(DocumentoReembolsosDAO.class.getName());
    
    public List<Object> listarDocumentos(Date fechaInicio, Date fechaFinal, Long usuarioCarga, 
            String estado, Integer desde, Integer hasta, Long idAprobador, String numeroRC, String tipoReembolso,
            String numeroReembolso, String numeroLC) throws Exception {
        try {
            List<Object> respuesta = new ArrayList<>();
            getEntityManager();
            
            String sql = "FROM DocumentoReembolsos d WHERE d.id > 0 ";
            
            //el idAprobar y el usuariocarga son obligaorios como unicos como primer filtro
            if(Objects.nonNull(idAprobador)){
                sql += " AND (d.idAprobador = :idAprobador OR d.usuarioCarga = :idAprobador)";
            }
            else if(Objects.nonNull(usuarioCarga)){
                sql += " AND d.usuarioCarga = :usuarioCarga";
            }
            
            if(Objects.nonNull(numeroReembolso) && !numeroReembolso.isBlank()){
                sql += " AND d.numeroReembolso = :numeroReembolso";
            }
            else{
                sql += " AND d.fechaCarga between :fechaInicio AND :fechaFinal";
                
                if(Objects.nonNull(estado) && !estado.isBlank()){
                    sql += " AND d.estado = :estado";
                }
                if(Objects.nonNull(numeroRC) && !numeroRC.isBlank()){
                    sql += " AND d.numeroRC = :numeroRC";
                }
                if(Objects.nonNull(tipoReembolso) && !tipoReembolso.isBlank()){
                    sql += " AND d.tipoReembolso = :tipoReembolso";
                }
                if(Objects.nonNull(numeroLC) && !numeroLC.isBlank()){
                    sql += " AND d.liquidacionCompra.numero = :numeroLC";
                }
            }
            
            sql += " order by d.fechaCarga";
            
            Query query = em.createQuery(sql);
            
            if(Objects.nonNull(idAprobador)){
                query.setParameter("idAprobador", idAprobador);
            }
            else if(Objects.nonNull(usuarioCarga)){
                query.setParameter("usuarioCarga", usuarioCarga);
            }
            
            if(Objects.nonNull(numeroReembolso) && !numeroReembolso.isBlank()){
                query.setParameter("numeroReembolso", numeroReembolso);
            }
            else{
                query.setParameter("fechaInicio", fechaInicio);
                query.setParameter("fechaFinal", fechaFinal);
            
                if(Objects.nonNull(estado) && !estado.isBlank()){
                    query.setParameter("estado", estado);
                }
                if(Objects.nonNull(numeroRC) && !numeroRC.isBlank()){
                    query.setParameter("numeroRC", numeroRC);
                }
                if(Objects.nonNull(tipoReembolso) && !tipoReembolso.isBlank()){
                    query.setParameter("tipoReembolso", tipoReembolso);
                }
                if(Objects.nonNull(numeroLC) && !numeroLC.isBlank()){
                    query.setParameter("numeroLC", numeroLC);
                }
            }
            
            //para obtener el total de los registros a buscar
            Integer totalRegistros = query.getResultList().size();
            respuesta.add(totalRegistros);
            
            LOGGER.log(Level.INFO, "total de registeos: {0}", totalRegistros);
            
            //para la paginacion
            query.setFirstResult(desde).setMaxResults(hasta);
            
            List<DocumentoReembolsos> listaPorFecha = query.getResultList();
            respuesta.add(listaPorFecha);

            return respuesta;

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    public DocumentoReembolsos guardarDocumentoReembolsos(DocumentoReembolsos data, String ids, Long idUsuario, boolean eliminar) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();

            if (Objects.nonNull(data.getId()) && data.getId() > 0) {
                em.merge(data); //update
            } else {
                em.persist(data); //insert
            }
            em.flush(); //Confirmar el insert o update
            
            //actualizar el estado de los documentos
            LOGGER.log(Level.INFO, "los id son: {0}", ids);
            String[] arrids = ids.split(",");
            for(String id : arrids){
                if(Objects.nonNull(id) && !id.isBlank()){
                    ArchivoXml xml = em.find(ArchivoXml.class, Long.parseLong(id));
                    if(Objects.nonNull(xml)){
                        xml.setEstadoSistema(data.getEstado());
                        xml.setIdReembolso(data.getId());
                        em.merge(xml);
                    }
                }
            }
            
            //eliminar los registros que no s enviaron al reembolso
            if(eliminar){
                Query query = em.createQuery("DELETE FROM ArchivoXml ax WHERE ax.estadoSistema = 'CARGADO' AND ax.idUsuarioCarga = :idUsuarioCarga AND ax.id NOT IN (" + ids + ")");
                query.setParameter("idUsuarioCarga", idUsuario);
                query.executeUpdate();
            }

            em.getTransaction().commit();

            return data;

        } catch (SQLException exc) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } catch (Exception exc) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, null, exc);
            if(exc.getMessage().contains("documentoreembolsos_numero_uk")){
                throw new Exception("El n\u00famero de reembolso "+data.getNumeroReembolso()+" ya existe, revise la configuraci\u00f3n en la pantalla secuenciales de reembolsos.");
            }
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    
    public List<DocumentoReembolsos> getDocumentosPorIdUsuario(Long idUsuario) throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM DocumentoReembolsos d WHERE d.usuarioCarga = :idUsuario");
            query.setParameter("idUsuario", idUsuario);

            List<DocumentoReembolsos> lista = query.getResultList();

            return lista;

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    
    
    
    public DocumentoReembolsos getDocumentosPorId(Long id) throws Exception {
        try {
            getEntityManager();

            return em.find(DocumentoReembolsos.class, id);

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    
    public boolean tieneDocumentosPorAprobarUsuario(Long idUsuarioAprobador, String ptiempo) throws Exception {
        try {
            getEntityManager();

            Query query = em.createNativeQuery("Select d.id FROM DocumentoReembolsos d "
                    + " WHERE d.idAprobador = ?idUsuario AND d.estado = ?estado"
                    + " AND (d.fechaAlerta is null or (d.fechaAlerta + interval '"+ptiempo+" hour') <= now())");
            
            query.setParameter("idUsuario", idUsuarioAprobador);
            query.setParameter("estado", "POR_AUTORIZAR");

            List<Long> lista = query.getResultList();

            if(lista.isEmpty()){
                return false;
            }
            else{
                lista.forEach(idReem -> {actualizarFechaAlerta(idReem, em);});
                return true;
            }

       } catch (NoResultException exc) {
            return false;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    /**
     * este metod busca los reembolsos pendientes para el rol contador y el rol auxiliar
     * @param ptiempo 
     * @throws Exception
     * @return 
     */
    public boolean tieneDocumentosPorAprobarContador(String ptiempo) throws Exception {
        try {
            getEntityManager();

            Query query1 = em.createNativeQuery("Select d.id FROM DocumentoReembolsos d "
                    + " WHERE d.estado = ?estadouno AND d.tipoReembolso = '4' "
                    + " AND (d.fechaAlerta is null or (d.fechaAlerta + interval '"+ptiempo+" hour') <= now())"
                    + " union "
                    + "Select d.id FROM DocumentoReembolsos d "
                    + " WHERE d.estado = ?estadodos AND d.tipoReembolso IN ('1','2','3') "
                    + " AND (d.fechaAlerta is null or (d.fechaAlerta + interval '"+ptiempo+" hour') <= now())");
            
            query1.setParameter("estadouno", "POR_AUTORIZAR");
            query1.setParameter("estadodos", "APROBADO");

            List<Long> lista1 = query1.getResultList();
            
            if(lista1.isEmpty()){
                return false;
            }
            else{
                lista1.forEach(idReem -> {actualizarFechaAlerta(idReem, em);});
                return true;
            }

       } catch (NoResultException exc) {
            return false;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    
    public void actualizarFechaAlerta(Long idReembolso, EntityManager em) {
        try {
            //aqui no se abre el entitymm
            em.getTransaction().begin();

            DocumentoReembolsos dr = em.find(DocumentoReembolsos.class, idReembolso);
            dr.setFechaAlerta(new Date());
            em.merge(dr); //update

            em.getTransaction().commit();
        
        } catch (Exception exc) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, null, exc);
        } finally {
            //aqui no se cierra el ent
        }
    }
}
