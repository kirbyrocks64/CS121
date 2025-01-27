public class Assertion {
    static AssertObj assertThat(Object o) {
	    return new AssertObj(o);
    }
    static AssertStr assertThat(String s) {
	    return new AssertStr(s);
    }
    static AssertBool assertThat(boolean b) {
	    return new AssertBool(b);
    }
    static AssertInt assertThat(int i) {
	    return new AssertInt(i);
    }

    public static class AssertObj {
        private final Object obj;
    
        public AssertObj(Object o) {
            this.obj = o;
        }
    
        public AssertObj isNotNull() {
            if (obj == null) {
                System.out.println("Object checked IsNotNull() was null.");
                throw new IllegalStateException();
            } return this;
        }
    
        public AssertObj isNull() {
            if (obj != null) {
                System.out.println("Object checked IsNull() was not null.");
                throw new IllegalStateException();
            } return this;
        }
    
        public AssertObj isEqualTo(Object o2) {
            if (!obj.equals(o2)) {
                System.out.println("Object checked isEqualTo() was not equivalent to input.");
                throw new IllegalStateException();
            } return this;
        }
    
        public AssertObj isNotEqualTo(Object o2) {
            if (obj.equals(o2)) {
                System.out.println("Object checked isNotEqualTo() was equivalent to input.");
                throw new IllegalStateException();
            } return this;
        }
    
        public AssertObj isInstanceOf(Class c) {
            if (!c.isInstance(obj)) {
                System.out.println("Object checked isInstanceOf() was not an instance of input.");
                throw new IllegalStateException();
            } return this;
        }
    }

    public static class AssertStr {
        private final String string;
    
        public AssertStr(String s) {
            this.string = s;
        }
    
        public AssertStr isNotNull() {
            if (string == null) {
                System.out.println("String checked IsNotNull() was null.");
                throw new IllegalStateException();
            } return this;
        }
    
        public AssertStr isNull() {
            if (string != null) {
                System.out.println("String checked IsNull() was not null.");
                throw new IllegalStateException();
            } return this;
        }
    
        public AssertStr isEqualTo(Object o2) {
            if (!string.equals(o2)) {
                System.out.println("String checked isEqualTo() was not equivalent to input.");
                throw new IllegalStateException();
            } return this;
        }
    
        public AssertStr isNotEqualTo(Object o2) {
            if (string.equals(o2)) {
                System.out.println("String checked isNotEqualTo() was equivalent to input.");
                throw new IllegalStateException();
            } return this;
        }
    
        public AssertStr startsWith(String s2) {
            if (!string.startsWith(s2)) {
                System.out.println("String checked startsWith() did not start with input.");
                throw new IllegalStateException();
            } return this;
        }
    
        public AssertStr isEmpty() {
            if (!string.isEmpty()) {
                System.out.println("String checked isEmpty() was not empty.");
                throw new IllegalStateException();
            } return this;
        }
    
        public AssertStr contains(String s2) {
            if (!string.contains(s2)) {
                System.out.println("String checked contains() did not contain input.");
                throw new IllegalStateException();
            } return this;
        }
    }

    public static class AssertBool {
        private final boolean bool;
    
        public AssertBool(boolean b) {
            this.bool = b;
        }
    
        public AssertBool isEqualTo(boolean b2) {
            if (bool != b2) {
                System.out.println("Boolean checked isEqualTo() was not equal to input.");
                throw new IllegalStateException();
            } return this;
        }
    
        public AssertBool isTrue() {
            if (!bool) {
                System.out.println("Boolean checked isTrue() was false.");
                throw new IllegalStateException();
            } return this;
        }
    
        public AssertBool isFalse() {
            if (bool) {
                System.out.println("Boolean checked isFalse() was true.");
                throw new IllegalStateException();
            } return this;
        }
    }

    public static class AssertInt {
        private final int num;
    
        public AssertInt(int i) {
            this.num = i;
        }
    
        public AssertInt isEqualTo(int i2) {
            if (num != i2) {
                System.out.println("Integer checked isEqualTo() was not equal to input.");
                throw new IllegalStateException();
            } return this;
        }
    
        public AssertInt isLessThan(int i2) {
            if (num >= i2) {
                System.out.println("Integer checked isLessThan() was greater than or equal to input.");
                throw new IllegalStateException();
            } return this;
        }
    
        public AssertInt isGreaterThan(int i2) {
            if (num <= i2) {
                System.out.println("Integer checked isGreaterThan() was less than or equal to input.");
                throw new IllegalStateException();
            } return this;
        }
    }
}