package com.whalez.wecanmakeit.ui.main.todo

import android.animation.ValueAnimator
import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.whalez.wecanmakeit.R
import com.whalez.wecanmakeit.room.data.Todo
import kotlinx.android.synthetic.main.home_todo_item.view.*
import kotlinx.android.synthetic.main.todo_item.view.*
import org.joda.time.DateTime
import kotlin.math.roundToInt


class TodoAdapter(private val type: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_ONLY_TODAY = 0
        const val TYPE_ALL_TODO = 1
    }

    private var todoList: List<Todo> = ArrayList()

    private var isSelectedItem = SparseBooleanArray(0)
    private var prePosition = -1

    private lateinit var context: Context

    private lateinit var listener: OnMoreClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        if (type == TYPE_ALL_TODO) {
            val view = inflater.inflate(R.layout.todo_item, parent, false)
            return TodoViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.home_todo_item, parent, false)
            return HomeTodoViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val todo = todoList[position]
        val dateTime = DateTime(todo.timestamp)
        if (type == TYPE_ALL_TODO) {
            val mHolder = holder as TodoViewHolder
            mHolder.yearMonth.text = dateTime.toString("yyyy.MM")
            mHolder.date.text = dateTime.toString("dd")
            mHolder.content.text = todo.content
            mHolder.changeVisibility(isSelectedItem[position])
            mHolder.btnMore.visibility = if (isSelectedItem[position]) View.VISIBLE else View.GONE
        } else {
            val mHolder = holder as HomeTodoViewHolder
            mHolder.content.text = todo.content
            mHolder.changeVisibility(isSelectedItem[position])
        }

    }

    override fun getItemCount(): Int = todoList.size

    fun getTodoAt(position: Int): Todo {
        return todoList[position]
    }

    fun setTodoList(todoList: List<Todo>) {
        this.todoList = todoList
        notifyDataSetChanged()
    }

    inner class TodoViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val todoItem: CardView = itemView.cv_todo_item
        val yearMonth: TextView = itemView.tv_year_month
        val date: TextView = itemView.tv_date
        val content: TextView = itemView.tv_content
        val btnMore: ImageButton = itemView.btn_more

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    if (isSelectedItem[adapterPosition]) {
                        isSelectedItem.put(adapterPosition, false)
                    } else {
                        isSelectedItem.put(prePosition, false)
                        isSelectedItem.put(adapterPosition, true)
                    }
                    if (prePosition != -1) notifyItemChanged(prePosition)
                    notifyItemChanged(adapterPosition)
                    prePosition = adapterPosition
                }
            }

            btnMore.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClick(todoList[adapterPosition], it)
                }
            }
        }

        fun changeVisibility(isExpanded: Boolean) {
            val dpValue = 250
            val d = context.resources.displayMetrics.density
            val maxHeight = (dpValue * d).roundToInt()
            val minHeight =
                context.resources.getDimension(R.dimen.todo_item_min_height).roundToInt()

            val valueAnimator =
                if (isExpanded) ValueAnimator.ofInt(minHeight, maxHeight) else ValueAnimator.ofInt(
                    maxHeight,
                    minHeight
                )
            valueAnimator.duration = 200
            valueAnimator.addUpdateListener {
                val heightValue = it.animatedValue as Int
                todoItem.apply {
                    layoutParams.height = heightValue
                    requestLayout()
                }
            }
            valueAnimator.start()
        }
    }

    inner class HomeTodoViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val todoItem: CardView = itemView.cv_today_todo_item
        val content: TextView = itemView.tv_today_content

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    if (isSelectedItem[adapterPosition]) {
                        isSelectedItem.put(adapterPosition, false)
                    } else {
                        isSelectedItem.put(prePosition, false)
                        isSelectedItem.put(adapterPosition, true)
                    }
                    if (prePosition != -1) notifyItemChanged(prePosition)
                    notifyItemChanged(adapterPosition)
                    prePosition = adapterPosition
                }
            }
        }

        fun changeVisibility(isExpanded: Boolean) {
            val dpValue = 250
            val d = context.resources.displayMetrics.density
            val maxHeight = (dpValue * d).roundToInt()
            val minHeight =
                context.resources.getDimension(R.dimen.todo_item_min_height).roundToInt()

            val valueAnimator =
                if (isExpanded) ValueAnimator.ofInt(minHeight, maxHeight) else ValueAnimator.ofInt(
                    maxHeight,
                    minHeight
                )
            valueAnimator.duration = 200
            valueAnimator.addUpdateListener {
                val heightValue = it.animatedValue as Int
                todoItem.apply {
                    layoutParams.height = heightValue
                    requestLayout()
                }
            }
            valueAnimator.start()
        }
    }


    interface OnMoreClickListener {
        fun onItemClick(todo: Todo, view: View)
    }

    fun setOnItemClickListener(listener: OnMoreClickListener) {
        this.listener = listener
    }

}