package application;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

/**
 * Classe centrale du GAB,, regroupant toutes les fonctionnalités.
 * 
 * @author Clara A., Christophe C., Imane B.
 * 
 */

public class CentralModule {

	private Graphics graphics;
	public TextToSpeech tts = new TextToSpeech();
	private SpeechRecognition speechReco;
	private SecurityModule securityModule;
	private HeadphoneDetection headphoneDetection = new HeadphoneDetection();
	private CardModule cardModule;
	private Database db = new Database();
	private int sum;
	private int noPIN;
	private boolean casquePresence = headphoneDetection.isHeadphonesConnected();

	/**
	 * Construit la classe Centrale avec les classes : Graphics cardModule et
	 * securityModule . Cela permet d'�viter les acc�s concurrents et la duplication
	 * d'éléments menant � une erreur
	 * 
	 * @param graphics       (affichage de l'interface graphique)
	 * @param cardModule     (permet la lecture de la carte � puce)
	 * @param securityModule (permet le hashage du code PIN afin de le v�rifier)
	 * 
	 * 
	 */

	public CentralModule(Graphics graphics, CardModule cardModule, SecurityModule securityModule) {

		this.graphics = graphics;
		this.cardModule = cardModule;
		this.securityModule = securityModule;

	}

	/**
	 * Fonction qui affiche les éléments de l'interface graphique en français,, en
	 * utilisant plusieurs éléments de l'interface graphique
	 * 
	 * Le fonctionnement se base sur des variables finales de la classe Graphics qui
	 * indiquent précisément où se trouve l'utilisateur afin d'effectuer les actions
	 * liés aux boutons de manière cohérente
	 * 
	 * @throws SQLException si la connexion à la base de données échoue
	 */

	public void setInFrench() throws SQLException {

		tts.setVoice("upmc-pierre-hsmm");

		String cardATR = cardModule.getStringATR();

		graphics.setMainText("GUICHET AUTOMATIQUE BANCAIRE");

		if (graphics.getScreenState() == graphics.getInsertCard()) {

			if (casquePresence) {

				tts.speak("Bienvenue sur votre guichet L 2 C 1 . insérez votre carte");

			}

			graphics.setScreenText("INSEREZ VOTRE CARTE");

			graphics.getScreenText()
					.setX(graphics.getCenterX() - graphics.getScreenText().getBoundsInLocal().getWidth() / 2);

		}

		else if (graphics.getScreenState() == graphics.getWaiting()) {

			int BDDpresence = checkRIB(cardATR);

			if (BDDpresence == 0) {
				graphics.setScreenText2("CARTE INSEREE, VEUILLEZ PATIENTER");

				if (casquePresence)
					tts.speak("CARTE insérée, VEUILLEZ PATIENTER");

			} else if (BDDpresence == 1) {
				graphics.setScreenText2("CARTE INSEREE, \n  NOUS INTERROGEONS VOTRE BANQUE");

				if (casquePresence)
					tts.speak("CARTE INSérée,NOUS INTERROGEONS VOTRE BANQUE");

			} else {
				graphics.setScreenText2("CARTE NON RECONNUE \n VEUILLEZ RECUPERER VOTRE CARTE");

				if (casquePresence)
					tts.speak("CARTE NON RECONNUE, VEUILLEZ récupérer VOTRE CARTE");

				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						Platform.runLater(() -> {
							System.exit(1);
						});

					}
				}, 3500);

			}

		}

		else if (graphics.getScreenState() == graphics.getFirstChoices()
				|| graphics.getScreenState() == graphics.getFirstChoicesNopin()) {

			graphics.setScreenText("");
			graphics.setScreenText2("");
			graphics.setRightupText("RETRAIT");
			graphics.setRightdownText("DEPOT");
			graphics.getLeftupText().setText("CONSULTATION DU SOLDE");
			graphics.getLeftdownText().setText("ACCES WEB");

			graphics.getSaisieSomme().clear();
			graphics.getLeftupText().setX(graphics.getScene().getX() + 60);
			graphics.getLeftupText()
					.setY(graphics.getRightup().getLayoutY() + graphics.getRightup().getPrefWidth() / 2);
			graphics.getLeftdownText().setX(graphics.getScene().getX() + 60);
			graphics.getLeftdownText()
					.setY(graphics.getRightdown().getLayoutY() + graphics.getRightdown().getPrefWidth() / 2);
			graphics.getRightupText().setX(
					graphics.getEcran().getWidth() - (graphics.getRightupText().getBoundsInLocal().getWidth() - 20));
			graphics.getRightupText()
					.setY(graphics.getRightup().getLayoutY() + graphics.getRightup().getPrefWidth() / 2);
			graphics.getRightdownText().setX(
					graphics.getEcran().getWidth() - (graphics.getRightdownText().getBoundsInLocal().getWidth() - 20));
			graphics.getRightdownText()
					.setY(graphics.getLeftdown().getLayoutY() + graphics.getLeftdown().getPrefWidth() / 2);

			if (casquePresence) {

				tts.speak("quelle transaction souhaitez vous effectuer ? ");

				new Thread(() -> {

					try {

						if (speechReco == null) {

							speechReco = new SpeechRecognition();
							noPIN = 0;

						}

						Thread.sleep(2000);

						String speech = speechReco.waitForResult();

						Platform.runLater(() -> {

							if (speech.equals("depot")) {

								if (noPIN != 0) {

									graphics.setScreenState(graphics.getSumTodeposit());

								} else {
									graphics.setScreenState(graphics.getEnterPinDeposit());
									noPIN++;
								}

								graphics.reAfficherEcran();

							} else if (speech.equals("consulter de solde") || speech.equals("consulter le solde")) {

								if (noPIN != 0) {

									graphics.setScreenState(graphics.getBalanceCheck());
								} else {
									graphics.setScreenState(graphics.getEnterPinBalancecheck());
									noPIN++;
								}
								graphics.reAfficherEcran();

							} else if (speech.equals("retrait")) {

								if (noPIN != 0) {

									graphics.setScreenState(graphics.getSumTowithdraw());
								} else {
									graphics.setScreenState(graphics.getEnterPinWithdraw());
									noPIN++;
								}
								graphics.reAfficherEcran();

							} else {
								if (casquePresence)
									tts.speak("Désolé, je n'ai pas compris votre demande ");

								new Timer().schedule(new TimerTask() {
									@Override
									public void run() {
										Platform.runLater(() -> {
											graphics.setScreenState(graphics.getFirstChoices());
											graphics.reAfficherEcran();
										});

									}
								}, 4000);
							}

						});

					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}).start();

			}

		} else if (graphics.getScreenState() == graphics.getEnterPinBalancecheck()
				|| graphics.getScreenState() == graphics.getEnterPinDeposit()
				|| graphics.getScreenState() == graphics.getEnterPinWithdraw()) {

			graphics.setScreenText("ENTREZ VOTRE CODE PIN");

			if (casquePresence) {

				if (graphics.getScreenState() == graphics.getEnterPinBalancecheck()) {

					if (casquePresence)
						tts.speak(
								" d'accord, effectuons une consultation du solde . utilisez le pavé numérique en braille pour entrer votre code pine  ");

				} else if (graphics.getScreenState() == graphics.getEnterPinDeposit()) {

					if (casquePresence)
						tts.speak(
								" d'accord, effectuons un dépôt . utilisez le pavé numérique en braille pour entrer votre code pine  ");

				} else {

					if (casquePresence)
						tts.speak(
								" d'accord, effectuons un retrait . utilisez le pavé numérique en braille pour entrer votre code pine  ");

				}

			}
			graphics.getScreenText().setY(200);

			graphics.setRightupText("");
			graphics.setRightdownText("");
			graphics.setLeftupText("");
			graphics.setLeftdownText("");

			graphics.getRoot().getChildren().add(graphics.getSaisieCodePIN());

		}

		else if (graphics.getScreenState() == graphics.getCancel()) {

			graphics.getGroup().getChildren().add(graphics.getWaitImage());
			graphics.setScreenText("ANNULATION EN COURS");

			if (casquePresence)
				tts.speak("ANNULATION EN COURS");
			graphics.getScreenText().setY(160);

			graphics.getRoot().getChildren().remove(graphics.getSaisieCodePIN());
			graphics.setRightupText("");
			graphics.setRightdownText("");
			graphics.setLeftupText("");
			graphics.setLeftdownText("");
			graphics.setScreenText2("");
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					Platform.runLater(() -> {
						System.exit(1);
						
					});

				}
			}, 1700);
			graphics.getScreenText()
					.setX(graphics.getCenterX() - graphics.getScreenText().getBoundsInLocal().getWidth() / 2);

		} else if (graphics.getScreenState() == graphics.getSumTodeposit()) {

			graphics.setRightupText("");
			graphics.setRightdownText("");
			graphics.getLeftupText().setText("");
			graphics.getLeftdownText().setText("");

			graphics.getScreenText2().setText("");

			graphics.setScreenText("ENTREZ LA SOMME QUE VOUS SOUHAITEZ DEPOSER");

			if (casquePresence)
				tts.speak("ENTREZ LA SOMME QUE VOUS SOUHAITEZ DéPOSER");
			graphics.getscreenText().setX(50);
			graphics.getScreenText().setY(160);
			graphics.getRoot().getChildren().remove(graphics.getSaisieCodePIN());
			graphics.getRoot().getChildren().add(graphics.getSaisieSomme());

		}

		else if (graphics.getScreenState() == graphics.getSuccesDeposit()) {

			if (casquePresence) {

				graphics.getRightup().setOnMouseEntered(null);

				graphics.getRightdown().setOnMouseEntered(null);

				graphics.getLeftup().setOnMouseEntered(null);

				graphics.getLeftdown().setOnMouseEntered(null);

			}

			graphics.setRightupText("");
			graphics.setRightdownText("");
			graphics.getLeftupText().setText("");
			graphics.getLeftdownText().setText("");

			graphics.getScreenText2().setText("");

			db.connect();

			sum = Integer.parseInt(graphics.getSaisieSomme().getText());

			graphics.getRoot().getChildren().remove(graphics.getSaisieSomme());
			db.depot(sum, "carte.code_atr = '" + cardATR + "' AND carte.id_compte = id",
					"carte.code_atr = '" + cardATR + "'");
			db.disconnect();

			graphics.getScreenText().setX(
					graphics.getEcran().getWidth() - (graphics.getScreenText().getBoundsInLocal().getWidth() - 20));
			graphics.setScreenText("DEPOT EFFECTUE AVEC SUCCES");
			graphics.getScreenText2().setText("FAIRE UNE AUTRE TRANSACTION ?");

			if (casquePresence)
				tts.speak("dépôt effectué avec succès. souhaitez vous faire une autre transaction ? ");

			graphics.getLeftupText().setText("NON");
			graphics.getLeftdownText().setText("OUI");
			graphics.getRightdownText().setText("TICKET");

			graphics.getLeftup().setOnMouseEntered(e -> {

				if (casquePresence)
					tts.speak("bouton haut gauche : non . ");
			});

			graphics.getLeftdown().setOnMouseEntered(e -> {

				if (casquePresence)
					tts.speak("bouton bas gauche : oui . ");
			});

			graphics.getRightdown().setOnMouseEntered(e -> {

				if (casquePresence)
					tts.speak("bouton bas droit : ticket . ");
			});

			graphics.getScreenText2().setX(70);
			graphics.getScreenText2().setY(200);

		} else if (graphics.getScreenState() == graphics.getSumTowithdraw()) {

			graphics.setRightupText("");
			graphics.setRightdownText("");
			graphics.getLeftupText().setText("");
			graphics.getLeftdownText().setText("");

			graphics.getScreenText2().setText("");

			graphics.setScreenText("ENTREZ LA SOMME QUE VOUS SOUHAITEZ RETIRER");

			if (casquePresence)

				tts.speak("entrez la somme que vous souhaitez retirer .  ");
			graphics.getscreenText().setX(50);
			graphics.getScreenText().setY(160);
			graphics.getRoot().getChildren().remove(graphics.getSaisieCodePIN());
			graphics.getRoot().getChildren().add(graphics.getSaisieSomme());

		} else if (graphics.getScreenState() == graphics.getSuccesWithdraw()) {

			if (casquePresence) {

				graphics.getRightup().setOnMouseEntered(null);

				graphics.getRightdown().setOnMouseEntered(null);

				graphics.getLeftup().setOnMouseEntered(null);

				graphics.getLeftdown().setOnMouseEntered(null);

			}

			graphics.setRightupText("");
			graphics.setRightdownText("");
			graphics.getLeftupText().setText("");
			graphics.getLeftdownText().setText("");

			db.connect();

			int sum = Integer.parseInt(graphics.getSaisieSomme().getText());

			double solde = db.getSolde("gab.carte.code_atr = '" + cardATR + "' AND id_compte = id");
			double plafond = db.getPlafond("gab.carte.code_atr = '" + cardATR + "' AND id_compte = id");

			if (db.billetsDisponibles(sum)) {
				if (solde > sum) {

					if (plafond >= sum) {

						new Billets(sum);
						db.retrait(sum, "carte.code_atr = '" + cardATR + "' AND carte.id_compte = id",
								"carte.code_atr = '" + cardATR + "'");

						graphics.setScreenText("VEUILLEZ RECUPERER VOS BILLETS");

						if (casquePresence)
							tts.speak("veuillez récupérer vos billets . souhaitez vous faire une autre transaction ?");
						graphics.getScreenText().setY(200);
						graphics.getRightdownText().setText("TICKET");
					} else {
						graphics.setScreenText("RETRAIT IMPOSSIBLE PLAFOND DEPASSE");

						if (casquePresence)
							tts.speak(
									"retrait impossible, plafond dépassé . souhaitez vous faire une autre transaction ? ");
						graphics.getSaisieSomme().setText("") ; 
						graphics.getScreenText().setY(200);
					}

				} else {

					graphics.setScreenText("RETRAIT IMPOSSIBLE SOLDE INSUFFISANT");

					if (casquePresence)
						tts.speak(
								"retrait impossible, solde insuffisant . souhaitez vous faire une autre transaction ?");
					graphics.getSaisieSomme().setText("") ; 
					
					graphics.getScreenText().setY(200);
				}

			} else {
				graphics.setScreenText("RETRAIT IMPOSSIBLE BILLETS INSUFFISANTS");

				if (casquePresence)
					tts.speak(
							"retrait impossible, billets insuffisants . souhaitez vous faire une autre transaction ? ");
				
				graphics.getSaisieSomme().setText("") ; 
				graphics.getScreenText().setY(200);

				db.disconnect();

			}

			graphics.getScreenText().setY(200);
			graphics.getRightdownText().setText("TICKET");

			graphics.getScreenText2().setText("FAIRE UNE AUTRE TRANSACTION ?");

			graphics.getLeftupText().setText("NON");
			graphics.getLeftdownText().setText("OUI");

			graphics.getLeftup().setOnMouseEntered(e -> {

				if (casquePresence)
					tts.speak("bouton haut gauche : non . ");
			});

			graphics.getLeftdown().setOnMouseEntered(e -> {

				if (casquePresence)
					tts.speak("bouton bas gauche : oui . ");
			});

			graphics.getRightdown().setOnMouseEntered(e -> {

				if (casquePresence)
					tts.speak("bouton bas droit : ticket . ");
			});

			graphics.getScreenText2().setX(70);
			graphics.getScreenText2().setY(200);
			graphics.getScreenText().setX(
					graphics.getEcran().getWidth() - (graphics.getScreenText().getBoundsInLocal().getWidth() - 20));

			graphics.getScreenText().setY(160);

			graphics.getScreenText2().setX(70);

		} else if (graphics.getScreenState() == graphics.getBalanceCheck()) {

			if (casquePresence) {

				graphics.getRightup().setOnMouseEntered(null);

				graphics.getRightdown().setOnMouseEntered(null);

				graphics.getLeftup().setOnMouseEntered(null);

				graphics.getLeftdown().setOnMouseEntered(null);

			}

			graphics.setRightupText("");
			graphics.setRightdownText("");
			graphics.getLeftupText().setText("");
			graphics.getLeftdownText().setText("");

			graphics.getScreenText2().setText("");

			db.connect();

			double solde = db.getSolde("gab.carte.code_atr = '" + cardATR + "' AND id_compte = id");
			graphics.getGroup().getChildren().remove(graphics.getSaisieCodePIN());

			graphics.setScreenText("VOTRE SOLDE EST DE " + solde + " euros");

			int soldeInt = (int) (solde);
			String soldeString = String.valueOf(soldeInt);

			if (casquePresence)

				tts.speak("votre solde est de ......... " + soldeString
						+ " euros . ... souhaitez vous faire une autre transaction ?");

			graphics.getScreenText().setY(160);

			db.disconnect();

			graphics.getScreenText2().setText("FAIRE UNE AUTRE TRANSACTION ?");

			graphics.getLeftupText().setText("NON");
			graphics.getLeftdownText().setText("OUI");
			graphics.getRightdownText().setText("TICKET");

			graphics.getLeftup().setOnMouseEntered(e -> {

				if (casquePresence)
					tts.speak("bouton haut gauche : non . ");
			});

			graphics.getLeftdown().setOnMouseEntered(e -> {
				if (casquePresence)

					tts.speak("bouton bas gauche : oui . ");
			});

			graphics.getRightdown().setOnMouseEntered(e -> {

				if (casquePresence)
					tts.speak("bouton bas droit : ticket . ");
			});

			graphics.getScreenText2().setX(70);
			graphics.getScreenText2().setY(200);

		} else if (graphics.getScreenState() == graphics.getIncorrectPinBalanceCheck()
				|| graphics.getScreenState() == graphics.getIncorrectPinDeposit()
				|| graphics.getScreenState() == graphics.getIncorrectPinWithdraw()) {

			if (securityModule.getTentative() == 0) {
				graphics.getScreenText2().setText(
						" il ne vous reste plus de tentatives .\n veuillez récupérer votre carte dans votre banque");

				if (casquePresence)
					tts.speak("il ne vous reste plus de tentatives . veuillez récupérer votre carte dans votre banque");
				
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						Platform.runLater(() -> {
							System.exit(1);
						});

					}
				}, 5000);
				graphics.getScreenText2().setX(70);
				graphics.getScreenText2().setY(200);
				graphics.setScreenText("");
				graphics.getRoot().getChildren().remove(graphics.getSaisieCodePIN());

			} else {
				graphics.getScreenText2().setText(
						" code pin incorrect, il vous reste " + securityModule.getTentative() + " tentative(s)");

				if (casquePresence)
					tts.speak("code pine incorrect, il vous reste " + String.valueOf(securityModule.getTentative())
							+ " tentative");
				graphics.getScreenText2().setX(70);
				graphics.getScreenText2().setY(200);
				graphics.getScreenText2().setX(100);
				graphics.getScreenText2().setY(250);

			}

			graphics.getSaisieCodePIN().clear();

		} else {

		}

	}

	/**
	 * Fonction qui affiche les �l�ments de l'linterface graphique en anglais,, en
	 * utilisant plusieurs �l�ments de l'interface graphique
	 */

	public void setInEnglish() throws SQLException {

		tts.setVoice("cmu-slt-hsmm");

		String cardATR = cardModule.getStringATR();

		graphics.setMainText("AUTOMATIC TELLER MACHINE");

		if (graphics.getScreenState() == graphics.getInsertCard()) {

			if (casquePresence) {

				tts.speak("Welcome to your automatic teller machine . insert your card ");

			}

			graphics.setScreenText("INSERT YOUR CARD");

			graphics.getScreenText()
					.setX(graphics.getCenterX() - graphics.getScreenText().getBoundsInLocal().getWidth() / 2);

		}

		else if (graphics.getScreenState() == graphics.getWaiting()) {

			if (casquePresence) {

			}

			int BDDpresence = checkRIB(cardATR);

			if (BDDpresence == 0) {
				graphics.setScreenText2("CARD INSERTED, PLEASE WAIT ");

				if (casquePresence)

					tts.speak("card inserted , please wait ");

			} else if (BDDpresence == 1) {
				graphics.setScreenText2("CARD INSERTED , \n  Please wait, we are querying your bank");

				if (casquePresence)
					tts.speak("Please wait, we are querying your bank");

			} else {
				graphics.setScreenText2("CARD NOT RECOGNIZED \n PLEASE RETRIVE YOUR CARD");

				if (casquePresence)
					tts.speak("CARD NOT RECOGNIZED .  PLEASE RETRIVE YOUR CARD");

				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						Platform.runLater(() -> {
							graphics.getStage().close();
						});

					}
				}, 1500);

			}

		}

		else if (graphics.getScreenState() == graphics.getFirstChoices()
				|| graphics.getScreenState() == graphics.getFirstChoicesNopin()

		) {

			graphics.setScreenText("");
			graphics.setScreenText2("");
			graphics.setRightupText("WITHDRAWAL");
			graphics.setRightdownText("DEPOSIT");
			graphics.getLeftupText().setText("BALANCE CHECK ");
			graphics.getLeftdownText().setText("WEB");

			if (casquePresence) {

				graphics.getRightup().setOnMouseEntered(e -> {

					if (casquePresence)
						tts.speak(" WITHDRAWAL  ");
				});

				graphics.getRightdown().setOnMouseEntered(e -> {

					if (casquePresence)
						tts.speak(" DEPOSIT ");
				});

				graphics.getLeftup().setOnMouseEntered(e -> {

					if (casquePresence)
						tts.speak(" BALANCE CHECK ");
				});

				graphics.getLeftdown().setOnMouseEntered(e -> {

					if (casquePresence)
						tts.speak(" ACCESS TO THE WEB ");
				});

			}
			graphics.getSaisieSomme().clear();

			graphics.getLeftupText().setX(graphics.getScene().getX() + 60);
			graphics.getLeftupText()
					.setY(graphics.getRightup().getLayoutY() + graphics.getRightup().getPrefWidth() / 2);

			graphics.getLeftdownText().setX(graphics.getScene().getX() + 60);
			graphics.getLeftdownText()
					.setY(graphics.getRightdown().getLayoutY() + graphics.getRightdown().getPrefWidth() / 2);
			graphics.getRightupText().setX(
					graphics.getEcran().getWidth() - (graphics.getRightupText().getBoundsInLocal().getWidth() - 20));
			graphics.getRightupText()
					.setY(graphics.getRightup().getLayoutY() + graphics.getRightup().getPrefWidth() / 2);

			graphics.getRightdownText().setX(
					graphics.getEcran().getWidth() - (graphics.getRightdownText().getBoundsInLocal().getWidth() - 20));
			graphics.getRightdownText()
					.setY(graphics.getLeftdown().getLayoutY() + graphics.getLeftdown().getPrefWidth() / 2);
		} else if (graphics.getScreenState() == graphics.getEnterPinBalancecheck()
				|| graphics.getScreenState() == graphics.getEnterPinDeposit()
				|| graphics.getScreenState() == graphics.getEnterPinWithdraw()) {

			graphics.setScreenText(" ENTER YOUR PIN CODE ");

			if (casquePresence) {

				tts.speak("enter your pin code .");

			}
			graphics.getScreenText().setY(200);

			graphics.setRightupText("");
			graphics.setRightdownText("");
			graphics.setLeftupText("");
			graphics.setLeftdownText("");



		}

		else if (graphics.getScreenState() == graphics.getCancel()) {

			graphics.getGroup().getChildren().add(graphics.getWaitImage());

			graphics.setScreenText("CANCELATION IN PROGRESS ");

			if (casquePresence)
				tts.speak(" CANCELATION IN PROGRESS ");
			graphics.getScreenText().setY(160);

			graphics.getRoot().getChildren().remove(graphics.getSaisieCodePIN());
			graphics.setRightupText("");
			graphics.setRightdownText("");
			graphics.setLeftupText("");
			graphics.setLeftdownText("");
			graphics.setScreenText2("");

			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					Platform.runLater(() -> {
						graphics.getGroup().getChildren().remove(graphics.getWaitImage());
						graphics.setScreenText("");
						graphics.getStage().close();
						new Graphics();
					});

				}
			}, 1700);
			graphics.getScreenText()
					.setX(graphics.getCenterX() - graphics.getScreenText().getBoundsInLocal().getWidth() / 2);

		} else if (graphics.getScreenState() == graphics.getSumTodeposit()) {

			graphics.setRightupText("");
			graphics.setRightdownText("");
			graphics.getLeftupText().setText("");
			graphics.getLeftdownText().setText("");

			graphics.getScreenText2().setText("");

			graphics.setScreenText("ENTER THE AMOUNT YOU WANT TO DEPOSIT");

			if (casquePresence)
				tts.speak("\"ENTER THE AMOUNT YOU WANT TO DEPOSIT");
			graphics.getscreenText().setX(50);
			graphics.getScreenText().setY(160);
			graphics.getRoot().getChildren().remove(graphics.getSaisieCodePIN());
			graphics.getRoot().getChildren().add(graphics.getSaisieSomme());

		}

		else if (graphics.getScreenState() == graphics.getSuccesDeposit()) {

			if (casquePresence) {

				graphics.getRightup().setOnMouseEntered(null);

				graphics.getRightdown().setOnMouseEntered(null);

				graphics.getLeftup().setOnMouseEntered(null);

				graphics.getLeftdown().setOnMouseEntered(null);

			}

			graphics.setRightupText("");
			graphics.setRightdownText("");
			graphics.getLeftupText().setText("");
			graphics.getLeftdownText().setText("");

			graphics.getRoot().getChildren().remove(graphics.getSaisieSomme());

			graphics.getScreenText2().setText("");

			db.connect();

			sum = Integer.parseInt(graphics.getSaisieSomme().getText());
			db.depot(sum, "carte.code_atr = '" + cardATR + "' AND carte.id_compte = id",
					"carte.code_atr = '" + cardATR + "'");
			db.disconnect();

			graphics.getScreenText().setX(
					graphics.getEcran().getWidth() - (graphics.getScreenText().getBoundsInLocal().getWidth() - 20));
			graphics.setScreenText("DEPOSIT SUCCESSFULLY MADE");

			graphics.getScreenText2().setText("MAKE ANOTHER TRANSACTION?");

			if (casquePresence)
				tts.speak("DEPOSIT SUCCESSFULLY MADE. would you like to make another transaction ? ");

			graphics.getLeftupText().setText("NO");
			graphics.getLeftdownText().setText("YES");
			graphics.getRightdownText().setText("RECEIPT");

			graphics.getLeftup().setOnMouseEntered(e -> {

				if (casquePresence)
					tts.speak(" no . ");
			});

			graphics.getLeftdown().setOnMouseEntered(e -> {

				if (casquePresence)
					tts.speak(" yes . ");
			});

			graphics.getScreenText2().setX(70);
			graphics.getScreenText2().setY(200);

		} else if (graphics.getScreenState() == graphics.getSumTowithdraw()) {

			graphics.setRightupText("");
			graphics.setRightdownText("");
			graphics.getLeftupText().setText("");
			graphics.getLeftdownText().setText("");

			graphics.getScreenText2().setText("");

			graphics.setScreenText("ENTER THE AMOUNT YOU WANT TO WITHDRAW ");

			if (casquePresence)
				tts.speak("ENTER THE AMOUNT YOU WANT TO WITHDRAW .  ");
			graphics.getscreenText().setX(50);
			graphics.getScreenText().setY(160);
			graphics.getRoot().getChildren().remove(graphics.getSaisieCodePIN());
			graphics.getRoot().getChildren().add(graphics.getSaisieSomme());

		} else if (graphics.getScreenState() == graphics.getSuccesWithdraw()) {

			if (casquePresence) {

				graphics.getRightup().setOnMouseEntered(null);

				graphics.getRightdown().setOnMouseEntered(null);

				graphics.getLeftup().setOnMouseEntered(null);

				graphics.getLeftdown().setOnMouseEntered(null);

			}

			graphics.setRightupText("");
			graphics.setRightdownText("");
			graphics.getLeftupText().setText("");
			graphics.getLeftdownText().setText("");

			graphics.getRoot().getChildren().remove(graphics.getSaisieSomme());

			db.connect();

			int sum = Integer.parseInt(graphics.getSaisieSomme().getText());

			double solde = db.getSolde("gab.carte.code_atr = '" + cardATR + "' AND id_compte = id");
			double plafond = db.getPlafond("gab.carte.code_atr = '" + cardATR + "' AND id_compte = id");

			 if (db.billetsDisponibles(sum)) {
				if (solde > sum) {

					if (plafond >= sum) {
						db.retrait(sum, "carte.code_atr = '" + cardATR + "' AND carte.id_compte = id",
								"carte.code_atr = '" + cardATR + "'");

						new Billets(sum);

						graphics.setScreenText("PLEASE COLLECT YOUR BANKNOTES ");

						if (casquePresence)
							tts.speak("PLEASE COLLECT YOUR BANKNOTES . would you like to make another transaction ?");
						graphics.getScreenText().setY(200);
						graphics.getRightdownText().setText("TICKET");
					} else {
						graphics.setScreenText(" IMPOSSIBLE WITHDRAWAL, LIMIT EXCEEDED");

						if (casquePresence)
							tts.speak(
									"IMPOSSIBLE WITHDRAWAL,  LIMIT EXCEEDED . would you like to make another transaction ? ");
						graphics.getScreenText().setY(200);
					}

				} else {

					graphics.setScreenText("IMPOSSIBLE WITHDRAWAL, INSUFFICIENT BALANCE ");

					if (casquePresence)
						tts.speak(
								"IMPOSSIBLE WITHDRAWAL, insufficient balance. would you like to make another transaction ? ");
					graphics.getScreenText().setY(200);
				}

			} else {
				graphics.setScreenText("IMPOSSIBLE WITHDRAWAL, INSUFFICIENT BANKNOTES ");
				if (casquePresence)
					tts.speak(
							"IMPOSSIBLE WITHDRAWAL, INSUFFICIENT BANKNOTES . would you like to make another transaction ? ");
				graphics.getScreenText().setY(200);

				db.disconnect();

			}

			graphics.getScreenText().setY(200);
			graphics.getRightdownText().setText("TICKET");

			graphics.getScreenText2().setText(" MAKE ANOTHER TRANSACTION ?");

			graphics.getLeftupText().setText("NO ");
			graphics.getLeftdownText().setText("YES ");

			graphics.getLeftup().setOnMouseEntered(e -> {

				if (casquePresence)
					tts.speak("no . ");
			});

			graphics.getLeftdown().setOnMouseEntered(e -> {

				if (casquePresence)
					tts.speak("yes . ");
			});

			graphics.getScreenText2().setX(70);
			graphics.getScreenText2().setY(200);
			graphics.getScreenText().setX(
					graphics.getEcran().getWidth() - (graphics.getScreenText().getBoundsInLocal().getWidth() - 20));

			graphics.getScreenText().setY(160);

			graphics.getScreenText2().setX(70);

		} else if (graphics.getScreenState() == graphics.getBalanceCheck()) {

			if (casquePresence) {

				graphics.getRightup().setOnMouseEntered(null);

				graphics.getRightdown().setOnMouseEntered(null);

				graphics.getLeftup().setOnMouseEntered(null);

				graphics.getLeftdown().setOnMouseEntered(null);

			}

			graphics.setRightupText("");
			graphics.setRightdownText("");
			graphics.getLeftupText().setText("");
			graphics.getLeftdownText().setText("");

			graphics.getScreenText2().setText("");

			db.connect();

			double solde = db.getSolde("gab.carte.code_atr = '" + cardATR + "' AND id_compte = id");
			graphics.getGroup().getChildren().remove(graphics.getSaisieCodePIN());

			graphics.setScreenText(" YOUR BALANCE IS " + solde + " EUROS ");
			int soldeInt = (int) (solde);
			String soldeString = String.valueOf(soldeInt);

			if (casquePresence)
				tts.speak(" YOUR BALANCE IS ......... " + soldeString
						+ " euros . ... would you like to make another transaction ?  ");

			graphics.getScreenText().setY(160);
			db.disconnect();

			graphics.getScreenText2().setText("MAKE ANOTHER TRANSACTION ? ");

			graphics.getLeftupText().setText("NO");
			graphics.getLeftdownText().setText("YES");
			graphics.getRightdownText().setText("RECEIPT");

			graphics.getLeftup().setOnMouseEntered(e -> {

				if (casquePresence)
					tts.speak("no . ");
			});

			graphics.getLeftdown().setOnMouseEntered(e -> {

				if (casquePresence)
					tts.speak("yes  . ");
			});

			graphics.getScreenText2().setX(70);
			graphics.getScreenText2().setY(200);

		} else if (graphics.getScreenState() == graphics.getIncorrectPinBalanceCheck()
				|| graphics.getScreenState() == graphics.getIncorrectPinDeposit()
				|| graphics.getScreenState() == graphics.getIncorrectPinWithdraw()) {

			if (securityModule.getTentative() == 0) {
				graphics.getScreenText2()
						.setText(" You have no more attempts left. \n  Please retrieve your card from the bank");

				if (casquePresence)
					tts.speak("You have no more attempts left.   Please retrieve your card from the bank");
				graphics.getScreenText2().setX(70);
				graphics.getScreenText2().setY(200);
				graphics.setScreenText("");
				graphics.getRoot().getChildren().remove(graphics.getSaisieCodePIN());

			} else {
				graphics.getScreenText2()
						.setText(" incorrect pin code . you have " + securityModule.getTentative() + " attempts left ");

				if (casquePresence)
					tts.speak(" incorrect pin code . you have " + String.valueOf(securityModule.getTentative())
							+ "  attempts left   ");
				graphics.getScreenText2().setX(70);
				graphics.getScreenText2().setY(200);
				graphics.getScreenText2().setX(100);
				graphics.getScreenText2().setY(250);

			}

			graphics.getSaisieCodePIN().clear();

		} else {

		}

	}

	/**
	 * Permet d'effectuer la vérification du code PIN en utilisant la classe
	 * SecurityModule et la classe Database Cette fonction est utilisée dans la
	 * classe Graphics
	 * 
	 * @return <br>
	 *         un booléen <b>True</b> La vérification du code PIN a réussi <br>
	 *         <b>False</b> La vérification du code PIN a échoué au bout de trois
	 *         tentatives <br>
	 * @throws SQLException -si l'éxecution de la requête SQL échoue
	 */

	public boolean getMDPSuccess() throws SQLException {

		String cardATR = cardModule.getStringATR();
		String mdp = graphics.getSaisieCodePIN().getText();
		db.connect();
		String hashKey = db.gethashKey("code_atr = '" + cardATR + "'");
		String mdp_hash = db.getmdp_hash("code_atr = '" + cardATR + "'");
		db.disconnect();

		boolean response = false;

		try {
			db.connect();
			response = securityModule.passwordcheck(mdp_hash, hashKey, mdp);

			db.disconnect();
		} catch (Exception e) {

			e.printStackTrace();
		}

		return response;
	}

	/**
	 * Permet de vérifier si la carte insérée fait partie de la banque ou d'une
	 * banque partenaire, et la rejeter sinon, tout cela grâce à la vérification du
	 * RIB
	 * 
	 * @return <br>
	 *         un entier <b>0</b> La carte fait partie de la banque <br>
	 *         <b>1</b> La carte fait partie d'une banque partenaire <br>
	 *         <b>2</b> La carte n'est pas reconnue <br>
	 * @throws SQLException -si l'éxecution de la requête SQL échoue
	 */

	public int checkRIB(String cardATR) throws SQLException {

		db.connect();

		String RIB = db.getRIB("code_atr = '" + cardATR + "' AND id_compte = id");

		if (RIB.length() > 5) {
			String premiersCaracteres = RIB.substring(0, 5);

			if (premiersCaracteres.equals("12345")) {
				return 0;
			} else if (premiersCaracteres.equals("54321")) {
				return 1;
			}
		}

		db.disconnect();

		return 2;

	}

}
