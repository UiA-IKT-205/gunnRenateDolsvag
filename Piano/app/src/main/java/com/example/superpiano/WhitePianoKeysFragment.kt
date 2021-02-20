package com.example.superpiano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.superpiano.databinding.FragmentWhitePianoKeysBinding
import kotlinx.android.synthetic.main.fragment_white_piano_keys.view.*

class WhitePianoKeysFragment : Fragment() {

    private var _binding: FragmentWhitePianoKeysBinding? = null
    private val binding get() = _binding!!
    private lateinit var note:String

    var onKeyDown:((note:String) -> Unit)? = null
    var onKeyUp:((note:String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // senderinn default verdi med Elvis operator ?:
            note = it.getString("NOTE") ?: "?" // hvis satt til 0 blir noten satt til?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWhitePianoKeysBinding.inflate(inflater)
        val view = binding.root

        view.whiteToneKeyButton.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> this@WhitePianoKeysFragment.onKeyDown?.invoke(note)
                    MotionEvent.ACTION_UP -> this@WhitePianoKeysFragment.onKeyUp?.invoke(note)
                }
                return true
            }
        })
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(note: String) =
            WhitePianoKeysFragment().apply {
                arguments = Bundle().apply {
                    putString("NOTE", note)
                }
            }
    }
}