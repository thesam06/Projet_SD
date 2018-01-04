package serveurCentraliseSimplifie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;

import bd.ClientBD;
import messeges.Message;

public class ServiceClient implements Runnable {
	// String to finish the communication -> ctrl-d
	// private static final String Finish="end";

	private Socket my_connection;
	private String Finish = "" + (char) 4;
	private String id;
	private String login;
	private String password;
	private Message message;

	private void terminer() {
		try {
			if (my_connection != null) {
				System.out.format("Terminaison pour %s\n", id);
				if (message != null) {
					message.save();
				}
				my_connection.close();
			}
		} catch (IOException e) {
			System.out.format("Terminaison pour %s\n", id);
			e.printStackTrace();
		}
		return;
	}

	public ServiceClient(Socket la_connection, String mid) {
		my_connection = la_connection;
		id = mid;
		System.out.format("[Serveur]: Thread %s created for connection\n", id);
	}

	public void run() {
		// Phase d initialisation
		BufferedReader flux_entrant = null;
		PrintWriter ma_sortie = null;
		try {
			Charset chrs = Charset.forName("UTF-8");
			InputStreamReader isr = new InputStreamReader(my_connection.getInputStream(), chrs);
			flux_entrant = new BufferedReader(isr); // file d'entrée
			// flux de sortie en mode autoflush
			ma_sortie = new PrintWriter(my_connection.getOutputStream(), true);
			String c_ip = my_connection.getInetAddress().toString();
			int c_port = my_connection.getPort();
			System.out.format("[%s] client admis IP %s  sur le port %d\n", id, c_ip, c_port);
			ma_sortie.format("[%s] : Hello %s  sur le port %d, \n", id, c_ip, c_port);
		} catch (Exception e1) {
			System.out.println("Initialisation Error");
			e1.printStackTrace();
		}

		login(flux_entrant, ma_sortie);
		String message_lu = new String();
		int line_num = 0;
		// Fin de l initialisation
		// Boucle principale
		if (message != null) {
			System.out.println("le client a reussi de se connecter");
			while (true) {
				try {
					message_lu = flux_entrant.readLine();
					message.addMessage(message_lu);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				if (message_lu == null) {
					System.out.println("Client deconnecté, je termine\n");
					terminer();
					return;
				}
				System.out.format("%s [line_%d]--> [%s]]\n", id, line_num, message_lu);
				if (message_lu.contains(Finish)) {
					System.out.format("[%s] :  [%s] recu, Transmission finie\n", id, message_lu);
					ma_sortie.println("Fermeture de la connexion");
					terminer();
					return;
				}
				line_num++;
			}
		}

	}

	public boolean check(String str) {
		if (str == null || str.contains(Finish)) {
			System.out.println("Client deconnecté, je termine\n");
			terminer();
			return false;
		} else {
			return true;
		}
	}

	private void login(BufferedReader flux_entrant, PrintWriter ma_sortie) {
		ClientBD cbd = new ClientBD();
		ma_sortie.println("");
		ma_sortie.println("Vous etez deja client chez nous? Oui/Non");
		try {
			String reponse = flux_entrant.readLine();
			if (reponse.contains("Oui")) {
				continuerLogin(flux_entrant, ma_sortie, cbd);
			} else if (reponse.contains("Non")) {
				creationNouveauClient(flux_entrant, ma_sortie, cbd);
				continuerLogin(flux_entrant, ma_sortie, cbd);
			} else {
				ma_sortie.println("Response invalide, veuilles repondre par Oui ou Non!");
				login(flux_entrant, ma_sortie);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void creationNouveauClient(BufferedReader flux_entrant, PrintWriter ma_sortie, ClientBD cbd) {
		boolean confirm = false;
		String nom = "", prenom = "", user = "", motdepasse = "", motdepasse2 = "";
		do {
			try {
				ma_sortie.println("");
				ma_sortie.println("Entrez votre nom svp: ");
				nom = flux_entrant.readLine();
				if (!check(nom)) {
					return;
				}
				ma_sortie.println("Entrez votre prenom svp: ");
				prenom = flux_entrant.readLine();
				if (!check(prenom)) {
					return;
				}
				ma_sortie.println("Entrez votre login svp: ");
				user = flux_entrant.readLine();
				if (!check(user)) {
					return;
				}
				ma_sortie.println("Entrez votre motdepasse svp: ");
				motdepasse = flux_entrant.readLine();
				if (!check(motdepasse)) {
					return;
				}
				ma_sortie.println("Confirmez votre motdepasse svp: ");
				motdepasse2 = flux_entrant.readLine();
				if (motdepasse.contentEquals(motdepasse2)) {
					ma_sortie.println("Confirmez svp par Oui/Non: ");
					ma_sortie.println(String.format("nom = %s , prenom = %s , login = %s , motdepasse = %s",
							nom, prenom, user, motdepasse));
					String reponseConfirmant = flux_entrant.readLine();
					if (reponseConfirmant.contains("Oui")) {
						confirm = true;
					} else if (reponseConfirmant.contains("Non")) {
						ma_sortie.println("Alors on recommence...");
					} else {
						ma_sortie.println("Il falait repondre par Oui ou Non... On recomence!");
					}
				} else {
					ma_sortie.println("les mot de passes ne sont pas identiques... on recommence!");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (!confirm);
		cbd.ajouterClient(nom, prenom, user, motdepasse);
		System.out.println("[Serveur]: Nouveau Client!");
		System.out.println(String.format("nom = %s , prenom = %s , login = %s , motdepasse = %s", nom, prenom,
				user, motdepasse));
	}

	private void continuerLogin(BufferedReader flux_entrant, PrintWriter ma_sortie, ClientBD cbd) {
		while (!cbd.connectionClient(login, password)) {
			try {
				ma_sortie.println("");
				ma_sortie.println("Please enter your login:");
				login = flux_entrant.readLine();
				if (!check(login)) {
					return;
				}
				ma_sortie.println("Please enter your password:");
				password = flux_entrant.readLine();
				if (!check(password)) {
					return;
				}
				if (!cbd.connectionClient(login, password)) {
					ma_sortie.println("login ou mot de pass est incorrect! veuillez reessayer...");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		cbd.chargerInfoClient(login);
		
		message = new Message(cbd.getIdClient(),cbd.getNom(),cbd.getPrenom(), login, password);
		System.out.format("[%s] : Client logged with login: %s and password: %s \n", id, login, password);
	}
}