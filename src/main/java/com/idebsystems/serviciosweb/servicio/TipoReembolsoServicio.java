/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2023
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import com.idebsystems.serviciosweb.dao.TipoReembolsoDAO;
import com.idebsystems.serviciosweb.dao.UsuarioDAO;
import com.idebsystems.serviciosweb.dto.TipoReembolsoDTO;
import com.idebsystems.serviciosweb.entities.TipoReembolso;
import com.idebsystems.serviciosweb.entities.Usuario;
import com.idebsystems.serviciosweb.mappers.TipoReembolsoMapper;
import com.idebsystems.serviciosweb.mappers.UsuarioMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jorge
 */
public class TipoReembolsoServicio {
    
    private static final Logger LOGGER = Logger.getLogger(TipoReembolsoServicio.class.getName());
    
    private final TipoReembolsoDAO dao = new TipoReembolsoDAO();
    
    public List<TipoReembolsoDTO> listarTipoReembolso(String esPrincipal) throws Exception {
        try {
            List<TipoReembolsoDTO> listaDto = new ArrayList();
            
            List<TipoReembolso> data = dao.listarTipoReembolso(esPrincipal);
            
            UsuarioDAO userDao = new UsuarioDAO();
            
            
            data.forEach(ent ->{
                try{
                    TipoReembolsoDTO dto = TipoReembolsoMapper.INSTANCE.entityToDto(ent);

                    //agregar el usuario
                    Usuario usuario = userDao.buscarUsuarioPorId(ent.getIdUsuarioModifica());
                    dto.setUsuario(UsuarioMapper.INSTANCE.entityToDto(usuario));

                    listaDto.add(dto);
                    
                }catch(Exception exc){
                    LOGGER.log(Level.SEVERE, null, exc);
                }
            });

            return listaDto;
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    public TipoReembolsoDTO guardarTipoReembolso(TipoReembolsoDTO dto) throws Exception {
        try{
            TipoReembolso ent = TipoReembolsoMapper.INSTANCE.dtoToEntity(dto);
            ent.setFechaModifica(new Date());
            TipoReembolso respuesta = dao.guardarTipoReembolso(ent);
            dto = TipoReembolsoMapper.INSTANCE.entityToDto(respuesta);
            return dto;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    /**este metodo obtiene el siguiente numero de reemboslo, y lo 
     * actualiza solamente si se pasa true en campo actualiza
     * @param id
     * @param actualizar 
     * @return 
     * @throws java.lang.Exception
     */
    public String generarNumeroReembolso(Long id, boolean actualizar) throws Exception {
        try{
            
            TipoReembolso tr = dao.getTipoReembolsoPorId(id);
            long sec = tr.getSecuencial() + 1;
            String numero = sec + "-" + tr.getNomenclatura();
            
            if(actualizar){
                tr.setSecuencial(sec);

                dao.guardarTipoReembolso(tr);
            }
            
            return numero;
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
