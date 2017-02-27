package com.example.android.toolutil.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 *  视频播放服务
 * Created by ys on 2017/2/25.
 */

public class MediaPlayerService extends Service {

    //视频播放监听回调
    private static IMediaPlayer sMediaPlayer;

    /**
     *  启动服务的意图
     * @param context 上下文
     * @return Intent 意图
     */
    public static Intent newIntent(Context context){
        return new Intent(context,MediaPlayerService.class);
    }

    public static void intentToStart(Context context){
        context.startService(newIntent(context));
    }

    public static void intentToStop(Context context){
        context.stopService(newIntent(context));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     *  设置视频回调监听
     * @param mp 回调监听
     */
    public static void setMediaPlayer(IMediaPlayer mp){
        if(sMediaPlayer != null && sMediaPlayer != mp){
            //重新赋值sMediaPlayer
            if(sMediaPlayer.isPlaying())
                sMediaPlayer.stop();
            sMediaPlayer.release();
            sMediaPlayer = null;
        }
        sMediaPlayer = mp;
    }

    public static IMediaPlayer getMediaPlayer(){
        return sMediaPlayer;
    }

}
