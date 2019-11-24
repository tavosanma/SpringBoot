package com.example.demo.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.ICliente;
import com.example.demo.models.dao.IFacturaDao;
import com.example.demo.models.dao.IProductoDao;
import com.example.demo.models.entity.Cliente;
import com.example.demo.models.entity.Factura;
import com.example.demo.models.entity.Producto;

@Service
public class ClienteServiceImplement implements IClienteService {

	@Autowired
	private ICliente clienteDao;
	
	@Autowired
	private IProductoDao iProductoDao;
	
	@Autowired
	private IFacturaDao iFacturaDao;
	
	@Override
	@Transactional(readOnly=true) // solo de lectura por que es una consulta
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}

	@Override
	@Transactional  // transaccion de escritura
	public void save(Cliente cliente) {
		clienteDao.save(cliente);
		
	}

	@Override
	@Transactional(readOnly=true)
	public Cliente findOne(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);
	
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

	@Override
	public List<Producto> findByNombre(String term) {
		
		// return iProductoDao.findByNombre(term);
		return iProductoDao.findByNombreLikeIgnoreCase("%"+term+"%");
		
	}

	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		 iFacturaDao.save(factura);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(Long id) {
		return iProductoDao.findById(id).orElse(null);
	}

	@Override
	public Factura findFacturaById(Long id) {
		return iFacturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteFactura(Long id) {
		iFacturaDao.deleteById(id);
		
	}

	@Override
	@Transactional
	public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id) {
		return iFacturaDao.fetchByIdWithClienteWithItemFacturaWithProducto(id);
	}

	@Override
	public Cliente fetchByIdWithFacturas(Long id) {
		return clienteDao.fetchByIdWithFacturas(id);
	}

	
	

	
}
