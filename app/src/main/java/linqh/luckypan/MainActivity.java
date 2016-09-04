package linqh.luckypan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView iv_bt;
    private LuckPan luckPan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_bt= (ImageView) findViewById(R.id.iv_bt);
        luckPan= (LuckPan) findViewById(R.id.luckypan);
//        DisplayMetrics displayMetrics=getResources().getDisplayMetrics();
//        int width=displayMetrics.widthPixels;

        iv_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!luckPan.isStart()){
                    luckPan.luckystart();
                    iv_bt.setImageResource(R.drawable.stop);
                }else {
                    if(!luckPan.isShouldEnd()){
                        luckPan.luckyend();
                        iv_bt.setImageResource(R.drawable.start);
                    }
                }
            }
        });
    }
}
