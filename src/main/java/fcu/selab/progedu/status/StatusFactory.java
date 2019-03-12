package fcu.selab.progedu.status;

public class StatusFactory {
  /**
   * 
   * @param type is the status type
   * @param assignmentType is the  projectType
   * @return Status status
   */
  public static Status getStatus(String type, String assignmentType) {
    StatusEnum statusEnum = StatusEnum.getStatusEnum(type);
    StatusAssigmentTypeEnum statusProjectType = StatusAssigmentTypeEnum
        .getStatusProjectTypeEnum(assignmentType);

    switch (statusProjectType) {
      case WEB: {
        switch (statusEnum) {
          case INITIALIZATION: {
            return new Initialization();
          }
          case CHECKSTYLE_FAILURE: {
            return new WebCheckstyleFailure();
          }
          case UNIT_TEST_FAILURE: {
            return new WebUnitTestFailure();
          }
          case BUILD_SUCCESS: {
            return new BuildSuccess();
          }
  
          default: {
            return null;
          }
        }
      }
      default: {
        switch (statusEnum) {
          case INITIALIZATION: {
            return new Initialization();
          }
          case COMPILE_FAILURE: {
            return new CompileFailure();
          }
          case CHECKSTYLE_FAILURE: {
            return new CheckstyleFailure();
          }
          case UNIT_TEST_FAILURE: {
            return new UnitTestFailure();
          }
          case BUILD_SUCCESS: {
            return new BuildSuccess();
          }
  
          default: {
            return null;
          }
        }
      }
    }
  }
}


