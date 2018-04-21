package ui.anwesome.com.rectbridgeview

/**
 * Created by anweshmishra on 21/04/18.
 */

import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*

class RectBridgeView (ctx : Context) : View(ctx) {
    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val renderer : Renderer = Renderer(this)
    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class State (private var prevScale : Float = 0f, var dir : Float = 0f, var j : Int = 0) {
        val scales : Array<Float> = arrayOf(0f, 0f, 0f, 0f, 0f)
        fun update (stopcb : (Float) -> Unit) {
            scales[j] += dir * 0.1f
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += dir.toInt()
                if (j == scales.size || j == -1) {
                    j -= dir.toInt()
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(prevScale)

                }
            }
        }
        fun startUpdating (startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }
    data class Animator(var view : View, var animated : Boolean = false) {
        fun animate (updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex : Exception) {

                }
            }
        }
        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class RectBridge(var i : Int, private val state : State = State()) {
        fun draw(canvas : Canvas, paint : Paint) {
            val k : Float = 3f
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val y1 : Float = 0.9f * h
            val y2 : Float = 0.75f * h
            val lw : Float = 0.02f * h
            canvas.save()
            canvas.translate(0f, -0.3f * h)
            paint.strokeWidth = lw
            paint.strokeCap = Paint.Cap.ROUND
            for(i in 0..1) {
                canvas.drawRect(i * w * (1 -   ((state.scales[0]) / k)) , y2, w * i + w/k * state.scales[0] * (1 - i), y1, paint)
            }
            val xLine : Float = (w / (2 * k)) * state.scales[1]
            val yk1 : Float = y1 - lw/2
            val yk2 : Float = y2 + lw/2
            canvas.save()
            canvas.translate(w/2, yk1  + (yk2 - yk1)  * this.state.scales[2])
            canvas.drawLine(-xLine, 0f, xLine, 0f, paint)
            canvas.restore()
            val rectSize : Float = Math.min(w,h)/12
            canvas.save()
            canvas.translate((w - rectSize) * this.state.scales[4], yk2 - rectSize)
            val mid : Float = rectSize/2
            val sq : Float = mid * state.scales[3]
            canvas.drawRect(RectF(mid - sq, mid - sq, mid + sq, mid + sq), paint)
            canvas.restore()
            canvas.restore()
        }
        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }

    data class Renderer(var view : RectBridgeView) {
        private val rectBridge : RectBridge = RectBridge(0)
        private val animator : Animator = Animator(view)
        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            paint.color = Color.parseColor("#1976D2")
            rectBridge.draw(canvas, paint)
            animator.animate {
                rectBridge.update {
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            rectBridge.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun create(activity : Activity) : RectBridgeView {
            val view : RectBridgeView = RectBridgeView(activity)
            activity.setContentView(view)
            return view
        }
    }
}