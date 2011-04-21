package uk.ac.nottingham.horizon.exploding.author.client.util;

public class TimeLineIdGenerator {
	static int index = 0;
	
	public static String getID(){
		return String.valueOf(index++);
	}
}
