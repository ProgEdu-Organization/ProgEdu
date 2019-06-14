package fcu.selab.progedu.status;

import fcu.selab.progedu.service.AssignmentTypeEnum;

public class StatusFactorySelector {
  /**
   * getStatusFactory
   * 
   * @param projectType project type
   */
  public static StatusFactory getStatusFactory(AssignmentTypeEnum projectType) {
    switch (projectType) {
      case JAVAC: {
        return new JavacStatusFactory();
      }
      case MAVEN: {
        return new MavenStatusFactory();
      }
      case WEB: {
        return new WebStatusFactory();
      }
      case APP: {
        return null;
      }
      default: {
        return null;
      }
    }
  }
}
