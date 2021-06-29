package fcu.selab.progedu.conn;

import org.gitlab.api.models.GitlabProject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GitlabServiceTest {

  GitlabService gitlabService = GitlabService.getInstance();

  @Test
  void getAllProjects() {

    List<GitlabProject> gitlabProjectList = gitlabService.getAllProjects();
    for (GitlabProject gitlabProject: gitlabProjectList) {
      System.out.println(gitlabProject.getName());
    }
  }
}