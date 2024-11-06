public class Assertion {
    /* You'll need to change the return type of the assertThat methods */
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
}