package by.bsuir.feedthegarfieldcat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MiniGameActivity extends AppCompatActivity {

    private boolean click1 = false;
    private boolean click2 = false;
    private boolean click3 = false;

    private TextView scoreLabel;
    private TextView tapLabel;

    private MediaPlayer player;
    private ShortSoundEffectPlayer shPlayer;

    private RelativeLayout rLayout;
    private LinearLayout lLayout;

    private ImageView lasagna;
    private ImageView pizza;
    private ImageView shrimp;
    private ImageView medicine;

    private ImageView cat1;
    private ImageView cat2;
    private ImageView cat3;

    private float lSpeed;
    private float pSpeed;
    private float sSpeed;
    private float mSpeed;

    private int screenHeight;

    private float X_INIT_START = -150.0f;
    private float Y_INIT_START;

    private float X1_LINE;
    private float X2_LINE;
    private float X3_LINE;
    private float X_LINES[] = new float[3];

    private boolean action_flg = false;
    private boolean start_flg = false;

    private Timer timer = new Timer();
    private Handler handler = new Handler();

    private float lasagnaX, lasagnaY;
    private float pizzaX, pizzaY;
    private float shrimpX, shrimpY;
    private float medicineX, medicineY;

    private long score = 0;

    private void processCatClick(float catX, float catY, float catWidth, float catHeight){
        float cX1 = catX; float cX2 = catX + catWidth;
        float cY1 = catY + (screenHeight - lLayout.getHeight()); float cY2 = cY1 + catHeight;

        boolean hit = false;

        float lCenterX = lasagnaX + lasagna.getWidth() / 2;
        float lCenterY = lasagnaY + lasagna.getHeight() / 2;


        if (lCenterX >= cX1 && lCenterX <= cX2 &&
            lCenterY >= cY1 && lCenterY <= cY2){
            hit = true;
            score += 50;
        }

        float pCenterX = pizzaX + pizza.getWidth() / 2;
        float pCenterY = pizzaY + pizza.getHeight() / 2;
        if (pCenterX >= cX1 && pCenterX <= cX2 &&
                pCenterY >= cY1 && pCenterY <= cY2){
            hit = true;
            score += 30;
        }

        float sCenterX = shrimpX + shrimp.getWidth() / 2;
        float sCenterY = shrimpY + shrimp.getHeight() / 2;
        if (sCenterX >= cX1 && sCenterX <= cX2 &&
                sCenterY >= cY1 && sCenterY <= cY2){
            hit = true;
            score += 10;
        }

        float mCenterX = medicineX + medicine.getWidth() / 2;
        float mCenterY = medicineY + medicine.getHeight() / 2;
        if (mCenterX >= cX1 && mCenterX <= cX2 &&
                mCenterY >= cY1 && mCenterY <= cY2){
            stopSong();
            shPlayer.playOverSound();

            if (timer != null) {
                timer.cancel();
                timer = null;
            }

            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }

        if (hit){
            shPlayer.playEatSound();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_game);

        shPlayer = new ShortSoundEffectPlayer(this);

        rLayout = this.findViewById(R.id.RelativeLayout);
        lLayout = this.findViewById(R.id.LinearLayout);

        lasagna = this.findViewById(R.id.Lasagna);
        pizza = this.findViewById(R.id.Pizza);
        shrimp = this.findViewById(R.id.Shrimp);
        medicine = this.findViewById(R.id.Medicine);

        cat1 = this.findViewById(R.id.Cat1);
        cat2 = this.findViewById(R.id.Cat2);
        cat3 = this.findViewById(R.id.Cat3);

        scoreLabel = this.findViewById(R.id.ScoreLabel);
        tapLabel = this.findViewById(R.id.TapLabel);


        lasagna.setX(X_INIT_START);
        pizza.setX(X_INIT_START);
        shrimp.setX(X_INIT_START);
        medicine.setX(X_INIT_START);


        cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start_flg){
                    click1 = true;
                }
            }
        });

        cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start_flg){
                    click2 = true;
                }
            }
        });

        cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start_flg){
                    click3 = true;
                }
            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!start_flg) {
            tapLabel.setVisibility(View.INVISIBLE);
            start_flg = true;
                initializeMainLinesX();
                initializeScreenHeight();
            Y_INIT_START = screenHeight;
                initializeSpeeds();
                setAllFoodsTheirXLine();
                setBackAllFoodsY();


            playSong();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);

        }
        return super.onTouchEvent(event);
    }

    private float getRandomLineX(){
        Random rand = new Random();
        return X_LINES[rand.nextInt(3)];
    }

    public void checkCollision(){
        if (click1){
            processCatClick(cat1.getX(), cat1.getY(), cat1.getWidth(), cat1.getHeight());
            click1 = false;
        }

        if (click2){
            processCatClick(cat2.getX(), cat2.getY(), cat2.getWidth(), cat2.getHeight());
            click2 = false;
        }

        if (click3){
            processCatClick(cat3.getX(), cat3.getY(), cat3.getWidth(), cat3.getHeight());
            click3 = false;
        }
    }

    public void changePos() {
        if (start_flg) {

            checkCollision();

            lasagnaY += lSpeed;
            if (lasagnaY > screenHeight) {
                lasagnaX = getRandomLineX();
                lasagnaY = -150.0f;
            }
            lasagna.setX(lasagnaX);
            lasagna.setY(lasagnaY);


            pizzaY += pSpeed;
            if (pizzaY > screenHeight) {
                pizzaX = getRandomLineX();
                pizzaY = -150.0f;
            }
            pizza.setX(pizzaX);
            pizza.setY(pizzaY);


            shrimpY += sSpeed;
            if (shrimpY > screenHeight) {
                shrimpX = getRandomLineX();
                shrimpY = -150.0f;
            }
            shrimp.setX(shrimpX);
            shrimp.setY(shrimpY);


            medicineY += mSpeed;
            if (medicineY > screenHeight) {
                medicineX = getRandomLineX();
                medicineY = -150.0f;
            }
            medicine.setX(medicineX);
            medicine.setY(medicineY);
        }

        scoreLabel.setText("Score: " + String.valueOf(score));
    }

    private void initializeScreenHeight(){
        screenHeight = rLayout.getHeight();
    }

    private void initializeSpeeds(){
        lSpeed = Math.round(screenHeight / 50.0);
        pSpeed = Math.round(screenHeight / 60.0);
        sSpeed = Math.round(screenHeight / 70.0);
        mSpeed = Math.round(screenHeight / 65.0);
    }

    private void initializeMainLinesX(){
        float centerOffset = shrimp.getWidth() / 2;
        X1_LINE = cat1.getX() + cat1.getWidth() / 2 - centerOffset; X_LINES[0] = X1_LINE;
        X2_LINE = cat2.getX() + cat2.getWidth() / 2 - centerOffset; X_LINES[1] = X2_LINE;
        X3_LINE = cat3.getX() + cat3.getWidth() / 2 - centerOffset; X_LINES[2] = X3_LINE;
    }

    private void setBackAllFoodsY(){
        lasagna.setY(Y_INIT_START);
        pizza.setY(Y_INIT_START);
        shrimp.setY(Y_INIT_START);
        medicine.setY(Y_INIT_START);
    }

    private void setAllFoodsTheirXLine(){
        lasagna.setX(getRandomLineX());
        pizza.setX(getRandomLineX());
        shrimp.setX(getRandomLineX());
        medicine.setX(getRandomLineX());

        lasagnaX = lasagna.getX();
        pizzaX = pizza.getX();
        shrimpX = shrimp.getX();
        medicineX = medicine.getX();
    }

    public void playSong() {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.catsong);
            player.setLooping(true);
        }

        player.start();
    }

    public void pause(View v) {
        if (player != null) {
            player.pause();
        }
    }

    public void stopSong() {
        stopPlayer();
    }

    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
}