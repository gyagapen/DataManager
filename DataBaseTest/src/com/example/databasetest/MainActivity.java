package com.example.databasetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity  implements OnClickListener{

	Button addButton = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //récupération du bouton ajout nouveau produit et ajout comportement
        addButton = (Button)findViewById(R.id.AddButton);
        addButton.setOnClickListener(this);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	public void onClick(View v) {
		
		//si bouton ajout nouveau produit
		if(v == addButton)
		{
			//ouverture de l'acitivité ajout nouveau produit
			Intent addIntent = new Intent(this, AddActivity.class);
			startActivity(addIntent);
		}
		
	}
}
