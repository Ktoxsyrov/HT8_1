package com.example.ht8_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ht8_1.adapter.HeroesAdapter

private lateinit var heroRecyclerAdapter: HeroesAdapter

interface OnItemClickListener{
    fun onItemClicked(position: Int, view: View)
}

class MainActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
       // if(savedInstanceState==null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerForList, HeroListFragment())
                .commit()
    }
}