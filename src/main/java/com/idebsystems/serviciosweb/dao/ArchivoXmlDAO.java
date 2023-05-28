/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dao;

import com.idebsystems.serviciosweb.dto.ArchivoXmlDTO;
import com.idebsystems.serviciosweb.entities.ArchivoXml;
import com.idebsystems.serviciosweb.entities.Usuario;
import com.idebsystems.serviciosweb.mappers.ArchivoXmlMapper;
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
public class ArchivoXmlDAO extends Persistencia {

    private static final Logger LOGGER = Logger.getLogger(ArchivoXmlDAO.class.getName());
    
    public String guardarDatosArchivo(ArchivoXml archivoXml) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();

            if (Objects.nonNull(archivoXml.getId()) && archivoXml.getId() > 0) {
                em.merge(archivoXml);
            } else {
                em.persist(archivoXml);
            }
            em.flush();

            em.getTransaction().commit();
            
            LOGGER.log(Level.INFO, "el id archivoXml: {0}", archivoXml.getId());

            return "OK";

        } catch (SQLException exc) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } catch (Exception exc) {
            rollbackTransaction();
            if(exc.getMessage().contains("archivoxml_autorizacion_key"))
                return "esa factura ya está cargada en la base de datos";
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    public List<ArchivoXml> listarArchivosXml() throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM ArchivoXml r order by r.fechaAutorizacion");

            List<ArchivoXml> listaArchivoXml = query.getResultList();

            return listaArchivoXml;

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    public List<Object> listarPorFecha(Date fechaInicio, Date fechaFinal, Long idUsuarioCarga, 
            String claveAcceso, String ruc, String tipoDocumento, String estadoSistema,
            Integer desde, Integer hasta, boolean seleccionados) throws Exception {
        try {
            List<Object> respuesta = new ArrayList<>();
            getEntityManager();

            String sql = "FROM ArchivoXml ax "; //WHERE ax.fechaEmision between :fechaInicio AND :fechaFinal";
            
            
            if(Objects.nonNull(claveAcceso) && !claveAcceso.isBlank()){
                sql += " where ax.claveAcceso = :claveAcceso";
            }
            else{
                sql += " where ax.fechaEmision between :fechaInicio AND :fechaFinal";
            }
            
            if(Objects.nonNull(idUsuarioCarga)){
                sql += " AND ax.idUsuarioCarga = :idUsuarioCarga";
            }
            if(Objects.nonNull(tipoDocumento) && !tipoDocumento.isBlank()){
                sql += " AND ax.tipoDocumento = :tipoDocumento";
            }
            if(Objects.nonNull(ruc) && !ruc.isBlank()){
                sql += " AND ax.comprobante like '%"+ruc.toUpperCase()+"%'";
            }
            if(Objects.nonNull(estadoSistema) && !estadoSistema.isBlank()){
                sql += " AND ax.estadoSistema = :estadoSistema";
            }
            
            sql += " order by ax.fechaEmision";
            
            Query query = em.createQuery(sql);
                    
            if(Objects.nonNull(claveAcceso) && !claveAcceso.isBlank()){
                query.setParameter("claveAcceso", claveAcceso);
            }
            else{
                query.setParameter("fechaInicio", fechaInicio);
                query.setParameter("fechaFinal", fechaFinal);
            }
            
            if(Objects.nonNull(idUsuarioCarga)){
                query.setParameter("idUsuarioCarga", idUsuarioCarga);
            }
            if(Objects.nonNull(tipoDocumento) && !tipoDocumento.isBlank()){
                query.setParameter("tipoDocumento", tipoDocumento);
            }
            if(Objects.nonNull(estadoSistema) && !estadoSistema.isBlank()){
                query.setParameter("estadoSistema", estadoSistema);
            }

            //para obtener el total de los registros a buscar
            Integer totalRegistros = query.getResultList().size();
            respuesta.add(totalRegistros);
            
            LOGGER.log(Level.INFO, "total de registeos: {0}", totalRegistros);
            
            //para la paginacion
            query.setFirstResult(desde).setMaxResults(hasta);
            
            List<ArchivoXml> listaPorFecha = query.getResultList();
            respuesta.add(listaPorFecha);
            
            LOGGER.log(Level.INFO, "consultados por rango desde hasta: {0}", listaPorFecha.size());
                        
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<ArchivoXml> criteriaQuery = cb.createQuery(ArchivoXml.class);
//            Root<ArchivoXml> root = criteriaQuery.from(ArchivoXml.class);
//            criteriaQuery.select(root).where(cb.between(root.get("fechaEmision"), fechaInicio, fechaFinal));
//            em.createQuery(criteriaQuery).getResultList().size();

            //aqui si el valor de "hasta" es mayor a mil, se los debe marcar como que fueron exportados.
            //esta parte es asi porque se les llama para exportar a excel, y aqui se les debe marcar
            if(hasta > 1000 && !seleccionados){
                em.getTransaction().begin();
                for(ArchivoXml xml : listaPorFecha) {
                    LOGGER.log(Level.INFO, "se va a actualizar el exportado a true {0}", xml.getId());
                    xml.setExportado(true);
                    em.merge(xml);
                }
                em.getTransaction().commit();
            }

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
    
    
    public ArchivoXml getArchivoXmlPorClaveAcceso(String claveAcceso) throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM ArchivoXml r WHERE r.claveAcceso = :claveAcceso");
            query.setParameter("claveAcceso", claveAcceso);

            List<ArchivoXml> listaArchivoXml = query.getResultList();
            
            if(!listaArchivoXml.isEmpty()){
                return listaArchivoXml.get(0);
            }
            
            return null;

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    
    public void anularArchivosXml(List<ArchivoXml> lista) throws Exception{
        try{
            getEntityManager();
            
            em.getTransaction().begin();
            
            lista.forEach(data -> {
                ArchivoXml xml = em.find(ArchivoXml.class, data.getId());
                
                xml.setEstadoSri("ANULADO");
                xml.setEstadoSistema("ANULADO");
                xml.setRazonAnulacion(data.getRazonAnulacion());
                xml.setFechaAnula(new Date());
                
                Usuario user = em.find(Usuario.class, data.getIdUsuarioCarga());
                
                xml.setUsuarioAnula(user.getUsuario());
                
                em.merge(xml);
            });
            
            em.getTransaction().commit();
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }finally {
            closeEntityManager();
        }
    }
    
    
    
    public void guardarTiposGasto(String[] datos) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();

            Query query = em.createQuery("UPDATE ArchivoXml a SET a.tipoGasto = :tipoGasto WHERE a.id = :id");
                    
            for(String data : datos){
                
                String[] obj = data.split(":");
                query.setParameter("id", Long.parseLong(obj[0]));
                query.setParameter("tipoGasto", obj[1]);
                query.executeUpdate();
            }
            
            em.flush();

            em.getTransaction().commit();
            

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
    
    
    
    
    public List<Object> listarConDetalles(Date fechaInicio, Date fechaFinal, Long idUsuarioCarga, 
            String claveAcceso, String ruc, String tipoDocumento, String estadoSistema,
            Integer desde, Integer hasta, boolean seleccionados) throws Exception {
        try {
            List<Object> respuesta = new ArrayList<>();
            getEntityManager();

            String sql = "select \n" +
"a.id, a.tipodocumento, a.estadosri, a.autorizacion, a.fechaautorizacion, a.fechaemision,\n" +
"a.ambiente, a.comprobante, a.urlarchivo, a.nombrearchivoxml, a.nombrearchivopdf, a.idusuariocarga,\n" +
"a.fechacarga, a.codigojdproveedor, a.claveacceso, a.exportado, a.razonanulacion, a.usuarioanula,\n" +
"a.fechaanula, a.estadosistema, a.tipogasto, a.esfisica,\n" +
"d.detalle, d.preciounitario\n" +
"from archivoxml a\n" +
"left outer join (\n" +
"select id, replace(cast((cast(detalle as json)->'descripcion') as text), '\"', '') detalle, \n" +
"cast((cast(detalle as json)->'precioUnitario') as text) preciounitario\n" +
"from (\n" +
"select id, \n" +
"cast(json_array_elements(cast(comprobante as json)->'factura'->'detalles'->'detalle') as text) detalle\n" +
"from archivoxml\n" +
"where cast(comprobante as json)->'factura'->'detalles'->'detalle'->>'descripcion' is null\n" +
"and tipodocumento = '01'\n" +
"union\n" +
"select id, \n" +
"cast(cast(comprobante as json)->'factura'->'detalles'->'detalle' as text)\n" +
"from archivoxml\n" +
"where cast(comprobante as json)->'factura'->'detalles'->'detalle'->>'descripcion' is not null\n" +
"and tipodocumento = '01'\n" +
") detalles\n" +
") d on a.id = d.id";
            
            
            if(Objects.nonNull(claveAcceso) && !claveAcceso.isBlank()){
                sql += " where a.claveAcceso = ?claveAcceso";
            }
            else{
                sql += " where a.fechaEmision between ?fechaInicio AND ?fechaFinal";
            }
            
            if(Objects.nonNull(idUsuarioCarga)){
                sql += " AND a.idUsuarioCarga = ?idUsuarioCarga";
            }
            if(Objects.nonNull(tipoDocumento) && !tipoDocumento.isBlank()){
                sql += " AND a.tipoDocumento = ?tipoDocumento";
            }
            if(Objects.nonNull(ruc) && !ruc.isBlank()){
                sql += " AND a.comprobante like '%"+ruc.toUpperCase()+"%'";
            }
            if(Objects.nonNull(estadoSistema) && !estadoSistema.isBlank()){
                sql += " AND a.estadoSistema = ?estadoSistema";
            }
            
            sql += " order by a.fechaEmision";
            
            LOGGER.log(Level.INFO, "el sql: {0}", sql);
            
            Query query = em.createNativeQuery(sql);
                    
            if(Objects.nonNull(claveAcceso) && !claveAcceso.isBlank()){
                query.setParameter("claveAcceso", claveAcceso);
            }
            else{
                query.setParameter("fechaInicio", fechaInicio);
                query.setParameter("fechaFinal", fechaFinal);
            }
            
            if(Objects.nonNull(idUsuarioCarga)){
                query.setParameter("idUsuarioCarga", idUsuarioCarga);
            }
            if(Objects.nonNull(tipoDocumento) && !tipoDocumento.isBlank()){
                query.setParameter("tipoDocumento", tipoDocumento);
            }
            if(Objects.nonNull(estadoSistema) && !estadoSistema.isBlank()){
                query.setParameter("estadoSistema", estadoSistema);
            }

            //para obtener el total de los registros a buscar
            Integer totalRegistros = query.getResultList().size();
            respuesta.add(totalRegistros);
            
            LOGGER.log(Level.INFO, "total de registeos: {0}", totalRegistros);
            
            //para la paginacion
            query.setFirstResult(desde).setMaxResults(hasta);
            
            List<Object[]> listaConDets = query.getResultList();
            
            List<ArchivoXmlDTO> listaPorFecha = new ArrayList<>();
            
            listaConDets.forEach(obj -> {
                try{
                    ArchivoXmlDTO xml = new ArchivoXmlDTO();

                    xml.setId((Long)obj[0]);
                    xml.setTipoDocumento(obj[1].toString());
                    xml.setEstadoSri(obj[2].toString());
                    xml.setNumeroAutorizacion(obj[3].toString());
                    xml.setFechaAutorizacion((Date)obj[4]);
                    xml.setFechaEmision((Date)obj[5]);
                    xml.setAmbiente(obj[6].toString());
                    xml.setComprobante(obj[7].toString());
                    xml.setUrlArchivo(obj[8] != null ? obj[8].toString() : null);
                    xml.setNombreArchivoXml(obj[9] != null ? obj[9].toString() : null);
                    xml.setNombreArchivoPdf(obj[10] != null ? obj[10].toString() : null);
                    xml.setIdUsuarioCarga((Long)obj[11]);
                    xml.setFechaCarga((Date)obj[12]);
                    xml.setCodigoJDProveedor(obj[13] != null ? obj[13].toString() : null);
                    xml.setClaveAcceso(obj[14] != null ? obj[14].toString() : null);
                    xml.setExportado(obj[15] != null ? (Boolean)obj[15] : false);
                    xml.setRazonAnulacion(obj[16] != null ? obj[16].toString() : null);
                    xml.setUsuarioAnula(obj[17] != null ? obj[17].toString() : null);
                    xml.setFechaAnula(obj[18] != null ? (Date)obj[18] : null);
                    xml.setEstadoSistema(obj[19] != null ? obj[19].toString() : null);
                    xml.setTipoGasto(obj[20] != null ? obj[20].toString() : null);
                    xml.setEsFisica(obj[21] != null ? (Boolean)obj[21] : false);
                    //
                    xml.setDetalle(obj[22] != null ? obj[22].toString() : null);
                    xml.setPrecioUnitario(obj[23] != null ? obj[23].toString() : null);

                    listaPorFecha.add(xml);
                }catch(Exception exc){
                    LOGGER.log(Level.SEVERE, null, exc);
                }
            });
            
            
            respuesta.add(listaPorFecha);
            
            LOGGER.log(Level.INFO, "consultados por rango desde hasta: {0}", listaPorFecha.size());
                        
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<ArchivoXml> criteriaQuery = cb.createQuery(ArchivoXml.class);
//            Root<ArchivoXml> root = criteriaQuery.from(ArchivoXml.class);
//            criteriaQuery.select(root).where(cb.between(root.get("fechaEmision"), fechaInicio, fechaFinal));
//            em.createQuery(criteriaQuery).getResultList().size();

            //aqui si el valor de "hasta" es mayor a mil, se los debe marcar como que fueron exportados.
            //esta parte es asi porque se les llama para exportar a excel, y aqui se les debe marcar
            if(hasta > 1000 && !seleccionados){
                em.getTransaction().begin();
                for(ArchivoXmlDTO dto : listaPorFecha) {
                    LOGGER.log(Level.INFO, "se va a actualizar el exportado a true {0}", dto.getId());
                    dto.setExportado(true);
                    
                    ArchivoXml archivoXml = ArchivoXmlMapper.INSTANCE.dtoToEntity(dto);
                    
                    em.merge(archivoXml);
                }
                em.getTransaction().commit();
            }

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
    
    
    public void anularPorArchivo(List<ArchivoXml> lista) throws Exception{
        try{
            getEntityManager();
            
            em.getTransaction().begin();

            Query query = em.createQuery("UPDATE ArchivoXml a SET a.estadoSistema = 'ANULADO', "
                    + " a.estadoSri = 'ANULADO', a.razonAnulacion = :razonAnulacion, a.usuarioAnula = :usuarioAnula, "
                    + " a.fechaAnula = :fechaAnula WHERE a.claveAcceso = :claveAcceso ");

            lista.forEach(data -> {
                query.setParameter("razonAnulacion", data.getRazonAnulacion());
                query.setParameter("claveAcceso", data.getClaveAcceso());
                query.setParameter("usuarioAnula", data.getUsuarioAnula());
                query.setParameter("fechaAnula", new Date());
                query.executeUpdate();
            });
            
            em.getTransaction().commit();
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }finally {
            closeEntityManager();
        }
    }
    
    
    
    
    public void guardarAsistentes(String[] datos) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();

            Query query = em.createQuery("UPDATE ArchivoXml a SET a.asistentes = :asistentes WHERE a.id = :id");
                    
            for(String data : datos){
                
                String[] obj = data.split(":");
                if(obj.length == 2 && Objects.nonNull(obj[1]) && !obj[1].isBlank()){
                    query.setParameter("id", Long.parseLong(obj[0]));
                    query.setParameter("asistentes", obj[1].toUpperCase());
                    query.executeUpdate();
                }
            }
            
            em.flush();

            em.getTransaction().commit();
            

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
}
