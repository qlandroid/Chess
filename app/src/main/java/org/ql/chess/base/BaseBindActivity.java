package org.ql.chess.base;

import android.view.View;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.ql.chess.R;

/**
 * @author ql
 *         邮箱 email:strive_bug@yeah.net
 *         创建时间 2017/11/25
 */

public class BaseBindActivity extends BaseActivity {
    @Override
    public void createView() {
        LayoutUtils.bind(this);
    }

    @Override
    public void initStatusBar() {
        super.initStatusBar();
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.qmui_topbar_item_left_back) {
            clickTopBarBack();
        }

    }

    public void clickTopBarBack() {
        finish();
    }

    @Override
    public void initBar() {
    }



}
