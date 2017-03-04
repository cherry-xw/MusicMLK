package org.wy.dialog;

import org.wy.db.MusicListDao;
import org.wy.utils.CommonData;

import com.wy.activity.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SeeSelectImageDialog extends DialogFragment {
	
	private View view;
	private int imgid = 0;
	private ImageView seeelectimg;
	
	public SeeSelectImageDialog() {
		// TODO Auto-generated constructor stub
	}
	
	public SeeSelectImageDialog(int imgid){
		this.imgid = imgid;
	}
	
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
		getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogPrompt;
		getDialog().setCanceledOnTouchOutside(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.dialog_see_select_image, null);
		seeelectimg = (ImageView) view.findViewById(R.id.seeelectimg);
		if(imgid != 0){
			seeelectimg.setImageResource(imgid);
		}
		return view;
	}
	
}
