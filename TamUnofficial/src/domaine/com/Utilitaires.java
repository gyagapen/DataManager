package domaine.com;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class Utilitaires{
	
	static String idMobitrans = "";
	
	//initialise la classe utilitaires
	public static void initialiserUtilitaires()
	{
		idMobitrans = "";
	}
	
	public static String getIdMobitrans()
	{
		
		if(idMobitrans.equals(""))
		{
			idMobitrans = getConfigUrl();
		}
		
		return idMobitrans;
	}

	 public static String getConfigUrl()
	 {
		 String url = "";
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


			 HttpClient client = new DefaultHttpClient(httpParameters);

			 //cookiemanager.setCookie("http://tam.mobitrans.fr", "MOBITRANS_ID");

			 String myUserAgent = "Mozilla/5.0 (Linux; U; Android 2.3.4; fr-fr; GT-I9100 Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

			 HttpGet request = new HttpGet("http://tam.mobitrans.fr");
			 request.setHeader("User-Agent", myUserAgent);


			 HttpResponse response = null;
			 try{
				 response = client.execute(request);
			 }
			 catch (Exception ex)
			 {
				 //code erreur
				 return "101";

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
			 url = str.toString();


		 }
		 catch (Exception e)
		 {
			 Log.d("HTML VALUE", "ERREUR");
			 e.printStackTrace();
		 }

		 Document doc = Jsoup.parse(url);
		 Element link = doc.select("a").first();
		 String href = link.attr("href");
		 //Log.d("PARSER URL LINK", href);


		 idMobitrans = href.substring(12, 19);
		 //Log.d("VALLLEEEUUUUR",idMobitrans);

		 return idMobitrans;
	 }
}
