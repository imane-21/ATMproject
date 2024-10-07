package application;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

/**
 * Classe permettant la reconnaissance vocale (en français) des transactions que
 * le client souhaiterait effectuer (retrait/dépôt/consultation du solde)
 * 
 * L'API CMu Sphinx a été utilisée ici ainsi que le modèle de reconnaissance
 * vocale française développé par LIUM
 * 
 * @author GOXR3PLUS, Imane B.
 * 
 *         <br>
 *         classe inspirée du code trouvé sur
 *         https://github.com/goxr3plus/Java-Speech-Recognizer-Tutorial--Calculator/tree/master
 *
 */

public class SpeechRecognition {

	private String result;
	private LiveSpeechRecognizer recognizer;
	private CountDownLatch latch;
	Configuration configuration = new Configuration();

	/**
	 * Construit la classe SpeechRecognition . La configuration se sert de plusieurs
	 * fichiers distincts : <br>
	 * <b> la grammaire </b> définit les règles syntaxiques et lexicales d'une
	 * langue, afin de rechercher des séquences de mots spécifiques. <br>
	 * <b> le dictionnaire </b> contient des associations entre les mots et leurs
	 * prononciations phonétiques, pour convertir les séquences de mots en séquences
	 * de phonèmes. <br>
	 * <b> le modèle de langue</b> creprésente la structure et la probabilité des
	 * séquences de mots dans une langue, permettent de distinguer les phrases
	 * grammaticalement correctes des phrases qui ne le sont pas. <br>
	 * <b> le mpdèle accoustique </b> contient des informations sur les
	 * caractéristiques acoustiques des sons de la parole dans une langue donnée,
	 * construits à partir d'un grand corpus de données vocales pré-enregistrées et
	 * sont utilisés pour modéliser les probabilités des sons de la parole <br>
	 * 
	 * 
	 * 
	 */
	public SpeechRecognition() {

		configuration.setAcousticModelPath("resources/french_acoustic/");
		configuration.setDictionaryPath("resources/french_lm/french.dic");
		configuration.setLanguageModelPath("resources/french_lm/french.lm");
		configuration.setGrammarPath("resources/grammars");
		configuration.setGrammarName("grammar");
		configuration.setUseGrammar(true);

		if (recognizer == null) {

			try {

				recognizer = new LiveSpeechRecognizer(configuration);
			} catch (IOException ex) {
				System.err.println("Error initializing LiveSpeechRecognizer: " + ex.getMessage());
			}

		}

		recognizer.startRecognition(true);

	}

	/**
	 * Méthode qui lance la reconnaissance vocale
	 * 
	 * La méthode "getHypothesis()" renvoie la meilleure estimation de la
	 * transcription du signal vocal fourni
	 * 
	 * Un mécanisme de synchronisation est utilisé
	 * (java.util.concurrent.CountDownLatch) pour permettre au Thread de
	 * reconnaissance vocale d'attendre qu'un mot ait été prononcé avant de faire une hypothèse
	 */

	protected void startSpeechThread() {
		Thread speechThread = new Thread(() -> {
			System.out.println("Vous pouvez commencer à parler...");
			try {
				SpeechResult speechResult = recognizer.getResult();

				if (speechResult != null) {
					result = speechResult.getHypothesis();
					System.out.println("Vous avez dit [" + result + "]");

				} else {
					System.out.println("Je n'ai pas compris ce que vous avez dit.");
				}
			} catch (Exception ex) {
				System.err.println("Erreur durant la reconnaissance vocale " + ex.getMessage());
			} finally {

				latch.countDown();
			}
			System.out.println("Fin du thread de la reconnaissance vocale...");
		});
		speechThread.start();
	}

	/**
	 * Méthode qui attend la fin de la reconnaissance vocale pour renvoyer le résultat
	 * 
	 * @return  
	 * 			chaine de caractères reconnue 
	 */
	
	public String waitForResult() throws InterruptedException {

		latch = new CountDownLatch(1);

		startSpeechThread();

		latch.await();
		return result;
	}

////////////////////GETTERS

	public String getResult() {
		return result;
	}

}
