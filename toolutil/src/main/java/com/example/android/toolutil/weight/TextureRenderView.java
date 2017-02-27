package com.example.android.toolutil.weight;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.android.toolutil.utils.IRenderView;
import com.example.android.toolutil.utils.MeasureHelper;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.ISurfaceTextureHolder;
import tv.danmaku.ijk.media.player.ISurfaceTextureHost;

/**
 *  TextureView 方式渲染Video
 * Created by ys on 2017/2/27 0027.
 */

public class TextureRenderView extends TextureView implements IRenderView{

    private static final String TAG = "TextureRenderView";

    private MeasureHelper mMeasureHelper;
    private SurfaceCallback mSurfaceCallback;

    public TextureRenderView(Context context) {
        super(context);
        initView(context);
    }

    public TextureRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TextureRenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextureRenderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    /**
     *  初始化
     * @param context
     */
    private void initView(Context context){
        mMeasureHelper = new MeasureHelper(this);
        mSurfaceCallback = new SurfaceCallback(this);
        //绑定
        setSurfaceTextureListener(mSurfaceCallback);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public boolean shouldWaitForResize() {
        return false;
    }

    @Override
    public void setVideoSize(int videoWidth, int videoHeight) {
        if(videoWidth > 0 && videoHeight > 0){
            mMeasureHelper.setVideoSize(videoWidth,videoHeight);
            requestLayout();
        }
    }

    @Override
    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        if(videoSarNum > 0 && videoSarDen > 0){
            mMeasureHelper.setVideoSampleAspectRatio(videoSarNum,videoSarDen);
            requestLayout();
        }
    }

    @Override
    public void setVideoRotation(int degree) {
        mMeasureHelper.setVideoRotation(degree);
        //旋转View
        setRotation(degree);
    }

    @Override
    public void setAspectRatio(int aspectRatio) {
        mMeasureHelper.setAspectRatio(aspectRatio);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //计算大小尺寸
        mMeasureHelper.doMeasure(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(mMeasureHelper.getMeasureWidth(),mMeasureHelper.getMeasureHeight());
    }

    @Override
    public void addRenderCallback(@NonNull IRenderCallback callback) {
        mSurfaceCallback.addRenderCallback(callback);
    }

    @Override
    public void removeRenderCallback(@NonNull IRenderCallback callback) {
        mSurfaceCallback.removeRenderCallback(callback);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(TextureRenderView.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(TextureRenderView.class.getName());
    }

    /**
     *   获取当前的View 的控制器
     * @return
     */
    public IRenderView.ISurfaceHolder getSurfaceHolder(){
        return new InternalSurfaceHolder(this,mSurfaceCallback.mSurfaceTexture,mSurfaceCallback);
    }


    private static final class InternalSurfaceHolder implements IRenderView.ISurfaceHolder{

        private TextureRenderView mTextureView;
        private SurfaceTexture mSurfaceTexture;
        private ISurfaceTextureHost mSurfaceTextureHost;

        public InternalSurfaceHolder(@NonNull TextureRenderView textureView,
                                     @NonNull SurfaceTexture surfaceTexture,
                                     @NonNull ISurfaceTextureHost surfaceTextureHost){
            mTextureView = textureView;
            mSurfaceTexture = surfaceTexture;
            mSurfaceTextureHost = surfaceTextureHost;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void bindToMediaPlayer(IMediaPlayer mp) {
            if(mp == null)
                return;
            if( (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) &&
                    (mp instanceof ISurfaceTextureHolder) ){
                ISurfaceTextureHolder textureHolder = (ISurfaceTextureHolder) mp;
                mTextureView.mSurfaceCallback.setOwnSurfaceTexture(false);

                SurfaceTexture surfaceTexture = textureHolder.getSurfaceTexture();
                if(surfaceTexture != null){
                    mTextureView.setSurfaceTexture(surfaceTexture);
                }else{
                    textureHolder.setSurfaceTexture(mSurfaceTexture);
                    textureHolder.setSurfaceTextureHost(mTextureView.mSurfaceCallback);
                }
            }else{
                mp.setSurface(openSurface());
            }
        }

        @NonNull
        @Override
        public IRenderView getRenderView() {
            return mTextureView;
        }

        @Nullable
        @Override
        public SurfaceHolder getSurfaceHolder() {
            return null;
        }

        @Nullable
        @Override
        public Surface openSurface() {
            if(mSurfaceTexture == null)
                return null;
            return new Surface(mSurfaceTexture);
        }

        @Nullable
        @Override
        public SurfaceTexture getSurfaceTexture() {
            return mSurfaceTexture;
        }
    }

    private static final class SurfaceCallback implements TextureView.SurfaceTextureListener,ISurfaceTextureHost{
        private SurfaceTexture mSurfaceTexture;
        private boolean mIsFormatChanged;
        private int mWidth;
        private int mHeight;

        private boolean mOwnSurfaceTexture = true;
        private boolean mWillDetachFromWindow = false;
        private boolean mDidDetachFromWindow = false;

        private WeakReference<TextureRenderView> mWeakRenderView;
        //线程安全的Map
        private Map<IRenderCallback,Object> mRenderCallbackMap = new ConcurrentHashMap<>();

        public SurfaceCallback(@NonNull TextureRenderView renderView){
            mWeakRenderView = new WeakReference<TextureRenderView>(renderView);
        }

        /**
         *  是不是自己的SurfaceTexture
         * @param ownSurfaceTexture
         */
        public void setOwnSurfaceTexture(boolean ownSurfaceTexture){
            mOwnSurfaceTexture = ownSurfaceTexture;
        }

        /**
         *  将要解绑
         */
        public void willDetachFromWindow(){
            mWillDetachFromWindow = true;
        }

        /**
         *   已解绑
         */
        public void didDetachFromWindow(){
            mDidDetachFromWindow = true;
        }

        /**
         *  绑定 TextureView 的渲染回调 与 MediaPlayer 回调
         * @param callback
         */
        public void addRenderCallback(@NonNull IRenderCallback callback){
            //
            mRenderCallbackMap.put(callback,callback);

            ISurfaceHolder surfaceHolder = null;
            if(mSurfaceTexture != null){
                if(surfaceHolder == null){
                    surfaceHolder = new InternalSurfaceHolder(mWeakRenderView.get(),mSurfaceTexture,this);
                }
                callback.onSurfaceCreated(surfaceHolder,mWidth,mHeight);
            }

            if(mIsFormatChanged){
                if(surfaceHolder == null)
                    surfaceHolder = new InternalSurfaceHolder(mWeakRenderView.get(),mSurfaceTexture,this);
                callback.onSurfaceChanged(surfaceHolder,0,mWidth,mHeight);
            }
        }

        /**
         *  移除渲染回调
         * @param callback
         */
        public void removeRenderCallback(@NonNull IRenderCallback callback){
            mRenderCallbackMap.remove(callback);
        }

        /**
         *  Surface Texture is ready
         * @param surfaceTexture
         * @param width
         * @param height
         */
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            mSurfaceTexture = surfaceTexture;
            mIsFormatChanged = false;
            mWidth = 0;
            mHeight = 0;

            ISurfaceHolder surfaceHolder = new InternalSurfaceHolder(mWeakRenderView.get(),surfaceTexture,this);
            for(IRenderCallback renderCallback : mRenderCallbackMap.keySet()){
                //回调所有的渲染监听
                renderCallback.onSurfaceCreated(surfaceHolder,0,0);
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
            mSurfaceTexture = surfaceTexture;
            mIsFormatChanged = true;
            mWidth = width;
            mHeight = height;

            ISurfaceHolder surfaceHolder = new InternalSurfaceHolder(mWeakRenderView.get(),surfaceTexture,this);
            for (IRenderCallback renderCallback : mRenderCallbackMap.keySet()){
                renderCallback.onSurfaceChanged(surfaceHolder,0,width,height);
            }
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            mSurfaceTexture = surfaceTexture;
            mIsFormatChanged = false;
            mWidth = 0;
            mHeight = 0;

            ISurfaceHolder surfaceHolder = new InternalSurfaceHolder(mWeakRenderView.get(),surfaceTexture,this);
            for (IRenderCallback renderCallback : mRenderCallbackMap.keySet()){
                renderCallback.onSurfaceDestroyed(surfaceHolder);
            }
            return mOwnSurfaceTexture;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }

        /**
         *
         * @param surfaceTexture
         */
        @Override
        public void releaseSurfaceTexture(SurfaceTexture surfaceTexture) {
            if (surfaceTexture == null) {
                Log.d(TAG, "releaseSurfaceTexture: null");
            } else if (mDidDetachFromWindow) {
                if (surfaceTexture != mSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): release different SurfaceTexture");
                    surfaceTexture.release();
                } else if (!mOwnSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): release detached SurfaceTexture");
                    surfaceTexture.release();
                } else {
                    Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): already released by TextureView");
                }
            } else if (mWillDetachFromWindow) {
                if (surfaceTexture != mSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): release different SurfaceTexture");
                    surfaceTexture.release();
                } else if (!mOwnSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): re-attach SurfaceTexture to TextureView");
                    setOwnSurfaceTexture(true);
                } else {
                    Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): will released by TextureView");
                }
            } else {
                if (surfaceTexture != mSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: alive: release different SurfaceTexture");
                    surfaceTexture.release();
                } else if (!mOwnSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: alive: re-attach SurfaceTexture to TextureView");
                    setOwnSurfaceTexture(true);
                } else {
                    Log.d(TAG, "releaseSurfaceTexture: alive: will released by TextureView");
                }
            }
        }
    }
}
