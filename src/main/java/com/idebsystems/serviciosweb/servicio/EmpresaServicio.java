/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import com.idebsystems.serviciosweb.dao.EmpresaDAO;
import com.idebsystems.serviciosweb.dto.EmpresaDTO;
import com.idebsystems.serviciosweb.entities.Empresa;
import com.idebsystems.serviciosweb.mappers.EmpresaMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jorge
 */
public class EmpresaServicio {
 
    private static final Logger LOGGER = Logger.getLogger(EmpresaServicio.class.getName());
    
    private final EmpresaDAO dao = new EmpresaDAO();
    
    public List<EmpresaDTO> listarEmpresas() throws Exception {
        try {
            List<EmpresaDTO> lista = new ArrayList();
            
            List<Empresa> data = dao.listarEmpresas();
            
            data.forEach(ent ->{
                EmpresaDTO dto = EmpresaMapper.INSTANCE.entityToDto(ent);
                lista.add(dto);
            });

            return lista;
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    public EmpresaDTO guardarEmpresa(EmpresaDTO dto) throws Exception {
        try{
            Empresa ent = EmpresaMapper.INSTANCE.dtoToEntity(dto);
            ent.setFechaModifica(new Date());
            Empresa resp = dao.guardarEmpresa(ent);
            dto = EmpresaMapper.INSTANCE.entityToDto(resp);
            return dto;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    public List<EmpresaDTO> listarEmpresasPorRol(long idRol) throws Exception {
        try {
            List<EmpresaDTO> lista = new ArrayList();
            
            List<Empresa> data = dao.listarEmpresasPorRol(idRol);
            
            data.forEach(ent ->{
                EmpresaDTO dto = EmpresaMapper.INSTANCE.entityToDto(ent);
                lista.add(dto);
            });

            return lista;
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
