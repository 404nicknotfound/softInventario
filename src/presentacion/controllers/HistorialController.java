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
import java.time.format.DateTimeFormatter;

public class HistorialController {

    @FXML private TableView<Venta> tablaVentas;
    @FXML private TableColumn<Venta, String> colId;
    @FXML private TableColumn<Venta, String> colFecha;
    @FXML private TableColumn<Venta, Double> colTotal;
    @FXML private TableColumn<Venta, String> colEstado;
    @FXML private TableColumn<Venta, Integer> colItems;

    @FXML private TableView<ItemVenta> tablaItems;
    @FXML private TableColumn<ItemVenta, String> colProducto;
    @FXML private TableColumn<ItemVenta, Integer> colCantidad;
    @FXML private TableColumn<ItemVenta, Double> colPrecio;
    @FXML private TableColumn<ItemVenta, Double> colSubtotal;

    private Tienda tienda;
    private Stage stage;
    private ObservableList<Venta> ventasObservable = FXCollections.observableArrayList();
    private ObservableList<ItemVenta> itemsObservable = FXCollections.observableArrayList();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
        cargarVentas();
    }
    public void setStage(Stage stage) { this.stage = stage; }

    @FXML
    public void initialize() {
        // Tabla ventas
        colId.setCellValueFactory(c -> new SimpleStringProperty(
            c.getValue().getId().substring(0, 8) + "...")); // muestra solo primeros 8 chars
        colFecha.setCellValueFactory(c -> new SimpleStringProperty(
            c.getValue().getFecha().format(formatter)));
        colTotal.setCellValueFactory(c -> new SimpleDoubleProperty(
            c.getValue().calcularTotal()).asObject());
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(
            c.getValue().getEstado()));
        colItems.setCellValueFactory(c -> new SimpleIntegerProperty(
            c.getValue().getItemsVenta().size()).asObject());

        tablaVentas.setItems(ventasObservable);

        // Tabla items
        colProducto.setCellValueFactory(c -> new SimpleStringProperty(
            c.getValue().getProducto().getNombre()));
        colCantidad.setCellValueFactory(c -> new SimpleIntegerProperty(
            c.getValue().getCantidad()).asObject());
        colPrecio.setCellValueFactory(c -> new SimpleDoubleProperty(
            c.getValue().getPrecioUnitario()).asObject());
        colSubtotal.setCellValueFactory(c -> new SimpleDoubleProperty(
            c.getValue().calcularSubTotal()).asObject());

        tablaItems.setItems(itemsObservable);

        // Al seleccionar una venta → muestra sus ítems
        tablaVentas.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                itemsObservable.clear();
                if (newVal != null) itemsObservable.addAll(newVal.getItemsVenta());
            }
        );
    }

    private void cargarVentas() {
        ventasObservable.clear();
        ventasObservable.addAll(tienda.getVentas());
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