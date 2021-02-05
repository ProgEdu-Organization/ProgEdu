package fcu.selab.progedu.conn;

import fcu.selab.progedu.utils.JavaIoUtile;
import org.gitlab.api.models.GitlabProject;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class GitlabServiceTest {

    GitlabService gitlabService = GitlabService.getInstance();

    @Test
    public void createRootProject() {
        String projectName = "unit-test-for-create-root-project";
        GitlabProject gitlabProject = gitlabService.createRootProject(projectName);

        assertNotNull(gitlabProject);

        // after test delete this project
        assertTrue( gitlabService.deleteRootProject(projectName) );
    }

    @Test
    public void cloneProject() {
        String projectName = "unit-test-for-create-root-project";
        gitlabService.createRootProject(projectName);
        String targetPath = gitlabService.cloneProject("root", projectName);

        assertNotEquals(targetPath, "");

        if (targetPath != "") {
            assertTrue( JavaIoUtile.deleteDirectory(Paths.get(targetPath).toFile()) );
        } else {
            fail();
        }


    }

    @Test
    public void cloneProjectToTargetPath() {
        String projectName = "unit-test-for-create-root-project";
        gitlabService.createRootProject(projectName);

        String targetParentPathString = System.getProperty("java.io.tmpdir") + "/" +  "unit-test-for-ProgEdu-cloneProjectToTargetPath";
        String targetPathString = targetParentPathString + "/" + projectName;

        Path targetPath = Paths.get(targetPathString);

        assertTrue( gitlabService.cloneProject("root", projectName, targetPath) );

        // after test delete this project
        assertTrue( gitlabService.deleteRootProject(projectName) );

        assertTrue( JavaIoUtile.deleteDirectory(Paths.get(targetParentPathString).toFile()) );

    }

  @Test
  public void getAllProjects() {

      System.out.println(gitlabService.getAllProjects());
  }
}