package com.example.demo.models.dao;

import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo.models.entity.Cliente;

//dao = data access object
public interface ICliente extends PagingAndSortingRepository<Cliente, Long> { // paging.. tiene las mismas carácteristicas que extienda de CrudRepository
	
	//left por si es que cliente no cuenta con facturas, si no se coloca , abrá error en ver los detalles de los clientes que no tienen una factura
	@Query("select c from Cliente c left join fetch c.facturas f where c.id=?1")
	public Cliente fetchByIdWithFacturas(Long id);

}
