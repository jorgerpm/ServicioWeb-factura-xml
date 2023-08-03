/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import com.idebsystems.serviciosweb.dao.FirmaDigitalDAO;
import com.idebsystems.serviciosweb.dao.UsuarioDAO;
import com.idebsystems.serviciosweb.dto.FirmaDigitalDTO;
import com.idebsystems.serviciosweb.entities.FirmaDigital;
import com.idebsystems.serviciosweb.entities.Usuario;
import com.idebsystems.serviciosweb.mappers.FirmaDigitalMapper;
import com.idebsystems.serviciosweb.mappers.UsuarioMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

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
}
