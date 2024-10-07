package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;

import javafx.scene.layout.GridPane;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


/**
*
* Classe permettant l'impression du ticket à la fin de la transaction .
* 
* @author Imane B.
*/

public class Ticket {



	private GridPane gridPane = new GridPane();

	private int typeTransaction;
	private Stage stage2 = new Stage();
	private boolean isInFrench;

	/**
	 * Construit la classe Ticket
	 * 
	 * @param typeTransaction indique la transaction qui a été effectuée <br>
	 *                        <b>0</b> retrait <br>
	 *                        <b>1</b> dépôt <br>
	 *                        <b>2</b> consultation du solde <br>
	 * @param isInFrench   
	 * 			 <b>true</b> le guichet a été mis en français par le client <br>
	 *           <b>false</b> le guichet a été mis en anglais par le client <br> 
	 * 
	 * 
	 * 
	 */

	public Ticket(int typeTransaction, boolean isInFrench) {

		this.typeTransaction = typeTransaction;
		this.isInFrench = isInFrench;

		VBox root = new VBox(10);
		root.setPadding(new Insets(10));

		gridPane.setHgap(10);
		gridPane.setVgap(10);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		if (this.isInFrench) {

			Text text1 = new Text("CARTE BANCAIRE");
			Text text2 = new Text("BANQUE 2023-L2C1");

			Text dateTime = new Text("LE " + dtf.format(now));

			Text text3 = new Text();
			if (this.typeTransaction == 0) {
				text3.setText("RETRAIT ");
			} else if (this.typeTransaction == 1) {
				text3.setText("DEPOT ");
			} else if (this.typeTransaction == 2) {
				text3.setText(" CONSULTATION DU SOLDE  ");
			}

			Text text4 = new Text("MONTANT : ");

			gridPane.add(text1, 0, 0);
			gridPane.add(text2, 0, 1);
			gridPane.add(dateTime, 0, 2);
			gridPane.add(text3, 0, 3);
			gridPane.add(text4, 0, 4);

		} else {

			Text text1 = new Text("BANK CARD");
			Text text2 = new Text("2023-L2C1 BANK ");

			Text dateTime = new Text(dtf.format(now));

			Text text3 = new Text();
			if (this.typeTransaction == 0) {
				text3.setText("WITHDRAWAL ");
			} else if (this.typeTransaction == 1) {
				text3.setText("DEPOSIT ");
			} else if (this.typeTransaction == 2) {
				text3.setText(" BALANCE CHECK ");
			}

			Text text4 = new Text("AMOUNT  : ");

			gridPane.add(text1, 0, 0);
			gridPane.add(text2, 0, 1);
			gridPane.add(dateTime, 0, 2);
			gridPane.add(text3, 0, 3);
			gridPane.add(text4, 0, 4);

		}

		root.getChildren().add(gridPane);

		Scene scene = new Scene(root, 300, 200);

		stage2.setScene(scene);
		stage2.show();

	}

////////////////////GETTERS

	public GridPane getGridPane() {
		return gridPane;
	}

	public Stage getStage2() {
		return stage2;
	}

}
