package bookworm.data;

import bookworm.models.Book;
import bookworm.models.Bookshelf;
import bookworm.models.BookshelfType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Profile("testing")
class BookshelfJdbcTemplateRepositoryTest {

    @Autowired
    BookshelfJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {knownGoodState.set();}

    @Test
    void shouldAddBookshelf() {
        assertTrue(repository.add(makeBookshelf(), UUID.fromString("ae5dcdb6-732c-11ec-90d6-0242ac120003")));
    }

    @Test
    void shouldRenameBookshelf() {
        Bookshelf testShelf = new Bookshelf(new ArrayList<Book>(), "Changed name", UUID.fromString("615264fe-7328-11ec-90d6-0242ac120003"), BookshelfType.MEMBER_SHELF);
        assertTrue(repository.update(testShelf, UUID.fromString("ae5dcdb6-732c-11ec-90d6-0242ac120003")));
    }

    @Test
    void shouldChangeBookshelfParent() {
        Bookshelf testShelf = new Bookshelf(new ArrayList<Book>(), "Combat Sports", UUID.fromString("615264fe-7328-11ec-90d6-0242ac120003"), BookshelfType.GROUP_SHELF);
        testShelf.setParentBookshelfId(UUID.fromString("8fe0dc4c-4bf0-45b1-8349-33186543faff"));
        assertTrue(repository.update(testShelf, UUID.fromString("61526bb6-7328-11ec-90d6-0242ac120003")));
    }

    @Test
    void shouldFindBookshelfByLibraryId() {
        assertEquals(3, repository.findByLibraryId(UUID.fromString("9f71448c-7326-11ec-90d6-0242ac120003")).size());
    }

    private Bookshelf makeBookshelf() {
        return new Bookshelf(new ArrayList<Book>(), "test_shelf", UUID.fromString("63052f06-c504-43ac-bf4f-9034af6e6b86"), BookshelfType.MEMBER_SHELF);
    }
}
