package org.ql.chess.adapter;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.ql.chess.R;
import org.ql.chess.db.Role;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/17
 *
 * @author ql
 */

public class RoleSingleAdapter extends BaseQuickAdapter<Role, BaseViewHolder> {
    public RoleSingleAdapter() {
        super(R.layout.item_text);
    }

    @Override
    protected void convert(BaseViewHolder helper, Role item) {
        helper.setText(R.id.item_text_tv_0, item.getName());
    }
}
