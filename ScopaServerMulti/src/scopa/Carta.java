package scopa;

import java.awt.Image;
import java.io.Serializable;
import java.net.URL;

import javax.swing.ImageIcon;

public class Carta implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7999358490766142373L;
	private String nome;
	private int seme;
	private int numero;
	private int punteggio;
	private URL immagine;
	
	
	public Carta() {
		
	}
	
	public Carta(String nome, int seme, int numero, int punteggio, URL immagine) {
		this.nome = nome;
		this.seme = seme;
		this.numero = numero;
		this.punteggio = punteggio; 
		this.immagine = immagine;
	}
	
	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public int getSeme() {
		return seme;
	}


	public void setSeme(int seme) {
		this.seme = seme;
	}


	public int getNumero() {
		return numero;
	}


	public void setNumero(int numero) {
		this.numero = numero;
	}


	public int getPunteggio() {
		return punteggio;
	}


	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}


	public URL getImmagine() {
		return immagine;
	}


	public void setImmagine(URL immagine) {
		this.immagine = immagine;
	}


}
