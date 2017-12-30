package serveurCentraliseSimplifie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
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
				message.save();
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

	private void login(BufferedReader flux_entrant, PrintWriter ma_sortie) {
		try {
			ma_sortie.println("");
			ma_sortie.println("Please enter your login:");
			login = flux_entrant.readLine();
			if (login == null || login.contains(Finish)) {
				System.out.println("Client deconnecté, je termine\n");
				terminer();
			} else {
				ma_sortie.println("Please enter your password:");
				password = flux_entrant.readLine();
				if (password == null || password.contains(Finish)) {
					System.out.println("Client deconnecté, je termine\n");
					terminer();
				} else {
					message = new Message(id,login,password);
					System.out.format("[%s] : Client logged with login: %s and password: %s \n", id, login,
							password);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}