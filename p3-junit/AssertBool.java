public class AssertBool {
    private final boolean bool;

    AssertBool(boolean b) {
        bool = b;
    }

    public void isEqualTo(boolean b2) {
        if (bool != b2) {
            System.out.println("Boolean checked isEqualTo() was not equal to input.");
            throw new IllegalStateException();
        }
    }

    public void isTrue() {
        if (!bool) {
            System.out.println("Boolean checked isTrue() was false.");
            throw new IllegalStateException();
        }
    }

    public void isFalse() {
        if (bool) {
            System.out.println("Boolean checked isFalse() was true.");
            throw new IllegalStateException();
        }
    }
}
