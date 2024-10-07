
package application;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe permettant de lancer l'application du GAB
 * 
 * @author Imane B.
 * 
 */

public class App extends Application {

	public static void main(String[] args) throws SQLException {

		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Graphics graphics = new Graphics();

		graphics.reAfficherEcran();
		new CentralModule(graphics, graphics.getCardModule(), new SecurityModule());

		graphics.getStage().setOnCloseRequest(event -> {

			System.exit(1);

		});

	}
}