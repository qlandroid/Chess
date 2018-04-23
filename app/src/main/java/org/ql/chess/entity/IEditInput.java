package org.ql.chess.entity;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/3/16
 *
 * @author ql
 */

public interface IEditInput {
    void setInput(String s);

    String getInput();

    int getEditInputType();

    /**
     * 获得提示信息,提示信息为当前描述详情信息
     * @return
     */
    CharSequence getHintContent();

    boolean isShowHintContent();
}
