package com.example.superpiano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.superpiano.databinding.FragmentBlackPianoKeysBinding
import kotlinx.android.synthetic.main.fragment_black_piano_keys.view.*

class BlackPianoKeysFragment : Fragment() {

    private var _binding: FragmentBlackPianoKeysBinding? = null
    private val binding get() = _binding!!
    private lateinit var note:String

    var onKeyDown:((note:String) -> Unit)? = null
    var onKeyUp:((note:String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = it.getString("NOTE") ?: "?"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBlackPianoKeysBinding.inflate(inflater)
        val view = binding.root

        view.BlackToneKeyButton.setOnTouchListener(object: View.OnTouchListener{

            var activateButtonStartTime = 0

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> this@BlackPianoKeysFragment.onKeyDown?.invoke(note)
                    MotionEvent.ACTION_UP -> this@BlackPianoKeysFragment.onKeyUp?.invoke(note)
                }
                return true
            }
        })
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(note: String) =
            BlackPianoKeysFragment().apply {
                arguments = Bundle().apply {
                    putString("NOTE", note)
                }
            }
    }
}