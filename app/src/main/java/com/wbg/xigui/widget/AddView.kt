package com.wbg.xigui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.wbg.xigui.R
import otherwise
import yes

class AddView :
    LinearLayout {
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    private lateinit var plusTv: TextView
    private lateinit var reduceTv: TextView
    private lateinit var numTv: TextView
    private var listener: NumChangedListener? = null


    init {
        LayoutInflater.from(context).inflate(R.layout.add_view, this)
        initView(this)
    }

    private fun initView(view: View) {
        plusTv = view.findViewById(R.id.plus_tv)
        reduceTv = view.findViewById(R.id.reduce_tv)
        numTv = view.findViewById(R.id.num_tv)
        plusTv.setOnClickListener {
            if (numTv.text.toString().toInt() < 99)
                listener?.onNumChange(numTv.text.toString().toInt() + 1)
        }
        reduceTv.setOnClickListener {
            if (numTv.text.toString().toInt() > 0)
                listener?.onNumChange(numTv.text.toString().toInt() - 1)
        }
    }

    fun setNum(num: Int) {
//        numTv.text = (num > 99).yes { "99+" }.otherwise { "$num" }
        numTv.text = "$num"
    }

    fun getNum(): Int {
        return numTv.text.toString().toInt()
    }

    fun setNumChangeListener(listener: NumChangedListener) {
        this.listener = listener
    }

    interface NumChangedListener {
        abstract fun onNumChange(int: Int)
    }
}