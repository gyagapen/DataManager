package com.example.intenttest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class OtherActivity extends Activity implements OnClickListener{

	private Button boutonValider = null;
	private Button callButton = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layoutcoucou);
		
		//fetching button
		boutonValider = (Button)findViewById(R.id.ButtonValider);
		
		//fetchingCallButton
		callButton = (Button)findViewById(R.id.Appeler);

		boutonValider.setOnClickListener(this);
		callButton.setOnClickListener(this);
		
		}

	//while clicking boutonValider
	public void onClick(View v) {
		
		
		if(v == boutonValider)
		{
			//fetching input field
			final EditText editTextNom = (EditText)findViewById(R.id.editTextNom);
			
			//verify input length
			if(editTextNom.length() > 0)
			{
				//create the intent
				Intent intent = new Intent();
				
				//fetch input
				String inputValue = editTextNom.getText().toString();
				
				//add input to intent
				intent.putExtra("Nom", inputValue);
				
				//return result with intent
				setResult(RESULT_OK, intent);
				
				//terminate activity
				finish();
			}

		}
		else if(v == callButton)
		{
			//create new intent for calling
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:9807708"));
			startActivity(callIntent);
		}
		
		
	}
}
