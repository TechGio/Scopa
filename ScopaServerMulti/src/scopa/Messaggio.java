package scopa;

import java.io.Serializable;
import java.util.ArrayList;

public class Messaggio implements Serializable {

	private static final long serialVersionUID = -802667070226488995L;
	Carta carta = null;
	ArrayList<Carta> combinazione = null;
	String risultatoPartita = null;
	Giocatore giocatore = null;
	Tavolo tavolo = null;
	String comando = null;
	boolean b;
	 
	 //costruttore vuoto della classe.
	public Messaggio(){}
	
	//costruttore della classe utilizzato quando si crea il messaggio che invia la nuova mano.
	public Messaggio(String comando, Giocatore giocatore) {
		this.comando = comando;
		this.giocatore = giocatore;
	}
	
	//costruttore della classe utilizzato quando si comunica il risultato della partita.
	public Messaggio(String comando, String risultatoPartita) {
		this.comando = comando;
		this.risultatoPartita = risultatoPartita;
	}
	
	//costruttore della classe utilizzato dopo ogni mossa di qualsiasi giocatore.
	public Messaggio (String comando, Carta carta, ArrayList<Carta> combinazione, Giocatore giocatore, Tavolo tavolo){
		this.comando = comando;
		this.carta = carta;
		this.combinazione = combinazione;
		this.giocatore = giocatore;
		this.tavolo = tavolo;
	}
	
	//metodo set del boolean.
	public void setB(boolean bool) {
		b = bool;
	}
	
	//metodo set della stringa che mostra il risultato della partita.
	public void setRisultatoPartita(String risultatoPartita) {
		this.risultatoPartita = risultatoPartita;
	}
}