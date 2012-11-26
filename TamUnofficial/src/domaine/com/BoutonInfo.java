package domaine.com;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BoutonInfo implements OnClickListener {
	
	String id = null;
	Button lebouton = null;
	Activity vue = null;
	String idMobitrans = null;
	public static final int CODE_RETOUR = 0;

	
	public BoutonInfo(String unId, Button unBouton, Activity laVue, String unIdMobitrans) {
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
    		Intent monIntent = new Intent(vue,AutreActivity.class);
    		monIntent.putExtra("id", id);
    		monIntent.putExtra("idMobitrans", idMobitrans);
    		vue.startActivityForResult(monIntent, CODE_RETOUR);
    	}
	}

}
