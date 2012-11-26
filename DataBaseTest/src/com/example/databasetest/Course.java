package com.example.databasetest;

public class Course {
	
	private int id;
	private String produit;
	private int quantite;
	private boolean achete;
	
	public Course(String produit, int quantite)
	{
		this.produit = produit;
		this.quantite = quantite;
		this.achete = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProduit() {
		return produit;
	}

	public void setProduit(String produit) {
		this.produit = produit;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

	public boolean isAchete() {
		return achete;
	}

	public void setAchete(boolean achete) {
		this.achete = achete;
	}

	
}
