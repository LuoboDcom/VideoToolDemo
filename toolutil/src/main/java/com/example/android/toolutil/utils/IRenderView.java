package com.example.android.toolutil.utils;

import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 *  渲染方法接口
 * Created by ys on 2017/2/27 0027.
 */

public interface IRenderView {

    /**
     *  Video View 的大小设置方式
     *    全屏，4:3，16:9 等等
     */
    int AR_ASPECT_FIT_PARENT = 0;  //without clip  不会被裁剪
    int AR_ASPECT_FILL_PARENT = 1; //may clip      可能会被裁剪
    int AR_ASPECT_WRAP_CONTENT = 2; //包裹
    int AR_MATCH_PARENT = 3;
    int AR_16_9_FIT_PARENT = 4; //16:9
    int AR_4_3_FIT_PARENT = 5;  //4:3

    /**
     *  返回当前View
     * @return
     */
    View getView();

    /**
     *  是否等待重新计算大小
     * @return
     */
    boolean shouldWaitForResize();

    /**
     *  设置视频宽高
     * @param videoWidth    宽
     * @param videoHeight   高
     */
    void setVideoSize(int videoWidth,int videoHeight);

    /**
     *  设置视频宽高比率
     * @param videoSarNum
     * @param videoSarDen
     */
    void setVideoSampleAspectRatio(int videoSarNum,int videoSarDen);

    /**
     *  设置视频方向
     * @param degree 角度
     */
    void setVideoRotation(int degree);

    /**
     *  设置视频尺寸显示模式
     * @param aspectRatio
     */
    void setAspectRatio(int aspectRatio);

    /**
     *  添加渲染回调
     * @param callback
     */
    void addRenderCallback(@NonNull IRenderCallback callback);

    /**
     * 移除渲染回调
     * @param callback
     */
    void removeRenderCallback(@NonNull IRenderCallback callback);

    /**
     *  Surface view 句柄
     */
    interface ISurfaceHolder{

        void bindToMediaPlayer(IMediaPlayer mp);

        @NonNull
        IRenderView getRenderView();

        @Nullable
        SurfaceHolder getSurfaceHolder();

        @Nullable
        Surface openSurface();

        @Nullable
        SurfaceTexture getSurfaceTexture();
    }


    /**
     *  渲染回调
     */
    interface IRenderCallback{

        /**
         *  Surface窗口创建
         * @param holder
         * @param width     可以为0
         * @param height    可以为0
         */
        void onSurfaceCreated(@NonNull ISurfaceHolder holder,int width,int height);

        /**
         *  Surface 窗口创建
         * @param holder
         * @param format    可以为0
         * @param width
         * @param height
         */
        void onSurfaceChanged(@NonNull ISurfaceHolder holder,int format,int width,int height);

        void onSurfaceDestroyed(@NonNull ISurfaceHolder holder);
    }
}
