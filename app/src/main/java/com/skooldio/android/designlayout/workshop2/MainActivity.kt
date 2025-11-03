package com.skooldio.android.designlayout.workshop2

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skooldio.android.designlayout.workshop2.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var randomNumber = 0
    private var roundCount = 0
    private var currentInput = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ผูก viewbinding กับ layout และตั้งเป็น content view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ปรับ padding ให้รองรับ system bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // เริ่มเกมครั้งแรก
        startNewGame()

        // ปุ่มหมายเลข 0-9 (ต้องแน่ใจว่าใน layout มี id เหล่านี้)
        val numberButtons = listOf(
            binding.button0, binding.button1, binding.button2, binding.button3,
            binding.button4, binding.button5, binding.button6, binding.button7,
            binding.button8, binding.button9
        )

        numberButtons.forEach { btn ->
            btn.setOnClickListener {
                if (currentInput.length < 2) { // จำกัดให้ใส่ได้สูงสุด 2 หลัก (0-99)
                    currentInput += btn.text.toString()
                    binding.textViewInput.text = currentInput
                }
            }
        }

        // ปุ่มลบ
        binding.buttonDelete.setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = currentInput.dropLast(1)
                binding.textViewInput.text = if (currentInput.isEmpty()) "-" else currentInput
            }
        }

        // ปุ่ม OK -> ตรวจคำตอบ
        binding.buttonSubmit.setOnClickListener {
            if (currentInput.isEmpty()) return@setOnClickListener

            val guess = try {
                currentInput.toInt()
            } catch (e: NumberFormatException) {
                // ป้องกันกรณีแปลผิดพลาด (ไม่ควรเกิด)
                currentInput = ""
                binding.textViewInput.text = "-"
                return@setOnClickListener
            }

            roundCount++
            binding.textViewRoundNum.text = roundCount.toString()

            when {
                guess < randomNumber -> binding.textViewResultNum.text = ">"
                guess > randomNumber -> binding.textViewResultNum.text = "<"
                else -> { // ถูกต้อง -> ไปหน้า ResultActivity พร้อมส่งค่า
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra("answer", randomNumber)
                    intent.putExtra("rounds", roundCount)
                    startActivity(intent)
                    finish() // ปิด MainActivity เพื่อไม่ให้กด back กลับมา
                }
            }

            // รีเซ็ต input หลังการทาย
            currentInput = ""
            binding.textViewInput.text = "-"
        }
    }

    private fun startNewGame() {
        randomNumber = Random.nextInt(0, 100) // สุ่ม 0..99
        roundCount = 0
        currentInput = ""
        binding.textViewInput.text = "-"
        binding.textViewResultNum.text = "?"
        binding.textViewRoundNum.text = "0"
    }
}
