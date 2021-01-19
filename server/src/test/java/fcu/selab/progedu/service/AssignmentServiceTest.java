package fcu.selab.progedu.service;

import org.junit.Test;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.*;

public class AssignmentServiceTest {

    @Test
    public void createAssignment() {
        AssignmentService assignmentService = AssignmentService.getInstance();
        Date currentDate = new Date();
        try {
            InputStream f = this.getClass().getResourceAsStream("/MvnQuickStart.zip");

            final FormDataContentDisposition dispo = FormDataContentDisposition
                    .name("file")
                    .fileName("MvnQuickStart.zip")
                    .size(f.available())
                    .build();

            assignmentService.createAssignment("unit-test-for-createAssigment", currentDate, currentDate, "", "maven", f, dispo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}