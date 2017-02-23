package com.example.android.toolutil.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.toolutil.R;

/**
 *   播放器参数设置
 * Created by ys on 2017/2/23 0023.
 */

public class VideoSettings {

    private Context mAppContext;
    private SharedPreferences mSharedPreferences;

    public VideoSettings(Context context){
        mAppContext = context.getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mAppContext);
    }

    /**
     *  能否后台播放
     * @return boolean
     */
    public boolean getEnableBackgroundPlay(){
        String key = mAppContext.getString(R.string.pref_key_enable_background_play);
        return mSharedPreferences.getBoolean(key,false);
    }

    /**
     *  播放器个数
     * @return int
     */
    public int getPlayer(){
        String key = mAppContext.getString(R.string.pref_key_player);
        String value = mSharedPreferences.getString(key,"");
        try {
            return Integer.parseInt(value);
        }catch (NumberFormatException e){
            //e
            return 0;
        }
    }

    /**
     *  视频编码方式
     * @return boolean
     */
    public boolean getUsingMediaCodec(){
        String key = mAppContext.getString(R.string.pref_key_using_media_codec);
        return mSharedPreferences.getBoolean(key,false);
    }

    /**
     *  视频编码自动旋转角度
     * @return boolean
     */
    public boolean getUsingMediaCodecAutoRotate(){
        String key = mAppContext.getString(R.string.pref_using_media_codec_auto_roatte);
        return mSharedPreferences.getBoolean(key,false);
    }

    /**
     *
     * @return
     */
    public boolean getMediaCodecHandleResolutionChange(){
        String key = mAppContext.getString(R.string.pref_media_codec_handle_resolution_change);
        return mSharedPreferences.getBoolean(key,false);
    }

    /**
     *  是否使用 opensl_es
     * @return boolean
     */
    public boolean getUsingOpenSLES(){
        String key = mAppContext.getString(R.string.pref_using_opensl_es);
        return mSharedPreferences.getBoolean(key,false);
    }

    /**
     *  像素规格
     * @return
     */
    public String getPixelFormat(){
        String key = mAppContext.getString(R.string.pref_pixel_format);
        return mSharedPreferences.getString(key,"");
    }

    
}
