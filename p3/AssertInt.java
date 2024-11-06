public class AssertInt {
    private final int num;

    AssertInt(int i) {
        num = i;
    }

    public void isEqualTo(int i2) {
        if (num != i2) {
            System.out.println("Integer checked isEqualTo() was not equal to input.");
            throw new IllegalStateException();
        }
    }

    public void isLessThan(int i2) {
        if (num >= i2) {
            System.out.println("Integer checked isLessThan() was greater than or equal to input.");
            throw new IllegalStateException();
        }
    }

    public void isGreaterThan(int i2) {
        if (num <= i2) {
            System.out.println("Integer checked isGreaterThan() was less than or equal to input.");
            throw new IllegalStateException();
        }
    }
}
