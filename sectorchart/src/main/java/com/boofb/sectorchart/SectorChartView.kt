package com.boofb.sectorchart

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.IntRange
import kotlin.math.min


class SectorChartView : View {


    private var mViewData: List<SectorDataInterface>? = null
    private var mSectorPaint: Paint? = null

    /**
     * 总数值
     */
    private var mTotalValue: Double = 1.0

    private var mInnerRadius = dp2px(80F)
    private var mOutRadius = dp2px(120F)
    private var mAnimDuration: Int = 1000
    private var mAnimType: Int = 1

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        context?.obtainStyledAttributes(attrs, R.styleable.SectorChartView, 0, 0)?.let {

            mOutRadius = it.getDimension(R.styleable.SectorChartView_scv_out_radius, mOutRadius)
            mInnerRadius =
                it.getDimension(R.styleable.SectorChartView_scv_inner_radius, mInnerRadius)
            mAnimDuration = it.getInt(R.styleable.SectorChartView_scv_anim_duration, mAnimDuration)
            mAnimType = it.getInt(R.styleable.SectorChartView_scv_anim_type, 1)
            it.recycle()
        }

        mSectorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mSectorPaint?.style = Paint.Style.STROKE

        mSectorPaint?.strokeWidth = mOutRadius - mInnerRadius
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val v = min(widthMeasureSpec, heightMeasureSpec)

        super.onMeasure(v, v)

    }

    /**
     * 设置数据
     */
    fun setViewData(dataList: List<SectorDataInterface>): Unit {
        mViewData = dataList
        handleData()
        invalidate()
        if (mAnimType == 1) {
            startAnim()
        } else {
            startAnim2()
        }
    }


    /**
     * 处理数据
     */
    private fun handleData() {

        if (mViewData.isNullOrEmpty()) {
            return
        }

        val sum = mViewData!!.sumOf {
            it.getSectorValue()
        }
        mTotalValue = sum
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawAllArc(canvas)
    }

    /**
     * 绘制所有弧形
     */
    private fun drawAllArc(canvas: Canvas) {

        val oval = RectF(
            width / 2F - mOutRadius,
            height / 2F - mOutRadius,
            width / 2F + mOutRadius,
            height / 2F + mOutRadius
        )

        var lastAngle = -90F
        var totalSweepAngle = 0.0

        mViewData?.let { dataList ->
            for (m in dataList) {
                mSectorPaint?.color = m.getSectorColor()
                var sweepAngle = (m.getSectorValue() / mTotalValue) * 360F
                var needReturn = false
                if ((totalSweepAngle + sweepAngle) > mCurrentSweepAngle) {
                    needReturn = true
                    sweepAngle = mCurrentSweepAngle - totalSweepAngle
                }
                totalSweepAngle += sweepAngle

                canvas.drawArc(
                    oval,
                    lastAngle,
                    sweepAngle.toFloat() * mCurrentAnimSlide,
                    false,
                    mSectorPaint!!
                )
                if (needReturn) {
                    return
                }
                lastAngle += sweepAngle.toFloat()
            }
        }
    }

    private var mCurrentAnimSlide = 1F
    private var mCurrentSweepAngle = 360F

    private fun startAnim() {

        val animator = ValueAnimator.ofFloat(0F, 1F)
        animator.duration = 600
        animator.addUpdateListener {
            mCurrentAnimSlide = it.animatedValue as Float
            invalidate()
        }
        animator.interpolator = DecelerateInterpolator()

        animator.start()
    }

    private fun startAnim2() {
        val animator = ValueAnimator.ofFloat(0F, 360F)
        animator.duration = 2000
        animator.addUpdateListener {
            mCurrentSweepAngle = it.animatedValue as Float
            invalidate()
        }
        animator.interpolator = DecelerateInterpolator()

        animator.start()
    }

    fun setInnerRadius(dp: Float): Unit {
        mInnerRadius = dp
    }

    fun setOutRadius(dp: Float): Unit {
        mOutRadius = dp
    }

    fun setAnimDuration(duration: Int): Unit {
        mAnimDuration = duration
    }

    fun setAnimType(@IntRange(from = 1, to = 2) type: Int) {
        mAnimType = type
    }

    private fun dp2px(p: Float): Float {
        return p * resources.displayMetrics.density + 0.5f
    }

    private fun dp2px(p: Int): Int {
        return (p * resources.displayMetrics.density + 0.5).toInt()
    }
}