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
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author jorge
 */
public class DocumentoReembolsosDAO extends Persistencia {
    
    private static final Logger LOGGER = Logger.getLogger(DocumentoReembolsosDAO.class.getName());
    
    public List<Object> listarDocumentos(Date fechaInicio, Date fechaFinal, Long usuarioCarga, 
            String estado, Integer desde, Integer hasta, Long idAprobador) throws Exception {
        try {
            List<Object> respuesta = new ArrayList<>();
            getEntityManager();
            
            

            String sql = "FROM DocumentoReembolsos d ";
            sql += " where d.fechaCarga between :fechaInicio AND :fechaFinal";
            
            if(Objects.nonNull(idAprobador)){
                sql += " AND d.idAprobador = :idAprobador";
            }
            else if(Objects.nonNull(usuarioCarga)){
                sql += " AND d.usuarioCarga = :usuarioCarga";
            }
            
            if(Objects.nonNull(estado) && !estado.isBlank()){
                sql += " AND d.estado = :estado";
            }
            
            sql += " order by d.fechaCarga";
            
            Query query = em.createQuery(sql);
            
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFinal", fechaFinal);
            
            if(Objects.nonNull(idAprobador)){
                query.setParameter("idAprobador", idAprobador);
            }
            else if(Objects.nonNull(usuarioCarga)){
                query.setParameter("usuarioCarga", usuarioCarga);
            }
            
            if(Objects.nonNull(estado) && !estado.isBlank()){
                query.setParameter("estado", estado);
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
            String[] arrids = ids.split(",");
            for(String id : arrids){
                if(Objects.nonNull(id) && !id.isBlank()){
                    ArchivoXml xml = em.find(ArchivoXml.class, Long.parseLong(id));
                    xml.setEstadoSistema(data.getEstado());
                    em.merge(xml);
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
}
