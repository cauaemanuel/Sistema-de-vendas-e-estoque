package com.sales_management_system.Repository;

import com.sales_management_system.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

}
