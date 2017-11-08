package com.example.btholmes.scavenger11.model;

import android.content.Context;
import android.util.Log;

import com.example.btholmes.scavenger11.data.Constant;
import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.HashMap;




public class Analytics {

    private Context context;
    private GoogleAnalytics googleAnalytics;

    public Analytics(Context context){
        this.context = context;
        googleAnalytics = GoogleAnalytics.getInstance(context);
        googleAnalytics.setLocalDispatchPeriod(-1);
    }

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     * <p/>
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public static enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
    }

    private static final long NO_EVENT_VALUE = -1;

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<>();

    private synchronized Tracker getTracker(TrackerName name ) {
        if (!mTrackers.containsKey(name)) {
            String trackingId = context.getSharedPreferences(Constant.DEFAULT_SHARED_PREF, Context.MODE_PRIVATE).getString(Constant.ANALYTICS_KEY, "");
            Tracker tracker = googleAnalytics.newTracker(trackingId);
            tracker.setSampleRate(100.0);
            tracker.setSessionTimeout(2000);
            tracker.enableAutoActivityTracking(false);
            tracker.setAnonymizeIp(true);
            tracker.enableExceptionReporting(true);
            tracker.enableAutoActivityTracking(true);
            mTrackers.put(name, tracker);

            // This reports the entire stack trace to Google Analytics
            ArrayList<String> packages = new ArrayList<>();
            packages.add("edu.iastate.MyState");
            ExceptionReporter reporter = new ExceptionReporter(tracker, Thread.getDefaultUncaughtExceptionHandler(),context);
//            reporter.setExceptionParser(new AnalyticsExceptionParser(context, packages));
            Thread.setDefaultUncaughtExceptionHandler(reporter);
        }
        return mTrackers.get(name);
    }

    public boolean analyticsAllowed() {
        if (context==null) return false;
//        SharedPreferences sharedPref = context.getSharedPreferences(MainActivity.DEFAULT_SHARED_PREF, Context.MODE_PRIVATE);
//        return sharedPref.getBoolean(MyAccountFragment.ANALYTICS_PREFERENCE, true) && !BuildConfig.IS_DEBUG;
        return true;
    }

    /**
     * public method used to report custom activity to googleAnalytics.
     * Default reporting only reports the fragment title, so additional
     * reporting might be useful, e.g. Reporting "Sports" when a user
     * goes into the "Sports" section of news.
     *
     * @param name - String representing section of app user is in.
     */
    public void reportScreen(String name) {
        if (!analyticsAllowed()) return;

        Tracker tracker = getTracker(TrackerName.APP_TRACKER);
        tracker.setScreenName(name);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        googleAnalytics.dispatchLocalHits();

        Log.d("Analytics: Screen",name);

    }

    public void reportEvent(String eventCategory, String eventAction) {
        reportEvent(eventCategory,eventAction,null);
    }

    public void reportEvent(String eventCategory, String eventAction, String eventLabel) {
        reportEvent(eventCategory,eventAction,eventLabel,NO_EVENT_VALUE);
    }

    public void reportEvent(String eventCategory, String eventAction, String eventLabel, long eventValue) {

        if (!analyticsAllowed()) return;

        HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder()
                .setCategory(eventCategory)
                .setAction(eventAction);

        if(eventLabel!=null) eventBuilder.setLabel(eventLabel);
        if(eventValue!=NO_EVENT_VALUE) eventBuilder.setValue(eventValue);

        Tracker tracker = getTracker(TrackerName.APP_TRACKER);
        tracker.send(eventBuilder.build());
        googleAnalytics.dispatchLocalHits();

        Log.d("Analytics: Event",eventAction);

    }

}
