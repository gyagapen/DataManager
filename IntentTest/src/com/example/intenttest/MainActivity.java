package com.example.intenttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Button monBouton = null;
	
	public static final int CODE_RETOUR = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		monBouton = (Button) findViewById(R.id.buttonToast);

		//add listener to button
		monBouton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void onClick(View v) {
		

		//vérification de la vue cliquée
		if(v == monBouton)
		{
			//Toast.makeText(MainActivity.this, "Coucou 2!", Toast.LENGTH_SHORT).show();
			
			//launching new activity
			Intent monIntent = new Intent(this, OtherActivity.class);
			startActivityForResult(monIntent, CODE_RETOUR);
			
		}
		
	}
	
	//manage result from other activity
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		//verify return code
		
		if(requestCode == CODE_RETOUR)
		{
			
			//verify result
			if(resultCode == RESULT_OK)
			{
				//fetch parameter Nom from intent
				String nom = data.getStringExtra("Nom");
				
				//display result
				Toast.makeText(this, "Votre nom est :"+nom, Toast.LENGTH_LONG).show();
				
			}
			else if(resultCode == RESULT_CANCELED) //if activity is cancelled
			{
				//notify that operation has been cancelled
				Toast.makeText(this, "Opération annulé", Toast.LENGTH_LONG).show();
			}
		}
	}

}
