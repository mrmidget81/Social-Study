package com.bill.socialstudy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bill.socialstudy.dataobjects.UserObject;
import com.facebook.GraphUser;
import com.facebook.ProfilePictureView;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

public class ProfileActivity extends Activity {

	private ListView listView;
	private ProfilePictureView profPicView;
	private TextView userNameView, collegeNameView;
	private Button doneButton;
	
	private UserObject user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		
		userNameView = (TextView) findViewById(R.id.selection_user_name);
		profPicView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
		listView = (ListView)findViewById(R.id.classes_and_sessions_list);
		collegeNameView = (TextView) findViewById(R.id.collegeName);
		doneButton = (Button) findViewById(R.id.done_button);
		
		Intent i = getIntent();
		int id = i.getIntExtra("id", -1);
		user = new UserObject(id, 1, "Bill", new int[] {}, new int[] {});
		
		final Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			Request req = Request.newMeRequest(session, new Request.GraphUserCallback() {
				
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if (session == Session.getActiveSession()) {
						if (user != null) {
							profPicView.setUserId(user.getId());
							userNameView.setText(user.getName());
						}
					}
				}
			});
			
			Request.executeBatchAsync(req);
		}
		
		collegeNameView.setText("This is a College Name");
		
		//TODO hit it with the DB
		ClassAndSessionArrayAdapter adapter = new ClassAndSessionArrayAdapter(this, new String[] {"And", "Classes", "Too"},
																			new String[] {"Got", "You", "Some", "Sessions"});
		listView.setAdapter(adapter);
		
		doneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				endProfileActivity();
			}
		});
	}

	private void endProfileActivity() {
		finish();
	}
	
}
