package com.bill.socialstudy;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ClassInfoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_class_info, menu);
        return true;
    }
}
