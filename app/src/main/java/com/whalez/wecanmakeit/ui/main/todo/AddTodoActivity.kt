package com.whalez.wecanmakeit.ui.main.todo

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.whalez.wecanmakeit.EXTRA_CONTENT
import com.whalez.wecanmakeit.EXTRA_TIMESTAMP
import com.whalez.wecanmakeit.R
import com.whalez.wecanmakeit.shortToast
import kotlinx.android.synthetic.main.activity_add_todo.*
import org.joda.time.DateTime

class AddTodoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        var year = DateTime().year
        var month = DateTime().monthOfYear
        var day = DateTime().dayOfMonth
        var timestamp = DateTime(year, month, day, 0, 0, 0).millis
        btn_show_date_picker.text = DateTime().toString("yyyy년 MM월 dd일")
        btn_show_date_picker.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, y, m, d ->
                    val changedTime = DateTime(y, m+1, d, 0, 0, 0)
                    timestamp = changedTime.millis
                    year = y
                    month = m+1
                    day = d
                    btn_show_date_picker.text = changedTime.toString("yyyy년 MM월 dd일")
                },
                year,
                month-1,
                day
            )
            dpd.show()
        }

        btn_back.setOnClickListener { finish() }

        btn_save.setOnClickListener {
            val content = tv_content.text.toString()
            if (content.isEmpty()) {
                shortToast(this, "저장할 내용이 없습니다!")
                return@setOnClickListener
            }
            intent.putExtra(EXTRA_TIMESTAMP, timestamp)
                .putExtra(EXTRA_CONTENT, content)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}