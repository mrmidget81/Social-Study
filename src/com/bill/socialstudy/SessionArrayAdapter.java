package com.bill.socialstudy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SessionArrayAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] values;
	
	public SessionArrayAdapter(Context context, String[] values) {
		super(context, R.layout.sessionrow, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.sessionrow, parent, false);
		
		TextView textview = (TextView) rowView.findViewById(R.id.sessionName);
		textview.setText(values[position]);
		
		return rowView;
	}

}
