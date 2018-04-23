package org.ql.chess;

import android.ql.bindview.BindView;
import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.litepal.crud.DataSupport;
import org.ql.chess.adapter.AddRoleAdapter;
import org.ql.chess.base.BaseBindActivity;
import org.ql.chess.base.Layout;
import org.ql.chess.db.Role;

import java.util.ArrayList;
import java.util.List;

@Layout(layoutRes = R.layout.activity_role_edit, title = "角色编辑")
public class RoleEditActivity extends BaseBindActivity implements BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.activity_role_edit_rv)
    RecyclerView rv;
    @BindView(R.id.activity_role_edit_et_input_name)
    EditText et;
    @BindView(R.id.activity_role_edit_et_input_cardId)
    EditText etCardId;

    AddRoleAdapter adapter = new AddRoleAdapter();
    private List<Role> mRoleDatas = new ArrayList<>();

    @Override
    public void initData() {
        super.initData();
        mRoleDatas = DataSupport.findAll(Role.class);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        rv.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decor = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        adapter.setNewData(mRoleDatas);
        rv.addItemDecoration(decor);
        rv.setAdapter(adapter);

        adapter.setOnItemChildClickListener(this);
    }


    public void clickAdd(View view) {
        if (TextUtils.isEmpty(et.getText())) {
            return;
        }
        if (TextUtils.isEmpty(etCardId.getText())) {
            return;
        }
        Role role = new Role();
        role.setName(et.getText().toString());
        role.setCardId(etCardId.getText().toString());
        role.save();
        mRoleDatas.add(role);
        adapter.setNewData(mRoleDatas);
        adapter.notifyDataSetChanged();
        et.setText("");
        etCardId.setText("");
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Role role = mRoleDatas.get(position);
        if (ScoreDbHelper.checkRoleHaveScoreByRoleId(role.getId())) {
            displayMessageDialog(String.format("用户:%s\t\n有参加的比赛不能删除", role.getName()));
            return;
        }

        role.delete();
        mRoleDatas.remove(position);
        adapter.notifyDataSetChanged();
    }
}
