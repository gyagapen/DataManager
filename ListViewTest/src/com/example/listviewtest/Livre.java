package com.example.listviewtest;

public class Livre {
	
	private String titre;
	private String auteur;
	
	
	public Livre(String titre, String auteur) {
		this.titre = titre;
		this.auteur = auteur;
	}
	
	public String getAuteur() {
		return auteur;
	}
	
	public String getTitre() {
		return titre;
	}
	
	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}
	
	public void setTitre(String titre) {
		this.titre = titre;
	}

}
