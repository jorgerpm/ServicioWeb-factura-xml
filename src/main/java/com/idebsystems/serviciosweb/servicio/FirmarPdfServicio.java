/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import com.idebsystems.serviciosweb.dto.FirmaDigitalDTO;
import com.idebsystems.serviciosweb.dto.ParametroDTO;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.lowagie.text.Element;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author jorge
 */
public class FirmarPdfServicio {

    private static final Logger LOGGER = Logger.getLogger(FirmarPdfServicio.class.getName());

    public byte[] firmarConDigital(byte[] archivoAFirmar, FirmaDigitalDTO firmaDigital, boolean segundaFirma, String claveFirma,
            boolean terceraFirma) throws Exception {
        try {

            Certificate[] chain = null;
            BouncyCastleProvider bcp = new BouncyCastleProvider();
            Security.addProvider(bcp);
//            Security.insertProviderAt(bcp, 1);

            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

//            byte[] firmaBytes = DatatypeConverter.parseBase64Binary(firmaDigital.getArchivo());
            byte[] firmaBytes = Base64.getDecoder().decode(firmaDigital.getArchivo());

            ks.load(new ByteArrayInputStream(firmaBytes), claveFirma.toCharArray() /*firmaDigital.getClave().toCharArray()*/);
            String alias = (String) ks.aliases().nextElement();
            PrivateKey pk = (PrivateKey) ks.getKey(alias, claveFirma.toCharArray()/*firmaDigital.getClave().toCharArray()*/);
            chain = ks.getCertificateChain(alias);

            X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
            System.out.println("Signer ID serial     " + cert.getSerialNumber());
            System.out.println("Signer ID version    " + cert.getVersion());
            System.out.println("Signer ID issuer     " + cert.getIssuerDN());
            System.out.println("Signer ID not before " + cert.getNotBefore());
            System.out.println("Signer ID not after  " + cert.getNotAfter());

            String digestAlgorithm = "SHA512";
//            MakeSignature.CryptoStandard subfilter = MakeSignature.CryptoStandard.CMS;

            // Creating the reader and the stamper
            PdfReader reader = new PdfReader(archivoAFirmar);

            File filepout = Files.createTempFile("signed", ".pdf").toFile();

            FileOutputStream os = new FileOutputStream(filepout);
            PdfStamper stamper
                    = PdfStamper.createSignature(reader, os, '\0');
            // Creating the appearance
            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            appearance.setReason("Firma digital personal");
            appearance.setLocation(cert.getIssuerDN().getName()
                    // + "\r\nSigner ID not before: " + cert.getNotBefore()
                    + "\r\nSigner ID not after: " + cert.getNotAfter());

            //left,bottom,Right,top
            Rectangle rectangle = new Rectangle(65, 55, 240, 140);
            //para cuando se firma la segunda vez, debe cambiar de posicion
            if (segundaFirma) {
                rectangle = new Rectangle(365, 55, 540, 140);
            }
            if (terceraFirma) {
                rectangle = new Rectangle(615, 55, 790, 140);
            }

            System.out.println("top: " + rectangle.getTop());
            System.out.println("bottom: " + rectangle.getBottom());
            System.out.println("right: " + rectangle.getRight());
            System.out.println("left: " + rectangle.getLeft());


            String fieldSig = "sig";
            if (segundaFirma) {
                fieldSig = "sigAprroved";
            }
            if (terceraFirma) {
                fieldSig = "aprroved";
            }

            appearance.setVisibleSignature(rectangle, reader.getNumberOfPages(), fieldSig);

            // Creating the signature
            ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, "BC");//SunJSSE
            ExternalDigest digest = new BouncyCastleDigest();
            MakeSignature.signDetached(appearance, digest, pks, chain,
                    null, null, null, 0, null);
            reader.close();

            FileInputStream fis = new FileInputStream(filepout);
            
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            try {
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            byte[] bytes = bos.toByteArray();


            
//            for(Provider p : Security.getProviders()){
//                System.out.println(p.getName());
//            }
//                        Security.removeProvider("BC");
//            System.out.println(Security.getProvider("BC").getName());
//            System.out.println(Security.getProvider("BC"));
            
            return bytes;

        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            if(exc.getMessage().contains("wrong password") || exc.getMessage().contains("keystore password was incorrect")){
                throw new Exception("LA CLAVE INGRESADA ES INCORRECTA");
            }
            throw new Exception(exc);
        }

    }

    public byte[] firmarConImagen(byte[] archivoAFirmar, FirmaDigitalDTO firmaDigital, boolean segundaFirma) {
        try {

            File filepout = Files.createTempFile("signed", ".pdf").toFile();
            FileOutputStream os = new FileOutputStream(filepout);

            byte[] firmaBytes = Base64.getDecoder().decode(firmaDigital.getArchivo());

            PdfReader reader = new PdfReader(archivoAFirmar);

            PdfStamper stamp = new PdfStamper(reader, os);
            PdfContentByte content = stamp.getOverContent(reader.getNumberOfPages());

            Image imagen = Image.getInstance(firmaBytes);
//            imagen.setAlignment(Image.BOTTOM);
            imagen.scaleAbsolute(150, 50);//ANCHO XALTO
            imagen.setAbsolutePosition(65, 55); //X Y
            
            if(segundaFirma){
                imagen.setAbsolutePosition(365, 55); //X Y
            }
            content.addImage(imagen);
            stamp.close();

            FileInputStream fis = new FileInputStream(filepout);
            //System.out.println(file.exists() + "!!");
            //InputStream in = resource.openStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            try {
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum); 
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            byte[] bytes = bos.toByteArray();

            return bytes;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    
    
    
    
    public byte[] agregarImagenRechazo(byte[] archivoAFirmar, String razonRechazo, String tipoReembolso) {
        try {

            File filepout = Files.createTempFile("signed", ".pdf").toFile();
            FileOutputStream os = new FileOutputStream(filepout);

            

            PdfReader reader = new PdfReader(archivoAFirmar);

            PdfStamper stamp = new PdfStamper(reader, os);
            

            //obtener la imagen desde el path
            ParametroServicio ps = new ParametroServicio();
            ParametroDTO pi = ps.listarParametros().stream().filter(p -> p.getNombre().equalsIgnoreCase("IMAGEN_RECHAZO")).findAny().orElse(new ParametroDTO());
            
//            File fileImagen = new File(pi.getValor());
            
            
//            byte[] imagenBytes = null;
            Image imagen = Image.getInstance(pi.getValor());
            
//            imagen.setAlignment(Image.BOTTOM);
//            imagen.scaleAbsolute(150, 50);//ANCHO XALTO
            imagen.setAbsolutePosition(20, 20); //X Y
            
            //se recorre la cantidad de paginas para colocar la imagen en cada pagina
            for(int i=1;i<=reader.getNumberOfPages();i++){
                PdfContentByte content = stamp.getOverContent(i);
                content.addImage(imagen);
            }
            
            
            //aca se coloca la razon de rechazo, pero solo en la ultima pagina
            int x = 20;
            int y = 140;
            if(tipoReembolso.equalsIgnoreCase("VIAJES")){
                x = 20;
                y = 340;
            }
                    
            PdfContentByte content = stamp.getOverContent(reader.getNumberOfPages());
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, "UTF-8", true);
            FontFactory.getFont(FontFactory.HELVETICA, Font.DEFAULTSIZE, Font.NORMAL).getBaseFont();
            content.setFontAndSize(baseFont, 10);
            content.beginText();
            content.showTextAligned(Element.ALIGN_LEFT, "Motivo rechazo: " + razonRechazo, x, y, 0);
            content.endText();
            //ESTA ES LA RAZON DEL RECHAZO PORQUE SI POR_AUTORIZAR
            
            stamp.close();

            FileInputStream fis = new FileInputStream(filepout);
            //System.out.println(file.exists() + "!!");
            //InputStream in = resource.openStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            try {
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum); //no doubt here is 0
                    //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
//                    System.out.println("read " + readNum + " bytes,");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            byte[] bytes = bos.toByteArray();

            return bytes;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    
    
    
    public static void comprobar() {
        try {

            /*
            Random rnd = new Random();
            KeyStore kall = PdfPKCS7.loadCacertsKeyStore();
            kall.load(new FileInputStream("/home/jorge/Downloads/Persona-Natural-1717667842.p12"), "Jorge0210074*".toCharArray());
            
            PdfReader reader = new PdfReader("/home/jorge/aqui.pdf");
            AcroFields af = reader.getAcroFields();
            
            ArrayList names = af.getSignatureNames();
            for (int k = 0; k < names.size(); ++k) {
               String name = (String)names.get(k);
                System.out.println("name: " + name);
                System.out.println("af: " +  af.getRevision(name));
               int random = rnd.nextInt();
               FileOutputStream out = new FileOutputStream("revision_" + random + "_" + af.getRevision(name) + ".pdf");
 
               byte bb[] = new byte[8192];
                InputStream ip = af.extractRevision(name);
               int n = 0;
               while ((n = ip.read(bb)) > 0)
               out.write(bb, 0, n);
               out.close();
               ip.close();
 
               PdfPKCS7 pk = af.verifySignature(name);
                System.out.println("af.verifySignature(name): " + af.verifySignature(name).getDigestAlgorithm());
                System.out.println("af.verifySignature(name): " + af.verifySignature(name).getLocation());
                System.out.println("af.verifySignature(name): " + af.verifySignature(name).getSignName());
                System.out.println("af.verifySignature(name): " + af.verifySignature(name).getSignDate().getTime());
                Calendar cal = pk.getSignDate();
               Certificate pkc[] = pk.getCertificates();
               Object fails[] = PdfPKCS7.verifyCertificates(pkc, kall, null, cal);
               if (fails == null) {
                   System.out.print(pk.getSignName());
               }
               else {
                   System.out.print("Firma no válida");
               }
                File f = new File("revision_" + random + "_" + af.getRevision(name) + ".pdf");
               f.delete();
//System.out.println("ffff: " + f.getAbsolutePath());
            }
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
