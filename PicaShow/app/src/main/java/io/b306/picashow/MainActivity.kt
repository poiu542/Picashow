package io.b306.picashow

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.TextView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hours = (0..23).toList()
        val minutes = listOf("00", "10", "20", "30", "40", "50")

        // 시간 및 분 Spinner의 ID들을 리스트로 저장
        val hourSpinnerIDs =
            listOf(R.id.hourSpinner1, R.id.hourSpinner2, R.id.hourSpinner3, R.id.hourSpinner4)
        val minuteSpinnerIDs = listOf(
            R.id.minuteSpinner1,
            R.id.minuteSpinner2,
            R.id.minuteSpinner3,
            R.id.minuteSpinner4
        )
        val selectedHourTextViewIDs = listOf(R.id.tv_selectedHour1, R.id.tv_selectedHour2, R.id.tv_selectedHour3, R.id.tv_selectedHour4)
        val selectedMinuteTextViewIDs = listOf(R.id.tv_selectedMinute1, R.id.tv_selectedMinute2, R.id.tv_selectedMinute3, R.id.tv_selectedMinute4)

        val etPlan1: EditText = findViewById(R.id.et_plan1)
        val etPlan2: EditText = findViewById(R.id.et_plan2)
        val etPlan3: EditText = findViewById(R.id.et_plan3)
        val etPlan4: EditText = findViewById(R.id.et_plan4)
        val imageView: ImageView = findViewById(R.id.iv_main)

        // 각 ID에 대하여 Spinner를 설정
        for (i in hourSpinnerIDs.indices) {
            val hourSpinner: Spinner = findViewById(hourSpinnerIDs[i])
            val minuteSpinner: Spinner = findViewById(minuteSpinnerIDs[i])
            val selectedHourTextView: TextView = findViewById(selectedHourTextViewIDs[i])  // i+1을 사용하여 id를 동적으로 가져옵니다.
            val selectedMinuteTextView: TextView = findViewById(selectedMinuteTextViewIDs[i])  // i+1을 사용하여 id를 동적으로 가져옵니다.

            hourSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hours)
            minuteSpinner.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, minutes)

            hourSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    selectedHourTextView.text = hours[position].toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            minuteSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    selectedMinuteTextView.text = minutes[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }

        val button: Button = findViewById(R.id.btn_main)
        button.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
//            val combinedKeyword =
//                "${etPlan1.text}, ${etPlan2.text}, ${etPlan3.text}, ${etPlan4.text}"

            // 예시로 첫 번째 키워드로 AI 그림을 생성하게 했습니다.
//            fetchAIArt(combinedKeyword) { bitmap ->
//                // bitmap은 AI가 생성한 그림입니다.
//                // 이 bitmap을 ImageView에 설정하여 사용자에게 보여줄 수 있습니다.
//                imageView.setImageBitmap(bitmap)
//            }
        }

    }

}