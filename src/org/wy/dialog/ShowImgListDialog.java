package org.wy.dialog;

import java.util.Vector;

import org.wy.activity.MainActivity;
import org.wy.utils.CommonData;
import org.wy.utils.SaveUtils;

import android.app.DialogFragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.wy.activity.R;

public class ShowImgListDialog extends DialogFragment {

	private View view;
	private Gallery gallery;
	private String setwhichback = "main";

	private Vector<Integer> myImageIds = new Vector<Integer>();
	
	public ShowImgListDialog() {
		
	}
	
	/**
	 * @param myImageIds ͼƬid��vector������������
	 */
	public ShowImgListDialog(Vector<Integer> myImageIds,String setwhichback) {
		this.myImageIds = myImageIds;
		this.setwhichback = setwhichback;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_FRAME, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.dialog_backshow, container, false);
		init();
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogPrompt;
		getDialog().setCanceledOnTouchOutside(true);
	}

	private void init() {
		
		// װ��Gallery���
		gallery = (Gallery) view.findViewById(R.id.gallery);
		// ������������ͼ�����ݵ�ImageAdapter����
		ImageAdapter imageAdapter = new ImageAdapter(view.getContext());
		// ����Gallery�����Adapter����
		gallery.setAdapter(imageAdapter);
		
		gallery.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(setwhichback.equals("main")){
					Bundle b = new Bundle();
					b.putInt("imgresid", myImageIds.elementAt(position));
					Message msg = new Message();
					msg.what = 5;
					msg.setData(b);
					CommonData.handler_changetutu.sendMessage(msg);
				}else if(setwhichback.equals("wave")){
					SaveUtils.setWaveBackData(myImageIds.elementAt(position));
					Toast.makeText(view.getContext(), R.string.action_settings_success,Toast.LENGTH_LONG).show();
				}
				return false;
				
			}
		});
	}
	
	

	public class ImageAdapter extends BaseAdapter {

		private int mGalleryItemBackground;
		private Context context;

		public ImageAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
			// ���Gallery���������
			TypedArray typedArray = context.obtainStyledAttributes(R.styleable.Gallery);
			mGalleryItemBackground = typedArray.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
			typedArray.recycle();/* �ö����styleable�����ܹ�����ʹ�� */  
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return myImageIds.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(context);
			/* �趨ͼƬ��imageView���� */
			i.setImageResource(myImageIds.elementAt(position%myImageIds.size()));
			i.setId(myImageIds.elementAt(position%myImageIds.size()));
			i.setScaleType(ImageView.ScaleType.FIT_XY); /* �����趨ͼƬ�Ŀ�� */
			i.setLayoutParams(new Gallery.LayoutParams(800, 450)); /* �����趨Layout�Ŀ�� */
			i.setBackgroundResource(mGalleryItemBackground); /* �趨Gallery����ͼ */
			return i;
		}

	}

}
