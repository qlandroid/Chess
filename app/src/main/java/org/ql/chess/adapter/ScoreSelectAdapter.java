package org.ql.chess.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.ql.chess.R;
import org.ql.chess.ScoreSelectActivity;
import org.ql.chess.db.Role;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/17
 *
 * @author ql
 */

public class ScoreSelectAdapter extends BaseQuickAdapter<IScoreSelectEntity, BaseViewHolder> {
    public ScoreSelectAdapter() {
        super(R.layout.item_score);
    }

    @Override
    protected void convert(BaseViewHolder helper, IScoreSelectEntity item) {
        helper.setText(R.id.item_score_tv_name, item.getScoreName())
                .setText(R.id.item_score_tv_address, item.getAddress());
        RecyclerView rv = helper.getView(R.id.item_score_rv);
        if (rv.getAdapter() == null) {
            rv.setLayoutManager(new GridLayoutManager(rv.getContext(), 2));
            ScoreListAdapter s = new ScoreListAdapter();
            rv.setAdapter(s);
        }

        ScoreListAdapter s = (ScoreListAdapter) rv.getAdapter();
        s.setNewData(item.getSingleScoreList());
        s.notifyDataSetChanged();
    }

}
