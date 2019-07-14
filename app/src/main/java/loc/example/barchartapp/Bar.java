package loc.example.barchartapp;

import android.graphics.RectF;

public class Bar {

    private String month;
    private String spend;
    private RectF rect;

    public Bar(String month, String spend) {
        this.month = month;
        this.spend = spend;
    }

    public RectF getRect() {
        return rect;
    }

    public void setRect(float left, float top, float right, float bottom) {
        rect = new RectF(left, top, right, bottom);
    }
}
