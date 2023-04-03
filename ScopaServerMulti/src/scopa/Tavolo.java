package scopa;

import java.io.Serializable;
import java.util.ArrayList;

public class Tavolo implements Serializable{
	
	private static final long serialVersionUID = -1;
	private ArrayList<Carta> carteInTavolo  = new ArrayList<>();
	
	//metodo che aggiunge la carta passata come argomento al tavolo corrente.
	public void aggiungiCartaInTavolo(Carta cartaDaAggiungere) {
		carteInTavolo.add(cartaDaAggiungere);
	}
	
	//metodo che restituisce l'arraylist delle carte presenti sul tavolo.
	public ArrayList<Carta> getCarteInTavolo(){
		return carteInTavolo;
	}
	
	//metodo che restituisce il numero delle carte in tavolo.
	public int numeroCarteInTavolo() {
		return carteInTavolo.size();
	}
	
	//metodo che rimuove dal tavolo la carta passata come argomento e che in caso di scopa assegna anche il punto.
	public void rimuoviCartaInTavolo(Carta cartaDaRimuovere, Giocatore giocatore) {
		for(int i = 0; i < carteInTavolo.size(); i++) {
			if(cartaDaRimuovere.getNome().equals(carteInTavolo.get(i).getNome()))
				{carteInTavolo.remove(i); break;}
		}
		if(carteInTavolo.size() == 0)
			giocatore.incrementaScope();
	}
}
