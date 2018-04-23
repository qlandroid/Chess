package org.ql.chess;

import android.ql.bindview.BindView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.ql.chess.base.BaseBindActivity;
import org.ql.chess.base.Layout;

@Layout(layoutRes = R.layout.activity_function, title = "功能选择",backRes = 0)
public class FunctionActivity extends BaseBindActivity {
    @BindView(R.id.activity_function_tv_to_add_role)
    TextView tvToAddRole;
    @BindView(R.id.activity_function_tv_to_score)
    TextView tvToScore;

    @Override
    public void initWidget() {
        super.initWidget();
        tvToAddRole.setOnClickListener(this);
        tvToScore.setOnClickListener(this);
    }

    @Override
    public void forbidClick(View v) {
        super.forbidClick(v);
        switch (v.getId()) {
            case R.id.activity_function_tv_to_add_role:
                startActivity(RoleEditActivity.class);
                break;
            case R.id.activity_function_tv_to_score:
                startActivity(ScoreSelectActivity.class);
                break;
        }
    }
}
