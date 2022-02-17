package by.bsuir.feedthegarfieldcat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    public static UserInfo curUser;

    private TextView greetLabel;
    Button signOut;
    GoogleSignInClient mGoogleSignInClient;

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Signed out", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.helpItem:{
                Intent intent = new Intent(getApplicationContext(), ReferenceActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.dotsItem:{
                //Toast.makeText(this, "dots", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        greetLabel = this.findViewById(R.id.GreetingLabel);


        System.err.println(getFilesDir().toPath().toString());


        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signOut = findViewById(R.id.button_sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.button_sign_out:
                        signOut();
                        break;
                }
            }
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName().split(" ")[0];
            String personId = acct.getId();

            curUser = Database.Instance().getUser(personId);

            if (curUser == null){
                curUser =
                        Database.Instance().putNewUser(personId,
                        new UserInfo(
                                personName,
                                new ArrayList<UserInfo.GameInfo>(),
                                new ArrayList<UserInfo.Achievement>()
                        ));
            }

            greetLabel.setText(getString(R.string.greeting_string, curUser.getUserName()));
        }



        GifImageView cat = (GifImageView) this.findViewById(R.id.CatAnimationIcon);
        cat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motion) {
                ((GifDrawable) cat.getDrawable()).setLoopCount(1);
                ((GifDrawable) cat.getDrawable()).reset();
                return true;
            }
        });

        ((GifDrawable) cat.getDrawable()).stop();

        View fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                Snackbar.make(view,
                        "Developer: Plotnikov V. V. Group: 951005\nAssignment #1 overseen by: Petrovskaya V. V.",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
        );

        Button button = this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "My score in FeedTheCat has reached " + String.valueOf(curUser.getHighScore()) + "! Try to beat me, if you can...";
                String shareSub = "FeedTheCat";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        });

        Button feedButton = this.findViewById(R.id.feedButton);
        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MiniGameActivity.class);
                startActivity(intent);
            }
        });

        View historyButton = this.findViewById(R.id.history_button);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
                startActivity(intent);
            }
        });

        View achievButton = this.findViewById(R.id.achievements_button);
        achievButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AchievementActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        signOut();
    }
}