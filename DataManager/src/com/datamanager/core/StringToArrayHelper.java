package com.datamanager.core;

import java.util.ArrayList;
import java.util.Collections;

public class StringToArrayHelper {

	private ArrayList<String> listOfStrings;
	private String stringToConvert;
	private String separator;
	
	public StringToArrayHelper(String aString, String aSeparator) {
	
		stringToConvert = aString;
		separator = aSeparator;
		
		//building the arraylist
		listOfStrings = new ArrayList<String>();
		String tempArray[] = aString.split(aSeparator);
		
		if(tempArray.length > 0)
		{
			Collections.addAll(listOfStrings, tempArray);
		}
		
	}
	
	
	public boolean containsEleement(String anElement)
	{
		return listOfStrings.contains(anElement);
	}
	
	/*
	 * Add an element to the list if doesn't already exists
	 */
	public void addElementToList(String anElement)
	{
		
		if(!containsEleement(anElement))
		{
			listOfStrings.add(anElement);
		}
	}
	
	
	/**
	 * Remove an element from list if exists
	 * @param anElement
	 */
	public void removeElementFromList(String anElement)
	{
		if(containsEleement(anElement))
		{
			listOfStrings.remove(anElement);
		}
	}
	
	
	public int count()
	{
		return listOfStrings.size();
	}
	
	public String getElement(int position)
	{
		return listOfStrings.get(position);
	}
	
	public String convertListToString()
	{
		String results = "";
		for(int i=0; i<listOfStrings.size(); i++)
		{
			if(!listOfStrings.get(i).trim().equals(""))
			{
				results = results+separator+listOfStrings.get(i);
			}
		}
		
		return results;
	}
}
