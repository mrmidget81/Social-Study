package com.bill.socialstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bill.socialstudy.dataobjects.ClassObject;
import com.facebook.GraphUser;
import com.facebook.ProfilePictureView;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

public class HomeFragment extends Fragment {
	
	private ProfilePictureView profPicView;
	private TextView userNameView;
	private int user_id;
	
	private ClassObject[] classes = new ClassObject[] {new ClassObject(251, "two fifty fun", "*cough*", new int[] {})};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home, container, false);
		
		profPicView = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
		profPicView.setCropped(true);
		
		userNameView = (TextView) view.findViewById(R.id.selection_user_name);
		
		final Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			Request req = Request.newMeRequest(session, new Request.GraphUserCallback() {
				
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if (session == Session.getActiveSession()) {
						if (user != null) {
							profPicView.setUserId(user.getId());
							userNameView.setText(user.getName());
							user_id = Integer.parseInt(user.getId());
						}
					}
				}
			});
			
			Request.executeBatchAsync(req);
		}
		
		OnClickListener profClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showProfile();
			}
		};
		view.findViewById(R.id.user_info_section).setOnClickListener(profClickListener);
		
		ListView classListView = (ListView) view.findViewById(R.id.classListView);
		String[] classNames = new String[] {"15-251", "15-150", "21-355", "lol", "rofl", "yo mama", "is so fat", "she weighs like","50000 pounds"};
		
		ClassArrayAdapter adapter = new ClassArrayAdapter(this.getActivity(), classNames);
		classListView.setAdapter(adapter);
		
		classListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				openClass(position);
			}
		});
		
		return view;
	}
	
	private void showProfile() {
		Intent i = new Intent(this.getActivity(), ProfileActivity.class);
		i.putExtra("id", user_id);
		startActivity(i);
	}

	private void openClass(int index) {
		Intent i = new Intent(this.getActivity(), ClassActivity.class);
		i.putExtra("Class_id", classes[index].getId());
		startActivity(i);
	}
}
