package scopa;

import java.awt.Image;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.ImageIcon;

//0 per bastoni, 1 per coppe, 2 per denari, 3 per spade.

public class Mazzo implements Serializable {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4780094458977446819L;
	private String[] nomiCarte = {"1b","2b","3b","4b","5b","6b","7b","8b","9b","10b","1c","2c","3c","4c","5c","6c","7c","8c","9c","10c","1d","2d","3d","4d","5d","6d","7d","8d","9d","10d","1s","2s","3s","4s","5s","6s","7s","8s","9s","10s"};
	private int[] punteggiCarte = {16,12,13,14,15,18,21,10,10,10};
	private String[] nomiImmaginiCarte = {"Bastoni (1).png", "Bastoni (2).png", "Bastoni (3).png", "Bastoni (4).png", "Bastoni (5).png", "Bastoni (6).png", "Bastoni (7).png", "Bastoni (8).png", "Bastoni (9).png", "Bastoni (10).png", "Coppe (1).png", "Coppe (2).png", "Coppe (3).png", "Coppe (4).png", "Coppe (5).png", "Coppe (6).png", "Coppe (7).png", "Coppe (8).png", "Coppe (9).png", "Coppe (10).png", "Denari (1).png", "Denari (2).png", "Denari (3).png", "Denari (4).png", "Denari (5).png", "Denari (6).png", "Denari (7).png", "Denari (8).png", "Denari (9).png", "Denari (10).png", "Spade (1).png", "Spade (2).png", "Spade (3).png", "Spade (4).png", "Spade (5).png", "Spade (6).png", "Spade (7).png", "Spade (8).png", "Spade (9).png", "Spade (10).png"};
	private ArrayList<Carta> mazzo;
	private URL[] urlImmagini = new URL[40];
	
	//costruttore
	public Mazzo() {
		creaMazzo();
		Collections.shuffle(mazzo);
	}
	
	//metodo che crea il mazzo da zero, creando quindi tutti gli oggetti carte che compongono il mazzo.
	public void creaMazzo() {
		mazzo = new ArrayList<>();
		int count = 0;
		for(int i = 0; i < 40; i++) {
			try {
				urlImmagini[i] = new URL("file:///C:\\Users\\Giuseppe\\eclipse-workspace\\ScopaServer\\src\\scopa\\" + nomiImmaginiCarte[i]);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		for(int j = 0; j<4; j++) {
			for(int i = 0; i<10; i++) {
				Carta carta = new Carta(nomiCarte[count], j, i+1, punteggiCarte[i], urlImmagini[count]);
				mazzo.add(carta);
				count++;
			}
		}
		
	}
	
	//metodo che restituisce il mazzo.
	public ArrayList<Carta> getMazzo(){
		return mazzo;
	}
	
	//ritorna la lunghezza del mazzo.
	public int getSize() {
		return mazzo.size();
	}
	
	//metodo che pesca una carta dal mazzo e la ritorna.
	public Carta pescata() {
		Carta cartaPescata = mazzo.get(mazzo.size()-1);
		mazzo.remove(mazzo.get(mazzo.size()-1));
		return cartaPescata;
	}
}
