public class TestClassMethodTests {
    @Test
    public void A() {

    }

    @Test
    public void B() {
        
    }

    @Test
    public void C() {

    }

    @Test
    public void D() {

    }

    @Before
    public void ceforeTest() {
        System.out.println("kaboom2");
    }

    @Before
    public void beforeTest() {
        System.out.println("kaboom");
    }

    @After
    public void afterTest() {
        System.out.println("kasploosh");
    }

    @After
    public void bfterTest() {
        System.out.println("kasploosh2");
    }

    @BeforeClass
    public static void beforeClassTest() {
        System.out.println("Good morning!");
    }

    @AfterClass
    public static void afterClassTest() {
        System.out.println("Good night!");
    }
}