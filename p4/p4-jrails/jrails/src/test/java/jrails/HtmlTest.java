package jrails;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.*;

public class HtmlTest {

    private Html html;

    @Before
    public void setUp() throws Exception {
        html = new Html();
    }

    @Test
    public void empty() {
        assertThat(View.empty().toString(), isEmptyString());
    }

    @Test
    public void test0() {
        html = new Html();
        html = html.p(html.t("loading test 0"));
        assert(html.toString().equals("<p>loading test 0</p>"));
    }

    @Test
    public void test1() {
        html = new Html();
        html = html.p(html.t("hello world"));
        assert(html.toString().equals("<p>hello world</p>"));
    }
}
