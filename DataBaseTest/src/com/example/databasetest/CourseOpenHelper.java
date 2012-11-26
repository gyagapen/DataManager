package com.example.databasetest;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CourseOpenHelper extends SQLiteOpenHelper {



	public CourseOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	// version de la base de donn�es
	private static final int DATABASE_VERSION = 1;

	// Nom de la bd
	private static final String COURSE_BASE_NAME = "course.db";

	// Nom de la table
	public static final String COURSE_TABLE_NAME = "Course";

	// Description des colonnes
	public static final String COLUMN_ID = "ID";
	public static final int NUM_COLUMN_ID = 0;
	public static final String COLUMN_PRODUIT = "PRODUIT";
	public static final int NUM_COLUMN_PRODUIT = 1;
	public static final String COLUMN_QUANTITE = "QUANTITE";
	public static final int NUM_COLUMN_QUANTITE = 2;
	public static final String COLUMN_ACHETE = "ACHETE";
	public static final int NUM_COLUMN_ACHETE = 3;

	// Requ�te SQL pour la cr�ation de la base
	private static final String REQUETE_CREATION_BDD = "CREATE TABLE "
			+ COURSE_TABLE_NAME + " (" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRODUIT
			+ " TEXT NOT NULL, " + COLUMN_QUANTITE + " INTEGER NOT NULL. "
			+ COLUMN_ACHETE + " INTEGER NOT NULL);";

	/**
	 * Cr�ation de la bd
	 */
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(REQUETE_CREATION_BDD);
	}

	/**
	 * Maj de la bd
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Lorsque l'on change le num�ro de version de la bd, on supprime la table puis on la recr�e
		if(newVersion > DATABASE_VERSION)
		{
			db.execSQL("DROP TABLE "+COURSE_TABLE_NAME+" ;");
			onCreate(db);
		}

	}

}
