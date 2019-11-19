package fcu.selab.progedu.status;

import java.io.IOException;
import java.util.ArrayList;

import fcu.selab.progedu.data.FeedBack;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class MavenUnitTestFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String unitTest = "";
    String startStr = "Failed tests:";
    String goal = "Tests run:";
    int goalStr = consoleText.indexOf(goal, consoleText.indexOf(goal) + 1);

    unitTest = consoleText.substring(consoleText.indexOf(startStr), goalStr - 1);
    //<, > will be HTML tag, change to the " 
    unitTest = unitTest.replaceAll("<", "\"").replaceAll(">", "\"");
    
    return unitTest.trim();
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    consoleText = consoleText + "\n";
    int endIndex = consoleText.length();
    ArrayList<FeedBack> feedbacklist = new ArrayList<>();
    while (consoleText.indexOf("Failed tests:") != -1) {
      int nextrow = consoleText.indexOf("\n");
      int nextfailedtest = consoleText.indexOf("Failed tests:");
      if (nextfailedtest > nextrow) {
        consoleText = consoleText.substring(nextrow + 1, endIndex);
        endIndex = endIndex - nextrow - 1;
      } else {
        int nextcolon = consoleText.indexOf(":", 13);
        feedbacklist.add(new FeedBack(
            StatusEnum.UNIT_TEST_FAILURE,
            "",
            consoleText.substring(nextcolon + 1, nextrow).trim(),
            "",
            ""
        ));
        consoleText = consoleText.substring(nextrow + 1, endIndex);
        endIndex = endIndex - nextrow - 1;
      }
    }
    return feedbacklist;
  }

  @Override
  public String toJson(ArrayList<FeedBack> arrayList) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writerWithDefaultPrettyPrinter()
          .writeValueAsString(arrayList);
      return jsonString;
    } catch (JsonGenerationException e) {
      e.printStackTrace();
      return "JsonGenerationException Error";
    } catch (JsonMappingException e) {
      e.printStackTrace();
      return "JsonMappingException Error";
    } catch (IOException e) {
      e.printStackTrace();
      return "IOException Error";
    }
  }

  public String console() {
    String test = "-------------------------------------------------------\n"
        + "Running selab.myapp.AppTest\n"
        + "FK\n"
        + "Tests run: 1, Failures: 1, Errors: 0, Skipped: 0, "
        + "Time elapsed: 0.051 sec <<< FAILURE!\n"
        + "test(selab.myapp.AppTest)  Time elapsed: 0.007 sec  <<< FAILURE!\n"
        + "org.junit.ComparisonFailure: expected:<[Hello World]!> but was:<[Fuck U]!>\n"
        + "\tat org.junit.Assert.assertEquals(Assert.java:115)\n"
        + "\tat org.junit.Assert.assertEquals(Assert.java:144)\n"
        + "\tat selab.myapp.AppTest.test(AppTest.java:21)\n"
        + "\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n"
        + "\tat sun.reflect.NativeMethodAccessorImpl.invoke(Na"
        + "tiveMethodAccessorImpl.java:62)\n"
        + "\tat sun.reflect.DelegatingMethodAccessorImpl.invok"
        + "e(DelegatingMethodAccessorImpl.java:43)\n"
        + "\tat java.lang.reflect.Method.invoke(Method.java:498)\n"
        + "\tat org.junit.runners.model.FrameworkMethod$1.runR"
        + "eflectiveCall(FrameworkMethod.java:50)\n"
        + "\tat org.junit.internal.runners.model.ReflectiveCal"
        + "lable.run(ReflectiveCallable.java:12)\n"
        + "\tat org.junit.runners.model.FrameworkMethod.invokeE"
        + "xplosively(FrameworkMethod.java:47)\n"
        + "\tat org.junit.internal.runners.statements.InvokeMeth"
        + "od.evaluate(InvokeMethod.java:17)\n"
        + "\tat org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)\n"
        + "\tat org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)\n"
        + "\tat org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)\n"
        + "\tat org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)\n"
        + "\tat org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)\n"
        + "\tat org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)\n"
        + "\tat org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)\n"
        + "\tat org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)\n"
        + "\tat org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)\n"
        + "\tat org.junit.runners.ParentRunner.run(ParentRunner.java:363)\n"
        + "\tat org.apache.maven.surefire.junit4.JUnit4Provider.execute(JUnit4Provider.java:252)\n"
        + "\tat org.apache.maven.surefire.junit4.JUnit4Provider.exe"
        + "cuteTestSet(JUnit4Provider.java:141)\n"
        + "\tat org.apache.maven.surefire.junit4.JUnit4Provider.invoke(JUnit4Provider.java:112)\n"
        + "\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n"
        + "\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n"
        + "\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(Delegating"
        + "MethodAccessorImpl.java:43)\n"
        + "\tat java.lang.reflect.Method.invoke(Method.java:498)\n"
        + "\tat org.apache.maven.surefire.util.ReflectionUtils.invoke"
        + "MethodWithArray(ReflectionUtils.java:189)\n"
        + "\tat org.apache.maven.surefire.booter.ProviderFactory$Prov"
        + "iderProxy.invoke(ProviderFactory.java:165)\n"
        + "\tat org.apache.maven.surefire.booter.ProviderFactory.invo"
        + "keProvider(ProviderFactory.java:85)\n"
        + "\tat org.apache.maven.surefire.booter.ForkedBooter.runSuites"
        + "InProcess(ForkedBooter.java:115)\n"
        + "\tat org.apache.maven.surefire.booter.ForkedBooter.main(For"
        + "kedBooter.java:75)\n"
        + "\n"
        + "\n"
        + "Results :\n"
        + "\n"
        + "Failed tests:   test(selab.myapp.AppTest): expected:<[Hello Wo"
        + "rld]!> but was:<[Fuck U]!>\n"
        + "\n"
        + "Tests run: 1, Failures: 1, Errors: 0, Skipped: 0\n"
        + "\n"
        + "[INFO] -----------------------------------------------------------";
    return test;
  }

}