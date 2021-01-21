package fcu.selab.progedu.conn;

import org.junit.Test;

public class JenkinsServiceTest {

    JenkinsService jenkinsService = JenkinsService.getInstance();

    @Test
    public void getConsole() {
        String out = jenkinsService.getConsole("sss2_hhh11", 1);
        System.out.printf(out);
    }

    @Test
    public void getConsoleUrl() {
        String out = jenkinsService.getConsoleUrl("sss2_hhh11", 1);
        System.out.printf(out);
    }
}