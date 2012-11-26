package domaine.com;

 
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
 
 
public class DatabaseHelper extends SQLiteOpenHelper {
 
    /**
     * Intialisation des variables principales
     */
 
    private static String DB_PATH = "/data/data/domaine.com/databases/";
    private static String DB_NAME = "BDTam.sqlite";
 
    private final Context myContext;
 
 
 
    /**
     * Num&#65533;ro de version de la DB.
     * Si ce num&#65533;ro change, la fonction onUpgrade est ex&#65533;cut&#65533;e
     * pour effacer le contenu de la DB et recr&#65533;er la nouvelle
     * version du sch&#65533;ma.
     */
    private static final int DATABASE_VERSION = 14;
 
    /**
     * Lien vers la session DB.
     */
    private SQLiteDatabase db;
 
    /**
     * Nom de la table de la DB (oui une seule table).
     */
    private static String TABLE_SERIES = "series";
 
 
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        //db = getWritableDatabase();
        this.myContext = context;
        try {
        createDataBase();
        //System.out.println("ok bd");
 
        } catch (IOException e) {
        // TODO Auto-generated catch block
 
        e.printStackTrace();
 
        //System.out.println("erreur bd");
        }
 
    }
 
    /**
     * Ex&#65533;cut&#65533; si la DB n'existe pas.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL("CREATE TABLE ligne(num_ligne TEXT, _id INTEGER PRIMARY KEY)");
        //Toast.makeText(myContext, "La base est cr&#65533;&#65533;", Toast.LENGTH_SHORT).show();
 
    }
 
 
    /**
     * Ex&#65533;cut&#65533; chaque fois que le num&#65533;ro de version de DB change.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	//db.execSQL("DROP TABLE ligne" );
        onCreate(db);
        try {
          copyDataBase();
            Log.i("TABBB", "nach copydatabase");
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
        
        //onCreate(db);
    }
 
 
 
 
 
 
     /**
     * Charge le contenu de la table Livres
     * et retourne ce contenu sous la forme d'une liste de NOM + " " + PRENOM;
     */
    public ArrayList<HashMap<String, String>> getSeries() {
 
 
 
        //Cr&#65533;ation de la ArrayList qui nous permettra de remplire la listView
        ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();
 
        //On d&#65533;clare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;
 
 
        String[] colonnesARecup = new String[] { "_id", "title", "category", "resume" };
        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursorResults = db.query(TABLE_SERIES, colonnesARecup, null,
                null, null, null, "title asc, category asc", null);
        if (null != cursorResults) {
            if (cursorResults.moveToFirst()) {
                do {
                	map = new HashMap<String, String>();
                	map.put("_id", cursorResults.getString(cursorResults.getColumnIndex("_id")));
                	map.put("titre", cursorResults.getString(cursorResults.getColumnIndex("title")));
                	map.put("category", cursorResults.getString(cursorResults.getColumnIndex("category")));
                	map.put("resume", cursorResults.getString(cursorResults.getColumnIndex("resume")));
 
                    output.add(map);
 
                } while (cursorResults.moveToNext());
            } // end&#65533;if
        }
        cursorResults.close();
        db.close();
        return output;
    }
 
 
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE); 
    	}catch(SQLiteException e){ 
    		//database does't exist yet.
    		Log.i("BDBUZZ","Erreur Bd"+e);	
    	} 
    	if(checkDB != null){ 
    		checkDB.close(); 
    	} 
    	return checkDB != null ? true : false;
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
 
    }
 
 
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    public void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        //dbExist = false;
 
        Log.i("BDBUZZ","Checkdatabase "+dbExist);
 
        String DEBUG_TAG = "BDBUZZ";
		if (dbExist) {
            Log.i(DEBUG_TAG , "createDataBase -> La base existe");

            
        
            // do nothing - database already exist
        } else {
            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
        	Log.i("BDBUZZ","Avant getreadable");
 
            this.getReadableDatabase();
            Log.i("BDBUZZ","Apres getreadable");
            Log.i(DEBUG_TAG, "else, db existiert nicht 1");
            try {
                copyDataBase();
                Log.i(DEBUG_TAG, "nach copydatabase");
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }
 
 
	public long insertLigne(int numLigne)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put("num_ligne", Integer.toString(numLigne));
		return this.getWritableDatabase().insert("ligne", null, initialValues);
	}
 
 
	   @Override
		public synchronized void close() {
	 
	    	    if(db != null)
	    		    db.close();
	 
	    	    super.close();
	 
		}
	   
	   
	   public Ligne[] getAllLigne(){
			//Récupère dans un Cursor les valeurs correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)

			Cursor c = this.getWritableDatabase().query("ligne", null, null, null, null, null, null, null);
			return convertCursorToLignes(c);
		}
	   
	   
		private Ligne[] convertCursorToLignes(Cursor c){
			//si aucun élément n'a été retourné dans la requête, on renvoie null
			if (c.getCount() == 0)
				return null;
	 
			Ligne tabRes[] = new Ligne[c.getCount()];
			
			
			//Sinon on se place sur le premier élément
			c.moveToFirst();
			tabRes[0] = new Ligne(c.getString(0));
			
			if(!c.isLast())
			{
				c.moveToNext();

				int i = 1;

				while (!c.isLast())
				{
					tabRes[i] = new Ligne(c.getString(0));
					i++;
					c.moveToNext();
				}

				tabRes[i] = new Ligne(c.getString(0));
			}
			//On ferme le cursor
			c.close();
	 
			//On retourne le livre
			return tabRes;
		}
		
		
		   public Arret[] getArretFromLigne(String num_ligne){
				//Récupère dans un Cursor les valeurs correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)

				Cursor c = this.getWritableDatabase().query("arret", new String[] { "id_arret", "_id", "nom_arret", "num_ligne", "favoris"}, "num_ligne LIKE \"" + num_ligne +"\"", null, null, null, null, null);
				return convertCursorToArrets(c);
			}
		
		   
			private Arret[] convertCursorToArrets(Cursor c){
				//si aucun élément n'a été retourné dans la requête, on renvoie null
				if (c.getCount() == 0)
					return null;
		 
				Arret tabRes[] = new Arret[c.getCount()];
				
				
				//Sinon on se place sur le premier élément
				c.moveToFirst();
				tabRes[0] = new Arret(c.getString(3),c.getString(0),c.getString(2));
				
				if(!c.isLast())
				{
					c.moveToNext();

					int i = 1;

					while (!c.isLast())
					{
						tabRes[i] = new Arret(c.getString(3),c.getString(0),c.getString(2));
						i++;
						c.moveToNext();
					}

					tabRes[i] = new Arret(c.getString(3),c.getString(0),c.getString(2));
				}
				//On ferme le cursor
				c.close();
		 
				//On retourne le livre
				return tabRes;
			}
			

			   public boolean isFavoris(String num_arret){
					//Récupère dans un Cursor les valeurs correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)

					Cursor c = this.getWritableDatabase().query("arret", new String[] { "favoris","id_arret"}, "id_arret LIKE \"" + num_arret +"\"", null, null, null, null, null);
					
					c.moveToFirst();
					
					boolean res = false;
					
					if (c.getInt(0)==0)
					{
						res = true;
					}
					
					return res;
				}
			   
			   
			   
			   
				public int updateFavoris(String num_arret, int favoris){
					
					ContentValues values = new ContentValues();
					values.put("favoris", favoris);
					return this.getWritableDatabase().update("arret", values, "id_arret LIKE \"" + num_arret +"\"", null);
				}
				
				public void ajouterFavoris(String num_arret)
				{
					this.updateFavoris(num_arret, 0);
				}
				
				public void supprimerFavoris(String num_arret)
				{
					this.updateFavoris(num_arret, 1);
				}
				
				
				//modifie l'etat de favoris d'un arret
				//si arret est deja favoris, on suprrime le favoris
				//sinon on l'ajoute
				public void modifierFavoris(String num_arret)
				{
					if(isFavoris(num_arret))
					{
						//suppression
						supprimerFavoris(num_arret);
					}
					else
					{
						ajouterFavoris(num_arret);
					}
				}
				
				//retourne les arrets marques comme favoris
				public Arret[] getArretFavoris(){

					Cursor c = this.getWritableDatabase().query("arret", new String[] { "id_arret", "_id", "nom_arret", "num_ligne", "favoris"}, "favoris LIKE '0'", null, null, null, null, null);
					return convertCursorToArrets(c);
				}
			
			   
				
				
 
 
 
}