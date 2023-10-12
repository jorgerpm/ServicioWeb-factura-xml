/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.pdf.qrcode.BitMatrix;
import com.itextpdf.text.pdf.qrcode.QRCodeWriter;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import java.security.*;
import java.io.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.file.Files;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import org.bouncycastle.jce.provider.BouncyCastleProvider;



import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.imageio.ImageIO;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.jce.PrincipalUtil;
//import com.itextpdf.barcodes.BarcodeQRCode;
//import com.itextpdf.kernel.colors.ColorConstants;
//import com.itextpdf.kernel.geom.Rectangle;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfPage;
//import com.itextpdf.kernel.pdf.PdfReader;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.kernel.pdf.WriterProperties;
//import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
//import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
//import com.itextpdf.layout.Canvas;
//import com.itextpdf.layout.element.Image;

/**
 *
 * @author jorge
 */
public class Firmar {
    
     
     
    public static void main1(String[] args) {
        try {
 
            KeyStore ks = KeyStore.getInstance("pkcs12");
            
            ks.load(new FileInputStream("/home/jorge/Downloads/Persona-Natural-1717667842.p12"), "Jorge0210074*".toCharArray());
            
            String alias = (String)ks.aliases().nextElement();
            
            PrivateKey key = (PrivateKey)ks.getKey(alias, "Jorge0210074*".toCharArray());
            
            
            Certificate[] chain = ks.getCertificateChain(alias);
            
            // Recibimos como parámetro de entrada el nombre del archivo PDF a firmar
            com.lowagie.text.pdf.PdfReader reader = new com.lowagie.text.pdf.PdfReader("/home/jorge/Desktop/hola.pdf"); 
            FileOutputStream fout = new FileOutputStream("/home/jorge/Desktop/prueba.pdf");
 
            // Añadimos firma al documento PDF
            PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
            PdfSignatureAppearance sap = stp.getSignatureAppearance();
            sap.setCrypto(key, chain, null, PdfSignatureAppearance.SELF_SIGNED);


            sap.setReason("yo el autor");
        sap.setLocation("LisBon");

            // Añade la firma visible. Podemos comentarla para que no sea visible.
            sap.setVisibleSignature(new com.lowagie.text.Rectangle(200,100,300,200), 1, "sig");
//            sap.setImage(Image.getInstance("/home/jorge/nifa/carga xml/firma.gif"));
//            sap.setSignatureGraphic(Image.getInstance("/home/jorge/nifa/carga xml/firma.gif"));
//            sap.setCertificationLevel(0);
//            sap.setAcro6Layers(false);
//            sap.setLayer2Text("xxxx");
//            sap.setLayer4Text("");
//            sap.setProvider("SunJSSE");
//             sap.setImageScale(0.1f);
//sap.getLayer(2).addImage(Image.getInstance("/home/jorge/nifa/carga xml/firma.gif"), 1,2,3,4,5,6);

//sap.getLayer(2).restoreState();
//sap.getLayer(4).setFontAndSize(BaseFont.createFont(), 15);
//sap.getLayer(4).showText("xxx");
//ColumnText ct = new ColumnText(layer2);
//        ct.setRunDirection(PdfWriter.RUN_DIRECTION_DEFAULT);
//        ct.setAlignment(Element.ALIGN_MIDDLE);
//        ct.setSimpleColumn(new Phrase("esta es una prueba", new Font()), 200, 100, 300, 200,
//                300, Element.ALIGN_MIDDLE);
//        ct.go();


             
             sap.setExternalDigest(new byte[522], null, "RSA");
             sap.preClose();
             
             PdfPKCS7 sig = sap.getSigStandard().getSigner();
             Signature sign = Signature.getInstance("SHA1withRSA");
             sign.initSign(key);
             byte buf[] = new byte[8192];
             int n;
             InputStream inp = sap.getRangeStream();
             while((n = inp.read(buf)) > 0){
                 sign.update(buf, 0, n);
             }
             
             sig.setExternalDigest(sign.sign(), null, "RSA");
             PdfDictionary dic = new PdfDictionary();
             dic.put(PdfName.CONTENTS, new PdfString(sig.getEncodedPKCS1()).setHexWriting(true));
             sap.close(dic);
             

//stp.close();
            
           // aprobar();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
 
    }
    
    public static void main(String arg[]){
        try{
        Certificate[] chain = null;
            BouncyCastleProvider bcp = new BouncyCastleProvider();
            Security.addProvider(bcp);
//            Security.insertProviderAt(bcp, 1);

            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());



            ks.load(new FileInputStream("/home/jorge/Downloads/Persona-Natural-1717667842.p12"), "Jorge0210074*".toCharArray());
            String alias = (String) ks.aliases().nextElement();
            PrivateKey pk = (PrivateKey) ks.getKey(alias, "Jorge0210074*".toCharArray());
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
            com.itextpdf.text.pdf.PdfReader reader = new com.itextpdf.text.pdf.PdfReader("/home/jorge/Desktop/hola.pdf");

//            File filepout = Files.createTempFile("signed", ".pdf").toFile();
FileOutputStream fout = new FileOutputStream("/home/jorge/Desktop/prueba.pdf");
//            FileOutputStream os = new FileOutputStream(filepout);
            com.itextpdf.text.pdf.PdfStamper stamper
                    = com.itextpdf.text.pdf.PdfStamper.createSignature(reader, fout, '\0');
            
            
            
            crearQr("Firmado por: "+ PrincipalUtil.getSubjectX509Principal(cert).getValues(PdfPKCS7.X509Name.CN).get(0)
                    + "\r\nFecha: "+new Date()
                    +"\r\nRazon: Firma personal de documentos electronicos. "
                    + "\r\nEmisor: "+cert.getIssuerDN().getName()
                    + "\r\nValida hasta: " + cert.getNotAfter());
            com.itextpdf.text.Image imagen = com.itextpdf.text.Image.getInstance("/home/jorge/Desktop/demo.png");
            imagen.setAbsolutePosition(10, 55); //X Y
            imagen.scaleAbsolute(75, 75);//ANCHO XALTO
            
            com.itextpdf.text.pdf.PdfContentByte content = stamper.getOverContent(reader.getNumberOfPages());
            content.addImage(imagen);
            
            
            
            // Creating the appearance
            com.itextpdf.text.pdf.PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            System.out.println("appearance.getContact(): " + PrincipalUtil.getSubjectX509Principal(cert).getValues(PdfPKCS7.X509Name.CN).get(0)); 
            
            System.out.println("appearance.getFieldName(): "+DateFormat.getInstance().format(appearance.getSignDate().getTime())); 
            
//            appearance.setReasonCaption("RAZON: ");
//            appearance.setReason("Firma digital personal");
//            appearance.setLocation(cert.getIssuerDN().getName()
//                    // + "\r\nSigner ID not before: " + cert.getNotBefore()
//                    + "\r\nSigner ID not after: " + cert.getNotAfter());
            
//            appearance.getLayer(2).setFontAndSize(com.itextpdf.text.pdf.BaseFont.createFont(), 15);
            appearance.setLayer2Text("Firmado por: "+ PrincipalUtil.getSubjectX509Principal(cert).getValues(PdfPKCS7.X509Name.CN).get(0)
            +"\r\nFecha: "+DateFormat.getInstance().format(appearance.getSignDate().getTime()));
//            appearance.getLayer(2).newlineShowText(cert.getSubjectDN().getName());
            
            
            //left,bottom,Right,top
            com.itextpdf.text.Rectangle rectangle = new com.itextpdf.text.Rectangle(65, 55, 240, 140);
            //para cuando se firma la segunda vez, debe cambiar de posicion
            

            System.out.println("top: " + rectangle.getTop());
            System.out.println("bottom: " + rectangle.getBottom());
            System.out.println("right: " + rectangle.getRight());
            System.out.println("left: " + rectangle.getLeft());


            String fieldSig = "sig";
            

            appearance.setVisibleSignature(rectangle, reader.getNumberOfPages(), fieldSig);

            // Creating the signature
            ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, "BC");//SunJSSE
            ExternalDigest digest = new BouncyCastleDigest();
            MakeSignature.signDetached(appearance, digest, pks, chain,
                    null, null, null, 0, null);
            reader.close();

            
            
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }
     
     
    public static void aprobar() {
        try {
  
            Random rnd = new Random();
            KeyStore kall = PdfPKCS7.loadCacertsKeyStore();
            PdfReader reader = new PdfReader("/home/jorge/Desktop/prueba.pdf");
            AcroFields af = null;//reader.getAcroFields();
            ArrayList names = af.getSignatureNames();
            for (int k = 0; k < names.size(); ++k) {
               String name = (String)names.get(k);
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
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main3(String arg[]){
        try{
            
//            String url = "https://itextpdf.com/";
//    
//    PdfReader reader = new PdfReader("/home/jorge/Desktop/hola.pdf");
//    PdfWriter writer = new PdfWriter("/home/jorge/Desktop/prueba.pdf", new WriterProperties());
//    PdfDocument pdfDocument = new PdfDocument(reader, writer);
//    
//    // Adding QRCode to first page
//    PdfPage pdfPage = pdfDocument.getFirstPage();
//    PdfCanvas pdfCanvas = new PdfCanvas(pdfPage);
//    Rectangle rect = new Rectangle(100, 600, 100, 100);
//    
//    BarcodeQRCode barcodeQRCode = new BarcodeQRCode(url);
//    PdfFormXObject pdfFormXObject =
//        barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
//    Image qrCodeImage =
//        new Image(pdfFormXObject).setWidth(rect.getWidth()).setHeight(rect.getHeight());
//    
//    Canvas qrCanvas = new Canvas(pdfCanvas, rect);
//    qrCanvas.add(qrCodeImage);
//    qrCanvas.close();
//    
//    pdfDocument.close();
            
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }
    
    
    public static void crearQr(String data){
        try{
             // The data that the QR code will contain
//        String data = "www.geeksforgeeks.org";
 
        // The path where the image will get saved
        String path = "/home/jorge/Desktop/demo.png";
 
        // Encoding charset
        String charset = "UTF-8";
 
            
        
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap
            = new HashMap<EncodeHintType,
                          ErrorCorrectionLevel>();
 
        hashMap.put(EncodeHintType.ERROR_CORRECTION,
                    ErrorCorrectionLevel.L);
        
            com.google.zxing.common.BitMatrix matrix = new MultiFormatWriter().encode(
            new String(data.getBytes(charset), charset),
            BarcodeFormat.QR_CODE, 125, 125);
 
        MatrixToImageWriter.writeToFile(
            matrix,
            path.substring(path.lastIndexOf('.') + 1),
            new File(path));
        
            
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }
}
