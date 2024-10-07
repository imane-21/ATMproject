package application;

import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Classe permettant l'affichage des billets lors du retrait
 * 
 * @author Christophe C., Imane B.
 * 
 */

public class Billets {

	private Stage stage2 = new Stage();

	/**
	 * Construit la classe Billets en créant une nouvelle fenêtre contenant le
	 * retrait demandé
	 * 
	 * @param somme somme qui doit être affichée en billets
	 * 
	 * 
	 */

	public Billets(int somme) throws SQLException {

		Database db1 = new Database();
		db1.connect();
		int nbbillet50danslaBDD = db1.getBillet("nb_billet50");
		int nbbillet20danslaBDD = db1.getBillet("nb_billet20");
		int nbbillet10danslaBDD = db1.getBillet("nb_billet10");
		db1.disconnect();

		int billet50aretirer = 0;
		int billet20aretirer = 0;
		int billet10aretirer = 0;

		while (somme > 0) {
			if (nbbillet50danslaBDD == 0 & nbbillet20danslaBDD == 0 & nbbillet10danslaBDD == 0) {
				break;
			}
			if (somme >= 50 & nbbillet50danslaBDD > 0) {
				somme = somme - 50;
				billet50aretirer += 1;
				nbbillet50danslaBDD = nbbillet50danslaBDD - 1;
			} else if (somme >= 20 & nbbillet20danslaBDD > 0) {
				somme = somme - 20;
				billet20aretirer += 1;
				nbbillet20danslaBDD = nbbillet20danslaBDD - 1;
			} else if (somme >= 10 & nbbillet10danslaBDD > 0) {
				somme = somme - 10;
				billet10aretirer += 1;
				nbbillet10danslaBDD = nbbillet10danslaBDD - 1;
			} else {
				break;
			}
		}

		VBox root = new VBox(10);
		root.setPadding(new Insets(10));

		GridPane gridPane = new GridPane();
		gridPane.setHgap(-80);
		gridPane.setVgap(10);

		for (int i = 0; i < billet50aretirer; i++) {
			gridPane.add(createBanknoteImageView("images/50_euros.jpg"), i, 0);
			gridPane.add(createBanknoteImageView("images/50_euros.jpg"), i, 0);
			gridPane.add(createBanknoteImageView("images/50_euros.jpg"), i, 0);
		}

		for (int i = 0; i < billet20aretirer; i++) {
			gridPane.add(createBanknoteImageView("images/20_euros.jpg"), i, 1);
			gridPane.add(createBanknoteImageView("images/20_euros.jpg"), i, 1);
			gridPane.add(createBanknoteImageView("images/20_euros.jpg"), i, 1);
		}

		for (int i = 0; i < billet10aretirer; i++) {
			gridPane.add(createBanknoteImageView("images/10_euros.png"), i, 2);
			gridPane.add(createBanknoteImageView("images/10_euros.png"), i, 2);
			gridPane.add(createBanknoteImageView("images/10_euros.png"), i, 2);
		}

		root.getChildren().add(gridPane);

		Scene scene = new Scene(root, 300, 200);

		stage2.setScene(scene);
		stage2.show();

	}

	/**
	 * Crée une image de billet
	 * 
	 * @param imageName image du billet que l'on souhaite afficher
	 * 
	 * 
	 */

	private ImageView createBanknoteImageView(String imageName) {
		Image image = new Image(getClass().getResourceAsStream(imageName));
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(120);
		imageView.setFitHeight(70);
		return imageView;
	}

}
