package linqh.luckypan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by dz1 on 2016/1/5.
 */
public class SurfaceViewTemple extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    //绘制的画布
    private Canvas mCanvs;
    private SurfaceHolder mHolder;
    //用于绘制的线程和标志
    private Thread mThread;
    private boolean isRunning;

    //绘图用的图文
    private int[] mImages=new int[]{R.drawable.danfan,R.drawable.f040,R.drawable.iphone,R.drawable.f040,R.drawable.meizi,R.drawable.f040};
    private int[] mColors=new int[]{0xFF13581a,0xffeab483,0xFF13581a,0xffeab483,0xFF13581a,0xffeab483};
    private String[] mStrs=new String[]{"单反相机","恭喜发财","IPHONE","恭喜发财","一套衣服","恭喜发财"};
    private Bitmap[] mBitmaps;
    private int mItemCount=6;


    public SurfaceViewTemple(Context context) {
        super(context,null);
    }

    public SurfaceViewTemple(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder=getHolder();
        mHolder.addCallback(this);

        setFocusable(true);
        setFocusableInTouchMode(true);

        setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning=true;
        mCanvs=mHolder.lockCanvas();

        mThread=new Thread(this);
        mThread.start();
    }
    @Override
    public void run() {
        while (isRunning) {
          draw();
        }
    }

    private void draw() {
        try {
            if(mCanvs!=null){
                //绘制
                drawBg();
            }
        }catch (Exception e){

        }finally {
            if(mCanvs!=null){
                mHolder.unlockCanvasAndPost(mCanvs);
            }
        }
    }

    private void drawBg() {


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning=false;
    }


}
