package app.deadmc.bunnywatchface

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import java.util.logging.Logger
import kotlin.math.cos
import kotlin.math.sin


class DisplayHelper(val applicationContext: Context) {

    val TAG = DisplayHelper::class.java.simpleName

    val backgroundPaint = Paint()
    val textPaint = Paint()
    val secondsPaint = Paint()
    val circlePaint = Paint()
    val resources = applicationContext.resources

    val timeHelper = TimeHelper()

    var radius = 0f
    var middleX = 0f
    var middleY = 0f
    var hourAngle = 0f
    var minuteAngle = 0f
    var secondAngle = 0f
    var ambientMode = false
    var backgroundImage: Bitmap
    var hourClockHandImage: Bitmap
    var minuteClockHandImage: Bitmap
    var secondClockHandImage: Bitmap


    init {
        backgroundPaint.apply {
            isAntiAlias = true
        }

        val options = BitmapFactory.Options().apply {
            inScaled = false
            inDither = false
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.rabbit, options)
        hourClockHandImage = BitmapFactory.decodeResource(resources, R.drawable.minute_hand, options)
        minuteClockHandImage = BitmapFactory.decodeResource(resources, R.drawable.hour_hand, options)
        secondClockHandImage = BitmapFactory.decodeResource(resources, R.drawable.second_hand, options)

        textPaint.apply {
            typeface = ResourcesCompat.getFont(applicationContext, R.font.circe_regular)
            isAntiAlias = true
            color = ContextCompat.getColor(applicationContext, R.color.toxic)
        }

        circlePaint.apply {
            color = ContextCompat.getColor(applicationContext, R.color.clockhand_color)
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        secondsPaint.apply {
            color = ContextCompat.getColor(applicationContext, R.color.toxic)
            isAntiAlias = true
            strokeWidth = 20f
            style = Paint.Style.STROKE
        }
    }

    fun draw(canvas: Canvas, bounds: Rect) {
        timeHelper.refresh()
        calculateParameters(bounds)
        drawBackground(canvas, bounds)
        if (!ambientMode)
            drawClockHand(canvas, bounds, secondAngle, secondClockHandImage, false)
        drawClockHand(canvas, bounds, minuteAngle, minuteClockHandImage)
        drawClockHand(canvas, bounds, hourAngle, hourClockHandImage)
        canvas.drawCircle(bounds.exactCenterX(), bounds.exactCenterY(), 5f, circlePaint)
    }

    fun calculateParameters(bounds: Rect) {
        radius = bounds.width() * 0.5.toFloat()
        middleX = bounds.width() * 0.5.toFloat()
        middleY = bounds.width() * 0.5.toFloat()

        secondAngle = (timeHelper.getSecond() * 6f + 180f) % 360f
        hourAngle = (timeHelper.getHour() * 30f + 180f) % 360f
        minuteAngle = (timeHelper.getMinute() * 6f + 180f) % 360f
    }

    fun drawBackground(canvas: Canvas, bounds: Rect) {
        canvas.drawColor(resources.getColor(R.color.black))
        canvas.drawBitmap(backgroundImage, null, bounds, backgroundPaint)
    }

    fun drawClockHand(canvas: Canvas, bounds: Rect, angle: Float, bitmap: Bitmap, needDx: Boolean = true) {
        val rotator = Matrix()
        rotator.reset()
        rotator.postRotate(angle)
        val dXAngle = Math.toRadians(angle.toDouble() - 180)
        val dYAngle = Math.toRadians(angle.toDouble() - 180)
        var dx = 0.0
        var dy = 0.0

        if (needDx) {
            dx = Math.cos(dXAngle) * hourClockHandImage.width / 2
            dy = Math.sin(dYAngle) * hourClockHandImage.width / 2
        }
        rotator.postTranslate(bounds.exactCenterX() + dx.toInt(), bounds.exactCenterY() + dy.toInt())
        canvas.drawBitmap(bitmap, rotator, backgroundPaint)
    }
}