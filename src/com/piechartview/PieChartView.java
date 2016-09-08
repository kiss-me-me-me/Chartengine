package com.piechartview;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.chartengine.R;
import com.example.chartengine.R.color;

/**
 * Created by Administrator on 2015/4/20.
 */
public class PieChartView extends View{
    //移动的值
    private final int MOVE_VALUE = 10;
    //视图的高度
    private int height;
    //视图的宽度
    private int width;

    //半坐标
    private int  halfX;
    private  int halfY;
    //中心点
    private Point contentPoint;
    //12点位置的点
    private Point topPoint;
    //6点钟位置
    private Point buttomPoint;

    //矩阵的高度 和 宽度
    private float rectHeight;
    private float rectWidth;

    //定义一个map集合，key值为图标的名称，value为图标弧度的值
    private Map<String,Float> chartMap;
    //保存rectf对象的集合
    private List<RectF> rectFs;
    //每个弧度的开始坐标点集合，结束坐标点集合
    private List<Point> startPointList;
    private List<Point> endPointList;

    //总角度
    private float altogetherAngle = 360f;
    //定义初始的角度
    private float initAngle;
    //当前结束时的角度
    private float currentEndAngle;

    //旋转视图时的起始旋转角度
    private float startRotate;
    
    private int  radius,circleColor,circleTextColor, drawArcColor1,drawArcColor2;//初始化矩阵的高度和宽度；小圆的颜色；小圆中心字的颜色，第一道弧形的颜色；第二道弧形的颜色
        

    public PieChartView(Context context) {
        super(context);
        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    
    public void setChartMap(Map<String,Float> chartMap,int radius,int circleColor,int circleTextColor,int drawArcColor1,int drawArcColor2){
        this.chartMap = chartMap;
//        for (int i = 0; i < chartMap.size(); i++) {
//        	float data = chartMap.get(""+i);
//        	if (data ==0) {
//				return;
//			}
//		}
        rectHeight = rectWidth = radius;
        this.circleColor = circleColor;
        this.circleTextColor = circleTextColor;
        this.drawArcColor1 = drawArcColor1;
        this.drawArcColor2 = drawArcColor2;
        invalidate();
    }

    //初始化方法
    private void init(Context context){
        //12点位置的起始角度为 -90%
        initAngle = -90.0f;
        //初始化起始旋转角度
        startRotate = -90.0f;

        //当前结束的角度 应该等于 初始的角度
        currentEndAngle = initAngle;

        //获取视图的高度和宽度
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        height = windowManager.getDefaultDisplay().getHeight();
        width = windowManager.getDefaultDisplay().getWidth();

        //圆心的X，Y也就是视图的中心
        halfX = (int) (width / 2);
        halfY = (int) (rectHeight / 2);
        contentPoint = new Point(halfX,halfY);
        topPoint = new Point(width,0);
        buttomPoint = new Point(width, (int) rectHeight);
        //初始化矩形集合
        rectFs = new ArrayList<RectF>();
        //初始化开始坐标点集合，结束坐标点集合
        startPointList = new ArrayList<Point>();
        endPointList = new ArrayList<Point>();

    }

    //重写onDraw方法
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        createData(canvas);
    }

    /**
     *根据数据去调用drawArc
     * */
    private void createData(Canvas canvas){
        if(null == chartMap){
            return ;
        }
        //初始角度
        float startAngle = 0.0f;
        float sweepAngle = 0.0f;
        //初始化画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        //便利数据源，根据I的值去给他们分配颜色，正式情况下应该重写这里
        for(int i = 0;i < chartMap.size();i++){
            switch (i){
                case 0:
                    paint.setColor(getResources().getColor(drawArcColor1));
                    break;
                case 1:
                    paint.setColor(getResources().getColor(drawArcColor2));
                    break;
                    default:
                    	paint.setColor(getResources().getColor(R.color.d));
                    break;
            }
            startAngle = currentEndAngle;
            //经过的角度，计算
            sweepAngle = altogetherAngle * chartMap.get(i+"");
            //重新为当前角度赋值
            // 在android中 12点位置的起始角度为 -90%  没有找到怎么把初始12点位置设置为0度 旋转360度的方法  NND
            currentEndAngle = startAngle + sweepAngle;

            //添加开始坐标点和结束坐标点
            Point startPoint = new Point();

            //调用画弧度的方法
            drawArc(canvas,paint,startAngle,sweepAngle,false);
            drawArc(canvas,paint,startAngle,sweepAngle,false);
            
           
            float data = chartMap.get("0");
            //保留两位小数
            DecimalFormat   fnum  =   new  DecimalFormat("##0.00");    
            String   dataRelust = fnum.format(data * 100)+"%";
            
            Paint p = new Paint();
      		p.setColor(circleColor);// 设置红色
    		p.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
            canvas.drawCircle(contentPoint.x, rectHeight/2, rectWidth/4, p);
        	p.setColor(circleTextColor);// 设置红色
    		p.setTextSize(50);
    		canvas.drawText(dataRelust, width/2-50, rectHeight/2+20, p);// 画文本
        }
    }

    /**
     * 定义画弧度的方法
     * @param canvas 画布
     * @param paint 画笔
     * @param startAngle 弧度初始角度
     * @param sweepAngle 弧度经过角度
     * @param isBulge 是否凸出
     * */
    private void drawArc(Canvas canvas,Paint paint,float startAngle,float sweepAngle,boolean isBulge){
        if(!isBulge){
//            RectF rectF = new RectF(0,0,rectWidth,rectHeight);
            RectF rectF = new RectF(width/2-rectWidth/2,0,width/2+rectWidth/2,rectHeight);
            //把每一个矩阵都放入矩阵集合中
            rectFs.add(rectF);
            canvas.drawArc(rectF,startAngle,sweepAngle,true,paint);
        }else{
            RectF rectF = new RectF(width/2-rectWidth/2,0,width/2+rectWidth/2 + 20,rectHeight + 20);
            //把每一个矩阵都放入矩阵集合中
            rectFs.add(rectF);
            canvas.drawArc(rectF,startAngle + 10,sweepAngle - 10,true,paint);
        }
    }

    private float downX;
    private float downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                calculationRectf(event.getX(),event.getY());
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float moveX = event.getX() - downX;
                final float moveY = event.getY() - downY;
                downX = event.getX();
                downY = event.getY();
                //如果是在上右
                if(downY <= rectHeight / 2 && downX >= rectWidth / 2){
                    if(moveY >= 0 && moveX >= 0){
                        currentEndAngle += MOVE_VALUE;
                    }else if(moveY <= 0 && moveX <= 0){
                        currentEndAngle -= MOVE_VALUE;
                    }
                }
                //如果是在下右
                else if(downY >= rectHeight / 2 && downX >= rectWidth / 2){
                    if(moveY >= 0 && moveX <= 0){
                        currentEndAngle += MOVE_VALUE;
                    }else if(moveY <= 0 && moveX >= 0){
                        currentEndAngle -= MOVE_VALUE;
                    }
                }
                //如果实在下左
                else if(downY >= rectHeight / 2 && downX <= rectWidth / 2){
                    if(moveY <= 0 && moveX <= 0){
                        currentEndAngle += MOVE_VALUE;
                    }else if(moveY >= 0 && moveX >= 0){
                        currentEndAngle -= MOVE_VALUE;
                    }
                }
                //如果实在上左
                else if(downY <= rectHeight / 2 && downX <= rectWidth / 2){
                    if(moveY >= 0 && moveX <= 0){
                        currentEndAngle -= MOVE_VALUE;
                    }else if(moveY <= 0 && moveX >= 0){
                        currentEndAngle += MOVE_VALUE;
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upMethod();
                break;
        }
        return true;
    }

    /**
     * 根据点击的点，去计算出当前这个点的角度
     * 通过按下的那个点 和圆的中心点，按下的点向左横向引出与12点位置组合而成的线
     * */
    private void calculationRectf(float x,float y){
//        int x1= (int) Math.abs(topPoint.x-x);
//        int y1= (int) Math.abs(contentPoint.y-y);
//        double z1=Math.sqrt(x1*x1+y1*y1);
//        int jiaodu=Math.round((float)(Math.asin(y1/z1)/Math.PI*180));
//        System.out.println("topPoint.x："+topPoint.x);
//        System.out.println("topPoint.y:"+topPoint.y);
//        System.out.println("x："+x);
//        System.out.println("y："+y);
//        System.out.println("jiaodu："+jiaodu);
        //便利这一个集合判断当前的这一个点属于哪一个对象之中
        for(int i = 0; i <rectFs.size() ; i++){
            //找到这个对象
            if(rectFs.get(i).contains(x,y)){
                rotateView();
                break;
            }
        }
    }

    /***
     * 转动view的方法
     * */
    private void rotateView(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(10);
                        currentEndAngle ++;
                        myHandler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    /**
     * 停止时调用的方法
     * */
    private void upMethod(){
        //停止时判断底部定点的坐标在那个arc的坐标之下
    }

    /**
     * 更改UI
     * */
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };
}
