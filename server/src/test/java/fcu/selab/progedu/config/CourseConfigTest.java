package fcu.selab.progedu.config;

import fcu.selab.progedu.exception.LoadConfigFailureException;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourseConfigTest {

    @Test
    public void getCourseName() {
        CourseConfig courseConfig = CourseConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "COURSE_NAME", courseConfig.getCourseName(), false);
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getSchoolEmail() {
        CourseConfig courseConfig = CourseConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "COURSE_SCHOOL_EMAIL", courseConfig.getSchoolEmail(), false);
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCourseFullName() {
        CourseConfig courseConfig = CourseConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "COURSE_FULL_NAME", courseConfig.getCourseFullName(), false);
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTomcatServerIp() {
        CourseConfig courseConfig = CourseConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "COURSE_TOMCAT_SERVER_IP", courseConfig.getTomcatServerIp());
        } catch (LoadConfigFailureException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCourseKey() {
        System.out.println("Course Key hasn't been used now, perhaps it can be removed in future.");
    }

    @Test
    public void getBaseuri() {
        CourseConfig courseConfig = CourseConfig.getInstance();
        assertEquals(courseConfig.getBaseuri(), "");
    }
}