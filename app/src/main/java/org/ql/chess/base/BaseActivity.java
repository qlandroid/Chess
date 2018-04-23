package org.ql.chess.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.ql.bindview.BindViewUtils;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.ql.chess.R;

import java.util.ArrayList;


/**
 * Created by mrqiu on 2017/9/19.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int CHOOSE_PHOTO = 0xffff;
    public static final String BUNDLE = "bundle";
    private BaseFragment currentKJFragment;
    public QMUITopBar mTopBar;
    private QMUITipDialog mLoadingDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
        createView();
        BindViewUtils.find(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initBar();

        initStatusBar();
        initData();
        init(savedInstanceState);
        initWidget();

    }

    public void initBar() {
        View view = findViewById(R.id.topbar);
        if (view == null) {
            return;
        }
        mTopBar = (QMUITopBar) view;

        QMUIAlphaImageButton backImageButton = mTopBar.addLeftBackImageButton();
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*backImageButton.setImageResource(R.drawable.icon_back_blue);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) backImageButton.getLayoutParams();
        layoutParams.width = 500;
        layoutParams.leftMargin = 16;
        backImageButton.setLayoutParams(layoutParams);
        backImageButton.setChangeAlphaWhenPress(true);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });*/
    }

    public void init(Bundle savedInstanceState) {
    }

    ;

    public void initStatusBar() {


    }


    /*********************************************************************/
    /*********************************************************************/
    /********************
     * 用于隐藏软键盘
     **********************************/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /********************************************************************/

    public abstract void createView();

    public void initData() {

    }

    public void initWidget() {

    }

    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            onClickKeyToBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 当点击返回键
     */
    public void onClickKeyToBack() {

    }

    /***********************************************************************/

    public void myChangeFragment(int resView, BaseFragment targetFragment) {
        if (targetFragment.equals(currentKJFragment)) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(resView, targetFragment, targetFragment.getClass()
                    .getName());
        }
        if (targetFragment.isHidden()) {
            transaction.show(targetFragment);
            targetFragment.onChange();
        }
        if (currentKJFragment != null && currentKJFragment.isVisible()) {
            transaction.hide(currentKJFragment);
        }
        currentKJFragment = targetFragment;
        transaction.commit();

    }

    private final static int MIN_CLICK_TIME = 300;
    private long lastTime;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.qmui_topbar_item_left_back) {
            finish();
        }
        if (System.currentTimeMillis() - lastTime > MIN_CLICK_TIME) {
            forbidClick(v);
            lastTime = System.currentTimeMillis();
        }
    }

    public void forbidClick(View v) {

    }

    public void toFocusable(View v) {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
    }

    public <T extends Object> void startActivity(Class<T> t) {
        Intent intent = new Intent(this, t);
        startActivity(intent);
    }

    public <T extends BaseActivity> void startActivity(Class<T> t, int requestCode) {
        Intent intent = new Intent(this, t);
        startActivityForResult(intent, requestCode);
    }

    public <T extends BaseActivity> void startActivity(Class<T> t, Bundle bundle) {
        Intent intent = new Intent(this, t);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    public <T extends BaseActivity> void startActivity(Class<T> t, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, t);
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, requestCode);
    }

    public Bundle getBundle() {
        Intent intent = getIntent();
        if (intent == null) {
            return null;
        }
        return intent.getBundleExtra("bundle");
    }

    public void displayLoadingDialog(CharSequence msg) {
        cancelLoadingDialog();
        mLoadingDialog = new QMUITipDialog.Builder(this)
                .setTipWord(msg)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        mLoadingDialog.show();
    }

    public void cancelLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**********************************************************************************/
    /******************************
     * 打开相册
     *********************************/
    public void toOpenAlbum() {
        //判断是否添加权限 读取照片
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //当前没有权限,去申请权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        } else {
            //已经获取权限
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);//打开相册
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
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

    public void displayImage(String imagePath) {

    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
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

    public void onImgSelectResult(ArrayList<String> mSelectPath) {

    }

    public void openLocationSuccess() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] granResults) {
        switch (requestCode) {
            case 1:
                if (granResults.length > 0 && granResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
        }

    }

    public BaseActivity setTextView(String s, TextView tv) {
        setTextView(s, "", tv);
        return this;
    }

    public BaseActivity setTextView(String s, String normal, TextView tv) {
        if (TextUtils.isEmpty(s)) {
            s = normal;
        }
        tv.setText(s);
        return this;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }




    public void displayMessageDialog(CharSequence msg) {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage(msg)
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void displayMessageDialog(CharSequence msg, String action, QMUIDialogAction.ActionListener l) {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage(msg)
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.cancel();
                    }
                })
                .addAction(action, l)
                .show();
    }
}


