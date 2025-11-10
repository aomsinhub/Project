package com.skooldio.android.designlayout.workshop2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skooldio.android.designlayout.workshop2.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rounds = intent.getIntExtra("rounds", 0)

        binding.textViewCongrats.text = "🎉 CONGRATS! 🎉"
        binding.textViewRoundNum.text = "$rounds"


        binding.buttonPlayAgain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
