package com.shenkar.todolistproject;
/**
 * Tamir Yakar & Avi Mesilati 
 * 
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

 
import com.google.analytics.tracking.android.EasyTracker;

// The Main Function 
public class Main extends ListActivity implements   OnClickListener,OnItemLongClickListener,OnItemClickListener, OnGestureListener
{

	//Variables
	 private ItemListBaseAdapter adapter		   ;
	 private Button 			 Addbtn			   ;
	 private  DBAdapter 		 db 			   ;
	 private Cursor 			 cursor			   ;
	 private String 			 title			   ;
	 private String 		     description	   ;
	 private String 			 location		   ;
	 private int 				 code			   ;
	 private LocationManager 	 lm				   ;
	 
 			
	 
	  @Override
	public void onStart() 
	  {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);  
	  }

	  
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		InitVaribles();	// init varibles
		GetTasks();		// get tasks from database
	}
	// init Varibles
	private void InitVaribles() 
	{
		  // set the add button
	     Addbtn  = (Button) findViewById(R.id.btnAdd);
	      //add listener  
	     Addbtn.setOnClickListener(this);
	      // data base object 
	     db = new DBAdapter(this);
	      // array list object (A singleton class)  
	   //  databasesingleton = DataBaseSingleton.getInstance();		
	      // adapter for the list view   
	     adapter = new ItemListBaseAdapter(this, DataBaseSingleton.getArrayList(),db );
	      // location manager object 
	     lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
	      // set the adapter
	     setListAdapter(adapter);
	     
/*	      // add to list view long click listener 
	     getListView().setOnItemLongClickListener(this);
	     
	      // add to list view on item click listener
	     getListView().setOnItemClickListener(this); */
	     final GestureDetector detector = new GestureDetector(this, this);
	     getListView().setOnTouchListener(new OnTouchListener() {
	    	    public boolean onTouch(View view, MotionEvent e) {
	    	        detector.onTouchEvent(e);
	    	        return false;
	    	    }
	    	});
 
	}
	
	
 
	
	
	
	
	// Update The List View 
	private void GetTasks() 
	{
		DataBaseSingleton.getArrayList().clear();

        db.open();
        // get all the task from the data base
        cursor =  db.getAllTask();
        
        // set all tasks into the list view    
        if(cursor.moveToFirst())
        {	
        	do
        	{
        		ItemDetails item = new ItemDetails();
        		item.setId(Integer.parseInt(cursor.getString(0)));
        		item.setTitle(cursor.getString(1));
        		item.setDate(cursor.getString(2));
        		item.setTime(cursor.getString(3));
        		item.setDescription(cursor.getString(4));
        		item.setFlagDone(Integer.parseInt(cursor.getString(5)));
        		item.setLocation(cursor.getString(6));
        		item.setFlagAlarm(Integer.parseInt(cursor.getString(7)));
        		DataBaseSingleton.setArray(item);
        	}while(cursor.moveToNext());	
        	db.close();
        }
        cursor.close();
}
	// Add Button Click Listener
	@Override
	public void onClick(View v) 
	{
		showDialog(0);
 
	}

	//Search Object inside the list view
	private int SearchObject(int id){
		
		
		for(int i=0;i<DataBaseSingleton.getArrayList().size();i++)
		{
			
			if(DataBaseSingleton.getArrayList().get(i).getId() == id){
				
			return i;
			}
		}
			return 0;	
	}
	
	@Override
    protected Dialog onCreateDialog(int id)
	{
	  // Add Task Dialog 
	  //########################################################################	
	  if(id==0){
       LayoutInflater factory = LayoutInflater.from(this);
       final View textEntryView = factory.inflate(R.layout.add, null);
       return new AlertDialog.Builder(Main.this)
       .setTitle("Add Item")
       .setView(textEntryView)
       .setPositiveButton("Set", new DialogInterface.OnClickListener() 
       {
          @Override
		public void onClick(DialogInterface dialog, int whichButton) {
 	
           EditText etTitle = (EditText)textEntryView.findViewById(R.id.etTitle);
           EditText etDescription = (EditText)textEntryView.findViewById(R.id.etDescrip);    
           //get the title from edit text 
           title = etTitle.getText().toString();
           // get the description from edit text 
           description=etDescription.getText().toString();
           //check if user input text to title
           if(title.length() >0){
        	   // go to AddTask function 
        	   AddTask(title, description);
           }
           etTitle.setText("");
           etDescription.setText("");
            
          }
              })
             .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              @Override
			public void onClick(DialogInterface dialog, int whichButton) {
            	  //close dialog
            	  dialog.dismiss();
              }
              })
            .create();
		}
	    //#################################################################################
	  
		// Edit Task Dialog
	    //####################################################################################
		if(id==1){ 
			LayoutInflater factory = LayoutInflater.from(this);
            final View textEntryView = factory.inflate(R.layout.edititemmenu, null);
            EditText etTitle = (EditText)textEntryView.findViewById(R.id.etTitle);
        	EditText etDescription = (EditText)textEntryView.findViewById(R.id.etDescription);
        	int position = SearchObject(code);
        	
        	etTitle.setText(DataBaseSingleton.getArrayList().get(position).getTitle());
        	if(DataBaseSingleton.getArrayList().get(position).getDescription().equals("Not Set")){
        		
        	}else{
        		etDescription.setText(DataBaseSingleton.getArrayList().get(position).getDescription());
        	}
 
            return new AlertDialog.Builder(Main.this)
                .setTitle("Set Item")
                .setView(textEntryView)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
					public void onClick(DialogInterface dialog, int whichButton) {
                    	int position = SearchObject(code);
                        EditText etTitle = (EditText)textEntryView.findViewById(R.id.etTitle);
                    	EditText etDescription = (EditText)textEntryView.findViewById(R.id.etDescription);
                    	//get the title
                    	title = etTitle.getText().toString();
                    	//get the description
                    	description = etDescription.getText().toString();
                        //update database
						db.open();
	        			db.updateTask(code, title, description);
	        			db.close();
	        			//update the Array List 
	        			DataBaseSingleton.getArrayList().get(position).setTitle(title);
	        			DataBaseSingleton.getArrayList().get(position).setDescription(description);
	        			adapter.notifyDataSetChanged();
                    }
                })
					
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
					public void onClick(DialogInterface dialog, int whichButton) {
                      //close dialog		
                      dialog.dismiss();
                    }
                })
                	.create();
			
               }
		//###################################################################################
		
		// Add Location To Task 
		//#######################################################################################
 			if(id==2){
				LayoutInflater factory = LayoutInflater.from(this);
	            final View textEntryView = factory.inflate(R.layout.addlocation, null);
	            return new AlertDialog.Builder(Main.this)
	            .setTitle("Add Location")
                .setView(textEntryView)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
					public void onClick(DialogInterface dialog, int whichButton) {
                    	
                    	EditText etLocation = (EditText)textEntryView.findViewById(R.id.etLoc);
                    	// get the location that user entered
                    	location = etLocation .getText().toString();
                    	etLocation.setText("");
                     
                    	 
                    	List<Address> addressList=null; 
                    	Geocoder geoCoder = new Geocoder(Main.this, Locale.getDefault());
                    	try {
                    		// get a list of address
							addressList  = geoCoder.getFromLocationName(location,5);
							
						} catch (IOException e) {
							 Toast.makeText(Main.this,"Location not found!!! " +
							 		"Please change address and try again.",Toast.LENGTH_SHORT).show();
							e.printStackTrace();
							return;
						}
                    	// if the addressList is empty 
                    	 if(addressList.isEmpty())
                         {
                             AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Main.this);  
                             dlgAlert.setMessage("Location not found!!! Please change address and try again."); 
                             dlgAlert.setTitle("Location Error"); 
                             dlgAlert.setPositiveButton("OK", null); 
                             dlgAlert.setCancelable(true); 
                             dlgAlert.create().show();
                             return;
                         }
                    	 else{
                    		 //go to chooseAddress function
                    		 chooseAddress(addressList);
 
                    	 }
                    	dialog.dismiss();
         
                    	
                    }})
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
					public void onClick(DialogInterface dialog, int whichButton) {

                      dialog.dismiss();
                    }
                })
                .create();
			}//##########################################################################################
		
		return null;
	}
	// Choose Address func 
	public void chooseAddress( final List<Address> la){
		
		
		List<String> listItems = new ArrayList<String>();
		
	    for (int j=0; j<la.size(); j++)
	    {	
		  Address returnedAddress = la.get(j);
		  StringBuilder s = new StringBuilder();
		  s.append(returnedAddress.getAddressLine(1)).append("   ").
		  append(returnedAddress.getAddressLine(0));   
		  listItems.add(s.toString());
		 }
		  //print to screen all the list of address
		  CharSequence[] Items = listItems.toArray(new CharSequence[listItems.size()]);
		  AlertDialog IntervalAlarmChoice=new AlertDialog.Builder(this).setItems
				  
		  (Items, new DialogInterface.OnClickListener()
		  {
		  @Override
		public void onClick(DialogInterface dialog, int which) {
			
			  int c = which--;
			  // get user choice of address and send it to setAlaramLocation function
			  setAlaramLocation(la.get(c));
			  //close dialog
			  dialog.dismiss();
		  }
		  })
	      .create();
		  IntervalAlarmChoice.show();   
		 }
	//Set Alaram Location func
	public void setAlaramLocation(Address myaddress){ 
		
		int requestcode = code+10;
		int position = SearchObject(code);
		//update database 
		db.open();
		db.updateTaskLocation(DataBaseSingleton.getArrayList().get(position).getId() ,
				myaddress.getAddressLine(0)+"\n"+myaddress.getAddressLine(1));
		db.close();
		DataBaseSingleton.getArrayList().get(position).setLocation(myaddress.getAddressLine(1)+"\n"+
								myaddress.getAddressLine(0));
		// update the view
		adapter.notifyDataSetChanged();
		// new intent
		Intent intent = new Intent(Main.this,BroadcastManager.class);
		// get title
		 String ti =DataBaseSingleton.getArrayList().get(position).getTitle();
		 //put title
		 intent.putExtra("abc",ti);
		 //put requestcode 
		 intent.putExtra("codeLocation", Integer.toString(requestcode));
		 
		 PendingIntent pi = PendingIntent.getBroadcast(Main.this, 
					  		requestcode , intent ,PendingIntent.FLAG_ONE_SHOT);
		 //add alarm
	     lm.addProximityAlert(myaddress.getLatitude(), myaddress.getLongitude(),1000, -1, pi);
	     Toast.makeText(Main.this, "Location Alram Is Set", Toast.LENGTH_LONG).show();
	}
	
	// Add Task 
	private void AddTask(String ti, String de  ) 
	{
		//set the new item
		 ItemDetails it = new ItemDetails();
		 it.setTitle(ti);
		 it.setDescription(de);
		 it.setFlagDone(0);
		 it.setLocation("");
		 //update the database
		 db.open();
		 Cursor cursor = db.getAllTask();
		
		 //get id for new task
	     if( cursor.moveToLast() )
	     {
	    	code = Integer.parseInt(cursor.getString(0));
	    	code ++;
	      }
	     else{code =0;}
	     
	    	cursor.close();
	    	db.close();
	    	//update database
	    	db.open();
	    	it.setId(code);
	    	db.insertTask(ti, Integer.toString(code), "Not Set", "Not Set", de, "Not Set");
	    	db.close();
	    	//update Array List
	    	DataBaseSingleton.setArray(it);
			Toast.makeText(getApplicationContext(),"Task Added" , Toast.LENGTH_SHORT).show();
			//update the adapter 
			adapter.notifyDataSetChanged();
	}
	
	//Options Alert Dialog Box	
	public AlertDialog OptionsDialogBox(final ItemDetails it, View arg1) {
		final CharSequence[] options = {"Add Location","Edit" , "Share Task" ,"Navigate", "Delete"  };
		AlertDialog IntervalAlarmChoice=new AlertDialog.Builder(this).setItems(options, new DialogInterface.OnClickListener()
		{
		  @Override
		public void onClick(DialogInterface dialog, int which) {
			  
			  // add task
			  if(options[which]=="Add Location")
			  {
				  code = it.getId();
				  showDialog(2);
				  //close dialog
				  dialog.dismiss();  
	          }
			  // edit task 
			  if(options[which]=="Edit")
	        	{
				  code = it.getId();
				  AlertDialog di = (AlertDialog) onCreateDialog(1);
				  di.show();
				  //close dialog
				  dialog.dismiss();
	        	}
			  //share task
			  if(options[which]=="Share Task")
	        	{
				  //new intent
				  Intent sendIntent = new Intent();
				  //get title and Description
				  String mesg = "Title:"+it.getTitle()+"\nDescription:"+it.getDescription();
				  sendIntent.setAction(Intent.ACTION_SEND);
				  sendIntent.putExtra(Intent.EXTRA_TEXT, mesg);
				  sendIntent.setType("text/plain");
				  //start Activity
				  startActivity(sendIntent);
				  //close dialog
				  dialog.dismiss();
	        	} 
			  
			  // delete task
			  if(options[which]=="Delete")
	        	{
				  	//update the database
					db.open();
		        	db.deleteTask(it);
		        	db.close();
		        	//update the Array List
		        	DataBaseSingleton.getArrayList().remove(it);
		        	// update the adapter 
		        	adapter.notifyDataSetChanged();
		        	//new intent
		        	Intent intent = new Intent(Main.this, BroadcastManager.class);
		        	// cancel time alarm 
	        		PendingIntent.getBroadcast(Main.this, it.getId(), intent,
	        									PendingIntent.FLAG_UPDATE_CURRENT).cancel();
	        		//cancel location alarm
	       		    PendingIntent pi = PendingIntent.getBroadcast(Main.this, 
	       				(it.getId()+10)  , intent ,PendingIntent.FLAG_ONE_SHOT);
	        		lm.removeProximityAlert(pi );
	        		Toast.makeText(getApplicationContext(),"Task Removed", Toast.LENGTH_SHORT).show();
	        	}
			  
			  // Navigate To Location (if the user entered location)
			  if(options[which]=="Navigate")
	        	{
				  // check if there is location 
				  if(it.getLocation() != "Not Set"){
					  
					List<Address> addressList=null; 
                  	Geocoder geoCoder = new Geocoder(Main.this, Locale.getDefault());
                  	try {
						addressList  = geoCoder.getFromLocationName(it.getLocation(),1);
					} catch (IOException e) {
					 
						e.printStackTrace();
					}
                  	if(addressList.isEmpty())
                    {
                  		Toast.makeText(Main.this, "Add Location To Navigate", Toast.LENGTH_LONG).show();
                  		
                    }else{
                    	//new intent
                    	Intent navigation = new Intent(Intent.ACTION_VIEW, Uri
						        .parse("http://maps.google.com/maps?saddr="
						                + addressList.get(0).getLatitude() + ","
						                + addressList.get(0).getLongitude() + "&daddr="
						                +  addressList.get(0).getLatitude() + "," + addressList.get(0).getLongitude()));
                    	//start the intent
						startActivity(navigation);
                    }
				  }
	        	}
			  //close dialog
			  dialog.dismiss();
		  }
		  })
	      .create();   
		  return IntervalAlarmChoice;
	} 
	// Item Long Click listener
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
	{
		AlertDialog diaBox = OptionsDialogBox(DataBaseSingleton.getArrayList().get(arg2),arg0);
		diaBox.show();
		return false;
	}
	
	//  Show Details  AlertDialog
   public void ShowDetails(int pos){
	   
	 // new AlertDialog	
	 AlertDialog alertDialog = new AlertDialog.Builder(Main.this).create(); 
	 alertDialog.setTitle("Task Details");
	 ItemDetails it= DataBaseSingleton.getArrayList().get(pos);
	 alertDialog.setMessage("Title: "+it.getTitle()+"\n"+"Description: "+it.getDescription()+
			 				"\n"+"Alarm: "+it.getDate()+"  "+ it.getTime()+"\n"+"Location: "+it.getLocation());
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
				public void onClick(DialogInterface dialog, int which) {
                
                dialog.dismiss();
                }
        });
	  alertDialog.show();	
}
	// Item Click listener
	@Override
	public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
		// new AlertDialog 
		ShowDetails(position);
	}
	
	 @Override
	public void onStop() {
		 
		    super.onStop();
		    EasyTracker.getInstance().activityStop(this);  
		  }


	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		 
		return false;
	}


	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		 
		return false;
	}


	@Override
	public void onLongPress(MotionEvent arg0) {
	 
		Toast.makeText(this, "LongPress", Toast.LENGTH_LONG).show();
	}


	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "single tap", Toast.LENGTH_LONG).show();
		return false;
	} 
}


