/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. (C) 2023
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.mappers;

import com.idebsystems.serviciosweb.dto.LiquidacionCompraDTO;
import com.idebsystems.serviciosweb.entities.LiquidacionCompra;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author jorge
 */
@Mapper
public interface LiquidacionCompraMapper {
    
    LiquidacionCompraMapper INSTANCE = Mappers.getMapper(LiquidacionCompraMapper.class);
            
    LiquidacionCompraDTO entityToDto(LiquidacionCompra ent);
    LiquidacionCompra dtoToEntity(LiquidacionCompraDTO dto);
}
