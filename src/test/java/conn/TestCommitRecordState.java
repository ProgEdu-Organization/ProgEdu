package conn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.AssignmentDbManager;

public class TestCommitRecordState {

  static CommitRecordDbManager commitRecordDb = CommitRecordDbManager.getInstance();
  static CommitRecordStateDbManager commitRecordStateDb = CommitRecordStateDbManager.getInstance();
  static AssignmentDbManager projectDb = AssignmentDbManager.getInstance();

  public static void main(String[] args) {
    List<String> lsNames = new ArrayList<>();
    lsNames = projectDb.listAllAssignmentNames();

    for (String name : lsNames) {

      Map<String, Integer> map = new HashMap<>();

      map = commitRecordDb.getCommitRecordStateCounts(name);

      int success = 0;
      int nb = 0;
      int ctf = 0;
      int csf = 0;
      int cpf = 0;
      int ccs = 0;

      if (map.containsKey("S")) {
        success = map.get("S");
      }

      if (map.containsKey("NB")) {
        nb = map.get("NB");
      }

      if (map.containsKey("CTF")) {
        ctf = map.get("CTF");
      }

      if (map.containsKey("CSF")) {
        csf = map.get("CSF");
      }

      if (map.containsKey("CPF")) {
        cpf = map.get("CPF");
      }

      ccs = success + ctf + csf + cpf;

      commitRecordStateDb.addCommitRecordState(name, success, csf, cpf, ctf, nb, ccs);

    }

  }

}
