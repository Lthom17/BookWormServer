package bookworm.domain;

import bookworm.data.BookRepository;
import bookworm.data.BookshelfRepository;
import bookworm.models.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LibraryService {

    private BookshelfRepository bookshelfRepo;
    private BookRepository bookRepo;


    public LibraryService(BookshelfRepository bookshelfRepo, BookRepository bookRepo) {
        this.bookshelfRepo = bookshelfRepo;
        this.bookRepo = bookRepo;
    }

    public Library buildLibrary(UUID library_id) {
        Library library = new Library(library_id);
        List<Bookshelf> bookshelves = bookshelfRepo.findByLibraryId(library_id);
        for (Bookshelf bs : bookshelves) {
            List<Book> books = bookRepo.findByBookshelfId(bs.getBookshelfId());
            bs.setBooks(books);
        }

        library.setBookshelves(bookshelves);

        return library;
    }

    public Result<List<Bookshelf>> findBookshelvesByLibraryId(String libraryId) {
        Result<List<Bookshelf>> result = new Result<List<Bookshelf>>();
        if (libraryId == null || libraryId.isBlank()) {
            result.addMessage("A library id is required.", ResultType.INVALID);
            return result;
        }

        List<Bookshelf> bookshelves = bookshelfRepo.findByLibraryId(UUID.fromString(libraryId));
        result.setPayload(bookshelves);
        return result;

    }

}