/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.mappers;

import com.idebsystems.serviciosweb.dto.MenuRolDTO;
import com.idebsystems.serviciosweb.entities.MenuRol;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author israe
 */
@Mapper
public interface MenuRolMapper {
    MenuRolMapper INSTANCE = Mappers.getMapper(MenuRolMapper.class);
            
    MenuRolDTO entityToDto(MenuRol menuRol);
    MenuRol dtoToEntity(MenuRolDTO menuRolDto);
}
