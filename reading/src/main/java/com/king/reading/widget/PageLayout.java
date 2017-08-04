package com.king.reading.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.king.reading.C;
import com.king.reading.SysApplication;
import com.king.reading.common.utils.BitmapUtils;
import com.king.reading.ddb.Line;
import com.king.reading.ddb.Page;
import com.king.reading.ddb.Rect;
import com.king.reading.module.read.ReadDetailActivity;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.SoftReference;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayerUtil;

import static com.king.reading.common.utils.BitmapUtils.decodeUriAsBitmap;


/**
 * 创建者     王开冰
 * 创建时间   2017/7/6 10:55
 * 描述	      ${TODO}点读页面的点读放大按钮设置
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class PageLayout extends ViewGroup {
    private final String TAG = "PageLayout";
    private int mLayoutWidth;
    private int mLayoutHeight;
    private Button[] mButtons;
    private Page mPage;
    private int mIndex;
    private int mLastIndex;
    private float mFrameLeft;
    private float mFrameTop;
    private float mFrameRight;
    private float mFrameBottom;
    private Context mContext;
    public Bitmap mReadingBgBitmap;
    private SoftReference<Bitmap> mReadingBgSoftReference;
    private Uri mUri;
    private Bitmap mBtBitmap;
    private float mSampleSize;
    private SoftReference<Bitmap> mBottonSoftReference;
    private BitmapDrawable mBitmapDrawable;
    private Button mButton;
    private ScaleAnimation mScaleAnimation;
    private Handler mHandler;
    public static boolean mIsReadingOnclickFinished;
    private static int mEndWordIndex;
    private  static int mStartWordIndex =-1;
    private int mCurrentPosition;

    public PageLayout(Context context) {
        super(context);
        setWillNotDraw(false);//加上这个标记，执行完onMeasure/onLayout,就会去执行onDraw方法
        setOnClickListener(null);
    }

    public PageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setWillNotDraw(false);//加上这个标记，执行完onMeasure/onLayout,就会去执行onDraw方法
        setOnClickListener(null);
    }

    /**
     * 设置点读按钮
     *
     * @param page
     * @param imageUri
     * @param handler
     * @param position
     */
    public void setConfigs(Page page, Uri imageUri, Handler handler, int position) {
        mUri = imageUri;
        mPage = page;
        mHandler = handler;
        mCurrentPosition = position;
        mButtons = new Button[page.lines.size()];
        //得到图片的背景
        mReadingBgBitmap = decodeUriAsBitmap(imageUri, mContext);
        //弱引用保存bitmap
        mReadingBgSoftReference = new SoftReference<Bitmap>(mReadingBgBitmap);//点击的时候再产生bitmap
        initialize();
        Log.d(TAG, "setConfigs");
        requestLayout();
    }

    /**
     * 初始化按钮
     */
    private void initialize() {
        removeAllViews();

        for (int i = 0; i < mButtons.length; i++) {
            mButtons[i] = new Button(SysApplication.getApplication());
            final int finalI = i;
            mButtons[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    mIsReadingOnclickFinished = false;
                    if (ReadDetailActivity.mIsRepeateImageButtonchecked) {
                        if (!ReadDetailActivity.mIsStartWordSelected) {//选择开始句
                            ReadDetailActivity.mIsStartWordSelected = true;
                            mIndex = finalI;
                            mStartWordIndex = finalI;
                            mLastIndex = finalI;
                            ReadDetailActivity.mStartPageIndex = mCurrentPosition;
                            Toast.makeText(SysApplication.getApplication(), "选择开始句了", Toast.LENGTH_SHORT).show();
                            mHandler.sendEmptyMessage(C.HANDLE_SELECTED_START_WORD_REPEAT_MODEL);
//                            invalidate();
                        } else {//选择结束句
                            ReadDetailActivity.mEndPageIndex = mCurrentPosition;
                            if (ReadDetailActivity.mStartPageIndex > mCurrentPosition) {
                                Toast.makeText(SysApplication.getApplication(), "结束句不能选择在起始句的前面", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (ReadDetailActivity.mStartPageIndex == mCurrentPosition) {
                                if (mStartWordIndex > finalI) {
                                    Toast.makeText(SysApplication.getApplication(), "结束句不能选择在起始句的前面", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }


                            ReadDetailActivity.mIsEndWordsSelected = true;
                            mEndWordIndex = finalI;
//                            invalidate();
                            Toast.makeText(SysApplication.getApplication(), "选择结束句了", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        readingOnclick(finalI);

                    }
                }

            });
            mButtons[i].setBackgroundColor(Color.TRANSPARENT);
            addView(mButtons[finalI]);
        }
    }

    /**
     * 点击某个句子的点读
     *
     * @param finalI
     */
    private void readingOnclick(int finalI) {
        MediaPlayerUtil.stop();
        removeFrame();
        mHandler.sendEmptyMessage(C.HANDLE_INTERRUPT_READING_ALL_PAGE);
        mIndex = finalI;
        mLastIndex = finalI;
        //                    setBorderLine(finalI);
        getButtonBitmap(mIndex);
        buttonAnimatino(mIndex);//执行按钮的放大
        Toast.makeText(SysApplication.getApplication(), "点击了按钮" + finalI, Toast.LENGTH_SHORT).show();
        //                    String sdUrl = "file:///storage/emulated/0/SHReading/Res/SJ_SHBD_1B/page002/sound/p002002.mp3";
        String sdUrl = "http://192.168.3.197:8080/P031006.mp3";
        EventBus.getDefault().post(mPage.lines.get(finalI).translation);


        //        MediaPlayerUtil.playFromIntenet(SysApplication.getApplication(), mPage.lines.get(finalI).encryptSoundURL, 1.0f, null);
        MediaPlayerUtil.playFromIntenet(SysApplication.getApplication(), sdUrl, 1.0f, null);

        MediaPlayerUtil.getInstance().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mIsReadingOnclickFinished = true;
                clearButtonAnimationAndRemoveFrame();
            }
        });
    }

    /**
     * 移除放大的动画和removeFrame
     */
    public void clearButtonAnimationAndRemoveFrame() {
        if (mScaleAnimation != null) {//这一句并没有效果，并不能让动画回到初始的位置
            mScaleAnimation.cancel();
        }
        // 不一定会及时回收
        if (mBottonSoftReference.get() != null && !mBottonSoftReference.get().isRecycled()) {
            mBottonSoftReference.get().recycle();
            mBtBitmap = null;
        }
        System.gc();
        mButton.setBackgroundColor(Color.TRANSPARENT);
        removeFrame();
    }

    /**
     * 全文自动播放
     *
     * @param current
     * @param total
     */
    public void autoReading(int current, int total) {
        Toast.makeText(mContext, "全文自动播放" + current + "----" + total, Toast.LENGTH_SHORT).show();
        if (mPage.lines.size() == 0) {
            // 此页没有可跟读配置，自动翻至下一页
            mHandler.sendEmptyMessage(C.HANDLE_READING_TURN_PAGE);
            return;
        }

        if (mIndex <= 0) {
            // 从上次停止的那一句开始
            mIndex = mLastIndex;
        }

        if (ReadDetailActivity.mIsRepeateImageButtonchecked) {
            if (mIndex <= 0&&current==ReadDetailActivity.mStartPageIndex) {
                mIndex = mStartWordIndex;
                mLastIndex = mStartWordIndex;
            } else {
                mIndex = mLastIndex;
            }
        }


        getButtonBitmap(mIndex);
        buttonAnimatino(mIndex);

        EventBus.getDefault().post(mPage.lines.get(mIndex).translation);

        String sdUrl = "http://192.168.3.197:8080/P031006.mp3";
//        MediaPlayerUtil.playFromIntenet(SysApplication.getApplication(), mPage.lines.get(mIndex).encryptSoundURL, 1.0f, null);
        MediaPlayerUtil.playFromIntenet(SysApplication.getApplication(), sdUrl, 1.0f, null);

        MediaPlayerUtil.getInstance().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mScaleAnimation != null) {//这一句并没有效果，并不能让动画回到初始的位置
                    mScaleAnimation.cancel();
                }
                // 对会在按钮上的bitmap进行回收

                if (mBottonSoftReference.get() != null && !mBottonSoftReference.get().isRecycled()) {
                    mBottonSoftReference.get().recycle();
                    mBtBitmap = null;
                }
                System.gc();

                mButton.setBackgroundColor(Color.TRANSPARENT);

                if (ReadDetailActivity.mIsRepeateImageButtonchecked) {//复读播放模式
                    if (ReadDetailActivity.mIsEndWordsSelected) {//选择了结束句
                        //循环播放这一段
                        Toast.makeText(SysApplication.getApplication(), "循环播放这一段", Toast.LENGTH_SHORT).show();
                        repeateReadingOnCompleteSetting();
                    } else if (ReadDetailActivity.mIsStartWordSelected) {//只选择了开始句
                        //重复播放这一句
                        Toast.makeText(SysApplication.getApplication(), "循环播放这一段", Toast.LENGTH_SHORT).show();
                        repeateReadingOnCompleteSetting();
                    }
                } else {//全文播放

                    autoreadingOncompleteSetting();
                }

            }
        });
    }

    /**
     * 复读播放，音频读完的监听设置
     */
    private void repeateReadingOnCompleteSetting() {
        if (ReadDetailActivity.mStartPageIndex == ReadDetailActivity.mEndPageIndex||!ReadDetailActivity.mIsEndWordsSelected) {//当前页的复读

            if (mIndex < mEndWordIndex) {
                mLastIndex++;
                mIndex = mLastIndex;
                mHandler.sendEmptyMessageDelayed(C.HANDLE_AUTO_READING_ALL_PAGE, 1000);
            } else {//复读播放
                mIndex = mStartWordIndex;
                mLastIndex = mStartWordIndex;
                autoReading(0, 5);
            }

        } else {//跨页复读
            if (mCurrentPosition < ReadDetailActivity.mEndPageIndex) {//全文播放

                if (mIndex < mPage.lines.size() - 1) {
                    mLastIndex++;
                    mIndex = mLastIndex;
                    mHandler.sendEmptyMessageDelayed(C.HANDLE_AUTO_READING_ALL_PAGE, 1000);
                } else {
                    mLastIndex = 0;// 重置
                    removeFrame();
                    mHandler.sendEmptyMessage(C.HANDLE_READING_TURN_PAGE);
                }

            } else {//读到当前页

                if (mIndex < mEndWordIndex) {
                    mLastIndex++;
                    mIndex = mLastIndex;
                    mHandler.sendEmptyMessageDelayed(C.HANDLE_AUTO_READING_ALL_PAGE, 1000);
                } else {//复读播放
                    mHandler.sendEmptyMessage(C.HANDLE_REPEATE_MODE_READING);
                }
            }
        }
    }

    /**
     * 全文播放，音频读完的监听设置
     */
    private void autoreadingOncompleteSetting() {
        // 全文播放
        if (mIndex < mPage.lines.size() - 1) {
            mLastIndex++;
            mIndex = mLastIndex;
            mHandler.sendEmptyMessageDelayed(C.HANDLE_AUTO_READING_ALL_PAGE, 1000);
        } else {
            mLastIndex = 0;// 重置
            removeFrame();
            mHandler.sendEmptyMessage(C.HANDLE_READING_TURN_PAGE);
        }
    }

    /**
     * 执行按钮的放大
     *
     * @param index
     */
    private void buttonAnimatino(int index) {
        mBitmapDrawable = new BitmapDrawable(mBottonSoftReference.get());
        mButton = mButtons[index];
        mButton.setBackgroundDrawable(mBitmapDrawable);

        mScaleAnimation = new ScaleAnimation(1.0f, mSampleSize, 1.0f, mSampleSize, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        mScaleAnimation.setDuration(200);
        mScaleAnimation.setFillAfter(true);
        mButton.setAnimation(mScaleAnimation);
        mScaleAnimation.startNow();
    }

    /**
     * 根据坐标得到bitmap
     *
     * @param index
     */
    private void getButtonBitmap(int index) {
        // 判断是否是由于翻页引起的页面bitmap销毁
        if (mReadingBgBitmap == null) {
            mReadingBgBitmap = decodeUriAsBitmap(mUri, mContext);
            mReadingBgSoftReference = new SoftReference<Bitmap>(mReadingBgBitmap);
        }
        // 从软引用中获取bitmap
        // 把按钮的强引用对象用软引用来存储
        mBtBitmap = ImageCrop(mReadingBgSoftReference.get(), mPage.lines.get(index));
        mBottonSoftReference = new SoftReference<Bitmap>(mBtBitmap);
    }

    /**
     * 得到放大按钮的背景
     *
     * @param bitmap
     * @param line
     * @return
     */
    private Bitmap ImageCrop(Bitmap bitmap, Line line) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int cropW = (int) (Float.parseFloat(line.frame.size.width) * w);
        int cropH = (int) (Float.parseFloat(line.frame.size.height) * h);

        int retX = (int) (Float.parseFloat(line.frame.origin.x) * w);// 截取区域的左上角坐标
        if (retX < 0) {
            retX = 0;
        }
        int retY = (int) (Float.parseFloat(line.frame.origin.y) * h);
        if (retY < 0) {
            retY = 0;
        }
        float scaleSizeX1 = (float) (retX + cropW * 1.25 > w ? (float) (2 * w - 2 * retX - cropW) / (float) (cropW) : 1.5);// 宽度不超出右边界
        float scaleSizeX2 = (float) (retX - cropW * 0.25 < 0 ? (float) (2 * retX + cropW) / (float) (cropW) : 1.5);// 宽度不超出左边界
        float scaleSizeY1 = (float) (retY + cropH * 1.25 > h ? (float) (2 * h - 2 * retY - cropH) / (float) (cropH) : 1.5);// 高度不超出边界
        float scaleSizeY2 = (float) (retY - cropH * 0.25 < 0 ? (float) (2 * retY + cropH) / (float) (cropH) : 1.5);// 高度不超出边界

        float mSampleSize1 = scaleSizeX1 > scaleSizeY1 ? scaleSizeY1 : scaleSizeX1;// 返回放大的比例
        float mSampleSize2 = scaleSizeX2 > scaleSizeY2 ? scaleSizeY2 : scaleSizeX2;
        mSampleSize = mSampleSize1 > mSampleSize2 ? mSampleSize2 : mSampleSize1;
        // 下面这句是关键
        return BitmapUtils.GetRoundedCornerBitmap(Bitmap.createBitmap(bitmap, retX, retY, cropW, cropH, null, false));
    }

    /**
     * 移除画面，重新构建
     */
    public void removeFrame() {
        mIndex = -1;
        initialize();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        Log.d(TAG, "onMeasure->childCount=" + childCount);
        // 获取该ViewGroup的实际长和宽 涉及到MeasureSpec类的使用
        mLayoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        mLayoutHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG, "**** specSize_Widht " + mLayoutWidth + " * specSize_Heigth   *****" + mLayoutHeight);
        // 设置本ViewGroup的宽高
        setMeasuredDimension((int) mLayoutWidth, (int) mLayoutHeight);
        int moduleWidth = 0, moduleHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);// 获得每个对象的引用
            moduleWidth = (int) (Float.parseFloat(mPage.lines.get(i).frame.size.width) * mLayoutWidth);
            moduleHeight = (int) (Float.parseFloat(mPage.lines.get(i).frame.size.height) * mLayoutHeight);
            LayoutParams layoutParams = new LayoutParams(moduleWidth, moduleHeight);
            child.setLayoutParams(layoutParams);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //不是复读的开始页和结束页，不进行绘制
        if (mStartWordIndex==-1||mCurrentPosition!=ReadDetailActivity.mStartPageIndex||mCurrentPosition!=ReadDetailActivity.mEndPageIndex||mPage==null)
            return;
        if (ReadDetailActivity.mIsRepeateImageButtonchecked) {
            int childCount = getChildCount();
            Log.d(TAG, "onDraw->childCount=" + childCount);
            Paint paint = new Paint();
            // 为边框设颜色
            paint.setColor(Color.RED);
            paint.setStrokeWidth(3);// 设置线条宽度
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeCap(Paint.Cap.SQUARE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setAntiAlias(true);
            paint.setDither(true);
            if (ReadDetailActivity.mIsStartWordSelected) {
                setBorderLine(mStartWordIndex);
                // 画矩形
                RectF r = new RectF(mFrameLeft, mFrameTop, mFrameRight, mFrameBottom);
                Log.d(TAG, "mFrameLeft=" + mFrameLeft + ",mFrameTop=" + mFrameTop + ",mFrameRight=" + mFrameRight + ",mFrameBottom=" + mFrameBottom);
                canvas.drawRoundRect(r, 15f, 15f, paint);
            }

            if (ReadDetailActivity.mIsEndWordsSelected) {
                setBorderLine(mEndWordIndex);
            // 画矩形
            RectF r = new RectF(mFrameLeft, mFrameTop, mFrameRight, mFrameBottom);
            Log.d(TAG, "mFrameLeft=" + mFrameLeft + ",mFrameTop=" + mFrameTop + ",mFrameRight=" + mFrameRight + ",mFrameBottom=" + mFrameBottom);
            canvas.drawRoundRect(r, 15f, 15f, paint);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        Log.d(TAG, "onLayout->childCount=" + childCount);
        int startLeft = 0;// 设置每个子View的起始横坐标
        int startTop = 0; // 每个子View距离父视图的位置
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            startLeft = (int) (Float.parseFloat(mPage.lines.get(i).frame.origin.x) * mLayoutWidth);
            startTop = (int) (Float.parseFloat(mPage.lines.get(i).frame.origin.y) * mLayoutHeight);
            Log.i(TAG, "**** MeasuredWidth ****" + child.getMeasuredWidth());
            Log.i(TAG, "**** MeasuredHeight ****" + child.getMeasuredHeight());
            child.layout(startLeft, startTop, startLeft + (child.getMeasuredWidth()), startTop + (child.getMeasuredHeight()));
        }
    }

    /**
     * 点读画红框
     *
     * @param index
     */
    private void setBorderLine(int index) {
        Rect frame = mPage.lines.get(index).frame;
        mFrameLeft = Float.parseFloat(frame.origin.x) * mLayoutWidth;
        mFrameTop = Float.parseFloat(frame.origin.y) * mLayoutHeight;
        mFrameRight = mFrameLeft + Float.parseFloat(frame.size.getWidth()) * mLayoutWidth;
        mFrameBottom = mFrameTop + Float.parseFloat(frame.size.getHeight()) * mLayoutHeight;
//        invalidate();
    }


    public void reset() {
        mIndex = -1;
        mLastIndex = 0;
        recycleBitmap();// 对点读放大的动画和产生的button上的view进行重置
    }

    /**
     * 回收页面背景和Button的bitmap
     */
    private void recycleBitmap() {
        if (mScaleAnimation != null) {
            mScaleAnimation.cancel();
        }
        if (mBottonSoftReference != null && mBottonSoftReference.get() != null && !mBottonSoftReference.get().isRecycled()) {
            mBottonSoftReference.get().recycle();
            mBtBitmap = null;
        }
        System.gc();

        if (mButton != null) {
            mButton.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
