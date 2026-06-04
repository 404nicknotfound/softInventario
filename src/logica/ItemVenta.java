package logica;

import java.io.Serializable;

public class ItemVenta implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Producto producto;
	private int cantidad;
	private double precioUnitario;

	public ItemVenta(Producto producto, int cantidad) {
		this.producto = producto;
		this.cantidad = cantidad;
		this.precioUnitario = producto.getPrecio();
		
	}
	
	public double calcularSubTotal() {
		return cantidad * precioUnitario;
		
	}

	public Producto getProducto() {
		return producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public double getSubTotal() {
		return calcularSubTotal();
	}

	@Override
	public String toString() {
		return cantidad + "x " + producto.getNombre() + " - $" + precioUnitario +
	               " c/u =" + getSubTotal();
	}
	
}
