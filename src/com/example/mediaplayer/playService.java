package com.example.mediaplayer;

//import android.R.integer;
import android.R.integer;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;



public class playService extends Service{
		
		private static String TAG = "MainService";
		private MediaPlayer player;
		private AudioManager mAm;
		private MyOnAudioFocusChangeListener mListener;
		public static final int UPDATE_TEXT = 1;
		//private DownloadBinder mBinder = new DownloadBinder();
		private Intent intent;
		private Intent timeIntent;
		private float playTime = 0;
		  private int progress = 0;  
		  private boolean playstate = false;
		  private boolean completion = false;
	   private OnProgressListener onProgressListener;
	   private MsgBinder msgBinder;

		public void onCreate()
		{
			Log.i(TAG, "onCreate");
			msgBinder = new MsgBinder();
			intent = new Intent("com.example.communication.RECEIVER");
			timeIntent = new Intent("com.example.communication.RECEIVERTIME");
			mAm = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
			mListener = new MyOnAudioFocusChangeListener();
			if(player==null)
			{
				player = new MediaPlayer();
			}
			player.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer arg0) {
					Log.w("send", "send broast");
					
					// player.release();
					//onProgressListener.onProgress(0);
					completion = true;
					sendBroadcast(intent);
					
				}
			});
		}
	
		 public class MsgBinder extends Binder{  
			         /** 
			          * 获取当前Service的实例 
			         * @return 
			         */  
			        public playService getService(){  
			            return playService.this;  
			         }  
			     }  
			public IBinder onBind(Intent intent)
			{
				return new MsgBinder();  
			}
		 public void setOnProgressListener(OnProgressListener onProgressListener) {  
			         this.onProgressListener = onProgressListener;  
			    }  
		 public int getProgress() {  
			 if(player==null||!playstate) {
				 return progress;  
			 	}
			 if (player.isPlaying()) {
				 progress = player.getCurrentPosition();  
			}
			 	//progress = player.getDuration();  
			      return progress;  
			}  
		   public void startDownLoad(){  
			  
				   if (playstate) {
					   new Thread(new Runnable() {  
			                 
			                
			               public void run() {  
			            	   Log.w("run", "run is ok");
			            	   if (player!=null&&playstate) {
			            		   while(player.isPlaying()){  
					                      
				                         
				                       //进度发生变化通知调用方  
				                	  // Log.w("thread", "thread start");
			            			  
				                       if(onProgressListener != null){  
				                    	 
				                    	onProgressListener.onProgress(player.getCurrentPosition());
				                    	timeIntent.putExtra("playing", "OK");
				                    	timeIntent.putExtra("progress", player.getCurrentPosition());
											sendBroadcast(timeIntent);
				                    		   
										}
				                          try {
											Thread.sleep(1000);
										} catch (Exception e) {
											// TODO: handle exception
										}
				                          // Log.w("onProgressListener", "onProgressListener start");
				                      }  
				                         
				                    
				                         
				                   }  
							}
			                  
			                 
			           }).start();  
				}
			           
			       }  
			     

		public void onStart(Intent intent,int startid)
		{
			Toast.makeText(this, "my service start",Toast.LENGTH_SHORT).show();
			Log.i(TAG, "onStart");
			int result = mAm.requestAudioFocus(mListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
			if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
				
				Log.i(TAG, "requestAudioFocus successfully");
				
				try {
					if (appContext.getSelecSongUrlString()!=null) {
						String stateString = intent.getStringExtra("state");
					
						
						
						if (stateString!=null) {
							Log.w("state", stateString);
							if (stateString.equals("playing")) {
								start();
							}
							else if (stateString.equals("pause")) {
								
								pause(false);
							}
							else if(stateString.equals("play")) {
								pause(true);
							}
							else if (stateString.equals("seekTo")){
								 seekTo();
							}
							else if (stateString.equals("click seek")) {
								String clickProgress = intent.getStringExtra("progress");
								Log.w("progress",clickProgress+ "");
								clickSeekTo(Integer.parseInt(clickProgress));
								
							}
								
							
						}
						
						
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
			}
			else {
				
				Log.e(TAG, "requestAudioFocus failed");
			}
		}
		
		public void onDestroy()
		{
			Toast.makeText(this, "My Service Stoped",Toast.LENGTH_SHORT).show();
			Log.i(TAG, "onDestory");
			player.stop();
			mAm.abandonAudioFocus(mListener);
			player.release();
		}
		 private class MyOnAudioFocusChangeListener implements  
		              OnAudioFocusChangeListener  
		      {  
		          @Override  
		         public void onAudioFocusChange(int focusChange)  
		          {  
		             Log.i(TAG, "focusChange=" + focusChange);  
		          }  
		      }  
		 public void start()
		 {
			 player.reset();
				try {
					player.setDataSource(appContext.getSelecSongUrlString());
					
					player.prepare();
					//player.start();
					playstate = true;
					completion = true;
					player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {  
						               @Override  
						           public void onPrepared(MediaPlayer mediaPlayer) {  
						            	   player.start();//开始音频  
						            		onProgressListener.GetProgress(player.getDuration());
						               }  
						           });  

					//if (completion) {
					
						
					//}
					//intent.putExtra("allTime",player.getDuration());
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			
		 }
		 public void seekTo()
		 {	
			 if (player.isPlaying()) {
				 playTime = playTime + 60000;
				 if (playTime>player.getDuration()) {
					 
					 playTime = player.getDuration();
					 player.seekTo((int)playTime);
					 player.start();
					 playTime = 0;
				 }
				 else {
					 player.seekTo((int)playTime);
				}
					
					
					 Log.w("time", playTime +"" );
				
				 /*else {
					Toast.makeText(getApplication(), "已经到最后啦，请按下一曲", Toast.LENGTH_SHORT).show();*/
				}
		
			
		 }
		 public void clickSeekTo(int progress)
		 {	
			 if (player.isPlaying()) {
					 player.seekTo(progress);
					 Log.w("click seek", progress +"" );
				}
		 }
		 public void stop()
			{
				if (player != null)
				{
					player.stop();
				}
			}
			public void pause(boolean state)
			{
				if (state) {
					
					player.start();
					msgBinder.getService().startDownLoad();
				}
				else {
					player.pause();
				}
			}
		/*	public void next()
			{
				if (mediaPlayer!=null) {
					
					i++;
					SongInfo NextSongInfo = songList.get(i);
					playMp3Url = NextSongInfo.getUrlString();
					songAdapter.setSelectedPosition(i);                       
					songAdapter.notifyDataSetInvalidated();      
					start();
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
						start();
					}
			}*/
			public interface OnProgressListener {  
				    void onProgress(int progress);
				    void GetProgress(int time);
				}  
	
}
