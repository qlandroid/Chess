package org.ql.chess;

import android.app.Activity;
import android.content.Intent;
import android.ql.bindview.BindView;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.litepal.crud.DataSupport;
import org.ql.chess.adapter.ScoreAdapter;
import org.ql.chess.adapter.ScoreListAdapter;
import org.ql.chess.base.BaseBindActivity;
import org.ql.chess.base.Layout;
import org.ql.chess.db.Role;
import org.ql.chess.db.Role_Score;
import org.ql.chess.db.ScoreGroup;
import org.ql.chess.db.SingleScore;

import java.util.ArrayList;
import java.util.List;

@Layout(layoutRes = R.layout.activity_score_details, title = "记分器")
public class ScoreDetailsActivity extends BaseBindActivity implements BaseQuickAdapter.OnItemLongClickListener {
    private static final String SCORE_ID = "scoreId";
    private static final int REQUEST_ADD_GROUP = 222;
    @BindView(R.id.activity_score_rv)
    RecyclerView rv;
    @BindView(R.id.activity_score_rv_total)
    RecyclerView rvTotal;

    private int scoreId;//记分id;
    List<ScoreGroup> scoreGroups;
    private ScoreAdapter scoreAdapter = new ScoreAdapter();
    private ScoreListAdapter scoreListAdapter = new ScoreListAdapter();

    public static void putScoreId(int scoreId, Bundle bundle) {
        bundle.putInt(SCORE_ID, scoreId);
    }

    @Override
    public void initData() {
        super.initData();

        resetData();
    }

    private void resetData() {
        scoreId = getBundle().getInt(SCORE_ID);
        scoreGroups = DataSupport.where("scoreId = ?", String.valueOf(scoreId)).order("id desc").find(ScoreGroup.class);
        scoreAdapter.setNewData(scoreGroups);
        scoreAdapter.notifyDataSetChanged();

        List<Role> roleByScoreId = Role_Score.getRoleByScoreId(scoreId);

        //计算总分
        List<SingleScore> list = new ArrayList<>();
        for (Role role : roleByScoreId) {
            SingleScore e = new SingleScore();
            e.setRoleId(role.getId());
            list.add(e);
        }
        for (ScoreGroup scoreGroup : scoreGroups) {
            for (SingleScore singleScore : scoreGroup.getChildrenOfSingleScore()) {
                for (SingleScore s : list) {
                    if (singleScore.getRoleId() == s.getRoleId()) {
                        float score = s.getScore();
                        score += singleScore.getScore();
                        s.setScore(score);
                    }
                }
            }

        }

        scoreListAdapter.setNewData(list);
        scoreAdapter.notifyDataSetChanged();
        scoreListAdapter.notifyDataSetChanged();

    }

    @Override
    public void initBar() {
        super.initBar();
        mTopBar.addRightTextButton("添加数据", R.id.top_bar_right).setOnClickListener(this);
    }

    @Override
    public void initWidget() {
        super.initWidget();


        rv.setLayoutManager(new LinearLayoutManager(this));


        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        rv.setAdapter(scoreAdapter);

        scoreAdapter.setOnItemLongClickListener(this);

        rvTotal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTotal.setAdapter(scoreListAdapter);
    }


    @Override
    public void forbidClick(View v) {
        super.forbidClick(v);
        switch (v.getId()) {
            case R.id.top_bar_right:
                Bundle bundle = new Bundle();
                ScoreGroupActivity.putScoreId(scoreId, bundle);
                startActivity(ScoreGroupActivity.class, bundle, REQUEST_ADD_GROUP);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_ADD_GROUP:
                resetData();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
        final ScoreGroup scoreGroup = scoreGroups.get(position);
        new QMUIDialog.MessageDialogBuilder(this)
                .setMessage(String.format("请确定是否要删除 序号:%d 记录？", scoreGroups.size() - position))
                .setTitle("提示")
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {

                        ScoreDbHelper.delectScoreGroupById(scoreGroup.getId());
                        resetData();
                        dialog.cancel();
                    }
                })
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.cancel();
                    }
                }).show();
        return true;
    }
}
