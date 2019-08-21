package fcu.selab.progedu.project;

public abstract class AssignmentType extends ProjectTypeImp {

  public abstract void createTemplate(String uploadDirectory);

  public abstract void createTestCase(String testDirectory);

}
