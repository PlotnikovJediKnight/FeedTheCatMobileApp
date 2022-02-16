package by.bsuir.feedthegarfieldcat;

import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreLabel = findViewById(R.id.scoreLabel);
        TextView highScoreLabel = findViewById(R.id.highScoreLabel);

        long score = getIntent().getLongExtra("SCORE", 0);
        scoreLabel.setText(getString(R.string.result_score, score));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER", MainActivity.curUser.getUserName());
        editor.putLong("LAST_SCORE", score);
        editor.apply();

        Intent intent = new Intent(this, AppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), AppWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

        long highScore = MainActivity.curUser.getHighScore();
        MainActivity.curUser.AddNewGame(new UserInfo.GameInfo(new Date(), score));


        if (score > highScore) {
            MainActivity.curUser.setHighScore(score);
            highScoreLabel.setText(getString(R.string.high_score, score));

        } else {
            highScoreLabel.setText(getString(R.string.high_score, highScore));
        }

        processAchievement();
        Database.Instance().saveDatabaseToFile();
    }

    public void tryAgain(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onBackPressed() {}

    private static final int[] array = {100, 500, 1000, 1500, 2000, 2500, 3000, 5000, 10000, 100000};
    private void processAchievement(){
        MainActivity.curUser.getUserAchievement().clear();
        for (int i = 0; i < array.length; ++i){
            if (MainActivity.curUser.getHighScore() >= array[i]){
                MainActivity.curUser.AddNewAchievement(Database.achievements.get(i));
            }
        }
    }

}