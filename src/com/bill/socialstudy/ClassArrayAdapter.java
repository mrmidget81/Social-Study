package com.bill.socialstudy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ClassArrayAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] values;
	
	public ClassArrayAdapter(Context context, String[] values) {
		super(context, R.layout.classrow, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.classrow, parent, false);
		
		TextView textview = (TextView) rowView.findViewById(R.id.className);
		textview.setText(values[position]);
		
		return rowView;
	}

}
