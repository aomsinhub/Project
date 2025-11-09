package com.skooldio.android.designlayout.workshop2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skooldio.android.designlayout.workshop2.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var randomNumber = 0
    private var roundCount = 0
    private var currentInput = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startNewGame()

        // ปุ่มตัวเลข 0-9
        val numberButtons = listOf(
            binding.button0, binding.button1, binding.button2,
            binding.button3, binding.button4, binding.button5,
            binding.button6, binding.button7, binding.button8, binding.button9
        )

        // เมื่อกดตัวเลข
        numberButtons.forEach { button ->
            button.setOnClickListener {
                if (currentInput.length < 2) { // กำหนดไม่ให้เกิน 2 หลัก
                    currentInput += button.text
                    updateInput()
                }
            }
        }

        // ปุ่มลบตัวสุดท้าย
        binding.buttonDelete.setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = currentInput.dropLast(1)
                updateInput()
            }
        }

        // ปุ่ม OK สำหรับตรวจคำตอบ
        binding.buttonSubmit.setOnClickListener {
            if (currentInput.isEmpty()) return@setOnClickListener

            val guess = currentInput.toIntOrNull() ?: return@setOnClickListener
            roundCount++

            when {
                guess < randomNumber -> binding.textViewResultNum.text = ">"
                guess > randomNumber -> binding.textViewResultNum.text = "<"
                else -> {
                    val result = GameResult(randomNumber, roundCount)
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra("answer", result.answer)
                    intent.putExtra("rounds", result.rounds)
                    startActivity(intent)
                    finish()
                }
            }

            currentInput = ""
            updateInput()
            binding.textViewRoundNum.text = roundCount.toString()
        }
    }

    private fun startNewGame() {
        randomNumber = Random.nextInt(0, 100)
        roundCount = 0
        currentInput = ""
        binding.textViewResultNum.text = "?"
        binding.textViewRoundNum.text = "0"
        updateInput()
    }

    private fun updateInput() {
        binding.textViewInput.text = if (currentInput.isEmpty()) "-" else currentInput
    }
}
