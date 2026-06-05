package logica;

import java.util.ArrayList;
import java.util.List;

import persistencia.Persistencia;

public class Tienda {
	private Inventario inventario;
	private List<Venta> ventas;
	private List<Usuario> usuarios;
	private Persistencia persistencia;
	
	@SuppressWarnings("unchecked")
	public Tienda() {
		this.persistencia = new Persistencia();
		// Inventario — objeto único
		this.inventario = (Inventario) persistencia.leer("inventario.bin");
		if (this.inventario == null) this.inventario = new Inventario();

		// Ventas — lista
		this.ventas = (List<Venta>) persistencia.leer("ventas.bin");
		if (this.ventas == null) this.ventas = new ArrayList<>();

		// Usuarios — lista
		this.usuarios = (List<Usuario>) persistencia.leer("usuarios.bin");
		if (this.usuarios == null) this.usuarios = new ArrayList<>();
	}
	
	public void registrarProducto(Producto producto) {
		inventario.agregarProducto(producto);
		escribirInventario();
	}
	public Producto buscarProducto(String codigoBarras) {
		return inventario.buscarProducto(codigoBarras);
	}
	
	public List<Producto> buscarProductoPorNombre(String nombre) {
	    return inventario.buscarPorNombre(nombre);
	}
	
	public Venta iniciarVenta(double tasaImpuesto) {
		return new Venta(tasaImpuesto);
	}
	public void procesarVenta(Venta venta) {
		venta.completarVenta();
		ventas.add(venta);
		for(ItemVenta itemVenta: venta.getItemsVenta()) {
			Producto p = inventario.buscarProducto(itemVenta.getProducto().getCodigoBarras());
			inventario.actualizarStock(p.getCodigoBarras(), p.getCantidadActual() - itemVenta.getCantidad());
		}
		persistencia.escribir(ventas, "ventas.bin");
		escribirInventario();
		
	}
	
	public void escribirInventario() {
		persistencia.escribir(inventario, "inventario.bin");
	}
	
	public void registrarUsuario(Usuario usuario) {
	    usuarios.add(usuario);
	    persistencia.escribir(usuarios, "usuarios.bin");
	}

	public Inventario getInventario() {
		return inventario;
	}

	public void setInventario(Inventario inventario) {
		this.inventario = inventario;
	}

	public List<Venta> getVentas() {
		return ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Persistencia getPersistencia() {
		return persistencia;
	}

	public void setPersistencia(Persistencia persistencia) {
		this.persistencia = persistencia;
	}
	
	
	
	
}
