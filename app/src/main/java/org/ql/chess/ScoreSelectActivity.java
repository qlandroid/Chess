package org.ql.chess;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.ql.bindview.BindView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.litepal.crud.DataSupport;
import org.ql.chess.adapter.IScoreSelectEntity;
import org.ql.chess.adapter.ScoreSelectAdapter;
import org.ql.chess.base.BaseBindActivity;
import org.ql.chess.base.Layout;
import org.ql.chess.db.Score;
import org.ql.chess.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

@Layout(layoutRes = R.layout.activity_score_list, title = "比赛选择")
public class ScoreSelectActivity extends BaseBindActivity implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {

    private static final int REQUEST_EDIT_SCORE = 222;
    private static final int REQUEST_DETAILS = 333;
    @BindView(R.id.activity_score_select_list_rv)
    RecyclerView rv;

    private List<IScoreSelectEntity> list = new ArrayList<>();
    ScoreSelectAdapter adapter = new ScoreSelectAdapter();
    private List<Score> all;

    @Override
    public void initBar() {
        super.initBar();
        mTopBar.addRightTextButton("添加比赛", R.id.top_bar_right).setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        refresh();
    }

    private void refresh() {
        list.clear();
        all = DataSupport.order("id desc").find(Score.class);
        if (all == null) {
            return;
        }
        for (Score score : all) {
            list.add(score);
        }

        adapter.setNewData(list);
    }


    @Override
    public void initWidget() {
        super.initWidget();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 10, Color.GRAY));

        rv.setAdapter(adapter);

        adapter.setOnItemLongClickListener(this);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);

    }

    @Override
    public void forbidClick(View v) {
        super.forbidClick(v);
        switch (v.getId()) {
            case R.id.top_bar_right:
                startActivity(ScoreEditActivity.class, REQUEST_EDIT_SCORE);
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Bundle bundle = new Bundle();
        Score iScoreSelectEntity = (Score) list.get(position);
        ScoreDetailsActivity.putScoreId(iScoreSelectEntity.getId(), bundle);
        startActivity(ScoreDetailsActivity.class, bundle, REQUEST_DETAILS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DETAILS){
            refresh();
            return;
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_EDIT_SCORE:
                refresh();
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        final Score score = all.get(position);
        new QMUIDialog.MessageDialogBuilder(this)
                .setMessage(String.format("比赛名称:%s\t\n地址:%s\t\n是否确定删除本次比赛？", score.getScoreName(), score.getAddress()))
                .setTitle("提示")
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        ScoreDbHelper.delectScoreById(score.getId());
                        dialog.cancel();
                        refresh();
                    }
                }).addAction("取消", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.cancel();
            }
        }).show();
        return true;
    }
}
