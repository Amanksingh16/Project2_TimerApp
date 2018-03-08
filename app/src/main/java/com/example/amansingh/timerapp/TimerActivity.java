package com.example.amansingh.timerapp;

import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TimerActivity extends AppCompatActivity {

    SeekBar seek;
    int progressChangedValue = 0;
    TextView text;
    Button start;
    Button stop;
    CountDownTimer timer;
    Calendar calender;
    SimpleDateFormat simpleDateFormat;
    String Date;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);


        start = (Button)findViewById(R.id.start_stop_button);
        stop = (Button)findViewById(R.id.stop);

        stop.setEnabled(false);

        text = (TextView)findViewById(R.id.time_text_view);
        seek = (SeekBar) findViewById(R.id.timer_seekbar);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 progressChangedValue = progress;
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(progressChangedValue*1000), TimeUnit.MILLISECONDS.toMinutes(progressChangedValue*1000) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(progressChangedValue*1000)), TimeUnit.MILLISECONDS.toSeconds(progressChangedValue*1000) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(progressChangedValue*1000)));
                text.setText(hms);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(progressChangedValue*1000), TimeUnit.MILLISECONDS.toMinutes(progressChangedValue*1000) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(progressChangedValue*1000)), TimeUnit.MILLISECONDS.toSeconds(progressChangedValue*1000) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(progressChangedValue*1000)));
                text.setText(hms);
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressChangedValue != 0)
                {
                    start.setEnabled(false);
                    stop.setEnabled(true);
                    calender = Calendar.getInstance();
                    simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Date = simpleDateFormat.format(calender.getTime());
                    timer = new CountDownTimer(progressChangedValue * 1000, 1000) {

                        String starttime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(progressChangedValue * 1000), TimeUnit.MILLISECONDS.toMinutes(progressChangedValue * 1000) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(progressChangedValue * 1000)), TimeUnit.MILLISECONDS.toSeconds(progressChangedValue * 1000) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(progressChangedValue * 1000)));

                        public void onTick(long millisUntilFinished) {
                            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                            text.setText(hms);
                            seek.setProgress((int) (millisUntilFinished / 1000));
                        }

                        public void onFinish()
                        {
                            seek.setProgress(0);
                            text.setText("00:00:00");
                            start.setEnabled(true);
                            stop.setEnabled(false);
                            recordtime(starttime,Date);
                            player = MediaPlayer.create(getApplicationContext(),R.raw.song);
                            player.start();
                        }
                    }.start();
                }
                else
                {
                    if (progressChangedValue == 0)
                    {
                        Toast.makeText(TimerActivity.this,"First set the timer",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                stop.setEnabled(false);
                start.setEnabled(true);
                seek.setProgress(0);
                text.setText("00:00:00");
            }
        });
    }
    void recordtime(String time,String date) {
        try {
            SQLiteDatabase db = this.openOrCreateDatabase("timedb", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS timer(date VARCHAR , time VARCHAR)");
            Log.i("date:",date);
            db.execSQL("INSERT INTO timer(date , time) VALUES('" +date+ "','"+time+"')");
            Toast.makeText(this, "The Timer is finished and saved", Toast.LENGTH_LONG).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
