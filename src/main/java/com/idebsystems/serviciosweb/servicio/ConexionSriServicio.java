/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
//import org.apache.xml.serialize.OutputFormat;
//import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author jorge
 */
public class ConexionSriServicio {

    private static final Logger LOGGER = Logger.getLogger(ConexionSriServicio.class.getName());
    
    public static void main(String... arg){
        try{
            //funciona con el ?wsdl o sin este tambien si funciona
            String urlSri = "https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline";
            String claveAcc = ("2402202301179141513200121360500004243054126153318");
            
            ConexionSriServicio s = new ConexionSriServicio();
            s.getAutorizacionComprobante(claveAcc, urlSri);
//            s.prueba2(lista);
            
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }

    public String getAutorizacionComprobante(String claveAcceso, String urlSri) throws Exception {
        try {
            //aqui llamar al servicio web del sri de autorizacion.
            //esto es para la parte del certificado, pero ya no es necesario, si se conecta sin certificado, 
            //si en algun momento pide el certificado, se puede utilizar el metodo del final
            //el metodo prueba2...
//                    InputStream trustStream = new FileInputStream("/home/jorge/certificados/certificadoSri/certificadoSslSri");
//char[] trustPassword = "changeit".toCharArray();
//
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//trustStore.load(trustStream, trustPassword);
//
//            TrustManagerFactory trustFactory =
//    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//trustFactory.init(trustStore);
//            TrustManager[] trustManagers = trustFactory.getTrustManagers();
//
//            SSLContext sslContext = SSLContext.getInstance("SSL");
//sslContext.init(null, trustManagers, null);
//SSLContext.setDefault(sslContext);




//System.out.println("props: "
//                    + System.getProperty("javax.net.ssl.keyStore"));
//            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                    return null;
//                }
//
//                public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                }
//
//                public void checkServerTrusted(X509Certificate[] certs,
//                        String authType) {
//                }
//            }}; // Install the all-trusting trust manager
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null,
//                    trustAllCerts, new java.security.SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); //Create all-trusting host name verifier 
//                    HostnameVerifier allHostsValid = new HostnameVerifier() {
//                public boolean verify(String hostname,SSLSession session) {
//                    return true;
//                }
//            }; // Install the all-trusting host verifier 
//            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);



            //Code to make a webservice HTTP request
            String responseString = "";
            String outputString = "";
            String wsURL = urlSri;
            URL url = new URL(wsURL);
            URLConnection connection = url.openConnection();
            HttpsURLConnection httpConn = (HttpsURLConnection) connection;
//            httpConn.setSSLSocketFactory(sslContext.getSocketFactory());

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            String xmlInput
                    = " <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ec=\"http://ec.gob.sri.ws.autorizacion\">\n"
                    + "   <soapenv:Header/>\n"
                    + "   <soapenv:Body>\n"
                    + "      <ec:autorizacionComprobante>\n"
                    + "         <!--Optional:-->\n"
                    + "         <claveAccesoComprobante>" + claveAcceso + "</claveAccesoComprobante>\n"
                    + "      </ec:autorizacionComprobante>\n"
                    + "   </soapenv:Body>\n"
                    + "</soapenv:Envelope>";

            LOGGER.log(Level.INFO, "va a llamar con claveAcceso: {0}", claveAcceso);

            byte[] buffer = new byte[xmlInput.length()];
            buffer = xmlInput.getBytes();
            bout.write(buffer);
            byte[] b = bout.toByteArray();
            String SOAPAction
                    = "";
// Set the appropriate HTTP parameters.
            httpConn.setRequestProperty("Content-Length",
                    String.valueOf(b.length));
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestProperty("SOAPAction", SOAPAction);
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);

//            System.out.println("aun nop: " + bout);
//for(Provider p : Security.getProviders()){
//                System.out.println(p.getName());
//            }

            OutputStream out = httpConn.getOutputStream();

//            System.out.println("ya hizo el connecctoutputtt");

//            System.out.println("httpConn.getLocalCertificates(): " + httpConn.getLocalCertificates());

//Write the content of the request to the outputstream of the HTTP Connection.
            out.write(b);
            out.close();
//Ready with sending the request.
//            System.out.println("cerro los out");
//Read the response.
            InputStreamReader isr
                    = new InputStreamReader(httpConn.getInputStream());
//            System.out.println("auiq hace getInputStream");
            BufferedReader in = new BufferedReader(isr);

//Write the SOAP message response to a String.
            while ((responseString = in.readLine()) != null) {
                outputString = outputString + responseString;
            }
//Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.
//            System.out.println("antes del parsexml");

            if (outputString.contains("Rejected")) {
                throw new Exception("NO SE CONECTO: " + outputString);
            }

//            Document document = parseXmlFile(outputString);
//            NodeList nodeLst = document.getElementsByTagName("RespuestaAutorizacionComprobante");
//            String weatherResult = nodeLst.item(0).getTextContent();
//            System.out.println("Weather: " + weatherResult);

//            System.out.println("outputString: " + outputString);

            //esto es cuando no encuentra comprobantes con la clave de acceso consultada
            if(outputString.contains("<numeroComprobantes>0</numeroComprobantes>")){
                throw new Exception("No existe comprobante con la clave consultada");
            }

//Write the SOAP message formatted to the console.
            String formattedSOAPResponse = formatXML(outputString);
//            System.out.println(formattedSOAPResponse);

            
            
//            Document comprob = parseXmlFile(formattedSOAPResponse);
//            comprob.
//return weatherResult;
//httpConn.disconnect();

            return formattedSOAPResponse;
            
        } catch (Exception exc) {
            if(!exc.getMessage().contains("No existe comprobante con la clave consultada"))
                LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
        finally{
            //httpConn.disconnect();
        }
    }

    //format the XML in your String
    private String formatXML(String unformattedXml) {
        try {
            Document document = parseXmlFile(unformattedXml);
//            OutputFormat format = new OutputFormat(document);
//            format.setIndenting(true);
//            format.setIndent(3);
//            format.setOmitXMLDeclaration(true);
//            Writer out = new StringWriter();
//            XMLSerializer serializer = new XMLSerializer(out, format);
//            serializer.serialize(document);
//            return out.toString();


//aqui se obtiene desde que tag se quiere obtener el xml, para no mostrar todo como en el soap
            NodeList nodeLst = document.getElementsByTagName("RespuestaAutorizacionComprobante");
            
            
            DOMSource domSource = new DOMSource(nodeLst.item(0));
       StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
       transformer.transform(domSource, result);
       
       return writer.toString();
            
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private Document parseXmlFile(String in) {
        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    
      
/*      public void prueba2(List<ArchivoSriDTO> lista) {
        try {
            System.out.println("props: "
                    + System.getProperty("javax.net.ssl.keyStore"));
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs,
                        String authType) {
                }
            }}; // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null,
                    trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); //Create all-trusting host name verifier 
                    HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname,SSLSession session) {
                    return true;
                }
            }; // Install the all-trusting host verifier 
                    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            //Code to make a webservice HTTP request String responseString = "";
            String outputString = "";
            String wsURL
                    = "https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline";
            URL url = new URL(wsURL);
            URLConnection connection
                    = url.openConnection();
            HttpsURLConnection httpConn = (HttpsURLConnection) connection;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            String xmlInput = "
      <soapenv:
            Envelope 
            xmlns:soapenv =
            \"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ec=\"http://ec.gob.sri.ws.autorizacion\">\n"
      + "   <soapenv:Header/>\n" + "   <soapenv:Body>\n" + "
           <ec:
            autorizacionComprobante >\n
            " + "         <!--Optional:
            -- >\n" + "
              <claveAccesoComprobante > "+lista.get(0).getClaveAcceso()+" <  / claveAccesoComprobante >\n
            "
      + "      </ec:autorizacionComprobante>\n" + "   </soapenv:Body>\n" +
      "</soapenv:Envelope>"; byte[] buffer = new byte[xmlInput.length()];
            buffer = xmlInput.getBytes();
            bout.write(buffer);
            byte[] b
                    = bout.toByteArray();
            String SOAPAction = ""; // Set the appropriate HTTP
            parameters.httpConn.setRequestProperty("Content-Length",
                    String.valueOf(b.length));
            httpConn.setRequestProperty("Content-Type",
                    "text/xml; charset=utf-8");
            httpConn.setRequestProperty("SOAPAction",
                    SOAPAction); //
            httpConn.setRequestProperty("Access-Control-Allow-Origin", "");
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            OutputStream out = httpConn.getOutputStream();
            //Write the content of the request to the outputstream of the HTTP
            Connection.out.write(b);
            out.close(); //Ready with sending the request.
            //Read the response. InputStreamReader isr = new
            InputStreamReader(httpConn.getInputStream());
            BufferedReader in = new BufferedReader(isr); //Write the SOAP message response to a String. while
            ((responseString = in.readLine()) != null) {
                outputString = outputString
                        + responseString;
            } //Parse the String output to a org.w3c.dom.Document
            and be able to reach every node with the org
            .w3c.dom API
            . Document document = parseXmlFile(outputString);
            NodeList nodeLst
                    = document.getElementsByTagName("RespuestaAutorizacionComprobante");
            String weatherResult = nodeLst.item(0).getTextContent(); //Write the SOAP
            message formatted to the console.String formattedSOAPResponse
                    = formatXML(outputString);
            System.out.println(formattedSOAPResponse);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
*/
     
}
