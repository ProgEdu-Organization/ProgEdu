package fcu.selab.progedu.service;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.junit.Test;

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
            System.out.println(f.available());

            final FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
            final FormDataContentDisposition dispo = FormDataContentDisposition
                    .name("file")
                    .fileName("MvnQuickStart.zip")
                    .size(f.available())
                    .build();

//            FormDataContentDisposition formDataContentDisposition = new FormDataContentDisposition("");
            assignmentService.createAssignment("franky-test", currentDate, currentDate, "", "maven", f, dispo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}