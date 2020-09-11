package fcu.selab.progedu.config;

import static org.junit.Assert.assertFalse;

public class ConfigTestExample {
    public static void testConfigHasValue(String explanation, String configStr) {
        testConfigHasValue(explanation, configStr, true);
    }

    public static void testConfigHasValue(String explanation, String configStr, boolean isNecessary) {
        if( isNecessary ) {
            System.out.println(explanation + ": " + configStr);
            assertFalse(configStr.isEmpty());

        } else {
            if( configStr.isEmpty() ) {
                System.out.println("The \"" + explanation + "\" config value is empty but doesn't matter");
            } else {
                System.out.println(explanation + ": " + configStr);
            }
        }
    }


}
