package com.example.talkingclock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.talkingclock.databinding.ActivityAddAlarmBinding;
import com.example.talkingclock.databinding.ActivityMainBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class Add_Alarm extends AppCompatActivity {

    private ActivityAddAlarmBinding binding;
    private MaterialTimePicker picker;// used in Showtime picker method

    private Calendar calendar; // used in Showtime picker method
    private AlarmManager alarmManager; // used in setAlarm Method
    private PendingIntent pendingIntent; // used in Set Alarm method


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createNotificationChannel();


        binding.selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(); //method to pick th etime
            }
        });

        binding.setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm(); // method to confirm the time
            }
        });

        binding.cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();


            }
        });

    }

    private void cancelAlarm() {
        Intent intent = new Intent(this , AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0 , intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager == null){
            alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this,"Alarm canceled", Toast.LENGTH_SHORT).show();
    }

    private void setAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0 , intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //should use SetExact not InExact
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

        Toast.makeText(this, "Alarm Set Suceesfully", Toast.LENGTH_SHORT).show();


    }

    private void showTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();

        picker.show(getSupportFragmentManager(),"TalkingClock");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picker.getHour() > 12){
                    binding.selectedTime.setText(
                            String.format("%02d", (picker.getHour() - 12)+ " : "+ String.format("%02d", picker.getMinute() )+" PM" )
                    );
                }else {
                    binding.selectedTime.setText(picker.getHour()+" : " + picker.getMinute() + " AM ");
                }

                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);

            }
        });
    }

    private void createNotificationChannel() {
        // to check the if the android in oreo or above or not to send notification

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            CharSequence name = "TalkingClockAlarmChannel";
            String description = "Channel for Talking Clock Alarm MAnager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("TalkingClock1.0", name , importance); // this should remain same as in AlarmRecivejava
            channel.setDescription(description);

            NotificationManager notificationManager= getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


    }
}