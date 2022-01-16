package fcu.selab.progedu.status;

public class PythonCompileFailure implements Status {

   private static final Logger LOGGER = LoggerFactory.getLogger(PythonCompileFailure.class);


   @Override
   public String extractFailureMsg(String consoleText) {
     return null;
   }

   @Override
   public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
     return null;
   }
}
