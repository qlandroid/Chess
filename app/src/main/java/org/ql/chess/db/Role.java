package org.ql.chess.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/16
 *
 * @author ql
 */

public class Role extends DataSupport {

    private int id;
    private String name;
    private String cardId;//手机号
    private List<Score> scores = new ArrayList<>();

    private List<SingleScore> singleScores = new ArrayList<>();

    public List<SingleScore> getSingleScores() {
        return singleScores;
    }

    public void setSingleScores(List<SingleScore> singleScores) {
        this.singleScores = singleScores;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
