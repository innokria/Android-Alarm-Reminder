/* Copyright 2014 Sheldon Neilson www.neilson.co.za
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package in.com.ralarm;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import in.com.ralarm.database.Database;
import in.com.ralarm.preferences.AlarmPreferencesActivity;

public class AlarmActivity extends BaseActivity {

	ImageButton newButton;
	ListView mathAlarmListView;
	AlarmListAdapter alarmListAdapter;

	public String loadJSONFromAsset() {
		String json = null;
		try {
			InputStream is = AlarmActivity.this.getAssets().open("med.txt");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alarm_activity);


        //insert testing ******************************************


		try {loadJSONFromAsset();
			Database.init(getApplicationContext());
			//Database.deleteAll();

			JSONArray m_jArry = new JSONArray(loadJSONFromAsset());
		    	for (int i = 0; i < m_jArry.length(); i++) {
				JSONObject jo_inside = m_jArry.getJSONObject(i);
				Log.d("Details-->", jo_inside.getString("Active"));
				String time = jo_inside.getString("Time");
				String dayss = jo_inside.getString("Days");
				String active = jo_inside.getString("Medication");
				String name = jo_inside.getString("Medication");

        		//Alarm.Day[] days = {Alarm.Day.MONDAY, Alarm.Day.TUESDAY, Alarm.Day.WEDNESDAY, Alarm.Day.THURSDAY, Alarm.Day.FRIDAY, Alarm.Day.SATURDAY, Alarm.Day.SUNDAY};
				Alarm.Day[] days = {Alarm.Day.MONDAY, Alarm.Day.TUESDAY, Alarm.Day.WEDNESDAY, Alarm.Day.THURSDAY, Alarm.Day.FRIDAY};
				//Alarm.Day[] daysA ={Alarm.Day.valueOf(dayss)};//one day basis
   			    Alarm a = new Alarm();
					Alarm.Day[] daysA;
					String[] items = dayss.split(",");

					List<Alarm.Day> result = new LinkedList<Alarm.Day>();
					for (String item : items)
					{
						result.add(Alarm.Day.valueOf(item));
            		}

					a.setDays(result.toArray(new Alarm.Day[result.size()]));

					//a.setDays(daysA);
					a.setAlarmActive(true);
					a.setAlarmTime(time);
					a.setDifficulty(Alarm.Difficulty.EASY);
            		a.setAlarmName(name);
					a.setVibrate(true);
					Database.create(a);


			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/*
		//insert test
    	Database.init(getApplicationContext());
		//Alarm.Day[] days = {Alarm.Day.MONDAY, Alarm.Day.TUESDAY, Alarm.Day.WEDNESDAY, Alarm.Day.THURSDAY, Alarm.Day.FRIDAY, Alarm.Day.SATURDAY, Alarm.Day.SUNDAY};
		Alarm.Day[] days = {Alarm.Day.MONDAY, Alarm.Day.TUESDAY, Alarm.Day.WEDNESDAY, Alarm.Day.THURSDAY, Alarm.Day.FRIDAY};
        Alarm a = new Alarm();
		a.setAlarmActive(true);
		a.setAlarmTime("17:08");
		a.setDays(days);
		a.setAlarmName("rahul");
		a.setVibrate(true);
    	Database.create(a);
    	// insert ends ******************************************
         */
		// insert ends ******************************************


		mathAlarmListView = (ListView) findViewById(android.R.id.list);
		mathAlarmListView.setLongClickable(true);
		mathAlarmListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
				final Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
				Builder dialog = new AlertDialog.Builder(AlarmActivity.this);
				dialog.setTitle("Delete");
				dialog.setMessage("Delete this alarm?");
				dialog.setPositiveButton("Ok", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						Database.init(AlarmActivity.this);
						Database.deleteEntry(alarm);
						AlarmActivity.this.callMathAlarmScheduleService();
						
						updateAlarmList();
					}
				});
				dialog.setNegativeButton("Cancel", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				dialog.show();

				return true;
			}
		});

		callMathAlarmScheduleService();

		alarmListAdapter = new AlarmListAdapter(this);
		this.mathAlarmListView.setAdapter(alarmListAdapter);
		mathAlarmListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
				Intent intent = new Intent(AlarmActivity.this, AlarmPreferencesActivity.class);
				intent.putExtra("alarm", alarm);
				startActivity(intent);
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);		
		menu.findItem(R.id.menu_item_save).setVisible(false);
		menu.findItem(R.id.menu_item_delete).setVisible(false);
	    return result;
	}
		
	@Override
	protected void onPause() {
		// setListAdapter(null);
		Database.deactivate();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateAlarmList();
	}
	
	public void updateAlarmList(){
		Database.init(AlarmActivity.this);
		final List<Alarm> alarms = Database.getAll();
		alarmListAdapter.setMathAlarms(alarms);
		
		runOnUiThread(new Runnable() {
			public void run() {
				// reload content			
				AlarmActivity.this.alarmListAdapter.notifyDataSetChanged();				
				if(alarms.size() > 0){
					findViewById(android.R.id.empty).setVisibility(View.INVISIBLE);
				}else{
					findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.checkBox_alarm_active) {
			CheckBox checkBox = (CheckBox) v;
			Alarm alarm = (Alarm) alarmListAdapter.getItem((Integer) checkBox.getTag());
			alarm.setAlarmActive(checkBox.isChecked());
			Database.update(alarm);
			AlarmActivity.this.callMathAlarmScheduleService();
			if (checkBox.isChecked()) {
				Toast.makeText(AlarmActivity.this, alarm.getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();
			}
		}

	}

}