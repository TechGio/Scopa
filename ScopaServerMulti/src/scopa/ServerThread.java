package scopa;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.SynchronousQueue;

public class ServerThread extends Thread {
	
	protected Socket socket;
	ObjectInputStream input = null;
	ObjectOutputStream output = null;
	ArrayList<Carta> combinazione;
	String stringaMessaggio;
	String risultatoPartita;
	Carta cartaGiocata;
	int numeroGiocatore;
	int numeroPartita;
	int numAltroGioc;
	
	//costruttore 
	public ServerThread(Socket clientSocket, int numeroGiocatore, int numeroPartita) throws SocketException {
		super();
		this.socket = clientSocket;
		this.numeroGiocatore = numeroGiocatore;
		this.numeroPartita = numeroPartita;
		socket.setSoTimeout(0);
	}
	
	//metodo run della classe che estende la classe Thread.
	public void run() {
		try {
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			while(!ServerMain.ciSonoDueGiocatori()) {}
			whilePlaying();
		} catch (IOException e) {
			if(numeroGiocatore == 0)
				numAltroGioc = 1;
			else numAltroGioc = 0;
			ServerMain.chiudiLeConnessioni(numeroPartita, numAltroGioc);
		}catch(ClassNotFoundException e) {}
	}
	
	//metodo che gestisce la ricezione della carta giocata e della combinazione inviata dal client, e l'invio della nuova situazione aggiornata.
	public void whilePlaying() throws IOException, ClassNotFoundException{
		Messaggio messaggio;
		while(ServerMain.mazzo[numeroPartita].getSize() > 0 || (ServerMain.giocatori[numeroPartita][0].size() + ServerMain.giocatori[numeroPartita][1].size()) > 0 || ServerMain.turno[numeroPartita] ==0) {
			try {
				Object ob = input.readObject();
				messaggio = (Messaggio)ob;
			}catch(IOException e) {
				if (!socket.isClosed()) 
					throw new IOException(); 
				else {e.printStackTrace(); continue;}
			}
			
			if(messaggio.comando.equals("comb")) {
				combinazione = messaggio.combinazione;
				cartaGiocata = messaggio.carta;
				ServerMain.interpretaCombinazione(numeroGiocatore, combinazione, cartaGiocata, numeroPartita);
			}
		}
		ServerMain.assegnaCarteAdUltimo(numeroPartita);
		ServerMain.comunicaRisultati(numeroPartita);
	}
	
	//metodo che invia il messaggio al client associato.
	void invia(Messaggio mess) {
		try {
			output.writeObject(mess);
			output.flush();
		}
		catch (Exception e) {e.printStackTrace();System.out.println("Impossibile inviare messaggio al client");};
	}
	
}