package com.example.databasetest;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class Repository<T> implements IRepository<T> {
	
	//base de donn�es
	protected SQLiteDatabase maBDD;
	
	protected SQLiteOpenHelper sqLiteOpenHelper;
	
	/**
	 * Constructeur par d�faut
	 */
	public Repository() {
	
	}
	
	/**
	 * Ouverture de la connexion
	 */
	public void Open()
	{
		maBDD = sqLiteOpenHelper.getWritableDatabase();
	}
	
	/**
	 * Fermeture de la connexion
	 */
	public void close()
	{
		maBDD.close();
	}

}
