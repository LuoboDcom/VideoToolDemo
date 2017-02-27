package com.example.android.toolutil.weight;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.android.toolutil.services.MediaPlayerService;
import com.example.android.toolutil.utils.VideoSettings;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 *  Video 播放View
 * Created by ys on 2017/2/23 0023.
 */

public class IjkVideoView extends FrameLayout {

    private Context mAppContext;
    private VideoSettings mVideoSettings;

    /**
     *  所有可能的内部状态 VideoView状态
     */
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    /**
     *  宽高
     */
    private int mVideoWidth;
    private int mVideoHeight;

    /**
     *   mCurrentState ：video view 当前状态
     *   mTargetState : video video 被操作后想要达到的状态
     */
    private int mCurrentState = STATE_IDLE;
    private int mTargetState = STATE_IDLE;

    /**
     *  视频播放回调监听器
     */
    private IMediaPlayer mMediaPlayer = null;
    /**
     *  视频数据渲染控制器
     */
    private InfoHudViewHolder mHudViewHolder;

    /**
     *  视频标题UI
     */
    private TextView mSubtitleDisplay;


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

        //设置Video 后台是否可播放
        initBackground();
        //初始渲染
        initRenders();

        //初始化宽高
        mVideoWidth = 0;
        mVideoHeight = 0;
        //设置焦点
        setFocusable(true);
        //touch 获得焦点
        setFocusableInTouchMode(true);
        //获得焦点
        requestFocus();
        //设置 VideoView初始状态
        mCurrentState = STATE_IDLE;
        mTargetState = STATE_IDLE;
        //定义视频标题
        mSubtitleDisplay = new TextView(context);
        mSubtitleDisplay.setTextSize(24);
        mSubtitleDisplay.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
        );
        //添加到视图中
        addView(mSubtitleDisplay,lp);
    }

    private boolean mEnableBackgroundPlay = false;

    /**
     *  设置后台是否可播放
     */
    private void initBackground() {
        //获取xml设置
        mEnableBackgroundPlay = mVideoSettings.getEnableBackgroundPlay();
        if(mEnableBackgroundPlay){
            //TODO 设置后台播放服务
            MediaPlayerService.intentToStart(getContext());
            mMediaPlayer = MediaPlayerService.getMediaPlayer();
            //绑定渲染控制器
            if(mHudViewHolder != null){
                mHudViewHolder.setMediaPlayer(mMediaPlayer);
            }

        }
    }

    private void initRenders() {
        //TODO 初始渲染
    }
}
