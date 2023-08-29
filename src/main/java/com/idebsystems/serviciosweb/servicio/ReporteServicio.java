/*
 * Proyecto API Rest (ServiciosWebGestionCompras)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import com.idebsystems.serviciosweb.dao.ArchivoXmlDAO;
import com.idebsystems.serviciosweb.dao.DocumentoReembolsosDAO;
import com.idebsystems.serviciosweb.dao.ParametroDAO;
import com.idebsystems.serviciosweb.dao.ReporteDAO;
import com.idebsystems.serviciosweb.dto.DocumentoReembolsosDTO;
import com.idebsystems.serviciosweb.dto.FirmaDigitalDTO;
import com.idebsystems.serviciosweb.dto.ReporteDTO;
import com.idebsystems.serviciosweb.entities.ArchivoXml;
import com.idebsystems.serviciosweb.entities.DocumentoReembolsos;
import com.idebsystems.serviciosweb.entities.Parametro;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author jorge
 */
public class ReporteServicio {

    private static final Logger LOGGER = Logger.getLogger(ReporteServicio.class.getName());

    private final ReporteDAO dao = new ReporteDAO();
    
    public ReporteDTO generarReportePdf(String reporte, Long id) throws Exception {
        try{
            JasperPrint jasperPrint = null;
            
            if (reporte.equalsIgnoreCase("PDFFIRMA")) {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("id", id);
                jasperPrint = dao.compilacionReportePdf("rp_recepcion", parameters, null);
            }
            
            
            if (jasperPrint != null) {
                byte[] flujo = JasperExportManager.exportReportToPdf(jasperPrint);
                
                Base64.Encoder encode = Base64.getEncoder();
                
                String repobase64 = encode.encodeToString(flujo);
                
                ReporteDTO rpdto = new ReporteDTO();
                rpdto.setReporteBase64(repobase64);
                rpdto.setRespuesta("OK");
                
                return rpdto;
                
            }
            return new ReporteDTO();
            
        }catch(Exception exc){
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    public ReporteDTO generarReporteXls(String reporte, String fechaIni, String fechaFin) throws Exception {
        try{
            JasperPrint jasperPrint = null;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Calendar cini = Calendar.getInstance();
            if(Objects.nonNull(fechaIni) && !fechaIni.isBlank()){
                cini.setTime(sdf.parse(fechaIni));
                cini.set(Calendar.HOUR_OF_DAY, 0);
                cini.set(Calendar.MINUTE, 0);
                cini.set(Calendar.SECOND, 0);
            }

            Calendar cfin = Calendar.getInstance();
            if(Objects.nonNull(fechaFin) && !fechaFin.isBlank()){
                cfin.setTime(sdf.parse(fechaFin));
                cfin.set(Calendar.HOUR_OF_DAY, 23);
                cfin.set(Calendar.MINUTE, 59);
                cfin.set(Calendar.SECOND, 59);
            }

            if (reporte.equalsIgnoreCase("XLSSOLICITUD")) {
                jasperPrint = dao.compilacionReporteCsv("rp_csv_solicitud", new Timestamp(cini.getTimeInMillis()), new Timestamp(cfin.getTimeInMillis()));
            }
            
            
            if (jasperPrint != null) {
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
                
                JRXlsExporter exporterXLS = new JRXlsExporter();
                exporterXLS.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
                SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
                configuration.setDetectCellType(Boolean.TRUE);
                configuration.setWhitePageBackground(Boolean.FALSE);
                configuration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
                //con este se quita lo que la primera columna se expande cuando no hay nada.vacio
                configuration.setRemoveEmptySpaceBetweenColumns(Boolean.TRUE);
                exporterXLS.setConfiguration(configuration);
                exporterXLS.exportReport();
                
                byte[] flujo = baos.toByteArray();
                
                Base64.Encoder encode = Base64.getEncoder();
                
                String repobase64 = encode.encodeToString(flujo);
                
                ReporteDTO rpdto = new ReporteDTO();
                rpdto.setReporteBase64(repobase64);
                rpdto.setRespuesta("OK");
                
                return rpdto;
                
            }
            return new ReporteDTO();
            
        }catch(Exception exc){
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    public ReporteDTO generarReporteFirma(String reporte, String ids, String tiposGasto, String tipoReembolso, Long idUsuario,
            long idAprobador, String listaAsistentes, DocumentoReembolsosDTO reembDto, String claveFirma) throws Exception {
        try{
            //aqui primero guardar los tipos de gasto por cada registro
            ArchivoXmlDAO xmldao = new ArchivoXmlDAO();
            xmldao.guardarTiposGasto(tiposGasto.split(","));
            LOGGER.log(Level.INFO, "listaAsistentes: {0}", listaAsistentes);
            if(Objects.nonNull(listaAsistentes))
                xmldao.guardarAsistentes(listaAsistentes.split(","));
            
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("condicion", " AND cabecera.id in ("+ids+") ");
            //parameters.put("", new Date());
            parameters.put("p_motivoViaje", reembDto.getMotivoViaje());
            parameters.put("p_periodoViaje", reembDto.getPeriodoViaje());
            parameters.put("p_lugarViaje", reembDto.getLugarViaje());
            
            //segun el tipo de reembolso generar el formato pdf
            String nombreReporte = "rp_gastoreembolso";
            if(tipoReembolso.equalsIgnoreCase("VIAJES")){
                nombreReporte = "rp_gastoviajes";
                
                //enviar los datos como parametros
                parameters.put("p_fondoEntregado", reembDto.getFondoEntregado());
                parameters.put("p_observaciones", reembDto.getObservaciones());
            }
            else{
                //enviar los datos como parametros
                parameters.put("p_seleccion", reembDto.getSeleccion());
                if(reembDto.getSeleccion().equalsIgnoreCase("JUSTIFICACIÓN FONDOS DE ATENCIÓN")){
                    parameters.put("p_fondoEntregado", reembDto.getFondoEntregado());
                }
            }
            
            JasperPrint jasperPrint = dao.compilacionReportePdf(nombreReporte, parameters, null);


            if (jasperPrint != null) {
                byte[] flujo = JasperExportManager.exportReportToPdf(jasperPrint);

                //si es si firma se debe enviar a firmar el pdf
                if(reporte.equalsIgnoreCase("SIFIRMA")){
                    //buscar la firma activa del usuario que genera el pdf
                    FirmaDigitalDTO firmaDto;
                    try{
                        FirmaDigitalServicio srvFD = new FirmaDigitalServicio();
                        firmaDto = srvFD.getFirmaActivaPorIdUsuario(idUsuario, false);
                    }catch(Exception exc){
                        ReporteDTO rpdto = new ReporteDTO();
                        rpdto.setRespuesta("NO EXISTE LA FIRMA DIGITAL ASOCIADA AL USUARIO");
                        return rpdto;
                    }

                    try{
                        if(firmaDto.getTipoFirma() == 0){
                            //se coloca la clave que enviaron desde la pantalla
                            FirmarPdfServicio ff=new FirmarPdfServicio();
                            flujo = ff.firmarConDigital(flujo, firmaDto, false, claveFirma, false);
                        }
                        if(firmaDto.getTipoFirma() == 1){
                            FirmarPdfServicio ff=new FirmarPdfServicio();
                            flujo = ff.firmarConImagen(flujo, firmaDto, false);
                        }
                    }catch(Exception exc){
                        ReporteDTO rpdto = new ReporteDTO();
                        rpdto.setRespuesta("NO SE PUDO OBTENER LA FIRMA ELECTRONICA. " + exc.getMessage());
                        return rpdto;
                    }
                }

                Base64.Encoder encode = Base64.getEncoder();

                String repobase64 = encode.encodeToString(flujo);

                ReporteDTO rpdto = new ReporteDTO();
                
                if(reporte.equalsIgnoreCase("SIFIRMA")){
                    
                    //obtener los parametros
                    ParametroDAO pdao = new ParametroDAO();
                    List<Parametro> params = pdao.listarParametros();
                    
                    Parametro purl = params.stream().filter(p -> p.getNombre().equalsIgnoreCase("URL_SISTEMA")).findAny().get();
                    Parametro pcarp = params.stream().filter(p -> p.getNombre().equalsIgnoreCase("CARPETA_REEMBOLSOS")).findAny().get();
                    
                    DocumentoReembolsos doc = new DocumentoReembolsos();
                    doc.setEstado("POR_AUTORIZAR");
    //                doc.setFechaAutoriza(fechaAutoriza);
                    doc.setFechaCarga(new Date());
    //                doc.setId(idUsuario);
                    doc.setPathArchivo(purl.getValor() + pcarp.getValor() + doc.getFechaCarga().getTime()+".pdf");
    //                doc.setRazonRechazo(reporte);
    //                doc.setUsuarioAutoriza(reporte);
                    doc.setUsuarioCarga(idUsuario);
                    doc.setIdsXml(ids);
                    doc.setTipoReembolso(tipoReembolso);
                    doc.setIdAprobador(idAprobador);
                
                    
                    doc.setMotivoViaje(reembDto.getMotivoViaje());
                    doc.setPeriodoViaje(reembDto.getPeriodoViaje());
                    doc.setLugarViaje(reembDto.getLugarViaje());
                    doc.setFondoEntregado(reembDto.getFondoEntregado());
                    doc.setObservaciones(reembDto.getObservaciones());
                    doc.setSeleccion(reembDto.getSeleccion());
                
                
                    
                    DocumentoReembolsosDAO docdao = new DocumentoReembolsosDAO();
                    docdao.guardarDocumentoReembolsos(doc, ids, idUsuario, true);
                    
                    rpdto.setPathArchivo(pcarp.getValor() + doc.getFechaCarga().getTime()+".pdf");
                    
                    //aqui enviar el correo de que se genero el nuevo reembolso
                    CorreoServicio remsrv = new CorreoServicio();
                    remsrv.enviarCorreoNuevoReembolso(idUsuario, repobase64, doc);
                    
                    
                }                
                
                rpdto.setReporteBase64(repobase64);
                rpdto.setRespuesta("OK");
LOGGER.log(Level.INFO, "completo: {0}", rpdto );
                return rpdto;

            }
            ReporteDTO r = new ReporteDTO();
            r.setRespuesta("ERROR AL GENERAR EL REPORTE");
            return r;
            
        }catch(Exception exc){
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    
    public ReporteDTO generarRidePdf(ArchivoXml archivoXml, String version) throws Exception {
        try{
            
//            LOGGER.log(Level.INFO, "el compro: {0}", archivoXml.getComprobante());
            
            JSONObject jsonObject = new JSONObject(archivoXml.getComprobante()); 
            String xml = XML.toString(jsonObject);
            
//            LOGGER.log(Level.INFO, "el xml del comp: {0}", xml);
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
//            dbf.setNamespaceAware(false);
//dbf.setValidating(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            Document documentXml = db.parse(new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + xml)));
            
            
            
//            LOGGER.log(Level.INFO, "creado el documento: {0}", documentXml);
            
            List<String> datos = crearXPathYReporte(archivoXml.getTipoDocumento(), version);
            
            String xpath = datos.get(0);
            String nombreReporte = datos.get(1);
            
//            LOGGER.log(Level.INFO, "xpath: {0}", xpath);
            
            JRXmlDataSource jrds = new JRXmlDataSource(documentXml, xpath);
            
//            LOGGER.log(Level.INFO, "creo el jrds: {0}", jrds);
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("numeroAutorizacion", archivoXml.getNumeroAutorizacion());
            parameters.put("fechaAutorizacion", archivoXml.getFechaAutorizacion());
            
//            LOGGER.log(Level.INFO, "antes de compilar");
            
            JasperPrint jasperPrint = dao.compilacionReportePdf(nombreReporte, parameters, jrds);
            
//            LOGGER.log(Level.INFO, "listo el jasperprint: {0}", jasperPrint);
            
            if (jasperPrint != null) {
                byte[] flujo = JasperExportManager.exportReportToPdf(jasperPrint);
                
                Base64.Encoder encode = Base64.getEncoder();
                
                String repobase64 = encode.encodeToString(flujo);
                
                ReporteDTO rpdto = new ReporteDTO();
                rpdto.setReporteBase64(repobase64);
                rpdto.setRespuesta("OK");
                
                return rpdto;
                
            }
            else{
                LOGGER.log(Level.INFO, "ah sido nulllo:");
            }
            return new ReporteDTO();
            
        }catch(Exception exc){
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    private List<String> crearXPathYReporte(String tipoDocumentoSri, String version){
        List<String> data = new ArrayList<>();

            switch (tipoDocumentoSri) {
                case "01": {
                    data.add("/factura/detalles/detalle");
                    data.add("factura");
                    break;
                }
                case "04": {
                    data.add("/notaCredito/detalles/detalle");
                    data.add("notacredito");
                    break;
                }
                case "05": {
                    data.add("/notaCredito/detalles/detalle");
                    data.add("notacredito");
                    break;
                }
                case "06": {
                    data.add("/notaCredito/detalles/detalle");
                    data.add("notacredito");
                    break;
                }
                case "07": {
                    if(version.equalsIgnoreCase("2.0.0")){
                        data.add("/comprobanteRetencion/docsSustento/docSustento/retenciones/retencion");
                        data.add("retencion");
                    }
                    else{
                        data.add("/comprobanteRetencion/impuestos/impuesto");
                        data.add("retencion_1");
                    }
                    
                    break;
                }
                default: {
                    data.add("NONE");
                    data.add("NONE");
                }
            }
            
            return data;
    }
    
    
//    public static void main(String[] srg){
//        try{
//            
//            ReporteServicio sr = new ReporteServicio();
//            
//            ArchivoXml archivoXml = new ArchivoXml();
//            
//                    archivoXml.setComprobante("{\"factura\":{\"infoTributaria\":{\"claveAcceso\":\"2802202301179267066700120010010000934271009722718\",\"ruc\":1792670667001,\"razonSocial\":\"CORPORACION BKS S.A.\",\"estab\":\"001\",\"ptoEmi\":\"001\",\"ambiente\":2,\"nombreComercial\":\"GANASALUD\",\"codDoc\":\"01\",\"tipoEmision\":1,\"secuencial\":\"000093427\",\"dirMatriz\":\"AV. 12 DE OCTUBRE Y CORUÑA, EDIF. URBAN PLAZA, PISO 14 OFICINA 19\"},\"infoAdicional\":{\"campoAdicional\":[{\"nombre\":\"Pedido\",\"content\":1093756},{\"nombre\":\"Forma de pago\",\"content\":\"CASHMANAGEMENT\"},{\"nombre\":\"Telefono\",\"content\":\"0985924970\"},{\"nombre\":\"Direccion\",\"content\":\"QUITO\"},{\"nombre\":\"Mail\",\"content\":\"none@gmail.com\"},{\"nombre\":\"Observacion\",\"content\":null},{\"nombre\":\"Contribuyente Régimen RIMPE\",\"content\":\"Contribuyente Régimen RIMPE\"},{\"nombre\":\"Agente de Retención\",\"content\":\"No. Resolución: NAC-DNCRASC20-00000001\"}]},\"detalles\":{\"detalle\":{\"descripcion\":\"GANA SALUD PARA MI\",\"precioUnitario\":5,\"precioTotalSinImpuesto\":5,\"descuento\":0,\"impuestos\":{\"impuesto\":{\"codigoPorcentaje\":0,\"tarifa\":0,\"codigo\":2,\"valor\":0,\"baseImponible\":5}},\"codigoPrincipal\":\"GS001\",\"cantidad\":1,\"detallesAdicionales\":{\"detAdicional\":{\"valor\":\"GANASALUD\",\"nombre\":\"Grupo Producto\"}}}},\"id\":\"comprobante\",\"infoFactura\":{\"direccionComprador\":\"QUITO\",\"identificacionComprador\":1719052621,\"razonSocialComprador\":\"LUIS ANGEL SANDOVAL FERNANDEZ\",\"obligadoContabilidad\":\"SI\",\"fechaEmision\":\"28/02/2023\",\"dirEstablecimiento\":\"AV. 12 DE OCTUBRE Y CORUÑA, EDIF. URBAN PLAZA\",\"propina\":0,\"pagos\":{\"pago\":{\"total\":5,\"plazo\":0,\"formaPago\":20,\"unidadTiempo\":\"dias\"}},\"tipoIdentificacionComprador\":\"05\",\"importeTotal\":5,\"totalDescuento\":0,\"totalConImpuestos\":{\"totalImpuesto\":{\"codigoPorcentaje\":0,\"codigo\":2,\"valor\":0,\"baseImponible\":5}},\"moneda\":\"DOLAR\",\"totalSinImpuestos\":5},\"version\":\"1.0.0\"}}");
//                    archivoXml.setFechaAutorizacion(new Date());
//                    archivoXml.setTipoDocumento("01");
//                    archivoXml.setNumeroAutorizacion("123");
//                    
//                    sr.generarRidePdf(archivoXml);
//            
//        }catch(Exception exc){
//            exc.printStackTrace();
//        }
//    }
}


