package com.example.testmotion

import android.R.attr.radius
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
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
    private var itemTextSize = d2p(20f)
    private val rect = RectF()
    private var itemAnimDuration = DEFAULT_ANIM_DURATION
    private var indicatorLocation = d2p(10f)
    private var text: String? = ""


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

    private val paintTextVND = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = itemTextColor
        textSize = d2p(15f)
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    private var textSize = context.resources.getDimensionPixelOffset(R.dimen.sp8).toFloat()

    private val paintHexagon = Path()

    private val corners = floatArrayOf(
        0f, 0f,   // Top left radius in px
        15f, 15f,   // Top right radius in px
        15f, 15f,     // Bottom right radius in px
        0f, 0f      // Bottom left radius in px
    )

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
        barBackgroundColor =
            typedArray.getColor(R.styleable.DemoDraw_backgroundColor, this.barBackgroundColor)
        barIndicatorColor =
            typedArray.getColor(R.styleable.DemoDraw_indicatorColor, this.barIndicatorColor)
        itemTextColor = typedArray.getColor(R.styleable.DemoDraw_textColor, this.itemTextColor)
        itemTextSize = typedArray.getDimension(R.styleable.DemoDraw_textSize, this.itemTextSize)
        text = typedArray.getString(R.styleable.DemoDraw_textTitle)
        typedArray.recycle()
//        setBackgroundColor(barBackgroundColor)
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
        val point1_draw = Point(0, 15)
        val point2_draw = Point(30, 0)
        val point3_draw = Point(30, height)
        val point4_draw = Point(0, height - 15)
        val path = Path()
        path.moveTo(point1_draw.x.toFloat(), point1_draw.y.toFloat())
        path.lineTo(point2_draw.x.toFloat(), point2_draw.y.toFloat())
        path.lineTo(point3_draw.x.toFloat(), point3_draw.y.toFloat())
        path.lineTo(point4_draw.x.toFloat(), point4_draw.y.toFloat())

        path.close()
        paint.color = Color.parseColor("#BAB399")
        canvas.drawPath(path, paint)


        text?.let {
            val strokePaint = Paint()
            strokePaint.color = resources.getColor(R.color.error)
            strokePaint.style = Paint.Style.FILL
            strokePaint.strokeWidth =
                context.resources.getDimensionPixelOffset(R.dimen.sp1).toFloat()
            val rectBorder = RectF(
                0f,
                15f,
                paintText.measureText("$it VND") + 10,
                height - 15f
            )
            val path2 = Path()
            path2.addRoundRect(rectBorder, corners, Path.Direction.CW)
            canvas.drawPath(path2, strokePaint)

            canvas.drawText(
                it,
                paintText.measureText("$text") / 2 + 20,
                ((height - 15) / 2).toFloat() + paintText.textSize / 2,
                paintText
            )
            canvas.drawText(
                "VND",
                paintText.measureText("$text") / 2 + paintText.measureText("$text VND") / 2 + 10,
                ((height - 15) / 2).toFloat() + paintTextVND.textSize / 2,
                paintTextVND
            )
        }

        paintHexagon.moveTo(centerX, centerY + radius)
        paintHexagon.lineTo(centerX - triangleHeight, centerY + radius / 2)
        paintHexagon.lineTo(centerX - triangleHeight, centerY - radius / 2)
        paintHexagon.lineTo(centerX, centerY - radius)
        paintHexagon.lineTo(centerX + triangleHeight, centerY - radius / 2)
        paintHexagon.lineTo(centerX + triangleHeight, centerY + radius / 2)
        paintHexagon.moveTo(centerX, centerY + radius)

    }

    private fun d2p(dp: Float): Float {
        return resources.displayMetrics.densityDpi.toFloat() / 160.toFloat() * dp
    }

    var widthMeasure: Int = 0
    var heightMeasure: Int = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var desiredWidth = (paintText.measureText("1.000 VND") + 50).toInt()
        val desiredHeight = 100
        text?.let {
            desiredWidth = (paintText.measureText("$text VND") + 50).toInt()
        }

        //Measure Width
        widthMeasure = when {
            MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY -> {
                //Must be this size
                MeasureSpec.getSize(widthMeasureSpec)
            }
            MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST -> {
                //Can't be bigger than...
                desiredWidth.coerceAtMost(MeasureSpec.getSize(widthMeasureSpec))
            }
            else -> {
                //Be whatever you want
                desiredWidth
            }
        }

        //Measure Height
        heightMeasure = when {
            MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY -> {
                //Must be this size
                MeasureSpec.getSize(heightMeasureSpec)
            }
            MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST -> {
                //Can't be bigger than...
                desiredHeight.coerceAtMost(MeasureSpec.getSize(heightMeasureSpec))
            }
            else -> {
                //Be whatever you want
                desiredHeight
            }
        }

        //MUST CALL THIS
        setMeasuredDimension(widthMeasure, heightMeasure)
    }
}