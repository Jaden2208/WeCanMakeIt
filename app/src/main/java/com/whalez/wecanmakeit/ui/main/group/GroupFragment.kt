package com.whalez.wecanmakeit.ui.main.group

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whalez.wecanmakeit.R
import kotlinx.android.synthetic.main.fragment_group.*

class GroupFragment : Fragment() {

    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_create_group.setOnClickListener {
            val intent = Intent(mContext, CreateNewGroupActivity::class.java)
            startActivity(intent)
        }
    }
}