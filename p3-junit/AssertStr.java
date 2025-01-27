public class AssertStr {
    private final String string;

    AssertStr(String s) {
        string = s;
    }

    public AssertStr isNotNull() {
        if (string == null) {
            System.out.println("String checked IsNotNull() was null.");
            throw new IllegalStateException();
        } else {
            return this;
        }
    }

    public void isNull() {
        if (string != null) {
            System.out.println("String checked IsNull() was not null.");
            throw new IllegalStateException();
        }
    }

    public void isEqualTo(Object o2) {
        if (!string.equals(o2)) {
            System.out.println("String checked isEqualTo() was not equivalent to input.");
            throw new IllegalStateException();
        }
    }

    public void isNotEqualTo(Object o2) {
        if (string.equals(o2)) {
            System.out.println("String checked isNotEqualTo() was equivalent to input.");
            throw new IllegalStateException();
        }
    }

    public void startsWith(String s2) {
        if (!string.startsWith(s2)) {
            System.out.println("String checked startsWith() did not start with input.");
            throw new IllegalStateException();
        }
    }

    public void isEmpty() {
        if (!string.isEmpty()) {
            System.out.println("String checked isEmpty() was not empty.");
            throw new IllegalStateException();
        }
    }

    public void contains(String s2) {
        if (!string.contains(s2)) {
            System.out.println("String checked contains() did not contain input.");
            throw new IllegalStateException();
        }
    }
}
