package com.example.superpiano

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.superpiano.data.NoteSheet
import com.example.superpiano.databinding.FragmentPianoBinding
import com.example.superpiano.util.millisecondsToSecondsFormat
import kotlinx.android.synthetic.main.fragment_piano.*
import kotlinx.android.synthetic.main.fragment_piano.view.*
import java.io.File
import java.io.FileOutputStream

class PianoLayout : Fragment() {

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val whiteKeys = listOf("C1","D1","E1","F1","G1","A1","B1","C2","D2","E2","F2","G2","A2","B2")
    private val blackKeys = listOf("C1#","D1#","F1#","G1#","A1#","C2#","D2#","F2#","G2#","A2#")
    private var musicTune:MutableList<NoteSheet> = mutableListOf<NoteSheet>()

    var onSave:((file:Uri) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root

        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val initialStartTime = System.currentTimeMillis()

        whiteKeys.forEach { noteValue ->

            val whitePianoKey = WhitePianoKeysFragment.newInstance(noteValue)

            var activateButtonStartTime:Long = 0

            whitePianoKey.onKeyDown = { note ->
                activateButtonStartTime = System.currentTimeMillis()
                println("Piano key down $note")
            }

            whitePianoKey.onKeyUp = {
                val activateButtonEndTime = System.currentTimeMillis()
                val totalActivatedButtonTime = activateButtonEndTime - activateButtonStartTime

                if (musicTune.isNullOrEmpty()) {
                    val initialRelativeStartTime:Long = 0
                    val note = NoteSheet(it, millisecondsToSecondsFormat(initialRelativeStartTime), millisecondsToSecondsFormat(totalActivatedButtonTime))
                    musicTune.add(note)
                    println("Piano key up")
                    println("Values saved to notesheet: $note")
                } else {

                    val newRelativeTime = activateButtonEndTime - initialStartTime

                    val note = NoteSheet(it, millisecondsToSecondsFormat(newRelativeTime), millisecondsToSecondsFormat(totalActivatedButtonTime))

                    musicTune.add(note)
                    println("Piano key up")
                    println("Values saved to notesheet: $note")
                }

            }
            fragmentTransaction.add(view.WhiteKeysLayout.id, whitePianoKey, "note_$noteValue")
        }

        blackKeys.forEach { noteValue ->

            val blackPianoKey = BlackPianoKeysFragment.newInstance(noteValue)
            var activateButtonStartTime:Long = 0

            blackPianoKey.onKeyDown = { note ->
                activateButtonStartTime = System.currentTimeMillis()
                println("Piano key down $note")
            }

            blackPianoKey.onKeyUp = {
                val activateButtonEndTime = System.currentTimeMillis()
                val totalActivatedButtonTime = activateButtonEndTime - activateButtonStartTime

                if (musicTune.isNullOrEmpty()) {
                    val initialRelativeStartTime:Long = 0
                    val note = NoteSheet(it, millisecondsToSecondsFormat(initialRelativeStartTime), millisecondsToSecondsFormat(totalActivatedButtonTime))
                    musicTune.add(note)
                    println("Piano key up")
                    println("Values saved to notesheet: $note")
                } else {

                    val newRelativeTime = activateButtonEndTime - initialStartTime

                    val note = NoteSheet(it, millisecondsToSecondsFormat(newRelativeTime), millisecondsToSecondsFormat(totalActivatedButtonTime))

                    musicTune.add(note)
                    println("Piano key up")
                    println("Values saved to notesheet: $note")
                }
            }
            fragmentTransaction.add(view.BlackKeysLayout.id, blackPianoKey, "note_$noteValue")
        }

        fragmentTransaction.commit()


        view.saveMusicTuneButton.setOnClickListener {
            var filename = view.fileNameInput.text.toString()
            val filepath = this.activity?.getExternalFilesDir(null)
            var checkForDuplicateFilename = false
            val checkExternalStorageReadOnly = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED_READ_ONLY
            val checkAvailabilityExternalStorage = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

            val newFile = File(filepath, filename)
            newFile.toString()

            File("/storage/emulated/0/Android/data/com.example.superpiano/files").walk().forEach {
                // Prints out existing files since I can't view content in Device file explorer due to "Permission denied" error.
                println("IT: $it")

                if (it == newFile) {
                    checkForDuplicateFilename = true
                }
            }

            when {
                musicTune.isEmpty() ->
                    Toast.makeText(activity,"Please play a tune to save", Toast.LENGTH_SHORT).show();
                filename.isEmpty() ->
                    Toast.makeText(activity,"Please enter a filename", Toast.LENGTH_SHORT).show();
                checkForDuplicateFilename ->
                    Toast.makeText(activity,"Filepath already exists",Toast.LENGTH_SHORT).show();

                else -> {
                    when {
                        checkExternalStorageReadOnly -> {
                            Toast.makeText(activity,"External storage is read only", Toast.LENGTH_SHORT).show();
                            disableButton(saveMusicTuneButton) }
                        !checkAvailabilityExternalStorage -> {
                            Toast.makeText(activity,"Not enough space on external storage", Toast.LENGTH_SHORT).show();
                            disableButton(saveMusicTuneButton)
                        } else -> {
                            filename = "$filename.music"
                            val content:String = musicTune.map {
                                it.toString()
                            }.reduce { acc, s -> acc + s + "\n" }
                            saveFile(filename,content)
                        }
                    }
                }
            }
        }
        return view
    }

    private fun saveFile(filename:String, content:String) {
        val filepath = this.activity?.getExternalFilesDir(null)

        if (filepath != null) {
            val file = File(filepath,filename)
            FileOutputStream(file, true).bufferedWriter().use { writer ->
                writer.write(content)
            }

            this.onSave?.invoke(file.toUri())
            Toast.makeText(activity, "File successfully saved", Toast.LENGTH_SHORT).show();
            musicTune.clear()
            fileNameInput.text.clear()
            FileOutputStream(File(filepath, filename)).close()

        } else {
            Toast.makeText(activity, "Could not get external filepath", Toast.LENGTH_SHORT).show()
        }
    }

    private fun disableButton(button: Button) {
        button.isEnabled = false
        button.setTextColor(ContextCompat.getColor(saveMusicTuneButton.context, R.color.red_213_disable_button))
    }
}