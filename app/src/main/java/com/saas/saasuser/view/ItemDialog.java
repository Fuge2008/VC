

package com.saas.saasuser.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.saas.saasuser.R;
import com.saas.saasuser.util.StringUtils;

/**通用纵向Item选项dialog
 */
public class ItemDialog extends Dialog implements OnItemClickListener {
//	private static final String TAG = "ItemDialog";  

	public interface OnDialogItemClickListener {

		/**点击item事件的回调方法
		 * @param requestCode 传入的用于区分某种情况下的showDialog
		 * @param position
		 * @param item
		 */
		void onDialogItemClick(int requestCode, int position, String item);
	}

	private Context context;
	private String[] items;
	private String title; 

	private int requestCode;
	private OnDialogItemClickListener listener;
	/** 
	 * 带监听器参数的构造函数 
	 */  
	public ItemDialog(Context context, String[] items,
			int requestCode, OnDialogItemClickListener listener) {
		this(context, items, null, requestCode, listener);
	}
	public ItemDialog(Context context, String[] items, String title,
			int requestCode, OnDialogItemClickListener listener) {
		super(context, R.style.MyDialog);

		this.context = context;
		this.items = items;
		this.title = title;
		this.requestCode = requestCode;
		this.listener = listener;  
	}

	private TextView tvItemDialogTitle;
	private ListView lvItemDialog;
	private ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.item_dialog); 
		setCanceledOnTouchOutside(true);

		tvItemDialogTitle = (TextView) findViewById(R.id.tvItemDialogTitle);
		lvItemDialog = (ListView) findViewById(R.id.lvItemDialog);

		tvItemDialogTitle.setVisibility(StringUtils.isNotEmpty(title, true) ? View.VISIBLE : View.GONE);
		tvItemDialogTitle.setText("" + StringUtils.getCurrentString());
		
		adapter = new ArrayAdapter<String>(context, R.layout.item_dialog_item, items);
		lvItemDialog.setAdapter(adapter);
		lvItemDialog.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (listener != null) {
			listener.onDialogItemClick(requestCode, position, adapter == null ? null : adapter.getItem(position));
		}
		dismiss();  
	}

}

