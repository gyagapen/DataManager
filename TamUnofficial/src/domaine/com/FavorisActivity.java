package domaine.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FavorisActivity extends Activity implements Runnable {
	
	Button monBouton = null;
	DatabaseHelper bdd = null;
	volatile String idMobitrans;
	volatile Handler handler;
	volatile Arret[] res;
	volatile LinearLayout lay;
	volatile Activity ctx;
	volatile int j;

	//boite de dialogue : pour patienter
	ProgressDialog progressDialog;


	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutligne);
        

        
        //initialisation de la boite de dialogue
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Chargement en cours");


        idMobitrans = null;
        
        lay = (LinearLayout)findViewById(R.id.ButtonLigne);
    	
        //affichage de la boite de dialog
        progressDialog.show();
        
        //on cree une nouveau thread qui va faire la demande id Mobitrans
        //pendant ce temps une boite de dialogue demandant de patienter s'affichera
        Thread t1 = new Thread(this);
       
        t1.start();
    	

        
         ctx = this;
         j = 0;
        
         handler = new Handler() {

        	public void handleMessage(android.os.Message msg) {

        	if(msg.what == 0) {

        		Arret unArret = res[j];
        		ajoutButton("L"+unArret.getNum_ligne()+" "+unArret.getNom_arret(),  lay, unArret.getNum_arret(), ctx, idMobitrans);
        		j++;

        	}
        	if(msg.what == 101) {

            	AlertDialog alert = new AlertDialog.Builder(ctx).create();
            	alert.setTitle("Erreur...");
            	alert.setMessage("Pas de connexion internet ! Fermeture de l'application");
            	alert.setButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						finish();
					}
				});
            	alert.show();
            	


        	}
        	

        	};

        	};
    
    
    }
        
   protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {


    		// Vérifie que le résultat est OK

    		if(resultCode == RESULT_OK) {

    			// On récupére le paramètre "Nom" de l'intent

    			String nom = data.getStringExtra("Nom");

    			// On affiche le résultat

    			//Toast.makeText(this, "Votre nom est : " + nom, Toast.LENGTH_SHORT).show();

    			// Si l'activité est annulé

    		} else if (resultCode == RESULT_CANCELED) {

    			// On affiche que l'opération est annulée

    			Toast.makeText(this, "Opération annulé", Toast.LENGTH_SHORT).show();

    		}else if (resultCode == 101) {
    			
    			//pas de connexion possible avec le serveur

    			Toast.makeText(this, "Connexion serveur impossible", Toast.LENGTH_SHORT).show();

    		}
    		else if (resultCode == 102) {
    			
    	        /*//initialisation de la boite de dialogue
    	        progressDialog = new ProgressDialog(this);
    	        progressDialog.setMessage("Chargement en cours");


    	        idMobitrans = null;
    	        
    	        lay = (LinearLayout)findViewById(R.id.ButtonLigne);
    	    	
    	        //affichage de la boite de dialog
    	        progressDialog.show();
    	        
    	        //on cree une nouveau thread qui va faire la demande id Mobitrans
    	        //pendant ce temps une boite de dialogue demandant de patienter s'affichera
    	        Thread t1 = new Thread(this);
    	       
    	        t1.start();
*/    			
    		}
    		



    }

   public void  ajoutButton(String contenu, LinearLayout lay, String unId, Activity laVue, String idMobitrans)
   {
		
	   Button unBouton = new Button(this);
	   unBouton.setText(contenu);
	   lay.addView(unBouton);
      
        OnClickListener test = new BoutonInfo(unId, unBouton, laVue, idMobitrans);
	   unBouton.setOnClickListener(test);
   }
   
   @Override
   protected void onDestroy() {
	   	// TODO Auto-generated method stub
	   super.onDestroy();
	   
   }



@Override
public void run() {
	
	
    if (idMobitrans == null)
    {
    	//on recupere l'id mobitrans a travers la classe utilitaires
    	idMobitrans = Utilitaires.getIdMobitrans();
    }
    
    
    //code erreur
    if (idMobitrans == "101")
    {
    	handler.sendEmptyMessage(101);
    }
    
    //Création d'une instance de la BD
    bdd = new DatabaseHelper(this);
   
  
   
   
   //on recupere les arrets favoris
   res = bdd.getArretFavoris();
   
   bdd.close();

   //s'il y a au moins un favoris
   if (res != null)
   {
	   //pour chaque ligne on cree le bouton associe
	   for(int i=0; i<res.length; i++)
	   {
		   //uneLigne = res[i];
		   handler.sendEmptyMessage(0);

		   //ajoutButton("Ligne "+laLigne.getNum_ligne(),  lay, laLigne.getNum_ligne(), this, idMobitrans);
	   }
   }



    //on ferme la boite de dialogue
    progressDialog.dismiss();
    
    

   
	
}

@Override
protected void onRestart() {
	
	super.onRestart();
	//initialisation de la boite de dialogue
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Chargement en cours");

    j = 0;
    
    
    lay = (LinearLayout)findViewById(R.id.ButtonLigne);
    
    lay.removeAllViews();
    
	
    //affichage de la boite de dialog
    progressDialog.show();
    
    //on cree une nouveau thread qui va faire la demande id Mobitrans
    //pendant ce temps une boite de dialogue demandant de patienter s'affichera
    Thread t1 = new Thread(this);
   
    t1.start();
}
	
}	



