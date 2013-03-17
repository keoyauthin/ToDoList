package com.shenkar.todolistproject;
/**
 * This class Responsible of list view 

 * 
 * The adapter of the list view
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

 

public class ItemListBaseAdapter extends BaseAdapter  
{
	//Variables
	private static ArrayList<ItemDetails> itemDetailsrrayList;
	
	private Context 	   con;
	
	private AlarmManager 	am;
	  
	private LayoutInflater l_Inflater;
	
	private DBAdapter 	   db;
	
	private Calendar 	   myCalendar = Calendar.getInstance();
		
	private Calendar 	   rightnow   = Calendar.getInstance();

	//constructor
	public ItemListBaseAdapter(Context context, ArrayList<ItemDetails> results , DBAdapter _db) 
	{
		 con=context;
		 am = (AlarmManager) con.getSystemService (Context.ALARM_SERVICE);
		 itemDetailsrrayList = results;
		 l_Inflater = LayoutInflater.from(context); 
		 db=_db;
	}
	//get the array list 
	public ArrayList<ItemDetails> getlist()
	{
		return itemDetailsrrayList;
	}
	
	// get the size of array list 
	@Override
	public int getCount() {
	 return itemDetailsrrayList.size();
	}
	//get specific task from array by position
	@Override
	public Object getItem(int position) {
	 return itemDetailsrrayList.get(position);
	}
	//get the item id
	@Override
	public long getItemId(int position) {
	 return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
	  final ViewHolder holder;
	  final int id;
	  DataBaseSingleton databasesingleton = DataBaseSingleton.getInstance();
	  
	 if (convertView == null) 
	 {
		  convertView = l_Inflater.inflate(R.layout.item_select, null);
		  holder = new ViewHolder();
		  holder.iv = (ImageView)convertView.findViewById(R.id.ivLoc);
		  holder.txt_itemName = (TextView) convertView.findViewById(R.id.name);
		  holder.imagebtn = (ImageButton)convertView.findViewById(R.id.imagebtn);
		  holder.imagebtn .setFocusable(false);
		  holder.chkdone = (CheckBox)convertView.findViewById(R.id.chkDone);
		  convertView.setTag(holder);	 
	 } else {
	  holder = (ViewHolder) convertView.getTag();
	 }
      // set title & description of each task 
	 holder.txt_itemName.setText(DataBaseSingleton.getArrayList().get(position).getTitle());
	 

	 id = itemDetailsrrayList.get(position).getId();
	 
	 //if task has alarm
	if(itemDetailsrrayList.get(position).getFlagAlarm()==1)
	{
		//set image clock on
		 holder.imagebtn.setImageResource(R.drawable.clock_on);
		
	} 
	else
	{
		 //set image clock off
		 holder.imagebtn.setImageResource(R.drawable.clock_off);
		
	}
 
	// if task marked as done
	if(itemDetailsrrayList.get(position).getFlagDone()==1)
	{
		//put a line on text
		holder.txt_itemName.setPaintFlags(holder.txt_itemName.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
		//set check box 
		holder.chkdone.setChecked(true);

	}
	else
	{
		// remove the line from text 
		holder.txt_itemName.setPaintFlags(holder.txt_itemName.getPaintFlags()&~Paint.STRIKE_THRU_TEXT_FLAG);
		//set check box
		holder.chkdone.setChecked(false);
	}
	
	
	if(itemDetailsrrayList.get(position).getLocation().equals("Not Set")){
		
		holder.iv.setVisibility(View.INVISIBLE);
		
	}else{
		holder.iv.setVisibility(View.VISIBLE);
		
	}

	// Click Listener of the clock image
	 holder.imagebtn.setOnClickListener (new OnClickListener() {
	
		 @Override
		public void onClick(View v) 
		 {
			if(itemDetailsrrayList.get(position).getFlagAlarm()!=1){
			 final Dialog dialog = new Dialog(con);
			 //set layout
			 dialog.setContentView(R.layout.time_date);
			 // show dialog
			 dialog.show();
			 // new TimePicker
			 TimePicker tp = (TimePicker)dialog.findViewById(R.id.timePicker1);
			 // set listener
			 tp.setOnTimeChangedListener(onTimeChangedListener);
			 // set view to 24 hour
			 tp.setIs24HourView(true);
			 // new DatePicker
			 DatePicker dp = (DatePicker)dialog.findViewById(R.id.datePicker1);
			 // set click listener
			 dp.init(rightnow.get(Calendar.YEAR),rightnow.get( Calendar.MONTH),
					 rightnow.get(Calendar.DAY_OF_MONTH), onDateChangedListener);
			 // get button cancel from layout
			 Button btnCancel = (Button) dialog.findViewById(R.id.btncancel);
			 //set listener
			 btnCancel.setOnClickListener(new OnClickListener() {
				 
				 @Override
				public void onClick(View v) {
					 
					 dialog.dismiss();
					 
				 }
				 
			 });
			 
			 // get button set
			 Button dialogButton = (Button) dialog.findViewById(R.id.btnSet);
			 // set listener
			 dialogButton.setOnClickListener(new OnClickListener() {
				 
			 @Override
			public void onClick(View v) {
				 //check the time
				 if( testTime() == 1 )
				 {
					 // set the image
					 holder.imagebtn.setImageResource(R.drawable.clock_on);
					 //update the alarm flag
					 itemDetailsrrayList.get(position).setFlagAlarm(1);					 
					 String date = null;
					 String time =null;
					 SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy");
					 date = sdfDate.format(myCalendar.getTime());
					 SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
					 time = sdfTime.format(myCalendar.getTime());
					 //update the date and tim
					 itemDetailsrrayList.get(position).setDate(date);
					 itemDetailsrrayList.get(position).setTime(time);
					 //update database
					  db.open();
					  db.updateTaskTime(id, time);
					  db.updateTaskDate(id, date);
					  db.updateTaskAlarm(id, "1");
					  db.close();
					  // call setAlarm
					  setAlarm(position);
					  myCalendar = Calendar.getInstance();
					  rightnow   = Calendar.getInstance();
					  //close dialog
					  dialog.dismiss();
				 }
				 else{
					 //time Invaild
					 Toast.makeText(con, "Time Invaild", Toast.LENGTH_SHORT).show();
				 }
			}
			// set time alarm
			private void setAlarm(int position) {
				
				  //new intent
				  Intent intent = new Intent(con, BroadcastManager.class);
				  //put code in intent 
				  intent.putExtra("code", Integer.toString(itemDetailsrrayList.get(position).getId()));
				  // put title in intent
				  intent.putExtra("message", itemDetailsrrayList.get(position).getTitle());
				  // put the "Time" Flag
				  intent .putExtra("choice","Time");
				  //new pendingIntent
				  PendingIntent pendingIntent = PendingIntent.getBroadcast(con, itemDetailsrrayList.get(position).getId(),
						  intent, 0);
				  // set the alarm
				  am.set(AlarmManager.RTC_WAKEUP , myCalendar.getTimeInMillis(), pendingIntent);
				  
				  Toast.makeText(con, "Alarm Is Set", Toast.LENGTH_SHORT).show();
				 
			}});
			}
			else{
				//set the image
				holder.imagebtn.setImageResource(R.drawable.clock_off);
				//set flag alarm
				itemDetailsrrayList.get(position).setFlagAlarm(0);
				//set date
				itemDetailsrrayList.get(position).setDate("Not Set");
				//set time
				itemDetailsrrayList.get(position).setTime("");
				//update database
				db.open();
				db.updateTaskAlarm(id, "0");
				db.updateTaskDate(id, "");
				db.updateTaskTime(id, "");
				db.close();
				//new intent 
				Intent intent = new Intent(con, BroadcastManager.class);
				// cancel the alarm
	        	PendingIntent.getBroadcast(con,itemDetailsrrayList.get(position).getId() ,
	        			                intent,PendingIntent.FLAG_UPDATE_CURRENT).cancel();
	        	
	        	 Toast.makeText(con, "Alarm Abort", Toast.LENGTH_SHORT).show();
			}
		 }
		 });
	 
	 // check box click listener
	 //puts a line in middle of the text
	 holder.chkdone.setOnClickListener(new OnClickListener() {
		 @Override
		public void onClick(View v) 
	        { 
			 	//if the check box is checked
			 	if(holder.chkdone.isChecked()){
			 		//put line
			 		holder.txt_itemName.setPaintFlags
			 		(holder.txt_itemName.getPaintFlags()
			 				|Paint.STRIKE_THRU_TEXT_FLAG)			;
			 		//update database
			 		db.open();
			 		db.updateTaskDone(id,"1")						;
			 		db.close()										;
			 		//update the array list
			 		itemDetailsrrayList.get(position).setFlagDone(1);
			 	}else{
			 		//remove the line
			 		holder.txt_itemName.setPaintFlags
			 		(holder.txt_itemName.getPaintFlags()
			 				&~Paint.STRIKE_THRU_TEXT_FLAG)			;
			 		
			 		//update database 
			 		db.open()										;
			 		db.updateTaskDone(id,"0")						;
			 		db.close()										;
			 		//update the array list
			 		itemDetailsrrayList.get(position).setFlagDone(0);
			 	}
	        }
	 });
		 return convertView;
		}
	
	
	public	class ViewHolder 
		{
		 ImageView   iv					;
		 TextView 	 txt_itemName		;
		 CheckBox 	 chkdone			;
		 ImageButton imagebtn			;
		}
	// check the time selected by the user
	public int testTime(){
		
		 if(myCalendar.after(rightnow))
		 {
			 
			 return 1		;
		 }
		 	 return 0		;
	}
	
    OnTimeChangedListener onTimeChangedListener
    = new OnTimeChangedListener(){

    	//get time of the user choice
  @Override
  public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
    
		myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)  ;
		myCalendar.set(Calendar.MINUTE, minute)			 ;
		myCalendar.set(Calendar.SECOND,0)				 ;
  }};
  
  
  OnDateChangedListener onDateChangedListener
  = new OnDateChangedListener(){
	  
	  //get date of the user choice
  @Override  
  public void onDateChanged(DatePicker view, int year, int month, int day) {
      
	  myCalendar.set(Calendar.YEAR, year)		;
	  myCalendar.set(Calendar.MONTH, month)		;
	  myCalendar.set(Calendar.DAY_OF_MONTH, day);
	  
  }};
}