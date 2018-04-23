package org.ql.chess.db;

import org.litepal.crud.DataSupport;

/**
 * 描述： 单场比分 分数
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/16
 *
 * @author ql
 */

public class SingleScore extends DataSupport {

    private int id;
    private int roleId;
    private float score;//分数
    private long createData;//创建时间

    private int scoreGroupId;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getScoreGroupId() {
        return scoreGroupId;
    }

    public void setScoreGroupId(int scoreGroupId) {
        this.scoreGroupId = scoreGroupId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public long getCreateData() {
        return createData;
    }

    public void setCreateData(long createData) {
        this.createData = createData;
    }

    public Role getRole() {
        Role role;
        try {
             role = DataSupport.where("id = ?", String.valueOf(roleId)).find(Role.class).get(0);
        }catch (Exception e){
            return null;
        }
        return role;
    }
}
