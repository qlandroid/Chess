package org.ql.chess.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.ql.bindview.BindViewUtils;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.ql.chess.R;

/**
 * Created by mrqiu on 2017/10/2.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    private final int REQUEST_CHOOSE_PHOTO = 22;//点击头像切换头像

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int viewId = createView();
        View view = inflater.inflate(viewId, container, false);
        BindViewUtils.find(this, view);
        return view;
    }

    public int createView() {
        return -1;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initWidget(view);

    }


    protected void initWidget(View view) {
    }

    protected void initData() {
    }

    public void onChange() {

    }

    public void startActivity(Class toAty, Bundle bundle) {
        Intent intent = new Intent(getActivity(), toAty);
        intent.putExtra(BaseActivity.BUNDLE, bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    public void widgetClick(View v) {
    }

    public void startActivity(Class clazz) {
        Intent i = new Intent(getActivity(), clazz);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void startActivity(Class clazz, int requestCode) {
        Intent i = new Intent(getActivity(), clazz);
        startActivityForResult(i, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void startActivity(Class inputStringActivityClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(getActivity(), inputStringActivityClass);
        intent.putExtra(BaseActivity.BUNDLE, bundle);
        startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }



    public void setTextView(String s, TextView tv) {
        setTextView(s, "", tv);
    }

    public void setTextView(String s, String normal, TextView tv) {
        if (TextUtils.isEmpty(s)) {
            s = normal;
        }
        tv.setText(s);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void displayNoDesign() {
        new QMUIDialog.MessageDialogBuilder(getContext())
                .setMessage("该版本未开放，请耐心等待")
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.cancel();
                    }
                }).setTitle("提示")
                .show();
    }

    public void displayMsgDialog(String title, String msg) {
        new QMUIDialog.MessageDialogBuilder(getContext())
                .setMessage(msg)
                .setTitle(title)
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void displayMsgDialog(String msg) {
        displayMsgDialog("提示", msg);
    }


    public void displayLoading() {
        displayLoading("");
    }

    public void displayLoading(CharSequence msg) {
        ((BaseActivity) getActivity()).displayLoadingDialog(msg);
    }

    public void cancelLoading() {
        ((BaseActivity) getActivity()).cancelLoadingDialog();

    }

    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);//打开相册
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHOOSE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        //4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getContext(), uri)) {
            //如果是document类型的Uri ，则通过document id处理；
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id;
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads")
                        , Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri ,则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    public void displayImage(String imagePath) {

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

}
