package org.ql.chess;

import android.app.Activity;
import android.ql.bindview.BindView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.litepal.crud.DataSupport;
import org.ql.chess.adapter.IRoleSelectEntity;
import org.ql.chess.adapter.RoleSelectAdapter;
import org.ql.chess.base.BaseBindActivity;
import org.ql.chess.base.Layout;
import org.ql.chess.db.Role;
import org.ql.chess.db.Role_Score;
import org.ql.chess.db.Score;

import java.util.ArrayList;
import java.util.List;

@Layout(layoutRes = R.layout.activity_score_edit, title = "添加比赛")
public class ScoreEditActivity extends BaseBindActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.activity_score_edit_tv_ok)
    TextView tvOk;
    @BindView(R.id.activity_score_edit_et_input_name)
    EditText etInputName;
    @BindView(R.id.activity_score_edit_et_input_address)
    EditText etInputAddress;
    @BindView(R.id.activity_score_edit_tv_select_count)
    TextView tvSelectCount;
    @BindView(R.id.activity_score_edit_rv)
    RecyclerView rv;

    RoleSelectAdapter adapter = new RoleSelectAdapter();
    List<IRoleSelectEntity> list = new ArrayList<>();
    private List<Role> roleAll;

    @Override
    public void initData() {
        super.initData();
        roleAll = DataSupport.findAll(Role.class);
        if (roleAll == null) {
            return;
        }

        for (Role role : roleAll) {
            list.add(new RoleSelectEntity(role.getName()));
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();
        adapter.setNewData(list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        adapter.setOnItemClickListener(this);

        tvOk.setOnClickListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        IRoleSelectEntity iRoleSelectEntity = list.get(position);
        iRoleSelectEntity.setSelect(!iRoleSelectEntity.isSelect());
        int selectCount = 0;
        for (IRoleSelectEntity roleSelectEntity : list) {
            if (roleSelectEntity.isSelect()) {
                selectCount++;
            }
        }
        tvSelectCount.setText(String.format("选中人数:%d", selectCount));
        adapter.notifyItemChanged(position);
    }

    @Override
    public void forbidClick(View v) {
        super.forbidClick(v);
        switch (v.getId()) {
            case R.id.activity_score_edit_tv_ok:
                if (TextUtils.isEmpty(etInputName.getText())) {
                    return;
                }
                if (TextUtils.isEmpty(etInputAddress.getText())) {
                    return;
                }
                List<Role> roles = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    IRoleSelectEntity iRoleSelectEntity = list.get(i);
                    if (iRoleSelectEntity.isSelect()) {
                        roles.add(roleAll.get(i));
                    }
                }

                if (roles.size() == 0) {
                    displayMessageDialog("请选择人数");
                    return;
                }

                Score score = new Score();
                score.setAddress(etInputAddress.getText().toString().trim());
                score.setScoreName(etInputName.getText().toString().trim());
                score.setCreateDate(System.currentTimeMillis());

                if (!score.save()) {
                    displayMessageDialog("保存失败");
                    return;
                }

                Role_Score.saveR_S(score.getId(), roles);
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }
    }

    public static class RoleSelectEntity implements IRoleSelectEntity {
        private String name;
        private boolean isSelect;

        public RoleSelectEntity(String name) {
            this.name = name;
        }

        public RoleSelectEntity(String name, boolean isSelect) {
            this.name = name;
            this.isSelect = isSelect;
        }

        @Override
        public CharSequence getRoleName() {
            if (name == null) {
                return "";
            }
            return name;
        }

        @Override
        public boolean isSelect() {
            return isSelect;
        }

        @Override
        public void setSelect(boolean b) {
            isSelect = b;
        }
    }
}
