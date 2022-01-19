package bookworm.domain;

import bookworm.data.BookRepository;
import bookworm.data.BookshelfRepository;
import bookworm.models.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class BookshelfService {

    private final BookshelfRepository BOOKSHELF_REPOSITORY;
    private final BookRepository BOOK_REPOSITORY;

    public BookshelfService(BookshelfRepository bookshelfRepository, BookRepository bookRepository) {
        this.BOOKSHELF_REPOSITORY = bookshelfRepository;
        this.BOOK_REPOSITORY = bookRepository;
    }

    public Result<Object> addBookshelf(String name, String bookshelfType, UUID libraryId, String parentBookshelfId) {
        Result<Object> result = validateInput(name, bookshelfType);
        if (!result.isSuccess()) {
            return result;
        }

        Bookshelf bookshelf = buildBookshelf(name, bookshelfType);
        result = validateBookshelf(bookshelf);
        validateUUID(libraryId, result);

        if (!result.isSuccess()) {
            return result;
        }

        if(!Validations.isNullOrBlank(parentBookshelfId)) {
            bookshelf.setParentBookshelfId(UUID.fromString(parentBookshelfId));
        }

        if (libraryId == null) {
            result.addMessage("library ID must be specified for add operation", ResultType.INVALID);
            return result;
        }

        if (!BOOKSHELF_REPOSITORY.add(bookshelf, libraryId)) {
            result.addMessage("Bookshelf add failed.", ResultType.INVALID);
        }

        return result;
    }

    public Result<Object> updateBookshelf(Bookshelf bookshelf, UUID libraryId) {
        Result<Object> result = validateBookshelf(bookshelf);
        validateUUID(libraryId, result);
        if (!result.isSuccess()) {
            return result;
        }

        if (!BOOKSHELF_REPOSITORY.update(bookshelf, libraryId)) {
            String msg = String.format("bookshelf ID %s not found", bookshelf.getBookshelfId().toString());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }
        return result;
    }

    @Transactional
    public boolean deleteBookshelf(UUID bookshelfId) {
        Result<Object> result = new Result<>();
        validateUUID(bookshelfId, result);
        boolean bookDeleted = false, bookshelfDeleted = false;

        if (!result.isSuccess()) {
            return false;
        }

        if (BOOK_REPOSITORY.findByBookshelfId(bookshelfId).size() > 0) {
            bookDeleted = BOOK_REPOSITORY.deleteByBookshelfId(bookshelfId);
        } else
            bookDeleted = true;

        bookshelfDeleted = BOOKSHELF_REPOSITORY.delete(bookshelfId);

        if (!bookDeleted || !bookshelfDeleted) {
            return false;
        } else
            return true;
    }

    public Result<Object> addBookToBookshelf(Book book, UUID bookshelfId) {
        Result<Object> result = validateBook(book);
        validateUUID(bookshelfId, result);

        if (!result.isSuccess()) {
            return result;
        }

        if (BOOK_REPOSITORY.add(book, bookshelfId) == null) {
            result.addMessage("Book add failed", ResultType.INVALID);
        }

        return result;
    }

    public boolean removeBookFromBookshelf(Book book, UUID bookshelfId) {
        Result<Object> result = new Result();
        if (book == null || book.getIsbn() == null || book.getIsbn().isBlank()) {
            return false;
        }

        return BOOK_REPOSITORY.delete(book, bookshelfId);
    }

    private Bookshelf buildBookshelf(String name, String bookshelfType) {
        BookshelfType type;

        if (bookshelfType.equalsIgnoreCase("Member")) {
            type = BookshelfType.MEMBER_SHELF;
        } else
            type = BookshelfType.GROUP_SHELF;

        return new Bookshelf(new ArrayList<Book>(), name, generateBookshelfId(), type);
    }

    private UUID generateBookshelfId() {
        return UUID.randomUUID();
    }

    private Result<Object> validateBookshelf(Bookshelf bookshelf) {
        Result<Object> result = new Result<>();

        if (bookshelf == null) {
            result.addMessage("Bookshelf cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(bookshelf.getName())) {
            result.addMessage("Bookshelf name is required", ResultType.INVALID);
        }

        if (bookshelf.getType() == null) {
            result.addMessage("Bookshelf type is required", ResultType.INVALID);
        }

        if (bookshelf.getParentBookshelfId() != null && BOOKSHELF_REPOSITORY.findByBookshelfId(bookshelf.getParentBookshelfId()) == null) {
            result.addMessage("Bookshelf parent does not exist", ResultType.INVALID);
            return result;
        }

        if (bookshelf.getParentBookshelfId() != null && !BOOKSHELF_REPOSITORY.findByBookshelfId(bookshelf.getParentBookshelfId()).getType().equals(bookshelf.getType())) {
            result.addMessage("Bookshelf and bookshelf parent must be the same type", ResultType.INVALID);
        }

        return result;
    }

    private Result<Object> validateInput(String name, String bookshelfType) {
        Result<Object> result = new Result<>();


        if (Validations.isNullOrBlank(name)) {
            result.addMessage("Bookshelf name is required", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(bookshelfType)) {
            result.addMessage("Bookshelf type is required", ResultType.INVALID);
        }

        return result;
    }

    private Result<Object> validateBook(Book book) {
        Result<Object> result = new Result<>();

        if (book == null) {
            result.addMessage("Book cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(book.getIsbn())) {
            result.addMessage("Book requires an ISBN", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(book.getAuthor())) {
            result.addMessage("Book requires an author", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(book.getTitle())) {
            result.addMessage("Book requires a title", ResultType.INVALID);
        }

        return result;
    }

    private void validateUUID(UUID value, Result<Object> result) {
        if (Validations.isNullOrBlank(value)) {
            result.addMessage("UUID cannot be null", ResultType.INVALID);
        }
    }
}