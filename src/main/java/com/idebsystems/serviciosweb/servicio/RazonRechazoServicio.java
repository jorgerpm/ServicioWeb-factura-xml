/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.idebsystems.serviciosweb.servicio;

import com.idebsystems.serviciosweb.dao.RazonRechazoDAO;
import com.idebsystems.serviciosweb.dto.RazonRechazoDTO;
import com.idebsystems.serviciosweb.entities.RazonRechazo;
import com.idebsystems.serviciosweb.mappers.RazonRechazoMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author israe
 */
public class RazonRechazoServicio {
    
    private static final Logger LOGGER = Logger.getLogger(RazonRechazoServicio.class.getName());
    
    private final RazonRechazoDAO dao = new RazonRechazoDAO();
    
    public List<RazonRechazoDTO> listarRazonesRechazo(boolean estado) throws Exception {
        try {
            List<RazonRechazoDTO> listaRazonesDto = new ArrayList();
            
            List<RazonRechazo> listaRazones = dao.listarRazonesRechazo(estado);
            
            listaRazones.forEach(ent ->{
                RazonRechazoDTO dto = RazonRechazoMapper.INSTANCE.entityToDto(ent);
                listaRazonesDto.add(dto);
            });

            return listaRazonesDto;
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    public RazonRechazoDTO guardarRazonRechazo(RazonRechazoDTO razonDto) throws Exception {
        try{
            RazonRechazo razon = RazonRechazoMapper.INSTANCE.dtoToEntity(razonDto);
            razon.setFechaModifica(new Date());
            RazonRechazo razonRespuesta = dao.guardarRazonRechazo(razon);
            razonDto = RazonRechazoMapper.INSTANCE.entityToDto(razonRespuesta);
            return razonDto;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
