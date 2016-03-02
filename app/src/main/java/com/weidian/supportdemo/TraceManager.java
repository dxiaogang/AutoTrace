package com.weidian.supportdemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengcunhan on 16/3/2.
 */
public class TraceManager {
    private  List<TraceInfo> mTraceInfos;
    private static TraceManager INSTANCE;
    private static Object Lock=new Object();

    public static TraceManager getInstance(Context context){
        synchronized (Lock){
            if(null==INSTANCE){
                INSTANCE=new TraceManager(context);
            }
            return INSTANCE;
        }
    }

    public TraceManager(Context context){
        AssetManager manager=context.getAssets();
        try {
            InputStream inputStream= manager.open("trace.json");
            int length=inputStream.available();
            byte[] buffer=new byte[length];
            inputStream.read(buffer);
            inputStream.close();
            JSONObject jsonObject=new JSONObject(new String(buffer));
            JSONArray traceArray=jsonObject.optJSONArray("trace");
            if(null!=traceArray && traceArray.length()>0){
                mTraceInfos=new ArrayList<>();
                for(int i=0;i<traceArray.length();i++){
                    JSONObject object=traceArray.getJSONObject(i);
                    if(null!=object){
                        TraceInfo info=new TraceInfo();
                        info.activity=object.optString("activity","");
                        String ids=object.optString("ids","");
                        if(!TextUtils.isEmpty(ids)){
                            info.ids=ids.split(",");
                        }
                        mTraceInfos.add(info);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String[] getIdsByActivityName(String name){
        if(null!=mTraceInfos && mTraceInfos.size()>0){
            for(TraceInfo info:mTraceInfos){
                if(TextUtils.equals(info.activity,name)){
                    return info.ids;
                }
            }
        }
        return null;
    }

    public boolean isNeedTrace(String activityName,String viewName){
        if(TextUtils.isEmpty(viewName)){
            return false;
        }
        String[] ids=getIdsByActivityName(activityName);
        if(null!=ids && ids.length>0){
            for(String id:ids){
                if(viewName.endsWith(id)){
                    return true;
                }
            }
        }
        return false;
    }
    public static class TraceInfo{
        public String activity;
        public String[] ids;
    }
}
