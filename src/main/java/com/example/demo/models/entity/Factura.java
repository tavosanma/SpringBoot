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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@Entity
@Table(name="facturas")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Factura implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String descripcion;
	
	private String observacion;
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") //para formatear la fecha en el json
	private Date createAt;
	
	// muchas facturas un cliente y un cliente muchas facturas
	@ManyToOne(fetch=FetchType.LAZY) //carga floja, las consultas se realizan a medida que se invocan los metodos
	@JsonBackReference // relacion contraparte que no queremos serializar en el json, va junto con jmanagedReference en la clase cliente
	private Cliente cliente;

	//una factura muchos items factura...itemsfactura no me interesa la relación con factura
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="factura_id")  //clave foránea 
	private List<ItemFactura> items;
	
	public Factura() {
		this.items  = new ArrayList<ItemFactura>();
	}
	@PrePersist
	public void prePersist() {
		createAt = new Date();	
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	 //se omite este atributo y no lo incluye en el xml o sino se haría un ciclo infinito por que hay una direccion en ambos lados cliente y factura
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<ItemFactura> getItems() {
		return items;
	}
	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}
	public void addItemFactura(ItemFactura item) { 
		this.items.add(item);
	}
	public Double getTotal() {
		Double total = 0.0;
		
		int size = items.size();
		
		for (int i=0; i< size; i++) {
			total += items.get(i).calcularImporte();
		}
		return total;
	}

}
