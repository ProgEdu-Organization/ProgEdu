package fcu.selab.progedu.config;

import fcu.selab.progedu.exception.LoadConfigFailureException;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class CourseConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseConfigTest.class);

    @Test
    public void getCourseName() {
        CourseConfig courseConfig = CourseConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "COURSE_NAME", courseConfig.getCourseName(), false);
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getSchoolEmail() {
        CourseConfig courseConfig = CourseConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "COURSE_SCHOOL_EMAIL", courseConfig.getSchoolEmail(), false);
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getCourseFullName() {
        CourseConfig courseConfig = CourseConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "COURSE_FULL_NAME", courseConfig.getCourseFullName(), false);
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void getTomcatServerIp() {
        CourseConfig courseConfig = CourseConfig.getInstance();

        try {
            ConfigTestExample.testConfigHasValue( "COURSE_TOMCAT_SERVER_IP", courseConfig.getTomcatServerIp());
        } catch (LoadConfigFailureException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
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