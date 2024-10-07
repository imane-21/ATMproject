package application;

import java.sql.SQLException;

//import java.io.File;

//import javax.smartcardio.CardException;

//import application.Module.CardInfo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
//import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
//import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Classe graphique du GAB, permettant un affichage complet sous JavaFX
 * 
 * Cette classe crée plusieurs boutons, images textes et champs de saisie
 * 
 * @author Imane B.
 * 
 */

public class Graphics {

	private Group root = new Group();
	private Scene scene = new Scene(root, 800, 700, Color.LIGHTGOLDENRODYELLOW);
	private Stage stage = new Stage();

	// initialisation des textes

	private Text mainText = new Text();
	private Text screenText = new Text();
	private Text screenText2 = new Text();
	private Text userText = new Text();
	private Text rightupText = new Text();
	private Text leftupText = new Text();
	private Text rightdownText = new Text();
	private Text leftdownText = new Text();

	// variable permettant de valider le saisieCodePIN

	private String userPINCode = "";

	// variables de positionnement du texte et d'aspect des boutons

	private int x = 275;
	private int y = 500;
	private int w = 50;
	private int buttonSize = 30;

	// initialsisation des images utilisées

	/*
	 * getClass().getResourceAsStream("") permet d'avoir l'URL relative et evite
	 * donc les problemes de chargement d'images en cas de changements d'emplacement
	 */

	private Image img0 = new Image(getClass().getResourceAsStream("images/0.png"));
	private ImageView imgview0 = new ImageView();

	private Image img1 = new Image(getClass().getResourceAsStream("images/1.png"));
	private ImageView imgview1 = new ImageView();

	private Image img2 = new Image(getClass().getResourceAsStream("images/2.png"));
	private ImageView imgview2 = new ImageView();

	private Image img3 = new Image(getClass().getResourceAsStream("images/3.png"));
	private ImageView imgview3 = new ImageView();

	private Image img4 = new Image(getClass().getResourceAsStream("images/4.png"));
	private ImageView imgview4 = new ImageView();

	private Image img5 = new Image(getClass().getResourceAsStream("images/5.png"));
	private ImageView imgview5 = new ImageView();

	private Image img6 = new Image(getClass().getResourceAsStream("images/6.png"));
	private ImageView imgview6 = new ImageView();

	private Image img7 = new Image(getClass().getResourceAsStream("images/7.png"));
	private ImageView imgview7 = new ImageView();

	private Image img8 = new Image(getClass().getResourceAsStream("images/8.png"));
	private ImageView imgview8 = new ImageView();

	private Image img9 = new Image(getClass().getResourceAsStream("images/9.png"));
	private ImageView imgview9 = new ImageView();

	private Image cancelimg = new Image(getClass().getResourceAsStream("images/cancel.png"));
	private ImageView imgv = new ImageView();
	private Button cancel = new Button("CANCEL", imgv);

	private Image clearimg = new Image(getClass().getResourceAsStream("images/clear.png"));
	private ImageView imgv1 = new ImageView();
	private Button clear = new Button("CLEAR", imgv1);

	private Image enterimg = new Image(getClass().getResourceAsStream("images/circle.png"));
	private ImageView imgv2 = new ImageView();
	private Button enter = new Button("ENTER", imgv2);

	private Image choixlangueimg = new Image(getClass().getResourceAsStream("images/angleterre.png"));
	private ImageView imgv4 = new ImageView();
	private Button choixlangue = new Button("", imgv4);

	private Image homeimg = new Image(getClass().getResourceAsStream("images/home.png"));
	private ImageView homeimgv = new ImageView();
	private Button home = new Button("", homeimgv);

	private Rectangle ecran = new Rectangle(700, 460, Color.LIGHTBLUE);

	private ImageView logo = new ImageView();
	private Image logoimg = new Image(getClass().getResourceAsStream("images/logo.png"));

	private Image gifimg = new Image(getClass().getResourceAsStream("images/insertcard.gif"));
	private static ImageView gif = new ImageView();

	private Image waitimg = new Image(getClass().getResourceAsStream("images/loading.gif"));
	private static ImageView wait = new ImageView();

	private Image info = new Image(getClass().getResourceAsStream("images/info.png"));
	private ImageView infoimgv = new ImageView();
	private Button infobutton = new Button("", infoimgv);

	// initialisation des boutons

	private Button one = new Button("1", imgview1);
	private Button two = new Button("2", imgview2);
	private Button three = new Button("3", imgview3);
	private Button four = new Button("4", imgview4);
	private Button five = new Button("5", imgview5);
	private Button six = new Button("6", imgview6);
	private Button seven = new Button("7", imgview7);
	private Button eight = new Button("8", imgview8);
	private Button nine = new Button("9", imgview9);
	private Button zero = new Button("0", imgview0);

	private Button leftup = new Button();
	private Button leftdown = new Button();
	private Button rightup = new Button();
	private Button rightdown = new Button();

	private PasswordField saisieCodePIN = new PasswordField();
	private TextField saisieSomme = new TextField();

	// permet de savoir l'etat du bouton de changement de langue pour l'effectuer
	// correctement

	private boolean isInFrench = true;

	// variable permettant de savoir à quelle page se situe l'interface graphique
	// afin
	// d'effectuer les changements associés

	private static final int INSERT_CARD = 0;
	private static final int WAITING = 1;
	private static final int FIRST_CHOICES = 2;
	private static final int ENTER_PIN_DEPOSIT = 3;
	private static final int INCORRECT_PIN_DEPOSIT = 4;
	private static final int INCORRECT_PIN_WITHDRAW = 5;
	private static final int BALANCE_CHECK = 6;
	private static final int SUCCES_DEPOSIT = 7;
	private static final int CURRENCY = 8;
	private static final int CUSTOMER_AREA = 9;
	private static final int INCORRECT_PIN_X3 = 10;
	private static final int SUM_TOWITHDRAW = 11;
	private static final int SUM_TODEPOSIT = 12;
	private static final int CANCEL = 13;
	private static final int ENTER_PIN_WITHDRAW = 14;
	private static final int ENTER_PIN_BALANCECHECK = 15;
	private static final int SUCCES_WITHDRAW = 16;
	private static final int INCCORRECT_PIN_BALANCECHECK = 17;
	private static final int ANOTHER_TRANSACTION = 18;
	private static final int FIRST_CHOICES_NOPIN = 19;

	private int currentScreenState = 0;

	// import des autres classes

	private CardModule cardModule;
	private CentralModule centralModule;
	private Ticket ticket;
	private HeadphoneDetection headphoneDetection = new HeadphoneDetection();
	boolean casquePresence = headphoneDetection.headphonesConnected();
	TextToSpeech tts = new TextToSpeech();
	private Database db = new Database();

// variables permettant de centrer les textes 

	private double centerX = scene.getWidth() / 2;

	// permet de créer un accès web

	private WebView webView = new WebView();
	private WebEngine webEngine = webView.getEngine();

	private int nbbillet50danslaBDD;
	private int nbbillet20danslaBDD;
	private int nbbillet10danslaBDD;

	public Graphics() {

		/**
		 * Construit la classe Graphique
		 * 
		 */

		cardModule = new CardModule(this);
		centralModule = new CentralModule(this, this.getCardModule(), new SecurityModule());

		mainText.setText("GUICHET AUTOMATIQUE BANCAIRE");
		mainText.setX(centerX - mainText.getBoundsInLocal().getWidth() / 2);
		mainText.setY(50);
		mainText.setFont(Font.font("Verdana", 20));
		mainText.setFill(Color.BLACK);
		mainText.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		ecran.setX(50);
		ecran.setY(10);

		screenText.setText("INSEREZ VOTRE CARTE");
		screenText.setX(centerX - screenText.getBoundsInLocal().getWidth() / 2);
		screenText.setY(250);
		screenText.setFont(Font.font("Verdana", 20));
		screenText.setFill(Color.BLACK);
		screenText.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		screenText2.setText("");

		screenText2.setY(250);
		screenText2.setX((centerX - screenText.getBoundsInLocal().getWidth() / 2) - 30);
		screenText2.setFont(Font.font("Verdana", 20));
		screenText2.setFill(Color.BLACK);
		screenText2.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		one.setLayoutX(x);
		one.setLayoutY(y);
		one.setPrefWidth(buttonSize);
		one.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		two.setLayoutX(x + w);
		two.setLayoutY(y);
		two.setPrefWidth(buttonSize);
		two.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		three.setLayoutX(x + 2 * w);
		three.setLayoutY(y);
		three.setPrefWidth(buttonSize);
		three.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		four.setLayoutX(x);
		four.setLayoutY(y + w);
		four.setPrefWidth(buttonSize);
		four.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		five.setLayoutX(x + w);
		five.setLayoutY(y + w);
		five.setPrefWidth(buttonSize);
		five.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		six.setLayoutX(x + 2 * w);
		six.setLayoutY(y + w);
		six.setPrefWidth(buttonSize);
		six.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		seven.setLayoutX(x);
		seven.setLayoutY(y + 2 * w);
		seven.setPrefWidth(buttonSize);
		seven.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		eight.setLayoutX(x + w);
		eight.setLayoutY(y + 2 * w);
		eight.setPrefWidth(buttonSize);
		eight.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		nine.setLayoutX(x + 2 * w);
		nine.setLayoutY(y + 2 * w);
		nine.setPrefWidth(buttonSize);
		nine.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		zero.setLayoutX(x + w);
		zero.setLayoutY(y + 3 * w);
		zero.setPrefWidth(buttonSize);
		zero.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		imgv.setImage(cancelimg);
		imgv.setFitHeight(15);
		imgv.setFitWidth(15);

		cancel.setPrefWidth(100);
		cancel.setPrefHeight(15);
		cancel.setLayoutX(x + 3 * w);
		cancel.setLayoutY(y);
		cancel.setStyle("-fx-background-color: #FE7669 ; -fx-font-weight: bold");

		imgv1.setImage(clearimg);
		imgv1.setFitHeight(15);
		imgv1.setFitWidth(15);

		imgv2.setImage(enterimg);
		imgv2.setFitHeight(15);
		imgv2.setFitWidth(15);

		imgv4.setImage(choixlangueimg);
		imgv4.setFitHeight(50);
		imgv4.setFitWidth(70);

		homeimgv.setImage(homeimg);
		homeimgv.setFitHeight(50);
		homeimgv.setFitWidth(50);

		imgview0.setImage(img0);
		imgview1.setImage(img1);
		imgview2.setImage(img2);
		imgview3.setImage(img3);
		imgview4.setImage(img4);
		imgview5.setImage(img5);
		imgview6.setImage(img6);
		imgview7.setImage(img7);
		imgview8.setImage(img8);
		imgview9.setImage(img9);
		infoimgv.setImage(info);

		imgview0.setFitHeight(15);
		imgview0.setFitWidth(15);

		imgview1.setFitHeight(15);
		imgview1.setFitWidth(15);

		imgview2.setFitHeight(15);
		imgview2.setFitWidth(15);

		imgview3.setFitHeight(15);
		imgview3.setFitWidth(15);

		imgview4.setFitHeight(15);
		imgview4.setFitWidth(15);

		imgview5.setFitHeight(15);
		imgview5.setFitWidth(15);

		imgview6.setFitHeight(15);
		imgview6.setFitWidth(15);

		imgview7.setFitHeight(15);
		imgview7.setFitWidth(15);

		imgview8.setFitHeight(15);
		imgview8.setFitWidth(15);

		imgview9.setFitHeight(15);
		imgview9.setFitWidth(15);

		infoimgv.setFitHeight(50);
		infoimgv.setFitWidth(50);

		try {
			db.connect();
			nbbillet50danslaBDD = db.getBillet("nb_billet50");
			nbbillet20danslaBDD = db.getBillet("nb_billet20");
			nbbillet10danslaBDD = db.getBillet("nb_billet10");

			db.disconnect();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		Tooltip tooltip_GAB = new Tooltip("Billets disponibles :\n Billets de 50 : " + nbbillet50danslaBDD
				+ "\n Billets de 20 : " + nbbillet20danslaBDD + "\n Billets de 100 : " + nbbillet10danslaBDD);
		tooltip_GAB.setShowDelay(Duration.millis(100));

		Tooltip.install(infobutton, tooltip_GAB);

		infobutton.setStyle("-fx-background-radius: 50em; " + 
				"-fx-min-width: 50px; " + 
				"-fx-min-height: 50px; " + 
				"-fx-max-width: 50px; " + 
				"-fx-max-height: 50px; " + 
				"-fx-text-fill: white;" 
		);

	
		infobutton.setLayoutX(690);
		infobutton.setLayoutY(410);

		leftup.setLayoutX(10);
		leftup.setLayoutY(240);
		leftup.setPrefWidth(buttonSize);
		leftup.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		leftdown.setLayoutX(10);
		leftdown.setLayoutY(330);
		leftdown.setPrefWidth(buttonSize);
		leftdown.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		rightup.setLayoutX(760);
		rightup.setLayoutY(240);
		rightup.setPrefWidth(buttonSize);
		rightup.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: bold");

		rightdown.setLayoutX(760);
		rightdown.setLayoutY(330);
		rightdown.setPrefWidth(buttonSize);
		rightdown.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight: 800");

		choixlangue.setLayoutX(50);
		choixlangue.setLayoutY(10);
		choixlangue.setPrefWidth(70);
		choixlangue.setPrefHeight(50);
		choixlangue.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight:bold");
		choixlangue.setPadding(Insets.EMPTY);

		home.setLayoutX(680);
		home.setLayoutY(10);
		home.setPrefWidth(70);
		home.setPrefHeight(50);
		home.setStyle("-fx-background-color: #B6C1EC ; -fx-font-weight:bold");
		home.setPadding(Insets.EMPTY);

		clear.setPrefWidth(100);
		clear.setPrefHeight(15);
		clear.setLayoutX(x + 3 * w);
		clear.setLayoutY(y + w);
		clear.setStyle("-fx-background-color: #FFF45C; -fx-font-weight: bold");

		enter.setPrefWidth(100);
		enter.setPrefHeight(15);
		enter.setLayoutX(x + 3 * w);
		enter.setLayoutY(y + 2 * w);
		enter.setStyle("-fx-background-color: #6DCAA8 ; -fx-font-weight: bold");

		rightupText.setStyle("-fx-background-color: #6DCAA8 ; -fx-font-weight: bold");
		rightdownText.setStyle("-fx-background-color: #6DCAA8 ; -fx-font-weight: bold");
		leftdownText.setStyle("-fx-background-color: #6DCAA8 ; -fx-font-weight: bold");
		leftupText.setStyle("-fx-background-color: #6DCAA8 ; -fx-font-weight: bold");

		rightupText.setFont(Font.font("Verdana", 12));
		rightdownText.setFont(Font.font("Verdana", 12));
		leftdownText.setFont(Font.font("Verdana", 12));
		leftupText.setFont(Font.font("Verdana", 12));

		userText.setX(centerX - userText.getBoundsInLocal().getWidth() / 2);

		screenText.setX(centerX - screenText.getBoundsInLocal().getWidth() / 2);

		mainText.setX(centerX - mainText.getBoundsInLocal().getWidth() / 2);

		gif.setImage(gifimg);
		gif.setFitWidth(250);
		gif.setFitHeight(250);
		gif.setX(270);
		gif.setY(250);

		wait.setImage(waitimg);
		wait.setFitWidth(100);
		wait.setFitHeight(100);
		wait.setX(120);
		wait.setY(200);

		saisieCodePIN.setEditable(false); // ceci force l'utilisateur à utiliser le pavé numérique présent sur la
											// fenetre
		saisieCodePIN.setLayoutX(370);
		saisieCodePIN.setLayoutY(350);
		saisieCodePIN.setPrefHeight(25);
		saisieCodePIN.setPrefWidth(80);

		saisieSomme.setEditable(false);
		saisieSomme.setLayoutX(370);
		saisieSomme.setLayoutY(300);
		saisieSomme.setPrefHeight(25);
		saisieSomme.setPrefWidth(80);

		// on ajoute tous les éléments au "groupe" de la fenetre

		root.getChildren().add(one);
		root.getChildren().add(two);
		root.getChildren().add(three);
		root.getChildren().add(four);
		root.getChildren().add(five);
		root.getChildren().add(six);
		root.getChildren().add(seven);
		root.getChildren().add(eight);
		root.getChildren().add(nine);
		root.getChildren().add(zero);
		root.getChildren().add(cancel);
		root.getChildren().add(clear);
		root.getChildren().add(enter);
		root.getChildren().add(ecran);
		root.getChildren().add(screenText);
		root.getChildren().add(screenText2);
		root.getChildren().add(leftup);
		root.getChildren().add(leftdown);
		root.getChildren().add(rightup);
		root.getChildren().add(rightdown);
		root.getChildren().add(choixlangue);
		root.getChildren().add(rightupText);
		root.getChildren().add(leftupText);
		root.getChildren().add(rightdownText);
		root.getChildren().add(leftdownText);
		root.getChildren().add(mainText);
		root.getChildren().add(userText);
		root.getChildren().add(home);
		root.getChildren().add(gif);
		root.getChildren().add(infobutton);

		logo.setImage(logoimg);
		logo.setFitHeight(100);
		logo.setFitWidth(300);

		stage.setScene(scene);
		stage.setResizable(false);
		stage.getIcons().add(logoimg); // permet d'ajouter un logo à l'application
		stage.show();

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param clear Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *              Efface le contenu des champs de saisie
		 */

		clear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				userPINCode = "";
				saisieCodePIN.clear();
				saisieSomme.clear();

			}

		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param cancel Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *               Annule la transaction en cours
		 */

		cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				currentScreenState = CANCEL;
				reAfficherEcran();

			}
		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param choixlangue Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *                    Change la langue du GAB (français / anglais)
		 */

		choixlangue.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				isInFrench = !isInFrench;

				if (!isInFrench) {
					reAfficherEcran();
					choixlangueimg = new Image(getClass().getResourceAsStream("images/france.png"));
					imgv4.setImage(choixlangueimg);

				} else {
					reAfficherEcran();
					choixlangueimg = new Image(getClass().getResourceAsStream("images/angleterre.png"));
					imgv4.setImage(choixlangueimg);

				}

			}

		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param one Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *            Ajoute "1" au champ de texte
		 */

		one.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (currentScreenState == ENTER_PIN_WITHDRAW || currentScreenState == ENTER_PIN_DEPOSIT
						|| currentScreenState == ENTER_PIN_BALANCECHECK || currentScreenState == INCORRECT_PIN_WITHDRAW
						|| currentScreenState == INCORRECT_PIN_DEPOSIT
						|| currentScreenState == INCCORRECT_PIN_BALANCECHECK) {

					saisieCodePIN.appendText("1");
					saisieCodePIN.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				} else {

					saisieSomme.appendText("1");
					saisieSomme.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				}

			}
		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param two Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *            Ajoute "2" au champ de texte
		 */

		two.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (currentScreenState == ENTER_PIN_WITHDRAW || currentScreenState == ENTER_PIN_DEPOSIT
						|| currentScreenState == ENTER_PIN_BALANCECHECK || currentScreenState == INCORRECT_PIN_WITHDRAW
						|| currentScreenState == INCORRECT_PIN_DEPOSIT
						|| currentScreenState == INCCORRECT_PIN_BALANCECHECK) {

					saisieCodePIN.appendText("2");
					saisieCodePIN.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				} else {
					saisieSomme.appendText("2");
					saisieSomme.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				}

			}
		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param three Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *              Ajoute "3" au champ de texte
		 */

		three.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (currentScreenState == ENTER_PIN_WITHDRAW || currentScreenState == ENTER_PIN_DEPOSIT
						|| currentScreenState == ENTER_PIN_BALANCECHECK || currentScreenState == INCORRECT_PIN_WITHDRAW
						|| currentScreenState == INCORRECT_PIN_DEPOSIT
						|| currentScreenState == INCCORRECT_PIN_BALANCECHECK) {

					saisieCodePIN.appendText("3");
					saisieCodePIN.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				} else {
					saisieSomme.appendText("3");
					saisieSomme.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				}

			}
		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param four Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *             Ajoute "4" au champ de texte
		 */

		four.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (currentScreenState == ENTER_PIN_WITHDRAW || currentScreenState == ENTER_PIN_DEPOSIT
						|| currentScreenState == ENTER_PIN_BALANCECHECK || currentScreenState == INCORRECT_PIN_WITHDRAW
						|| currentScreenState == INCORRECT_PIN_DEPOSIT
						|| currentScreenState == INCCORRECT_PIN_BALANCECHECK) {

					saisieCodePIN.appendText("4");
					saisieCodePIN.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				} else {
					saisieSomme.appendText("4");
					saisieSomme.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				}

			}
		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param five Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *             Ajoute "5" au champ de texte
		 */

		five.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (currentScreenState == ENTER_PIN_WITHDRAW || currentScreenState == ENTER_PIN_DEPOSIT
						|| currentScreenState == ENTER_PIN_BALANCECHECK || currentScreenState == INCORRECT_PIN_WITHDRAW
						|| currentScreenState == INCORRECT_PIN_DEPOSIT
						|| currentScreenState == INCCORRECT_PIN_BALANCECHECK) {

					saisieCodePIN.appendText("5");
					saisieCodePIN.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				} else {
					saisieSomme.appendText("5");
					saisieSomme.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				}

			}
		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param six Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *            Ajoute "6" au champ de texte
		 */

		six.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (currentScreenState == ENTER_PIN_WITHDRAW || currentScreenState == ENTER_PIN_DEPOSIT
						|| currentScreenState == ENTER_PIN_BALANCECHECK || currentScreenState == INCORRECT_PIN_WITHDRAW
						|| currentScreenState == INCORRECT_PIN_DEPOSIT
						|| currentScreenState == INCCORRECT_PIN_BALANCECHECK) {

					saisieCodePIN.appendText("6");
					saisieCodePIN.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				} else {
					saisieSomme.appendText("6");
					saisieSomme.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				}

			}
		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param seven Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *              Ajoute "7" au champ de texte
		 */

		seven.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (currentScreenState == ENTER_PIN_WITHDRAW || currentScreenState == ENTER_PIN_DEPOSIT
						|| currentScreenState == ENTER_PIN_BALANCECHECK || currentScreenState == INCORRECT_PIN_WITHDRAW
						|| currentScreenState == INCORRECT_PIN_DEPOSIT
						|| currentScreenState == INCCORRECT_PIN_BALANCECHECK) {

					saisieCodePIN.appendText("7");
					saisieCodePIN.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				} else {
					saisieSomme.appendText("7");
					saisieSomme.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				}

			}
		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param eight Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *              Ajoute "8" au champ de texte
		 */

		eight.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (currentScreenState == ENTER_PIN_WITHDRAW || currentScreenState == ENTER_PIN_DEPOSIT
						|| currentScreenState == ENTER_PIN_BALANCECHECK || currentScreenState == INCORRECT_PIN_WITHDRAW
						|| currentScreenState == INCORRECT_PIN_DEPOSIT
						|| currentScreenState == INCCORRECT_PIN_BALANCECHECK) {

					saisieCodePIN.appendText("8");
					saisieCodePIN.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				} else {
					saisieSomme.appendText("8");
					saisieSomme.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				}

			}
		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param nine Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *             Ajoute "9" au champ de texte
		 */

		nine.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (currentScreenState == ENTER_PIN_WITHDRAW || currentScreenState == ENTER_PIN_DEPOSIT
						|| currentScreenState == ENTER_PIN_BALANCECHECK || currentScreenState == INCORRECT_PIN_WITHDRAW
						|| currentScreenState == INCORRECT_PIN_DEPOSIT
						|| currentScreenState == INCCORRECT_PIN_BALANCECHECK) {

					saisieCodePIN.appendText("9");
					saisieCodePIN.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				} else {
					saisieSomme.appendText("9");
					saisieSomme.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				}

			}
		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param zero Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *             Ajoute "0" au champ de texte
		 */

		zero.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (currentScreenState == ENTER_PIN_WITHDRAW || currentScreenState == ENTER_PIN_DEPOSIT
						|| currentScreenState == ENTER_PIN_BALANCECHECK || currentScreenState == INCORRECT_PIN_WITHDRAW
						|| currentScreenState == INCORRECT_PIN_DEPOSIT
						|| currentScreenState == INCCORRECT_PIN_BALANCECHECK) {

					saisieCodePIN.appendText("0");
					saisieCodePIN.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				} else {
					saisieSomme.appendText("0");
					saisieSomme.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
				}

			}
		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param home Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *             Retourne à la page d'accueil des choix de transactions
		 */

		home.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				root.getChildren().remove(webView);
				root.getChildren().remove(saisieSomme);
				root.getChildren().remove(saisieCodePIN);

				saisieCodePIN.clear();
				saisieSomme.clear();

				currentScreenState = FIRST_CHOICES;
				reAfficherEcran();
			}

		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param enter Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *              Change la page de l'interface graphique en fonction de l'état
		 *              actuel
		 */

		enter.setOnAction(new EventHandler<ActionEvent>() {

			@Override

			public void handle(ActionEvent event) {

				boolean idSucces = false;

				if (currentScreenState == ENTER_PIN_DEPOSIT ||

						currentScreenState == ENTER_PIN_WITHDRAW

						|| currentScreenState == ENTER_PIN_BALANCECHECK

						|| currentScreenState == INCORRECT_PIN_WITHDRAW

						|| currentScreenState == INCORRECT_PIN_DEPOSIT

						|| currentScreenState == INCCORRECT_PIN_BALANCECHECK) {

					try {

						idSucces = centralModule.getMDPSuccess();

					} catch (SQLException e) {

						e.printStackTrace();

					}

					if (idSucces) {

						if (currentScreenState == ENTER_PIN_DEPOSIT) {

							currentScreenState = SUM_TODEPOSIT;

							reAfficherEcran();

						} else if (currentScreenState == ENTER_PIN_WITHDRAW) {

							currentScreenState = SUM_TOWITHDRAW;

							reAfficherEcran();

						} else if (currentScreenState == ENTER_PIN_BALANCECHECK) {

							currentScreenState = BALANCE_CHECK;

							reAfficherEcran();

						} else if (currentScreenState == INCORRECT_PIN_WITHDRAW) {

							currentScreenState = SUM_TOWITHDRAW;

							reAfficherEcran();

						} else if (currentScreenState == INCCORRECT_PIN_BALANCECHECK) {

							currentScreenState = BALANCE_CHECK;

							reAfficherEcran();

						}

						else if (currentScreenState == INCORRECT_PIN_DEPOSIT) {

							currentScreenState = SUM_TODEPOSIT;

							reAfficherEcran();

						}

					} else {

						if (currentScreenState == ENTER_PIN_DEPOSIT) {

							currentScreenState = INCORRECT_PIN_DEPOSIT;

							reAfficherEcran();

						} else if (currentScreenState == ENTER_PIN_WITHDRAW) {

							currentScreenState = INCORRECT_PIN_WITHDRAW;

							reAfficherEcran();

						} else if (currentScreenState == ENTER_PIN_BALANCECHECK) {

							currentScreenState = INCCORRECT_PIN_BALANCECHECK;

							reAfficherEcran();

						} else if (currentScreenState == INCORRECT_PIN_WITHDRAW) {

							currentScreenState = INCORRECT_PIN_WITHDRAW;

							reAfficherEcran();

						}

						else if (currentScreenState == INCCORRECT_PIN_BALANCECHECK) {

							currentScreenState = INCCORRECT_PIN_BALANCECHECK;

							reAfficherEcran();

						} else if (currentScreenState == INCORRECT_PIN_DEPOSIT) {

							currentScreenState = INCORRECT_PIN_DEPOSIT;

							reAfficherEcran();

						}

					}

				}

				else if (currentScreenState == SUM_TODEPOSIT) {

					root.getChildren().remove(saisieSomme);

					currentScreenState = SUCCES_DEPOSIT;

					reAfficherEcran();

				}

				else if (currentScreenState == SUM_TOWITHDRAW) {
					root.getChildren().remove(saisieSomme);

					currentScreenState = SUCCES_WITHDRAW;

					reAfficherEcran();

				}

			}

		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param leftdown Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *                 Effectue l'action demandée (affichage d'un navigateur web ou
		 *                 une autre transaction en fonction de l'état actuel)
		 */

		leftdown.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (currentScreenState == FIRST_CHOICES) {

					currentScreenState = CURRENCY;

					webEngine.load("https://www.google.com/");

					webView.setLayoutX(50);
					webView.setLayoutY(70);
					webView.setPrefSize(700, 400);

					root.getChildren().add(webView);

				} else if (currentScreenState == SUCCES_WITHDRAW || currentScreenState == SUCCES_DEPOSIT
						|| currentScreenState == BALANCE_CHECK) {

					currentScreenState = FIRST_CHOICES_NOPIN;
					reAfficherEcran();
				}

			}
		});

		/**
		 * la methode "setOnAction" permet de définir l'action que l'on souhaite
		 * effectuer lorsque le bouton en question est pressé
		 * 
		 * @param leftup Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *               Effectue l'action demandée (consultation du solde ou fermeture
		 *               du GAB )
		 */

		leftup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (currentScreenState == FIRST_CHOICES) {

					currentScreenState = ENTER_PIN_BALANCECHECK;
					reAfficherEcran();

				} else if (currentScreenState == FIRST_CHOICES_NOPIN) {

					currentScreenState = BALANCE_CHECK;
					reAfficherEcran();

				} else if (currentScreenState == SUCCES_WITHDRAW || currentScreenState == SUCCES_DEPOSIT
						|| currentScreenState == BALANCE_CHECK) {

					stage.close();
				}

			}
		});

		rightdown.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (currentScreenState == FIRST_CHOICES) {

					currentScreenState = ENTER_PIN_DEPOSIT;
					reAfficherEcran();

				} else if (currentScreenState == FIRST_CHOICES_NOPIN) {

					currentScreenState = SUM_TODEPOSIT;
					reAfficherEcran();

				} else if (currentScreenState == SUCCES_WITHDRAW) {

					try {
						db.connect();
						if (db.impression()) {

							ticket = new Ticket(0, isInFrench);
							Text text5 = new Text(saisieSomme.getText() + "euros");
							ticket.getGridPane().add(text5, 1, 5);

							db.disconnect();

						} else {

							screenText2.setText("ENCRE/PAPIER INDISPONIBLE");
							tts.speak("ENCRE/PAPIER INDISPONIBLE");

						}
					} catch (SQLException e) {

					}

				} else if (currentScreenState == SUCCES_DEPOSIT) {

					try {
						db.connect();
						if (db.impression()) {

							ticket = new Ticket(1, isInFrench);
							Text text5 = new Text(saisieSomme.getText() + "euros");
							ticket.getGridPane().add(text5, 1, 5);

							db.disconnect();

						} else {

							screenText2.setText("ENCRE/PAPIER INDISPONIBLE");
							tts.speak("ENCRE/PAPIER INDISPONIBLE");

						}
					} catch (SQLException e) {

					}

				} else if (currentScreenState == BALANCE_CHECK) {

					try {
						db.connect();
						if (db.impression()) {
							
							double solde = db.getSolde("gab.carte.code_atr = '" + cardModule.getStringATR() + "' AND id_compte = id");
							ticket = new Ticket(3, isInFrench);
							Text text5 = new Text(solde + "euros");
							ticket.getGridPane().add(text5, 1, 5);

							db.disconnect();

						} else {

							screenText2.setText("ENCRE/PAPIER INDISPONIBLE");
							tts.speak("ENCRE/PAPIER INDISPONIBLE");

						}
					} catch (SQLException e) {

					}

				}
			}
		});

		rightup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (currentScreenState == FIRST_CHOICES) {

					currentScreenState = ENTER_PIN_WITHDRAW;
					reAfficherEcran();

				} else if (currentScreenState == SUCCES_WITHDRAW) {

					currentScreenState = FIRST_CHOICES_NOPIN;

					reAfficherEcran();

				} else if (currentScreenState == FIRST_CHOICES_NOPIN) {

					currentScreenState = SUM_TOWITHDRAW;

					reAfficherEcran();

				}

			}
		});

		/**
		 * la méthode "setOnMouseEntered" permet de définir l'action à effectuer quand
		 * le bouton en question est survolé à l'aide de la souris
		 * 
		 * 
		 * @param clear Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *              Dicte l'action effectuée lors du survol du bouton clear
		 */

		clear.setOnMouseEntered(e -> {

			if (isInFrench()) {

				tts.setVoice("upmc-pierre-hsmm");
				tts.speak("effacer");

			} else {
				tts.setVoice("cmu-slt-hsmm");
				tts.speak("clear");

			}

		});

		/**
		 * la méthode "setOnMouseEntered" permet de définir l'action à effectuer quand
		 * le bouton en question est survolé à l'aide de la souris
		 * 
		 * 
		 * @param cancel Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *               Dicte l'action effectuée lors du survol du bouton cancel
		 */

		cancel.setOnMouseEntered(e -> {

			if (isInFrench()) {

				tts.setVoice("upmc-pierre-hsmm");
				tts.speak("annuler");

			} else {
				tts.setVoice("cmu-slt-hsmm");
				tts.speak("cancel");

			}

		});

		/**
		 * la méthode "setOnMouseEntered" permet de définir l'action à effectuer quand
		 * le bouton en question est survolé à l'aide de la souris
		 * 
		 * 
		 * @param enter Indique quel bouton doit effectuer les actions spécifiées
		 * 
		 *              Dicte l'action effectuée lors du survol du bouton enter
		 */

		enter.setOnMouseEntered(e -> {

			if (isInFrench()) {

				tts.setVoice("upmc-pierre-hsmm");
				tts.speak("entrer");

			} else {
				tts.setVoice("cmu-slt-hsmm");
				tts.speak("enter");

			}

		});

	}

	/**
	 * Cette méthode permet de réafficher les textes dans la langue souhaitée
	 */

	public void reAfficherEcran() {

		if (isInFrench()) {
			try {
				tts.setVoice("upmc-pierre-hsmm");
				centralModule.setInFrench();
			} catch (SQLException e) {

				e.printStackTrace();
			}

		} else {
			try {
				centralModule.setInEnglish();
			} catch (SQLException e) {

				e.printStackTrace();
			}

		}

	}

//////////////////// GETTERS

	public static int getAnotherTransaction() {
		return ANOTHER_TRANSACTION;
	}

	public int getFirstChoices() {
		return FIRST_CHOICES;
	}

	public int getInsertCard() {
		return INSERT_CARD;
	}

	public int getWaiting() {
		return WAITING;
	}

	public int getCancel() {
		return CANCEL;
	}

	public int getIncorrectPinWithdraw() {
		return INCORRECT_PIN_WITHDRAW;
	}

	public int getIncorrectPinDeposit() {
		return INCORRECT_PIN_DEPOSIT;
	}

	public int getIncorrectPinBalanceCheck() {
		return INCCORRECT_PIN_BALANCECHECK;
	}

	public int getBalanceCheck() {
		return BALANCE_CHECK;
	}

	public int getCurrency() {
		return CURRENCY;
	}

	public int getCustomerArea() {
		return CUSTOMER_AREA;
	}

	public int getIncorrectPinX3() {
		return INCORRECT_PIN_X3;
	}

	public int getSumTowithdraw() {
		return SUM_TOWITHDRAW;
	}

	public int getSumTodeposit() {
		return SUM_TODEPOSIT;
	}

	public Group getGroup() {
		return root;
	}

	public ImageView getGif() {
		return gif;
	}

	public Text getscreenText() {
		return screenText;
	}

	public Text getleftdownText() {
		return leftdownText;
	}

	public Text getrightdownText() {
		return rightdownText;
	}

	public Text getleftupText() {
		return leftupText;
	}

	public Text getrightupText() {
		return rightupText;
	}

	public ImageView getWaitImage() {
		return wait;
	}

	public Text getscreenText2() {
		return screenText2;
	}

	public Scene getScene() {
		return scene;
	}

	public Stage getStage() {
		return stage;
	}

	public Text getMainText() {
		return mainText;
	}

	public Text getScreenText() {
		return screenText;
	}

	public Text getRightupText() {
		return rightupText;
	}

	public Text getLeftupText() {
		return leftupText;
	}

	public Text getRightdownText() {
		return rightdownText;
	}

	public Text getLeftdownText() {
		return leftdownText;
	}

	public boolean isInFrench() {
		return isInFrench;
	}

	public int getScreenState() {
		return currentScreenState;
	}

	public Button getLeftup() {
		return leftup;
	}

	public Button getLeftdown() {
		return leftdown;
	}

	public Button getRightup() {
		return rightup;
	}

	public Button getRightdown() {
		return rightdown;
	}

	public double getCenterX() {
		return centerX;
	}

	public Rectangle getEcran() {
		return ecran;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public Text getScreenText2() {
		return screenText2;
	}

	public Group getRoot() {
		return root;
	}

	public String getUserPINCode() {
		return userPINCode;
	}

	public PasswordField getSaisieCodePIN() {
		return saisieCodePIN;
	}

	public WebView getWebView() {
		return webView;
	}

	public WebEngine getWebEngine() {
		return webEngine;
	}

	public int getSuccesDeposit() {
		return SUCCES_DEPOSIT;
	}

	public TextField getSaisieSomme() {
		return saisieSomme;
	}

	public int getEnterPinDeposit() {
		return ENTER_PIN_DEPOSIT;
	}

	public int getEnterPinWithdraw() {
		return ENTER_PIN_WITHDRAW;
	}

	public int getSuccesWithdraw() {
		return SUCCES_WITHDRAW;
	}

	public int getEnterPinBalancecheck() {
		return ENTER_PIN_BALANCECHECK;
	}

	public int getFirstChoicesNopin() {
		return FIRST_CHOICES_NOPIN;
	}

	public CardModule getCardModule() {
		return cardModule;
	}

//////////////////// SETTERS

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setMainText(String text) {
		this.mainText.setText(text);
	}

	public void setScreenText(String text) {
		this.screenText.setText(text);
	}

	public void setRightupText(String text) {
		this.rightupText.setText(text);
	}

	public void setLeftupText(String text) {
		this.leftupText.setText(text);
	}

	public void setScreenText2(String text) {
		this.screenText2.setText(text);
	}

	public void setRightdownText(String text) {
		this.rightdownText.setText(text);
	}

	public void setLeftdownText(String text) {
		this.leftdownText.setText(text);
	}

	public void setInFrench(boolean isInFrench) {
		this.isInFrench = isInFrench;
	}

	public void setScreenState(int currentScreenState) {
		this.currentScreenState = currentScreenState;
	}

}
