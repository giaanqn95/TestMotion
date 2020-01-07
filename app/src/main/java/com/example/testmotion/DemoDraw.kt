package com.example.testmotion

import android.R.attr.radius
import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import kotlin.math.sqrt


/**
 * Copyright by Intelin.
 * Creator: antdg-intelin
 * Date: 09/12/2019
 * Time: 10:35
 */

public class DemoDraw : View {

    private val DEFAULT_ANIM_DURATION = 200L
    private var barBackgroundColor = Color.parseColor("#00574B")
    private var barIndicatorColor = Color.parseColor("#FFFFFF")
    private var itemTextColor = Color.parseColor("#FF000000")
    private var itemTextSize = d2p(11F)
    private val rect = RectF()
    private var itemAnimDuration = DEFAULT_ANIM_DURATION
    private var indicatorLocation = d2p(10f)
    private var text = "ABC"


    private val paintIndicator = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = barIndicatorColor
    }

    private val paintText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = itemTextColor
        textSize = itemTextSize
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    private val paintHexagon = Path()

    constructor(context: Context) : super(context) {
        this.layoutParams = ViewGroup.LayoutParams(40, 50)
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.DemoDraw, 0, 0)
        barBackgroundColor = typedArray.getColor(
            R.styleable.DemoDraw_backgroundColor,
            this.barBackgroundColor
        )
        barIndicatorColor =
            typedArray.getColor(R.styleable.DemoDraw_indicatorColor, this.barIndicatorColor)
        itemTextColor = typedArray.getColor(R.styleable.DemoDraw_textColor, this.itemTextColor)
        itemTextSize = typedArray.getDimension(R.styleable.DemoDraw_textSize, this.itemTextSize)
        typedArray.recycle()
        setBackgroundColor(barBackgroundColor)
        // Update default attribute values
        paintIndicator.color = barIndicatorColor
        paintText.color = itemTextColor
        paintText.textSize = itemTextSize


//        if (itemFontFamily != 0) {
//            paintText.typeface = ResourcesCompat.getFont(context, itemFontFamily)
//        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        Log.d("AAAAAAA", "onDraw Called")
        super.onDraw(canvas)
        // Draw indicator
        rect.left = width - 10f
        rect.top = 100f
        rect.right = 10f
        rect.bottom = 50f
//        canvas.drawRoundRect(rect, 20f, 20f, paintIndicator)
//        canvas.drawText(
//            text,
//            (width / 2).toFloat(),
//            ((height + itemTextSize) / 2),
//            paintText
//        )

        val paintPoint = Paint()
        paintPoint.color = barIndicatorColor

        val triangleHeight =
            (sqrt(3.0) * radius / 2).toFloat()
        val centerX = (width / 2).toFloat()
        val centerY = (height / 2).toFloat()

        //NOTES: x ngang(hoành), y dọc(tung)
        /**
         * Vẽ view dựa trên Path sẽ phân bố cục từ trái sang phải và từ trên xuống dưới
         * point1_draw sử dụng path.moveTo() là bắt đầu vị trí thứ nhất
         * point2_draw sử dụng path.lineTo() là từ vị trí bắt đầu cho đến vị trí thứ 2
         * (tại thời điểm này hình dạng chưa được sẽ xong mà chỉ tạo được 1 đường thẳng, nên thể tích hình = 0
         *  => không thể hiện được lên view)
         * point3_draw sử dụng path.lineTo() là từ vị trí thứ 2 cho đến vị trí thứ 3
         * (lúc này ta tạo được 1 hình tam giác dựa trên 3 đường thẳng với 3 góc chỉ định
         * => hình đã được vẽ và thể hiện lên view)
         * point4_draw sử dụng path.lineTo() là từ vị trí thứ 3 cho đến vị trí thứ 4
         * (lúc này ta tạo được 1 hình thang cân dựa trên 4 đường thẳng với 4 góc chỉ định)
         * Tiếp tục những point tiếp theo ta chỉ định nó với vị trí và góc đối diện với 3 point còn lại
         * thì ta được thì lục giác
         * Nếu trong trường hợp hình bát giác thì điểm bắt đầu của point thứ 5 và 6 có khoảng cách tùy ý
         * với point 1 và 3 thì ta sẽ được hình bát giác
         * Còn hình vuông và hình chữ nhật không biết vẽ nữa là tao quỳ
         */
        val paint = Paint()
        val point1_draw = Point(0, 0)
        val point2_draw = Point(0, height / 2)
        val point3_draw = Point(0, height)
        val point4_draw = Point(width / 4, height)
        val point5_draw = Point(width / 2, height)
        val point6_draw = Point(width * 3 / 4, height)
        val point7_draw = Point(width, height)
        val point8_draw = Point(width, 0)
        val point9_draw = Point(width * 3 / 4, 0)
        val point10_draw = Point(width / 2, height * 2)
        val point11_draw = Point(width / 4, 0)
        val point12_draw = Point(width, 0)
        val path = Path()
        path.moveTo(point1_draw.x.toFloat(), point1_draw.y.toFloat())
        path.lineTo(point2_draw.x.toFloat(), point2_draw.y.toFloat())
        path.lineTo(point3_draw.x.toFloat(), point3_draw.y.toFloat())
        path.lineTo(point4_draw.x.toFloat(), point4_draw.y.toFloat())
        path.lineTo(point5_draw.x.toFloat(), point5_draw.y.toFloat())
        path.lineTo(point6_draw.x.toFloat(), point6_draw.y.toFloat())
        path.lineTo(point7_draw.x.toFloat(), point7_draw.y.toFloat())
        path.lineTo(point8_draw.x.toFloat(), point8_draw.y.toFloat())
        path.lineTo(point9_draw.x.toFloat(), point9_draw.y.toFloat())
        path.quadTo(
            (width / 2).toFloat(),
            (height).toFloat(),
            (width / 4).toFloat(),
            0F
        )

        path.quadTo(
            (0).toFloat(),
            (0).toFloat(),
            (width / 4).toFloat(),
            0F
        )


//        path.lineTo(point12_draw.x.toFloat(), point12_draw.y.toFloat())

        path.close()
        paint.color = Color.parseColor("#BAB399")
        canvas.drawPath(path, paint)

//        paintHexagon.moveTo(centerX, centerY + radius)
//        paintHexagon.lineTo(centerX - triangleHeight, centerY + radius / 2)
//        paintHexagon.lineTo(centerX - triangleHeight, centerY - radius / 2)
//        paintHexagon.lineTo(centerX, centerY - radius)
//        paintHexagon.lineTo(centerX + triangleHeight, centerY - radius / 2)
//        paintHexagon.lineTo(centerX + triangleHeight, centerY + radius / 2)
//        paintHexagon.moveTo(centerX, centerY + radius)

//        canvas.clipPath(paintHexagon)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        animateAlpha()
        return true
    }

    private fun animateAlpha() {
        val defaultAlpha = paintText.alpha
        val animator = ValueAnimator.ofInt(paintText.alpha, 0)
        animator.duration = itemAnimDuration
        animator.addUpdateListener {
            val value = it.animatedValue as Int
            paintText.alpha = value
            invalidate()
        }

        animator.addListener(object : Animator.AnimatorListener,
            ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator) {
                text = "XYZ"
                val a = ValueAnimator.ofInt(0, defaultAlpha)
                a.duration = itemAnimDuration

                a.addUpdateListener {
                    val value = it.animatedValue as Int
                    paintIndicator.alpha = value
                    invalidate()
                }
                a.start()
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationUpdate(p0: ValueAnimator?) {

            }

        })

        animator.start()
    }

    private fun animateIndicator(pos: Int) {
        val animator = ValueAnimator.ofFloat(indicatorLocation, 10f)
        animator.duration = itemAnimDuration
        animator.interpolator = DecelerateInterpolator()

        animator.addUpdateListener { animation ->
            indicatorLocation = animation.animatedValue as Float
        }

        animator.start()
    }

    private fun d2p(dp: Float): Float {
        return resources.displayMetrics.densityDpi.toFloat() / 160.toFloat() * dp
    }
}