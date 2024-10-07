package application;

import javax.sound.sampled.AudioSystem;

import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;

/**
 * Classe permettant la détection de casque sur la sortie audion afin de
 * déclencher l'assistance sonore aux personnes malvoyantes
 * 
 * @author Imane B.
 * 
 */

public class HeadphoneDetection {

	private boolean headphonesConnected = false;

	/**
	 * Construit la classe HeadphoneDetection
	 * 
	 */

	public HeadphoneDetection() {

		this.headphonesConnected = headphonesConnected();

	}

	/**
	 * Utilise les informations système sur les ports pour indiquer si un casque est
	 * branché ou non
	 * 
	 * @return <br>
	 *         <b>false</b> pas de casque présent sur la sortie audio <br>
	 *         <b>true</b> casque branché
	 * 
	 * 
	 */

	public boolean headphonesConnected() {

		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
		for (Mixer.Info mixerInfo : mixerInfos) {
			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			Line.Info[] lineInfos = mixer.getTargetLineInfo();
			for (Line.Info lineInfo : lineInfos) {
				if (lineInfo instanceof Port.Info) {
					Port.Info portInfo = (Port.Info) lineInfo;
					if (portInfo == Port.Info.HEADPHONE) {

						headphonesConnected = true;

					}
				}
			}
		}

		if (!headphonesConnected) {

			headphonesConnected = false;
		}

		return headphonesConnected;
	}

////////////////////GETTERS

	public boolean isHeadphonesConnected() {
		return headphonesConnected;
	}

}