package application;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;

/**
 * Classe permettant la conversion de texte en son
 * 
 * @author GOXR3PLUS, Imane B.
 * 
 *         <br>
 *         classe inspirée du code trouvé sur
 *         https://github.com/goxr3plus/Java-Text-To-Speech-Tutorial
 *
 */
public class TextToSpeech {

	private AudioPlayer tts;
	private MaryInterface marytts;

	/**
	 * Construit la classe TextToSpeech en créant une interface locale de l'API
	 * MaryTTS
	 */

	public TextToSpeech() {

		try {
			marytts = new LocalMaryInterface();

		} catch (MaryConfigurationException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Convertit le texte entre en parametre en son
	 * 
	 * @param text <br>
	 *             Le texte que l'on souhaite convertir .
	 * 
	 */

	public void speak(String text) {

		stopSpeaking();

		try (AudioInputStream audio = marytts.generateAudio(text)) {

			tts = new AudioPlayer();
			tts.setAudio(audio);

			tts.start();

		} catch (SynthesisException ex) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "Erreur lors de l'enonciation de la phrase.", ex);
		} catch (IOException ex) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "IO Exception", ex);
		}
	}

	/**
	 * Stoppe l'énonciation en cours
	 */
	public void stopSpeaking() {

		if (tts != null)
			tts.cancel();
	}

////////////////////SETTERS

	/**
	 * Change la voix qui énonce le texte
	 * 
	 * 
	 * 
	 */
	public void setVoice(String voice) {
		marytts.setVoice(voice);
	}

}
