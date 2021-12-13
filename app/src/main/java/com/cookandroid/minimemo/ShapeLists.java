package com.cookandroid.minimemo;

import android.graphics.Paint;

public class ShapeLists {

    int curShape;
    int startX;
    int startY;
    int stopX;
    int stopY;
    Paint paint;

    //그린 도형의 모양, 위치, 색 등 저장
    public ShapeLists(int curShape, int startX, int startY, int stopX, int stopY, Paint paint) {
        this.curShape = curShape;
        this.startX = startX;
        this.startY = startY;
        this.stopX = stopX;
        this.stopY = stopY;
        this.paint = paint;
    }
}
