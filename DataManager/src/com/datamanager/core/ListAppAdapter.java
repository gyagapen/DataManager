package com.datamanager.core;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.datamanager.tabActivities.AppLauncher;
import com.gyagapen.cleverconnectivity.R;

public class ListAppAdapter  extends ArrayAdapter<ApplicationDetail> implements ListAdapter, SectionIndexer	{

	private ArrayList<ApplicationDetail> applications = null;
	private  Activity activity = null;
	
	
	private SharedPrefsEditor sharedPrefsEditor;

	private static String sections = "-abcdefghilmnopqrstuvz";

	public ListAppAdapter(ArrayList<ApplicationDetail> someApps, Activity anActivity ,SharedPrefsEditor someSharedPrefsEditor) {

		super(anActivity, 0);
		applications = someApps;
		activity = anActivity;
		sharedPrefsEditor = someSharedPrefsEditor;


	}

	public int getCount() {

		return applications.size();
	}


	public ApplicationDetail getItem(int position) {

		return applications.get(position);
	}


	public long getItemId(int position) {
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent) {

		final LayoutInflater inflater = activity.getLayoutInflater();

		final View listEntry = inflater.inflate(R.layout.application_entry, null);    // initialize the layout from xml

		final TextView contactName = (TextView) listEntry.findViewById(R.id.applcicationName);
		final ImageView contactImage = (ImageView) listEntry.findViewById(R.id.applicationPhoto);
		final ApplicationDetail currentEntry = applications.get(position);
		contactName.setText(currentEntry.getAppname());
		contactImage.setImageDrawable(currentEntry.getIcon());
		
		ApplicationsManager appManager = new ApplicationsManager(activity);
		
		final CheckBox cbox = (CheckBox)listEntry.findViewById(R.id.applicationCbox);
	
		//determine if checkbox has to be checked
		StringToArrayHelper arrayHelper = new StringToArrayHelper(sharedPrefsEditor.getApplicationsOnSet(), AppLauncher.SEPARATOR);
		if(arrayHelper.containsEleement(currentEntry.getPname()))
		{
			cbox.setChecked(true);
		}

		//add oncheck listener
		AppMgrOnCheckListener appMgrOnCheckListener = new AppMgrOnCheckListener(currentEntry.getPname(), getContext());
		cbox.setOnCheckedChangeListener(appMgrOnCheckListener);

		return listEntry;

	}

	@Override
	public void notifyDataSetChanged() {

		//super.sort(new ApplicationNameComparator());
		super.notifyDataSetChanged();
	}

	public int getPositionForSection(int section) {

		for (int i=0; i < this.getCount(); i++) {
			String item = this.getItem(i).getAppname().toLowerCase();
			if (item.charAt(0) == sections.charAt(section))
				return i;
		}
		return 0;
	}


	public int getSectionForPosition(int arg0) {

		return 0;
	}


	public Object[] getSections() {

		String[] sectionsArr = new String[sections.length()];
		for (int i=0; i < sections.length(); i++)
			sectionsArr[i] = "" + sections.charAt(i);

		return sectionsArr;

	}


}
