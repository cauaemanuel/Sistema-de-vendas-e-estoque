package com.sales_management_system.mapper;

import com.sales_management_system.controller.dto.ProdutoDTO;
import com.sales_management_system.entity.Produto;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProdutoMap {


    Produto fromDTO(ProdutoDTO dto);

    ProdutoDTO fromProduto(Produto produto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Produto updateFromDTO(ProdutoDTO dto, @MappingTarget Produto produto);
}
