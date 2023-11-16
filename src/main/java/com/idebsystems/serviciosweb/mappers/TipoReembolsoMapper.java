/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.mappers;

import com.idebsystems.serviciosweb.dto.TipoReembolsoDTO;
import com.idebsystems.serviciosweb.entities.TipoReembolso;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author jorge
 */
@Mapper
public interface TipoReembolsoMapper {
    
    TipoReembolsoMapper INSTANCE = Mappers.getMapper(TipoReembolsoMapper.class);
            
    TipoReembolsoDTO entityToDto(TipoReembolso ent);
    TipoReembolso dtoToEntity(TipoReembolsoDTO dto);
}
