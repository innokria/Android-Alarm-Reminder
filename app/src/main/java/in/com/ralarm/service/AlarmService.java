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
package in.com.ralarm.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import in.com.ralarm.Alarm;
import in.com.ralarm.alert.AlarmAlertBroadcastReciever;
import in.com.ralarm.database.Database;

public class AlarmService extends Service {

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		Log.d(this.getClass().getSimpleName(),"onCreate()");
		super.onCreate();		
	}

	private Alarm getNext(){
		Set<Alarm> alarmQueue = new TreeSet<Alarm>(new Comparator<Alarm>() {
			@Override
			public int compare(Alarm lhs, Alarm rhs) {
				int result = 0;
				long diff = lhs.getAlarmTime().getTimeInMillis() - rhs.getAlarmTime().getTimeInMillis();				
				if(diff>0){
					return 1;
				}else if (diff < 0){
					return -1;
				}
				return result;
			}
		});
			// DB INIT
		Database.init(getApplicationContext());
		List<Alarm> alarms = Database.getAll();//loop through database records
		
		for(Alarm alarm : alarms){
			if(alarm.getAlarmActive()) {//only check the active alarm
				alarmQueue.add(alarm);
				System.out.println("aka===service queuing ==" + alarm.getAlarmName() + "=id=" + alarm.getId());
             	alarm.scheduleAll(getApplicationContext(), alarm.getId());
			}
			else {//cancel all the inactive alarm
				System.out.println("aka===service not active alarm==" + alarm.getAlarmName() + "=id=" + alarm.getId());
				Intent myIntent = new Intent(getApplicationContext(), AlarmAlertBroadcastReciever.class);
				myIntent.putExtra("alarm", new Alarm());
				System.out.println("aka===service started==and canceling alarm ====================== medication="+alarm.getAlarmName());
				PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarm.getId(), myIntent,PendingIntent.FLAG_CANCEL_CURRENT);
				AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
				alarmManager.cancel(pendingIntent);
			}

		}
		if(alarmQueue.iterator().hasNext()){
			return alarmQueue.iterator().next();
		}else{
			return null;
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Database.deactivate();
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		System.out.println("aka===service entry point");
		Log.d(this.getClass().getSimpleName(),"onStartCommand()");
		Alarm alarm = getNext();

		/* THIS IS NOT IN  USE - ONLY USED FOR SINGLE REMINDER HANDLING
		if(null != alarm){

			System.out.println("aka===service started==immidiate next medication="+alarm.getAlarmName());
		    alarm.schedule(getApplicationContext());

			Log.d(this.getClass().getSimpleName(),alarm.getTimeUntilNextAlarmMessage());
			
		}else{
			Intent myIntent = new Intent(getApplicationContext(), AlarmAlertBroadcastReciever.class);
			myIntent.putExtra("alarm", new Alarm());
			System.out.println("aka===service started==and canceling  medication="+alarm.getAlarmName());
         	PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent,PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(pendingIntent);
		}*/
		return START_NOT_STICKY;
	}

}
