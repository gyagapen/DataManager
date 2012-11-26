package com.example.databasetest;

import java.util.List;

import android.database.Cursor;

public interface IRepository<T> {
	
	public List getAll();
	public T GetById(int id);
	
	public void save(T entite);
	public void update(T entite);
	public void Delete(int id);
	
	public List ConvertCursorToListObject(Cursor c);
	public T ConvertCursorToObject(Cursor c);
	public T ConvertCursorToOneObject(Cursor c);

}
