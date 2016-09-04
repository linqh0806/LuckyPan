package linqh.luckypan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by dz1 on 2016/1/5.
 */
public class LuckPan extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    //绘制的画布
    private Canvas mCanvs;
    private SurfaceHolder mHolder;
    //用于绘制的线程和标志
    private Thread mThread;
    private boolean isRunning;

    //绘图用的图文
    private int[] mImages = new int[]{R.drawable.danfan, R.drawable.f040, R.drawable.iphone, R.drawable.f040, R.drawable.meizi, R.drawable.f040};
    private int[] mColors = new int[]{0xFF13581a, 0xffeab483, 0xFF13581a, 0xffeab483, 0xFF13581a, 0xffeab483};
    private String[] mStrs = new String[]{"单反相机", "恭喜发财", "IPHONE", "恭喜发财", "一套衣服", "恭喜发财"};
    private Bitmap[] mImageBitmaps;
    private Bitmap BgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
    private int mItemCount = 6;
    private Paint mArcPaint;
    private Paint mTextPaint;
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());

    //控件在布局 范围设置
    private RectF mRectf;
    private int mPadding;
    private int mCenter;
    private int mRadius;

    //原配转动时需要的参数
    private float mStartAngle;
    private float mSweepAngle;
    private float mSpeed;
    private boolean isShouldEnd;

    interface mLuckPan{
        void stop();
        void start();
    }

    public LuckPan(Context context) {
        super(context, null);
    }

    public LuckPan(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);

        setFocusable(true);
        setFocusableInTouchMode(true);

        setKeepScreenOn(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mPadding = getPaddingLeft();
        mRadius = width - mPadding * 2;
        mRectf = new RectF(mPadding, mPadding, width - mPadding, width - mPadding);
        mCenter=width/2;
        setMeasuredDimension(width, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setTextSize(mTextSize);

        mImageBitmaps=new Bitmap[mItemCount];
        for(int i=0;i<mItemCount;i++){
            mImageBitmaps[i]=BitmapFactory.decodeResource(getResources(),mImages[i]);
        }

        mThread = new Thread(this);
        mThread.start();

    }

    @Override
    public void run() {
        while (isRunning) {
            long start=System.currentTimeMillis();
            draw();
            long end=System.currentTimeMillis();
            if((end-start)<50){
                try {
                    Thread.sleep(50-(end-start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void draw() {
        try {
            mCanvs = mHolder.lockCanvas();
            if (mCanvs != null) {
                drawBg();
                float tmpAngle=mStartAngle;
                mSweepAngle = 360 / mItemCount;
                for (int i = 0; i < mItemCount; i++) {
                    mArcPaint.setColor(mColors[i]);
                    mCanvs.drawArc(mRectf, tmpAngle, mSweepAngle, true, mArcPaint);
                    drawText(tmpAngle, mSweepAngle, mStrs[i]);
                    drawIcon(tmpAngle, mImageBitmaps[i]);
                    tmpAngle+=mSweepAngle;
                }
                mStartAngle+=mSpeed;
                if(isShouldEnd){
                    mSpeed-=1;
                }
                if(mSpeed<=0){
                    mSpeed=0;
                    isShouldEnd=false;
                }

            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (mCanvs != null)
                mHolder.unlockCanvasAndPost(mCanvs);
        }
    }

    private void drawIcon(float tmpAngle,Bitmap image) {
        float imageWidth=mRadius/8;
        float angle= (float) ((tmpAngle+360/mItemCount/2)*Math.PI/180);
        int x= (int) (mCenter+mRadius/4*Math.cos(angle));
        int y= (int) (mCenter+mRadius/4*Math.sin(angle));
        RectF rectF=new RectF(x-imageWidth/2,y-imageWidth/2,x+imageWidth/2,y+imageWidth/2);
        mCanvs.drawBitmap(image, null, rectF, null);
    }

    private void drawText(float tmpAngle, float mSweepAngle, String mStr) {
        Path path=new Path();
        path.addArc(mRectf, tmpAngle, mSweepAngle);
        float textWidth=mTextPaint.measureText(mStr);
        float hOffset= (float) (mRadius*Math.PI/mItemCount/2-textWidth/2);
        float vOffset=mRadius/8;
        mCanvs.drawTextOnPath(mStr,path,hOffset,vOffset,mTextPaint);
    }


    private void drawBg() {
        mCanvs.drawColor(0Xffffffff);
        mCanvs.drawBitmap(BgBitmap, null, new RectF(mPadding/2, mPadding/2,
                getMeasuredWidth() - mPadding/2, getMeasuredHeight() - mPadding/2), null);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    public void luckystart(){
        mSpeed=50;
        isShouldEnd=false;
    }
    public void luckyend(){
        isShouldEnd=true;
    }
    public  boolean isStart(){
        return mSpeed!=0;
    }
    public boolean isShouldEnd(){
        return  isShouldEnd;
    }


}
