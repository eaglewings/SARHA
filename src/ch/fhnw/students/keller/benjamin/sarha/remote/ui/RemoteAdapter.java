package ch.fhnw.students.keller.benjamin.sarha.remote.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import ch.fhnw.students.keller.benjamin.sarha.comm.DeviceModel;

public class RemoteAdapter extends BaseAdapter{
	private ViewStock viewStock;
	private int count;
	
	public RemoteAdapter(Context context, DeviceModel deviceModel){
		System.out.println("hello wörld from adapter");
		viewStock = new ViewStock(context, deviceModel);
		count= deviceModel.getConfig().count();
		
	}
	
	@Override
	public int getCount() {
		
		return count;
	}

	@Override
	public Object getItem(int pos) {
		
		return viewStock.getItem(pos);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup viewGroup) {
		System.out.println("adapter getView");
		return viewStock.getView(pos);
	}

}
