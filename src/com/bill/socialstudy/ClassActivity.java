package com.bill.socialstudy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import com.bill.socialstudy.dataobjects.ClassObject;

public class ClassActivity extends Activity {

	private ClassObject myClass;
	private TextView nameView;
	private ListView sessionList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        
        Intent i = getIntent();
        int id = i.getIntExtra("id", 0);
        //TODO get from DB
        myClass = new ClassObject(id, "Radiation, Superhuman Strength, and You!", "Proffesor Banner", new int[] {});
        
        nameView = (TextView) findViewById(R.id.textView1);
        nameView.setText(myClass.getName());
        
        sessionList = (ListView) findViewById(R.id.listView1);
        sessionList.setAdapter(new SessionArrayAdapter(this, new String[] {"n00bs", "pros", "pwners", "Josh", "bitches ain't shit but hoes n tricks"}));
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_class, menu);
        return true;
    }
}
