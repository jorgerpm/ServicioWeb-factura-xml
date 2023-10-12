/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.idebsystems.serviciosweb.servicio;

import com.idebsystems.serviciosweb.dao.ParametroDAO;
import com.idebsystems.serviciosweb.dto.ParametroDTO;
import com.idebsystems.serviciosweb.entities.Parametro;
import com.idebsystems.serviciosweb.mappers.ParametroMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author israe
 */
public class ParametroServicio {
    private static final Logger LOGGER = Logger.getLogger(ParametroServicio.class.getName());
    
    private final ParametroDAO dao = new ParametroDAO();
    
    public List<ParametroDTO> listarParametros() throws Exception {
        try {
            List<ParametroDTO> listaParametroDto = new ArrayList<ParametroDTO>();
            
            List<Parametro> listaParametro = dao.listarParametros();
            
            listaParametro.forEach(parametro->{
                ParametroDTO parametroDto = new ParametroDTO();
                parametroDto = ParametroMapper.INSTANCE.entityToDto(parametro);
                listaParametroDto.add(parametroDto);
            });

            return listaParametroDto;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    public ParametroDTO guardarParametro(ParametroDTO parametroDto) throws Exception {
        try{
            Parametro parametro = ParametroMapper.INSTANCE.dtoToEntity(parametroDto);
            parametro.setFechaModifica(new Date());
            Parametro parametroRespuesta = dao.guardarParametro(parametro);
            parametroDto = ParametroMapper.INSTANCE.entityToDto(parametroRespuesta);
            return parametroDto;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
