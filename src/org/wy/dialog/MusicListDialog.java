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
	//�ȴ�����
	private LoadingDialog ld = null;
//	//ɾ������dialog����
//	PromptInfoDialog pid = null;
	//ɾ��������itemid
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
		//����dialog���
//		getDialog().getWindow().setLayout(1000, 670);
		//����dialogλ�ü���С
		final WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
		layoutParams.width = (int)(CommonData.ScreenWidth/2);
		layoutParams.height = (int)(CommonData.ScreenHeight/3*2);
		layoutParams.x = -(int)(CommonData.ScreenWidth/5);
		layoutParams.y = (int)(CommonData.ScreenHeight/90);
		getDialog().getWindow().setAttributes(layoutParams);
	}

	//�ر������б��޸�ͼ��
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
	
	/**��ʼ���ؼ�*/
	private void initView(){
		musiclistview = (ListView) view.findViewById(R.id.musiclistview);
		list_name = (TextView) view.findViewById(R.id.list_name);
		//���ֱ�ǩ
		txt_default_list = (TextView) view.findViewById(R.id.txt_default_list);
		txt_one_list = (TextView) view.findViewById(R.id.txt_one_list);
//		txt_two_list = (TextView) view.findViewById(R.id.txt_two_list);
	}
	
	/**��ʼ������*/
	private void init(){
		//��ʼ��������
		lad = new ListAdapter(view.getContext());
		//����������
		musiclistview.setAdapter(lad);

		musiclistview.setSelection(PlayMusic.itemId);

		//���������б��������¼�
		musiclistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PlayMusic.itemId = position;
				CommonData.flag_playing_list = PlayMusic.playing_label;
				CommonData.handler_changetutu.sendEmptyMessage(1);
			}
		});
		
		//���ֱ�ǩ�¼�
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
				//ɾ���ǵ�ǰ���ź󲻲���������ˢ���б����
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
				//ɾ���ǵ�ǰ���ź󲻲���������ˢ���б����
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
		
		//���Ĭ���б�Ϊ�գ����ڴ�ʱ���ֻ�������������Ӵ�Ĭ���б�
		if(new MusicListDao(view.getContext()).findAllMusic().isEmpty()){
			showaddAllMusicImg();
		}else{
			if(CommonData.flag_playing_list == 0){
				txt_default_list.performClick();
			}else if(CommonData.flag_playing_list == 1){
				txt_one_list.performClick();
			}
			
		}
	
		//���������������б�
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
		
//		//ȷ��ɾ����ˢ���б�
//		CommonData.handler_deletemusic = new Handler(){
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case 0:
//					//���²�ѯ�б�����
//					notify_all_music();
//					
//					if(PlayMusic.itemId == pos){
//						//ɾ����ǰ���ź�֪ͨ����ر����ֲ�������һ��
//						CommonData.handler_changetutu.sendEmptyMessage(3);
//					}else if(pos < PlayMusic.itemId){
//						PlayMusic.itemId--;
//					}
//					//ɾ���ǵ�ǰ���ź󲻲���������ˢ���б����
//					notifyMusicList();
//					pid = null;
//					break;
//				case 1:
//					showLoadDialog(-150,-220);
//					new Thread(){
//						public void run() {
//							//���
//							new MusicListDao(view.getContext()).clearAllMusic();
//							hand.sendEmptyMessage(1);
//						};
//					}.start();
//					pid = null;
//					//��պ�֪ͨ����ر�����
//					CommonData.handler_changetutu.sendEmptyMessage(4);
//					break;
//				default:
//					pid = null;
//					break;
//				}
//			}
//		};
	}
	
	//����ǰ��ǩ�����ݼ��뵽�����б���
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
		if(new MusicListDao(view.getContext()).findAllMusic().isEmpty()){//���ݿ�Ϊ��
			//�����б��������
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
		}else{//���ݿⲻ��
//			Toast.makeText(view.getContext(), R.string.application_no_music, Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressLint("HandlerLeak")
	//�����б�仯��ˢ���б�
	private Handler hand = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				//���
				notifyMusicList();
				showaddAllMusicImg();
				closeLoadDialog();
				Toast.makeText(view.getContext(), R.string.add_success, Toast.LENGTH_SHORT).show();
				txt_default_list.performClick();
				break;
			case 1:
				//���
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
	
	/**ˢ�������б� */
	public void notifyMusicList(){
		notify_all_music();
		if(lad != null){
			lad.notifyDataSetChanged();
		}
	}
	
	/**
	 * ��ʾ������ֵȴ���
	 */
	private void showLoadDialog(int x,int y){
		if(ld == null){
			ld = new LoadingDialog(false);
			ld.show(getFragmentManager(), null);
		}
	}
	/**
	 * �رյȴ���
	 */
	private void closeLoadDialog(){
		if(ld != null){
			ld.dismiss();
			ld = null;
		}
	}

	//��д������
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
			//һ�е�����
			SdcardMusic sdm = CommonData.all_music.elementAt(position);
			
			if(convertView == null){
				convertView = l.inflate(R.layout.item_musiclist, null);
			}	
			//���ڲ���ʱ�����ؼ�
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
			//���
			TextView listnum = (TextView) convertView.findViewById(R.id.listnum);
			//����
			TextView musicname = (TextView) convertView.findViewById(R.id.musicname);
			//����
			TextView artist = (TextView) convertView.findViewById(R.id.artist);
			//����ʱ��
			TextView duration = (TextView) convertView.findViewById(R.id.duration);
			//ϲ������
			final ImageView imgbtndeletemusic = (ImageView) convertView.findViewById(R.id.imgbtndeletemusic);
			
			listnum.setText((position+1)+"");
			musicname.setText(sdm.getTilte());
			if(sdm.getArtist().equals("<unknown>")){
				artist.setText("δ֪����");
			}else{
				artist.setText(sdm.getArtist());
			}
			if(MyTimeUtils.Ms2mmss(sdm.getDuration()).equals("ʶ�����")){
				duration.setTextColor(Color.RED);
			}else{
				duration.setTextColor(getResources().getColor(R.color.darkgray));
			}
			duration.setText(MyTimeUtils.Ms2mmss(sdm.getDuration()));
			
			if(PlayMusic.playing_label == 1){//��ʾɾ����ť
//				imgbtndeletemusic.setImageResource(R.drawable.recycler);
				imgbtndeletemusic.setImageResource(R.drawable.nopic);
			}else if(PlayMusic.playing_label == 0){//��ʾϲ����ť
				if(sdm.getStatus() == 2){
					imgbtndeletemusic.setImageResource(R.drawable.deletemusic2);
				}else{
					imgbtndeletemusic.setImageResource(R.drawable.deletemusic);
				}
			}

			imgbtndeletemusic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(CommonData.flag_playing_list != 1){//ϲ�����б��ڲ���״̬�������޸�
						if(PlayMusic.playing_label == 1){//ִ��ɾ������
//							if(pid == null){
//								pid = new PromptInfoDialog(
//										String.format(view.getContext().getString(R.string.del_or_not), sdm.getTilte()), 
//										view.getContext().getString(R.string.del_ps), 
//										sdm.getUrl());
//								pos = position;
//								pid.show(getFragmentManager(), null);
//								new MusicListDao(view.getContext()).unlikeMusic(sdm.getId());
//							}
						}else if(PlayMusic.playing_label == 0){//ִ�����Ϊϲ���б����
							CommonData.all_music = new MusicListDao(view.getContext()).findAllMusic();//���¼��������б���������
							SdcardMusic sdm = CommonData.all_music.elementAt(position);
							MusicListDao mmld = new MusicListDao(view.getContext());
							if(sdm.getStatus() == 2){//��ǰ���Ϊϲ��������
								mmld.unlikeMusic(sdm.getId());
								imgbtndeletemusic.setImageResource(R.drawable.deletemusic);
								Toast.makeText(view.getContext(), R.string.delete_like_music, Toast.LENGTH_SHORT).show();
							}else{//��ǰ���Ϊ��ϲ��������
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
