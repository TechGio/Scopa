 package scopa;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Client {
	
	private static int PORTA = 6789; //porta server
	private static ObjectInputStream in;
	private static ObjectOutputStream out;
	private static CampoDaGioco grafica;
	static int turno;
	static Tavolo tavolo = null;
	static Giocatore giocatore = null;
	static ImageIcon immagine = null;
	public static final String tuoTurno = "E' il tuo turno.";
	public static final String noTuoTurno = "Non è il tuo turno.";
	static ArrayList<Carta> combinazione;
	static boolean turnoMio;
	
	//crea la nuova grafica modificata.
	public static void setGrafica(Giocatore giocatore, Tavolo tavolo, ImageIcon immagine, String stringa, boolean bool) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					grafica = new CampoDaGioco(immagine, stringa, bool);
					grafica.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//timer che gestisce la ricezione dei messaggi da parte del server.
	public static void avviaAscolto() {
		Timer ascolto = new Timer();
		ascolto.schedule(new TimerTask() {
			public void run() {
				Messaggio messaggio;
				try {
					Object ob = in.readObject();
					if (ob == null) return;
					messaggio = (Messaggio)ob;
				} catch (Exception e) {return;}
				if (messaggio.comando.equals("risultato")) {
					grafica.modificaGrafica(giocatore, new Tavolo(), null, "Fine partita", false);
					JOptionPane.showMessageDialog(null, messaggio.risultatoPartita);
					ascolto.cancel();
					return;
				}
				if(messaggio.comando.equals("connessionePersa")) {
					JOptionPane.showMessageDialog(null, messaggio.risultatoPartita);
					try {
						in.close();
						out.close();
					} catch (Exception e) {}
					ascolto.cancel();
					return;
				}
				if (turnoMio) {	
					if(messaggio.comando.equals("nuovaMano")) {
						giocatore = messaggio.giocatore;
						grafica.modificaGrafica(giocatore, null, null, tuoTurno, true);
					}
					if(messaggio.comando.equals("nuovaMano&Tavolo")) {
						giocatore = messaggio.giocatore;
						tavolo = messaggio.tavolo;
						grafica.modificaGrafica(giocatore, tavolo, null, noTuoTurno, false);
						turnoMio = false;
					}
				} else {
					if(messaggio.comando.equals("nuovaMano")) {
						giocatore = messaggio.giocatore;
						grafica.modificaGrafica(giocatore, null, null, noTuoTurno, false);
					}
					if(messaggio.comando.equals("cartaAvversario")) {
						tavolo = messaggio.tavolo;
						grafica.modificaGrafica(null, tavolo, new ImageIcon(messaggio.carta.getImmagine()), tuoTurno, true);
						turnoMio = true;
					}
				}
			}
		}, 0, 500);
	}
	
	//metodo che invia il messaggio al server.
	public static void inviaMessaggio(Messaggio m) {
		try {
			out.writeObject(m);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//metodo che legge il messaggio che il server invia al client.
	public static Messaggio leggiMessaggio() {
		try {
			return (Messaggio)in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//main del client.
	public static void main(String[] args) {
		Socket mioSocket = null;
		Messaggio conf = null;
		boolean ok = false;
		while (!ok) {
			try {
				System.out.println("Prova connessione al server...\n");
				mioSocket = new Socket(InetAddress.getLocalHost(), PORTA);
				System.out.println("Connessione riuscita\n");
				out = new ObjectOutputStream(mioSocket.getOutputStream());
				in = new ObjectInputStream(mioSocket.getInputStream());
				conf = leggiMessaggio();
				ok = true; break;
			}
			catch (Exception e) {System.err.println("Impossibile stabilire una connessione\n");}
			JOptionPane.showMessageDialog(null,"Server non in linea");
		}
		turnoMio=conf.b;
		turno = 0;
		giocatore = conf.giocatore;
		tavolo = conf.tavolo;
		if(turnoMio)
			setGrafica(giocatore, tavolo, null, tuoTurno, turnoMio);
		else
			setGrafica(giocatore, tavolo, null, noTuoTurno, turnoMio);
		try {
			mioSocket.setSoTimeout(250);
		} catch (SocketException e) {}
		avviaAscolto();
	}
}
