package fcu.selab.progedu.service;

public enum ScoreModeEnum {
  YESNOSCOREMODE("yesNoMode"), NUMBERSCOREMODE("numberScoreMode");

  private String scoreMode;

  private ScoreModeEnum(String scoreMode) {
    this.scoreMode = scoreMode;
  }

  /**
   * @param scoreMode is proJectStatus String
   * @return status is getStatusProjecTypeEnum object
   */
  public static ScoreModeEnum getScoreModeEnum(String scoreMode) {
    for (ScoreModeEnum scoreModeType : ScoreModeEnum.values()) {
      if (scoreModeType.getTypeName().equals(scoreMode)) {
        return scoreModeType;
      }
    }
    return null;
  }

  public String getTypeName() {
    return this.scoreMode;
  }
}
