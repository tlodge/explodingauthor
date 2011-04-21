package uk.ac.nottingham.horizon.exploding.author.client.util;

import com.allen_sauer.gwt.log.client.Log;

public class TimeConverter {

	
	public static int getStartTime(int column, int sequence){
		return (column-1) * 100 + (sequence*25);
	}
	
	public static int getEndTime(int column, int sequence){
		if (sequence==0)
			return ((column-1) * 100) + 100;
		else
			return ((column-1) * 100) + (sequence*25) + 25;
	}
	
	public static int getColumnFromStartTime(int startTime){
		return ((int) (startTime /100))+1; 
	}
	
	public static int getSequenceFromStartTime(int startTime){
		float remainder = startTime % 100;
		return  (int) (remainder / 25);
	}
	
}
