package com.weidian.supportdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends BaseActivity {
    private static final String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name=getResources().getResourceName(R.layout.activity_main);
        String imageView=getResources().getResourceName(R.id.image_view);
        Log.e("name",name);
        Log.e("view",imageView);

        findViewById(R.id.image_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"i am imageView");
            }
        });
        findViewById(R.id.image_view).setTag(R.id.image_view,"abcdef");

    }

}
