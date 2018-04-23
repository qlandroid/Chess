package org.ql.chess.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.ql.chess.utils.DateUtils;
import org.ql.chess.R;
import org.ql.chess.db.ScoreGroup;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/16
 *
 * @author ql
 */

public class ScoreAdapter extends BaseQuickAdapter<ScoreGroup, BaseViewHolder> {
    public ScoreAdapter() {
        super(R.layout.item_rv);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreGroup item) {
        int i = getData().size() - helper.getAdapterPosition() ;
        helper.setText(R.id.item_rv_tv_0, String.format("序号:%d", i))
                .setText(R.id.item_rv_tv_date, DateUtils.getStringDate(item.getCreateData()));

        RecyclerView rv = helper.getView(R.id.item_rv);
        if (rv.getAdapter() == null) {
            ScoreListAdapter listAdapter = new ScoreListAdapter();
            rv.setLayoutManager(new GridLayoutManager(rv.getContext(), 2));
            rv.setAdapter(listAdapter);
        }
        ScoreListAdapter adapter = (ScoreListAdapter) rv.getAdapter();
        adapter.setNewData(item.getChildrenOfSingleScore());
        adapter.notifyDataSetChanged();
    }
}
