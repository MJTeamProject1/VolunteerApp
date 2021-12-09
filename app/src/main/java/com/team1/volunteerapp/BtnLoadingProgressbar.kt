package com.team1.volunteerapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.animation.doOnEnd
import kotlin.math.roundToInt

class BtnLoadingProgressbar(view : View) {
    val layout = view as LinearLayout
    val progressbar = view.findViewById<ProgressBar>(R.id.btn_loading_layout_progressbar)
    val textview = view.findViewById<TextView>(R.id.btn_loading_layout_tv)
    val ivDone = view.findViewById<ImageView>(R.id.btn_loading_layout_iv)

    fun setLoading(){
        layout.isEnabled = false
        textview.visibility = View.GONE
        progressbar.visibility = View.VISIBLE
        ivDone.visibility = View.GONE

        progressbar.scaleX = 1f
        ivDone.scaleX = 0f
    }

    fun setState(isSuccess:Boolean, onAnimationEnd: ()->Unit){
        val v = layout.background
        val bgColor = if(isSuccess)
            Color.parseColor("#66BB6A")
        else
            Color.parseColor("#E53935")

        val bgAnim = ObjectAnimator.ofFloat(0f,1f).setDuration(600L)
        bgAnim.addUpdateListener {
            val mul = it.animatedValue as Float
            v.colorFilter = PorterDuffColorFilter(adjustAlpha(bgColor,mul),PorterDuff.Mode.SRC_ATOP)
        }
        bgAnim.start()
        bgAnim.doOnEnd {
            if(isSuccess)
                filpProgressBar(R.drawable.ic_baseline_done_24,R.drawable.btn_loading_layout_bg_success){if(it) onAnimationEnd()}
            else
                filpProgressBar(R.drawable.ic_baseline_close_24,R.drawable.btn_loading_layout_bg_error){if(it) onAnimationEnd()}
        }

    }

    private fun filpProgressBar(img: Int, imagebackgroundDrawable: Int, isEnded:(Boolean)->Unit) {
        ivDone.setBackgroundResource(imagebackgroundDrawable)
        ivDone.setImageResource(img)

        val flip1 = ObjectAnimator.ofFloat(progressbar, "scaleX", 1f,0f)
        val flip2 = ObjectAnimator.ofFloat(ivDone, "scaleX", 0f,1f)
        flip1.duration = 400
        flip2.duration = 400
        flip1.interpolator = DecelerateInterpolator()
        flip2.interpolator = AccelerateDecelerateInterpolator()
        flip1.addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                progressbar.visibility = View.GONE
                ivDone.visibility = View.VISIBLE
                flip2.start()
            }
        })
        flip1.start()
        flip2.doOnEnd { isEnded(true) }
    }

    fun reset(){
        textview.visibility = View.VISIBLE
        progressbar.visibility = View.GONE
        ivDone.visibility = View.GONE
        progressbar.scaleX = 1f
        ivDone.scaleX = 0f
        layout.background.clearColorFilter()
        layout.isEnabled = true
    }

    private fun adjustAlpha(bgColor: Int, mul: Float): Int {
        val a  = (Color.alpha(bgColor)*mul).roundToInt()
        val red = Color.red(bgColor)
        val green = Color.green(bgColor)
        val blue = Color.blue(bgColor)
        return Color.argb(a,red, green, blue)
    }
}