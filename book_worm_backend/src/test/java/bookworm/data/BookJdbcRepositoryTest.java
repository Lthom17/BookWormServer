package bookworm.data;

import bookworm.models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookJdbcRepositoryTest {

    @Autowired
    BookJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindByBookshelfId() {

        List<Book> books = repository.findByBookshelfId(UUID.fromString("615272a0-7328-11ec-90d6-0242ac120003"));

        assertEquals(books.size(), 4);

    }

    @Test
    void shouldAddBook(){

        Book bookToAdd = new Book();
        bookToAdd.setIsbn("9788499182964");

        Book book = repository.add(bookToAdd, UUID.fromString("615272a0-7328-11ec-90d6-0242ac120003"));

        List<Book> books = repository.findByBookshelfId(UUID.fromString("615272a0-7328-11ec-90d6-0242ac120003"));

        assertEquals(bookToAdd.getIsbn(), book.getIsbn());
        assertEquals(books.size(), 4);

    }

    @Test
    void shouldDeleteBook(){

        Book bookToDelete = new Book();
        bookToDelete.setIsbn("9780425123348");

        assertTrue(repository.delete(bookToDelete, UUID.fromString("dc95af08-6669-400f-a401-a3064cffc0b9")));
    }

    @Test
    void shouldDeleteByBookshelfId(){
        assertTrue(repository.deleteByBookshelfId(UUID.fromString("8fe0dc4c-4bf0-45b1-8349-33186543faff")));
    }

}