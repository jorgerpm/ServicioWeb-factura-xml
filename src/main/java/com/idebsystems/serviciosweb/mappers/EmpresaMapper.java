/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.mappers;

import com.idebsystems.serviciosweb.dto.EmpresaDTO;
import com.idebsystems.serviciosweb.entities.Empresa;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author jorge
 */
@Mapper
public interface EmpresaMapper {
    
    EmpresaMapper INSTANCE = Mappers.getMapper(EmpresaMapper.class);
            
    EmpresaDTO entityToDto(Empresa empresa);
    Empresa dtoToEntity(EmpresaDTO empresaDto);
    
}
