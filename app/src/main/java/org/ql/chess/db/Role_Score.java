package org.ql.chess.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/18
 *
 * @author ql
 */

public class Role_Score extends DataSupport {
    private int id;
    private int roleId;
    private int scoreId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    public static void saveR_S(int scoreId, List<Role> roles) {
        for (Role role : roles) {
            Role_Score s = new Role_Score();
            s.setRoleId(role.getId());
            s.setScoreId(scoreId);
            s.save();
        }
    }

    public Role getRole() {

        return DataSupport.where("id = ?", String.valueOf(roleId)).find(Role.class).get(0);
    }

    public static void delectByScoreId(int scoreId) {
        List<Role_Score> role_scores = DataSupport.where("scoreid = ?", String.valueOf(scoreId)).find(Role_Score.class);
        if (role_scores == null) {
            return;
        }
        for (Role_Score role_score : role_scores) {
            role_score.delete();
        }
    }

    public static List<Role> getRoleByScoreId(int scoreId) {
        List<Role_Score> role_scores = DataSupport.where("scoreId = ?", String.valueOf(scoreId)).find(Role_Score.class);
        List<Role> list = new ArrayList<>();
        for (Role_Score role_score : role_scores) {
            list.add(role_score.getRole());
        }
        return list;

    }
}
