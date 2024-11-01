import java.lang.reflect.Method;
import java.util.*;
import java.lang.annotation.*;

public class Unit {
    Annotation[] annotations = {BeforeClass, AfterClass};
    public static Map<String, Throwable> testClass(String name) {
        Class<?> testClassFramework = null;
        Object testClass = null;
        try {
            testClassFramework = Class.forName(name);
            testClass = testClassFramework.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            System.out.println("Class matching name not found");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (testClassFramework == null || testClass == null) {
            System.out.println("No methods to display as the class could not be loaded.");
            throw new NullPointerException();
        }

        Method[] testMethods = testClassFramework.getMethods();
        Arrays.sort(testMethods, Comparator.comparing(Method::getName));

        // Perform all @BBeforeClass methods & check for methods with multiple annotations
        for (Method testMethod : testMethods) {
            Annotation[] testMethodAnnotations = testMethod.getAnnotations();
            if (testMethod.isAnnotationPresent(BeforeClass.class)) {
                try {
                    testMethod.invoke(testClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        return null;
    }

    public static Map<String, Object[]> quickCheckClass(String name) {
	throw new UnsupportedOperationException();
    }
}