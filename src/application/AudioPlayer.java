package application;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;

import javax.sound.sampled.SourceDataLine;

/**
 * Classe permettant de jouer du son sur l'application Java
 * 
 * @author Imane B.
 * 
 * 
 *
 */

public class AudioPlayer extends Thread {
	private AudioInputStream ais;
	private SourceDataLine line;
	private boolean exitRequested = false;

	public AudioPlayer() {

	}

	/**
	 * Méthode qui définit le flux audio à lire
	 * 
	 */

	public void setAudio(AudioInputStream audio) {
		this.ais = audio;
	}

	/**
	 * Méthode qui arrête la lecture audio
	 * 
	 */
	
	public void cancel() {
		if (line != null) {
			line.stop();
			line.close();
		}
		exitRequested = true;
	}
	
	/**
	 * La méthode run est appelée lorsqu'un Thread est démarré. Elle ouvre une ligne de données audio puis démarre la lecture . 
	 * 
	 */
	

	@Override
	public void run() {
		AudioFormat audioFormat = ais.getFormat();
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

		try {
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(audioFormat);
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		line.start();

		int nRead = 0;
		byte[] abData = new byte[65532];
		while (nRead != -1 && !exitRequested) {
			try {
				nRead = ais.read(abData, 0, abData.length);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			if (nRead >= 0) {
				line.write(abData, 0, nRead);
			}
		}

		line.drain();
		line.close();
	}
}
