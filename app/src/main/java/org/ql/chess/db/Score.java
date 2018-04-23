package org.ql.chess.db;

import org.litepal.crud.DataSupport;
import org.ql.chess.adapter.IScoreSelectEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/16
 *
 * @author ql
 */

public class Score extends DataSupport implements IScoreSelectEntity {
    private int id;
    private String scoreName;//比赛名称
    private long createDate;//创建时间


    private String address;//创建地点


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScoreName() {
        if (scoreName == null) {
            return "";
        }
        return scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }


    public String getAddress() {
        if (address == null) {
            return "";
        }
        return address;
    }

    @Override
    public List<Role> getRoles() {
        List<Role_Score> role_scores = DataSupport.where("scoreid = ?", String.valueOf(id)).find(Role_Score.class);
        List<Role> roles = new ArrayList<>();
        for (Role_Score role_score : role_scores) {
            Role role = role_score.getRole();
            roles.add(role);
        }
        return roles;
    }


    @Override
    public List<SingleScore> getSingleScoreList() {
        return getTotalScoreById(id);
    }

    private List<SingleScore> getTotalScoreById(int scoreId) {
        List<ScoreGroup> scoreGroups = DataSupport.where("scoreId = ?", String.valueOf(scoreId)).order("id desc").find(ScoreGroup.class);

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
        return list;
    }

    public void del() {
        Role_Score.delectByScoreId(id);
        for (ScoreGroup scoreGroup : getScoreGroup()) {
            scoreGroup.deleteSingScore();
            scoreGroup.delete();
        }

    }

    public static void delectAllById(int id) {
        Role_Score.delectByScoreId(id);
    }

    public List<ScoreGroup> getScoreGroup() {
        return DataSupport.where("scoreid = ?", String.valueOf(id)).find(ScoreGroup.class);
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
