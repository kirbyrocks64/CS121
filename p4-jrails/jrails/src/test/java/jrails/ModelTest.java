package jrails;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import books.Book;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

public class ModelTest {

    private Model model;

    @Before
    public void setUp() throws Exception {
        model = new Model(){};
    }

    @Test
    public void id() {
        assertThat(model.id(), notNullValue());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test1() {
        Book b1 = new Book();
        b1.title = "Pride and Prejudice";
        b1.author = "Jane Austen";
        b1.num_copies = 100;
        b1.save();
        Book b2 = new Book();
        b2.title = "Lord of the Rings";
        b2.author = "J.R.R. Tolkien";
        b2.num_copies = 200;
        b2.save();
        
        assert(Model.find(Book.class, b1.id()) != null);
        assert(Model.find(Book.class, b2.id()) != null);
        List<Book> lib = Model.all(Book.class);
        /* assert(lib.contains(b1));
        assert(lib.contains(b2)); */
        assert(lib.size() == 2);
    }
}