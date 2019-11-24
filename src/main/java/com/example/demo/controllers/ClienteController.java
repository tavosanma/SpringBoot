package com.example.demo.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.example.demo.models.entity.Cliente;
import com.example.demo.models.entity.Factura;
import com.example.demo.models.entity.ItemFactura;
import com.example.demo.models.entity.Producto;
import com.example.demo.models.service.IClienteService;
import com.example.demo.models.service.IUploadFileService;
import com.example.demo.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente") //se usa para el id, se quita el hidden id en el form
public class ClienteController {

	protected final Log logger = LogFactory.getLog(this.getClass());
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private IUploadFileService iUploadFileService;
	
	
	//metodo para ver la foto (otra forma está en la clase MvcConfig)
	@GetMapping(value="/uploads/{filename:.+}")
	@PreAuthorize("hasRole('ROLE_USER')") // @Secured("ROLE_USER") //otra forma de dar acceso a los recursos a ciertos usuarios en vés de usar antmatcher en la clase SpringSecurityConfig
	public ResponseEntity<Resource> verFoto(@PathVariable String filename){
		
		Resource recurso = null;
		try {
			recurso = iUploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"").body(recurso);
	}
	
	
	@GetMapping(value="/ver/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")//@Secured("ROLE_USER")
	public String ver(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash, Locale locale) {
		
		//Cliente cliente = clienteService.findOne(id);
		Cliente cliente = clienteService.fetchByIdWithFacturas(id); //Consulta optimizada
		if(cliente==null) {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.detalle.titulo", null, locale).concat(": ").concat(cliente.getNombre()));
		return "ver";
	}
	//para mostrar json 
	@GetMapping(value="/listar-rest")
	public @ResponseBody List<Cliente> listarRest(){
		return clienteService.findAll();
	}
	/*para mostrar en xml
	@GetMapping(value="/listar-rest")
	public @ResponseBody ClienteList listarRest(){
		return new ClienteList(clienteService.findall())  // CLiente List viene de package viewxml
	}*/
	
	
	@RequestMapping(value= {"/listar", "/"}, method=RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue="0") int page, Model model,
			Authentication authentication, HttpServletRequest request, Locale locale,
			@RequestParam(name = "format", defaultValue = "html") String format) {
		
		if(authentication != null) {
			logger.info("Hola usuario autenticado, tu username es: ".concat(authentication.getName()));
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			logger.info("Hola usuario autenticado de forma estática con: SecurityContextHolder, tu username es: ".concat(auth.getName()));
		}
		
		if(hasRole("ROLE_ADMIN")) {
			logger.info("Hola usuario ".concat(auth.getName()).concat("tienes acceso!"));
		} else {
			logger.info("Hola usuario ".concat(auth.getName()).concat(" No tienes acceso!"));
		}
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		
		if(securityContext.isUserInRole("ADMIN")) {
			logger.info("forma usando SecurityContextHolderAwareRequestWrapper: HOLA  ".concat(auth.getName()).concat("tienes acceso!"));
		} else {
			logger.info("forma usando SecurityContextHolderAwareRequestWrapper: HOLA ".concat(auth.getName()).concat(" No tienes acceso!"));
		}
		
		if(request.isUserInRole("ROLE_ADMIN")) {
			logger.info("forma usando HttpServletRequest: HOLA  ".concat(auth.getName()).concat("tienes acceso!"));
		} else {
			logger.info("forma usando HttpServletRequest: HOLA ".concat(auth.getName()).concat(" No tienes acceso!"));
		}
		
		//para mostrar todos los clientes en el xml, sino sacar el if y else y se mostrarán los 5 primeros;
		
		
			
		//se listan los clientes y se paginan de 5 registros en la página 0.. importar pageable de .domain
		Pageable pageRequest = PageRequest.of(page,5);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		
		return "listar";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')") // @Secured("ROLE_ADMIN")
	@GetMapping(value="/form")
	public String crear(Map<String, Object> model, Locale locale){ //similar a usar Model model
		
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.form.titulo.crear", null, locale));
		return "form";
	}
	@PostMapping(value="/form")
	@PreAuthorize("hasRole('ROLE_ADMIN')") // @Secured("ROLE_ADMIN")
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, 
			@RequestParam("file") MultipartFile foto, 
			RedirectAttributes flash, SessionStatus status, Locale locale) { //validar y ver si hay errores valid y binding
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo", null, locale));
			
			return "form";
		}
			if (!foto.isEmpty()) {
			//String rootPath = "C://Temp//uploads"; para que la img esté fuera del proyecto
			
				if(cliente.getId() !=null && cliente.getId() > 0 && cliente.getFoto()!=null && cliente.getFoto().length() > 0 ) {
					
					iUploadFileService.delete(cliente.getFoto());
				}
				
				String uniqueFilename = null;
				try {
					uniqueFilename = iUploadFileService.copy(foto);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				flash.addFlashAttribute("info", messageSource.getMessage("text.cliente.flash.foto.subir.success", null, locale) + "'" + uniqueFilename + "'");

				cliente.setFoto(uniqueFilename);
		}
			
			String mensajeFlash = (cliente.getId() != null) ? messageSource.getMessage("text.cliente.flash.editar.success", null, locale) : messageSource.getMessage("text.cliente.flash.crear.success", null, locale);

		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}
											
	@PreAuthorize("hasRole('ROLE_ADMIN')") // @Secured({"ROLE_ADMIN", "ROLE_USER"}) ó "hasAnyRole('ROLE_USER', 'ROLE_ADMIN')"
	@RequestMapping(value="form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String,  Object> model, RedirectAttributes flash, Locale locale) {
		
		Cliente cliente = null;
		if(id>0) {
			cliente = clienteService.findOne(id);
			if(cliente == null) {
				flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
				return "redirect:/listar";
			}
			
		}else {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.id.error", null, locale));
			return "redirect:/listar";
			
		}
		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.form.titulo.editar", null, locale));
		return "form";
	}
	
	@RequestMapping(value="eliminar/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')") // @Secured("ROLE_ADMIN")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash, Locale locale) {
		
		if(id > 0) {
			Cliente cliente = clienteService.findOne(id);
			clienteService.delete(id);
			flash.addFlashAttribute("success", messageSource.getMessage("text.cliente.flash.eliminar.success", null, locale));
			
			if(iUploadFileService.delete(cliente.getFoto())) {
				String mensajeFotoEliminar = String.format(messageSource.getMessage("text.cliente.flash.foto.eliminar.success", null, locale), cliente.getFoto());
				flash.addFlashAttribute("info", mensajeFotoEliminar);
			}
			
		}
		
		return "redirect:/listar";
	}
	
	private boolean hasRole(String role) {
		
		SecurityContext context = SecurityContextHolder.getContext();
		
		if(context == null) {
			return false;
		}
		Authentication auth = context.getAuthentication();
		
		if(auth == null) {
			return false;
		}
		
		//?= un generico , que puede ser cualquier objeto
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		
		/*for(GrantedAuthority authority: authorities) {
			if(role.equals(authority.getAuthority())) {
				logger.info("Hola usuario ".concat(auth.getName()).concat("tu Rol es: ".concat(authority.getAuthority())));
				return true;
			}
		}
		return false;*/
		// otra forma:
		return authorities.contains(new SimpleGrantedAuthority(role));
	}
	
	@GetMapping(value="/consumirJson")
	public String consumirJson(Model model, RestTemplate restTemplate) {
		
		Cliente[] clientes = restTemplate.getForObject("http://localhost:8080/api/clientes/listar", Cliente[].class);
		model.addAttribute("clientes", clientes);
		return "consumirJson";
	}
	
	
}
