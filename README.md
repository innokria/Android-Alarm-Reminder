
# AndroidAlarmReminder
Create Reminder From JSON list or Create your own TODO Reminders , inspired from https://github.com/SheldonNeilson/Android-Alarm-Clock

JSON SAMPLE 
__________________________________________________________
[
{
 "Active":"true",
 "Time":"09:42",
 "Days":"MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY",
 "Medication":"benzol"

},
{
 "Active":"true",
 "Time":"09:43",
 "Days":"WEDNESDAY,THURSDAY",
 "Medication":"cough"

},
{
 "Active":"true",
 "Time":"09:44",
 "Days":"THURSDAY",
 "Medication":"panadol"
 
}

]


___________________________________________________

AlarmActivity.java 

Loads JSON And Renders predefined Reminders




  try {        loadJSONFromAsset();
			   Database.init(getApplicationContext());
		
			    JSONArray m_jArry = new JSONArray(loadJSONFromAsset());
		    	for (int i = 0; i < m_jArry.length(); i++) {
				JSONObject jo_inside = m_jArry.getJSONObject(i);
				Log.d("Details-->", jo_inside.getString("Active"));
				String time = jo_inside.getString("Time");
				String dayss = jo_inside.getString("Days");
				String active = jo_inside.getString("Medication");
				String name = jo_inside.getString("Medication");
            	Alarm.Day[] days = {Alarm.Day.MONDAY, Alarm.Day.TUESDAY, Alarm.Day.WEDNESDAY, Alarm.Day.THURSDAY, Alarm.Day.FRIDAY};
			    Alarm a = new Alarm();
					Alarm.Day[] daysA;
					String[] items = dayss.split(",");

					List<Alarm.Day> result = new LinkedList<Alarm.Day>();
					for (String item : items)
					{
						result.add(Alarm.Day.valueOf(item));
            		}

					a.setDays(result.toArray(new Alarm.Day[result.size()]));
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
