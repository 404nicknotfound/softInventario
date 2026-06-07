package presentacion.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.beans.property.*;
import logica.*;

public class InventarioController {

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, String> colMarca;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, Integer> colStockMin;

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtCategoria;
    @FXML private TextField txtMarca;
    @FXML private TextField txtStock;
    @FXML private TextField txtStockMin;

    private Tienda tienda;
    private Stage stage;
    private ObservableList<Producto> productosObservable = FXCollections.observableArrayList();

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
        cargarProductos();
    }
    public void setStage(Stage stage) { this.stage = stage; }

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigoBarras()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colPrecio.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrecio()).asObject());
        colCategoria.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategoria()));
        colMarca.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMarca()));
        colStock.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getCantidadActual()).asObject());
        colStockMin.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getStockMinimo()).asObject());

        tablaProductos.setItems(productosObservable);

        // Al seleccionar una fila → rellena el formulario
        tablaProductos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) poblarFormulario(newVal);
            }
        );
    }

    private void cargarProductos() {
        productosObservable.clear();
        productosObservable.addAll(tienda.getInventario().listarProductos());
    }

    private void poblarFormulario(Producto p) {
        txtCodigo.setText(p.getCodigoBarras());
        txtNombre.setText(p.getNombre());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtCategoria.setText(p.getCategoria());
        txtMarca.setText(p.getMarca());
        txtStock.setText(String.valueOf(p.getCantidadActual()));
        txtStockMin.setText(String.valueOf(p.getStockMinimo()));
        txtCodigo.setDisable(true); // el código no se puede editar
    }
    
    @FXML
    public void handleAgregar() {
        if (!validarCampos()) return;
        try {
            Producto nuevo = new Producto(
                txtCodigo.getText().trim(),
                txtNombre.getText().trim(),
                "",
                Double.parseDouble(txtPrecio.getText().trim()),
                txtCategoria.getText().trim(),
                txtMarca.getText().trim(),
                Integer.parseInt(txtStock.getText().trim()),
                Integer.parseInt(txtStockMin.getText().trim())
            );
            tienda.registrarProducto(nuevo);
            cargarProductos();
            handleLimpiar();
        } catch (NumberFormatException e) {
            mostrarError("Precio, Stock y Stock Mínimo deben ser números válidos");
        }
    }

    @FXML
    public void handleActualizar() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Selecciona un producto de la tabla para actualizar");
            return;
        }
        try {
            seleccionado.setNombre(txtNombre.getText().trim());
            seleccionado.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
            seleccionado.setCategoria(txtCategoria.getText().trim());
            seleccionado.setMarca(txtMarca.getText().trim());
            seleccionado.setCantidadActual(Integer.parseInt(txtStock.getText().trim()));
            seleccionado.setStockMinimo(Integer.parseInt(txtStockMin.getText().trim()));
            tienda.escribirInventario();
            cargarProductos();
            handleLimpiar();
        } catch (NumberFormatException e) {
            mostrarError("Precio, Stock y Stock Mínimo deben ser números válidos");
        }
    }

    @FXML
    public void handleEliminar() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Selecciona un producto para eliminar");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar Producto");
        confirmacion.setContentText("¿Eliminar " + seleccionado.getNombre() + "?");
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                tienda.getInventario().eliminarProducto(seleccionado.getCodigoBarras());
                tienda.escribirInventario();
                cargarProductos();
                handleLimpiar();
            }
        });
    }

    @FXML
    public void handleLimpiar() {
        txtCodigo.clear(); txtNombre.clear(); txtPrecio.clear();
        txtCategoria.clear(); txtMarca.clear();
        txtStock.clear(); txtStockMin.clear();
        txtCodigo.setDisable(false);
        tablaProductos.getSelectionModel().clearSelection();
    }

    private boolean validarCampos() {
        if (txtCodigo.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()
                || txtPrecio.getText().trim().isEmpty() || txtStock.getText().trim().isEmpty()
                || txtStockMin.getText().trim().isEmpty()) {
            mostrarError("Código, Nombre, Precio, Stock y Stock Mínimo son obligatorios");
            return false;
        }
        return true;
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.show();
    }
    
    
    @FXML
    public void handleVentas() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/presentacion/fxml/ventas.fxml"));
            Parent root = loader.load();
            VentasController controller = loader.getController();
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
        } catch (Exception e) { e.printStackTrace(); }
    }
}
