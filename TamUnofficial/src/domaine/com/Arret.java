package domaine.com;

public class Arret {
	
	private String num_ligne;
	private String num_arret;
	private String nom_arret;
	private int favoris; //1 non et 0 oui
	
	public Arret(String unNumLigne, String unNumArret, String unNomArret) {
		
		num_ligne = unNumLigne;
		num_arret = unNumArret;
		nom_arret = unNomArret;
	}
	
	public String getNom_arret() {
		return nom_arret;
	}
	
	public String getNum_ligne() {
		return num_ligne;
		
		
	}public String getNum_arret() {
		return num_arret;
	}
	
	public void setNom_arret(String nom_arret) {
		this.nom_arret = nom_arret;
	}
	
	public void setNum_arret(String num_arret) {
		this.num_arret = num_arret;
	}
	
	public void setNum_ligne(String num_ligne) {
		this.num_ligne = num_ligne;
	}

}
