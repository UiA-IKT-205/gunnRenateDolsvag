package com.example.superpiano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.superpiano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_piano.*
import kotlinx.android.synthetic.main.fragment_piano.view.*

class PianoLayout : Fragment() {

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val whiteKeys = listOf("C1", "D1", "E1", "F1", "G1", "A1", "B1", "C2", "D2", "E2", "F2", "G2", "A2", "B2")
    private val blackKeys = listOf("C1#", "D1#", "F1#", "G1#", "A1#","C2#", "D2#", "F2#", "G2#", "A2#")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root

        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        whiteKeys.forEach {

            val whitePianoKey = WhitePianoKeysFragment.newInstance(it)

            whitePianoKey.onKeyDown = {
                println("Piano key down $it")
            }

            whitePianoKey.onKeyUp = {
                println("Piano key up $it")
            }

            fragmentTransaction.add(view.WhiteKeysLayout.id, whitePianoKey, "note_$it")
        }

        blackKeys.forEach {

            val blackPianoKey = BlackPianoKeysFragment.newInstance(it)

            blackPianoKey.onKeyDown = {
                println("Piano key down $it")
            }

            blackPianoKey.onKeyUp = {
                println("Piano key up $it")
            }

            fragmentTransaction.add(view.BlackKeysLayout.id, blackPianoKey, "note_$it")
        }

        fragmentTransaction.commit()

        return view
    }
}