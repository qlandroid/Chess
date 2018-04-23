package org.ql.chess.base;

import android.text.TextUtils;
import android.view.View;

import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author ql
 *         邮箱 email:strive_bug@yeah.net
 *         创建时间 2017/11/25
 */

public class LayoutUtils {

    public static void bind(Object object) {
        Class<?> aClass = object.getClass();
        if (!aClass.isAnnotationPresent(Layout.class)) {
            return;
        }
        Layout annotation = aClass.getAnnotation(Layout.class);


        int layoutRes = annotation.layoutRes();
        if (-1 != layoutRes) {
            try {
                Method setLayoutRes = aClass.getMethod("setContentView", new Class[]{int.class});
                if (setLayoutRes != null) {
                    setLayoutRes.invoke(object, layoutRes);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        Field mTopBar = null;
        try {
            mTopBar = aClass.getField("mTopBar");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (mTopBar == null) {
            return;
        }
        QMUITopBar topBar = null;
        try {
            Method findViewById = aClass.getMethod("findViewById", int.class);
            if (findViewById == null) {
                return;
            }
            int tobarId = annotation.topbarId();
            Object obj = findViewById.invoke(object, tobarId);
            if (!(obj instanceof QMUITopBar)) {
                return;
            }
            topBar = (QMUITopBar) obj;

            mTopBar.set(object, obj);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (topBar == null) return;

        String title = annotation.title();
        if (!TextUtils.isEmpty(title)) {
            topBar.setTitle(title);
        }


        int backRes = annotation.backRes();
        if (0 != backRes) {
            QMUIAlphaImageButton backImageButton = topBar.addLeftBackImageButton();
            backImageButton.setOnClickListener((View.OnClickListener) object);
            backImageButton.setImageResource(backRes);
            backImageButton.setChangeAlphaWhenPress(true);
        }
    }
}
