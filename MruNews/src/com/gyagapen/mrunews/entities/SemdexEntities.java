package com.gyagapen.mrunews.entities;

import java.util.ArrayList;

/**
 * List of semdex entity
 * 
 * @author guiyag
 * 
 */
public class SemdexEntities {

	private ArrayList<SemdexEntity> semdexEntities = new ArrayList<SemdexEntity>();
	private String lastUpdated = "";

	public SemdexEntities() {
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public ArrayList<SemdexEntity> getSemdexEntities() {
		return semdexEntities;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public void setSemdexEntities(ArrayList<SemdexEntity> semdexEntities) {
		this.semdexEntities = semdexEntities;
	}

}
