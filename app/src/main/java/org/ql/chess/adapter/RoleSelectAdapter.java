package org.ql.chess.adapter;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.ql.chess.R;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/17
 *
 * @author ql
 */

public class RoleSelectAdapter  extends BaseQuickAdapter<IRoleSelectEntity,BaseViewHolder>{
    public RoleSelectAdapter() {
        super(R.layout.item_role_select);
    }

    @Override
    protected void convert(BaseViewHolder helper, IRoleSelectEntity item) {
        helper.setText(R.id.item_role_select_tv_name,item.getRoleName());

        helper.getView(R.id.item_role_select_iv_checkbox).setSelected(item.isSelect());
    }
}
