package bookworm.domain;

import bookworm.data.BookshelfRepository;
import bookworm.data.MemberRepository;
import bookworm.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService service;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    BookshelfRepository bookshelfRepository;

    @MockBean
    LibraryService libService;



    @Test
    void shouldFindMemberByUsername(){
        Member member = makeMember();
        when(memberRepository.findByUsername("JohnnyTest")).thenReturn(member);
        Library library = makeLibrary();
        when(bookshelfRepository.findByLibraryId(member.getLibrary().getLibraryId())).thenReturn(library.getBookshelves());
        when(libService.buildLibrary(member.getLibrary().getLibraryId())).thenReturn(library);
        Result result = service.findByUsername("JohnnyTest");
        Member memResult = (Member) result.getPayload();
        Library libraryResult = memResult.getLibrary();
        assertEquals(memResult.getUsername(), "JohnnyTest");
        assertNotNull(memResult.getLibrary().getBookshelves());

    }

    @Test
    void shouldNotFindMemberWhenUsernameIsBlank(){

        Result result = service.findByUsername("");

        assertEquals(result.getType(), ResultType.INVALID);

    }

    @Test
    void shouldNotFindUserIfUsernameDoesNotExist(){
        String username = "YosemiteSam";
        when(memberRepository.findByUsername(username)).thenReturn(null);
        Result result = service.findByUsername(username);

        assertEquals(ResultType.NOT_FOUND, result.getType());
    }


    @Test
    void shouldAddMember(){

        Member member = makeNewMember();

        when(memberRepository.add(member)).thenReturn(member);

        Result result = service.add(member);

        Member resultMember = (Member) result.getPayload();

        assertEquals(resultMember.getUsername(), member.getUsername());

    }

    @Test
    void shouldNotAddMemberWithoutAUserName(){
        String username = " ";
        String password = "P@ssw0rd!";
        String email = "Bart@simpson.com";
        String firstName = "";
        String lastName = "";
        BookUser bookUser = new BookUser(username, password, email, firstName, lastName);
        List<String> roles = new ArrayList<>();
        roles.add("MEMBER");

        Member member =  new Member(bookUser, roles, false);
        Result result = service.add(member);

        assertEquals(result.getType(), ResultType.INVALID);

    }

    @Test
    void shouldNotAddMemberWithoutAPassword(){
        String username = "BartSims";
        String password = "";
        String email = "Bart@simpson.com";
        String firstName = "";
        String lastName = "";
        BookUser user = new BookUser(username, password, email, firstName, lastName);
        List<String> roles = new ArrayList<>();
        roles.add("MEMBER");

        Member member = new Member(user, roles, false);

        Result result = service.add(member);

        assertEquals(result.getType(), ResultType.INVALID);

    }




    private Member makeNewMember(){
        String username = "BartSims";
        String password = "P@ssw0rd!";
        String email = "Bart@simpson.com";
        String firstName = "";
        String lastName = "";
        BookUser bookUser = new BookUser(username, password, email, firstName, lastName);
        List<String> roles = new ArrayList<>();
        roles.add("MEMBER");

        return new Member(bookUser, roles, false);
    }


    private Member makeMember(){
        String username = "JohnnyTest";
        String email = "john@smith.com";
        String firstName = "";
        String lastName = "";
        String password = "'$2a$12$Y61I/H1LH6RKqEzLX96k1.5pKFKKkSOUupcgJrdGn4DhHP5Mg/Uaa'";
        BookUser user = new BookUser(username, password, email, firstName, lastName);

        Library lib = new Library(UUID.fromString("61526f94-7328-11ec-90d6-0242ac120003"));
        List<String> roles = new ArrayList<>();
        roles.add("MEMBER");

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

    private Library makeLibrary(){
        Library library = new Library(UUID.fromString("61526f94-7328-11ec-90d6-0242ac120003"));

        List<Book> books = new ArrayList<>();
        Book book1 = new Book();
        book1.setIsbn("9780618134700");
        Book book2 = new Book();
        book2.setIsbn("9781507844823");
        books.add(book1);
        books.add(book2);
        Bookshelf bookshelf =  new Bookshelf(books, "Fantasy Worlds with Dragons", UUID.fromString("615272a0-7328-11ec-90d6-0242ac120003"), BookshelfType.MEMBER_SHELF);
        List<Bookshelf> bookshelves = new ArrayList<>();
        bookshelves.add(bookshelf);
        library.setBookshelves(bookshelves);

        return library;
    }
}