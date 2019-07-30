package com.example.lokeshkaushik.sangeet;

/**
 * Created by Lokesh Kaushik on 24-Sep-16.
 */
public class SetTimer {
    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";

        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);



        if(seconds < 10){
            finalTimerString = "0" + seconds;
        }
        else{
            finalTimerString = "" + seconds;
        }

        finalTimerString =  "0"+ minutes + ":" + finalTimerString;

        return finalTimerString;
    }


    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);


        percentage =(((double)currentSeconds)/totalSeconds)*100;

        return percentage.intValue();
    }


    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);


        return currentDuration * 1000;
    }
}
