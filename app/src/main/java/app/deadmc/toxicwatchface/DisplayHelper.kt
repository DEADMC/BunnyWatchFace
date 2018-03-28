package app.deadmc.toxicwatchface

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat





class DisplayHelper(val applicationContext:Context) {

    val backgroundPaint = Paint()
    val textPaint = Paint()
    val secondsPaint = Paint()
    val resources = applicationContext.resources

    val timeHelper = TimeHelper()

    var radius = 0f
    var middleX = 0f
    var middleY = 0f
    var hourAngle = 0f
    var minuteAngle = 0f
    var secondAngle = 0f
    var ambientMode = false
    var backgroundImage:Bitmap
    var clockHandImage:Bitmap

    init {
        backgroundPaint.apply {
            //color = ContextCompat.getColor(applicationContext, R.color.background)
            isAntiAlias = true
        }

        val options = BitmapFactory.Options().apply {
            inScaled = false
            inDither = false
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background,options)
        clockHandImage = BitmapFactory.decodeResource(resources, R.drawable.clock_hand,options)

        textPaint.apply {
            typeface = ResourcesCompat.getFont(applicationContext,R.font.circe_regular)
            isAntiAlias = true
            color = ContextCompat.getColor(applicationContext, R.color.toxic)
        }

        secondsPaint.apply {
            color = ContextCompat.getColor(applicationContext,R.color.toxic)
            isAntiAlias = true
            strokeWidth = 20f
            style = Paint.Style.STROKE
        }
    }

    fun draw(canvas: Canvas, bounds: Rect) {
        timeHelper.refresh()
        calculateParameters(bounds)
        drawBackground(canvas,bounds)
    }

    fun calculateParameters(bounds: Rect) {
        radius = bounds.width()*0.5.toFloat()
        middleX = bounds.width()*0.5.toFloat()
        middleY = bounds.width()*0.5.toFloat()
        hourAngle = (360f-timeHelper.getHour()*6f)%360f
        minuteAngle = (360f-timeHelper.getMinute()*6f)%360f
        secondAngle = (360f-timeHelper.getSecond()*6f)%360f
    }

    fun drawBackground(canvas: Canvas, bounds: Rect) {
        //canvas.drawBitmap(backgroundImage,0f,0f,null)
        canvas.drawBitmap(backgroundImage,null,bounds,backgroundPaint)

        canvas.drawArc(0f, 0f, bounds.width().toFloat(), bounds.height().toFloat(),-90f, secondAngle,false,secondsPaint)


        /*
        if (ambientMode) {
            canvas.drawColor(Color.BLACK)
        } else {
            canvas.drawRect(
                    0f, 0f, bounds.width().toFloat(), bounds.height().toFloat(), backgroundPaint)
        }
        */

    }

    fun drawHourClockHand(canvas: Canvas, bounds: Rect) {

    }
}


