/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dao;

import com.idebsystems.serviciosweb.dto.ArchivoXmlDTO;
import com.idebsystems.serviciosweb.entities.ArchivoXml;
import com.idebsystems.serviciosweb.entities.Rol;
import com.idebsystems.serviciosweb.entities.Usuario;
import com.idebsystems.serviciosweb.servicio.ArchivoXmlServicio;
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
    
    public List<Object> listarPorFecha(Date fechaInicio, Date fechaFinal, Long idUsuarioCarga, String claveAcceso, 
            String rucNombre, String tipoDocumento, String estadoSistema,Integer desde, Integer hasta, boolean seleccionados, 
            long idReembolso, String exportado, String rucCliente, Long idRolSesion, List<String> clavesSeleccionadas) throws Exception {
        try {
            //aqui se debe quitar los documentos que el usuarioSesion no puede buscar segun su rol
            RolDAO rolDao = new RolDAO();
            Rol rol = rolDao.buscarRolPorId(idRolSesion);
            
            List<Object> respuesta = new ArrayList<>();
            getEntityManager();

            String sql = "SELECT ax FROM ArchivoXml ax ";
            
            
            if(Objects.nonNull(claveAcceso) && !claveAcceso.isBlank()){
                sql += " where ax.claveAcceso = :claveAcceso";
            }
            else if(Objects.nonNull(idReembolso) && idReembolso > 0){
                sql += " where ax.idReembolso = :idReembolso";
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
            else{
                String tx = "(";
                tx += rol.isbFactura() ? "'01','NV','MS'," : "";
                tx += rol.isbGuiaRemision() ? "'06'," : "";
                tx += rol.isbNotaCredito() ? "'04'," : "";
                tx += rol.isbNotaDebito() ? "'05'," : "";
                tx += rol.isbRetencion() ? "'07'," : "" ;
                tx += ")";
                tx = tx.replace(",)", ")");
                
                sql += " AND ax.tipoDocumento IN "+tx+"";
//                System.out.println("tipodoc:: " + sql);
            }
            
            if(Objects.nonNull(rucNombre) && !rucNombre.isBlank()){//este es el ruc o nombre del emisor
                sql += " AND (ax.comprobante like '%ruc\":"+rucNombre.toUpperCase()+"%' "
                        //este OR se hace porque en los miscelaneos y facturas fisicas se guarda ru":"333.... con todo el " al inicio delruc
                        + "OR ax.comprobante like '%ruc\":\""+rucNombre.toUpperCase()+"%' " 
                        + "OR ax.comprobante like '%razonSocial\":\""+rucNombre.toUpperCase()+"%') ";
                
            }
            if(Objects.nonNull(estadoSistema) && !estadoSistema.isBlank()){
                sql += " AND ax.estadoSistema = :estadoSistema";
            }
            if(Objects.nonNull(exportado) && !exportado.isBlank()){
                sql += " AND ax.exportado = :exportado";
            }
            if(Objects.nonNull(rucCliente) && !rucCliente.isBlank()){//a quien emiten, es de las empresas y siempre viene solo el ruc
                rucCliente = rucCliente.startsWith("0") ? "\""+rucCliente : rucCliente;
                
                sql += " AND (ax.comprobante like '%identificacionComprador\":"+rucCliente.toUpperCase()+"%' "//fact nd y nc
                + "OR ax.comprobante like '%identificacionSujetoRetenido\":"+rucCliente.toUpperCase()+"%' "//retencion
                + "OR ax.comprobante like '%identificacionProveedor\":"+rucCliente.toUpperCase()+"%')"; //liquidacion compra
            }
            //para cuando exporta seleccionados
            if(seleccionados){
                sql += " AND ax.claveAcceso IN :clavesAcceso ";
            }
            
            sql += " order by ax.fechaEmision";
            
            Query query = em.createQuery(sql, ArchivoXml.class);
                    
            if(Objects.nonNull(claveAcceso) && !claveAcceso.isBlank()){
                query.setParameter("claveAcceso", claveAcceso);
            }
            else if(Objects.nonNull(idReembolso) && idReembolso > 0){
                query.setParameter("idReembolso", idReembolso);
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
            if(Objects.nonNull(exportado) && !exportado.isBlank()){
                query.setParameter("exportado", Boolean.parseBoolean(exportado));
            }
//            if(Objects.nonNull(rucEmisor) && !rucEmisor.isBlank()){
//                query.setParameter("rucEmisor", rucEmisor);
//            }
            //para cuando exporta seleccionados
            if(seleccionados){
                query.setParameter("clavesAcceso", clavesSeleccionadas);
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
            if(hasta > 1000 /*&& !seleccionados*/) {
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
    
    
    
    
    public List<Object> listarConDetalles(Date fechaInicio, Date fechaFinal, Long idUsuarioCarga, String claveAcceso, 
            String rucNombre, String tipoDocumento, String estadoSistema, Integer desde, Integer hasta, boolean seleccionados, 
            long idReembolso, String exportado, String rucCliente, Long idRolSesion, List<String> clavesSeleccionadas) throws Exception {
        try {
            //aqui se debe quitar los documentos que el usuarioSesion no puede buscar segun su rol
            RolDAO rolDao = new RolDAO();
            Rol rol = rolDao.buscarRolPorId(idRolSesion);
            
            List<Object> respuesta = new ArrayList<>();
            getEntityManager();

            String sql = " select * from v_detalles_documentos a";
            
            
            if(Objects.nonNull(claveAcceso) && !claveAcceso.isBlank()){
                sql += " where a.claveAcceso = ?claveAcceso";
            }
            else if(Objects.nonNull(idReembolso) && idReembolso > 0){
                sql += " where a.idReembolso = ?idReembolso";
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
            else{
                String tx = "(";
                tx += rol.isbFactura() ? "'01','NV','MS'," : "";
                tx += rol.isbGuiaRemision() ? "'06'," : "";
                tx += rol.isbNotaCredito() ? "'04'," : "";
                tx += rol.isbNotaDebito() ? "'05'," : "";
                tx += rol.isbRetencion() ? "'07'," : "" ;
                tx += ")";
                tx = tx.replace(",)", ")");
                
                sql += " AND a.tipoDocumento IN "+tx+"";
//                System.out.println("tipodoc:: " + sql);
            }
            
            if(Objects.nonNull(rucNombre) && !rucNombre.isBlank()){//este es el ruc o nombre del emisor
                sql += " AND (a.comprobante like '%ruc\":"+rucNombre.toUpperCase()+"%' "
                        //este OR se hace porque en los miscelaneos y facturas fisicas se guarda ru":"333.... con todo el " al inicio delruc
                        + "OR ax.comprobante like '%ruc\":\""+rucNombre.toUpperCase()+"%' " 
                        + "OR a.comprobante like '%razonSocial\":\""+rucNombre.toUpperCase()+"%') ";
                
            }
            if(Objects.nonNull(estadoSistema) && !estadoSistema.isBlank()){
                sql += " AND a.estadoSistema = ?estadoSistema";
            }
            if(Objects.nonNull(exportado) && !exportado.isBlank()){
                sql += " AND a.exportado = ?exportado";
            }
            if(Objects.nonNull(rucCliente) && !rucCliente.isBlank()){
                rucCliente = rucCliente.startsWith("0") ? "\""+rucCliente : rucCliente;
                
                sql += " AND (a.comprobante like '%identificacionComprador\":"+rucCliente.toUpperCase()+"%' "//fact nd y nc
                + "OR a.comprobante like '%identificacionSujetoRetenido\":"+rucCliente.toUpperCase()+"%' "//retencion
                + "OR a.comprobante like '%identificacionProveedor\":"+rucCliente.toUpperCase()+"%')"; //liquidacion compra
            }
            //para cuando exporta seleccionados
            if(seleccionados){
                final StringBuilder aux = new StringBuilder("(");
                clavesSeleccionadas.forEach(ca -> {aux.append("'").append(ca).append("',");});
                aux.append(")");
                sql += " AND a.claveAcceso IN "+aux.toString().replace(",)", ")");
            }
            
            sql += " order by a.fechaEmision";
            
//            LOGGER.log(Level.INFO, "el sql: {0}", sql);
            
            Query query = em.createNativeQuery(sql);
                    
            if(Objects.nonNull(claveAcceso) && !claveAcceso.isBlank()){
                query.setParameter("claveAcceso", claveAcceso);
            }
            else if(Objects.nonNull(idReembolso) && idReembolso > 0){
                query.setParameter("idReembolso", idReembolso);
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
            if(Objects.nonNull(exportado) && !exportado.isBlank()){
                query.setParameter("exportado", Boolean.parseBoolean(exportado));
            }
//            if(Objects.nonNull(rucEmisor) && !rucEmisor.isBlank()){
//                query.setParameter("rucEmisor", rucEmisor);
//            }
            //para cuando exporta seleccionados
//            if(seleccionados){
//                query.setParameter("clavesAcceso", clavesSeleccionadas);
//            }
            

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
                    //para las retenciones
                    xml.setCodDocSustento(obj[24] != null ? obj[24].toString() : null);
                    xml.setNumDocSustento(obj[25] != null ? obj[25].toString() : null);
                    xml.setFechaEmisionDocSustento(obj[26] != null ? obj[26].toString() : null);
                    xml.setCodigoRetencion(obj[27] != null ? obj[27].toString() : null);
                    xml.setBaseImponible(obj[28] != null ? obj[28].toString() : null);
                    xml.setPorcentajeRetener(obj[29] != null ? obj[29].toString() : null);
                    xml.setValorRetenido(obj[30] != null ? obj[30].toString() : null);
                    xml.setIdReembolso(obj[31] != null ? (Long)obj[31] : null);
                    xml.setRucEmisor(obj[32] != null ? obj[32].toString() : null);
                    xml.setNumeroReembolso(obj[33] != null ? obj[33].toString() : null);
                    xml.setTipoReembolso(obj[34] != null ? obj[34].toString() : null);

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
            if(hasta > 1000 /*&& !seleccionados*/){
                em.getTransaction().begin();
                for(ArchivoXmlDTO dto : listaPorFecha) {
                    LOGGER.log(Level.INFO, "se va a actualizar el exportado a true {0}", dto.getId());
                    
                    ArchivoXml archivoXml = em.find(ArchivoXml.class, dto.getId());
                    archivoXml.setExportado(true);
                    
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
    
    public ArchivoXml eliminarXmlCargado(Long idXml) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();

            ArchivoXml ent = em.find(ArchivoXml.class, idXml);
            
            em.remove(ent);

            em.getTransaction().commit();
            
            return ent;

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
    
    private String formarSqlRucEmpresa(String rucEmpresa){
        try{
            
            return " AND ( "
+" ( "
+" cast(comprobante as json) -> '"+ArchivoXmlServicio.TAG_FACTURA+"' is not null "
+" and cast (comprobante as json) -> '"+ArchivoXmlServicio.TAG_FACTURA+"'->'infoTributaria'->>'ruc' = '"+rucEmpresa+"' "
+" ) "
+" or "
+" ( "
+" cast(comprobante as json) -> '"+ArchivoXmlServicio.TAG_RETENCION+"' is not null "
+" and cast (comprobante as json) -> '"+ArchivoXmlServicio.TAG_RETENCION+"'->'infoTributaria'->>'ruc' = '"+rucEmpresa+"' "
+" ) "
+" or "
+" ( "
+" cast(comprobante as json) -> '"+ArchivoXmlServicio.TAG_NOTACREDITO+"' is not null "
+" and cast (comprobante as json) -> '"+ArchivoXmlServicio.TAG_NOTACREDITO+"'->'infoTributaria'->>'ruc' = '"+rucEmpresa+"' "
+" ) "
+" or "
+" ( "
+" cast(comprobante as json) -> '"+ArchivoXmlServicio.TAG_NOTADEBITO+"' is not null "
+" and cast (comprobante as json) -> '"+ArchivoXmlServicio.TAG_NOTADEBITO+"'->'infoTributaria'->>'ruc' = '"+rucEmpresa+"' "
+" ) "
+" or "
+" ( "
+" cast(comprobante as json) -> '"+ArchivoXmlServicio.TAG_GUIAREMISION+"' is not null "
+" and cast (comprobante as json) -> '"+ArchivoXmlServicio.TAG_GUIAREMISION+"'->'infoTributaria'->>'ruc' = '"+rucEmpresa+"' "
+" ) "
+" ) ";
            
        }
        catch(Exception exc){
            LOGGER.log(Level.SEVERE, null, exc);
            return "";
        }
    }
}
