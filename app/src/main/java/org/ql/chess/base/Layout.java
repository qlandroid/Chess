package org.ql.chess.base;

import org.ql.chess.R;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ql
 *         邮箱 email:strive_bug@yeah.net
 *         创建时间 2017/11/25
 */
@Documented
@Inherited
//该注解可以作用于方法,类与接口
@Target({ElementType.METHOD, ElementType.TYPE})
//JVM会读取注解,所以利用反射可以获得注解
@Retention(RetentionPolicy.RUNTIME)
public @interface Layout {

    String title() default "";

    int layoutRes() default -1;

    int backRes() default R.drawable.qmui_icon_topbar_back;

    int topbarId() default R.id.topbar;
}
