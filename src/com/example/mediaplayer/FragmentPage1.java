package com.example.mediaplayer;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediaplayer.R.id;
import com.example.mediaplayer.playService.OnProgressListener;
//import mobile.android.ch14.mediaplayer.R;
//import android.app.Fragment;

public class FragmentPage1 extends Fragment implements View.OnClickListener,OnCompletionListener  {
			
	private List<SongInfo> songList = new ArrayList<SongInfo>(); 
	private Button downloadButton;
	private Button startButton;
	//private Button stopButton;
	private Button pauseButton;
	private Button nextButton;
	private Button prevButton;
	private Button seekToButton;
	private MediaPlayer mediaPlayer;
	//private String name[]={"111","222","333","4444"};
	//private SongInfo songInfo; 
	private SongAdapter songAdapter;
	private String playMp3Url = null;
	private String lastMp3Url = null;
	private int i = -1;
	private Intent intent;
	//private ProgressBar progressBar;
	private SeekBar progressBar;
	private MsgReceiver msgReceiver;
//	private ArrayAdapter<String> adapter;
	private playService msgService ;
	private CurrentPlayTimeReceiver currentPlayTimeReceiver;
	private Intent bindIntent;
	private TextView currentTime;
	private TextView allTime;
	private String allSencond="00";
	private String allMinute = "00";
	private String allHour = "00";
	
	private String curSencond="00";
	private String curMinute = "00";
	private String curHour = "00";
	
	private TextView playStateTextView;
	private OnRefreshCurrentTimeListener onRefreshCurrentTimeListener;
	/*private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
		switch (msg.what) {
		case 1:
		// 在这里可以进行UI操作
			currentTime.setText(msg.arg1);
		break;
		default:
		break;
		}
		}
		};*/
	public void onCompletion(MediaPlayer mp)
	{
		mp.release();
		//setTitle("资源已经释放");

	}
	/* public void setOnProgressListener1(OnRefreshCurrentTimeListener onRefreshCurrentTimeListener) {  
         this.onRefreshCurrentTimeListener = onRefreshCurrentTimeListener;  
    }  */
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		getSdSongInfo(activity);
		 intent = new Intent(activity,playService.class);
		if (!songList.isEmpty()) {
			
			songAdapter = new SongAdapter(activity, R.layout.songinfo, songList);
			Log.w("songAdapter", "songAdapter create ok");
		}
		
		
		// adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1,name);
		//Adapter adapter = new ArrayAdapter<T>
		
	}
		public View onCreateView(LayoutInflater inflater,
		ViewGroup container,Bundle savedInstanceState)
		{	
			
			View view =  inflater.inflate(R.layout.localmusic,container,false);
			ListView listView = (ListView) view.findViewById(R.id.listview);
			// Intent intent = new Intent("com.example.communication.MSG_ACTION");  
			     
		//	getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);  
			mediaPlayer = new MediaPlayer();
			msgReceiver = new MsgReceiver();
			currentPlayTimeReceiver = new CurrentPlayTimeReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction("com.example.communication.RECEIVER");
			
			IntentFilter intentFilter1 = new IntentFilter();
			intentFilter1.addAction("com.example.communication.RECEIVERTIME");
			 bindIntent = new Intent(getActivity(), playService.class);
			getActivity().bindService(bindIntent, conn, getActivity().BIND_AUTO_CREATE);
			
			getActivity().registerReceiver(msgReceiver, intentFilter);
			getActivity().registerReceiver(currentPlayTimeReceiver, intentFilter1);
			
			try {
				if (songAdapter!=null) {
					listView.setAdapter(songAdapter);
				}
				
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			//listView.getChildCount();
			Log.w("33333333", "open f1 ok" + listView.getChildCount());
			listView.setOnItemClickListener(new OnItemClickListener() {
				
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
						SongInfo songInfo = songList.get(position);
						Log.w("url111", songInfo.getUrlString());
						//playMp3Url.append(songInfo.getUrlString());
						playMp3Url = songInfo.getUrlString();
						songAdapter.setSelectedPosition(position);                       
						songAdapter.notifyDataSetInvalidated();      
						i = position;
						appContext.setSelecSongUrlString(playMp3Url);
						
						//getActivity().startService(intent);
						//listView.setSelected(true);
						//listView.setFocusable(true);
					//	listView.setSelector(R.drawable.listseleccolor);
						}
				
			} );
			 
				
				
//			listView.setSelected(true);
			//listView.setSelector(R.drawable.listseleccolor);
			/*listView.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				//@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					if (arg1 == true) {
						
						listView.setSelector(R.drawable.listseleccolor);
					}
					else {
						listView.setSelector(R.drawable.listseleccolor);
					}
				}
			});
			
*/			
			/*mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub
				//	i++;
					if (i<songList.size()) {
						i++;
					}
					else {
						Toast.makeText(getActivity(), "已经到底啦",Toast.LENGTH_SHORT).show();
					}
					SongInfo NextSongInfo = songList.get(i);
					playMp3Url = NextSongInfo.getUrlString();
					songAdapter.setSelectedPosition(i);                       
					songAdapter.notifyDataSetInvalidated(); 
					appContext.setSelecSongUrlString(playMp3Url);
					intent.putExtra("state", "playing");
					getActivity().startService(intent);
					
					//getActivity().startService(intent);
					//start();
				}
			});*/
			startButton = (Button) view.findViewById(R.id.start);
		//	stopButton = (Button)view.findViewById(R.id.stop);
			pauseButton = (Button) view.findViewById(R.id.pause);
			nextButton = (Button) view.findViewById(R.id.next);
			prevButton = (Button) view.findViewById(R.id.prev);
			seekToButton = (Button) view.findViewById(R.id.seekto);
			progressBar = (SeekBar) view.findViewById(R.id.myProgressBar);
			currentTime = (TextView) view.findViewById(R.id.currentTime);
			allTime = (TextView) view.findViewById(R.id.AllTime);
			playStateTextView = (TextView) view.findViewById(R.id.musicState);
			//progressBar = (ProgressBar) view.findViewById(R.id.myProgressBar);
			startButton.setOnClickListener(this);
			//stopButton.setOnClickListener(this);
			pauseButton.setOnClickListener(this);
			nextButton.setOnClickListener(this);
			prevButton.setOnClickListener(this);
			seekToButton.setOnClickListener(this);
			progressBar.setOnClickListener(this);
			
			pauseButton.setText("play");
			//currentTime.setText("hahah");
			//Intent intent = new Intent(getActivity(),playService.class);
			progressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					// fromUser  是否是用户操作
					
					if(fromUser){
						Log.w("progress1111", progress+"");
						intent.putExtra("state", "click seek");
						intent.putExtra("progress", progress+"");
						getActivity().startService(intent);
						
					}
				}
			});
			/*this.setOnProgressListener1(new OnRefreshCurrentTimeListener() {
				
				@Override
				public void onRefresh(int progress) {
					// TODO Auto-generated method stub
					Log.w("refresh", ""+progress);
					//currentTime.setText("hahah");
					
					
				}
			});*/
			return view;
		}
		
		public void onDestroy() {
	        super.onDestroy();
	       getActivity().unregisterReceiver(currentPlayTimeReceiver);//取消注册广播
	    }
		/* private void setOnProgressListener(
				OnRefreshCurrentTimeListener onRefreshCurrentTimeListener2) {
			// TODO Auto-generated method stub
			
		}*/
		
		ServiceConnection conn = new ServiceConnection() {  
 	         
	   	       public void onServiceDisconnected(ComponentName name) {  
	   	              
	   	        }  
	   	          
	   	        @Override  
	   	       public void onServiceConnected(ComponentName name, IBinder service) {  
	   	           //返回一个MsgService对象  
	   	        	Log.w("service222", "service is start ok ");
	   	           msgService = ((playService.MsgBinder)service).getService();  
	   	          //   msgService.startDownLoad();
	   	         // progressBar.setMax(msgService.getProgress());
	   	         
	   	            //注册回调接口来接收下载进度的变化  
	   	            msgService.setOnProgressListener(new OnProgressListener() {  
	   	                 
	   	                @Override  
	   	               public void onProgress(int progress) {
	   	                	//Log.w("progress", progress+"");
	   	                	//msgService.startDownLoad();
	   	                	/*if (progress == 0) {
								
	   	                	 progressBar.setMax(msgService.getProgress());
							}*/
	   	                	progressBar.setProgress(progress); 
	   	                	//currentTime.setText(msgService.getProgress()+"");
	   	                	/*if (onRefreshCurrentTimeListener!=null) {
								onRefreshCurrentTimeListener.onRefresh(progress);
							}
	   	                	Message message = new Message();
	   	                	message.what = 1;
	   	                	message.arg1 = progress;
	   	                	handler.sendMessage(message);*/
	   	                	//currentTime.setText(progress+"");
	   	                	
	   	                	
	   	                      
	   	                }  
	   	                public void GetProgress(int time)
	   	                {
	   	                	progressBar.setMax(time);
	   	                	msgService.startDownLoad();
	   	                	allSencond = time/1000%60+"";
	   	                	allHour = time/1000/60/60+"";
	   	                	allMinute = time/1000/60+"";
	   	                	if (Integer.parseInt(allHour)<10) {
								
	   	                		allHour = "0"+allHour;
							}
	   	                	if (Integer.parseInt(allMinute)<10) {
								
	   	                		allMinute = "0"+allMinute;
							}
	   	                	if (Integer.parseInt(allSencond)<10) {
								
	   	                		allSencond = "0"+allSencond;
							}
	   	                	allTime.setText(allHour+":"+allMinute+":"+allSencond);
	   	                	
	   	                	playStateTextView.setText("音乐正在播放");
	   	                }
	   	            });  
	   	              
	   	        }  
	   	    };  
	   
		void getSdSongInfo(Context context)
		{		
			
			 String url =null;
			Cursor cursor = context.getContentResolver().query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			  while (cursor.moveToNext()) {
				  	
				  SongInfo songInfo = new SongInfo();
					String title = cursor.getString((cursor
							.getColumnIndex(MediaStore.Audio.Media.TITLE)));// 音乐标题
					String artist = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ARTIST));// 艺术家
					
					 url = cursor.getString(cursor .getColumnIndex(MediaStore.Audio.Media.DATA)); 
					//songList.add(songInfo);
					//if (isMusic!=0) {
						
						songInfo.setSongName(title);
						songInfo.setArtistName(artist);
						songInfo.setUrlString(url);
						songList.add(songInfo);
						//cursor.moveToNext();
					//}
					/*for(SongInfo s:songList)
					{
						Log.w("name", s.getSongName().toString());
					}*/
				songInfo = null;
				Log.w("url", url);
			}
			
			//cursor.moveToNext();
			//cursor.moveToFirst();
			 // SongInfo servicePlaySongInfo = null;
			//  servicePlaySongInfo = songList.get(0);
			 // appContext.setSelecSongUrlString(servicePlaySongInfo.getUrlString());
			  cursor.close();
		}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			try
			{
				switch (arg0.getId())
				{
					case R.id.start:
						
						start();
						
						//progressBar.setVisibility(View.VISIBLE);
						break;
					/*case R.id.stop:
						stop();
						break;*/
					case R.id.pause:
						pause();
						break;
					case R.id.next:
						next();
						break;
					case R.id.prev:
						prev();
						break;
						case R.id.seekto:
							seekTo();
							break;
							
				}
			}
			catch (Exception e)
			{

			}

		}
	public void start()
	{	
		try {
			
			/*if (mediaPlayer!=null)*/ {
				Log.w("state", "start mp3 plsy");
				if (lastMp3Url!=null) {
					Log.w("name",lastMp3Url.equals(playMp3Url)+"");
					if (lastMp3Url.equals(playMp3Url)) {
						return;
					}
				}
				
			/*	mediaPlayer.reset();   
				mediaPlayer.setDataSource(playMp3Url);
			
				mediaPlayer.prepare();
				
				mediaPlayer.start();*/
				intent.putExtra("state", "playing");
				getActivity().startService(intent);
			
				lastMp3Url = playMp3Url;
				//playMp3Url = null;
				pauseButton.setText("pause");
				// msgService.startDownLoad();
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	public void stop()
	{
		if (mediaPlayer != null)
		{
			mediaPlayer.stop();
		}
	}
	public void pause()
	{
		if (mediaPlayer != null)
		{	
			if (mediaPlayer.isPlaying()) {
				if ("pause".equals(pauseButton.getText().toString()))
				{
					//mediaPlayer.start();
					//mediaPlayer.pause();
					
					intent.putExtra("state", "pause");
					getActivity().startService(intent);
					pauseButton.setText("play");
					playStateTextView.setText("暂停中");
					
				}
				else if ("play".equals(pauseButton.getText().toString()))
				{
					//mediaPlayer.pause();
					//mediaPlayer.start();
					intent.putExtra("state", "play");
					getActivity().startService(intent);
					pauseButton.setText("pause");
					playStateTextView.setText("音乐正在播放");
					//msgService.startDownLoad();
				}
		}
			else {
				
				if (lastMp3Url!=null) {
					
					if ("pause".equals(pauseButton.getText().toString()))
					{
						//mediaPlayer.start();
						//mediaPlayer.pause();
						intent.putExtra("state", "pause");
						getActivity().startService(intent);
						pauseButton.setText("play");
						playStateTextView.setText("暂停中");
						
					}
					else if ("play".equals(pauseButton.getText().toString()))
					{
						//mediaPlayer.pause();
						//mediaPlayer.start();
						intent.putExtra("state", "play");
						getActivity().startService(intent);
						pauseButton.setText("pause");
						playStateTextView.setText("音乐正在播放");
						//msgService.startDownLoad();
					}
					
				}
			}
			
			/*if (mediaPlayer.getCurrentPosition() == 1) {
				mediaPlayer.pause();
				pauseButton.setText("play");
			}
			else if(mediaPlayer.getCurrentPosition() == 0){
				mediaPlayer.start();
				pauseButton.setText("pause");
			}*/
		}
		
	}
	public void next()
	{
		if (mediaPlayer!=null) {
			
			i++;
			SongInfo NextSongInfo = songList.get(i);
			playMp3Url = NextSongInfo.getUrlString();
			songAdapter.setSelectedPosition(i);                       
			songAdapter.notifyDataSetInvalidated();
			appContext.setSelecSongUrlString(playMp3Url);
			intent.putExtra("state", "playing");
			getActivity().startService(intent);
			// msgService.startDownLoad();
			//start();
		}
	}
	public void prev()
	{
	if (mediaPlayer!=null) {
				
				i--;
				SongInfo NextSongInfo = songList.get(i);
				playMp3Url = NextSongInfo.getUrlString();
				songAdapter.setSelectedPosition(i);                       
				songAdapter.notifyDataSetInvalidated(); 
				appContext.setSelecSongUrlString(playMp3Url);
				intent.putExtra("state", "playing");
				getActivity().startService(intent);
				
				//start();
			}
	}
	public void seekTo()
	{
		intent.putExtra("state", "seekTo");
		getActivity().startService(intent);
	}
	
	public class MsgReceiver extends BroadcastReceiver{
		
		private Intent  intent1;
		public void onReceive(Context context,Intent intent)
		{
			Log.w("receive", "receive broast");
			
			if (i<songList.size()) {
				i++;
			}
			else {
				//i = songList.size();
				Toast.makeText(context, "最后一首啦",Toast.LENGTH_SHORT).show();
				return;
			}
			Log.w("songlist size",songList.size()+"");
			SongInfo NextSongInfo = songList.get(i);
			playMp3Url = NextSongInfo.getUrlString();
			songAdapter.setSelectedPosition(i);                       
			songAdapter.notifyDataSetInvalidated();
			appContext.setSelecSongUrlString(playMp3Url);
			intent1 = new Intent(getActivity(),playService.class);
			intent1.putExtra("state", "playing");
			context.startService(intent1);
			progressBar.setVisibility(View.VISIBLE);
		}
	}
	public class CurrentPlayTimeReceiver extends BroadcastReceiver{
		
		public void onReceive(Context context,Intent intent)
		{
			//Log.w("pliaying11111", intent.getStringExtra("playing")+intent.getStringExtra("progress"));
			if (intent.getStringExtra("playing").equals("OK")) {
				
				int time = intent.getIntExtra("progress", 0);
				curSencond = time/1000%60+"";
               	curHour = time/1000/60/60+"";
               	curMinute = time/1000/60+"";
               	if (Integer.parseInt(curHour)<10) {
					
               		curHour = "0"+curHour;
				}
               	if (Integer.parseInt(curMinute)<10) {
					
               		curMinute = "0"+curMinute;
				}
               	if (Integer.parseInt(curSencond)<10) {
					
               		curSencond = "0"+curSencond;
				}
				currentTime.setText(curHour+":"+curMinute+":"+curSencond);
				//return;
			}
		}
	}
	public interface OnRefreshCurrentTimeListener {  
	    
		void onRefresh(int progress);
	   
	}  
}
