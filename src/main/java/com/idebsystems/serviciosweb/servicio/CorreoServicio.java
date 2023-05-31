/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import com.idebsystems.serviciosweb.dao.ParametroDAO;
import com.idebsystems.serviciosweb.dao.UsuarioDAO;
import com.idebsystems.serviciosweb.dto.UsuarioDTO;
import com.idebsystems.serviciosweb.entities.ArchivoXml;
import com.idebsystems.serviciosweb.entities.DocumentoReembolsos;
import com.idebsystems.serviciosweb.entities.Parametro;
import com.idebsystems.serviciosweb.entities.Usuario;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author jorge
 */
public class CorreoServicio {

    private static final Logger LOGGER = Logger.getLogger(CorreoServicio.class.getName());

    public String enviarUrlNuevaClave(String correo) throws Exception {
        try {

            //generar la nueva contrasenia para el usuario registrado con el correo enviado
            UsuarioServicio userSrv = new UsuarioServicio();
            UsuarioDTO userdto = userSrv.generarClavePorCorreo(correo);
            if (Objects.isNull(userdto)) {
                return "USUARIO CON EL CORREO INGRESADO NO EXISTE";
            }

            //consultar los prametros del correo desde la base de datos.
            ParametroDAO paramDao = new ParametroDAO();
            List<Parametro> listaParams = paramDao.listarParametros();

            List<Parametro> paramsMail = listaParams.stream().filter(p -> p.getNombre().contains("MAIL")).collect(Collectors.toList());

            Parametro paramSubect = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("ASUNTOMAIL_RC")).findAny().get();
            Parametro paramMsm = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("MENSAJEMAIL_RC")).findAny().get();
            

            //transformar el mensaje con los datos
            String mensajeText = paramMsm.getValor().replace("[clave]", userdto.getClave());
            mensajeText = mensajeText.replace("[usuario]", userdto.getUsuario());
            mensajeText = mensajeText.replace("[nombre]", userdto.getNombre());

            return enviarCorreo(correo, paramSubect.getValor(), mensajeText, null);

        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }

    }

    public String enviarCorreo(String correo, String asunto, String mensajeCorreo, List<File> archivosAdjuntos) throws Exception {
        try {

            //consultar los prametros del correo desde la base de datos.
            ParametroDAO paramDao = new ParametroDAO();
            List<Parametro> listaParams = paramDao.listarParametros();

            List<Parametro> paramsMail = listaParams.stream().filter(p -> p.getNombre().contains("MAIL")).collect(Collectors.toList());
            //findAny().orElse(null);
            Parametro paramAliasCorreo = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("ALIASMAIL")).findAny().get();
            Parametro paramNomRemit = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("NOMBREREMITENTEMAIL")).findAny().get();

            Parametro paramHost = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("HOSTMAIL")).findAny().get();
            Parametro paramPuerto = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("PUERTOMAIL")).findAny().get();
            Parametro paramUserMail = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("USERMAIL")).findAny().get();
            Parametro paramClaveMail = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("CLAVEMAIL")).findAny().get();

            String host = paramHost.getValor(); //"smtp.office365.com";
            int port = Integer.parseInt(paramPuerto.getValor()); //587;
            String userName = paramUserMail.getValor();// "jorgep_m@hotmail.com";
            String password = paramClaveMail.getValor();

            Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
            properties.put("mail.smtp.user", userName);
            properties.put("mail.smtp.password", password);
            //
//            properties.setProperty("mail.smtp.ssl.trust", "*");
            properties.setProperty("mail.debug.auth", "true");
//            properties.setProperty("mail.debug", "true");

            // creates a new session with an authenticator
            Authenticator auth = new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            };
            Session session = Session.getInstance(properties, auth);

            LOGGER.info("paramNomRemit::: " + paramNomRemit.getValor());
            
            //seccion para comprobar si el correo tiene mas de uno, con el ;
            List<InternetAddress> correosDestino = new ArrayList<>();
            if(correo.contains(";")){
                List<String> correos = Arrays.asList(correo.split(";"));
                for(String c : correos){
                    if(Objects.nonNull(c) && !c.isBlank()){
                        InternetAddress addressEmail = new InternetAddress(c);
                        correosDestino.add(addressEmail);
                    }
                }
            }
            else{
                InternetAddress addressEmail = new InternetAddress(correo);
                correosDestino.add(addressEmail);
            }
            

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(paramAliasCorreo.getValor(), paramNomRemit.getValor()));
            msg.addRecipients(Message.RecipientType.BCC, correosDestino.toArray(new InternetAddress[correosDestino.size()]));
            msg.setSubject(asunto);
//            msg.setText(mensajeText);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(mensajeCorreo, "text/html; charset=utf-8");

            MimeMultipart multipart = new MimeMultipart("related");
            multipart.addBodyPart(messageBodyPart);
            
            
            //para cuando tenga un archivo adjunto
            if(Objects.nonNull(archivosAdjuntos)){
                for(File adjunto : archivosAdjuntos) {
                    BodyPart adjuntoPdf = new MimeBodyPart();
                    adjuntoPdf.setDataHandler(new DataHandler(new FileDataSource(adjunto)));
                    adjuntoPdf.setFileName(adjunto.getName());
                    
                    //se adjunta
                    multipart.addBodyPart(adjuntoPdf);
                }
            }
            
            
            msg.setContent(multipart);

            Transport.send(msg);
//Transport  t = session.getTransport("smtp");
//t.connect(userName, password);
//t.sendMessage(msg, msg.getAllRecipients());
//t.close();

            return "ENVIO EXITOSO";

        } catch (AddressException exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } catch (MessagingException exc) {
            LOGGER.log(Level.SEVERE, null, exc);
//            throw new Exception(exc);
            return exc.getMessage();
        } catch (UnsupportedEncodingException exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }

    public void enviarCorreoCargaArchivo(Long idUsuario, ArchivoXml archivoXml) throws Exception {
        try {
            Thread correo = new Thread(() -> {
                try {
                    //buscar el usuario con el iduser
                    UsuarioDAO userDao = new UsuarioDAO();
                    Usuario user = userDao.buscarUsuarioPorId(idUsuario);
                    
                    //consultar los prametros del correo desde la base de datos.
                    ParametroDAO paramDao = new ParametroDAO();
                    List<Parametro> listaParams = paramDao.listarParametros();
                    
                    List<Parametro> paramsMail = listaParams.stream().filter(p -> p.getNombre().contains("MAIL")).collect(Collectors.toList());
                    
                    Parametro paramSubect = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("ASUNTOMAIL_CA")).findAny().get();
                    Parametro paramMsm = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("MENSAJEMAIL_CA")).findAny().get();
                    Parametro destinatario = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("DESTINOMAIL_CA")).findAny().get();
                    
                    //transformar el mensaje con los datos
                    String mensajeText = paramMsm.getValor().replace("[nombre]", user.getNombre());
                    
                    enviarCorreo(destinatario.getValor(), paramSubect.getValor(), mensajeText, null);
                    
                } catch (Exception exc) {
                    LOGGER.log(Level.SEVERE, null, exc);
                }
            });

            correo.start();

        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    public void enviarCorreoNuevoReembolso(Long idUsuario, String pdfBase64, DocumentoReembolsos doc)  {
        try {
            Thread correo = new Thread(() -> {
                try {
                    //buscar el usuario con el iduser
                    UsuarioDAO userDao = new UsuarioDAO();
                    Usuario user = userDao.buscarUsuarioPorId(idUsuario);
                    
                    //consultar los prametros del correo desde la base de datos.
                    ParametroDAO paramDao = new ParametroDAO();
                    List<Parametro> listaParams = paramDao.listarParametros();
                    
                    List<Parametro> paramsMail = listaParams.stream().filter(p -> p.getNombre().contains("MAIL")).collect(Collectors.toList());
                    
                    Parametro pasunto = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("ASUNTOMAIL_REEMBOLSO")).findAny().get();
                    Parametro paramMsm = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("MENSAJEMAIL_REEMBOLSO")).findAny().get();
                    Parametro pdestino = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("DESTINOMAIL_REEMBOLSO")).findAny().get();   
            
                    //generar el pdf a adjuntar
                    
                    File fileCot = File.createTempFile("reembolso",".pdf");
                    FileOutputStream fis = new FileOutputStream(fileCot);
                    fis.write(Base64.getDecoder().decode(pdfBase64));
                    fis.close();

                    List<File> archivosAdjuntos = new ArrayList<>();
                    archivosAdjuntos.add(fileCot);
                    
                    //transformar el mensaje con los datos
                    String mensajeText = paramMsm.getValor().replace("[usuario]", user.getNombre());
                    mensajeText = mensajeText.replace("[estado]", doc.getEstado());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    mensajeText = mensajeText.replace("[fechaCarga]", sdf.format(doc.getFechaCarga()));
                    if(Objects.nonNull(doc.getFechaAutoriza()))
                        mensajeText = mensajeText.replace("[fechaAprueba]", sdf.format(doc.getFechaAutoriza()));
                    mensajeText = mensajeText.replace("[usuarioAprueba]", Objects.nonNull(doc.getUsuarioAutoriza()) ? doc.getUsuarioAutoriza() : "");
                    mensajeText = mensajeText.replace("[razonRechazo]", Objects.nonNull(doc.getRazonRechazo()) ? doc.getRazonRechazo() : "");
                    mensajeText = mensajeText.replace("[tipoReembolso]", (doc.getTipoReembolso().equalsIgnoreCase("VIAJES") ? "LIQUIDACION DE GASTO DE VIAJES" : (doc.getTipoReembolso().equalsIgnoreCase("GASTOS") ? "REEMBOLSO DE GASTOS" : doc.getTipoReembolso())));
                    mensajeText = mensajeText.replace("[numero]", doc.getId()+"");

                    //esta parte es para controlar si el correo se envia al jefe del usuario, que es el aporbador , o se envia 
                    //al contador que es los correos en los parametros
                    String correos = pdestino.getValor();
                    //si es para el jefe
                    if(doc.getIdAprobador() > 0){
                        correos = userDao.buscarUsuarioPorId(doc.getIdAprobador()).getCorreo();
                    }
                    //hasta aca
                    
                    enviarCorreo(correos, pasunto.getValor(), mensajeText, archivosAdjuntos);
                    
                } catch (Exception exc) {
                    LOGGER.log(Level.SEVERE, null, exc);
                }
            });

            correo.start();

        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
//            throw new Exception(exc);
        }
    }
    
    
    
    public void enviaCorreoApruebaRechaza(Long idUsuario, DocumentoReembolsos doc, String archivoBase64)  {
        try {
            Thread correo = new Thread(() -> {
                try {
                    //buscar el usuario con el iduser
                    UsuarioDAO userDao = new UsuarioDAO();
                    Usuario user = userDao.buscarUsuarioPorId(idUsuario);
                    
                    //consultar los prametros del correo desde la base de datos.
                    ParametroDAO paramDao = new ParametroDAO();
                    List<Parametro> listaParams = paramDao.listarParametros();
                    
                    Parametro pasunto = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("ASUNTOMAIL_RECHAZA_REEMBOLSO")).findAny().get();
                    Parametro paramMsm = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("MENSAJEMAIL_RECHAZA_REEMBOLSO")).findAny().get();
                    
                    //transformar el mensaje con los datos
                    String mensajeText = paramMsm.getValor().replace("[usuario]", user.getNombre());
                    mensajeText = mensajeText.replace("[estado]", doc.getEstado());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    mensajeText = mensajeText.replace("[fechaCarga]", sdf.format(doc.getFechaCarga()));
                    if(Objects.nonNull(doc.getFechaAutoriza()))
                        mensajeText = mensajeText.replace("[fechaAprueba]", sdf.format(doc.getFechaAutoriza()));
                    mensajeText = mensajeText.replace("[usuarioAprueba]", Objects.nonNull(doc.getUsuarioAutoriza()) ? doc.getUsuarioAutoriza() : "");
                    mensajeText = mensajeText.replace("[razonRechazo]", Objects.nonNull(doc.getRazonRechazo()) ? doc.getRazonRechazo() : "");
                    mensajeText = mensajeText.replace("[tipoReembolso]", (doc.getTipoReembolso().equalsIgnoreCase("VIAJES") ? "LIQUIDACION DE GASTO DE VIAJES" : (doc.getTipoReembolso().equalsIgnoreCase("GASTOS") ? "REEMBOLSO DE GASTOS" : doc.getTipoReembolso())));
                    mensajeText = mensajeText.replace("[numero]", doc.getId()+"");
                    

                    String asunto = pasunto.getValor().replace("[estado]", doc.getEstado());
                    
                    
                    //generar el pdf a adjuntar     
                    File fileCot = File.createTempFile("reembolso",".pdf");
                    FileOutputStream fis = new FileOutputStream(fileCot);
                    fis.write(Base64.getDecoder().decode(archivoBase64));
                    fis.close();

                    List<File> archivosAdjuntos = new ArrayList<>();
                    archivosAdjuntos.add(fileCot);
            
                    String correos = user.getCorreo();
                    
                    //cuando es aprobado se debe enviar tambien el correo al contador a parte del usuario que genera el pdf
                    //al correo que esta parametrizado
                    if(!doc.getEstado().equalsIgnoreCase("RECHAZADO")){
                        Parametro pdestino = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("DESTINOMAIL_REEMBOLSO")).findAny().get();
                        correos = correos + ";" + pdestino.getValor();
                    }
                    
                    enviarCorreo(correos, asunto, mensajeText, archivosAdjuntos);
                    
                } catch (Exception exc) {
                    LOGGER.log(Level.SEVERE, null, exc);
                }
            });

            correo.start();

        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
//            throw new Exception(exc);
        }
    }
    
    
    
    public void enviaCorreoSolicitaJustificacion(Long idUsuario, DocumentoReembolsos doc, String archivoBase64) throws Exception {
        try {
            //buscar el usuario con el iduser
            UsuarioDAO userDao = new UsuarioDAO();
            Usuario user = userDao.buscarUsuarioPorId(idUsuario);

            //consultar los prametros del correo desde la base de datos.
            ParametroDAO paramDao = new ParametroDAO();
            List<Parametro> listaParams = paramDao.listarParametros();

            Parametro pasunto = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("ASUNTOMAIL_SOLICITA_JUSTIFICACION")).findAny().get();
            Parametro paramMsm = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("MENSAJEMAIL_SOLICITA_JUSTIFICACION")).findAny().get();

            //transformar el mensaje con los datos
            String mensajeText = paramMsm.getValor().replace("[usuario]", user.getNombre());
            mensajeText = mensajeText.replace("[estado]", doc.getEstado());
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            mensajeText = mensajeText.replace("[fechaCarga]", sdf.format(doc.getFechaCarga()));
            if(Objects.nonNull(doc.getFechaAutoriza()))
                mensajeText = mensajeText.replace("[fechaAprueba]", sdf.format(doc.getFechaAutoriza()));
            mensajeText = mensajeText.replace("[usuarioAprueba]", Objects.nonNull(doc.getUsuarioAutoriza()) ? doc.getUsuarioAutoriza() : "");
            mensajeText = mensajeText.replace("[razonRechazo]", Objects.nonNull(doc.getRazonRechazo()) ? doc.getRazonRechazo() : "");
            mensajeText = mensajeText.replace("[tipoReembolso]", (doc.getTipoReembolso().equalsIgnoreCase("VIAJES") ? "LIQUIDACION DE GASTO DE VIAJES" : (doc.getTipoReembolso().equalsIgnoreCase("GASTOS") ? "REEMBOLSO DE GASTOS" : doc.getTipoReembolso())));
            mensajeText = mensajeText.replace("[numero]", doc.getId()+"");
            

            String asunto = pasunto.getValor().replace("[estado]", doc.getEstado());
            
            //generar el pdf a adjuntar     
            File fileCot = File.createTempFile("reembolso",".pdf");
            FileOutputStream fis = new FileOutputStream(fileCot);
            fis.write(Base64.getDecoder().decode(archivoBase64));
            fis.close();

            List<File> archivosAdjuntos = new ArrayList<>();
            archivosAdjuntos.add(fileCot);

            enviarCorreo(user.getCorreo(), asunto, mensajeText, archivosAdjuntos);

        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    
    public void enviaCorreoCargaJustificacion(Long idUsuario, DocumentoReembolsos doc, String archivoBase64) throws Exception {
        try {
            //buscar el usuario con el iduser
            UsuarioDAO userDao = new UsuarioDAO();
            Usuario user = userDao.buscarUsuarioPorId(idUsuario);

            //consultar los prametros del correo desde la base de datos.
            ParametroDAO paramDao = new ParametroDAO();
            List<Parametro> listaParams = paramDao.listarParametros();

            Parametro pasunto = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("ASUNTOMAIL_CARGA_JUSTIFICACION")).findAny().get();
            Parametro paramMsm = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("MENSAJEMAIL_CARGA_JUSTIFICACION")).findAny().get();
            Parametro paramDestino = listaParams.stream().filter(p -> p.getNombre().equalsIgnoreCase("DESTINOMAIL_CARGA_JUSTIFICACION")).findAny().get();

            //transformar el mensaje con los datos
            String mensajeText = paramMsm.getValor().replace("[usuario]", user.getNombre());
            mensajeText = mensajeText.replace("[estado]", doc.getEstado());
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            mensajeText = mensajeText.replace("[fechaCarga]", sdf.format(doc.getFechaCarga()));
            if(Objects.nonNull(doc.getFechaAutoriza()))
                mensajeText = mensajeText.replace("[fechaAprueba]", sdf.format(doc.getFechaAutoriza()));
            mensajeText = mensajeText.replace("[usuarioAprueba]", Objects.nonNull(doc.getUsuarioAutoriza()) ? doc.getUsuarioAutoriza() : "");
            mensajeText = mensajeText.replace("[razonRechazo]", Objects.nonNull(doc.getRazonRechazo()) ? doc.getRazonRechazo() : "");
            mensajeText = mensajeText.replace("[tipoReembolso]", (doc.getTipoReembolso().equalsIgnoreCase("VIAJES") ? "LIQUIDACION DE GASTO DE VIAJES" : (doc.getTipoReembolso().equalsIgnoreCase("GASTOS") ? "REEMBOLSO DE GASTOS" : doc.getTipoReembolso())));
            mensajeText = mensajeText.replace("[numero]", doc.getId()+"");
            
            
            //asunto del correo
            String asunto = pasunto.getValor().replace("[estado]", doc.getEstado());
            
            //generar el archivo a adjuntar
            LOGGER.log(Level.INFO, "el tipo: {0}", doc.getTipoJustificacionBase64());
            
            String tipof = doc.getTipoJustificacionBase64().split("/")[1];
            
            if(doc.getTipoJustificacionBase64().contains("vnd.ms-excel"))
                tipof = "xls";
            if(doc.getTipoJustificacionBase64().contains("vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                tipof = "xlsx";
            if(doc.getTipoJustificacionBase64().contains("text/plain"))
                tipof = "txt";
            
            
                    
            File fileCot = File.createTempFile("justificativo","." + tipof);
            FileOutputStream fis = new FileOutputStream(fileCot);
            fis.write(Base64.getDecoder().decode(archivoBase64));
            fis.close();

            List<File> archivosAdjuntos = new ArrayList<>();
            archivosAdjuntos.add(fileCot);

            enviarCorreo(paramDestino.getValor(), asunto, mensajeText, archivosAdjuntos);

        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
