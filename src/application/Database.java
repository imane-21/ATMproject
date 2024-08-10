package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Classe permettant d'accéder à la base de données contenant toutes les
 * informations nécessaires au fonctionnement du GAB (clients, guichet...)
 * 
 * @author Clara A., Christophe C.
 * 
 */

public class Database {

	private static final String URL = "jdbc:mysql://localhost:3306/gab";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "2023L2C1";

	private Connection connection;

	/**
	 * Méthode permettant la connexion à la base de données
	 * 
	 */

	public void connect() throws SQLException {
		try {

			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			System.out.println("Connexion rÃ©ussie !");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new SQLException("La connexion Ã  la base de donnÃ©es a Ã©chouÃ©.");
		}
	}

	/**
	 * Méthode permettant la déconnexion de la base de données
	 * 
	 */

	public void disconnect() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
			System.out.println("Connexion fermÃ©e.");
		}
	}

	/**
	 * Méthode qui enregistre les transactions effectuée dans un historique
	 * 
	 * @param somme     somme qui a été retirée ou déposée
	 * 
	 * @param id_compte numéro de compte du client qui a effectué la transaction en
	 *                  question
	 * 
	 */

	public void transaction(String somme, int id_compte) throws SQLException {
		try {
			LocalDate date = LocalDate.now();
			LocalTime time = LocalTime.now();
			String sql = "INSERT INTO gab.historique (date, heure, somme, beneficiaire, id_compte) VALUES (? ,? ,? ,? ,?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setObject(1, date);
			statement.setObject(2, time);
			statement.setString(3, somme);
			statement.setString(4, "en guichet");
			statement.setInt(5, id_compte);
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("La mise Ã  jour de la colonne a Ã©chouÃ©.");
		}
	}

	/**
	 * Méthode qui permet de déposer de l'argent sur le compte du client
	 * 
	 * @param somme      somme qui doit être déposée
	 * 
	 * @param condition  numéro de compte du client qui a effectué la transaction en
	 *                   question correspondant au code ATR de la carte insérée
	 * 
	 * @param condition2 condition sur laquelle le numéro du compte client doit etre
	 *                   sélectionné (en l'occurrence le code ATR de la carte
	 *                   insérée)
	 * 
	 */

	public void depot(int somme, String condition, String condition2) throws SQLException {
		try {
			String nomtable = "gab.compte, gab.carte";
			String nomcolonne = "solde";
			String sql = "UPDATE " + nomtable + " SET " + nomcolonne + " = " + nomcolonne + " + " + somme + " WHERE "
					+ condition;
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.executeUpdate();
			statement.close();

			int id = getidCompte(condition2);
			transaction("" + somme, id);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("La mise Ã  jour de la colonne a Ã©chouÃ©.");
		}

	}

	/**
	 * Méthode qui permet de retirer de l'argent depuis le compte du client
	 * 
	 * @param somme      somme qui doit être retirée
	 * 
	 * @param condition  numéro de compte du client qui a effectué la transaction en
	 *                   question correspondant au code ATR de la carte insérée
	 * 
	 * @param condition2 condition sur laquelle le numéro du compte client doit etre
	 *                   sélectionné (en l'occurrence le code ATR de la carte
	 *                   insérée)
	 * 
	 */

	public void retrait(int somme, String condition, String condition2) throws SQLException {
		try {

			String nomtable = "gab.compte, gab.carte";
			String nomcolonne1 = "solde";
			String nomcolonne2 = "plafond";
			String sql1 = "UPDATE " + nomtable + " SET " + nomcolonne1 + " = " + nomcolonne1 + " - ? WHERE "
					+ condition;
			String sql2 = "UPDATE " + nomtable + " SET " + nomcolonne2 + " = " + nomcolonne2 + " - ? WHERE "
					+ condition;

			PreparedStatement statement1 = connection.prepareStatement(sql1);
			PreparedStatement statement2 = connection.prepareStatement(sql2);
			statement1.setInt(1, somme);
			statement1.executeUpdate();
			statement1.close();
			statement2.setInt(1, somme);
			statement2.executeUpdate();
			statement2.close();

			retirebillet(somme);

			int id = getidCompte(condition2);
			transaction("-" + somme, id);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("La mise Ã  jour de la colonne a Ã©chouÃ©.");
		}
	}

	/**
	 * Méthode qui met à jour le nombre de billets disponibles dans le guichet après
	 * un retrait
	 * 
	 * @param somme somme qui a été retirée
	 * 
	 * 
	 * 
	 */

	public void retirebillet(int somme) throws SQLException {

		int billet50aretirer = 0;
		int billet20aretirer = 0;
		int billet10aretirer = 0;
		int nbbillet50danslaBDD = getBillet("nb_billet50");
		int nbbillet20danslaBDD = getBillet("nb_billet20");
		int nbbillet10danslaBDD = getBillet("nb_billet10");

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

		String sql4 = "UPDATE gab.guichet SET nb_billet10 = nb_billet10 - ?, nb_billet20 = nb_billet20 -?, nb_billet50 = nb_billet50 - ?";
		PreparedStatement statement4 = connection.prepareStatement(sql4);
		statement4.setInt(1, billet10aretirer);
		statement4.setInt(2, billet20aretirer);
		statement4.setInt(3, billet50aretirer);
		statement4.executeUpdate();
		statement4.close();
	}

	/**
	 * Méthode qui indique si le nombre de billets dans le guichet est suffisant
	 * pour le retrait demandé
	 * 
	 * @param somme somme que le client souhaite retirer
	 * 
	 * 
	 * 
	 */

	public boolean billetsDisponibles(int somme) throws SQLException {

		int nbbillet50danslaBDD = getBillet("nb_billet50");
		int nbbillet20danslaBDD = getBillet("nb_billet20");
		int nbbillet10danslaBDD = getBillet("nb_billet10");

		while (somme > 0) {
			if (nbbillet50danslaBDD == 0 & nbbillet20danslaBDD == 0 & nbbillet10danslaBDD == 0) {
				return false;
			}
			if (somme >= 50 & nbbillet50danslaBDD > 0) {
				somme = somme - 50;
				nbbillet50danslaBDD = nbbillet50danslaBDD - 1;
			} else if (somme >= 20 & nbbillet20danslaBDD > 0) {
				somme = somme - 20;
				nbbillet20danslaBDD = nbbillet20danslaBDD - 1;
			} else if (somme >= 10 & nbbillet10danslaBDD > 0) {
				somme = somme - 10;
				nbbillet10danslaBDD = nbbillet10danslaBDD - 1;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * Méthode qui renvoie le nombre de billets disponibles dans le guichet
	 * 
	 * @param condition type de billets dont on souhaite vérifier la disponibilité
	 * 
	 * 
	 * 
	 */

	public int getBillet(String condition) throws SQLException {
		int nbbillet = 0;
		try {
			String sql = "SELECT " + condition + " FROM gab.guichet";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				nbbillet = resultSet.getInt(condition);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Impossible de rÃ©cupÃ©rer le solde du client.");
		}
		return nbbillet;
	}

	/**
	 * Méthode qui renvoie le solde du client
	 * 
	 * @param condition compte du client correspondant au code de la carte insérée
	 * 
	 * 
	 * 
	 */

	public double getSolde(String condition) throws SQLException {
		double solde = 0;
		try {
			String sql = "SELECT solde FROM gab.compte , gab.carte WHERE " + condition;
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				solde = resultSet.getDouble("solde");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Impossible de rÃ©cupÃ©rer le solde du client.");
		}
		return solde;
	}

	/**
	 * Méthode qui renvoie le plafond du client
	 * 
	 * @param condition compte du client correspondant au code de la carte insérée
	 * 
	 * 
	 * 
	 */

	public double getPlafond(String condition) throws SQLException {
		int plafond = 0;
		try {
			String sql = "SELECT plafond FROM gab.compte , gab.carte WHERE " + condition;
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				plafond = resultSet.getInt("plafond");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Impossible de rÃ©cupÃ©rer le plafond du compte client.");
		}
		return plafond;

	}

	/**
	 * Méthode qui renvoie le numéro de compte du client
	 * 
	 * @param condition compte du client correspondant au code de la carte insérée
	 * 
	 * 
	 * 
	 */

	public int getidCompte(String condition) throws SQLException {
		int id_compte = 0;
		try {
			String sql = "SELECT id_compte FROM gab.carte WHERE " + condition;
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				id_compte = resultSet.getInt("id_compte");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Impossible de rÃ©cupÃ©rer l' id_compte du client.");
		}
		return id_compte;
	}

	/**
	 * Méthode qui renvoie le RIB du client
	 * 
	 * @param condition compte du client correspondant au code de la carte insérée
	 * 
	 * 
	 * 
	 */

	public String getRIB(String condition) throws SQLException {
		String RIB = "";
		try {
			String sql = "SELECT RIB FROM gab.compte, gab.carte WHERE " + condition;
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				RIB = resultSet.getString("RIB");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Impossible de rÃ©cupÃ©rer le RIB du client.");
		}
		return RIB;

	}

	/**
	 * Méthode qui renvoie le code PIN hashé de la carte du client
	 * 
	 * @param condition compte du client correspondant au code de la carte insérée
	 * 
	 * 
	 * 
	 */

	public String getmdp_hash(String condition) throws SQLException {
		String mdp_hash = "";
		try {
			String sql = "SELECT mdp_hash FROM gab.carte WHERE " + condition;
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				mdp_hash = resultSet.getString("mdp_hash");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Impossible de rÃ©cupÃ©rer le hash du mdp du client.");
		}
		return mdp_hash;
	}

	/**
	 * Méthode qui renvoie le hash du code de la carte du client
	 * 
	 * @param condition compte du client correspondant au code de la carte insérée
	 * 
	 * 
	 * 
	 */

	public String gethashKey(String condition) throws SQLException {
		String hashKey = "";
		try {
			String sql = "SELECT hashKey FROM gab.carte WHERE " + condition;
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				hashKey = resultSet.getString("hashKey");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Impossible de rÃ©cupÃ©rer le RIB du client.");
		}
		return hashKey;
	}

	/**
	 * Méthode qui indique la possibilité d'imprimer un ticket ou non
	 * 
	 * @return <br>
	 *         un boléen <b>true</b> Plus d'encre et/ou de papier <br>
	 *         <b>false</b> Encre et papier encore disponibles <br>
	 * 
	 */

	public boolean impression() throws SQLException {
		boolean disponible = false;
		boolean papier = false;
		boolean encre = false;
		int nb_papier = 0;
		int nb_encre = 0;
		try {
			String sql = "SELECT nb_papier FROM gab.guichet WHERE num=67890";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				nb_papier = resultSet.getInt("nb_papier");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Impossible de rÃ©cupÃ©rer le nombre de papier restant.");
		}

		if (nb_papier != 0) {
			nb_papier--;
			try {
				String sqlUpdate = "UPDATE gab.guichet SET nb_papier = ? WHERE num = 67890";
				PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate);
				updateStatement.setInt(1, nb_papier);
				updateStatement.executeUpdate();
				papier = true;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException("Impossible de mettre à jour le nombre de papier restant.");
			}
		}

		try {
			String sql = "SELECT nb_encre FROM gab.guichet WHERE num=67890";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				nb_encre = resultSet.getInt("nb_encre");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Impossible de rÃ©cupÃ©rer la quantité d'encre restante.");
		}

		if (nb_encre != 0) {
			nb_encre--;
			try {
				String sqlUpdate = "UPDATE gab.guichet SET nb_encre = ? WHERE num = 67890";
				PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate);
				updateStatement.setInt(1, nb_encre);
				updateStatement.executeUpdate();
				encre = true;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException("Impossible de mettre à jour la quantité d'encre.");
			}
		}

		if (papier && encre) {
			disponible = true;
		}

		return disponible;
	}

	/**
	 * Méthode qui renouvelle le plafond chaque semaine
	 * 
	 */

	public void renouvelerPlafond() throws SQLException {
		try {
			String nomTable = "gab.compte";
			String nomColonne = "plafond";
			int nouveauPlafond = 800;
			LocalDate dateActuelle = LocalDate.now();
			LocalDate dateRenouvellement = dateActuelle.plusWeeks(1);

			String sql = "UPDATE " + nomTable + " SET " + nomColonne
					+ " = ? WHERE DATE_ADD(DATE(date_maj), INTERVAL WEEKDAY(date_maj) DAY) <= ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, nouveauPlafond);
			statement.setDate(2, Date.valueOf(dateRenouvellement));
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("La mise à jour du plafond a échoué.");
		}
	}

}
