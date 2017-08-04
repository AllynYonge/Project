package com.king.reading.module.user;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.SysApplication;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.common.utils.AppScreenMgr;
import com.king.reading.common.utils.BitmapUtils;
import com.king.reading.common.utils.DialogUtils;
import com.king.reading.widget.crop.CropImageView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by AllynYonge on 21/06/2017.
 */

@Route(path = C.ROUTER_UPLOADAVATAR)
public class UploadAvatarActivity extends BaseActivity {
    private static final int REQUEST_CODE_CHOOSE = 1;

    @BindView(R.id.CropImageView)
    CropImageView cropImageView;
    @BindView(R.id.frame_upload_crop)
    FrameLayout layout;
    @BindView(R.id.tv_upload_next)
    TextView next;
    @BindView(R.id.iv_photo)
    ImageView mIvPhoto;
    @BindView(R.id.tv_crop_tips)
    TextView mTvCropTips;

    @Override
    public void onInitData(Bundle savedInstanceState) {
    }

    @Override
    public void onInitView() {
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle("上传头像");
        setRightText("跳过");
        layout.getLayoutParams().height = AppScreenMgr.getScreenWidth(getApplicationContext());
        layout.setLayoutParams(layout.getLayoutParams());
        /*cropImageView.setImageResource(R.mipmap.temp_logo);
        cropImageView.setCropAreaWidth(AppScreenMgr.getScreenWidth(getApplicationContext()));
        cropImageView.setAspectRatio(1,1);
        cropImageView.setCropShape(CropImageView.CropShape.OVAL);
        cropImageView.setCropMode(CropImageView.CropMode.FIXCROPAREA);*/
        next.setEnabled(true);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_upload;
    }

    @Override
    public void onRightClick(View v) {
        ARouter.getInstance().build(C.ROUTER_SELECTVERSION).navigation();
    }


    @OnClick(R.id.tv_upload_next)
    public void next(View view) {
        ARouter.getInstance().build(C.ROUTER_SELECTVERSION).navigation();
        //把头像剪切后的图像进行上传并本地保存
        Bitmap croppedImage = cropImageView.getCropedImageFrom();
        BitmapUtils.saveImage(croppedImage, Environment.getExternalStorageDirectory().getAbsolutePath(),"headImage.png");
        EventBus.getDefault().post(C.EADIMAGE_IS_UPDATE);
    }

    @OnClick(R.id.iv_photo)
    public void photoAndCamera(View v) {
        DialogUtils.showListDialog(this, R.array.photo_camera, "上传头像", new DialogUtils.IListDialogItemCallback() {
            @Override
            public void onListItemSelected(CharSequence text, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(SysApplication.getApplication(), "拍照上传", Toast.LENGTH_SHORT).show();
                        selectPhoto();
                        break;
                    case 1:
                        Toast.makeText(SysApplication.getApplication(), "相册上传", Toast.LENGTH_SHORT).show();
                        selectPhoto();

                        break;
                }
            }
        });
    }

    public void selectPhoto() {

        RxPermissions rxPermissions = new RxPermissions(this);


        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            Matisse.from(UploadAvatarActivity.this)
                                    .choose(MimeType.allOf())
                                    .countable(true)
                                    .capture(true)
                                    .captureStrategy(
                                            new CaptureStrategy(true, "com.king.reading.fileprovider"))
                                    .maxSelectable(1)
                                    //                                            .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                    .gridExpectedSize(
                                            getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                    .thumbnailScale(0.85f)
                                    .imageEngine(new GlideEngine())
                                    .forResult(REQUEST_CODE_CHOOSE);

                        } else {
                            Toast.makeText(UploadAvatarActivity.this, "权限阻止", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mTvCropTips.setVisibility(View.VISIBLE);
            List<Uri> uris = Matisse.obtainResult(data);
            int size = uris.size();
            Bitmap bitmap = BitmapUtils.decodeUriAsBitmap(uris.get(0), SysApplication.getApplication());
            cropImageView.setImageBitmap(bitmap);
            cropImageView.setCropAreaWidth(AppScreenMgr.getScreenWidth(getApplicationContext()));
            cropImageView.setAspectRatio(1, 1);
            cropImageView.setCropShape(CropImageView.CropShape.OVAL);
            cropImageView.setCropMode(CropImageView.CropMode.FIXCROPAREA);
        }
    }



}
