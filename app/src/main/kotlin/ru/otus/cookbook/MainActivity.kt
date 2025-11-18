package ru.otus.cookbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.otus.cookbook.databinding.ActivityMainBinding
import ru.otus.cookbook.ui.CookbookFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var cookbookFragment: CookbookFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        cookbookFragment = savedInstanceState
            ?.let { supportFragmentManager.findFragmentByTag(CookbookFragment::class.simpleName) as? CookbookFragment }
            ?: CookbookFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, cookbookFragment, CookbookFragment::class.simpleName)
            .addToBackStack(null)
            .commit()

    }
}