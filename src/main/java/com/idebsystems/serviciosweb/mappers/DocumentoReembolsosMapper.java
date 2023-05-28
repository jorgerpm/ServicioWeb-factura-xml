/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.mappers;

import com.idebsystems.serviciosweb.dto.DocumentoReembolsosDTO;
import com.idebsystems.serviciosweb.entities.DocumentoReembolsos;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author jorge
 */
@Mapper
public interface DocumentoReembolsosMapper {
    
    DocumentoReembolsosMapper INSTANCE = Mappers.getMapper(DocumentoReembolsosMapper.class);
            
    DocumentoReembolsosDTO entityToDto(DocumentoReembolsos entity);
    DocumentoReembolsos dtoToEntity(DocumentoReembolsosDTO dto);
}
