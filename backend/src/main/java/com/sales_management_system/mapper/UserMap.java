package com.sales_management_system.mapper;

import com.sales_management_system.controller.dto.UserDTO;
import com.sales_management_system.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMap {
    User fromDTO(UserDTO dto);

    UserDTO fromUser(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateFromDTO(UserDTO dto, @MappingTarget User user);
}
