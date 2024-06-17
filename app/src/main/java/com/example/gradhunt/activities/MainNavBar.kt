package com.example.gradhunt.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.gradhunt.R
import com.example.gradhunt.databinding.ActivityMainNavBarBinding
import com.example.gradhunt.fragments.Home
import com.example.gradhunt.fragments.NewPost
import com.example.gradhunt.fragments.Profile
import com.example.gradhunt.fragments.Search
import com.example.gradhunt.fragments.Settings

class MainNavBar : AppCompatActivity() {
    private lateinit var binding : ActivityMainNavBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainNavBarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Home())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.home -> replaceFragment(Home())
                R.id.search -> replaceFragment(Search())
                R.id.newPost -> replaceFragment(NewPost())
                R.id.profile -> replaceFragment(Profile())
                R.id.settings -> replaceFragment(Settings())

                else -> {

                }
            }

            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}