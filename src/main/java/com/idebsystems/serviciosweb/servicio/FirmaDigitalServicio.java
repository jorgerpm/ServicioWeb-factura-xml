/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import com.idebsystems.serviciosweb.dao.FirmaDigitalDAO;
import com.idebsystems.serviciosweb.dao.UsuarioDAO;
import com.idebsystems.serviciosweb.dto.FirmaDigitalDTO;
import com.idebsystems.serviciosweb.dto.ReporteDTO;
import com.idebsystems.serviciosweb.entities.FirmaDigital;
import com.idebsystems.serviciosweb.entities.Usuario;
import com.idebsystems.serviciosweb.mappers.FirmaDigitalMapper;
import com.idebsystems.serviciosweb.mappers.UsuarioMapper;
import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author jorge
 */
public class FirmaDigitalServicio {
    
    private static final Logger LOGGER = Logger.getLogger(FirmaDigitalServicio.class.getName());
    
    private final FirmaDigitalDAO dao = new FirmaDigitalDAO();
    
    public List<FirmaDigitalDTO> listarFirmas(long idUsuario, long idRol) throws Exception {
        try {
            List<FirmaDigitalDTO> lista = new ArrayList<>();
            
            List<FirmaDigital> data;
            if(idRol == 1){//para el administrador
                data = dao.listarFirmas();
            }
            else{
                data = dao.listarFirmasPorUsuario(idUsuario);
            }
            
            UsuarioDAO usdao = new UsuarioDAO();
            List<Usuario> uss = usdao.listarUsuarios();
                    
            data.forEach(firma->{
                FirmaDigitalDTO dto = FirmaDigitalMapper.INSTANCE.entityToDto(firma);
                dto.setFechaCaducaLong(firma.getFechaCaduca().getTime());
                
                Usuario user = uss.stream().filter(u -> Objects.equals(u.getId(), firma.getIdUsuario())).findAny().orElse(new Usuario());
                dto.setUsuario(UsuarioMapper.INSTANCE.entityToDto(user));
                
                lista.add(dto);
            });

            return lista;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    public FirmaDigitalDTO guardarFirmaDigital(FirmaDigitalDTO dto) throws Exception {
        try{
            FirmaDigital ent = FirmaDigitalMapper.INSTANCE.dtoToEntity(dto);
            ent.setFechaCaduca(new Date(dto.getFechaCaducaLong()));
            FirmaDigital respuesta = dao.guardarFirmaDigital(ent);
            FirmaDigitalDTO firmaDto = FirmaDigitalMapper.INSTANCE.entityToDto(respuesta);
            return firmaDto;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    public FirmaDigitalDTO getFirmaActivaPorIdUsuario(Long idUsuario, boolean esLogin) throws Exception {
        try {
            
            FirmaDigital ent = dao.getFirmaActivaPorIdUsuario(idUsuario);
            FirmaDigitalDTO dto = FirmaDigitalMapper.INSTANCE.entityToDto(ent);
            if(Objects.isNull(dto)){
                if(esLogin) return null;
                throw new Exception("NO EXISTE FIRMA DIGITAL REGISTRADA PARA EL USUARIO");
            }
            
            UsuarioDAO usdao = new UsuarioDAO();
            Usuario user = usdao.buscarUsuarioPorId(dto.getIdUsuario());
            dto.setUsuario(UsuarioMapper.INSTANCE.entityToDto(user));
            
            dto.setFechaCaducaLong(ent.getFechaCaduca().getTime());
            return dto;
       } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
        }
    }
    
    public boolean solicitarClaveFirma(Long idUsuario) throws Exception {
        try {
            
            FirmaDigital ent = dao.getFirmaActivaPorIdUsuario(idUsuario);
            
            if(Objects.isNull(ent)){
                throw new Exception("NO EXISTE FIRMA DIGITAL REGISTRADA PARA EL USUARIO");
            }
            
            return ent.getTipoFirma() == 0;
            
       } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
        }
    }
    
    
    public FirmaDigitalDTO validarFirmaNueva(FirmaDigitalDTO dto) throws Exception {
        FirmaDigitalDTO firmaDto = new FirmaDigitalDTO();
        try{
            
            if(Objects.nonNull(dto.getArchivo()) && !dto.getArchivo().isEmpty() && 
                    Objects.nonNull(dto.getClave()) && !dto.getClave().isEmpty()){
                
                byte[] biteArchivo = Base64.getDecoder().decode(dto.getArchivo());
                
                KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
                ks.load(new ByteArrayInputStream(biteArchivo), dto.getClave().toCharArray());
                String alias = (String) ks.aliases().nextElement();
                PrivateKey pk = (PrivateKey) ks.getKey(alias, dto.getClave().toCharArray());
                
                Certificate[] chain = null;
                BouncyCastleProvider bcp = new BouncyCastleProvider();
                Security.addProvider(bcp);
                chain = ks.getCertificateChain(alias);

                X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
            
                if(cert.getNotAfter().before(new Date())){
                    LOGGER.log(Level.INFO, "la fecha de la firma es menor a la fecha actual: ya caduco");
                    firmaDto.setRespuesta("LA FIRMA YA CADUC\u00d3. VERIFIQUE LA FECHA DE CADUCIDAD DE LA MISMA.");
                }
                else{
                    firmaDto.setRespuesta("OK");
                    firmaDto.setFechaCaducaLong(cert.getNotAfter().getTime());
                }
            }
            else{
                firmaDto.setRespuesta("DEBE INGRESAR EL ARCHIVO Y LA CLAVE");
            }
            
            return firmaDto;
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            firmaDto.setRespuesta(exc.getMessage());
            if(exc.getMessage().contains("wrong password") || exc.getMessage().contains("keystore password was incorrect")){
                firmaDto.setRespuesta("LA CLAVE INGRESADA ES INCORRECTA");
            }
            
            return firmaDto;
        }
    }
    
    
    public String validarTodoFirma(long idUsuario, String claveFirma) throws Exception {
        try {
            String respuesta = "";
            FirmaDigitalDTO dto = null;
            try{
                dto = getFirmaActivaPorIdUsuario(idUsuario, false);
            }catch(Exception exc){
                respuesta = exc.getMessage() != null ? exc.getMessage().replace("java.​lang.Exception", "") : exc.getMessage();
            }
            if(Objects.nonNull(dto)){
                dto.setClave(claveFirma);
                FirmaDigitalDTO resdto = validarFirmaNueva(dto);
                if(!resdto.getRespuesta().equalsIgnoreCase("OK")){
                    respuesta = resdto.getRespuesta();
                }
            }
            
            return respuesta;
            
       } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
        }
    }
}
