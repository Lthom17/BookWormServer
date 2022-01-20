package bookworm.models;

import java.util.Date;
import java.util.Objects;

public class Book {

    private String isbn;
    private String cover;
    private String author;
    private String title;
    private String description;
    private Date datePublished;


    public Book() {}

    public Book(String isbn, String author, String title) {
        this.isbn = isbn;
        this.author = author;
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isbn.equals(book.isbn) && Objects.equals(cover, book.cover) && author.equals(book.author) && title.equals(book.title) && Objects.equals(description, book.description) && Objects.equals(datePublished, book.datePublished);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, cover, author, title, description, datePublished);
    }
}