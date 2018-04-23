package org.ql.chess.db;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 描述： 每次记分时 人员分布
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/16
 *
 * @author ql
 */

public class ScoreGroup extends DataSupport {

    private int id;
    private long createData;//创建时间
    private int scoreId;


    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateData() {
        return createData;
    }

    public void setCreateData(long createData) {
        this.createData = createData;
    }

    public List<SingleScore> getChildrenOfSingleScore() {
        return DataSupport.where("scoregroupid = ?", String.valueOf(id)).find(SingleScore.class);
    }


    public void deleteSingScore() {
        for (SingleScore singleScore : getChildrenOfSingleScore()) {
            singleScore.delete();
        }
        delete();
    }
}
