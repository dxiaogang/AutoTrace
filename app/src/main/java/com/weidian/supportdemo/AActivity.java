package com.weidian.supportdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by fengcunhan on 16/3/2.
 */
public class AActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a);
        TextView tv=(TextView)findViewById(R.id.textView);
        tv.setText(getIntent().getStringExtra("name"));

        findViewById(R.id.button_a).setTag(R.id.button_a,"this is button a");
    }

}
