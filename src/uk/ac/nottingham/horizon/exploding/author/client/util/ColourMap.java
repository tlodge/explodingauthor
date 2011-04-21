package uk.ac.nottingham.horizon.exploding.author.client.util;

public class ColourMap {

	private static int index = 0;
	private static String colours[] = {"#7FFF00","#DC143C","#6495ED","#FFA500","#FFFF00","#FF0000","#2E8B57", "#FF1493", "#F0F8FF","#8B008B","#9ACD32", "#40E0D0", "#F5F5F5", "#A0522D"};
	
	public static void reset(){
		index = 0;
	}
	
	public static String getNextColour(){
		return colours[index++%colours.length];
	}
	
	
}
