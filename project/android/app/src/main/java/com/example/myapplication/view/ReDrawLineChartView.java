package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ReDrawLineChartView extends View {
    // 画笔
    private Paint paint;
    // 刻度之间的距离
    private int degreeSpace =0;
    // 模拟数据
    private float[] data = {3.2f, 4.3f, 2.5f, 3.2f, 3.8f, 7.1f, 1.3f, 5.6f};
    // 当前显示的数据数量
    private int showNum=data.length;

    /**
     *  构造函数
     */
    public ReDrawLineChartView(Context context) {
        super(context);
        initWork();
    }

    /**
     *  构造函数
     */
    public ReDrawLineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initWork();
    }

    /**
     *  构造函数
     */
    public ReDrawLineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWork();
    }

    /**
     *  初始化工作
     */
    private void initWork() {
        initPaint();
    }

    /**
     *  画笔设置
     */
    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 画笔样式为填充
        paint.setStyle(Paint.Style.FILL);
        // 颜色设为红色
        paint.setColor(Color.GRAY);
        // 宽度为3像素
        paint.setStrokeWidth(3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 控件上下左右边界四至及控件的宽度(同时也是高度!)
        int left = getLeft();
        int right = getRight();
        int top = getTop();
        int bottom = getBottom();
        int w = getWidth();

        // 图表距离控件边缘的距离
        int graphPadding = w / 10;
        // 图表上下左右四至
        int graphLeft = left;
        int graphBottom = bottom - graphPadding;
        int graphRight = right;
        int graphTop = top + graphPadding;
        // 图表宽度(也等同高度奥~)
        int graphW = graphRight - graphLeft;
        // 背景
        canvas.drawColor(Color.WHITE);
        // 画笔设置样式为STROKE样式，即只划线不填充
        paint.setStyle(Paint.Style.STROKE);

        // 坐标系绘制
        Path pivotPath = new Path();
        //Y轴
//        pivotPath.moveTo(graphLeft, graphBottom);
//        pivotPath.lineTo(graphLeft, graphTop);
        //Y轴箭头
//        pivotPath.lineTo(graphLeft - 12, graphTop + 20);
//        pivotPath.moveTo(graphLeft, graphTop);
//        pivotPath.lineTo(graphLeft + 12, graphTop + 20);
        //X轴
        pivotPath.moveTo(graphLeft, graphBottom);
        pivotPath.lineTo(graphRight, graphBottom);
        //X轴箭头
//        pivotPath.lineTo(graphRight - 20, graphBottom + 12);
//        pivotPath.moveTo(graphRight, graphBottom);
//        pivotPath.lineTo(graphRight - 20, graphBottom - 12);
        canvas.drawPath(pivotPath, paint);
        // Y轴刻度虚线
        for (int i = 1; i < 8; i++) {
            Path yKeduPath = new Path();
            // 线
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.STROKE);
            paint.setPathEffect(new DashPathEffect(new float[]{5,5},0));
            yKeduPath.moveTo(graphLeft, graphBottom - i * degreeSpace);
            yKeduPath.lineTo(graphRight, graphBottom - i * degreeSpace);
            canvas.drawPath(yKeduPath, paint);
            // 数字
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setPathEffect(null);
            canvas.drawText( "", graphPadding / 2, graphBottom - i * degreeSpace, paint);
        }
        // 刻度之间的距离
        degreeSpace = graphW / data.length;
        // X轴刻度虚线
        for (int i = 1; i < 8; i++) {
            Path xKeduPath = new Path();
            // 线
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1);
            paint.setPathEffect(new DashPathEffect(new float[]{5,5},0));
            xKeduPath.moveTo(graphLeft + i * degreeSpace, graphBottom);
            xKeduPath.lineTo(graphLeft + i * degreeSpace, graphTop);
            canvas.drawPath(xKeduPath, paint);
            // 数字
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(25);
            paint.setPathEffect(null);
            canvas.drawText(i + "", graphLeft + i * degreeSpace, graphBottom + graphPadding / 2, paint);
        }
        // 折线
        Path linePath = new Path();
        for (int i = 0; i < showNum; i++) {
            int toPointX = graphLeft + i * degreeSpace;
            int toPointY = graphBottom - ((int) (data[i] * degreeSpace));
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            if (i==0){
                linePath.moveTo(toPointX,toPointY);
            }else {
                linePath.lineTo(toPointX, toPointY);
            }
            // 节点圆圈
            canvas.drawCircle(toPointX, toPointY,10,paint);
            paint.setColor(Color.YELLOW);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(toPointX,toPointY,7,paint);
        }
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawPath(linePath, paint);
    }
}
