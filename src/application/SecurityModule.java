package application;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Classe permettant le hashage du code PIN entré par l'utilisateur
 * @author Emile J.
 */

public class SecurityModule {
	private int tentative = 3;

	/**
	 * @return renvoie un byte array, un tableau d'octet aléatoire généré de façon
	 *         sécurisé
	 */

	private static byte[] hashKeyGenerator() {

		SecureRandom sr = new SecureRandom();
		byte[] randomBytes = new byte[32];
		sr.nextBytes(randomBytes);
		return randomBytes;
	}

	/**
	 * @return  convertit le tableau d'octet alétoire obtenu en appelant
	 *         hashKeyGenerator en une chaîne de caractère hexadécimale
	 */

	public static String hashKeyGeneratorToString() {

		byte[] randomBytes = hashKeyGenerator();
		StringBuffer hexString = new StringBuffer();
		for (byte randomByte : randomBytes) {
			hexString.append(Integer.toString((randomByte & 0xff) + 0x100, 16).substring(1));
		}
		return hexString.toString();
	}

	/**
	 * @param passwordToHash
	 * 			mot de passe à hasher
	 * @param hash_key
	 * 			une clé de hashage
	 * @return  le mot de passe hashé
	                                     reconnu
	 */

	public String hash_SHA_256(String passwordToHash, String hash_key) {

		String generatedPassword = null;
		try {
			MessageDigest mdp = MessageDigest.getInstance("SHA-256");
			mdp.update(hash_key.getBytes());
			/*
			 * getBytes converti une chaine de caractère en un tableau d'octets en utilisant
			 * l'encodage de caractères par défaut de la plateforme (UTF-8 ici) update passe
			 * le message (msg) à l'instance de la classe MessageDigest (mdp), cette méthode
			 * prend en paramètre un tableau d'octet
			 */
			byte[] bytes = mdp.digest(passwordToHash.getBytes());
			/*
			 * la méthode digest applique la fonction de hashage sur l'objet courant et
			 * renvoie l'objet courant sous forme d'un tableau d'octet
			 */
			StringBuffer hexString = new StringBuffer();
			// Convertion du tableau d'octet en une chaine de caractère hexadécimaux
			for (byte element : bytes) {
				hexString.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	/**
	 * @param passwordToHash
	 * 		String mot de passe à hasher
	 * 	
	 * @return  le mot de passe hashé
	                                      reconnu
	 */

	public String hash_MD5(String passwordToHash) {

		String generatedPassword = null;
		try {
			MessageDigest mdp = MessageDigest.getInstance("MD5");
			byte[] bytes = mdp.digest(passwordToHash.getBytes());
			StringBuffer hexString = new StringBuffer();
			for (byte element : bytes) {
				hexString.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	/**
	 * @param passwordToHash
	 *	 mot de passe à hasher (String)
	 * @param hash_key
	 * 		 et une clé de hashage (String)
	 * @return  le mot de passe hashé en utilisant les méthodes hash_MD5 et
	 *         hash_SHA_256
	 */
	public String hash(String passwordToHash, String hash_key) {

		return hash_SHA_256(hash_MD5(passwordToHash), hash_key);
	}

	/**
	 * @param hashedpassword
	 * 		mot de passe à hasher (String)
	 *  @param hash_key
	 * 		une clé de hashage (String)
	 *  @param mdp
	 * 		et le mot de passe (String)
	 * @return booléen vrai si le mot de passe entré a le même hash que le mot de
	 *         passe hashé, faux au bout de 3 tentatives incorrectes
	 */

	public Boolean passwordcheck(String hashedpassword, String hash_key, String mdp) throws Exception {

		boolean mdp_correct = false;
		if (hash(mdp, hash_key).equals(hashedpassword)) {
			return !mdp_correct;
		} else {
			tentative--;
			if (tentative == 0) {
				tentative = 0;
			}
		}
		return mdp_correct;
	}

	/**
	 * @return  le nombre de tentatives restantes
	 */

	public int getTentative() {

		return tentative;
	}
}