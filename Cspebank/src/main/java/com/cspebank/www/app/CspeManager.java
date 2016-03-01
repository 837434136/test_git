package com.cspebank.www.app;

import android.app.Activity;

import com.cspebank.www.utils.LogTrace;

import java.util.LinkedList;
import java.util.List;

/**
 * @author yisinian.deng
 * 2015.11.18
 */
public class CspeManager {
	
	public static boolean isRunSynchroData = true;
	public static boolean isProcessCreFromKill;
	private static List<Activity> mActivites = new LinkedList<Activity>();

    public CspeManager() {
    	
    }

    /**
     * return mActivites size
     * @return
     */
    public int size(){
    	return mActivites.size();
	}

    /**
     * get forward activity
     * @return
     */
    public synchronized Activity getForwardActivity(){
        return size() > 0 ? mActivites.get(size() - 1) : null;
    }

    /**
     * add activity
     * @param activity
     */
    public synchronized void addActivity(Activity activity){
        mActivites.add(activity);
    }

    /**
     * remove activity
     * @param activity
     */
    public synchronized void removeActivity(Activity activity){
        if (mActivites.contains(activity)){
            mActivites.remove(activity);
        }
    }

    public synchronized void clear(){
        int activitySize = mActivites.size();
        LogTrace.d("activitySize is " + activitySize);
        for (int i = activitySize - 1; i > -1; i--){
        	
            Activity activity = mActivites.get(i);
            removeActivity(activity);
            activity.finish();
        }
    }

    public synchronized void clearTop(){
        int activitySize = mActivites.size();
        for (int i = activitySize - 2; i > -1; i--){
            Activity activity = mActivites.get(i);
            removeActivity(activity); 
            activity.finish();
        }
    }
	
}
