/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.idebsystems.serviciosweb.mappers;

import com.idebsystems.serviciosweb.dto.RazonRechazoDTO;
import com.idebsystems.serviciosweb.entities.RazonRechazo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author israe
 */
@Mapper
public interface RazonRechazoMapper {
    RazonRechazoMapper INSTANCE = Mappers.getMapper(RazonRechazoMapper.class);
            
    RazonRechazoDTO entityToDto(RazonRechazo razonRechazo);
    RazonRechazo dtoToEntity(RazonRechazoDTO razonRechazoDto);
}
