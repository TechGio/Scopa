package scopa;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMain {
	static final int PORT = 6789;
	static final Mazzo[] mazzo = new Mazzo[10];
	static final int NUMEROGIOCATORI = 2;
	static final Giocatore[][] giocatori = new Giocatore[10][2];
	static int[] turno = new int[10];
	static final Tavolo[] tavolo = new Tavolo[10];
	static int[] ultimoAPrendere = new int [10];
	static final ServerThread[][] servers = new ServerThread[10][2];
	static int[] punteggioUno = new int [10];
	static int[] punteggioDue = new int [10];
	static int numeroPartite = 0;
	
	//main del server.
	public static void main(String args[]) {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(PORT);
			while(numeroPartite < 10) {
		    	mazzo[numeroPartite] = new Mazzo();
		    	tavolo[numeroPartite] = new Tavolo();
		    	giocatori[numeroPartite][0] = new Giocatore();
				servers[numeroPartite][0] = new ServerThread(serverSocket.accept(), 0, numeroPartite);
				servers[numeroPartite][0].start();
				giocatori[numeroPartite][1] = new Giocatore();
				servers[numeroPartite][1] = new ServerThread(serverSocket.accept(), 1, numeroPartite);
				servers[numeroPartite][1].start();
				for(int i = 0; i < 2; i++) {
					tavolo[numeroPartite].aggiungiCartaInTavolo(mazzo[numeroPartite].pescata());
					tavolo[numeroPartite].aggiungiCartaInTavolo(mazzo[numeroPartite].pescata());
					for(int j = 0; j < 3; j++)
						giocatori[numeroPartite][i].cartePescate(mazzo[numeroPartite].pescata());
				}
				turno[numeroPartite] = 0;
				Messaggio m = new Messaggio("conf", null, null, giocatori[numeroPartite][0], tavolo[numeroPartite]);
				m.setB(true);
				servers[numeroPartite][0].invia(m);
				Thread.sleep(250);
				m = new Messaggio("conf", null, null, giocatori[numeroPartite][1],tavolo[numeroPartite]);
				m.setB(false);
				servers[numeroPartite][1].invia(m);
				Thread.sleep(250);
				numeroPartite++;
			}
		} catch (Exception e) {System.out.println("I/O error: " + e);}
	}
	
	//metodo che ritorna true se ci sono due client connessi.
	static boolean ciSonoDueGiocatori() {
		return giocatori[numeroPartite][1] != null;
	}
	
	//metodo che in base alla combinazione ricevuta aggiunge/toglie carte al tavolo e le assegna al giocatore opportuno, e invia ai client la nuova situazione.
	public static void interpretaCombinazione(int numeroGiocatore, ArrayList<Carta> combinazione, Carta cartaGiocata, int numeroPartita){
		int j;
		if(numeroGiocatore == 0) 
			j = 1;
		else 
			j = 0;
		if(combinazione == null) {
			tavolo[numeroPartita].aggiungiCartaInTavolo(cartaGiocata);
			giocatori[numeroPartita][numeroGiocatore].cartaRimossaDallaMano(cartaGiocata);
		} else {
			giocatori[numeroPartita][numeroGiocatore].cartaPresa(cartaGiocata);
			giocatori[numeroPartita][numeroGiocatore].cartaRimossaDallaMano(cartaGiocata);
			for(int i = 0; i < combinazione.size(); i++) {
				giocatori[numeroPartita][numeroGiocatore].cartaPresa(combinazione.get(i));
				tavolo[numeroPartita].rimuoviCartaInTavolo(combinazione.get(i), giocatori[numeroPartita][numeroGiocatore]);
			}
			if(tavolo[numeroPartita].getCarteInTavolo().size() == 0)
				giocatori[numeroPartita][numeroGiocatore].incrementaScope();
			ultimoAPrendere[numeroPartita] = numeroGiocatore;
		}
		
		Tavolo tavolino = new Tavolo();
		for (Carta carta : tavolo[numeroPartita].getCarteInTavolo())
			tavolino.aggiungiCartaInTavolo(carta);
		Giocatore giocator = new Giocatore();
		for (Carta carta : giocatori[numeroPartita][numeroGiocatore].getCarteInMano())
			giocator.cartePescate(carta);
		for (Carta carta : giocatori[numeroPartita][numeroGiocatore].getCartePrese())
			giocator.cartaPresa(carta);
		for (int y =0 ; y< giocatori[numeroPartita][numeroGiocatore].getScope(); y++)
			giocator.incrementaScope();
		
		servers[numeroPartita][numeroGiocatore].invia(new Messaggio("nuovaMano&Tavolo", null, null, giocator, tavolino));
		servers[numeroPartita][j].invia(new Messaggio("cartaAvversario", cartaGiocata, null, null, tavolino));
		
		if(mazzo[numeroPartita].getMazzo().size() > 0 && giocatori[numeroPartita][numeroGiocatore].getCarteInMano().size() == 0 && giocatori[numeroPartita][j].getCarteInMano().size() == 0)
			{
				distribuisciCarte(numeroPartita);
				turno[numeroPartita]++;
			}
	}
	
	
	
	//metodo che comunica il risultato della partita ai client.
	public static void comunicaRisultati(int numeroPartita) {
		Messaggio messaggio = new Messaggio("risultato", "");
		int v = vincitore(numeroPartita);
		if(v == 0) {
			messaggio.setRisultatoPartita("HAI VINTO!\nHai fatto " + punteggioUno[numeroPartita] + " punti contro " + punteggioDue[numeroPartita]);
			servers[numeroPartita][0].invia(messaggio);
			messaggio.setRisultatoPartita("HAI PERSO!\nHai fatto " + punteggioDue[numeroPartita] + " punti contro " + punteggioUno[numeroPartita]);
			servers[numeroPartita][1].invia(messaggio);
		}
		else if(v == 1) {
			messaggio.setRisultatoPartita("HAI VINTO!\nHai fatto " + punteggioDue[numeroPartita] + " punti contro " + punteggioUno[numeroPartita]);
			servers[numeroPartita][1].invia(messaggio);
			messaggio.setRisultatoPartita("HAI PERSO!\nHai fatto " + punteggioUno[numeroPartita] + " punti contro " + punteggioDue[numeroPartita]);
			servers[numeroPartita][0].invia(messaggio);
		}
		else {
			messaggio.setRisultatoPartita("PAREGGIO!\nAvete fatto entrambi " + punteggioUno[numeroPartita] + " punti");
			servers[numeroPartita][0].invia(messaggio);
			servers[numeroPartita][1].invia(messaggio);
		}
	}
			
		
	//metodo che decreta il vincitore della partita.
	public static int vincitore(int numeroPartita) {
		if(giocatori[numeroPartita][0].getCartePrese().size()>giocatori[numeroPartita][1].getCartePrese().size())
			punteggioUno[numeroPartita]++;
		else if(giocatori[numeroPartita][0].getCartePrese().size()<giocatori[numeroPartita][1].getCartePrese().size())
			punteggioDue[numeroPartita]++;
		if(giocatori[numeroPartita][0].denariPresi()>giocatori[numeroPartita][1].denariPresi())
			punteggioUno[numeroPartita]++;
		else if(giocatori[numeroPartita][0].denariPresi()<giocatori[numeroPartita][1].denariPresi())
			punteggioDue[numeroPartita]++;
		if(giocatori[numeroPartita][0].setteDiDenari()==true)
			punteggioUno[numeroPartita]++;
		else 
			punteggioDue[numeroPartita]++;
		if(giocatori[numeroPartita][0].settanta()>giocatori[numeroPartita][1].settanta())
			punteggioUno[numeroPartita]++;
		else if(giocatori[numeroPartita][0].settanta()<giocatori[numeroPartita][1].settanta())
			punteggioDue[numeroPartita]++;
		punteggioUno[numeroPartita] = punteggioUno[numeroPartita] + giocatori[numeroPartita][0].getScope();
		punteggioDue[numeroPartita] = punteggioDue[numeroPartita] + giocatori[numeroPartita][1].getScope();
		if(punteggioUno[numeroPartita]>punteggioDue[numeroPartita])
			return 0;
		else if(punteggioUno[numeroPartita]>punteggioDue[numeroPartita]) 
			return 1;
		else
			return 2;
	}
	
	//metodo che assegna la nuova mano ai due giocatori.
	public static void distribuisciCarte(int numeroPartita) {
		Giocatore giocator = new Giocatore();
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 3; j++) {
				giocatori[numeroPartita][i].cartePescate(mazzo[numeroPartita].pescata());
			}
			
			for (Carta carta : giocatori[numeroPartita][i].getCarteInMano())
				giocator.cartePescate(carta);
			for (Carta carta : giocatori[numeroPartita][i].getCartePrese())
				giocator.cartaPresa(carta);
			for (int y =0 ; y< giocatori[numeroPartita][i].getScope(); y++)
				giocator.incrementaScope();
			
			Messaggio messaggio = new Messaggio("nuovaMano", giocator);
			servers[numeroPartita][i].invia(messaggio);
			giocator = new Giocatore();
		}
	}
	
	//metodo che assegna le ultime carte rimaste in campo all'ultimo che ha preso
	public static void assegnaCarteAdUltimo(int numeroPartita) {
		for(int i = 0; i <tavolo[numeroPartita].getCarteInTavolo().size(); i++) {
			giocatori[numeroPartita][ultimoAPrendere[numeroPartita]].cartaPresa(tavolo[numeroPartita].getCarteInTavolo().get(i));
		}
	}
	
	//metodo che chiude le connessioni di due client col server.
	public static void chiudiLeConnessioni(int numeroPartita, int numeroGiocatore) {
		try {
			if(!servers[numeroPartita][numeroGiocatore].socket.isClosed())
				servers[numeroPartita][numeroGiocatore].invia(new Messaggio("connessionePersa", "Partita terminata a causa della disconnessione dell'altro giocatore."));
			if(!servers[numeroPartita][0].socket.isClosed()) {
				servers[numeroPartita][0].socket.close();
			}
			if(!servers[numeroPartita][1].socket.isClosed()) {
				servers[numeroPartita][1].socket.close();
			}
				
		} catch (Exception e) {}
		
	}
		
}