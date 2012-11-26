package domaine.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ArretActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.arretlayout);
        LinearLayout lay = (LinearLayout)findViewById(R.id.layoutest);
        
        
        //recuperation du button
        //Button buttonValider = (Button)findViewById(R.id.buttonValider);
        
        
        //String html = "";
        
		String num_ligne = null;
		String idMobitrans = null;
		
		
		//on recupere l'id et le mobitrans id
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    num_ligne= extras.getString("num_ligne");
		    idMobitrans = extras.getString("idMobitrans");
		    
		}
		
		
		 //creation de la BD
	      
        //Création d'une instance de la BD
        DatabaseHelper bdd = new DatabaseHelper(this);
        
        //on recupere les lignes de transport
        Arret[] res = bdd.getArretFromLigne(num_ligne);
        
        //pour chaque ligne on cree le bouton associe
        for(int i=0; i<res.length; i++)
        {
        	Arret lArret = res[i];
        	ajoutButton(lArret.getNom_arret(),  lay, lArret.getNum_arret(), this, idMobitrans);
        }
        
       bdd.close();
       
       //recuperation du button
       Button buttonValider = (Button)findViewById(R.id.buttonValider);
       
       buttonValider.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

					//Creation de l'intent
					Intent monIntent = new Intent();
					
	
					
					
					
					//on rajoute la variable nom dans l'intent
					//monIntent.putExtra("Nom", "");
					
					
					//on retourne le resultat avec comme message OK
					setResult(RESULT_OK, monIntent);
					
					//on termine l'activite
					finish();
					
					
				
				
				
			}
		});
        
        
        
        
       
	}
	
	   public void  ajoutButton(String contenu, LinearLayout lay, String unId, Activity laVue, String idMobitrans)
	   {
			
		   Button unBouton = new Button(this);
		   unBouton.setText(contenu);
		   lay.addView(unBouton);
	      
	        OnClickListener test = new BoutonInfo(unId, unBouton, laVue, idMobitrans);
		   unBouton.setOnClickListener(test);
	   }
	   
	   
	   
	   protected void onActivityResult(int requestCode, int resultCode, Intent data)
	    {


	    	// Vérification du code de retour



	    		// Vérifie que le résultat est OK

	    		if(resultCode == RESULT_OK) {


	    			// Si l'activité est annulé

	    		} else if (resultCode == RESULT_CANCELED) {

	    			// On affiche que l'opération est annulée

	    			Toast.makeText(this, "Opération annulé", Toast.LENGTH_SHORT).show();

	    		}else if (resultCode == 101) {
	    			
	    			//pas de connexion possible avec le serveur
	    			Toast.makeText(this, "Connexion serveur impossible", Toast.LENGTH_SHORT).show();
	    			
	    			//Creation de l'intent
	    			Intent monIntent = new Intent();
	    					
	    			
	    			//on retourne le resultat avec comme message OK
	    			setResult(RESULT_OK, monIntent);
	    			
	    			//on termine l'activite
	    			finish();
	    		}




	    }
	   

}
