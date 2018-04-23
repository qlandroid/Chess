package org.ql.chess.adapter;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/17
 *
 * @author ql
 */
public interface IRoleSelectEntity {
    CharSequence getRoleName();

    boolean isSelect();

    void setSelect(boolean b);
}
