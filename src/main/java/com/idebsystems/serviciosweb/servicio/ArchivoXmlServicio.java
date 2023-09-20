/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import com.google.gson.Gson;
import com.idebsystems.serviciosweb.dao.ArchivoXmlDAO;
import com.idebsystems.serviciosweb.dao.ParametroDAO;
import com.idebsystems.serviciosweb.dao.UsuarioDAO;
import com.idebsystems.serviciosweb.dto.AnularArchivoXmlDTO;
import com.idebsystems.serviciosweb.dto.ArchivoSriDTO;
import com.idebsystems.serviciosweb.dto.ArchivoXmlDTO;
import com.idebsystems.serviciosweb.dto.ProveedorDTO;
import com.idebsystems.serviciosweb.dto.ReporteDTO;
import com.idebsystems.serviciosweb.entities.ArchivoXml;
import com.idebsystems.serviciosweb.entities.Parametro;
import com.idebsystems.serviciosweb.entities.Usuario;
import com.idebsystems.serviciosweb.mappers.ArchivoXmlMapper;
import com.idebsystems.serviciosweb.util.FechaUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jorge
 */
public class ArchivoXmlServicio {

    private static final Logger LOGGER = Logger.getLogger(ArchivoXmlServicio.class.getName());

    public final static int INDENTATION = 4;
    public final static String PATH_FILE = "";
    public final static String TAG_RESPUESTA_AUTORIZACION = "autorizacion";
    public final static String TAG_COMPROBANTE = "comprobante";
    public final static String TAG_FACTURA = "factura";
    public final static String TAG_SIGNATURE = "ds:Signature";
    public final static String TAG_INFO_TRIBUTARIO = "infoTributaria";
    public final static String TAG_INFO_FACTURA = "infoFactura";
    public final static String TAG_RUC = "ruc";
    public final static String TAG_RAZON_SOCIAL = "razonSocial";
    public final static String TAG_CLAVE_ACCESO = "claveAcceso";
    public final static String TAG_FECHA_AUTORIZACON = "fechaAutorizacion";
    public final static String TAG_ESTADO_SRI = "estado";
    
    public final static String TAG_RETENCION = "comprobanteRetencion";
    public final static String TAG_NOTACREDITO = "notaCredito";
    public final static String TAG_NOTADEBITO = "notaDebito";
    public final static String TAG_GUIAREMISION = "guiaRemision";

    private final ArchivoXmlDAO dao = new ArchivoXmlDAO();
    private final ProveedorServicio provSrv = new ProveedorServicio();

    private String pathCarpetas;
    private ArchivoXml archivoXmlAux;
    private String version;

    public String getPathCarpetas() {
        return pathCarpetas;
    }

    public void setPathCarpetas(String pathCarpetas) {
        this.pathCarpetas = pathCarpetas;
    }

    public ArchivoXml getArchivoXmlAux() {
        return archivoXmlAux;
    }

    public void setArchivoXmlAux(ArchivoXml archivoXmlAux) {
        this.archivoXmlAux = archivoXmlAux;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    
    

    public String guardarArchivoXml(String fileB64) throws Exception {
        try {
            Decoder decoder = Base64.getDecoder();
            byte[] fileBytes = decoder.decode(fileB64);

            try (FileOutputStream fos = new FileOutputStream(PATH_FILE)) {
                fos.write(fileBytes);
            }

            return PATH_FILE;

        } catch (FileNotFoundException exc) {
            throw new Exception(exc);
        } catch (Exception exc) {
            throw new Exception(exc);
        }
    }

    public String guardarXmlToDB(String xmlB64, String nombreXml, String nombrePdf, /*String urlArchivo,*/ Long idUsuario, 
            String tipoDocumento, boolean enviarCorreo, Parametro paramCarpeta, Usuario usersesion, Long idRechazado, boolean esUnicoArchivo) throws Exception {
        try {
            //generar el tag xml segun el documento
            String tag_xml = TAG_FACTURA;
            String tagInfoDoc = TAG_INFO_FACTURA;
            switch (tipoDocumento) {
                case "01": {
                    tag_xml = TAG_FACTURA;
                    tagInfoDoc = TAG_INFO_FACTURA;
                    break;
                }
                case "04": {
                    tag_xml = TAG_NOTACREDITO;
                    tagInfoDoc = "infoNotaCredito";
                    break;
                }
                case "05": {
                    tag_xml = TAG_NOTACREDITO;
                    tagInfoDoc = "infoNotaDebito";
                    break;
                }
                case "06": {
                    tag_xml = TAG_GUIAREMISION;
                    tagInfoDoc = "infoGuidaRemision";
                    break;
                }
                case "07": {
                    tag_xml = TAG_RETENCION;
                    tagInfoDoc = "infoCompRetencion";
                    break;
                }
                default: {
                    tag_xml = TAG_FACTURA;
                    tagInfoDoc = TAG_INFO_FACTURA;
                }
            }
            
            if(Objects.isNull(usersesion)){
                UsuarioDAO usdao = new UsuarioDAO();
                usersesion = usdao.buscarUsuarioPorId(idUsuario);
            }
            
            //la urlArchivo tomar desde la bdd de los parametros.
            ParametroDAO paramDao = new ParametroDAO();
            List<Parametro> listaParams = paramDao.listarParametros();
            //IP con la url del sistema
            Parametro paramUrlSist = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("URL_SISTEMA")).findAny().get();

            Decoder decoder = Base64.getDecoder();
            byte[] fileBytes = decoder.decode(xmlB64);
            InputStream targetStream = new ByteArrayInputStream(fileBytes);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document documentXml = db.parse(targetStream);
            documentXml.getDocumentElement().normalize();

            Node node = buscarTagAutorizacion(documentXml.getFirstChild());

            if (Objects.isNull(node)) {
                node = documentXml.getFirstChild();
            }
            

            StringWriter writer = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(new DOMSource(node), new StreamResult(writer));

            String xmlString = writer.getBuffer().toString();

            JSONObject jsonObj = XML.toJSONObject(xmlString);

            String tag = jsonObj.keys().next();
            if (tag.toLowerCase().contains(TAG_RESPUESTA_AUTORIZACION)) {
//            if (jsonObj.toString().equalsIgnoreCase(TAG_RESPUESTA_AUTORIZACION)) {

                jsonObj = jsonObj.getJSONObject(tag);

                JSONObject jsonObjComp;

                try {
                    String xmlComp = jsonObj.getString(TAG_COMPROBANTE);
                    jsonObjComp = XML.toJSONObject(xmlComp);
                } catch (org.json.JSONException exc) {
                    jsonObjComp = jsonObj.getJSONObject(TAG_COMPROBANTE);
                }
                jsonObjComp.getJSONObject(tag_xml).remove(TAG_SIGNATURE);
                jsonObj.put(TAG_COMPROBANTE, jsonObjComp.toString());

                //sacar a parte la fecha de autorizacion para parsear en el formato dd/MM/yyyy
                String fechaAutoriza = "";
//                LOGGER.log(Level.INFO, "fechaUat:: {0}", jsonObj.has(TAG_FECHA_AUTORIZACON));

                try {
                    //LOGGER.log(Level.INFO, "fechaUat:: {0}", jsonObj.getJSONObject("fechaAutorizacion"));
                    if (jsonObj.getJSONObject(TAG_FECHA_AUTORIZACON) != null) {
//                        LOGGER.log(Level.INFO, "si tiene fechaautorizacion");
                        String fa = jsonObj.getJSONObject(TAG_FECHA_AUTORIZACON).getString("content");

                        fechaAutoriza = fa.substring(0, 10);

//                        LOGGER.log(Level.INFO, "fecha de actoriz: {0}", fechaAutoriza);
                    } else {
                        fechaAutoriza = jsonObj.getString(TAG_FECHA_AUTORIZACON);
                    }

                } catch (org.json.JSONException exc) {
//                    LOGGER.log(Level.INFO, "cayoo: {0}", exc.getMessage());
                    fechaAutoriza = jsonObj.getString(TAG_FECHA_AUTORIZACON);
                }

                //quitar la fecha de autorizacion
                jsonObj.remove(TAG_FECHA_AUTORIZACON);

                String json = jsonObj.toString();
//                LOGGER.log(Level.INFO, "el comprbante xml a json?::: {0}", json);

                ArchivoXmlDTO data = new Gson().fromJson(json, ArchivoXmlDTO.class);
//                LOGGER.log(Level.INFO, "ArchivoXmlDTO::: {0}", data);
                
                //poner el estado, en el json dice estado, pero el entity es estadoSri
                String estadoSri = jsonObj.getString(TAG_ESTADO_SRI);
                data.setEstadoSri(estadoSri);

                //buscar el proveedor con el ruc del xml
                String rucProveedor = jsonObjComp.getJSONObject(tag_xml).getJSONObject(TAG_INFO_TRIBUTARIO).get(TAG_RUC).toString();
                ProveedorDTO dto = provSrv.buscarProveedorRuc(rucProveedor);
//                LOGGER.log(Level.INFO, "id del provv: {0}", dto.getId());
                data.setCodigoJDProveedor(dto.getCodigoJD());

                //obtener la fecha de emision, es importante para las busquedas
                String fechaEmision = jsonObjComp.getJSONObject(tag_xml).getJSONObject(tagInfoDoc).get("fechaEmision").toString();
                //04/09/2022
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                String razonSocial = jsonObjComp.getJSONObject(tag_xml).getJSONObject(TAG_INFO_TRIBUTARIO).get(TAG_RAZON_SOCIAL).toString();

                data.setRazonSocial(razonSocial);
                data.setNombreArchivoPdf(nombrePdf);
                data.setNombreArchivoXml(nombreXml);
//                data.setUrlArchivo(urlArchivo); primero se debe crear la estructura de las carpetas para guardar esto
                data.setTipoDocumento(tipoDocumento);
                data.setFechaEmision(sdf.parse(fechaEmision));

                //colocar la fecha de autorizacion dependiendo del formato
                if(fechaAutoriza.contains("T"))
                        fechaAutoriza = fechaAutoriza.replace("T", " ");
                
                try {
                    sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    data.setFechaAutorizacion(sdf.parse(fechaAutoriza));
                } catch (ParseException exc) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    data.setFechaAutorizacion(sdf.parse(fechaAutoriza));
                }
                
//                LOGGER.log(Level.INFO, "fecha de auto completa: {0}", data.getFechaAutorizacion());

                //aqui crear la estructura de las carpetas.
                String pathCarpetas = crearEstructuraCarpetas(data, paramCarpeta);
                data.setUrlArchivo(paramUrlSist.getValor() + pathCarpetas);
                setPathCarpetas(pathCarpetas);

                data.setEstadoSistema("CARGADO");
                //aqui si el rol es del contador y auxiliar contador, se debe poner directo a APROBADO
                if(usersesion.getIdRol() == 4L || usersesion.getIdRol() == 5L){
                    data.setEstadoSistema("APROBADO");
                }
                //si se carga una retencion el estado debe pasar directo a aprobado
                if(data.getTipoDocumento().equalsIgnoreCase("07")){
                    data.setEstadoSistema("APROBADO");
                }
                
                //obtener la version del comprobante xml
                setVersion(jsonObjComp.getJSONObject(tag_xml).get("version").toString());
                data.setVersion(getVersion());
                
                ArchivoXml archivoXml = convertToEntity(data, idUsuario);

                String claveAcceso = jsonObjComp.getJSONObject(tag_xml).getJSONObject(TAG_INFO_TRIBUTARIO).get(TAG_CLAVE_ACCESO).toString();
                archivoXml.setClaveAcceso(claveAcceso);

                //aqui ver si es un rechazado el xml, si es rechazado se actualiza con el mismo id
                if(Objects.nonNull(idRechazado))
                    archivoXml.setId(idRechazado);
                
                //buscar el xml que se esta cargando a ver si existe en la bdd, 
                //si ya existe se debe ver el estado_sistema para que no deje guardar nuevamente
                //buscar si ya existe la clave de acceso en la bdd, ya no se debe hacer nada.
                if(esUnicoArchivo){
                    ArchivoXml aux = dao.getArchivoXmlPorClaveAcceso(archivoXml.getClaveAcceso());

                    if (Objects.nonNull(aux) && (archivoXml.getEstadoSistema().equalsIgnoreCase("RECHAZADO")
                            || archivoXml.getEstadoSistema().equalsIgnoreCase("ANULADO")) ) {
                        archivoXml.setId(aux.getId());
                    }
                }
                
                String respuesta = dao.guardarDatosArchivo(archivoXml);
                
                setArchivoXmlAux(archivoXml);
                
//                LOGGER.log(Level.INFO, "Tiene un id?? {0}", archivoXml.getId());
//                setIdTmp(archivoXml.getId());

                //despues de que se guardo el archivo en bdd enviar el correo para notificar la carga
                if (respuesta.equalsIgnoreCase("Ok") && enviarCorreo) {
                    CorreoServicio correoSrv = new CorreoServicio();
                    correoSrv.enviarCorreoCargaArchivo(idUsuario, archivoXml);
                }

                return respuesta;// + "~" + pathCarpetas;

            } else {
                //el archivo no tiene los datos completos, solo es el xml del comprobante, y sin la autorizacion
                LOGGER.log(Level.INFO, "Error, Mal estructura del archivo xml. No tiene la etiqueta: <autorizacion>");

//                if (jsonObj.isNull(TAG_FACTURA)) {
//                    System.out.println("no tienenenenenenne");
//                    return "El archivo xml no tiene la etiqueta factura.";
//                }

                jsonObj.getJSONObject(tag_xml).remove(TAG_SIGNATURE);

                JSONObject objjson = new JSONObject();
                objjson.put(TAG_COMPROBANTE, jsonObj.toString());

                String json = objjson.toString();
//                LOGGER.log(Level.INFO, "el comprbante xml a json?::: {0}", json);

                ArchivoXmlDTO data = new Gson().fromJson(json, ArchivoXmlDTO.class);
//                LOGGER.log(Level.INFO, "ArchivoXmlDTO::: {0}", data);

//                LOGGER.log(Level.INFO, "el comprobante::: {0}", data.getComprobante());

                return "Error, Mal estructura del archivo xml. No tiene la etiqueta: <autorizacion>.";
            }

            //return "OK"; //Archivo guardado en la base de datos";
        } catch (TransformerConfigurationException exc) {
            throw new Exception(exc);
        } catch (TransformerException exc) {
            throw new Exception(exc);
        }
    }

    public List<ArchivoXmlDTO> listarArchivosXml() throws Exception {
        try {
            List<ArchivoXmlDTO> listaArchivoXmlDto = new ArrayList();

            List<ArchivoXml> listaArchivoXml = dao.listarArchivosXml();

            listaArchivoXml.forEach(archivoXml -> {
                ArchivoXmlDTO archivoXmlDto = new ArchivoXmlDTO();
                archivoXmlDto = ArchivoXmlMapper.INSTANCE.entityToDto(archivoXml);
                listaArchivoXmlDto.add(archivoXmlDto);
            });

            return listaArchivoXmlDto;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }

    private ArchivoXml convertToEntity(ArchivoXmlDTO dto, Long idUsuario) {
        ArchivoXml archivoXml = new ArchivoXml();
        archivoXml.setAmbiente(dto.getAmbiente());
        archivoXml.setComprobante(dto.getComprobante());
        archivoXml.setEstadoSri(dto.getEstadoSri());
        archivoXml.setFechaAutorizacion(dto.getFechaAutorizacion());
        archivoXml.setId(null);
        archivoXml.setNumeroAutorizacion(dto.getNumeroAutorizacion());
        archivoXml.setIdUsuarioCarga(idUsuario);
        archivoXml.setFechaCarga(new Date());
        //
        archivoXml.setUrlArchivo(dto.getUrlArchivo());
        archivoXml.setNombreArchivoPdf(dto.getNombreArchivoPdf());
        archivoXml.setNombreArchivoXml(dto.getNombreArchivoXml());
        archivoXml.setCodigoJDProveedor(dto.getCodigoJDProveedor());
        archivoXml.setTipoDocumento(dto.getTipoDocumento());
        archivoXml.setFechaEmision(dto.getFechaEmision());
        archivoXml.setEstadoSistema(dto.getEstadoSistema());
        archivoXml.setVersion(dto.getVersion());
        
        return archivoXml;
    }

    public List<ArchivoXmlDTO> listarPorFecha(Date fechaInicio, Date fechaFinal, Long idUsuarioCarga, 
            String claveAcceso, String ruc, String tipoDocumento, String estadoSistema,
            Integer desde, Integer hasta, boolean seleccionados, boolean conDetalles) throws Exception {
        try {

            final List<ArchivoXmlDTO> listaArchivoXmlDto = new ArrayList();

            List<Object> respuesta;
            
            if(conDetalles){
                respuesta = dao.listarConDetalles(FechaUtil.fechaInicial(fechaInicio), FechaUtil.fechaFinal(fechaFinal), idUsuarioCarga, 
                    claveAcceso, ruc, tipoDocumento, estadoSistema, desde, hasta, seleccionados);
            }
            else{
                respuesta = dao.listarPorFecha(FechaUtil.fechaInicial(fechaInicio), FechaUtil.fechaFinal(fechaFinal), idUsuarioCarga, 
                    claveAcceso, ruc, tipoDocumento, estadoSistema, desde, hasta, seleccionados);
            }

            //sacar los resultados retornados
            Integer totalRegistros = (Integer) respuesta.get(0);
            
            //buscar los usuarios
            UsuarioDAO userDao = new UsuarioDAO();
            List<Usuario> listaUser = userDao.listarUsuarios();
            
            if(conDetalles){
                List<ArchivoXmlDTO> listaDtos = (List<ArchivoXmlDTO>) respuesta.get(1);
                listaDtos.forEach(archivoXmlDto -> {
                    
                    //en base a la lista de usuarios colocar el nombre de usuairo que cargo el archivo
                    Usuario user = listaUser.stream().filter(u -> u.getId() == archivoXmlDto.getIdUsuarioCarga()).findAny().get();
                    archivoXmlDto.setNombreUsuario(user.getNombre());
                    archivoXmlDto.setTotalRegistros(totalRegistros);

                    archivoXmlDto.setTipoDocumentoTexto(crearTipoDocumentoTexto(archivoXmlDto.getTipoDocumento()));
                   
                    listaArchivoXmlDto.add(archivoXmlDto);
                });
            }
            else{
                List<ArchivoXml> listaArchivoXml = (List<ArchivoXml>) respuesta.get(1);
                listaArchivoXml.forEach(archivoXml -> {
                    ArchivoXmlDTO archivoXmlDto = new ArchivoXmlDTO();
                    archivoXmlDto = ArchivoXmlMapper.INSTANCE.entityToDto(archivoXml);

                    //en base a la lista de usuarios colocar el nombre de usuairo que cargo el archivo
                    Usuario user = listaUser.stream().filter(u -> u.getId() == archivoXml.getIdUsuarioCarga()).findAny().get();
                    archivoXmlDto.setNombreUsuario(user.getNombre());
                    archivoXmlDto.setTotalRegistros(totalRegistros);

                    archivoXmlDto.setTipoDocumentoTexto(crearTipoDocumentoTexto(archivoXmlDto.getTipoDocumento()));

                    listaArchivoXmlDto.add(archivoXmlDto);
                });
            }

            return listaArchivoXmlDto;
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }

    private Node buscarTagAutorizacion(Node documentXml) {
        Node nodeAut = null;

        if (documentXml.hasChildNodes()) {
            for (int i = 0; i < documentXml.getChildNodes().getLength(); i++) {
                Node child = documentXml.getChildNodes().item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) child;
                    if (element.getTagName().equalsIgnoreCase(TAG_RESPUESTA_AUTORIZACION)) {
                        return child;
                    } else {
                        nodeAut = buscarTagAutorizacion(child);
                    }
                }
            }
        }
        return nodeAut;
    }

    public List<ArchivoSriDTO> cargarArchivoSri(List<ArchivoSriDTO> lista, Long idUsuario) throws Exception {
        try {
            UsuarioDAO usdao = new UsuarioDAO();
            Usuario usersesion = usdao.buscarUsuarioPorId(idUsuario);
            
            //la urlArchivo tomar desde la bdd de los parametros.
            ParametroDAO paramDao = new ParametroDAO();
            List<Parametro> listaParams = paramDao.listarParametros();
            //DESDE esta ubicacion crear la estructura de la carpeta
            Parametro paramCarpeta = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("CARPETA_ARCHIVOS")).findAny().get();
            
            //este es el path para el tomcat para giuaradar ;los archivos desde java al path del php
            //Parametro paramPathTomcat = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("PATH_TOMCAT")).findAny().get();
            
            //la urldel sri desde la bdd
            Parametro paramUrl = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("URLAUTORIZACIONSRI")).findAny().get();
            String urlSri = paramUrl.getValor();

            ConexionSriServicio sri = new ConexionSriServicio();
            boolean correcto = false;

            for (ArchivoSriDTO arc : lista) {

                //buscar si ya existe la clave de acceso en la bdd, ya no se debe hacer nada.
                ArchivoXml archivoXml = dao.getArchivoXmlPorClaveAcceso(arc.getClaveAcceso());

                if (Objects.isNull(archivoXml) || archivoXml.getEstadoSistema().equalsIgnoreCase("RECHAZADO")
                        || archivoXml.getEstadoSistema().equalsIgnoreCase("ANULADO") ) {

                    String respXml=null;
                    try {
                        respXml = sri.getAutorizacionComprobante(arc.getClaveAcceso(), urlSri);
                    } catch (Exception exc) {
                        respXml = null;
                        if(exc.getMessage() != null && exc.getMessage().contains("Connection reset")){
                            arc.setRespuesta("SIN CONEXION AL SRI");
                        }if(exc.getMessage() != null && exc.getMessage().contains("Rejected")){
                            arc.setRespuesta("SIN CONEXION AL SRI");
                        }else{
                            arc.setRespuesta("ERROR: " + exc.getMessage().replace("java.lang.Exception:", ""));
                        }
                    }

                    if (Objects.nonNull(respXml)) {
                        //aqui generar el pdf y el xml
                        Base64.Encoder encoder = Base64.getEncoder();
                        String fileb64 = encoder.encodeToString(respXml.getBytes());

                        arc.setFileBase64(fileb64);

                        String tipoDoc = arc.getClaveAcceso().substring(8, 10);

//                        LOGGER.log(Level.INFO, "tipodoc desde clave acceso: {0}", tipoDoc);

                        try {
                            String resp = guardarXmlToDB(fileb64, (arc.getClaveAcceso() + ".xml"), (arc.getClaveAcceso() + ".pdf"), 
                                    idUsuario, tipoDoc, false, paramCarpeta, usersesion, (archivoXml == null ? null : archivoXml.getId()), Boolean.FALSE );
//                            if(resp.contains("~"))
//                                arc.setRespuesta(resp.split("~")[0]);
//                            else
                            
                            ReporteServicio repoSrv = new ReporteServicio();
                            ReporteDTO reporteDTO = repoSrv.generarRidePdf(getArchivoXmlAux(), getVersion());
                            arc.setRideBase64(reporteDTO.getReporteBase64());

                            LOGGER.log(Level.INFO, "el nuevo ud:: {0}", getArchivoXmlAux().getId());
                            arc.setId(getArchivoXmlAux().getId());
                            arc.setRespuesta(resp);
                            LOGGER.log(Level.INFO, "el ppath despues de guararen bdd: {0}", getPathCarpetas());
                            arc.setPathArchivos(getPathCarpetas());
                            
                            //si es un rol contador se pone en APROBADO el estado
                            if(usersesion.getIdRol() == 4L){
                                arc.setEstadoSistema("APROBADO");
                            }
                            else{
                                arc.setEstadoSistema("CARGADO");
                            }
                            /*
                            //en esta parte se procede a escribir en disco el xml y el ride para no enviar los archivos en la respuesta
                            guardarArchivosDisco(arc, paramPathTomcat.getValor());
                            //se les pone en null para que no viajen en el response
                            arc.setFileBase64(null);
                            arc.setRideBase64(null);
                            //necesario quitar mas cosas, revisar porque toavia salio bkoen pipe
                            arc.setPathArchivos(null);
                            //*/
                            
                            
                            //si almenos uno es correcto se envia el correo
                            correcto = true;
                        } catch (Exception exc) {
                            LOGGER.log(Level.SEVERE, null, exc);
                            arc.setRespuesta("ERROR: " + exc.getMessage().replace("java.lang.Exception:", ""));
                        }
                    }
                } else {
//                    if(archivoXml.getEstadoSistema().equalsIgnoreCase("RECHAZADO")){
//                        arc.setId(archivoXml.getId());
//                        arc.setEstadoSistema("CARGADO");
//                        arc.setRespuesta("OK");
//                        arc.setPathArchivos(archivoXml.getUrlArchivo());
//                        correcto = true;
//                    }else{
                        //ya existe en la base de datos esa clave de acceso
                        arc.setId(archivoXml.getId());
                        arc.setEstadoSistema(archivoXml.getEstadoSistema());
                        arc.setRespuesta("La clave de acceso " + arc.getClaveAcceso() + " ya existe en la base de datos");
//                    }
                }
            }

            
            //despues de que se guardo el archivo en bdd enviar el correo para notificar la carga
            if(correcto){
//                CorreoServicio correoSrv = new CorreoServicio();
//                correoSrv.enviarCorreoCargaArchivo(idUsuario, null);
            }
            
            
            
            return lista;

        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }

    private String crearEstructuraCarpetas(ArchivoXmlDTO data, Parametro paramCarpeta) throws Exception {
        try {
//    anio
//         mes
//            tipoDocumento
//                    razonSocial (proveedor)

            if(Objects.isNull(paramCarpeta)){
                //la urlArchivo tomar desde la bdd de los parametros.
                ParametroDAO paramDao = new ParametroDAO();
                List<Parametro> listaParams = paramDao.listarParametros();
                //DESDE esta ubicacion crear la estructura de la carpeta
                paramCarpeta = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("CARPETA_ARCHIVOS")).findAny().get();
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(data.getFechaEmision());//de aqui anio y mes
            int year = cal.get(Calendar.YEAR);
            int monthInt = (cal.get(Calendar.MONTH) + 1);//el primer mes es cero por eso se suma + 1
            String month = monthInt+"";
            if(monthInt<10){
                month = "0"+month;
            }

//        String month = claveAcceso.substring(2, 3);
//        String year = claveAcceso.substring(4, 7);
//        String tipoDocInt = claveAcceso.substring(8, 9);
            String tipoDocumento = crearTipoDocumentoTexto(data.getTipoDocumento());


            String pathCompleto = paramCarpeta.getValor().replaceAll("/", "") + File.separator + year + File.separator + month + File.separator + tipoDocumento + File.separator + data.getRazonSocial();

//            LOGGER.log(Level.INFO, "path completo: {0}", pathCompleto);

            return pathCompleto;

        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }

    }

//    private String guardarArchivosDisco(String contenidoXmlFile, String pathCompleto) {
//        try {
//
//        } catch (Exception exc) {
//
//        }
//        return "Ok";
//    }
    
    
    public String anularArchivosXml(List<ArchivoXmlDTO> lista) throws Exception{
        try{
            List<ArchivoXml> data = new ArrayList<>();
            lista.forEach(dto->{
                ArchivoXml xml = ArchivoXmlMapper.INSTANCE.dtoToEntity(dto);
                data.add(xml);
            });
            
            dao.anularArchivosXml(data);
            
            return "OK";
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    private String crearTipoDocumentoTexto(String tipoDocumentoSri){
        String tipoDocumento;

            switch (tipoDocumentoSri) {
                case "01": {
                    tipoDocumento = "FACTURA";
                    break;
                }
                case "04": {
                    tipoDocumento = "NOTA_CREDITO";
                    break;
                }
                case "05": {
                    tipoDocumento = "NOTA_DEBITO";
                    break;
                }
                case "06": {
                    tipoDocumento = "GUIA_REMISION";
                    break;
                }
                case "07": {
                    tipoDocumento = "RETENCION";
                    break;
                }
                case "NV": {
                    tipoDocumento = "NOTA_VENTA";
                    break;
                }
                default: {
                    tipoDocumento = tipoDocumentoSri;
                }
            }
            
            return tipoDocumento;
    }
    
    public String anularXmlPorArchivo(AnularArchivoXmlDTO dto) throws Exception{
        try{
            byte[] archivoCsv = Base64.getDecoder().decode(dto.getArchivoAnularB64());
            
            File outputFile = Files.createTempFile("anulacsv", ".csv").toFile();
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                outputStream.write(archivoCsv);
            }
            
            List<ArchivoXml> data = new ArrayList<>();
            
            try (Scanner myReader = new Scanner(outputFile)) {
                while (myReader.hasNextLine()) {
                    String linea = myReader.nextLine();
                    LOGGER.log(Level.INFO, linea);
                    if(Objects.nonNull(linea) && !linea.isBlank()){
                        linea = linea.trim().replace("\r\n", "");

                        if(linea.split(";").length != 2){
                            LOGGER.log(Level.INFO, "No tiene dos columnas");
                            return "Todas las líneas deben tener dos columnas. 1=ClaveAcceso, 2=razonAnulacion";
                        }

                        ArchivoXml xml = new ArchivoXml();
                        xml.setClaveAcceso(linea.split(";")[0]);
                        xml.setRazonAnulacion(linea.split(";")[1]);
                        xml.setUsuarioAnula(dto.getUsuarioAnula());

                        data.add(xml);
                    }
                    else{
                        LOGGER.log(Level.INFO, "existe linea en blanco");
                    }
                }
            }
            
            LOGGER.log(Level.INFO, "completo el array");
            
            dao.anularPorArchivo(data);
            
            return "OK";
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    private void guardarArchivosDisco(ArchivoSriDTO arc, String paramPathTomcat){
        try{

            File file = new File(paramPathTomcat + arc.getPathArchivos());
            if(!file.exists()){
                file.mkdirs();
            }

            String[] cps = arc.getPathArchivos().split("/");
            String pc = paramPathTomcat;
            for(int i=0;i<cps.length;i++){
//                System.out.println("cps.length: " +cps.length);
                pc = pc + cps[i] + "/";
                if(i >= cps.length-4){
//                    System.out.println("pc: " + pc);
                    File fc = new File(pc);
                    fc.setReadable(true, false);
                    fc.setWritable(true, false);
                    fc.setExecutable(true, false);
                }
            }

            byte[] archivByte = Base64.getDecoder().decode(arc.getRideBase64());
            File outputFile = new File(file.getAbsolutePath(), arc.getClaveAcceso()+".pdf");
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                outputStream.write(archivByte);
            }
            outputFile.setReadable(true, false);

            archivByte = Base64.getDecoder().decode(arc.getFileBase64());
            outputFile = new File(file.getAbsolutePath(), arc.getClaveAcceso()+".xml");
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                outputStream.write(archivByte);
            }
            outputFile.setReadable(true, false);
            
        }catch(Exception exc){
            LOGGER.log(Level.SEVERE, null, exc);
        }
    }
    
}
