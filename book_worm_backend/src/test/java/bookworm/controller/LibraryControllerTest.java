package bookworm.controller;
import bookworm.domain.BookshelfService;
import bookworm.domain.LibraryService;
import bookworm.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LibraryControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    LibraryService libraryService;

    @MockBean
    BookshelfService bookshelfService;

    @Test
    @WithMockUser(roles = "MEMBER")
    void shouldBuildLibrary() throws Exception {
        String libraryId = "9f71448c-7326-11ec-90d6-0242ac120003";
        List<Bookshelf> bookshelves = makeBookshelfList();
        Library library = new Library(UUID.fromString(libraryId));
        library.setBookshelves(bookshelves);
        when(libraryService.buildLibrary(UUID.fromString(libraryId))).thenReturn(library);

        var request = MockMvcRequestBuilders.get("/api/library/{libraryId}", libraryId);

        mvc.perform(request).andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void shouldAddABookshelf() throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("libraryId", "d4f1e8c9-3077-4e5f-8c9c-085348044359");
        jsonObject.put("type", "Group");
        jsonObject.put("bookshelfName", "Fantasy Worlds with Dragons and Unicorns");
        jsonObject.put("parentId", "61526882-7328-11ec-90d6-0242ac120003");

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(jsonObject);

        String name = "Fantasy Worlds with Dragons and Unicorns";
        String type = "Group";
        String libraryId = "d4f1e8c9-3077-4e5f-8c9c-085348044359";
        String parentId = "61526882-7328-11ec-90d6-0242ac120003";
        Result result = new Result();
        when(bookshelfService.addBookshelf(name, type, UUID.fromString(libraryId), parentId)).thenReturn(result);

        var request = MockMvcRequestBuilders.post("/api/library/bookshelf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mvc.perform(request).andExpect(status().isCreated());

    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void shouldAddABookshelfWithoutParentId() throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("libraryId", "d4f1e8c9-3077-4e5f-8c9c-085348044359");
        jsonObject.put("type", "Group");
        jsonObject.put("bookshelfName", "Fantasy Worlds with Dragons and Unicorns");
        jsonObject.put("parentId", "");

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(jsonObject);

        String name = "Fantasy Worlds with Dragons and Unicorns";
        String type = "Group";
        String libraryId = "d4f1e8c9-3077-4e5f-8c9c-085348044359";
        String parentId = "";
        Result result = new Result();
        when(bookshelfService.addBookshelf(name, type, UUID.fromString(libraryId), parentId)).thenReturn(result);

        var request = MockMvcRequestBuilders.post("/api/library/bookshelf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mvc.perform(request).andExpect(status().isCreated());

    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void shouldNotAddBookshelfIfNameNull() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("libraryId", "d4f1e8c9-3077-4e5f-8c9c-085348044359");
        jsonObject.put("type", "Group");
        jsonObject.put("bookshelfName", null);
        jsonObject.put("parentId", "");

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(jsonObject);

        String type = "Group";
        String libraryId = "d4f1e8c9-3077-4e5f-8c9c-085348044359";
        String parentId = "";

        Result result = new Result();
        result.addMessage("Bookshelf name is required", ResultType.INVALID);
        when(bookshelfService.addBookshelf(null, type, UUID.fromString(libraryId), parentId)).thenReturn(result);

        var request = MockMvcRequestBuilders.post("/api/library/bookshelf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mvc.perform(request).andExpect(status().isBadRequest());


    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void shouldNotAddBookshelfIfBookshelfTypeIsNull() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("libraryId", "d4f1e8c9-3077-4e5f-8c9c-085348044359");
        jsonObject.put("type", null);
        jsonObject.put("bookshelfName", "Fantasy Worlds with Dragons and Unicorns");
        jsonObject.put("parentId", "");

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(jsonObject);

        String name = "Fantasy Worlds with Dragons and Unicorns";
        String type = null;
        String libraryId = "d4f1e8c9-3077-4e5f-8c9c-085348044359";
        String parentId = "";

        Result result = new Result();
        result.addMessage("Bookshelf name is required", ResultType.INVALID);
        when(bookshelfService.addBookshelf(name, null, UUID.fromString(libraryId), parentId)).thenReturn(result);

        var request = MockMvcRequestBuilders.post("/api/library/bookshelf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mvc.perform(request).andExpect(status().isBadRequest());


    }


    @Test
    @WithMockUser(roles = "MEMBER")
    void shouldAddBookToLibrary() throws Exception {
        Bookshelf bookshelf = makeBookshelf();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isbn", "9780306810619");
        jsonObject.put("title", "Personal Memoirs of Ulysses S. Grant");
        jsonObject.put("author", "Ulysses S. Grant");
        jsonObject.put("bookshelfId", bookshelf.getBookshelfId().toString());
        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(jsonObject);

        Book book = new Book("9780306810619", "Ulysses S. Grant", "Personal Memoirs of Ulysses S. Grant");
        Result<Object> result = new Result<>();
        when(bookshelfService.addBookToBookshelf(book, bookshelf.getBookshelfId())).thenReturn(new Result<Object>());

        var request = MockMvcRequestBuilders.post("/api/library/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mvc.perform(request).andExpect(status().isCreated());

    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void shouldNotAddBookToLibraryTitleIsBlank() throws Exception {
        Bookshelf bookshelf = makeBookshelf();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isbn", "9780306810619");
        jsonObject.put("title", "");
        jsonObject.put("author", "Ulysses S. Grant");
        jsonObject.put("bookshelfId", bookshelf.getBookshelfId().toString());
        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(jsonObject);

        Book book = new Book("9780306810619", "Ulysses S. Grant", "");
        Result result = new Result();
        result.addMessage(" ", ResultType.INVALID);
        when(bookshelfService.addBookToBookshelf(book, bookshelf.getBookshelfId())).thenReturn(result);

        var request = MockMvcRequestBuilders.post("/api/library/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mvc.perform(request).andExpect(status().isBadRequest());

    }

    
    @Test
    @WithMockUser(roles = "MEMBER")
    void shouldNotDeleteBookFromBookshelfIfMissingISBN() throws Exception {
        Bookshelf bookshelf = makeBookshelf();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isbn", "9780306810619");
        jsonObject.put("title", "Personal Memoirs of Ulysses S. Grant");
        jsonObject.put("author", "Ulysses S. Grant");
        jsonObject.put("bookshelfId", bookshelf.getBookshelfId().toString());
        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(jsonObject);

        Book book = new Book("9780306810619", "Ulysses S. Grant", "Personal Memoirs of Ulysses S. Grant");
        Result<Object> result = new Result<>();
        when(bookshelfService.addBookToBookshelf(book, bookshelf.getBookshelfId())).thenReturn(new Result<Object>());

        var request = MockMvcRequestBuilders.post("/api/library/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mvc.perform(request).andExpect(status().isCreated());

    }


    private List<Bookshelf> makeBookshelfList() {
        List<Bookshelf> bookshelves = new ArrayList<>();
        int count = 5;
        while (count > 0) {
            bookshelves.add(makeBookshelf());
            count--;
        }
        return bookshelves;
    }

    private Bookshelf makeBookshelf() {
        Book book = new Book("9780306810619", "Ulysses S. Grant", "Personal Memoirs of Ulysses S. Grant");
        List<Book> books = new ArrayList<>();
        books.add(book);

        return new Bookshelf(books, "Civil War", UUID.randomUUID(), BookshelfType.MEMBER_SHELF);

    }


}