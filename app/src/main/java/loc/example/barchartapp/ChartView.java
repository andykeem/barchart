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
import java.util.List;

public class ChartView extends View {

    private static final String TAG = ChartView.class.getSimpleName();
    private static final int PADDING = 24;

    private Paint mBarPaint;
    private Paint mCurrMoBarPaint;
    private float mMaxSpend;
    private float mBarWidth;
    private int mCanvasHeight;
    private int mCanvasWidth;
    private int mBarBottom;
    private boolean mPrepared;
    private List<Float> mSpends;
    private int mNumSpends;
    private List<Float> mBarTops = new ArrayList<>();
    private int mAmountHeight;
    private int mBarHeight;
    private int mMonthHeight;
    private List<Bar> mBars;

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
        mBarPaint = new Paint();
        mBarPaint.setColor(Color.LTGRAY);

        mCurrMoBarPaint = new Paint();
        mCurrMoBarPaint.setColor(Color.GRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        prepare();

        float left = PADDING;
        for (int i = 0; i < mNumSpends; i++) {

            RectF rect = new RectF();
            rect.bottom = mBarBottom;
            rect.left = left;
            rect.right = left + mBarWidth;
            rect.top = mBarTops.get(i);

            if (i == (mNumSpends - 1)) {
                canvas.drawRect(rect, mCurrMoBarPaint);
            } else {
                canvas.drawRect(rect, mBarPaint);
            }
            left = rect.right + PADDING;
        }
    }

    public void setSpends(List<Float> spends) {
        mSpends = spends;
    }

    private void prepare() {
        if (mPrepared) {
            return;
        }

        mCanvasHeight = getHeight();
        mCanvasWidth = getWidth();

        mAmountHeight = 200;
        mMonthHeight = 200;
        mBarHeight = mCanvasHeight - mMonthHeight;
        mBarBottom = mBarHeight;

        mNumSpends = mSpends.size();
        mBarWidth = (mCanvasWidth - (PADDING * (mNumSpends + 1))) / mNumSpends;

        mMaxSpend = Collections.max(mSpends);
        for (int i = 0; i < mNumSpends; i++) {
            float val = mSpends.get(i);
            float percent = (val / mMaxSpend);
            log(String.format("percent: %f", percent));
            val = mBarHeight * (1f - percent) + (mAmountHeight * percent);
            mBarTops.add(val);
        }

        String msg = String.format("canvas H: %d, bar H: %d, bar bottom: %d, tops: %s",
                mCanvasHeight, mBarHeight, mBarBottom, mBarTops);
        log(msg);

        mPrepared = true;
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }

    public void setBars(List<Bar> bars) {
        mBars = bars;
    }
}
