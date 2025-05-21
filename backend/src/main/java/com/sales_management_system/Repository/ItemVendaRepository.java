package com.sales_management_system.Repository;

import com.sales_management_system.entity.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, UUID> {
}
