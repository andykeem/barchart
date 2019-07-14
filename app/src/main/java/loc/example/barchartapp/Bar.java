package loc.example.barchartapp;

import android.graphics.RectF;

import java.text.NumberFormat;
import java.util.Locale;

public class Bar {

    private String month;
    private String spend;
    private RectF rect;
    private boolean current;

    public Bar(String month, String spend) {
        this.month = month;
        this.spend = spend;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSpend() {
        return spend;
    }

    public float getSpendValue() {
        return Float.valueOf(spend);
    }

    public String getSpendAmount() {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return format.format(getSpendValue());
    }

    public void setSpend(String spend) {
        this.spend = spend;
    }

    public RectF getRect() {
        return rect;
    }

    public void setRect(RectF rect) {
        this.rect = rect;
    }

    public void setRect(float left, float top, float right, float bottom) {
        rect = new RectF(left, top, right, bottom);
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean flag) {
        current = flag;
    }
}