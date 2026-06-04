package logica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Inventario implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, Producto> inventarioProductos;
	
	public Inventario() {
		this.inventarioProductos = new HashMap<>();
	}
	public void agregarProducto(Producto producto) {
		inventarioProductos.put(producto.getCodigoBarras(), producto);
	}
	
	public void eliminarProducto(String codigoBarras) {
		inventarioProductos.remove(codigoBarras);
	}
	public Producto buscarProducto(String codigoBarras) {
		return inventarioProductos.get(codigoBarras);
	}
	public List<Producto> listarProductos(){
		return new ArrayList<>(inventarioProductos.values());
	}
	public void actualizarStock(String codigoBarras, int cantidad) {
		Producto producto = buscarProducto(codigoBarras);
		if(producto != null) {
		producto.setCantidadActual(cantidad);
		}
	}
	
	public List<Producto> verificarStockMinimo() {
		List<Producto> productosBajos = new ArrayList<>();
		for(Producto producto: inventarioProductos.values() ) {
			if(producto.getCantidadActual() <= producto.getStockMinimo()) {
				productosBajos.add(producto);
			}
		}
		return productosBajos;
	}

}
