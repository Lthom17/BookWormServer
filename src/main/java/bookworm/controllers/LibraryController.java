package bookworm.controllers;

import bookworm.domain.BookshelfService;
import bookworm.domain.LibraryService;
import bookworm.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("api/library")
public class LibraryController {

    private final LibraryService libraryService;
    private final BookshelfService bookshelfService;

    public LibraryController(LibraryService libraryService, BookshelfService bookshelfService) {
        this.libraryService = libraryService;
        this.bookshelfService = bookshelfService;
    }

    @GetMapping("/{libraryId}")
    public Library buildLibrary(@PathVariable String libraryId) {
        return libraryService.buildLibrary(UUID.fromString(libraryId));
    }

    @PostMapping("/bookshelf")
    public ResponseEntity addBookshelf(@RequestBody Map<String, String> bookshelfInfo) {
        Result result = bookshelfService.addBookshelf(bookshelfInfo.get("bookshelfName"),
                bookshelfInfo.get("type"),
                UUID.fromString(bookshelfInfo.get("libraryId")), bookshelfInfo.get("parentId"));
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PostMapping("/book")
    public ResponseEntity addBook(@RequestBody Map<String, String> bookToAdd){
        Book book = new Book(bookToAdd.get("isbn"), bookToAdd.get("author") , bookToAdd.get("title"));
        UUID bookshelf_id = UUID.fromString(bookToAdd.get("bookshelfId"));
        Result<Object> result = bookshelfService.addBookToBookshelf(book, bookshelf_id);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping()
    public ResponseEntity updateBookshelf(@RequestBody Map<String, String> bookshelfToUpdate) {
        List<Book> bookList = new ArrayList<>();
        Bookshelf bookshelf = new Bookshelf(bookList,
                bookshelfToUpdate.get("name"), UUID.fromString(bookshelfToUpdate.get("bookshelfId")),
                BookshelfType.valueOf(bookshelfToUpdate.get("type")));

        Result result = bookshelfService.updateBookshelf(bookshelf, UUID.fromString(bookshelfToUpdate.get("libraryId")));
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/bookshelf")
    public ResponseEntity deleteBookshelf(@RequestBody String bookshelfId) {
        if (bookshelfService.deleteBookshelf(UUID.fromString(bookshelfId))) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/book")
    public ResponseEntity removeBookFromBookshelf(@RequestBody Map<String, String> bookToRemove) {
        Book book = new Book();
        book.setIsbn(bookToRemove.get("isbn"));
        if (bookshelfService.removeBookFromBookshelf(book, UUID.fromString(bookToRemove.get("bookshelfId")))) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}

