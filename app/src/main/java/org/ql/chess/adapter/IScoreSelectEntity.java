package org.ql.chess.adapter;

import org.ql.chess.db.Role;
import org.ql.chess.db.SingleScore;

import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/17
 *
 * @author ql
 */
public interface IScoreSelectEntity {
    CharSequence getScoreName();

    CharSequence getAddress();

    List<Role> getRoles();

    List<SingleScore> getSingleScoreList();

    long getCreateDate();
}
