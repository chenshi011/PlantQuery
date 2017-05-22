package com.android.cs.plantquery.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.cs.plantquery.R;

public class ChoosePicDialog extends Dialog {
	onItemClickListener onItemClickListener;
	public void setOnItemClickListener(onItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public ChoosePicDialog(Activity activity) {
		super(activity, R.style.transparentFrameWindowStyle);
		init(activity);
	}

	private void init(Activity activity) {
		View view = getLayoutInflater().inflate(R.layout.plant_add_pic_dialog,
				null);
		this.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		Window window = this.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.animationPopup);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = activity.getWindowManager().getDefaultDisplay().getHeight();
		// 设置显示位置
		this.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		this.setCanceledOnTouchOutside(true);

		String[] list = activity.getResources().getStringArray(
				R.array.add_pic_alert);
		ButtonAdapter adapter = new ButtonAdapter(activity, list);
		ListView listView = (ListView) view.findViewById(R.id.list_pic_item);
		listView.setAdapter(adapter);
	}

	class ButtonAdapter extends BaseAdapter {
		private String[] list;
		private Context context;

		public void setList(String[] list) {
			this.list = list;
		}

		public String[] getList() {
			return list;
		}

		public ButtonAdapter(Context context, String[] list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Holder holder = null;
			final String text = list[position];
			if (convertView == null) {
				holder = new Holder();
				convertView = new Button(context);
				holder.btn = (Button) convertView;
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(onItemClickListener!=null){
						onItemClickListener.onClick(v,position);
					}
				}
			});
			holder.btn.setText(text);
			return convertView;
		}

		class Holder {
			public Button btn;
		}
	}
	
	public interface onItemClickListener{
		void onClick(View v,int pos);
	}
}
