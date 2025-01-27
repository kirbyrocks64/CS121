import java.util.*;

public class RunTests {
    public static void main(String[] args) {
        testClassMethod();
        testObjectAssert();
        testStringAssert();
        testBoolAssert();
        testIntAssert();
    }

    private static void testClassMethod() {
        Map<String, Throwable> hold = Unit.testClass("TestClassMethodTests");
        for (String s : hold.keySet()) {
            System.out.println(s);
            System.out.println(hold.get(s));
        }
    }

    private static void testObjectAssert() {
        TestAssert testAssert1 = new TestAssert();
        TestAssert testAssert2 = new TestAssert();
        TestAssert testAssert3 = testAssert1;
        Assertion.assertThat(testAssert1).isNotNull();
        Assertion.assertThat(testAssert1).isEqualTo(testAssert3);
        Assertion.assertThat(testAssert1).isNotEqualTo(testAssert2);
        Assertion.assertThat(testAssert1).isInstanceOf(TestAssert.class);

        System.out.println("Object assertions passed");
    }

    private static void testStringAssert() {
        String testString = null;
        Assertion.assertThat("boop").isNotNull();
        Assertion.assertThat(testString).isNull();
        Assertion.assertThat("boop").isEqualTo("boop");
        Assertion.assertThat("boop").isNotEqualTo("broop");
        Assertion.assertThat("boop").startsWith("boo");
        Assertion.assertThat("antidisestablishmentarianism").contains("establishment");

        System.out.println("String assertions passed");
    }

    private static void testBoolAssert() {
        Assertion.assertThat(true).isTrue();
        Assertion.assertThat(false).isFalse();
        Assertion.assertThat(true).isEqualTo(true);

        System.out.println("Boolean assertions passed");
    }

    private static void testIntAssert() {
        Assertion.assertThat(12).isEqualTo(12);
        Assertion.assertThat(12).isLessThan(15);
        Assertion.assertThat(12).isGreaterThan(0);

        System.out.println("Integer assertions passed");
    }
}