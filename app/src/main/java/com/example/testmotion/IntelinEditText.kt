package com.example.testmotion

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

/**
 * Copyright by Intelin.
 * Creator: antdg-intelin
 * Date: 13/12/2019
 * Time: 11:56
 */

public class IntelinEditText : View {

    private var inputType = 0
    private var hint: String? = null
    private var title: String? = null
    private var error: String? = null
    private var ivLeft: Drawable? = null
    private var ivRight: Drawable? = null
    private var textColor = 0
    private var isUpperCase = false
    private var background: Int? = null
    var currency: String? = null
    private var setDisable = false
    private var maxLength = 0
    private var textSize = context.resources.getDimensionPixelOffset(R.dimen.sp8).toFloat()

    private val paintTextTitle = Paint().apply {
        style = Paint.Style.FILL
        textSize = textSize
        textColor = Color.parseColor("#FFFFFF")
    }

    private val paintTextView = Paint().apply {
        isAntiAlias = true
        textSize = textSize
        style = Paint.Style.FILL
        color = resources.getColor(R.color.colorPrimary)
    }

    private val paintBorder = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeWidth = d2p(30f)
        color = Color.parseColor("#ffffff")
    }

    private val rect = RectF()
    private var rectBorder = RectF()

    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        var ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.IntelinEditText)
        title = ta.getString(R.styleable.IntelinEditText_setTitle)
        error = ta.getString(R.styleable.IntelinEditText_setError)
        ivLeft = ta.getDrawable(R.styleable.IntelinEditText_setLeftIcon)
        ivRight = ta.getDrawable(R.styleable.IntelinEditText_setRightIcon)
        inputType = ta.getInteger(R.styleable.IntelinEditText_setInputType, 0)
        isUpperCase = ta.getBoolean(R.styleable.IntelinEditText_setTextUpperCase, false)
        hint = ta.getString(R.styleable.IntelinEditText_setHint)
        background = ta.getInt(R.styleable.IntelinEditText_setBackground, -1)
        currency = ta.getString(R.styleable.IntelinEditText_currency_edt)
        setDisable = ta.getBoolean(R.styleable.IntelinEditText_setDisable, false)
        maxLength = ta.getInt(R.styleable.IntelinEditText_setMaxLenghEdt, 35)
        textSize = ta.getDimension(R.styleable.IntelinEditText_text_Size, this.textSize)
        ta.recycle()

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val desiredHeight = context.resources.getDimensionPixelOffset(R.dimen.dp75)

        val height: Int
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize)
        } else {
            height = desiredHeight
        }

        setMeasuredDimension(
            widthMeasureSpec, height
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)
        drawTextTitle(canvas)
        drawTextView(canvas)
        drawLeftIcon(canvas)
        drawRightIcon(canvas)
    }

    private fun drawTextTitle(canvas: Canvas) {
        paintTextTitle.textSize = textSize
        title?.let {
            canvas.drawText(
                it,
                paintTextTitle.textSize + context.resources.getDimensionPixelOffset(R.dimen.sp8),
                context.resources.getDimensionPixelOffset(R.dimen.sp5) + textSize,
                paintTextTitle
            )
        }
    }

    private fun drawBorder(canvas: Canvas) {
        val strokePaint = Paint()
        strokePaint.color = resources.getColor(R.color.colorPrimary)
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = context.resources.getDimensionPixelOffset(R.dimen.sp1).toFloat()
        val r: Rect = canvas.clipBounds
        rectBorder = RectF(
            context.resources.getDimensionPixelOffset(R.dimen.sp8).toFloat(),
            textSize.toInt() + context.resources.getDimensionPixelOffset(R.dimen.sp12).toFloat(),
            r.right - context.resources.getDimensionPixelOffset(R.dimen.sp8).toFloat(),
            textSize.toInt() + context.resources.getDimensionPixelOffset(R.dimen.sp60).toFloat()
        )
        canvas.drawRoundRect(
            rectBorder,
            context.resources.getDimensionPixelOffset(R.dimen.sp3).toFloat(),
            context.resources.getDimensionPixelOffset(R.dimen.sp3).toFloat(),
            strokePaint
        )
    }

    private fun drawLeftIcon(canvas: Canvas) {
        ivLeft?.let {
            it.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
            canvas.rotate(
                0f,
                context.resources.getDimensionPixelOffset(R.dimen.sp30).toFloat(),
                context.resources.getDimensionPixelOffset(R.dimen.sp30).toFloat()
            )
            it.setBounds(
                rectBorder.left.toInt() + context.resources.getDimensionPixelOffset(R.dimen.sp12),
                rectBorder.top.toInt() + context.resources.getDimensionPixelOffset(R.dimen.sp13),
                context.resources.getDimensionPixelOffset(R.dimen.sp40),
                rectBorder.bottom.toInt() - context.resources.getDimensionPixelOffset(R.dimen.sp13)
            )
            it.draw(canvas)
        }
    }

    private fun drawRightIcon(canvas: Canvas) {
        ivRight?.let {
            it.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
            canvas.rotate(
                0f,
                context.resources.getDimensionPixelOffset(R.dimen.sp30).toFloat(),
                context.resources.getDimensionPixelOffset(R.dimen.sp30).toFloat()
            )
            it.setBounds(
                rectBorder.right.toInt() - context.resources.getDimensionPixelOffset(R.dimen.sp35),
                rectBorder.top.toInt() + context.resources.getDimensionPixelOffset(R.dimen.sp13),
                rectBorder.right.toInt() - context.resources.getDimensionPixelOffset(R.dimen.sp13),
                rectBorder.bottom.toInt() - context.resources.getDimensionPixelOffset(R.dimen.sp13)
            )
            it.draw(canvas)
        }
    }

    private fun drawTextView(canvas: Canvas) {
        paintTextView.textSize = context.resources.getDimensionPixelOffset(R.dimen.sp14).toFloat()
        title?.let {
            canvas.drawText(
                it,
                0,
                maxLength,
                context.resources.getDimensionPixelOffset(R.dimen.sp45) + textSize,
                context.resources.getDimensionPixelOffset(R.dimen.sp5).toFloat() + (rectBorder.top.toInt() + rectBorder.bottom.toInt()) / 2,
                paintTextView
            )
        }
    }

    private fun error(message: String?) {
        message?.let {

        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Toast.makeText(context, "AAAAA", Toast.LENGTH_SHORT).show()
        return true
    }

    private fun d2p(dp: Float): Float {
        return resources.displayMetrics.densityDpi.toFloat() / 160.toFloat() * dp
    }
}