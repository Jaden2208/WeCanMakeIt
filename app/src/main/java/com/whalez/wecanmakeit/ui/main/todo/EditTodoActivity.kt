package com.whalez.wecanmakeit.ui.main.todo

import android.app.Activity
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.whalez.wecanmakeit.EXTRA_CONTENT
import com.whalez.wecanmakeit.EXTRA_ID
import com.whalez.wecanmakeit.EXTRA_TIMESTAMP
import com.whalez.wecanmakeit.R
import kotlinx.android.synthetic.main.activity_edit_todo.*
import org.joda.time.DateTime

class EditTodoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo)

        val id = intent.getIntExtra(EXTRA_ID, -1)
        var timestamp = intent.getLongExtra(EXTRA_TIMESTAMP, -1)
        val content = intent.getStringExtra(EXTRA_CONTENT)

        btn_show_date_picker.text = DateTime(timestamp).toString("yyyy년 MM월 dd일")
        tv_content.setText(content)

        btn_back.setOnClickListener { finish() }

        btn_show_date_picker.setOnClickListener {
            val year = DateTime().year
            val month = DateTime().monthOfYear
            val day = DateTime().dayOfMonth
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, y, m, d ->
                    val changedTime = DateTime(y, m, d, 0, 0, 0)
                    timestamp = changedTime.millis
                    btn_show_date_picker.text = changedTime.toString("yyyy년 MM월 dd일")
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        btn_update.setOnClickListener {
            intent.apply {
                putExtra(EXTRA_ID, id)
                putExtra(EXTRA_TIMESTAMP, timestamp)
                putExtra(EXTRA_CONTENT, tv_content.text.toString())
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
