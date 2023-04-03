package scopa;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JToggleButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class CampoDaGioco extends JFrame {
	private ArrayList<Carta> combinazione;
	private JPanel contentPane;
	private Carta cartaGiocata;
	private Carta cartaSelezionata;
	private final ButtonGroup buttonGroup = new ButtonGroup(); 
	
	private JButton EnemyCard;
	private JToggleButton Hand1;
	private JToggleButton Hand2;
	private JToggleButton Hand3;
	JToggleButton cTav[] = new JToggleButton[8];
	JToggleButton cMan[] = new JToggleButton[3];
	JLabel labelTurno;
	JButton btnCombinazione;
	JButton GiocaCarta;
	private ActionListener tavAction[] = new ActionListener[8];
	boolean [] primaVolta = {true, true, true};
	
	private ImageIcon immagine;
	private boolean b;
	
	Color coloreSfondo = new Color(0, 160, 50);
	Color coloreManoSelezionata = new Color(180, 150, 50);
	Color coloreTavoloSelezionata = new Color(255, 50, 50);
	Color coloreBottoni = new Color(90,255,170);
	
	//costruttore della classe.
	public CampoDaGioco(ImageIcon immagine_, String stringa, boolean b_) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {this.setIconImage(new ImageIcon(new URL("file:///C:\\Users\\Giuseppe\\eclipse-workspace\\Scopa\\src\\scopa\\" + "Bastoni (1).png")).getImage());
		} catch (MalformedURLException e1) {}
		this.setTitle("Scopa");
		setBounds(100, 60, 940, 650);
		contentPane = new JPanel();
		contentPane.setBackground(coloreSfondo);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		combinazione = new ArrayList<>();
		this.immagine = immagine_;
		this.b = b_;
		
		EnemyCard = new JButton("");
		EnemyCard.setBackground(coloreSfondo);
		EnemyCard.setIcon(immagine);
		EnemyCard.setBounds(760, 250, 103, 141);
		contentPane.add(EnemyCard);
		
		tavAction[0] = e -> selezionaCartaTavolo0();
		tavAction[1] = e -> selezionaCartaTavolo1();
		tavAction[2] = e -> selezionaCartaTavolo2();
		tavAction[3] = e -> selezionaCartaTavolo3();
		tavAction[4] = e -> selezionaCartaTavolo4();
		tavAction[5] = e -> selezionaCartaTavolo5();
		tavAction[6] = e -> selezionaCartaTavolo6();
		tavAction[7] = e -> selezionaCartaTavolo7();
		
		
		for (int i=0; i<8;i++) {
			if (Client.tavolo.getCarteInTavolo().size()>i) 
				cTav[i]= new JToggleButton("", new ImageIcon(Client.tavolo.getCarteInTavolo().get(i).getImmagine()));
			else cTav[i]= new JToggleButton("", null);
			cTav[i].addActionListener(tavAction[i]);
			cTav[i].setBackground(coloreSfondo);
			cTav[i].setBorderPainted(false);
			if (i<4) cTav[i].setBounds(10+103*i,57,103,141); else cTav[i].setBounds(10+103*(i-4),209,103,141);
			contentPane.add(cTav[i]);
			if (Client.tavolo.getCarteInTavolo().size()<=i) cTav[i].setVisible(false);
		}
		
		if(Client.giocatore.getCarteInMano().size()>0) {
			Hand1 = new JToggleButton("", new ImageIcon(Client.giocatore.getCarteInMano().get(0).getImmagine()));
			buttonGroup.add(Hand1);
			Hand1.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent arg0) {
					if (cartaGiocata != null) return;
					cartaSelezionata = Client.giocatore.getCarteInMano().get(0);
				}
			});
			Hand1.setBounds(60, 426, 103, 141);
			Hand1.setBorderPainted(false);
			Hand1.setBackground(coloreSfondo);
			contentPane.add(Hand1);
		}
		
		if(Client.giocatore.getCarteInMano().size()>1) {
			Hand2 = new JToggleButton("", new ImageIcon(Client.giocatore.getCarteInMano().get(1).getImmagine()));
			buttonGroup.add(Hand2);
			Hand2.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent arg0) {
					if (cartaGiocata != null) return;
					cartaSelezionata = Client.giocatore.getCarteInMano().get(1);
				}
			});
			Hand2.setBounds(163, 426, 103, 141);
			Hand2.setBorderPainted(false);
			Hand2.setBackground(coloreSfondo);
			contentPane.add(Hand2);
		}
		
		if(Client.giocatore.getCarteInMano().size()>2) {
			Hand3 = new JToggleButton("", new ImageIcon(Client.giocatore.getCarteInMano().get(2).getImmagine()));
			buttonGroup.add(Hand3);
			Hand3.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent arg0) {
					if (cartaGiocata != null) return;
					cartaSelezionata = Client.giocatore.getCarteInMano().get(2); //se il bottone viene cliccato, la variabile cartaSelezionata assume il valore della carta cliccata.
				}
			});
			Hand3.setBounds(266, 426, 103, 141);
			Hand3.setBorderPainted(false);
			Hand3.setBackground(coloreSfondo);
			contentPane.add(Hand3);
		}
		
		GiocaCarta = new JButton("Gioca la carta");
		GiocaCarta.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if(cartaSelezionata != null) {
					cartaGiocata = cartaSelezionata;
					if(!cartaIntera() && !combinazioniCoppie() && !combinazioniTerne() && !combinazioniQuaterne()) {
						Client.inviaMessaggio(new Messaggio("comb", cartaGiocata, null, null, null));
						cartaGiocata = null;
						cartaSelezionata = null;
						combinazione = new ArrayList<Carta>();
						Hand1.doClick(); Hand2.doClick(); Hand3.doClick();
						for (JToggleButton b : cTav)
							b.setSelected(false);
					} else {
						if (primaVolta[0]) {JOptionPane.showMessageDialog(null,"Seleziona la/e carta/e che vuoi prendere."); primaVolta[0]=false;}
						GiocaCarta.setVisible(false);
						Hand1.doClick(); Hand2.doClick(); Hand3.doClick();
					}
				}
				else 
					JOptionPane.showMessageDialog(null,"Devi selezionare una carta da giocare.");
			}
		});
		GiocaCarta.setToolTipText("");
		GiocaCarta.setBounds(480, 465, 200, 60);
		GiocaCarta.setBackground(coloreBottoni);
		GiocaCarta.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		contentPane.add(GiocaCarta);
		
		JLabel lblNewLabel = new JLabel("<html>Ultima carta giocata<p>dall'avversario:</html>");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblNewLabel.setBounds(760, 150, 103, 100);
		contentPane.add(lblNewLabel);
		
		JLabel carteInTavolo = new JLabel("CARTE IN TAVOLO");
		carteInTavolo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
		carteInTavolo.setBounds(50, 12, 300, 38);
		contentPane.add(carteInTavolo);
		
		btnCombinazione = new JButton("Invia combinazione");
		btnCombinazione.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if(cartaGiocata != null) {
					if(combinazione.size() != 0) {
						if(verificaCombinazione()) {
							Client.inviaMessaggio(new Messaggio("comb", cartaGiocata, combinazione, null, null));
							cartaGiocata = null;
							cartaSelezionata = null;
							combinazione = new ArrayList<Carta>();
							Hand1.doClick(); Hand2.doClick(); Hand3.doClick();
							for (JToggleButton b : cTav)
								b.setSelected(false);
						} else {
							JOptionPane.showMessageDialog(null,"La combinazione selezionata non è corretta, selezionare una nuova combinazione.");
							combinazione=new ArrayList<Carta>();
							for (JToggleButton b : cTav)
								b.setSelected(false);
						}
					} else {
						if (primaVolta[1]) {JOptionPane.showMessageDialog(null,"Scegli la combinazione che vuoi prendere."); primaVolta[1]=false;}}
				} else { 
					JOptionPane.showMessageDialog(null,"Devi prima giocare una carta, se non ne hai già giocata una.");
					combinazione=new ArrayList<Carta>();
					for (JToggleButton b : cTav)
						b.setSelected(false);
				}
		}});
		btnCombinazione.setBounds(480, 140, 200, 60);
		btnCombinazione.setBackground(coloreBottoni);
		btnCombinazione.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		contentPane.add(btnCombinazione);
		
		
		if(!b) {
			btnCombinazione.setVisible(false);
			GiocaCarta.setVisible(false);}
		
		labelTurno = new JLabel(stringa);
		labelTurno.setBounds(490, 70, 200, 70);
		labelTurno.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		labelTurno.setForeground(new Color(220, 255, 230));
		contentPane.add(labelTurno);
	}
	
	void selezionaCartaTavolo0() {
		if(presente(Client.tavolo.getCarteInTavolo().get(0))) combinazione.remove(Client.tavolo.getCarteInTavolo().get(0)); 
		else combinazione.add(Client.tavolo.getCarteInTavolo().get(0));
	}
	void selezionaCartaTavolo1() {
		if(presente(Client.tavolo.getCarteInTavolo().get(1))) combinazione.remove(Client.tavolo.getCarteInTavolo().get(1)); 
		else combinazione.add(Client.tavolo.getCarteInTavolo().get(1));
	}
	void selezionaCartaTavolo2() {
		if(presente(Client.tavolo.getCarteInTavolo().get(2))) combinazione.remove(Client.tavolo.getCarteInTavolo().get(2)); 
		else combinazione.add(Client.tavolo.getCarteInTavolo().get(2));
	}
	void selezionaCartaTavolo3() {
		if(presente(Client.tavolo.getCarteInTavolo().get(3))) combinazione.remove(Client.tavolo.getCarteInTavolo().get(3)); 
		else combinazione.add(Client.tavolo.getCarteInTavolo().get(3));
	}
	void selezionaCartaTavolo4() {
		if(presente(Client.tavolo.getCarteInTavolo().get(4))) combinazione.remove(Client.tavolo.getCarteInTavolo().get(4)); 
		else combinazione.add(Client.tavolo.getCarteInTavolo().get(4));
	}
	void selezionaCartaTavolo5() {
		if(presente(Client.tavolo.getCarteInTavolo().get(5))) combinazione.remove(Client.tavolo.getCarteInTavolo().get(5)); 
		else combinazione.add(Client.tavolo.getCarteInTavolo().get(5));
	}
	void selezionaCartaTavolo6() {
		if(presente(Client.tavolo.getCarteInTavolo().get(6))) combinazione.remove(Client.tavolo.getCarteInTavolo().get(6)); 
		else combinazione.add(Client.tavolo.getCarteInTavolo().get(6));
	}
	void selezionaCartaTavolo7() {
		if(presente(Client.tavolo.getCarteInTavolo().get(7))) combinazione.remove(Client.tavolo.getCarteInTavolo().get(7)); 
		else combinazione.add(Client.tavolo.getCarteInTavolo().get(7));
	}
	
	//ritorna true se la carta selezionata è già presente nell'ArrayList combinazione.
	public boolean presente(Carta carta) {
		for(int i = 0; i < combinazione.size(); i++) {
			if(combinazione.get(i) == carta)
				return true;
		}
		return false;
	}
	
	//metodo che ritorna la carta scelta, dal giocatore, per essere giocata.
	public Carta getCartaGiocata() {
		return cartaGiocata;
	}
	
	//metodo che mette a schermo il risultato della partita
	public void risultatoPartita(String risultato) {
		JOptionPane.showMessageDialog(null, risultato);
	}
	
	public void combinazioneNonCorretta() {
		JOptionPane.showMessageDialog(null, "La combinazione di carte selezionata non è corretta.");
	}
	
	public void zeroMosseDisponibili() {
		JOptionPane.showMessageDialog(null, "Non puoi prendere niente con la carta che hai giocato.");
	}
	
	public void selezionaCartaIntera() {
		JOptionPane.showMessageDialog(null, "Seleziona la carta intera che desideri prendere. Devi per forza prendere la carta intera se è presente sul tavolo.");
	}
	
	public void selezionaCombinazione() {
		JOptionPane.showMessageDialog(null, "Seleziona la combinazione di carte che vuoi prendere");
	}
	
	//metodo che verifica la correttezza della combinazione scelta dal giocaotre.
	public boolean verificaCombinazione() {
		int somma = 0;
		for(int i = 0; i < combinazione.size(); i++) {
			somma = somma + combinazione.get(i).getNumero();
		}
		if(somma == cartaGiocata.getNumero()) 
			return true;
		else 
			return false;
	}
	
	//metodo che verifica se è presente una carta intera che può essere presa dal giocatore.
	public boolean cartaIntera() {
		for(int i = 0; i < Client.tavolo.numeroCarteInTavolo(); i++) {
			if(cartaGiocata.getNumero() == Client.tavolo.getCarteInTavolo().get(i).getNumero())
				return true;
		}
		return false;
	}
	
	//metodo che restituisce true se è presente almeno una combinazione di 2 carte che dia il numero della carta giocata.
	public boolean combinazioniCoppie() {
		for(int i = 0; i < Client.tavolo.numeroCarteInTavolo(); i++) {
			for(int j = i+1; j < Client.tavolo.numeroCarteInTavolo(); j++) {
				if(Client.tavolo.getCarteInTavolo().get(i).getNumero() + Client.tavolo.getCarteInTavolo().get(j).getNumero() == cartaGiocata.getNumero())
					return true;
			}
		}
		return false;
	}
	
	//metodo che restituisce true se è presente almeno una combinazioni di 3 carte che dia il numero della carta giocata.
	public boolean combinazioniTerne() {
		for(int i = 0; i < Client.tavolo.numeroCarteInTavolo(); i++) {
			for(int j = i + 1; j < Client.tavolo.numeroCarteInTavolo(); j++) {
				for(int k = j + 1; k< Client.tavolo.numeroCarteInTavolo(); k++) {
					if(Client.tavolo.getCarteInTavolo().get(i).getNumero() + Client.tavolo.getCarteInTavolo().get(j).getNumero() + Client.tavolo.getCarteInTavolo().get(k).getNumero() == cartaGiocata.getNumero())
						return true;
				}
			}
		}
		return false;
	}
	
	//metodo che restituisce true se è presente almeno una combinazioni di 4 carte che dia il numero della carta giocata.
	public boolean combinazioniQuaterne() {
		for(int i = 0; i < Client.tavolo.numeroCarteInTavolo(); i++) {
			for(int j = i + 1; j < Client.tavolo.numeroCarteInTavolo(); j++) {
				for(int k = j + 1; k < Client.tavolo.numeroCarteInTavolo(); k++) {
					for(int h = k + 1; h < Client.tavolo.numeroCarteInTavolo(); h++) {
						if(Client.tavolo.getCarteInTavolo().get(i).getNumero() + Client.tavolo.getCarteInTavolo().get(j).getNumero() + Client.tavolo.getCarteInTavolo().get(k).getNumero() + Client.tavolo.getCarteInTavolo().get(h).getNumero() == cartaGiocata.getNumero())
							return true;
					}
				}
			}
		}
		return false;
	}
	
	//metodo che modifica la grafica corrente.
	public void modificaGrafica(Giocatore giocatore, Tavolo tavolo, ImageIcon immagine, String stringa, boolean b) {
		this.b = b;
		if(immagine != null)
			EnemyCard.setIcon(immagine);
		if (tavolo !=null) {
			for (int f=0; f<8;f++) {
				if (f<tavolo.getCarteInTavolo().size()) {
					cTav[f].setVisible(true);
					cTav[f].setIcon(new ImageIcon(tavolo.getCarteInTavolo().get(f).getImmagine()));
				} else cTav[f].setVisible(false);
			}
		}
		if (giocatore!=null) {
			if(Client.giocatore.getCarteInMano().size()>0) {
				Hand1.setVisible(true);
				Hand1.setIcon(new ImageIcon(giocatore.getCarteInMano().get(0).getImmagine()));
			} else Hand1.setVisible(false);
			if(Client.giocatore.getCarteInMano().size()>1) {
				Hand2.setVisible(true);
				Hand2.setIcon(new ImageIcon(giocatore.getCarteInMano().get(1).getImmagine()));
			} else Hand2.setVisible(false);
			if(Client.giocatore.getCarteInMano().size()>2) {
				Hand3.setVisible(true);
				Hand3.setIcon(new ImageIcon(giocatore.getCarteInMano().get(2).getImmagine()));
			} else Hand3.setVisible(false);
		}
		GiocaCarta.setVisible(b);
		btnCombinazione.setVisible(b);
		labelTurno.setText(stringa);
	}
}
