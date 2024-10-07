package application;

import java.util.List;

import java.util.Timer;
import java.util.TimerTask;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

import javafx.application.Platform;

/**
 * Classe permettant la lecture de carte à puces
 * 
 * @author Imane B.
 * 
 */

public class CardModule {

	private Timer timer;

	private TerminalFactory factory;
	private CardTerminal terminal;
	private Card card;
	private ATR atr;
	private byte[] ATR;

	private Graphics graphics;

	/**
	 * Construit la classe CardModule avec la classe Graphics . Cela permet d'éviter
	 * les accès concurrents à la classe Graphics et la duplication d'éléments
	 * menant à une erreur
	 * 
	 * Un timer est mis en place afin de vérifier toutes les secondes si une carte
	 * est présente dans le terminal
	 * 
	 * @param graphics (affichage de l'interface graphique)
	 *
	 * 
	 * 
	 */

	public CardModule(Graphics graphics) {

		this.graphics = graphics;
		timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				checkCardPresence();
			}
		}, 0, 1000);

		try {
			factory = TerminalFactory.getDefault();
			List<CardTerminal> terminals = factory.terminals().list();

			if (!terminals.isEmpty()) {
				terminal = terminals.get(0);
			} else {
				System.out.println("Aucun terminal de carte trouvé.");
			}
		} catch (CardException e) {
			e.printStackTrace();
		}

		graphics.getGroup().getChildren().remove(graphics.getGif());
	}

	/**
	 * Cette fonction permet de détecter la présence du carte sur le terminal grâce
	 * à la bibliothèque javax.smartcardio
	 * 
	 * La méthode Platform.runLater va permettre d'éxécuter le "runnable" passé en
	 * paramètre dans temps futur non spécifié
	 * 
	 * On crée ensuite un timer pour simuler le temps de recherche du GAB du client
	 * dans la base de données de sa banque
	 *
	 * 
	 * 
	 */

	private void checkCardPresence() {
		try {
			if (terminal != null && terminal.isCardPresent()) {
				card = terminal.connect("*");

				if (card != null) {
					try {
						atr = card.getATR();
						ATR = atr.getBytes();
						System.out.println("ATR de la carte: " + bytesToHex(ATR));

						Platform.runLater(() -> {
							graphics.getGroup().getChildren().remove(graphics.getGif());
							graphics.getscreenText().setText("");
							graphics.getGroup().getChildren().add(graphics.getWaitImage());
							graphics.setScreenState(graphics.getWaiting());
							graphics.reAfficherEcran();

							new Timer().schedule(new TimerTask() {
								@Override
								public void run() {
									Platform.runLater(() -> {
										graphics.setScreenState(graphics.getFirstChoices());
										graphics.getGroup().getChildren().remove(graphics.getWaitImage());
										graphics.setScreenText2("");

										graphics.reAfficherEcran();

									});

								}
							}, 4000);

						});

						timer.cancel();
						timer.purge();

					} finally {

						card.disconnect(false);
					}
				} else {
					System.out.println("La connexion à la carte a échoué.");
				}
			}
		} catch (CardException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode converti la chaine en byte passée en paramètre en une chaine de
	 * caractères
	 *
	 * @param bytes tableau de bytes
	 *
	 * @return le tableau de bytes passé en paramètre converti en chaine de
	 *         caractères
	 *
	 * 
	 * 
	 */

	private String bytesToHex(byte[] bytes) {
		if (bytes == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

////////////////////GETTERS

	public String getStringATR() {
		return bytesToHex(ATR);
	}

}
