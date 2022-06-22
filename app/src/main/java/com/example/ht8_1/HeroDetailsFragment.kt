package com.example.ht8_1

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import coil.load
import com.example.ht8_1.databinding.FragmentHeroDetailsBinding
import com.example.ht8_1.databinding.FragmentHeroListBinding
import com.example.ht8_1.model.Hero

class HeroDetailsFragment(hero: Hero) : Fragment() {
    val hero = hero
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        println("hero details fr opened")
        val binding = FragmentHeroDetailsBinding.inflate(layoutInflater)
        binding.heroDetailsAvatar.load("https://api.opendota.com" + hero.img)
        binding.heroDetailsName.text = hero.localized_name
        binding.strStats.text = "${hero.base_str} + ${hero.str_gain}"
        binding.intStats.text = "${hero.base_int} + ${hero.int_gain}"
        binding.agiStats.text = "${hero.base_agi} + ${hero.agi_gain}"
        if(hero.attack_type == "Melee")
            binding.heroDetailsAttackType.setImageResource(R.drawable.ic_melee)
        else
            binding.heroDetailsAttackType.setImageResource(R.drawable.ic_ranged)
        when(hero.primary_attr){
            "int" -> {
                binding.intStats.typeface = Typeface.DEFAULT_BOLD
                binding.heroDetails.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.main_int))
            }
            "agi" -> {
                binding.agiStats.typeface = Typeface.DEFAULT_BOLD
                binding.heroDetails.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.main_agi))
            }
            "str" -> {
                binding.strStats.typeface = Typeface.DEFAULT_BOLD
                binding.heroDetails.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.main_str))
            }
        }
        binding.heroDetails.setOnClickListener {
            requireFragmentManager().beginTransaction().remove(this).commit()
        }
        return binding.root
    }

}