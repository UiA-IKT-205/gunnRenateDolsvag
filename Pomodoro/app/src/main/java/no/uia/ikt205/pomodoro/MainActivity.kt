package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    private lateinit var timer:CountDownTimer
    private lateinit var pauseTimer:CountDownTimer
    private lateinit var startButton:Button
    private lateinit var countdownDisplay:TextView
    private lateinit var seekbarSetWorkTime:SeekBar
    private lateinit var worktimeValueText: TextView
    private lateinit var seekbarSetPauseTime:SeekBar
    private lateinit var pausetimeDisplay:TextView
    private lateinit var repetitionInput:EditText
    private lateinit var countdownTimerText:TextView

    var timeToCountDownInMs = 0L
    var pauseTimeToCountDownInMs = 0L
    var numberOfWorkSessionsRepetitions:Int = 0
    val timeTicks = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekbarSetWorkTime = findViewById(R.id.seekBarSetWorkTime)
        // Set the SeekBar change listener for selected worktime
        seekbarSetWorkTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val worktimeCountdownValue = seekbarSetWorkTime.progress
                // Display the current value of work SeekBar
                updateCountDownDisplay(worktimeCountdownValue.toLong())
                updateWorktimeDisplay(worktimeCountdownValue.toLong())

                if (fromUser) {
                    timeToCountDownInMs = progress.toLong()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //
            }
        })

        seekbarSetPauseTime = findViewById(R.id.seekBarSetPauseTime)
        // Set the SeekBar change listener for selected pausetime
        seekbarSetPauseTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val pausetimeCountdownValue = seekbarSetPauseTime.progress
                // Display the current value of pause seekbar
                updatePausetimeDisplay(pausetimeCountdownValue.toLong())

                if (fromUser) {
                    pauseTimeToCountDownInMs = progress.toLong()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //
            }
        })

        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener(){
            startCountDown(it)
        }

        countdownDisplay = findViewById<TextView>(R.id.countDownTimerView)
        pausetimeDisplay = findViewById<TextView>(R.id.pausetimeValueText)
        worktimeValueText = findViewById<TextView>(R.id.worktimeValueText)
        repetitionInput = findViewById(R.id.countdownRepetitionsInput)
        // Info popup-text on which countdown that are currently running
        countdownTimerText = findViewById(R.id.countdownTimerText)
        countdownTimerText.toString()

    }

    private fun startCountDown(v: View){

        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }

            override fun onFinish() {
                numberOfWorkSessionsRepetitions = repetitionInput.text.toString().toInt()

                if (numberOfWorkSessionsRepetitions > 0) {
                    startPauseCountdown(countdownDisplay)
                    numberOfWorkSessionsRepetitions--
                    repetitionInput.setText(numberOfWorkSessionsRepetitions.toString())
                } else {
                    v.isEnabled = true
                    enableUserInteractionForActiveCountdown()
                    countdownTimerText.setText("")
                }
            }
        }

        timer.start()
        v.isEnabled = false
        disableUserInteractionForActiveCountdown()
        countdownTimerText.setText("Get to work!")
    }

    private fun startPauseCountdown(v: View) {

        pauseTimer = object : CountDownTimer(pauseTimeToCountDownInMs,timeTicks) {

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }

            override fun onFinish() {
                timer.start()
                countdownTimerText.setText("Get to work!")
            }
        }
        pauseTimer.start()
        countdownTimerText.setText("Pause")
    }

    private fun updateCountDownDisplay(timeInMs: Long){
        countdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

    private fun updatePausetimeDisplay(timeInMs: Long){
        pausetimeDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

    private fun updateWorktimeDisplay(timeInMs: Long){
        worktimeValueText.text = millisecondsToDescriptiveTime(timeInMs)
    }

    private fun disableUserInteractionForActiveCountdown() {
        seekbarSetWorkTime.isEnabled = false
        seekbarSetPauseTime.isEnabled = false
        repetitionInput.isEnabled = false
    }

    private fun enableUserInteractionForActiveCountdown() {
        seekbarSetWorkTime.isEnabled = true
        seekbarSetPauseTime.isEnabled = true
        repetitionInput.isEnabled = true
    }
}