package logica;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Venta implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private LocalDateTime fecha;
	private String estado;
	private Double valorTotal;
	private Double tasaImpuesto;
	private Double descuentoAplicado;
	private List<ItemVenta> itemsVenta;
	
	public Venta(Double tasaImpuesto){
		this.tasaImpuesto = tasaImpuesto;
		this.id = UUID.randomUUID().toString();
		this.fecha = LocalDateTime.now();
		this.estado = "PENDIENTE";
		this.itemsVenta = new ArrayList<ItemVenta>();
		this.descuentoAplicado = 0.0;
	}
	
	
	public double calcularSubtotalItems() {
		double valorTotal = 0;
		for(ItemVenta itemVenta : itemsVenta) {
			double precioSub = itemVenta.getSubTotal();
			valorTotal += precioSub;
		}
		return valorTotal;
	}
	
	public void agregarItem(ItemVenta itemVenta) {
		this.itemsVenta.add(itemVenta);
	}
	
	public void eliminarItem(ItemVenta itemVenta) {
		this.itemsVenta.remove(itemVenta);
	}
	
	public double calcularImpuesto() {
		return calcularSubtotalItems() * this.tasaImpuesto;
		
	}
	
	public void aplicarDescuento(double porcentaje) {
		double descuento = calcularSubtotalItems() * porcentaje;
		this.descuentoAplicado = descuento;
	}
	public double calcularTotal() {
		return calcularSubtotalItems() + calcularImpuesto() - this.descuentoAplicado;
	}
	
	public void completarVenta() {
		this.estado = "COMPLETADA";
	}
	
	public void cancelarVenta() {
		this.estado = "CANCELADA";
	}


	public String getId() {
		return id;
	}


	public LocalDateTime getFecha() {
		return fecha;
	}


	public String getEstado() {
		return estado;
	}


	public Double getValorTotal() {
		return valorTotal;
	}


	public Double getTasaImpuesto() {
		return tasaImpuesto;
	}


	public Double getDescuentoAplicado() {
		return descuentoAplicado;
	}


	public List<ItemVenta> getItemsVenta() {
		return itemsVenta;
	}
	
}
