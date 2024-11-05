import java.lang.reflect.*;
import java.util.*;
import java.lang.annotation.*;

public class Unit {
    public static Map<String, Throwable> testClass(String name) {
        Map<String, Throwable> results = new HashMap<>();

        Class<?> testClassFramework = null;
        Object testClass = null;
        try {
            testClassFramework = Class.forName(name);
            testClass = testClassFramework.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            System.out.println("Class matching name not found");
            e.printStackTrace();
            throw new RuntimeException();,
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        if (testClassFramework == null || testClass == null) {
            System.out.println("No methods to display as the class could not be loaded.");
            throw new NullPointerException();
        }

        Method[] testMethods = testClassFramework.getMethods();
        Arrays.sort(testMethods, Comparator.comparing(Method::getName));
        ArrayList<Method> beforeMethods = new ArrayList<>();
        ArrayList<Method> afterMethods = new ArrayList<>();

        // Perform all @BeforeClass methods
        // Check for methods with multiple annotations
        // Add all @Before and @After methods to storage lists
        for (Method testMethod : testMethods) {
            Annotation[] testMethodAnnotations = testMethod.getAnnotations();
            if (testMethodAnnotations.length > 1) {
                System.out.println("Method " + testMethod.getName() + " cannot have multiple annotations.");
                throw new RuntimeException();
            }

            if (testMethod.isAnnotationPresent(Before.class)) {
                beforeMethods.add(testMethod);
            } else if (testMethod.isAnnotationPresent(After.class)) {
                afterMethods.add(testMethod);
            } else if (testMethod.isAnnotationPresent(BeforeClass.class)) {
                if (!Modifier.isStatic(testMethod.getModifiers())) {
                    System.out.println("Method " + testMethod.getName() + " is @BeforeClass but is not static.");
                    throw new RuntimeException();
                }
                try {
                    testMethod.invoke(testClass);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }
        }
        
        // Perform all @Test methods, as well as @Before and @After methods
        for (Method testMethod : testMethods) {
            if (testMethod.isAnnotationPresent(Test.class)) {
                for (Method beforeMethod : beforeMethods) {
                    try {
                        beforeMethod.invoke(testClass);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException();
                    }
                }

                Throwable testResult = null;
                try {
                    testMethod.invoke(testClass);
                } catch (Exception e) {
                    testResult = e;
                }
                
                results.put(testMethod.getName(), testResult);

                for (Method afterMethod : afterMethods) {
                    try {
                        afterMethod.invoke(testClass);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException();
                    }
                }
            }
        }
        
        // Perform all @AfterClass methods
        for (Method testMethod : testMethods) {
            if (testMethod.isAnnotationPresent(AfterClass.class)) {
                if (!Modifier.isStatic(testMethod.getModifiers())) {
                    System.out.println("Method " + testMethod.getName() + " is @AfterClass but is not static.");
                    throw new RuntimeException();
                }
                try {
                    testMethod.invoke(testClass);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }
        }

        return results;
    }

    public static Map<String, Object[]> quickCheckClass(String name) {
	throw new UnsupportedOperationException();
    }
}