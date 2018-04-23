package org.ql.chess.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.litepal.crud.DataSupport;
import org.ql.chess.R;
import org.ql.chess.db.Role;
import org.ql.chess.db.SingleScore;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/16
 *
 * @author ql
 */

public class ScoreListAdapter extends BaseQuickAdapter<SingleScore, BaseViewHolder> {
    public ScoreListAdapter() {
        super(R.layout.item_text);
    }

    @Override
    protected void convert(BaseViewHolder helper, SingleScore item) {
        Role role = item.getRole();
        String name = "";
        if (role != null) {
            name = role.getName();
        }

        helper.setText(R.id.item_text_tv_0, String.format("%s:", name))
                .setText(R.id.item_text_tv_1, String.format("%.2f", item.getScore()));
    }
}
