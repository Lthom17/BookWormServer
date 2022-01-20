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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LibraryServiceTest {

    @Autowired
    LibraryService service;

    @MockBean
    BookshelfRepository bookshelfRepository;

    @MockBean
    BookRepository bookRepository;


    @Test
    void shouldBuildLibrary(){
        Member member = makeMember();
        UUID libraryId = member.getLibrary().getLibraryId();
        List<Bookshelf> bookshelves = makeBookshelfList();
        when(bookshelfRepository.findByLibraryId(libraryId)).thenReturn(bookshelves);

        Library expected = new Library(libraryId);
        expected.setBookshelves(bookshelves);

        Library result = service.buildLibrary(libraryId);
        assertEquals(expected, result);

    }

    @Test
    void shouldFindBookshelvesByLibraryId() {
        Member member = makeMember();
        UUID libraryId = member.getLibrary().getLibraryId();
        List<Bookshelf> bookshelves = makeBookshelfList();
        when(bookshelfRepository.findByLibraryId(libraryId)).thenReturn(bookshelves);

        Result result = service.findBookshelvesByLibraryId(libraryId.toString());

        assertNotNull(result);
        assertEquals(result.getPayload(), bookshelves);
    }

    private Member makeMember(){
        String username = "JohnnyTest";
        String email = "john@smith.com";
        String firstName = "";
        String lastName = "";
        String password = "'$2a$12$Y61I/H1LH6RKqEzLX96k1.5pKFKKkSOUupcgJrdGn4DhHP5Mg/Uaa'";
        BookUser user = new BookUser(username, password, email, firstName, lastName);

        Library lib = new Library(UUID.fromString("9f71448c-7326-11ec-90d6-0242ac120003"));
        List<String> roles = new ArrayList<>();
        roles.add("GROUP_FOUNDER");

        return new Member(user, lib, roles, false);
    }

    private List<Bookshelf> makeBookshelfList(){
        List<Bookshelf> bookshelves = new ArrayList<>();
        int count = 5;
        while(count > 0){
            bookshelves.add(makeBookshelf());
            count--;
        }
        return bookshelves;
    }

    private Bookshelf makeBookshelf(){
        Book book = new Book("9780306810619", "Ulysses S. Grant", "Personal Memoirs of Ulysses S. Grant");
        List<Book> books = new ArrayList<>();
        books.add(book);

        return new Bookshelf( books, "Civil War", UUID.randomUUID(), BookshelfType.MEMBER_SHELF);

    }

}