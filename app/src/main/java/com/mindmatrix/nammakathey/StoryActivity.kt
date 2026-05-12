package com.mindmatrix.nammakathey

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.mindmatrix.nammakathey.databinding.ActivityStoryBinding
import java.util.Locale

class StoryActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var hero: Hero
    private var isKannada = false
    private lateinit var tts: TextToSpeech
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hero = intent.getSerializableExtra("HERO_DATA") as Hero

        tts = TextToSpeech(this, this)

        binding.tvStoryTitle.text = hero.name_en
        
        storyAdapter = StoryAdapter(hero.story_en)
        binding.viewPagerStory.adapter = storyAdapter

        binding.viewPagerStory.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val currentList = if (isKannada) hero.story_kn else hero.story_en
                if (position == currentList.size - 1) {
                    binding.btnTakeQuiz.visibility = View.VISIBLE
                } else {
                    binding.btnTakeQuiz.visibility = View.GONE
                }
                tts.stop()
            }
        })

        binding.btnToggleLang.setOnClickListener {
            isKannada = !isKannada
            val currentList = if (isKannada) hero.story_kn else hero.story_en
            binding.tvStoryTitle.text = if (isKannada) hero.name_kn else hero.name_en
            storyAdapter.updateData(currentList)
            tts.stop()
        }

        binding.fabTts.setOnClickListener {
            val currentList = if (isKannada) hero.story_kn else hero.story_en
            val textToRead = currentList[binding.viewPagerStory.currentItem]
            val locale = if (isKannada) Locale("kn", "IN") else Locale.US
            tts.language = locale
            tts.speak(textToRead, TextToSpeech.QUEUE_FLUSH, null, null)
        }

        binding.btnTakeQuiz.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("HERO_DATA", hero)
            startActivity(intent)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
