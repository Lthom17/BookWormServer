package bookworm.domain;

import bookworm.data.BookRepository;
import bookworm.data.BookshelfRepository;
import bookworm.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookshelfServiceTest {

    @Autowired
    BookshelfService service;

    @MockBean
    BookshelfRepository bookshelfRepository;

    @MockBean
    BookRepository bookRepository;

    @Test
    void shouldAddNewBookshelfWithNoParent() {
        when(bookshelfRepository.add(any(Bookshelf.class), any(UUID.class))).thenReturn(true);

        Result<Object> result = service.addBookshelf("Test Shelf", "Member", UUID.fromString("6e75c7da-7da5-4e3f-a1f9-e110669a7108"), null);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldAddNewBookshelfWithParent() {
        when(bookshelfRepository.add(any(Bookshelf.class), any(UUID.class))).thenReturn(true);

        Result<Object> result = service.addBookshelf("Test Shelf", "Member", UUID.fromString("6e75c7da-7da5-4e3f-a1f9-e110669a7108"), "dc95af08-6669-400f-a401-a3064cffc0b9");
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotAddInvalidBookShelf() {
        when(bookshelfRepository.add(any(Bookshelf.class), any(UUID.class))).thenReturn(false);

        //Fail when no name
        Result<Object> result = service.addBookshelf("", "Member", UUID.fromString("6e75c7da-7da5-4e3f-a1f9-e110669a7108"), null);
        assertEquals(ResultType.INVALID, result.getType());

        //Fail when bad bookshelf type
        result = service.addBookshelf("Test Shelf", "asdas", UUID.fromString("6e75c7da-7da5-4e3f-a1f9-e110669a7108"), null);
        assertEquals(ResultType.INVALID, result.getType());

        //Fail when null bookshelf
        result = service.addBookshelf(null, null, null, null);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldRenameBookshelf() {
        Bookshelf testBookshelf = new Bookshelf(new ArrayList<Book>(), "Test Shelf", UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20"), BookshelfType.GROUP_SHELF);

        when(bookshelfRepository.update(testBookshelf, UUID.fromString("6e75c7da-7da5-4e3f-a1f9-e110669a7108"))).thenReturn(true);

        Result<Object> result = service.updateBookshelf(testBookshelf, UUID.fromString("6e75c7da-7da5-4e3f-a1f9-e110669a7108"));
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotPerformInvalidRename() {
        Bookshelf testBookshelf = new Bookshelf(new ArrayList<Book>(), "Test Shelf", UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20"), BookshelfType.GROUP_SHELF);

        //Fail when no library id
        when(bookshelfRepository.update(testBookshelf, null)).thenReturn(false);

        Result<Object> result = service.updateBookshelf(testBookshelf, null);
        assertEquals(ResultType.INVALID, result.getType());

        //Fail when null bookshelf
        when(bookshelfRepository.update(null, UUID.fromString("6e75c7da-7da5-4e3f-a1f9-e110669a7108"))).thenReturn(false);

        result = service.updateBookshelf(null, UUID.fromString("6e75c7da-7da5-4e3f-a1f9-e110669a7108"));
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldChangeBookshelfParent() {
        Bookshelf testBookshelf = new Bookshelf(new ArrayList<Book>(), "Test Shelf", UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20"), BookshelfType.MEMBER_SHELF);
        Bookshelf parentShelf = new Bookshelf(new ArrayList<Book>(), "Parent Shelf", UUID.fromString("7f5607cd-4945-40c3-a0e9-4f9661a5efa8"), BookshelfType.MEMBER_SHELF);

        testBookshelf.setParentBookshelfId(UUID.fromString("7f5607cd-4945-40c3-a0e9-4f9661a5efa8"));

        when(bookshelfRepository.findByBookshelfId(UUID.fromString("7f5607cd-4945-40c3-a0e9-4f9661a5efa8"))).thenReturn(parentShelf);
        when(bookshelfRepository.update(testBookshelf, UUID.fromString("6e75c7da-7da5-4e3f-a1f9-e110669a7108"))).thenReturn(true);

        testBookshelf.setParentBookshelfId(UUID.fromString("7f5607cd-4945-40c3-a0e9-4f9661a5efa8"));
        Result<Object> result = service.updateBookshelf(testBookshelf, UUID.fromString("6e75c7da-7da5-4e3f-a1f9-e110669a7108"));
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotChangeBookshelfToNonExistentParent() {
        Bookshelf testBookshelf = new Bookshelf(new ArrayList<Book>(), "Test Shelf", UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20"), BookshelfType.GROUP_SHELF);

        //Fail when using a non-existent bookshelf parent
        testBookshelf.setParentBookshelfId(UUID.fromString("7f5607cd-4945-40c3-a0e9-4f9661a5efa8"));

        when(bookshelfRepository.findByBookshelfId(UUID.fromString("7f5607cd-4945-40c3-a0e9-4f9661a5efa8"))).thenReturn(null);
        when(bookshelfRepository.update(testBookshelf, UUID.fromString("6e75c7da-7da5-4e3f-a1f9-e110669a7108"))).thenReturn(false);

        Result<Object> result = service.updateBookshelf(null, UUID.fromString("6e75c7da-7da5-4e3f-a1f9-e110669a7108"));
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotParentToDifferentBookshelfType() {
        Bookshelf testBookshelf = new Bookshelf(new ArrayList<Book>(), "Test Shelf", UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20"), BookshelfType.MEMBER_SHELF);
        Bookshelf parentShelf = new Bookshelf(new ArrayList<Book>(), "Parent Shelf", UUID.fromString("7f5607cd-4945-40c3-a0e9-4f9661a5efa8"), BookshelfType.GROUP_SHELF);


        //Fail when trying to parent a member shelf to a group shelf
        when(bookshelfRepository.findByBookshelfId(UUID.fromString("7f5607cd-4945-40c3-a0e9-4f9661a5efa8"))).thenReturn(parentShelf);
        when(bookshelfRepository.update(testBookshelf, UUID.fromString("6e75c7da-7da5-4e3f-a1f9-e110669a7108"))).thenReturn(false);

        testBookshelf.setParentBookshelfId(UUID.fromString("7f5607cd-4945-40c3-a0e9-4f9661a5efa8"));
        Result<Object> result = service.updateBookshelf(testBookshelf, UUID.fromString("6e75c7da-7da5-4e3f-a1f9-e110669a7108"));
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldDeleteBookshelf() {
        Bookshelf testBookshelf = new Bookshelf(new ArrayList<Book>(), "Test Shelf", UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20"), BookshelfType.MEMBER_SHELF);

        List<Book> mockBookList = new ArrayList<>();
        mockBookList.add(mock(Book.class));
        mockBookList.add(mock(Book.class));
        mockBookList.add(mock(Book.class));

        when(bookRepository.findByBookshelfId(UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20"))).thenReturn(mockBookList);
        when(bookRepository.deleteByBookshelfId(UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20"))).thenReturn(true);
        when(bookshelfRepository.delete(UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20"))).thenReturn(true);

        assertTrue(service.deleteBookshelf(UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20")));
    }

    @Test
    void shouldDeleteEmptyBookshelf() {

        when(bookRepository.findByBookshelfId(UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20"))).thenReturn(new ArrayList<Book>());
        when(bookshelfRepository.delete(UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20"))).thenReturn(true);

        assertTrue(service.deleteBookshelf(UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20")));
    }

    @Test
    void shouldNotDeleteNonExistentBookshelf() {
        List<Book> mockBookList = new ArrayList<>();
        mockBookList.add(mock(Book.class));
        mockBookList.add(mock(Book.class));
        mockBookList.add(mock(Book.class));

        when(bookRepository.findByBookshelfId(UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20"))).thenReturn(mockBookList);
        when(bookRepository.deleteByBookshelfId(UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20"))).thenReturn(false);
        when(bookshelfRepository.delete(UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20"))).thenReturn(false);

        assertFalse(service.deleteBookshelf(UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20")));
    }

    @Test
    void shouldAddBookToBookshelf() {
        Book testBook = new Book("Test ISBN", "Test Author" , "The Test Book");

        when(bookRepository.add(testBook, UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20"))).thenReturn(testBook);

        assertEquals(ResultType.SUCCESS, service.addBookToBookshelf(testBook, UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20")).getType());
    }

    @Test
    void shouldNotAddInvalidBookToBookshelf() {
        Book testBook = new Book("Test Book", "Test Author" , "The Test Book");

        //Fail when book is null
        assertEquals(ResultType.INVALID, service.addBookToBookshelf(null, UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20")).getType());

        //Fail when book isbn is blank
        testBook.setIsbn("");
        assertEquals(ResultType.INVALID, service.addBookToBookshelf(testBook, UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20")).getType());

        //Fail when book author is blank
        testBook.setAuthor("");
        assertEquals(ResultType.INVALID, service.addBookToBookshelf(testBook, UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20")).getType());

        //Fail when book title is blank
        testBook.setTitle("");
        assertEquals(ResultType.INVALID, service.addBookToBookshelf(testBook, UUID.fromString("513a2c24-1941-4dd9-8ea6-03bbb52bae20")).getType());
    }

    @Test
    void shouldRemoveBookFromBookshelf() {
        Book testBook = new Book("Test Book", "Test Author" , "The Test Book");

        when(bookRepository.delete(testBook, UUID.fromString("615270a2-7328-11ec-90d6-0242ac120003"))).thenReturn(true);
        assertTrue(service.removeBookFromBookshelf(testBook, UUID.fromString("615270a2-7328-11ec-90d6-0242ac120003")));
    }

    @Test
    void shouldNotRemoveInvalidBookFromBookshelf() {

        //Fail if book is null
        when(bookRepository.delete(null, UUID.fromString("615270a2-7328-11ec-90d6-0242ac120003"))).thenReturn(false);
        assertFalse(service.removeBookFromBookshelf(null, UUID.fromString("615270a2-7328-11ec-90d6-0242ac120003")));
    }
}