package fcu.selab.progedu.conn;

import org.gitlab.api.models.GitlabProject;
import org.junit.Test;

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
        System.out.print("targetPath: ");
        System.out.println(targetPath);

    }
}