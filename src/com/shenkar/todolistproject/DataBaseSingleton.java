package com.shenkar.todolistproject;

/**
 * This class responsible of the array list 
 */
import java.util.ArrayList;

public class DataBaseSingleton 
{
	//Variables
	private  static DataBaseSingleton instance = new DataBaseSingleton();
	
	private  static ArrayList<ItemDetails> Array_item_details=new ArrayList<ItemDetails>();

	private  DataBaseSingleton (){}
	
	
	public  static  synchronized DataBaseSingleton getInstance ()
	{
        return instance;
    }
	
	public static ArrayList<ItemDetails> getArrayList()
	{
		
		return Array_item_details;
	}
	// set new task 
	public static void setArray(ItemDetails It)
	{   
		 
		Array_item_details.add(0, It);
		Array_item_details.get(0).setDate("Not Set");
		Array_item_details.get(0).setTime("");
		
		if(Array_item_details.get(0).getDescription().length()==0 ){
			Array_item_details.get(0).setDescription("Not Set");
		}
		
		if(Array_item_details.get(0).getLocation().length()==0 ){
			Array_item_details.get(0).setLocation("Not Set");
		}
		
	}
	
	@Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
	
	 
}
