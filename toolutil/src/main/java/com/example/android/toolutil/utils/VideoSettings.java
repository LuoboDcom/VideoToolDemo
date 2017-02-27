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
     *  编码
     * @return boolean
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

    /**
     *  可否没有视图
     * @return boolean
     */
    public boolean getEnableNoView(){
        String key = mAppContext.getString(R.string.pref_enable_no_view);
        return mSharedPreferences.getBoolean(key,false);
    }

    /**
     * 是否支持SurfaceView
     * @return boolean
     */
    public boolean getEnableSurfaceView(){
        String key = mAppContext.getString(R.string.pref_enable_surface_view);
        return mSharedPreferences.getBoolean(key,false);
    }

    /**
     * 是否支持 TextureView
     * @return boolean
     */
    public boolean getEnableTextureView(){
        String key = mAppContext.getString(R.string.pref_enable_texture_view);
        return mSharedPreferences.getBoolean(key,false);
    }

    /**
     * 能否解绑Surface Texture View
     * @return boolean
     */
    public boolean getEnableDetachedSurfaceTextureView(){
        String key = mAppContext.getString(R.string.pref_enable_detached_surface_texture);
        return mSharedPreferences.getBoolean(key,false);
    }

    /**
     * 是否正在使用视频数据源
     * @return boolean
     */
    public boolean getUsingMediaDataSource(){
        String key = mAppContext.getString(R.string.pref_using_mediadatasource);
        return mSharedPreferences.getBoolean(key,false);
    }

    /**
     *  获取最近的文件夹路径
     * @return String
     */
    public String getLastDirectory(){
        String key = mAppContext.getString(R.string.pref_key_last_directory);
        return mSharedPreferences.getString(key,"/");
    }

    /**
     * 设置最近的文件夹路径
     * @param path 路径
     */
    public void setLastDirectory(String path){
        String key =  mAppContext.getString(R.string.pref_key_last_directory);
        mSharedPreferences.edit().putString(key,path).apply();
    }
}
