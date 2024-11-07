import java.lang.annotation.*;
import java.lang.reflect.*;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
                    testResult = e.getCause();
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
        System.out.println("Started function");
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

        System.out.println("Loaded class");

        Method[] testMethods = testClassFramework.getMethods();
        Arrays.sort(testMethods, Comparator.comparing(Method::getName));

        for (Method testMethod : testMethods) {
            if (testMethod.isAnnotationPresent(Property.class)) {
                Type[] inputTypes = testMethod.getGenericParameterTypes();
                Annotation[][] inputAnnotations = testMethod.getParameterAnnotations();

                System.out.println("got parameters");

                for (int i = 0; i < inputTypes.length; i++) {
                    if (!checkValidParameters(inputTypes[i], inputAnnotations[i], i)) {
                        System.out.println("Property " + testMethod.getName() + " has an invalid parameter.");
                        /* throw new InvalidParameterException(); */
                    }
                }

                System.out.println("checked annotations");

                List<List<Object>> argumentPossibilities = generateArgumentPossibilities(testMethod, testClass);
                for (List<Object> listArgs : argumentPossibilities) {
                    Object[] args = listArgs.toArray();
                    try {
                        boolean result = (boolean) testMethod.invoke(testClass, args);
                        if (!result) {
                            results.put(testMethod.getName(), args);
                            break;
                        }
                    } catch (Exception e) {
                        results.put(testMethod.getName(), args);
                        break;
                    }
                }
                results.putIfAbsent(testMethod.getName(), null);
            }
        }

        return results;
    }

    private static boolean checkValidParameters(Type type, Annotation[] annotations, int numAnnotation) {
        System.out.println("poop");
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
            } return true;
        }

        return false;
    }

    private static List<List<Object>> generateArgumentPossibilities(Method method, Object instance) {
        List<List<Object>> argsPerParameter = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            List<Object> args = new ArrayList<>();

            if (parameter.isAnnotationPresent(IntRange.class)) {
                IntRange range = parameter.getAnnotation(IntRange.class);
                for (int i = range.min(); i <= range.max(); i++) {
                    args.add(i);
                }
            } else if (parameter.isAnnotationPresent(StringSet.class)) {
                StringSet set = parameter.getAnnotation(StringSet.class);
                args.addAll(Arrays.asList(set.strings()));
            } else if (parameter.isAnnotationPresent(ListLength.class)) {
                ListLength listLength = parameter.getAnnotation(ListLength.class);
                AnnotatedType[] annotations = ((AnnotatedParameterizedType) parameter.getAnnotatedType()).getAnnotatedActualTypeArguments();
                AnnotatedType annotationSingle = annotations[0];
                Annotation annotationType = annotationSingle.getAnnotations()[0];

                for (int i = listLength.min(); i <= listLength.max(); i++) {
                    if (annotationType instanceof IntRange) {
                        IntRange range = annotationSingle.getAnnotation(IntRange.class);
                        List<Object> intList = IntStream.rangeClosed(range.min(), range.max())
                                                        .boxed()
                                                        .collect(Collectors.toList());
                        args.addAll(createAllListsOfSize(intList, i));
                    } else if (annotationType instanceof StringSet) {
                        StringSet set = annotationSingle.getAnnotation(StringSet.class);
                        List<Object> stringList = Arrays.asList(set.strings());
                        args.addAll(createAllListsOfSize(stringList, i));
                    } else if (annotationType instanceof ListLength) {
                        AnnotatedParameterizedType parameterizedType = (AnnotatedParameterizedType) annotationSingle;
                        Annotation nestedListType = parameterizedType.getAnnotatedActualTypeArguments()[0].getAnnotations()[0];
                        List<List<Object>> listOfLists = new ArrayList<>();

                        if (nestedListType instanceof IntRange) {
                            IntRange innerRange = (IntRange) nestedListType;
                            for (int innerValue = innerRange.min(); innerValue <= innerRange.max(); innerValue++) {
                                listOfLists.add(Collections.singletonList(innerValue));
                            }
                        } else if (nestedListType instanceof StringSet) {
                            StringSet innerSet = (StringSet) nestedListType;
                            for (String str : innerSet.strings()) {
                                listOfLists.add(Collections.singletonList(str));
                            }
                        } else {
                            throw new IllegalArgumentException("Unsupported list element type annotation");
                        }
                        
                        ListLength listLengthAnnotation = (ListLength) annotationType;
                        List<List<Object>> allCombinations = createNestedListsOfSize(listOfLists, listLengthAnnotation.max());
                        args.addAll(allCombinations);
                    } else {
                        throw new IllegalArgumentException("Unsupported list element type");
                    }

                }
            } else if (parameter.isAnnotationPresent(ForAll.class)) {
                ForAll forAll = parameter.getAnnotation(ForAll.class);
                try {
                    Method generatorMethod = method.getDeclaringClass().getMethod(forAll.name());
                    for (int i = 0; i < forAll.times(); i++) {
                        args.add(generatorMethod.invoke(instance));
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error invoking generator method for @ForAll", e);
                }
            }

            argsPerParameter.add(args);
        }

        List<List<Object>> permutations = cartesianProduct(argsPerParameter);
    
        return permutations.stream().limit(100).collect(Collectors.toList());
    }

    private static List<List<Object>> createAllListsOfSize(List<Object> elements, int size) {
        List<List<Object>> allLists = new ArrayList<>();
    
        if (size == 0) {
            allLists.add(new ArrayList<>());
            return allLists;
        }
    
        if (elements.isEmpty() || size < 0) {
            return allLists;
        }
    
        generatePermutations(allLists, elements, size, new ArrayList<>());
    
        return allLists;
    }
    
    private static void generatePermutations(List<List<Object>> allLists, List<Object> elements, int size, List<Object> current) {
        if (current.size() == size) {
            allLists.add(new ArrayList<>(current));
            return;
        }
    
        for (Object element : elements) {
            current.add(element);
            generatePermutations(allLists, elements, size, current);
            current.remove(current.size() - 1);
        }
    }

    private static List<List<Object>> createNestedListsOfSize(List<List<Object>> listOfLists, int outerSize) {
        List<List<Object>> allNestedLists = new ArrayList<>();
    
        if (outerSize == 0) {
            allNestedLists.add(new ArrayList<>());
            return allNestedLists;
        }
    
        if (listOfLists.isEmpty() || outerSize < 0) {
            return allNestedLists;
        }
    
        generatePermutationsOfLists(allNestedLists, new ArrayList<>(), listOfLists, outerSize);
    
        return allNestedLists;
    }
    
    private static void generatePermutationsOfLists(List<List<Object>> allNestedLists, List<List<Object>> currentNestedList, List<List<Object>> listOfLists, int outerSize) {
        if (currentNestedList.size() == outerSize) {
            allNestedLists.add(new ArrayList<>(currentNestedList));
            return;
        }
    
        for (List<Object> innerList : listOfLists) {
            currentNestedList.add(innerList);
            generatePermutationsOfLists(allNestedLists, currentNestedList, listOfLists, outerSize);
            currentNestedList.remove(currentNestedList.size() - 1);
        }
    }
    
    private static List<List<Object>> cartesianProduct(List<List<Object>> lists) {
        List<List<Object>> resultLists = new ArrayList<>();
        if (lists.isEmpty()) {
            resultLists.add(new ArrayList<>());
            return resultLists;
        } else {
            List<Object> firstList = lists.get(0);
            List<List<Object>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
            for (Object condition : firstList) {
                for (List<Object> remainingList : remainingLists) {
                    Object[] resultList = new Object[remainingList.size() + 1];
                    resultList[0] = condition;
                    System.arraycopy(remainingList.toArray(), 0, resultList, 1, remainingList.size());
                    resultLists.add(Arrays.asList(resultList));
                }
            }
        }
        return resultLists;
    }
}