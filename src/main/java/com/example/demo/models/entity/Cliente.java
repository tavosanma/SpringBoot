package com.example.demo.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity // será una tabla en la bd
@Table(name = "clientes")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cliente implements Serializable {

	@Id // clave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // id autoincremental
	private Long id;

	// @Column(name="nombre_cliente") para cambiar el valor en la tabla, longitud,
	// null, etc
	@NotEmpty // cambio no vacío, se valida en el formulario
	@Size(min=4, max=12) // rango de carácateres en el campo
	private String nombre;
	
	@NotEmpty
	private String apellido;
	
	@NotEmpty
	@Email // validación tipo email
	private String email;
	
	private String foto;
	
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE) // indica el formato en que se va a guardar
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") //para formatear la fecha en el json 
	@NotNull  // campo no nullo en el formulario, es una validación
	private Date createAt;

	/*@PrePersist // se creará la fecha al insertar un nuevo cliente en este caso
	public void prePersist() {
		createAt = new Date();
	}*/        // se agregará en el formulario mejor.
	
	//un Cliente muchas facturas, como hay una relación entre ambas se mapea
			//llave foránea													//, orphanRemoval = true -> opcional para eliminar registros huerfanos que no están asociados a ningun cliente
	@OneToMany(mappedBy="cliente", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JsonManagedReference//jmanagedr.. para que el json se haga una relacion desde factura a cliente y no de cliente a factura   // @JsonIgnore así se evita un bucle , ya que cliente contiene facturas y factura cliente, entonces no terminaría nunca 
	private List<Factura> facturas;
	
	
	public Cliente() {
		facturas = new ArrayList<Factura>();
	}

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	
	
	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}
	public void addFactura(Factura factura) {
		facturas.add(factura);
	}

	@Override
	public String toString() {
		return nombre + " " + apellido;
	}

	private static final long serialVersionUID = 1L;

}
