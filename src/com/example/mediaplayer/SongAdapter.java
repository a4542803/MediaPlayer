package com.example.mediaplayer;

import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SongAdapter extends ArrayAdapter<SongInfo> {
		
	private int resourceId;
	private int selectedPosition = -1;
	public SongAdapter(Context context,int textViewResourceId,List<SongInfo> objects)
	{
		super(context, textViewResourceId,objects);
		resourceId = textViewResourceId;
	}
	
	public View getView(int position,View converView,ViewGroup parent)
	{
		SongInfo songInfo = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (converView==null) {
			
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.songNameTextView = (TextView) view.findViewById(R.id.songname);
			viewHolder.artistTextView = (TextView) view.findViewById(R.id.artistname);
			view.setTag(viewHolder);
		}
		else {
			view = converView;
			viewHolder = (ViewHolder) view.getTag();
		}
		//view.setBackgroundResource(R.drawable.listseleccolor);
		//TextView songName = (TextView)view.findViewById(R.id.songname);
	//	songName.setText(songInfo.getSongName());
		//TextView artistName = (TextView) view.findViewById(R.id.artistname);
		//songName.setText(songInfo.getSongName());
		viewHolder.songNameTextView.setText(songInfo.getSongName());
		//artistName.setText(songInfo.getArtistName());
		viewHolder.artistTextView.setText(songInfo.getArtistName());
		//songInfo.setUrlString(urlString)
		if (selectedPosition == position) {
			
			view.setBackgroundColor(Color.GREEN);
		}
		else {
			
			view.setBackgroundColor(Color.TRANSPARENT);       
		}
		return view;
		
	}
	class ViewHolder{
		
		TextView songNameTextView;
		TextView artistTextView;
	}
	public void setSelectedPosition(int position) {
		// TODO Auto-generated method stub
		 selectedPosition = position;  
	}
}
