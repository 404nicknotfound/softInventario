package presentacion.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logica.Tienda;
import logica.Usuario;

public class LoginController {
	
	@FXML private TextField txtUsername;
	@FXML private PasswordField txtContrasena;
	private Tienda tienda;
	private Stage stage;
	
	public void setTienda(Tienda tienda) { this.tienda = tienda; }
	public void setStage(Stage stage) { this.stage = stage; }
	
	@FXML
	public void handleLogin() {
	    for (Usuario usuario : tienda.getUsuarios()) {
	        if (usuario.autenticar(txtUsername.getText(), txtContrasena.getText())) {
	            try {
	                FXMLLoader loader = new FXMLLoader(
	                    getClass().getResource("/presentacion/fxml/ventas.fxml"));
	                Parent root = loader.load();
	                VentasController controller = loader.getController();
	                controller.setTienda(tienda);
	                controller.setStage(stage);
	                stage.setScene(new Scene(root));
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	            return;
	        }
	    }
	    Alert alert = new Alert(Alert.AlertType.ERROR);
	    alert.setTitle("Error");
	    alert.setContentText("Usuario o contraseña incorrectos");
	    alert.show();
	}
	
	
}
