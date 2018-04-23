package org.ql.chess;

import org.litepal.crud.DataSupport;
import org.ql.chess.db.Role;
import org.ql.chess.db.Role_Score;
import org.ql.chess.db.Score;
import org.ql.chess.db.ScoreGroup;
import org.ql.chess.db.SingleScore;

import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/23
 *
 * @author ql
 */

public class ScoreDbHelper {


    public static void delectScoreById(int id) {
        //score_role scoreGroup singScore;
        Score score = DataSupport.find(Score.class, id);

        List<Role_Score> role_scores = DataSupport.where("scoreid = ?", String.valueOf(id)).find(Role_Score.class);

        for (Role_Score role_score : role_scores) {
            role_score.delete();
        }

        List<ScoreGroup> scoreGroups = DataSupport.where("scoreid = ?", String.valueOf(id)).find(ScoreGroup.class);
        for (ScoreGroup scoreGroup : scoreGroups) {
            List<SingleScore> singleScores = DataSupport.where("scoregroupid = ?", String.valueOf(scoreGroup.getId())).find(SingleScore.class);
            for (SingleScore singleScore : singleScores) {
                singleScore.delete();
            }
            scoreGroup.delete();
        }
        score.delete();

    }

    public static void delectScoreGroupById(int id) {
        ScoreGroup scoreGroup = DataSupport.find(ScoreGroup.class, id);
        List<SingleScore> singleScores = DataSupport.where("scoregroupid = ?", String.valueOf(id)).find(SingleScore.class);
        for (SingleScore singleScore : singleScores) {
            singleScore.delete();
        }
        scoreGroup.delete();
    }


    /**
     * 检查 角色是否拥有比赛
     *
     * @param id
     * @return true 有比赛,false 没有比赛
     */
    public static boolean checkRoleHaveScoreByRoleId(int id) {
        List<Role_Score> role_scores = DataSupport.where("roleid = ?", String.valueOf(id)).find(Role_Score.class);
        if (role_scores == null || role_scores.size() == 0) {
            return false;
        }
        return true;
    }
}
