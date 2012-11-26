package domaine.com;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

public class BoutonArret implements OnClickListener {
	
	String id = null;
	TextView lebouton = null;
	Activity vue = null;
	String idMobitrans = null;
	public static final int CODE_RETOUR = 0;

	
	public BoutonArret(String unId, TextView unBouton, Activity laVue, String unIdMobitrans) {
		// TODO Auto-generated constructor stub
		super();
		id = unId;
		lebouton = unBouton;
		vue = laVue;
		idMobitrans = unIdMobitrans;
		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
    	if(v == lebouton) {
    		Intent monIntent = new Intent(vue,ArretActivity.class);
    		monIntent.putExtra("num_ligne", id);
    		monIntent.putExtra("idMobitrans", idMobitrans);
    		vue.startActivityForResult(monIntent, CODE_RETOUR);
    	}
	}

}
