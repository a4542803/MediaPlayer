package com.example.mediaplayer;

//import android.R.integer;
import java.util.ArrayList;
import java.util.List;

//import com.example.phonesonglist.MainActivity;
//import com.example.phonesonglist.SongList;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;



public class MusicPlay extends FragmentActivity {
			
	private FragmentTabHost fragmentTabHost;
	
	private String texts[] = {"本地播放音乐","在线播放音乐","本地播放视频","在线播放视频"};
	private int imageButton[] = {R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img3};
	private Class fragmentArray[] = {FragmentPage1.class,FragmentPage2.class,FragmentPage3.class,FragmentPage4.class};
	//private List<SongInfo> songList = new ArrayList<SongInfo>(); 
	//private String name[]={"111","222","333","4444"};
	protected void onCreate(Bundle saveInstanceState){
		
		super.onCreate(saveInstanceState);
		setContentView(R.layout.tabiconmain);
		fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		Log.w("1111", MusicPlay.this.toString());
		Log.w("2222", getSupportFragmentManager().toString());
		fragmentTabHost.setup(this,getSupportFragmentManager(),R.id.maincontent);
		fragmentTabHost.setCurrentTab(0);
		/*fragmentTabHost.addTab(fragmentTabHost.newTabSpec("0").setIndicator("新闻"), FragmentPage1.class, null);  
		fragmentTabHost.addTab(fragmentTabHost.newTabSpec("1").setIndicator("音乐"), FragmentPage2.class, null);  
		fragmentTabHost.addTab(fragmentTabHost.newTabSpec("2").setIndicator("人生"), FragmentPage3.class, null);  
		fragmentTabHost.setSelected(true);*/
		//fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.layout.bt_selector);
		for (int i = 0; i < texts.length; i++) {
			
			//Intent intent=new Intent(this, fragmentArray[i]);
			   
			TabSpec spec = fragmentTabHost.newTabSpec(texts[i]).setIndicator(getView(i));
			fragmentTabHost.addTab(spec,fragmentArray[i],null);
		//	fragmentTabHost.addTab(spec);
			Log.w("次数"+i,fragmentTabHost.toString()+"[]"+fragmentArray[i].toString());
			//fragmentTabHost.setSelected(true);
			fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.layout.bt_selector);
			
			//fragmentTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
		
			
			
		}
		/*Intent intent = new Intent(this,playService.class);
		startService(intent);*/
		Log.w("service", "start service ok");
		//getSdSongInfo(MusicPlay.this);
		/*SongInfo songInfo = new SongInfo();
		//for (int i = 0; i < name.length; i++) {
			
			songInfo.setSongName(name[0]);
			songList.add(songInfo);
			
	//	}
		SongAdapter songAdapter = new SongAdapter(, R.layout.songinfo, songList);
		Log.w("songadapter", songAdapter.isEmpty()+"");
		Log.w("qqqq", songAdapter.getItemId(0)+"");
		ListView listView = (ListView) findViewById(R.id.listview);
		try {
			listView.setAdapter(songAdapter);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/
		
		
	}
	
	private View getView(int i){
		
		View view = View.inflate(MusicPlay.this,R.layout.tabcontent, null);
		ImageView imageView = (ImageView)view.findViewById(R.id.image);
		TextView textView = (TextView) view.findViewById(R.id.text);
		imageView.setImageResource(imageButton[i]);
		textView.setTextColor(Color.WHITE);
		textView.setText(texts[i]);
		setTitle(texts[i]);
		return view;
	}
	void getSdSongInfo(Context context)
	{	
		SongInfo songInfo = new SongInfo();
	
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		//cursor.moveToNext();
		String title = cursor.getString((cursor
				.getColumnIndex(MediaStore.Audio.Media.TITLE)));// 音乐标题
		String artist = cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Media.ARTIST));// 艺术家
		//songList.add(songInfo);
	/*	int isMusic = cursor.getInt(cursor
				.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
		Log.w("ismusic", isMusic+"");
		Log.w("name 11", title);
		if (isMusic!=0) {
			
			songInfo.setSongName(title);
			//songInfo.setArtistName(artist);
			songList.add(songInfo);
		}
		for(SongInfo s:songList)
		{
			Log.w("name", s.getSongName().toString());
		}*/
	}
}
