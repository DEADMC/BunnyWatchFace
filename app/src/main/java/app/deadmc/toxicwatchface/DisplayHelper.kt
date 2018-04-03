package app.deadmc.toxicwatchface

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import kotlin.math.cos
import kotlin.math.sin


class DisplayHelper(val applicationContext:Context) {

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
    var backgroundImage:Bitmap
    var hourClockHandImage:Bitmap
    var minuteClockHandImage:Bitmap
    var secondClockHandImage:Bitmap


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
        backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.rabbit,options)
        hourClockHandImage = BitmapFactory.decodeResource(resources, R.drawable.minute_hand,options)
        minuteClockHandImage = BitmapFactory.decodeResource(resources, R.drawable.hour_hand,options)
        secondClockHandImage = BitmapFactory.decodeResource(resources, R.drawable.second_hand,options)

        textPaint.apply {
            typeface = ResourcesCompat.getFont(applicationContext,R.font.circe_regular)
            isAntiAlias = true
            color = ContextCompat.getColor(applicationContext, R.color.toxic)
        }

        circlePaint.apply {
            color = ContextCompat.getColor(applicationContext,R.color.clockhand_color)
            isAntiAlias = true
            style = Paint.Style.FILL
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
        drawClockHand(canvas,bounds,secondAngle,secondClockHandImage)
        drawClockHand(canvas,bounds,minuteAngle,minuteClockHandImage)
        drawClockHand(canvas,bounds,hourAngle,hourClockHandImage)
        canvas.drawCircle(bounds.exactCenterX(),bounds.exactCenterY(),15f,circlePaint)
    }

    fun calculateParameters(bounds: Rect) {
        radius = bounds.width()*0.5.toFloat()
        middleX = bounds.width()*0.5.toFloat()
        middleY = bounds.width()*0.5.toFloat()
        hourAngle = (timeHelper.getHour()*30f+180f)%360f
        minuteAngle = (timeHelper.getMinute()*6f+180f)%360f
        secondAngle = (timeHelper.getSecond()*6f+180f)%360f

        Log.e("time","minute "+timeHelper.getMinute()+" angle $minuteAngle")
        Log.e("time","second "+timeHelper.getSecond()+" angle $secondAngle")
        Log.e("time","hour "+timeHelper.getHour()+" angle $hourAngle")
    }

    fun drawBackground(canvas: Canvas, bounds: Rect) {
        //canvas.drawBitmap(backgroundImage,0f,0f,null)
        canvas.drawColor(resources.getColor(R.color.black))
        canvas.drawBitmap(backgroundImage,null,bounds,backgroundPaint)


        //canvas.drawArc(0f, 0f, bounds.width().toFloat(), bounds.height().toFloat(),-90f, secondAngle,false,secondsPaint)



        /*
        if (ambientMode) {
            canvas.drawColor(Color.BLACK)
        } else {
            canvas.drawRect(
                    0f, 0f, bounds.width().toFloat(), bounds.height().toFloat(), backgroundPaint)
        }
        */

    }

    fun drawClockHand(canvas: Canvas, bounds: Rect,angle:Float,bitmap:Bitmap) {
        //canvas.rotate(90f)
        val rotator = Matrix()
        rotator.reset()
        rotator.postRotate(angle)
        //rotator.postRotate(0f,bounds.exactCenterX()*2-hourClockHandImage.width,bounds.exactCenterY()-hourClockHandImage.height)
        //rotator.setRotate(secondAngle,bounds.exactCenterX(),bounds.exactCenterY())
        rotator.postTranslate(bounds.exactCenterX()+bitmap.width* cos(angle)/2, bounds.exactCenterY()-bitmap.width* sin(angle)/2)
        //canvas.drawBitmap(hourClockHandImage,bounds.exactCenterX()-hourClockHandImage.width/2,bounds.exactCenterY()-hourClockHandImage.height/2,backgroundPaint)
        canvas.drawBitmap(bitmap,rotator,backgroundPaint)
        //canvas.rotate(0f)
    }
}


