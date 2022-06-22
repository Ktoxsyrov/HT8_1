package com.example.ht8_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ht8_1.databinding.FagmentInfoBinding

class InfoFragment: Fragment() {
    lateinit var binding: FagmentInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FagmentInfoBinding.inflate(layoutInflater)

        binding.root.setOnClickListener {
            requireFragmentManager().beginTransaction().remove(this).commit()
        }

        return binding.root
    }
}