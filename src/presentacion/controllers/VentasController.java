package presentacion.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

import javafx.beans.property.*;
import logica.*;

public class VentasController {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDescuento;
    @FXML private TableView<ItemVenta> tablaItems;
    @FXML private TableColumn<ItemVenta, String> colNombre;
    @FXML private TableColumn<ItemVenta, Integer> colCantidad;
    @FXML private TableColumn<ItemVenta, Double> colPrecio;
    @FXML private TableColumn<ItemVenta, Double> colSubtotal;
    @FXML private Label lblSubtotal;
    @FXML private Label lblImpuesto;
    @FXML private Label lblTotal;

    private Tienda tienda;
    private Stage stage;
    private Venta ventaActual;
    private ObservableList<ItemVenta> itemsObservable = FXCollections.observableArrayList();

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
        iniciarNuevaVenta();
    }
    public void setStage(Stage stage) { this.stage = stage; }

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(c -> 
            new SimpleStringProperty(c.getValue().getProducto().getNombre()));
        colCantidad.setCellValueFactory(c -> 
            new SimpleIntegerProperty(c.getValue().getCantidad()).asObject());
        colPrecio.setCellValueFactory(c -> 
            new SimpleDoubleProperty(c.getValue().getPrecioUnitario()).asObject());
        colSubtotal.setCellValueFactory(c -> 
            new SimpleDoubleProperty(c.getValue().calcularSubTotal()).asObject());

        tablaItems.setItems(itemsObservable);
    }
    
    private void actualizarTotales() {
        lblSubtotal.setText("$" + String.format("%.2f", ventaActual.calcularSubtotalItems()));
        lblImpuesto.setText("$" + String.format("%.2f", ventaActual.calcularImpuesto()));
        lblTotal.setText("$" + String.format("%.2f", ventaActual.calcularTotal()));
    }
    private void iniciarNuevaVenta() {
        ventaActual = tienda.iniciarVenta(0.19); // 19% IVA
        itemsObservable.clear();
        actualizarTotales();
    }
    
    
    @FXML
    public void handleBuscarCodigo() {
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty()) return;

        Producto producto = tienda.buscarProducto(codigo);
        if (producto != null) {
            agregarProductoAVenta(producto);
            txtCodigo.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Producto no encontrado");
            alert.setContentText("No existe un producto con código: " + codigo);
            alert.show();
        }
    }

    private void agregarProductoAVenta(Producto producto) {
        ItemVenta item = new ItemVenta(producto, 1);
        ventaActual.agregarItem(item);
        itemsObservable.add(item);
        actualizarTotales();
    }
    
    @FXML
    public void handleBuscarNombre() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) return;

        List<Producto> resultados = tienda.buscarProductoPorNombre(nombre);

        if (resultados.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sin resultados");
            alert.setContentText("No se encontraron productos con nombre: " + nombre);
            alert.show();
            return;
        }

        ChoiceDialog<Producto> dialog = new ChoiceDialog<>(resultados.get(0), resultados);
        dialog.setTitle("Seleccionar Producto");
        dialog.setHeaderText("Productos encontrados para: " + nombre);
        dialog.setContentText("Selecciona un producto:");

        dialog.showAndWait().ifPresent(producto -> {
            agregarProductoAVenta(producto);
            txtNombre.clear();
        });
    }
    
    @FXML
    public void handleAplicarDescuento() {
        String texto = txtDescuento.getText().trim();
        if (texto.isEmpty()) return;

        try {
            double porcentaje = Double.parseDouble(texto);
            if (porcentaje < 0 || porcentaje > 100) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Descuento inválido");
                alert.setContentText("El descuento debe estar entre 0 y 100");
                alert.show();
                return;
            }
            ventaActual.aplicarDescuento(porcentaje / 100);
            actualizarTotales();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ingresa un número válido para el descuento");
            alert.show();
        }
    }
    
    @FXML
    public void handleProcesarVenta() {
        if (itemsObservable.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Venta vacía");
            alert.setContentText("Agrega productos antes de procesar la venta");
            alert.show();
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Venta");
        confirmacion.setHeaderText("Total: " + lblTotal.getText());
        confirmacion.setContentText("¿Deseas procesar esta venta?");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                tienda.procesarVenta(ventaActual);
                Alert exito = new Alert(Alert.AlertType.INFORMATION);
                exito.setTitle("Éxito");
                exito.setContentText("¡Venta procesada correctamente!");
                exito.show();
                iniciarNuevaVenta();
            }
        });
    }
    
    @FXML
    public void handleNuevaVenta() {
        if (!itemsObservable.isEmpty()) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Nueva Venta");
            confirmacion.setContentText("¿Descartar la venta actual y comenzar una nueva?");
            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) iniciarNuevaVenta();
            });
        } else {
            iniciarNuevaVenta();
        }
    }
    
    @FXML
    public void handleCerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/presentacion/fxml/login.fxml"));
            Parent root = loader.load();
            LoginController controller = loader.getController();
            controller.setTienda(tienda);
            controller.setStage(stage);
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleInventario() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/presentacion/fxml/inventario.fxml"));
            Parent root = loader.load();
            InventarioController controller = loader.getController();
            controller.setTienda(tienda);
            controller.setStage(stage);
            stage.setScene(new Scene(root));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void handleHistorial() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/presentacion/fxml/historial.fxml"));
            Parent root = loader.load();
            HistorialController controller = loader.getController();
            controller.setTienda(tienda);
            controller.setStage(stage);
            stage.setScene(new Scene(root));
        } catch (Exception e) { e.printStackTrace(); }
    }
}