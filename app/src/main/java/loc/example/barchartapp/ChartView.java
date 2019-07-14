package loc.example.barchartapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChartView extends View {

    private static final String TAG = ChartView.class.getSimpleName();
    private static final int PADDING = 24;
    private static final int AMOUNT_HEIGHT = 200;
    private static final int MONTH_HEIGHT = 200;

    private Paint mPaintBar;
    private Paint mPaintCurrMoBar;
    private Paint mPaintText;

    private float mMaxSpend;
    private float mBarWidth;
    private float mBarWidthHalf;
    private int mCanvasHeight;
    private int mCanvasWidth;
    private int mBarBottom;
    private boolean mDrawPrepared;

    private int mBarHeight;
    private List<Bar> mBars;
    private int mNumBars;

    public ChartView(Context context) {
        super(context);
        init();
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        mPaintBar = new Paint();
        mPaintBar.setColor(Color.LTGRAY);

        mPaintCurrMoBar = new Paint();
        mPaintCurrMoBar.setColor(Color.GRAY);

        mPaintText = new Paint();
        mPaintText.setColor(Color.BLACK);
        float textSize = 40;
        mPaintText.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        prepareDraw();

        for (int i = 0; i < mNumBars; i++) {

            Bar bar = mBars.get(i);
            drawSpendAmount(canvas, bar);

            if (bar.isCurrent()) {
                canvas.drawRect(bar.getRect(), mPaintCurrMoBar);
            } else {
                canvas.drawRect(bar.getRect(), mPaintBar);
            }

            drawMonth(canvas, bar);
        }
    }

    private void drawMonth(Canvas canvas, Bar bar) {
        RectF rect = bar.getRect();
        String month = bar.getMonth();
        float monthWidth = mPaintText.measureText(month);
        float monthStartX = mBarWidthHalf - (monthWidth / 2);
        canvas.drawText(month, rect.left + monthStartX, rect.bottom + 56, mPaintText);
    }

    private void drawSpendAmount(Canvas canvas, Bar bar) {
        RectF rect = bar.getRect();
        String amount = bar.getSpendAmount();
        float spendWidth = mPaintText.measureText(amount);
        float spendStartX = mBarWidthHalf - (spendWidth / 2);
        canvas.drawText(amount, rect.left + spendStartX, rect.top - 24, mPaintText);
    }

    private void prepareDraw() {
        if (mDrawPrepared) {
            return;
        }

        mCanvasHeight = getHeight();
        mCanvasWidth = getWidth();

        mBarHeight = mCanvasHeight - MONTH_HEIGHT;
        mBarBottom = mBarHeight;

//        mNumSpends = mSpends.size();
        mNumBars = mBars.size();
        mBarWidth = (mCanvasWidth - (PADDING * (mNumBars + 1))) / mNumBars;
        mBarWidthHalf = mBarWidth / 2;

        mMaxSpend = getMaxSpend();

        float left = PADDING;
        for (int i = 0; i < mNumBars; i++) {

            Bar bar = mBars.get(i);

            float bottom = mBarBottom;
            float right = left + mBarWidth;
            float top = getBarTop(bar);

            bar.setRect(left, top, right, bottom);
            if (i == (mNumBars - 1)) {
                bar.setCurrent(true);
            }
            left = right + PADDING;
        }

        String msg = String.format("canvas H: %d, bar H: %d, bar bottom: %d",
                mCanvasHeight, mBarHeight, mBarBottom);
        log(msg);

        mDrawPrepared = true;
    }

    private float getBarTop(Bar bar) {
        float val = bar.getSpendValue();
        float percent = (val / mMaxSpend);
        log(String.format("percent: %f", percent));
        val = mBarHeight * (1f - percent) + (AMOUNT_HEIGHT * percent);
        Log.d(TAG, "bar top: " + val);
        return val;
    }

    private float getMaxSpend() {
        float val = 0.0f;
        Bar bar = Collections.max(mBars, new Comparator<Bar>() {
            @Override
            public int compare(Bar o1, Bar o2) {
                return Float.compare(o1.getSpendValue(), o2.getSpendValue());
            }
        });
        return bar.getSpendValue();
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }

    public void setBars(List<Bar> bars) {
        mBars = bars;
    }
}
