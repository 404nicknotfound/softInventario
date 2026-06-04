package logica;

import java.io.Serializable;

public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String codigoBarras;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria;
    private String marca;
    private int cantidadActual;
    private int stockMinimo;

    public Producto(String codigoBarras, String nombre, String descripcion,
                    double precio, String categoria, String marca,
                    int cantidadActual, int stockMinimo) {
        this.codigoBarras = codigoBarras;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.marca = marca;
        this.cantidadActual = cantidadActual;
        this.stockMinimo = stockMinimo;
    }

    // Getters
    public String getCodigoBarras() { return codigoBarras; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }
    public String getMarca() { return marca; }
    public int getCantidadActual() { return cantidadActual; }
    public int getStockMinimo() { return stockMinimo; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setCantidadActual(int cantidadActual) { this.cantidadActual = cantidadActual; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    @Override
    public String toString() {
        return "[" + codigoBarras + "] " + nombre + " - $" + precio +
               " | Stock: " + cantidadActual;
    }
}