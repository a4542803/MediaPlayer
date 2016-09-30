package com.example.mediaplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

public class FragmentPage2 extends Fragment {
	
	public View onCreView(LayoutInflater inflater,
			ViewGroup container,Bundle savedInstanceState)
			{	
				
				return inflater.inflate(R.layout.onlinemusic, null);
			}
}
