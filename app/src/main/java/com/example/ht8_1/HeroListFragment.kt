package com.example.ht8_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ht8_1.adapter.HeroesAdapter
import com.example.ht8_1.databinding.FragmentHeroListBinding
import com.example.ht8_1.model.Hero
import com.example.ht8_1.model.InfoFragment
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.combine
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.*

private lateinit var heroRecyclerAdapter: HeroesAdapter

class HeroListFragment : Fragment() {
    lateinit var binding: FragmentHeroListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeroListBinding.inflate(layoutInflater)
        println("hero list fr opened")

        val heroes = mutableListOf<Hero>()



        makeApiCall(heroes)
        println(heroes.size)

        while(heroes.size==0)
            Thread.sleep(100)

        binding.heroesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        heroRecyclerAdapter = HeroesAdapter()
        heroRecyclerAdapter.setHeroList(heroes)
        binding.heroesRecyclerView.adapter = heroRecyclerAdapter

        binding.heroesRecyclerView.addOnItemCLickListener(object: OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                val clickedHero = heroRecyclerAdapter.getHeroByPos(position)

              //  binding.infoFAB.hide()
                requireFragmentManager().beginTransaction()
                    .replace(R.id.containerForDetails, HeroDetailsFragment(clickedHero))
                    .commit()

            }
        })

        binding.infoFAB.setOnClickListener {
            requireFragmentManager().beginTransaction()
                .replace(R.id.containerForDetails, InfoFragment())
                .commit()
            Toast.makeText(requireContext(), "@kktoxs", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun RecyclerView.addOnItemCLickListener(onClickListener: OnItemClickListener){
        this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }
            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                }
            }
        })
    }

    private val URL = "https://api.opendota.com/api/heroStats"
    private val request = Request.Builder().url(URL).build()
    private val okHttpClient = OkHttpClient()

    private fun makeApiCall(heroesList: MutableList<Hero>?) {
        val dataFromFile = readFile()
        if (dataFromFile != null) {
            println("From file")
            val moshi = Moshi.Builder().build()
            val listHero = Types.newParameterizedType(List::class.java, Hero::class.java)
            val jsonAdapter: JsonAdapter<List<Hero>> = moshi.adapter(listHero)
            val heroes = jsonAdapter.fromJson(dataFromFile!!)
            println(heroes?.get(15))
            for (i in 0 until heroes!!.size) {
                heroesList?.add(heroes[i])
                // println(heroesList?.get(i))
            }
        } else {
            println("From network")
            okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    //ничего
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    writeFile(body)
                    println(body)
                    val moshi = Moshi.Builder().build()
                    val listHero = Types.newParameterizedType(List::class.java, Hero::class.java)
                    val jsonAdapter: JsonAdapter<List<Hero>> = moshi.adapter(listHero)
                    val heroes = jsonAdapter.fromJson(body!!)
                    println(heroes?.get(15))
                    for (i in 0 until heroes!!.size) {
                        heroesList?.add(heroes[i])
                        // println(heroesList?.get(i))
                    }

                }
            })
        }
    }
    private fun writeFile(json: String?){
        try{
            val bw = BufferedWriter(OutputStreamWriter(requireContext().openFileOutput("dotaHeroesFile", AppCompatActivity.MODE_PRIVATE)))
            bw.write(json)
            bw.close()
        } catch (e: FileNotFoundException){
            println("Файл не найден")
        }
    }
    private fun readFile():String?{
        try {
            val br = BufferedReader(InputStreamReader(requireContext().openFileInput("dotaHeroesFile")))
            return br.readText()
        }catch (e: FileNotFoundException){
            return null
        }
    }


}