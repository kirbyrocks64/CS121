import java.lang.annotation.*;
import java.lang.reflect.*;
import java.security.InvalidParameterException;
import java.util.*;

public class Unit {
    public static Map<String, Throwable> testClass(String name) {
        Map<String, Throwable> results = new HashMap<>();

        Class<?> testClassFramework = null;
        Object testClass = null;
        try {
            testClassFramework = Class.forName(name);
            testClass = testClassFramework.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            System.out.println("Class matching name not found.");
            e.printStackTrace();
            throw new RuntimeException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        if (testClassFramework == null || testClass == null) {
            System.out.println("Class could not be loaded.");
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
	    Map<String, Object[]> results = new HashMap<>();

        Class<?> testClassFramework = null;
        Object testClass = null;
        try {
            testClassFramework = Class.forName(name);
            testClass = testClassFramework.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            System.out.println("Class matching name not found.");
            e.printStackTrace();
            throw new RuntimeException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        if (testClassFramework == null || testClass == null) {
            System.out.println("Class could not be loaded.");
            throw new NullPointerException();
        }

        Method[] testMethods = testClassFramework.getMethods();
        Arrays.sort(testMethods, Comparator.comparing(Method::getName));

        for (Method testMethod : testMethods) {
            if (testMethod.isAnnotationPresent(Property.class)) {
                Object[] testResult = null;
                Type[] inputTypes = testMethod.getGenericParameterTypes();
                Annotation[][] inputAnnotations = testMethod.getParameterAnnotations();

                for (int i = 0; i < inputTypes.length; i++) {
                    if (!checkValidParameters(inputTypes[i], inputAnnotations[i], i)) {
                        System.out.println("Property " + testMethod.getName() + " has an invalid parameter.");
                    }
                }

                Parameter[] parameters = testMethod.getParameters();
                List<List<Object>> argumentCombinations = new ArrayList<>();
                for (int i = 0; i < parameters.length; i++) {
                    generateArgumentCombinations(parameters[i], argumentCombinations.get(i));
                }

                /* List<List<Object>> argumentCombinations = generateArgumentCombinations(testMethod, testClass); */

                for (List<Object> args : argumentCombinations) {
                    Object[] argsArray = args.toArray();
                    try {
                        boolean result = (boolean) testMethod.invoke(testClass, argsArray);
                        if (!result) {
                            results.put(testMethod.getName(), argsArray);
                        }
                    } catch (Throwable t) {
                        results.put(testMethod.getName(), argsArray);
                    }
                }
                
                results.put(testMethod.getName(), testResult);
            }
        }

        return results;
    }

    private static boolean checkValidParameters(Type type, Annotation[] annotations, int numAnnotation) {
        if (type.equals(Integer.class)) {
            if (annotations.length != 1) {
                System.out.println("Integer argument has incorrect number of annotations");
                throw new RuntimeException();
            } else if (!annotations[numAnnotation].annotationType().equals(IntRange.class)) {
                System.out.println("Integer parameter can only have @IntRage annotation.");
                throw new InvalidParameterException();
            } return true;
        } else if (type.equals(String.class)) {
            if (annotations.length != 1) {
                System.out.println("String argument has incorrect number of annotations");
                throw new RuntimeException();
            } else if (!annotations[numAnnotation].annotationType().equals(StringSet.class)) {
                System.out.println("Integer parameter can only have @StringSet annotation.");
                throw new InvalidParameterException();
            } return true;
        } else if (type.equals(List.class)) {
            if (!annotations[numAnnotation].annotationType().equals(ListLength.class)) {
                System.out.println("List parameter can only have @ListLength annotation.");
                throw new InvalidParameterException();
            }

            ParameterizedType listType = (ParameterizedType) type.getClass().getGenericSuperclass();
            Type componentType = listType.getActualTypeArguments()[0];
            return checkValidParameters(componentType, annotations, numAnnotation + 1);
        } else if (type.equals(Object.class)) {
            if (!annotations[numAnnotation].annotationType().equals(ForAll.class)) {
                System.out.println("Object parameter can only have @ForAll annotation.");
                throw new InvalidParameterException();
            }
        }

        return false;
    }

    private static List<List<Object>> generateArgumentCombinations(Parameter parameter, List<Object> inputsList) {
        Annotation[] annotations = parameter.getAnnotations();
        Annotation annotation = annotations[0];

        if (annotation instanceof IntRange) {
            IntRange range = (IntRange) annotation;
            for (int i = range.min(); i <= range.max(); i++) {
                inputsList.add(i);
            }
        } else if (annotation instanceof StringSet) {
            StringSet set = (StringSet) annotation;
            inputsList.addAll(Arrays.asList(set.strings()));
        } else if (annotation instanceof ListLength) {
            ListLength length = (ListLength) annotation;
            inputsList = generateListCombinations(parameter, length.min(), length.max());
        } else if (annotation instanceof ForAll) {
            ForAll forAll = (ForAll) annotation;
            inputsList = generateForAllValues(instance, forAll);
        }
        
        allCombinations.add(values);
        
        return cartesianProduct(allCombinations);
    }

    private static List<Object> generateListCombinations(Parameter parameter, int min, int max) {
        List<Object> lists = new ArrayList<>();
        
        return lists;
    }
}