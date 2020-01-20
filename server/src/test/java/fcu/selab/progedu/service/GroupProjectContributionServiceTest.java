package fcu.selab.progedu.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.data.Committer;
import fcu.selab.progedu.db.service.ProjectDbService;
import fcu.selab.progedu.status.StatusEnum;

public class GroupProjectContributionServiceTest {
  GroupCommitRecordService gcrs = GroupCommitRecordService.getInstance();
  ProjectDbService pcrdb = ProjectDbService.getInstance();
  String groupName = "group5";
  String projectName = "project5";

  @Test
  public void m1() {
    List<CommitRecord> commitRecords = pcrdb.getCommitRecords(groupName, projectName);
    List<Committer> committers = new ArrayList<>();
    CommitRecord preCommitRecord = null;
    for (CommitRecord cr : commitRecords) {
      String committerName = cr.getCommitter();
      Committer committer = getCommitter(committers, committerName);
      if (preCommitRecord == null) {
//        committer.addStatus(status);
      } else {

      }

    }

  }

  private Committer getCommitter(List<Committer> committers, String committerName) {
    for (Committer committer : committers) {
      if (committerName.equals(committer.getCommitter())) {
        return committer;
      }
    }
    return new Committer("committerName");
  }

  private String checkStatus(CommitRecord previous, CommitRecord current) {
    StatusEnum currentStatus = current.getStatus();
    if (previous == null) {

    } else {
      switch (previous.getStatus()) {
        case INITIALIZATION:
          switch (currentStatus) {
            case WEB_HTMLHINT_FAILURE:
            case WEB_STYLELINT_FAILURE:
            case WEB_ESLINT_FAILURE:
              return "failed";
            case BUILD_SUCCESS:
              return "success";
          }
        case WEB_HTMLHINT_FAILURE:
          switch (currentStatus) {
            case WEB_HTMLHINT_FAILURE:
              return "keep";
            case WEB_STYLELINT_FAILURE:
            case WEB_ESLINT_FAILURE:
              return "failed";
            case BUILD_SUCCESS:
              return "fixed";
          }
        case WEB_STYLELINT_FAILURE:
        case WEB_ESLINT_FAILURE:
        case BUILD_SUCCESS:

        default:
          break;
      }
    }

  }
}
