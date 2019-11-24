package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.entity.Cliente;
import com.example.demo.models.service.IClienteService;

@RestController // tiene el controller + el responseBody para json y xml
@RequestMapping("/api/clientes")
public class ClienteRestController {
	
	@Autowired
	private IClienteService iClienteService;

	@GetMapping(value = "/listar")
	public List<Cliente> listar(){
		return iClienteService.findAll();
	}
}
