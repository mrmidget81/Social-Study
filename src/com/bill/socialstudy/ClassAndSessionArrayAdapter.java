package com.bill.socialstudy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ClassAndSessionArrayAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] classes, sessions;
	
	public ClassAndSessionArrayAdapter(Context context, String[] classes, String[] sessions) {
		super(context, R.layout.classrow, concatArrays(classes, sessions));
		this.context = context;
		this.classes = classes;
		this.sessions = sessions;
	}

	private static String[] concatArrays(String[] classes, String[] sessions) {
		String[] both = new String[classes.length + sessions.length];
		System.arraycopy(classes, 0, both, 0, classes.length);
		System.arraycopy(sessions, 0, both, classes.length, sessions.length);
		return both;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (position < sessions.length) {
			View sessionView = inflater.inflate(R.layout.sessionrow, parent, false);
			
			TextView textview = (TextView) sessionView.findViewById(R.id.sessionName);
			textview.setText(sessions[position]);
			
			return sessionView;
		} else {
			View rowView = inflater.inflate(R.layout.classrow, parent, false);
			
			TextView textview = (TextView) rowView.findViewById(R.id.className);
			textview.setText(classes[position - sessions.length]);
			
			return rowView;
		}
	}
}
