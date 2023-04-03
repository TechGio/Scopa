package scopa;

import java.io.Serializable;
import java.util.ArrayList;

public class Giocatore implements Serializable{
	
	private static final long serialVersionUID = -6435003013934422767L;
	private ArrayList<Carta> cartePrese = new ArrayList<>();
	private ArrayList<Carta> carteInMano = new ArrayList<>();
	private int scope = 0;
	
	//metodo che aggiunge alla mano la carte pescata.
	public void cartePescate(Carta cartaPescata) {
		carteInMano.add(cartaPescata);
	}
	
	//metodo che aggiunge ad un arrayList tutte le carte prese.
	public void cartaPresa(Carta cartaPresa) {
		cartePrese.add(cartaPresa);
	}
	
	//metodo che rimuove dalla mano la carta passata come argomento.
	public void cartaRimossaDallaMano(Carta carta) {
		for(int i = 0; i < carteInMano.size(); i++) {
			if(carteInMano.get(i).getNome().equals(carta.getNome()))
				{carteInMano.remove(i); break;}
		}
	}
	
	//get dell'arraylist carte in mano.
	public ArrayList<Carta> getCarteInMano(){
		return carteInMano;
	}
	
	//metodo che resituisce la dimensione dell'arraylist carte in mano.
	public int size() {
		return carteInMano.size();
	}
	
	//get dell'int che indica il numero delle scope.
	public int getScope() {
		return scope;
	}
	
	//metodo che incrementa il numero scope.
	public void incrementaScope() {
		scope++;
	}
	
	//get dell'arraylist carte prese.
	public ArrayList<Carta> getCartePrese(){
		return cartePrese;
	}
	
	//metodo che resituisce quanti denari sono stati presi dal giocatore.
	public int denariPresi() {
		int n = 0;
		for(Carta carta: cartePrese) {
			if(carta.getSeme()==2)
				n++;
		}
		return n;
	}
	
	//metodo che resituisce true se il giocatore ha preso il sette di denari, false altrimenti.
	public boolean setteDiDenari() {
		for(Carta carta: cartePrese) {
			if(carta.getNumero()==7 && carta.getSeme()==2)
				return true;
		}
		return false;
	}
	
	//metodo che restituisce il punteggio della settanta di ogni giocatore.
	public int settanta() {
		int[] puntiPerSeme = new int[4];
		int sommaPunti = 0;
		
		for(int i = 0; i<4; i++)
		{
			int punteggio = 0;
			for(Carta carta: cartePrese) {
				if(carta.getSeme()==i && carta.getPunteggio()>punteggio) {
					punteggio = carta.getPunteggio();
					puntiPerSeme[i] = punteggio;
				}
			}
		}	
		for(int i = 0; i<4; i++)
			sommaPunti = sommaPunti + puntiPerSeme[i];
		return sommaPunti;
	}
}
