package com.shenkar.todolistproject;
 

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

 

public class Gesture extends SimpleOnGestureListener {
	
	private GestureDetector gDetector;
  
	   private static final int SWIPE_MIN_DISTANCE = 120;
	   private static final int SWIPE_MAX_OFF_PATH = 200;
	   private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	   	   
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

 @Override
 public boolean onFling(MotionEvent e1, MotionEvent e2,
                      float velocityX, float velocityY) {
   try {

	 float diffX = e1.getX() - e2.getX();
     float diffY = e1.getY() - e2.getY();
 
     // Down swipe
     if(-diffY > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY ){
      
      return false;
     }else{
     
     // Up swipe
     if( diffY > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY ){
   
     	return false;
     }}
     
     // Left swipe
     if (diffX > SWIPE_MIN_DISTANCE
     && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
    	 YourSlideleft();
    	 return false;
     
     // Right swipe
     } else if (-diffX > SWIPE_MIN_DISTANCE
     && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
    	 YourSlideRight();
     	return false;
     }
   } catch (Exception e) {
     
   }
   return false;
 }

 	public void YourSlideRight() {
	 
 	}

 	public void YourSlideleft() {
	 
	
 	}

}
