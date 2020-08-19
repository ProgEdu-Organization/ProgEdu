package fcu.selab.progedu.data;

import fcu.selab.progedu.service.ScoreModeEnum;

public class PairMatching {

    private int id;

    private int auId;

    private int reviewId;

    private ScoreModeEnum scoreModeEnum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuId() {
        return auId;
    }

    public void setAuId(int auId) {
        this.auId = auId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public ScoreModeEnum getScoreModeEnum() {
        return scoreModeEnum;
    }

    public void setScoreModeEnum(ScoreModeEnum scoreModeEnum) {
        this.scoreModeEnum = scoreModeEnum;
    }
}
