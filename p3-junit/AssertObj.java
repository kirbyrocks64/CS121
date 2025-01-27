public class AssertObj {
    private final Object obj;

    AssertObj(Object o) {
        obj = o;
    }

    public AssertObj isNotNull() {
        if (obj == null) {
            System.out.println("Object checked IsNotNull() was null.");
            throw new IllegalStateException();
        } else {
            return this;
        }
    }

    public void isNull() {
        if (obj != null) {
            System.out.println("Object checked IsNull() was not null.");
            throw new IllegalStateException();
        }
    }

    public void isEqualTo(Object o2) {
        if (!obj.equals(o2)) {
            System.out.println("Object checked isEqualTo() was not equivalent to input.");
            throw new IllegalStateException();
        }
    }

    public void isNotEqualTo(Object o2) {
        if (obj.equals(o2)) {
            System.out.println("Object checked isNotEqualTo() was equivalent to input.");
            throw new IllegalStateException();
        }
    }

    public void isInstanceOf(Class c) {
        if (!c.isInstance(obj)) {
            System.out.println("Object checked isInstanceOf() was not an instance of input.");
            throw new IllegalStateException();
        }
    }
}
