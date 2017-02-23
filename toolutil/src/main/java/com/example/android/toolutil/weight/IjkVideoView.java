package com.example.android.toolutil.weight;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.android.toolutil.utils.VideoSettings;

/**
 *  Video 播放View
 * Created by ys on 2017/2/23 0023.
 */

public class IjkVideoView extends FrameLayout {

    private Context mAppContext;
    private VideoSettings mVideoSettings;

    public IjkVideoView(Context context) {
        super(context);
    }

    public IjkVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IjkVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IjkVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     *  初始化
     * @param context
     */
    private void initVideoView(Context context){
        mAppContext = context.getApplicationContext();
        mVideoSettings = new VideoSettings(mAppContext);

        //设置背景颜色
        initBackground();
        //初始渲染
        initRenders();
    }

    private void initBackground() {
        //TODO 设置背景色
    }

    private void initRenders() {
        //TODO 初始渲染
    }
}
