package com.example.android.toolutil.utils;

import android.content.Context;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 *  final class
 *    保证类不可变，不能被继承
 *  大小测量的辅助类
 * Created by ys on 2017/2/27 0027.
 */

public final class MeasureHelper {
    //弱引用
    private WeakReference<View> mWeakView;

    //video view的宽高
    private int mVideoWidth;
    private int mVideoHeight;
    //比率
    private int mVideoSarNum;
    private int mVideoSarDen;
    //方向角度
    private int mVideoRotationDegree;

    //当前的宽高比率
    private int mCurrentAspectRatio = IRenderView.AR_ASPECT_FIT_PARENT;

    //最终计算出来的宽高
    private int mMeasureWidth;
    private int mMeasureHeight;

    public MeasureHelper(View view){
        mWeakView = new WeakReference<View>(view);
    }

    /**
     *  返回被测量的View
     * @return view
     */
    public View getView(){
        if(mWeakView == null){
            return null;
        }
        return mWeakView.get();
    }

    /**
     *  设置Video 的宽高
     * @param videoWidth    宽
     * @param videoHeight   高
     */
    public void setVideoSize(int videoWidth,int videoHeight){
        mVideoWidth = videoWidth;
        mVideoHeight = videoHeight;
    }

    /**
     *  比率
     * @param videoSarNum
     * @param videoSarDen
     */
    public void setVideoSampleAspectRatio(int videoSarNum,int videoSarDen){
        mVideoSarDen = videoSarDen;
        mVideoSarNum = videoSarNum;
    }

    /**
     *  设置视屏方向角度
     * @param videoRotationDegree
     */
    public void setVideoRotation(int videoRotationDegree){
        mVideoRotationDegree = videoRotationDegree;
    }

    /**
     *  在 View.onMeasure(int,int) 中调用
     * @param widthMeasureSpec      宽
     * @param heightMeasureSpec     高
     */
    public void doMeasure(int widthMeasureSpec,int heightMeasureSpec){
        //通过方向角度确定 横屏/竖屏
        if(mVideoRotationDegree == 90 || mVideoRotationDegree == 270){
            int tempSpec = widthMeasureSpec;
            widthMeasureSpec = heightMeasureSpec;
            heightMeasureSpec = tempSpec;
        }
        //宽度
        int width = View.getDefaultSize(mVideoWidth,widthMeasureSpec);
        int height = View.getDefaultSize(mVideoHeight,heightMeasureSpec);
        //宽高计算
        if(mCurrentAspectRatio == IRenderView.AR_MATCH_PARENT){
            //MatchParent
            width = widthMeasureSpec;
            height = heightMeasureSpec;
        }else if(mVideoWidth > 0 && mVideoHeight > 0){
            int widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);

            if(widthSpecMode == View.MeasureSpec.AT_MOST && heightMeasureSpec == View.MeasureSpec.AT_MOST){
                //如果测量模式是 AT_MOST ，即 大小没限制
                //计算宽高比
                float specAspectRatio = (float)widthSpecSize / (float) heightSpecSize;
                float displayAspectRatio;
                //当前设置的宽高比模式
                switch (mCurrentAspectRatio){
                    case IRenderView.AR_16_9_FIT_PARENT:
                        //横屏才是 16:9
                        displayAspectRatio = 16.0f/9.0f;
                        if(mVideoRotationDegree== 90 || mVideoRotationDegree == 270){
                            //判断为竖屏，要转换过来 9:16
                            displayAspectRatio = 1.0f/displayAspectRatio;
                        }
                        break;
                    case IRenderView.AR_4_3_FIT_PARENT:
                        //横屏才是 4:3
                        displayAspectRatio = 4.0f/3.0f;
                        if(mVideoRotationDegree== 90 || mVideoRotationDegree == 270){
                            //判断为竖屏，要转换过来 3:4
                            displayAspectRatio = 1.0f/displayAspectRatio;
                        }
                        break;
                    case IRenderView.AR_ASPECT_FILL_PARENT:
                    case IRenderView.AR_ASPECT_FIT_PARENT:
                    case IRenderView.AR_ASPECT_WRAP_CONTENT:
                    default:
                        //Video实际宽高比率
                        displayAspectRatio = (float) mVideoWidth / (float)mVideoHeight;
                        if(mVideoSarNum > 0 && mVideoSarDen >0)
                            displayAspectRatio = displayAspectRatio * mVideoSarNum / mVideoSarDen;
                        break;
                }
                //实际显示的宽高比 大于 测量的宽高比，则适配到实际比率
                boolean shouldBeWider = displayAspectRatio > specAspectRatio;
                switch (mCurrentAspectRatio){
                    case IRenderView.AR_ASPECT_FIT_PARENT:
                    case IRenderView.AR_16_9_FIT_PARENT:
                    case IRenderView.AR_4_3_FIT_PARENT:
                        if(shouldBeWider){  //
                            //too wide,fix width
                            width = widthSpecSize;
                            height = (int) (width/displayAspectRatio);
                        }else{
                            //too high,fix height
                            height = heightSpecSize;
                            width = (int) (height * displayAspectRatio);
                        }
                        break;
                    case IRenderView.AR_ASPECT_FILL_PARENT:
                        if(shouldBeWider){
                            //not high enough,fix height
                            height = heightSpecSize;
                            width = (int) (height * displayAspectRatio);
                        }else{
                            //not wide enough, fix width
                            width = widthSpecSize;
                            height = (int) (width/displayAspectRatio);
                        }
                        break;
                    case IRenderView.AR_ASPECT_WRAP_CONTENT:
                    default:
                        if(shouldBeWider){
                            //too wide, fix width
                            width = Math.min(mVideoWidth,widthSpecSize);
                            height = (int) (width / displayAspectRatio);
                        }else{
                            //too high, fix height
                            height = Math.min(mVideoHeight,heightSpecSize);
                            width = (int) (height * displayAspectRatio);
                        }
                        break;
                }
            }else if(widthSpecMode == View.MeasureSpec.EXACTLY && heightSpecMode == View.MeasureSpec.EXACTLY){
                //如果宽高测量 是确定值
                //the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;

                //根据实际比率调整宽高
                //for compatibility, we adjust size based on aspect ratio
                if(mVideoWidth * height < width * mVideoHeight){
                    width = height * mVideoWidth / mVideoHeight;
                }else if(mVideoWidth * height > width * mVideoHeight){
                    height = width * mVideoHeight / mVideoWidth;
                }
            }else if(widthSpecMode == View.MeasureSpec.EXACTLY){
                //宽的测量是确定值，则计算height
                width = widthSpecSize;
                height = width * mVideoHeight / mVideoWidth;
                if(heightSpecMode == View.MeasureSpec.AT_MOST && height > heightSpecSize){
                    height = heightSpecSize;
                }
            }else if(heightSpecMode == View.MeasureSpec.EXACTLY){
                //only the height is fixed, adjust the width to match aspect ration if possible
                height = heightSpecSize;
                width = height * mVideoWidth / mVideoHeight;
                if(widthSpecMode == View.MeasureSpec.AT_MOST && width > widthSpecSize){
                    //宽的测量模式是没限制的话，则宽的大小不能超过测量的大小
                    width = widthSpecSize;
                }
            }else{
                //宽高都没有给定，则计算video size
                width = mVideoWidth;
                height = mVideoHeight;
                if(heightSpecMode == View.MeasureSpec.AT_MOST && height > heightSpecSize){
                    //太高，减小 宽 和高
                    height = heightSpecSize;
                    width = height * mVideoWidth / mVideoHeight;
                }
                if(widthSpecMode == View.MeasureSpec.AT_MOST && width > widthSpecSize){
                    //太宽，减小 宽和高
                    width = widthSpecSize;
                    height = width * mVideoHeight / mVideoWidth;
                }
            }
        }else{
            //现在没有宽高，则采用指定的宽高大小
        }
        //最终指定的宽高
        mMeasureWidth = width;
        mMeasureHeight = height;
    }

    public int getMeasureWidth(){
        return mMeasureWidth;
    }

    public int getMeasureHeight(){
        return mMeasureHeight;
    }

    //设置宽高比率
    public void setAspectRatio(int aspectRatio){
        mCurrentAspectRatio = aspectRatio;
    }

    /**
     *  文字描述
     * @param context
     * @param aspectRatio   比率模式
     * @return
     */
    public static String getAspectRatioText(Context context, int aspectRatio){
        String text;
        switch (aspectRatio){
            case IRenderView.AR_ASPECT_FIT_PARENT:
                text = "Aspect / Fit Parent";
                break;
            case IRenderView.AR_ASPECT_FILL_PARENT:
                text = "Aspect / Fill Parent";
                break;
            case IRenderView.AR_ASPECT_WRAP_CONTENT:
                text = "Aspect / Wrap Parent";
                break;
            case IRenderView.AR_MATCH_PARENT:
                text = "Free / Fill parent";
                break;
            case IRenderView.AR_4_3_FIT_PARENT:
                text = "16:9 / Fit parent";
                break;
            case IRenderView.AR_16_9_FIT_PARENT:
                text = "4:3 / Fit parent";
                break;
            default:
                text = "N/A";
                break;
        }
        return text;
    }
}
