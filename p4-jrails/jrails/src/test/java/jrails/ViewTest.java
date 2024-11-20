package jrails;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.*;

public class ViewTest {

    @Test
    public void empty() {
        assertThat(View.empty().toString(), isEmptyString());
    }

    @Test
    public void testParagraphTag() {
        Html html = View.p(View.t("Hello, World!"));
        assertEquals("<p>Hello, World!</p>", html.toString());
    }
}