package presentacion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import logica.Tienda;
import logica.Usuario;
import presentacion.controllers.LoginController;

public class Principal extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		
	    Tienda tienda = new Tienda();
	    
	    if (tienda.getUsuarios().isEmpty()) {
	        tienda.registrarUsuario(new Usuario("Admin", "admin", "1234", "ADMIN"));
	    }
	    
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/login.fxml"));
	    Parent root = loader.load();
	    
	    LoginController controller = loader.getController();
	    controller.setTienda(tienda);
	    controller.setStage(stage);
	    
	    stage.setTitle("Xian POS");
	    stage.setScene(new Scene(root));
	    stage.show();
	}
}