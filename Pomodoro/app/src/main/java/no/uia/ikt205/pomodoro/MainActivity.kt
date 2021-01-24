package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    lateinit var button30:Button
    lateinit var button60:Button
    lateinit var button90:Button
    lateinit var button120:Button
    lateinit var coutdownDisplay:TextView

    var timeToCountDownInMs = 0L
    val timeTicks = 1000L
    var running = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener(){
            startCountDown(it)
        }

        button30 = findViewById<Button>(R.id.CountdownButton30)
        button30.setOnClickListener(){
            timeToCountDownInMs = 1800000L
            if (!running){
                updateCountDownDisplay(1800000)
            }
        }

        button60 = findViewById<Button>(R.id.CountdownButton60)
        button60.setOnClickListener(){
            timeToCountDownInMs = 3600000L
            if (!running){
                updateCountDownDisplay(3600000)
            }
        }

        button90 = findViewById<Button>(R.id.CountdownButton90)
        button90.setOnClickListener(){
            timeToCountDownInMs = 5400000L
            if (!running){
                updateCountDownDisplay(5400000)
            }
        }

        button120 = findViewById<Button>(R.id.CountdownButton120)
        button120.setOnClickListener(){
            timeToCountDownInMs = 7200000L
            if (!running){
                updateCountDownDisplay(7200000)
            }
        }

       coutdownDisplay = findViewById<TextView>(R.id.countDownView)

    }

    fun startCountDown(v: View){

        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
                running = false
            }

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }
        }

        if (!running){
            timer.start()
            running = true
        }
    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}