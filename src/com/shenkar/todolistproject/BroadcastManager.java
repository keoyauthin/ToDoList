package com.shenkar.todolistproject;
/**
 * This class is in charge of all the alarms
 */
 

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadcastManager extends BroadcastReceiver 
{
	//Variables
	private  NotificationManager 	nm			  ;
	private  PendingIntent 			contentIntent ; 
	private  DBAdapter 				db			  ; 

	@Override
	public void onReceive(Context context, Intent intent) {
		
		String choice = intent .getStringExtra("choice"); 
		//check if choice not null it's time alarm and if it null it's location alarm 
		if(choice != null){
			//get message from intent
			String message  = intent.getStringExtra("message"); 
			String m2 = "        ";
			m2+=message;
			//get code from intent
			String code = intent.getStringExtra("code");
			int requestcode = Integer.parseInt(code);
			//create a Notification 
			nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			CharSequence from = "ToDoList Reminder"; 
			Intent intent2 = new Intent(context, Main.class);
			contentIntent = PendingIntent.getActivity(context,requestcode  , intent2 , PendingIntent.FLAG_ONE_SHOT); 
			Notification notif = new Notification(R.drawable.notification,m2, System.currentTimeMillis()); 
			notif.setLatestEventInfo(context, from, m2, contentIntent);
			notif.flags |= Notification.FLAG_AUTO_CANCEL;
			notif.defaults |= Notification.DEFAULT_SOUND;
			db = new DBAdapter(context);
			//update the database
			db.open();
			db.updateTaskAlarm(requestcode, "0");
			db.close();
			nm.notify(requestcode , notif);
		}
		else{
			// get the tast title
			String tit =  intent.getStringExtra("abc");
			// get the code
			String code = intent.getStringExtra("codeLocation");
			Toast.makeText(context,tit + " Near Location", Toast.LENGTH_LONG).show();
			int requestcode = Integer.parseInt(code);
			Intent intent2 = new Intent(context, Main.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, requestcode  , intent2, 
										PendingIntent.FLAG_ONE_SHOT); 
			//create Notification
			nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notif = new Notification(R.drawable.notification,"",System.currentTimeMillis()); 
			notif.setLatestEventInfo(context,"Proximity Alert!",tit,pendingIntent);
			notif.flags |= Notification.FLAG_AUTO_CANCEL;
			notif.defaults |= Notification.DEFAULT_SOUND;
			int id = requestcode-10;
			//update the database
			db = new DBAdapter(context);
			db.open();
			db.updateTaskLocation(id, "Not Set");
			db.close();
			nm.notify(requestcode, notif);
		}
	}

 

}
