package org.ql.chess.adapter;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.ql.chess.R;
import org.ql.chess.db.Role;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/16
 *
 * @author ql
 */

public class AddRoleAdapter extends BaseQuickAdapter<Role, BaseViewHolder> {
    public AddRoleAdapter() {
        super(R.layout.item_role_add);
    }

    @Override
    protected void convert(BaseViewHolder helper, Role item) {
        helper.setText(R.id.item_role_add_tv_name, item.getName());
        helper.addOnClickListener(R.id.item_role_add_iv_remove);
    }
}
