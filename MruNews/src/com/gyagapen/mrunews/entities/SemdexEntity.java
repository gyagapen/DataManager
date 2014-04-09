package com.gyagapen.mrunews.entities;

public class SemdexEntity {
	
	private String name;
	private String nominal;
	private String lastClosingPrice;
	private String latestPrice;
	private String changePrice;
	private String changePercentage;
	
	
	public SemdexEntity() {
	}
	
	public String getChangePercentage() {
		return changePercentage;
	}
	
	public String getChangePrice() {
		return changePrice;
	}
	
	public void setChangePercentage(String changePercentage) {
		this.changePercentage = changePercentage;
	}
	
	public void setChangePrice(String changePrice) {
		this.changePrice = changePrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNominal() {
		return nominal;
	}

	public void setNominal(String nominal) {
		this.nominal = nominal;
	}

	public String getLastClosingPrice() {
		return lastClosingPrice;
	}

	public void setLastClosingPrice(String lastClosingPrice) {
		this.lastClosingPrice = lastClosingPrice;
	}

	public String getLatestPrice() {
		return latestPrice;
	}

	public void setLatestPrice(String latest) {
		this.latestPrice = latest;
	}
	

}
