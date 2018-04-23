package org.ql.chess;


import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/16
 *
 * @author ql
 */

public class MyApplication extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
