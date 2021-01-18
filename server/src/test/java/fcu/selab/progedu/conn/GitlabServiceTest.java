package fcu.selab.progedu.conn;

import org.gitlab.api.models.GitlabProject;
import org.junit.Test;

import static org.junit.Assert.*;

public class GitlabServiceTest {

    @Test
    public void createRootProject() {
        GitlabService gitlabService = GitlabService.getInstance();

        String projectName = "unit-test-for-create-root-project";
        GitlabProject gitlabProject = gitlabService.createRootProject(projectName);

        assertNotNull(gitlabProject);

        // after test delete this project
        assertTrue( gitlabService.deleteRootProject(projectName) );
    }
}