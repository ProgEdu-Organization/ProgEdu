package fcu.selab.progedu.service;

import fcu.selab.progedu.db.ScoreModeDbManager;
import org.junit.Test;

public class ScoreModeDbManagerTest {

  ScoreModeDbManager scoreModeDbManager = ScoreModeDbManager.getInstance();

  @Test
  public void getScoreModeIdByDescTest() {
    int id = scoreModeDbManager.getScoreModeIdByDesc("yesNoMode");
    System.out.println(id);
  }

  @Test
  public void getScoreModeDescByIdTest() {
    ScoreModeEnum scoreModeEnum = scoreModeDbManager.getScoreModeDescById(2);
    System.out.println(scoreModeEnum);
  }
}
