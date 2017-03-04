package org.wy.dialog;

import java.util.Vector;
import org.wy.db.MusicListDao;
import org.wy.entity.SdcardMusic;
import org.wy.utils.CommonData;
import org.wy.utils.CommonUtils;
import org.wy.utils.MyTimeUtils;
import org.wy.utils.PlayMusic;
import com.wy.activity.R;
import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MusicListDialog extends DialogFragment {
	
	private View view;
	private ListAdapter lad;
	private ListView musiclistview;
	private TextView txt_default_list,txt_one_list,txt_two_list,list_name;
	//等待窗口
	private LoadingDialog ld = null;
//	//删除音乐dialog对象
//	PromptInfoDialog pid = null;
	//删除单曲的itemid
	private int pos = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_FRAME, 0);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogMusicList;
		getDialog().setCanceledOnTouchOutside(true);
	}

	@Override
	public void onStart() {
		super.onStart();
		//设置dialog宽高
//		getDialog().getWindow().setLayout(1000, 670);
		//设置dialog位置及大小
		final WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
		layoutParams.width = (int)(CommonData.ScreenWidth/2);
		layoutParams.height = (int)(CommonData.ScreenHeight/3*2);
		layoutParams.x = -(int)(CommonData.ScreenWidth/5);
		layoutParams.y = (int)(CommonData.ScreenHeight/90);
		getDialog().getWindow().setAttributes(layoutParams);
	}

	//关闭音乐列表修改图标
	@Override
	public void onStop() {
		super.onStop();
		if(CommonData.handler_changetutu != null){
			CommonData.handler_changetutu.sendEmptyMessage(0);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.dialog_musiclist, null);
		initView();
		init();
		return view;
	}
	
	/**初始化控件*/
	private void initView(){
		musiclistview = (ListView) view.findViewById(R.id.musiclistview);
		list_name = (TextView) view.findViewById(R.id.list_name);
		//音乐标签
		txt_default_list = (TextView) view.findViewById(R.id.txt_default_list);
		txt_one_list = (TextView) view.findViewById(R.id.txt_one_list);
//		txt_two_list = (TextView) view.findViewById(R.id.txt_two_list);
	}
	
	/**初始化方法*/
	private void init(){
		//初始化适配器
		lad = new ListAdapter(view.getContext());
		//设置适配器
		musiclistview.setAdapter(lad);

		musiclistview.setSelection(PlayMusic.itemId);

		//设置音乐列表点击播放事件
		musiclistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PlayMusic.itemId = position;
				CommonData.flag_playing_list = PlayMusic.playing_label;
				CommonData.handler_changetutu.sendEmptyMessage(1);
			}
		});
		
		//音乐标签事件
		txt_default_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showaddAllMusicImg();
				CommonData.all_music = new MusicListDao(view.getContext()).findAllMusic();
				txt_default_list.setBackgroundResource(R.drawable.label_2);
				txt_one_list.setBackgroundResource(R.drawable.label_1);
//				txt_two_list.setBackgroundResource(R.drawable.label_1);
				txt_default_list.setText(lad.getCount()+"");
				txt_one_list.setText("");
//				txt_two_list.setText("");
				list_name.setText(R.string.list_default);
				PlayMusic.playing_label = 0;
				//删除非当前播放后不操作仅进行刷新列表操作
				notifyMusicList();
			}
		});
		txt_one_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CommonData.all_music = new MusicListDao(view.getContext()).findAllLoveMusic();
				txt_one_list.setBackgroundResource(R.drawable.label_2);
				txt_default_list.setBackgroundResource(R.drawable.label_1);
//				txt_two_list.setBackgroundResource(R.drawable.label_1);
				txt_default_list.setText("");
				txt_one_list.setText(lad.getCount()+"");
//				txt_two_list.setText("");
				list_name.setText(R.string.list_one);
				PlayMusic.playing_label = 1;
				//删除非当前播放后不操作仅进行刷新列表操作
				notifyMusicList();
			}
		});
//		txt_two_list.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				CommonData.all_music = new Vector<SdcardMusic>();
//				txt_two_list.setBackgroundResource(R.drawable.label_2);
//				txt_default_list.setBackgroundResource(R.drawable.label_1);
//				txt_one_list.setBackgroundResource(R.drawable.label_1);
//				txt_default_list.setText("");
//				txt_one_list.setText("");
//				txt_two_list.setText(lad.getCount()+"");
//				list_name.setText(R.string.list_two);
//				PlayMusic.playing_label = 2;
//			}
//		});
		
		//如果默认列表为空，则在打开时将手机中所有音乐添加带默认列表
		if(new MusicListDao(view.getContext()).findAllMusic().isEmpty()){
			showaddAllMusicImg();
		}else{
			if(CommonData.flag_playing_list == 0){
				txt_default_list.performClick();
			}else if(CommonData.flag_playing_list == 1){
				txt_one_list.performClick();
			}
			
		}
	
		//音乐数量点击清空列表
//		musicnum.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(CommonData.all_music.size()>0){
//					new PromptInfoDialog(
//							view.getContext().getString(R.string.clear_list), 
//							view.getContext().getString(R.string.clear_list_ps),
//							-1)
//					.show(getFragmentManager(), null);
//				}
//			}
//		});
		
//		//确认删除后刷新列表
//		CommonData.handler_deletemusic = new Handler(){
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case 0:
//					//重新查询列表数据
//					notify_all_music();
//					
//					if(PlayMusic.itemId == pos){
//						//删除当前播放后通知服务关闭音乐并播放下一曲
//						CommonData.handler_changetutu.sendEmptyMessage(3);
//					}else if(pos < PlayMusic.itemId){
//						PlayMusic.itemId--;
//					}
//					//删除非当前播放后不操作仅进行刷新列表操作
//					notifyMusicList();
//					pid = null;
//					break;
//				case 1:
//					showLoadDialog(-150,-220);
//					new Thread(){
//						public void run() {
//							//清空
//							new MusicListDao(view.getContext()).clearAllMusic();
//							hand.sendEmptyMessage(1);
//						};
//					}.start();
//					pid = null;
//					//清空后通知服务关闭音乐
//					CommonData.handler_changetutu.sendEmptyMessage(4);
//					break;
//				default:
//					pid = null;
//					break;
//				}
//			}
//		};
	}
	
	//将当前标签的数据加入到数据列表中
	private void notify_all_music(){
		if(PlayMusic.playing_label == 0){
			CommonData.all_music = new MusicListDao(view.getContext()).findAllMusic();
		}else if(PlayMusic.playing_label == 1){
			CommonData.all_music = new MusicListDao(view.getContext()).findAllLoveMusic();
		}
//		else if(PlayMusic.playing_label == 2){
//			CommonData.all_music = new (view.getContext()).findAllMusic_2();
//		}
	}
	
	private void showaddAllMusicImg(){
		if(new MusicListDao(view.getContext()).findAllMusic().isEmpty()){//数据库为空
			//播放列表添加音乐
			if(new MusicListDao(view.getContext()).findAllMusic().isEmpty()){
				showLoadDialog(-360,-305);
				new Thread(){
					public void run() {
						Vector<SdcardMusic> als = new Vector<SdcardMusic>();
						als = CommonUtils.getMusicFromSdCard(view.getContext());
						for(int i=0;i<als.size();i++){
							if(als.elementAt(i).getDuration() > 30000){
								new MusicListDao(view.getContext()).initData(als.elementAt(i));
							}
						}
						hand.sendEmptyMessage(0);
					};
				}.start();
			}
		}else{//数据库不空
//			Toast.makeText(view.getContext(), R.string.application_no_music, Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressLint("HandlerLeak")
	//音乐列表变化后刷新列表
	private Handler hand = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				//添加
				notifyMusicList();
				showaddAllMusicImg();
				closeLoadDialog();
				Toast.makeText(view.getContext(), R.string.add_success, Toast.LENGTH_SHORT).show();
				txt_default_list.performClick();
				break;
			case 1:
				//清空
				notifyMusicList();
				closeLoadDialog();
				showaddAllMusicImg();
				Toast.makeText(view.getContext(), R.string.delete_all_music_success, Toast.LENGTH_SHORT).show();
				break;
			default:
//				pid = null;
				break;
			}
		};
	};
	
	/**刷新音乐列表 */
	public void notifyMusicList(){
		notify_all_music();
		if(lad != null){
			lad.notifyDataSetChanged();
		}
	}
	
	/**
	 * 显示添加音乐等待框
	 */
	private void showLoadDialog(int x,int y){
		if(ld == null){
			ld = new LoadingDialog(false);
			ld.show(getFragmentManager(), null);
		}
	}
	/**
	 * 关闭等待框
	 */
	private void closeLoadDialog(){
		if(ld != null){
			ld.dismiss();
			ld = null;
		}
	}

	//重写适配器
	class ListAdapter extends BaseAdapter{
		
		private LayoutInflater l;
		
		public ListAdapter(Context context) {
			l = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return CommonData.all_music.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return CommonData.all_music.elementAt(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return CommonData.all_music.elementAt(position).getId();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//一列的数据
			SdcardMusic sdm = CommonData.all_music.elementAt(position);
			
			if(convertView == null){
				convertView = l.inflate(R.layout.item_musiclist, null);
			}	
			//正在播放时动画控件
			try {
				ImageView imgisplaying = (ImageView) convertView.findViewById(R.id.imgisplaying);
				if(CommonData.playstatus.flag_play == CommonData.playstatus.PLAY && position == PlayMusic.itemId && CommonData.flag_playing_list == PlayMusic.playing_label){
					convertView.setBackgroundColor(getResources().getColor(R.color.my_white));
					imgisplaying.setImageResource(R.drawable.anim_playing);
					AnimationDrawable animationDrawable = (AnimationDrawable) imgisplaying.getDrawable();
					animationDrawable.start();
				}else if(CommonData.playstatus.flag_play == CommonData.playstatus.PAUSE && position == PlayMusic.itemId){
					convertView.setBackgroundColor(getResources().getColor(R.color.half_blanchedalmond));
				}else{
					convertView.setBackgroundColor(getResources().getColor(R.color.half_blanchedalmond));
					imgisplaying.setImageResource(R.drawable.nopic);
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("sss", "animation is error", e);
			}
			//序号
			TextView listnum = (TextView) convertView.findViewById(R.id.listnum);
			//歌名
			TextView musicname = (TextView) convertView.findViewById(R.id.musicname);
			//歌手
			TextView artist = (TextView) convertView.findViewById(R.id.artist);
			//歌曲时间
			TextView duration = (TextView) convertView.findViewById(R.id.duration);
			//喜欢音乐
			final ImageView imgbtndeletemusic = (ImageView) convertView.findViewById(R.id.imgbtndeletemusic);
			
			listnum.setText((position+1)+"");
			musicname.setText(sdm.getTilte());
			if(sdm.getArtist().equals("<unknown>")){
				artist.setText("未知歌手");
			}else{
				artist.setText(sdm.getArtist());
			}
			if(MyTimeUtils.Ms2mmss(sdm.getDuration()).equals("识别错误")){
				duration.setTextColor(Color.RED);
			}else{
				duration.setTextColor(getResources().getColor(R.color.darkgray));
			}
			duration.setText(MyTimeUtils.Ms2mmss(sdm.getDuration()));
			
			if(PlayMusic.playing_label == 1){//显示删除按钮
//				imgbtndeletemusic.setImageResource(R.drawable.recycler);
				imgbtndeletemusic.setImageResource(R.drawable.nopic);
			}else if(PlayMusic.playing_label == 0){//显示喜欢按钮
				if(sdm.getStatus() == 2){
					imgbtndeletemusic.setImageResource(R.drawable.deletemusic2);
				}else{
					imgbtndeletemusic.setImageResource(R.drawable.deletemusic);
				}
			}

			imgbtndeletemusic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(CommonData.flag_playing_list != 1){//喜欢的列表处于播放状态，则不能修改
						if(PlayMusic.playing_label == 1){//执行删除操作
//							if(pid == null){
//								pid = new PromptInfoDialog(
//										String.format(view.getContext().getString(R.string.del_or_not), sdm.getTilte()), 
//										view.getContext().getString(R.string.del_ps), 
//										sdm.getUrl());
//								pos = position;
//								pid.show(getFragmentManager(), null);
//								new MusicListDao(view.getContext()).unlikeMusic(sdm.getId());
//							}
						}else if(PlayMusic.playing_label == 0){//执行添加为喜欢列表操作
							CommonData.all_music = new MusicListDao(view.getContext()).findAllMusic();//重新检索音乐列表所有数据
							SdcardMusic sdm = CommonData.all_music.elementAt(position);
							MusicListDao mmld = new MusicListDao(view.getContext());
							if(sdm.getStatus() == 2){//当前点击为喜欢的音乐
								mmld.unlikeMusic(sdm.getId());
								imgbtndeletemusic.setImageResource(R.drawable.deletemusic);
								Toast.makeText(view.getContext(), R.string.delete_like_music, Toast.LENGTH_SHORT).show();
							}else{//当前点击为不喜欢的音乐
								mmld.likeMusic(sdm.getId());
								imgbtndeletemusic.setImageResource(R.drawable.deletemusic2);
								Toast.makeText(view.getContext(), R.string.add_like_music, Toast.LENGTH_SHORT).show();
							}
						}
					}else{
						Toast.makeText(view.getContext(), R.string.like_is_playing, Toast.LENGTH_SHORT).show();
					}
				}
			});
			return convertView;
		}
		
	}
}
