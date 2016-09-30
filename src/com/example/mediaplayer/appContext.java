package com.example.mediaplayer;

import android.app.Application;

public class appContext extends Application{
			
	private static String selecSongUrlString;
	
	public void onCreate()
	{
		selecSongUrlString = null;
	}

	public static String getSelecSongUrlString() {
		return selecSongUrlString;
	}

	public  static void setSelecSongUrlString(String selecSongUrlString1) {
		selecSongUrlString = selecSongUrlString1;
	}
	
	
}
