package com.bill.socialstudy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bill.socialstudy.database.DatabaseHelper;
import com.facebook.FacebookActivity;
import com.facebook.GraphUser;
import com.facebook.LoginFragment;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;

public class SocialStudyMainActivity extends FacebookActivity {

	private static final int SPLASH = 0;
	private static final int HOME = 1;
	private static final int LOGOUT = 2;
	private static final int CREATE_PROF = 3;
	private static final int FRAGMENT_COUNT = CREATE_PROF + 1;
	
	private static final String FRAGMENT_PREFIX = "fragment";
	private static final String TAG = "Social Study";
	
	
	private boolean restoredFragment = false;
	private boolean isResumed = false;
	private boolean userexists = false;
	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
	
	private MenuItem logout;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_study_main);
        
        for (int i = 0; i < fragments.length; i++) {
        	restoreFragment(savedInstanceState, i);
        }
    }

    private String getBundleKey(int index) {
    	return FRAGMENT_PREFIX + index;
    }
    
	private void restoreFragment(Bundle savedInstanceState, int fragmentIndex) {
		Fragment fragment = null;
		if (savedInstanceState != null) {
			FragmentManager manager = getSupportFragmentManager();
			fragment = manager.getFragment(savedInstanceState, getBundleKey(fragmentIndex));
		}
		
		if (fragment != null) {
			fragments[fragmentIndex] = fragment;
			restoredFragment = true;
		} else {
			switch (fragmentIndex) {
			case SPLASH:
				fragments[SPLASH] = new SplashFragment();
				break;
			case HOME:
				fragments[HOME] = new HomeFragment();
				break;
			case LOGOUT:
				fragments[LOGOUT] = new LoginFragment();
				break;
			case CREATE_PROF:
				fragments[CREATE_PROF] = new SplashFragment();
				break;
			default:
				Log.w(TAG, "invalid fragment index");
				break;
			}
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		isResumed = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isResumed = true;
	}
	
	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		
		Session session = Session.getActiveSession();
		if (session == null || session.getState().isClosed()) {
			session = new Session(this);
			Session.setActiveSession(session);
		}
		
		FragmentManager manager = getSupportFragmentManager();
		
		if (restoredFragment) {
			return;
		}
		
		if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
			session.openForRead(this);
		} else if (session.isOpened()) {
			Fragment fragment = manager.findFragmentById(R.id.body_frame);
			if (!userexists && !(fragment instanceof CreateProfileFragment)) {
				manager.beginTransaction().replace(R.id.body_frame, fragments[CREATE_PROF]).commit();
			}
			else if (userexists && !(fragment instanceof HomeFragment)) {
				manager.beginTransaction().replace(R.id.body_frame, fragments[HOME]).commit();
			}
		} else {
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.body_frame, fragments[SPLASH]).commit();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		FragmentManager manager = getSupportFragmentManager();
		Fragment f = manager.findFragmentById(R.id.body_frame);
		for (int i = 0; i < fragments.length; i++) {
			if (fragments[i] == f) {
				manager.putFragment(outState, getBundleKey(i), fragments[i]);
			}
		}
	}

	@Override
	protected void onSessionStateChange(SessionState state, Exception exception) {
		if (isResumed) {
			FragmentManager manager = getSupportFragmentManager();
			int backStackSize = manager.getBackStackEntryCount();
			for (int i = 0; i < backStackSize; i++)
				manager.popBackStack();
			
			if (state.isOpened()) {
				final Session session = Session.getActiveSession();

				if (session != null && session.isOpened()) {
					Request req = Request.newMeRequest(session, new Request.GraphUserCallback() {
						
						@Override
						public void onCompleted(GraphUser user, Response response) {
							if (session == Session.getActiveSession()) {
								if (user != null) {
									loggedIn(Integer.parseInt(user.getId()));
								}
							}
						}
					});
					
					Request.executeBatchAsync(req);
				}
				
			} else if (state.isClosed()) {
				FragmentTransaction trans = manager.beginTransaction();
				trans.replace(R.id.body_frame, fragments[SPLASH]).commit();
			}
		}
	}

	protected void loggedIn(int userid) {
		FragmentManager manager = getSupportFragmentManager();
		
		if (DatabaseHelper.userExists(userid)) {
			userexists = true;
			FragmentTransaction trans = manager.beginTransaction();
			trans.replace(R.id.body_frame, fragments[HOME]).commit();
		} else {
			userexists = false;
			FragmentTransaction trans = manager.beginTransaction();
			trans.replace(R.id.body_frame, fragments[CREATE_PROF]).commit();
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		FragmentManager manager = getSupportFragmentManager();
		Fragment current = manager.findFragmentById(R.id.body_frame);
		
		if (current == fragments[HOME]) {
			if (menu.size() == 0) {
				logout = menu.add(R.string.logout);
			}
			return true;
		} else {
			menu.clear();
			logout = null;
		}
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.equals(logout)) {
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction trans = manager.beginTransaction();
			trans.add(R.id.body_frame, fragments[LOGOUT]).addToBackStack(null).commit();
			return true;
		}
		return false;
	}
	
	
}
