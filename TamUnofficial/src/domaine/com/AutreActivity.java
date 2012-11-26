package domaine.com;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



public class AutreActivity extends Activity implements Runnable {

	//boite de dialogue : pour patienter
	private ProgressDialog progressDialog;

	volatile String id;
	volatile String idMobitrans;
	volatile String html;
	volatile Handler handler;
	volatile  Button buttonValider;
	volatile  Button buttonFavoris;
	Activity act;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.layouttest);
		LinearLayout lay = (LinearLayout)findViewById(R.id.layoutest);


		//recuperation des buttons
		buttonValider = (Button)findViewById(R.id.buttonValider);
		buttonFavoris = (Button)findViewById(R.id.buttonFavoris);


		html = "";

		id = null;
		idMobitrans = null;
		act = this;

		//on recupere l'id et le mobitrans id
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id= extras.getString("id");
			idMobitrans = extras.getString("idMobitrans");
		}

		//initialisation de la boite de dialogue
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Chargement en cours");
		
		//Label du bouton favoris
		//on ouvre la bd
		DatabaseHelper bd = new DatabaseHelper(act); 
		boolean estFavoris = bd.isFavoris(id);
		bd.close();

		//si appartien deja aux favoris
		if(estFavoris)
		{
			buttonFavoris.setText("Supprimer Favoris");
			
		}
		else
		{
			buttonFavoris.setText("Ajouter Favoris");
		}


		//affichage de la boite de dialog
		progressDialog.show();


		//on cree une nouveau thread qui va faire la demande id Mobitrans
		//pendant ce temps une boite de dialogue demandant de patienter s'affichera

		Thread t2 = new Thread(this);
		t2.start();


		handler = new Handler() {

			public void handleMessage(android.os.Message msg) {

				if(msg.what == 0) {


					getHoraires(html);


					buttonValider.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub


							//Creation de l'intent
							Intent monIntent = new Intent();    					

							//on retourne le resultat avec comme message OK
							setResult(102, monIntent);

							//on termine l'activite
							finish();




						}
					});


					//boutton favoris en fonction si deja ajoute aux favoris ou pas

					DatabaseHelper bd = new DatabaseHelper(act); 
					boolean estFavoris = bd.isFavoris(id);
					bd.close();
					
					//si appartien deja aux favoris
					if(estFavoris)
					{
						
						buttonFavoris.setOnClickListener(new OnClickListener() {

							public void onClick(View v) {
								// TODO Auto-generated method stub

								DatabaseHelper bd = new DatabaseHelper(act); 
								bd.supprimerFavoris(id);
								bd.close();
								
								//on change le label
								//buttonFavoris.setText("Ajouter Favoris");
								

								//Creation de l'intent
								Intent monIntent = new Intent();    					

								//on retourne le resultat avec comme message OK
								setResult(RESULT_OK, monIntent);

								//on termine l'activite
								finish();  
								


							}
						});
					}
					else
					{
						buttonFavoris.setOnClickListener(new OnClickListener() {

							public void onClick(View v) {
								// TODO Auto-generated method stub

								DatabaseHelper bd = new DatabaseHelper(act); 
								bd.ajouterFavoris(id);
								bd.close();
								
								//on change le label
								//buttonFavoris.setText("Supprimer Favoris");


								//Creation de l'intent
								Intent monIntent = new Intent();    					

								//on retourne le resultat avec comme message OK
								setResult(RESULT_OK, monIntent);

								//on termine l'activite
								finish();   				
							}
						});
					}


				}

			};

		};







	}


	public String getTamHtml(String id, String idMobitrans) 
	{

		String html = "";
		try 
		{

			//TIME OUT
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is established.
			int timeoutConnection = 30000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT) 
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = 30000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			HttpGet request = new HttpGet("http://tam.mobitrans.fr/index.php?p=49&id="+id+"&m=1&I="+idMobitrans+"&rd=6356");

			//on change le user agent
			String myUserAgent = "Mozilla/5.0 (Linux; U; Android 2.3.4; fr-fr; GT-I9100 Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
			request.setHeader("User-Agent", myUserAgent);

			HttpResponse response = null;
			try{
				response = client.execute(request);
			}
			catch (Exception ex)
			{
				//Creation de l'intent
				Intent monIntent = new Intent();


				//on retourne le resultat avec comme message OK
				setResult(101, monIntent);

				//on termine l'activite
				finish();

			}


			InputStream in = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder str = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null)
			{
				str.append(line);
			}
			in.close();
			html = str.toString();


		}
		catch (Exception e)
		{
			Log.d("HTML VALUE", "ERREUR");
			e.printStackTrace();
		}

		Log.d("HTML VALUE", html);



		return html;
	}

	public String getHoraires(String html)
	{


		Document doc = Jsoup.parse(html);
		Element link = doc.getElementsByClass("corpsL").first();
		html = link.html();

		String reg = "#(.*)#";

		//Gestion des noms des arrets
		html = html.replace("<b>", "#");
		html = html.replace("</b>", "#"); 

		//Gestion des horaires
		html = html.replace("Prochain passage", "#Prochain passage "); 
		html = html.replace("min", "min#"); 
		html = html.replace("proche", "proche#"); 

		//Gestion des horaires indispo
		html = html.replace("disponible", "#Horaires indisponibles#"); 
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(html);


		LinearLayout lay = (LinearLayout)findViewById(R.id.layoutest);

		while (m.find()) {
			Log.d("PARSER", (m.group(1)));
			ajoutLabel(m.group(1),  lay);

		}



		return html;
	}


	public void  ajoutLabel(String contenu, LinearLayout lay)
	{
		TextView tv = new TextView(this);
		tv.setText(contenu);

		lay.addView(tv);
	}


	@Override
	public void run() {

		//affiche les horaires de tram, risque de prendre un peu de temps
		//affichage d'une boite de dialogue qui demande de patienter en attendant

		html = getTamHtml(id, idMobitrans);
		handler.sendEmptyMessage(0);
		progressDialog.dismiss();


	}


}
