package fcu.selab.progedu.jenkinsconfig;

import org.junit.Test;

import static org.junit.Assert.*;

public class MavenDindConfigTest {

    @Test
    public void readPipeline() {
        MavenDindConfig mavenDindConfig = new MavenDindConfig("test");
        mavenDindConfig.readPipeline();
    }
}