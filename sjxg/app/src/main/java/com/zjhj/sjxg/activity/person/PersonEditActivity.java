package com.zjhj.sjxg.activity.person;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.soundcloud.android.crop.Crop;
import com.zjhj.commom.api.CommonApi;
import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.result.MapiImageResult;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.FileUtil;
import com.zjhj.commom.util.JGJBitmapUtils;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.base.RequestCode;
import com.zjhj.sjxg.util.ControllerUtil;
import com.zjhj.sjxg.widget.MainAlertDialog;
import com.zjhj.sjxg.widget.ModifySingleDialog;
import com.zjhj.sjxg.widget.PhotoDialog;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonEditActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.nick_tv)
    TextView nickTv;
    @Bind(R.id.phone_tv)
    TextView phoneTv;

    PhotoDialog photoDialog;
    ModifySingleDialog modifySingleDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_edit);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back);
        center.setText("个人资料");

        MapiUserResult userResult = userSP.getUserBean();
        if(null!=userResult){
            phoneTv.setText(TextUtils.isEmpty(userResult.getMobile())?"":userResult.getMobile());
            nickTv.setText(TextUtils.isEmpty(userResult.getNickname())?"":userResult.getNickname());
        }

        String avatar_pic = TextUtils.isEmpty(userResult.getAvatar_pic())?"":userResult.getAvatar_pic();
        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(avatar_pic);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(45), DPUtil.dip2px(45)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController( image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        image.setController(controller);

        modifySingleDialog = new ModifySingleDialog(this);
        modifySingleDialog.setLeftBtnText("取消").setRightBtnText("保存").setTitle("修改昵称").setHint("请输入昵称");
        modifySingleDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        modifySingleDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void initListener() {
        modifySingleDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifySingleDialog.dismiss();
            }
        });

        modifySingleDialog.setRightClickListener(new View.OnClickListener() {//提交
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(modifySingleDialog.getInfo())) {
                    MainToast.showShortToast("请输入昵称");
                    return;
                }
                modify("",modifySingleDialog.getInfo(),2);
                modifySingleDialog.dismiss();
            }
        });
    }

    private void modify(String head, String name, final int type){
        showLoading();
        UserApi.saveinfo(this, head, name, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                MapiUserResult userResult =  userSP.getUserBean();
                if(2==type){
                    String nickname = success.getJSONObject("data").getString("nickname");
                    userResult.setNickname(TextUtils.isEmpty(nickname)?"":nickname);
                    userSP.saveUserBean(userResult);
                    nickTv.setText(TextUtils.isEmpty(nickname)?"":nickname);
                }else if(1==type){
                    String avatar_pic = success.getJSONObject("data").getString("avatar_pic");
                    userResult.setAvatar_pic(TextUtils.isEmpty(avatar_pic)?"":avatar_pic);
                    userSP.saveUserBean(userResult);

                    //创建将要下载的图片的URI
                    Uri imageUri = Uri.parse(TextUtils.isEmpty(avatar_pic)?"":avatar_pic);
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                            .setResizeOptions(new ResizeOptions(DPUtil.dip2px(45), DPUtil.dip2px(45)))
                            .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController( image.getController())
                            .setControllerListener(new BaseControllerListener<ImageInfo>())
                            .build();
                    image.setController(controller);

                }

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        MapiUserResult userResult = userSP.getUserBean();
        if(null!=userResult){
            phoneTv.setText(userResult.getMobile());
        }
    }

    @OnClick({R.id.back, R.id.head_ll, R.id.nick_ll, R.id.phone_ll, R.id.addr_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.head_ll:
                if (photoDialog == null)
                    photoDialog = new PhotoDialog(this, R.style.image_dialog_theme);
                photoDialog.setImagePath("head_image.jpg");
                photoDialog.showDialog();
                break;
            case R.id.nick_ll:
                modifySingleDialog.setInfo(TextUtils.isEmpty(nickTv.getText()) ? "" : nickTv.getText().toString());
                modifySingleDialog.show();
                break;
            case R.id.phone_ll:
                ControllerUtil.go2ModifyPhone();
                break;
            case R.id.addr_ll:
                ControllerUtil.go2AddrList();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        DebugLog.i("requestCode=" + requestCode + "resultCode=" + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.CAMERA:
                    File picture = FileUtil.createFile(this, "head_image.jpg", FileUtil.TYPE_IMAGE);
                    startPhotoZoom(Uri.fromFile(picture));
                    break;
                case RequestCode.PICTURE:
                    if (data != null)
                        startPhotoZoom(data.getData());
                    break;
                case Crop.REQUEST_CROP: //缩放以后
                    if (data != null) {
                        File picture2 = FileUtil.createFile(this, "head_image_crop.jpg", FileUtil.TYPE_IMAGE);
                        String filename = picture2.getAbsolutePath();
//                        Bitmap bitmap = BitmapFactory.decodeFile(filename);
//                        JGJBitmapUtils.saveBitmap2file(bitmap, filename);
                        if (JGJBitmapUtils.rotateBitmapByDegree(filename, filename, JGJBitmapUtils.getBitmapDegree(filename))) {
                            uploadImage(picture2);
                        } else
                            DebugLog.i("图片保存失败");
                    }
                    break;
            }
        }
    }

    private void uploadImage(File file) {
        showLoading();
        CommonApi.uploadImage(this, file, new RequestCallback<MapiImageResult>() {
            @Override
            public void success(MapiImageResult success) {
                hideLoading();
                if (null != success) {

                    String url = TextUtils.isEmpty(success.getUrl())?"":success.getUrl();

                    modify(url,"",1);

                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                DebugLog.i(message);
            }
        });
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Uri outUrl = Uri
                .fromFile(FileUtil.createFile(this, "head_image_crop.jpg", FileUtil.TYPE_IMAGE));
        Crop.of(uri, outUrl).asSquare().withMaxSize(600, 600).start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=photoDialog) {
            photoDialog.dismiss();
            photoDialog = null;
        }

        if(null!=modifySingleDialog) {
            modifySingleDialog.dismiss();
            modifySingleDialog = null;
        }

    }
}
