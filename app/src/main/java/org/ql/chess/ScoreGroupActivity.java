package org.ql.chess;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.ql.bindview.BindView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.litepal.crud.DataSupport;
import org.ql.chess.adapter.IScoreGroupEntity;
import org.ql.chess.adapter.ScoreGroupAdapter;
import org.ql.chess.base.BaseBindActivity;
import org.ql.chess.base.Layout;
import org.ql.chess.db.Role;
import org.ql.chess.db.Score;
import org.ql.chess.db.ScoreGroup;
import org.ql.chess.db.SingleScore;
import org.ql.chess.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

@Layout(layoutRes = R.layout.activity_score_group, title = "添加对局分数")
public class ScoreGroupActivity extends BaseBindActivity {
    public static final String SCORE_ID = "scoreId";
    @BindView(R.id.activity_score_group_rv)
    RecyclerView rv;
    @BindView(R.id.activity_score_group_tv_ok)
    TextView tvOk;
    @BindView(R.id.activity_score_group_tv_check)
    TextView tvCheck;

    List<IScoreGroupEntity> list = new ArrayList<>();
    Score scoreBean;

    private ScoreGroupAdapter adapter = new ScoreGroupAdapter();
    private ScoreGroup sg;
    private Button button;


    public static void putScoreId(int id, Bundle b) {
        b.putInt(SCORE_ID, id);
    }

    @Override
    public void initData() {
        super.initData();
        int anInt = getBundle().getInt(SCORE_ID);
        scoreBean = DataSupport.find(Score.class, anInt);

        if (scoreBean.getRoles() == null) {
            return;
        }
        for (Role role : scoreBean.getRoles()) {
            list.add(new ScoreGroupEntity(role.getName(), "0"));
        }
        adapter.setNewData(list);

    }

    @Override
    public void initBar() {
        super.initBar();
        button = mTopBar.addRightTextButton("0", R.id.top_bar_right);
        button.setTextColor(Color.RED);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        tvOk.setOnClickListener(this);
        tvCheck.setOnClickListener(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 10, Color.GRAY));

        rv.setAdapter(adapter);
        adapter.setOnTextChangeListener(new ScoreGroupAdapter.OnTextChangeListener() {
            @Override
            public void onTextChange() {
                double total = 0;
                for (IScoreGroupEntity iScoreGroupEntity : list) {
                    String input = iScoreGroupEntity.getInput();
                    if (TextUtils.isEmpty(input)) {
                        continue;
                    }
                    try {
                        double v = Double.parseDouble(input);
                        total += v;
                    } catch (Exception e) {

                    }
                }
                String format = String.format("%.0f", total);
                setTitleScore(format);
            }
        });
    }

    @Override
    public void forbidClick(View v) {
        super.forbidClick(v);
        switch (v.getId()) {
            case R.id.activity_score_group_tv_ok:
                long date = System.currentTimeMillis();
                sg = new ScoreGroup();
                sg.setScoreId(scoreBean.getId());
                sg.setCreateData(date);

                List<SingleScore> singleScores = new ArrayList<>();

                float total = 0;
                for (int i = 0; i < list.size(); i++) {
                    IScoreGroupEntity iScoreGroupEntity = list.get(i);
                    float score = 0;
                    try {
                        score = Float.parseFloat(iScoreGroupEntity.getInput());
                        total = total + score;
                        SingleScore e = new SingleScore();
                        e.setScore(score);
                        Role role = scoreBean.getRoles().get(i);
                        e.setRoleId(role.getId());
                        e.setCreateData(date);
                        singleScores.add(e);
                    } catch (Exception e) {
                        displayMessageDialog("请输入正确分数");
                        return;
                    }
                }
                if (total > 0) {
                    //有人输少了或者有人赢多了，请检查；
                    new QMUIDialog.MessageDialogBuilder(this)
                            .setTitle("提示")
                            .setMessage(String.format("有人输少了或者有人赢多了，请检查\t\n合计总分%f,请检查", total))
                            .addAction("确定", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.cancel();
                                }
                            }).show();
                    return;
                }
                if (total < 0) {
                    //有人输多了或者有人赢少了，请检查
                    new QMUIDialog.MessageDialogBuilder(this)
                            .setTitle("提示")
                            .setMessage(String.format("有人输多了或者有人赢少了，请检查\t\n合计总分%f,请检查", total))
                            .addAction("确定", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.cancel();
                                }
                            }).show();
                    return;
                }
                if (total != 0) {

                    new QMUIDialog.MessageDialogBuilder(this)
                            .setTitle("提示")
                            .setMessage(String.format("合计总分%f,请检查", total))
                            .addAction("确定", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.cancel();
                                }
                            }).show();
                    return;
                }
                sg.save();
                for (SingleScore singleScore : singleScores) {
                    singleScore.setScoreGroupId(sg.getId());
                    singleScore.save();
                }

                setResult(Activity.RESULT_OK);
                finish();
                break;
            case R.id.activity_score_group_tv_check:
                checkScore();
                break;
        }
    }

    private void checkScore() {
        float total = 0;
        for (int i = 0; i < list.size(); i++) {
            IScoreGroupEntity iScoreGroupEntity = list.get(i);
            float score = 0;
            try {
                score = Float.parseFloat(iScoreGroupEntity.getInput());
                total = total + score;
            } catch (Exception e) {
                displayMessageDialog("请输入正确分数");
                return;
            }
        }
        if (total > 0) {
            //有人输少了或者有人赢多了，请检查；
            new QMUIDialog.MessageDialogBuilder(this)
                    .setTitle("提示")
                    .setMessage(String.format("有人输少了或者有人赢多了，请检查\t\n合计总分%f,请检查", total))
                    .addAction("确定", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.cancel();
                        }
                    }).show();
            return;
        } else if (total < 0) {
            //有人输多了或者有人赢少了，请检查
            new QMUIDialog.MessageDialogBuilder(this)
                    .setTitle("提示")
                    .setMessage(String.format("有人输多了或者有人赢少了，请检查\t\n合计总分%f,请检查", total))
                    .addAction("确定", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.cancel();
                        }
                    }).show();
            return;
        }

        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage(String.format("分数正确"))
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.cancel();
                    }
                }).show();

    }

    public void setTitleScore(String titleScore) {
        button.setText(titleScore);
    }

    public static class ScoreGroupEntity implements IScoreGroupEntity {
        private String roleName;
        private String score;

        public ScoreGroupEntity(String roleName, String score) {
            this.roleName = roleName;
            this.score = score;
        }

        @Override
        public CharSequence getRoleName() {
            return roleName;
        }

        @Override
        public void setInput(String s) {
            score = s;
        }

        @Override
        public String getInput() {
            return score;
        }

        @Override
        public int getEditInputType() {
            return 0;
        }

        @Override
        public CharSequence getHintContent() {
            return "请输入分数";
        }

        @Override
        public boolean isShowHintContent() {
            return true;
        }
    }

}
