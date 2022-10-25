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
import com.idebsystems.serviciosweb.entities.Parametro;
import com.idebsystems.serviciosweb.entities.Usuario;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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

            Parametro paramNomRemit = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("NOMBREREMITENTEMAIL")).findAny().get();
            Parametro paramSubect = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("ASUNTOMAIL_RC")).findAny().get();
            Parametro paramMsm = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("MENSAJEMAIL_RC")).findAny().get();
            Parametro aliasCorreoEnvio = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("ALIASMAIL")).findAny().get();

            //transformar el mensaje con los datos
            String mensajeText = paramMsm.getValor().replace("[clave]", userdto.getClave());
            mensajeText = mensajeText.replace("[usuario]", userdto.getUsuario());
            mensajeText = mensajeText.replace("[nombre]", userdto.getNombre());

            return enviarCorreo(correo, paramSubect.getValor(), mensajeText, aliasCorreoEnvio.getValor(), paramNomRemit.getValor());

        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }

    }

    public String enviarCorreo(String correo, String asunto, String mensajeCorreo, String aliasCorreoEnvio, String nombreRemitente) throws Exception {
        try {

            //consultar los prametros del correo desde la base de datos.
            ParametroDAO paramDao = new ParametroDAO();
            List<Parametro> listaParams = paramDao.listarParametros();

            List<Parametro> paramsMail = listaParams.stream().filter(p -> p.getNombre().contains("MAIL")).collect(Collectors.toList());
            //findAny().orElse(null);

            Parametro paramHost = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("HOSTMAIL")).findAny().get();
            Parametro paramPuerto = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("PUERTOMAIL")).findAny().get();
            Parametro paramUserMail = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("USERMAIL")).findAny().get();
            Parametro paramClaveMail = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("CLAVEMAIL")).findAny().get();

            String host = paramHost.getValor(); //"smtp.office365.com";
            int port = Integer.parseInt(paramPuerto.getValor()); //587;
            String userName = paramUserMail.getValor();
            String password = paramClaveMail.getValor();

            Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
            properties.put("mail.user", userName);
            properties.put("mail.password", password);
            //
            properties.setProperty("mail.debug.auth", "true");

            // creates a new session with an authenticator
            Authenticator auth = new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            };
            Session session = Session.getInstance(properties, auth);

            LOGGER.info("paramNomRemit::: " + nombreRemitente);
            
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
            msg.setFrom(new InternetAddress(aliasCorreoEnvio, nombreRemitente));
            msg.addRecipients(Message.RecipientType.TO, correosDestino.toArray(new InternetAddress[correosDestino.size()]));
            msg.setSubject(asunto);
//            msg.setText(mensajeText);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(mensajeCorreo, "text/html; charset=utf-8");

            MimeMultipart multipart = new MimeMultipart("related");
            multipart.addBodyPart(messageBodyPart);
            msg.setContent(multipart);

            Transport.send(msg);

            return "ENVIO EXITOSO";

        } catch (AddressException exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } catch (MessagingException exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
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
                    
                    Parametro paramNomRemit = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("NOMBREREMITENTEMAIL")).findAny().get();
                    Parametro paramSubect = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("ASUNTOMAIL_CA")).findAny().get();
                    Parametro paramMsm = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("MENSAJEMAIL_CA")).findAny().get();
                    Parametro aliasCorreoEnvio = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("ALIASMAIL")).findAny().get();
                    Parametro destinatario = paramsMail.stream().filter(p -> p.getNombre().equalsIgnoreCase("DESTINOMAIL_CA")).findAny().get();
                    
                    //transformar el mensaje con los datos
                    String mensajeText = paramMsm.getValor().replace("[nombre]", user.getNombre());
                    
                    enviarCorreo(destinatario.getValor(), paramSubect.getValor(), mensajeText, aliasCorreoEnvio.getValor(), paramNomRemit.getValor());
                    
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
}
