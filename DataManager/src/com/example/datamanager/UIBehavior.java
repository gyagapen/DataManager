package com.example.datamanager;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

/**
 * Define behavior of all UI components
 * 
 * @author Gui
 * 
 */
public class UIBehavior implements OnClickListener {

	public void onClick(View v) {

		// Checkbox to enable/disable data connection
		if (v.getId() == R.id.checkBoxData) {
			CheckBox cb = (CheckBox) v;

			// data is enabled
			if (cb.isChecked()) {

			} else {
				// data disabled
			}
		} else if (v.getId() == R.id.checkBoxDataMgr) // checkbox to control
														// Data manager service
		{
			CheckBox cb = (CheckBox) v;

			// data manager service is enabled
			if (cb.isChecked()) {

			} else {
				// data manager service is disabled
			}
		}
		else if (v.getId() == R.id.editTextTimeOn) //
		{
			
		}
		

	}

}
