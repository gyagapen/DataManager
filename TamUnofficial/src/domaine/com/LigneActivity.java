package domaine.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LigneActivity extends Activity implements Runnable {
	
	Button monBouton = null;
	DatabaseHelper bdd = null;
	volatile String idMobitrans;
	volatile Handler handler;
	volatile Ligne[] res;
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

        		Ligne uneLigne = res[j];
        		ajoutButton("Ligne "+uneLigne.getNum_ligne(),  lay, uneLigne.getNum_ligne(), ctx, idMobitrans);
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
    

     
        
        
        
        
       
        
        /*ajoutButton("16/Val d'aurelle",  lay, "419", this, idMobitrans);
        ajoutButton("1/Comedie",  lay, "5", this, idMobitrans);
        ajoutButton("1/Odysseum",  lay, "20", this, idMobitrans);*/
    
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
    		



    }

   public void  ajoutButton(String contenu, LinearLayout lay, String unId, Activity laVue, String idMobitrans)
   {
		
	   TextView unBouton = new TextView(this);
	   unBouton.setText(contenu);
	   unBouton.setHeight(50);
	   lay.addView(unBouton);
      
        OnClickListener test = new BoutonArret(unId, unBouton, laVue, idMobitrans);
	   unBouton.setOnClickListener(test);
   }
   
   @Override
   protected void onDestroy() {
	   	// TODO Auto-generated method stub
	   super.onDestroy();
	   
   }



@Override
public void run() {
	
	
	//on recupere l'id mobitrans a travers la classe utilitaires
    idMobitrans = Utilitaires.getIdMobitrans();
    
    
    //code erreur
    if (idMobitrans == "101")
    {
    	handler.sendEmptyMessage(101);
    }
    
    //Création d'une instance de la BD
    bdd = new DatabaseHelper(this);
   
  
   
   
   //on recupere les lignes de transport
   res = bdd.getAllLigne();

   bdd.close();
   
   //pour chaque ligne on cree le bouton associe
   for(int i=0; i<res.length; i++)
   {
   		//uneLigne = res[i];
   		handler.sendEmptyMessage(0);

   		//ajoutButton("Ligne "+laLigne.getNum_ligne(),  lay, laLigne.getNum_ligne(), this, idMobitrans);
   }



    //on ferme la boite de dialogue
    progressDialog.dismiss();
    
    

   
	
}
	
}	



