package fcu.selab.progedu.project;

import fcu.selab.progedu.data.ZipFileInfo;

public abstract class AssignmentType extends ProjectTypeImp {

  public abstract void createTemplate(String uploadDirectory);

  public abstract ZipFileInfo createTestCase(String testDirectory);

}
