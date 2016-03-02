package com.weidian.supportdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by fengcunhan on 16/3/2.
 */
public class BaseActivity extends AppCompatActivity {
    private final String TAG=this.getClass().getSimpleName();
    private TraceManager mTraceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTraceManager = TraceManager.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        long time=System.currentTimeMillis();
        setClickListener(findViewById(android.R.id.content));
        Log.e("cost time",(System.currentTimeMillis()-time)+"");
    }

    public void onClick(View view){
        Intent intent=new Intent();
        intent.setClass(this,AActivity.class);
        switch (view.getId()){
            case R.id.button_a:
                intent.putExtra("name","a");
                break;
            case R.id.button_b:
                intent.putExtra("name","b");
                break;
            case R.id.button_c:
                intent.putExtra("name","c");
                break;
            case R.id.button_d:
                intent.putExtra("name","d");
                break;
        }
        startActivity(intent);
    }

    private void setClickListener(final View view) {
        if (null != view) {
            boolean hasOnClick = view.hasOnClickListeners();
            if (hasOnClick) {
                final View.OnClickListener listener = getOnClickListener(view);
                int id = view.getId();
                String name = getResources().getResourceName(id);
                //AdapterView不能设置OnClickListener
                if (!(view instanceof AdapterView)) {
                    //检查是否在配置文件中,请求OnClickListener没替换过
                    if (!(listener instanceof DelegentOnClickListener) &&
                            mTraceManager.isNeedTrace(this.getClass().getName(), name)) {
                        view.setOnClickListener(new DelegentOnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e(TAG, "new clickListener");
                                if (null != listener) {
                                    listener.onClick(v);
                                }
                                Log.e(TAG, v.getTag(v.getId()) + "");
                            }
                        });
                    }
                }
            }
            if (view instanceof ViewGroup) {
                final ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                if (childCount > 0) {
                    for (int i = 0; i < childCount; i++) {
                        View childView = viewGroup.getChildAt(i);
                        setClickListener(childView);
                    }
                }
            }
        }

    }


    private View.OnClickListener getOnClickListener(View view) {
        boolean hasOnClick = view.hasOnClickListeners();
        if (hasOnClick) {
            try {
                Method method = View.class.getDeclaredMethod("getListenerInfo", null);
                if (null != method) {
                    method.setAccessible(true);
                    Object object = method.invoke(view, new Object[]{});
                    if (null != object) {
                        Class listenerInfoClazz = object.getClass();
                        Field mOnClickListener = listenerInfoClazz.getDeclaredField("mOnClickListener");
                        if (null != mOnClickListener) {
                            mOnClickListener.setAccessible(true);
                            Object listener = mOnClickListener.get(object);
                            if (null != listener && listener instanceof View.OnClickListener) {
                                return (View.OnClickListener) listener;
                            }
                        }
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

