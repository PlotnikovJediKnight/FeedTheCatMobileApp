package by.bsuir.feedthegarfieldcat;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        for (int appWidgetId : appWidgetIds){

            long value = sharedPrefs.getLong("LAST_SCORE", 0);
            String user = sharedPrefs.getString("USER", "");
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            remoteViews.setTextViewText(R.id.widget_text_view, "User " + user + "\n got " + value + " score!");
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
