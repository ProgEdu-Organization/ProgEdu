package fcu.selab.progedu.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

public class AssignmentServiceTest {

  public static void main(String[] args) throws FileNotFoundException {

    AssignmentService as = new AssignmentService();
//    as.createAssignment(assignmentName, releaseTime, deadline, readMe, assignmentType, file,
//        fileDetail);
    String assignmentName = "82904";
//    as.getProject(assignmentName);
    String readMe = "test";
    Date releaseTime = new Date();
    Date deadline = new Date();
    File file = new File("C:\\Users\\hoky\\Documents\\ProgEduFile\\MvnQuickStart.zip");
    InputStream is = new FileInputStream(file);
    FormDataContentDisposition fd = FormDataContentDisposition.name("MvnQuickStart.zip")
        .fileName("MvnQuickStart.zip").creationDate(releaseTime).modificationDate(releaseTime)
        .readDate(releaseTime).size(1222).build();
    as.editProject(assignmentName, releaseTime, deadline, readMe, is, fd);

  }

}
