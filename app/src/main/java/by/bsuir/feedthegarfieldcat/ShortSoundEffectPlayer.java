package by.bsuir.feedthegarfieldcat;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class ShortSoundEffectPlayer {
    private static SoundPool soundPool;
    private static int eatSound, overSound;

    ShortSoundEffectPlayer(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // SoundPool is deprecated in API level 21.(Lollipop)
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(2)
                    .build();
        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        eatSound = soundPool.load(context, R.raw.eatsound, 1);
        overSound = soundPool.load(context, R.raw.gameover, 1);
    }

    void playEatSound() {
        soundPool.play(eatSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    void playOverSound() {
        soundPool.play(overSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
