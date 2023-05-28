/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.mappers;

import com.idebsystems.serviciosweb.dto.FirmaDigitalDTO;
import com.idebsystems.serviciosweb.entities.FirmaDigital;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author jorge
 */
@Mapper
public interface FirmaDigitalMapper {
    
    FirmaDigitalMapper INSTANCE = Mappers.getMapper(FirmaDigitalMapper.class);
            
    FirmaDigitalDTO entityToDto(FirmaDigital entity);
    FirmaDigital dtoToEntity(FirmaDigitalDTO dto);
}
