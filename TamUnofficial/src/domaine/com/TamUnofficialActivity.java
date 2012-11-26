package domaine.com;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class TamUnofficialActivity extends TabActivity  {
    
	private TabHost tabHost;

	private TabSpec tabSpec;

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //initialisation de la class utilitaires
        Utilitaires.initialiserUtilitaires();

        Intent intentLigne = new Intent(this, LigneActivity.class);
        Intent intentFavoris = new Intent(this, FavorisActivity.class);
        
        tabHost = getTabHost();

        //1er onglet de la tab
        tabSpec = tabHost.newTabSpec("Lignes").setIndicator("Lignes").setContent(intentLigne);
        tabHost.addTab(tabSpec);
        
        //deuxieme onglet de la Tab
        tabSpec = tabHost.newTabSpec("Favoris").setIndicator("Favoris").setContent(intentFavoris);
        tabHost.addTab(tabSpec);



        
    
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

  
	
}	
