package ui.anwesome.com.rectbridgeview

/**
 * Created by anweshmishra on 21/04/18.
 */

import android.view.*
import android.content.*
import android.graphics.*

class RectBridgeView (ctx : Context) : View(ctx) {
    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas : Canvas) {

    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}