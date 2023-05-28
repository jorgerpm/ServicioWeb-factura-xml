/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.mappers;

import com.idebsystems.serviciosweb.dto.FacturaFisicaDTO;
import com.idebsystems.serviciosweb.entities.FacturaFisica;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author jorge
 */
@Mapper
public interface FacturaFisicaMapper {
    FacturaFisicaMapper INSTANCE = Mappers.getMapper(FacturaFisicaMapper.class);
            
    FacturaFisicaDTO entityToDto(FacturaFisica entity);
    FacturaFisica dtoToEntity(FacturaFisicaDTO dto);
}
