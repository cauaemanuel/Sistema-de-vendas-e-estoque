package com.sales_management_system.mapper;

import com.sales_management_system.controller.dto.ClienteDTO;
import com.sales_management_system.entity.Cliente;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClienteMap {

    Cliente fromDTO(ClienteDTO dto);

    ClienteDTO fromClient(Cliente p);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Cliente updateFromDTO(ClienteDTO dto, @MappingTarget Cliente cliente);
}
