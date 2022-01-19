package bookworm.data;

import bookworm.data.mappers.BookMapper;
import bookworm.models.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;

@Repository
public class BookJdbcTemplateRepository implements BookRepository {
    private final JdbcTemplate jdbcTemplate;

    public BookJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> findByBookshelfId(UUID bookshelf_id) {
        final String sql = "select isbn "
                + "from book "
                + "where bookshelf_id = ?;";
        return jdbcTemplate.query(sql, new BookMapper(), bookshelf_id.toString());
    }

    @Override
    public Book add(Book book, UUID bookshelf_id) {
        final String sql = "insert into book (isbn, bookshelf_id) "
                + " values (?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, book.getIsbn());
            ps.setString(2, bookshelf_id.toString());
            return ps;
        });
        if (rowsAffected <= 0) {
            return null;
        }
        return book;
    }

    @Override
    public boolean delete(Book book, UUID bookshelfId) {
        final String sql = "delete from book where isbn = ? and bookshelf_id = ?;";
        return jdbcTemplate.update(sql, book.getIsbn(), bookshelfId.toString()) > 0;
    }

    @Override
    public boolean deleteByBookshelfId(UUID bookshelf_id) {
        final String sql = "delete from book where bookshelf_id = ?";
        return jdbcTemplate.update(sql, bookshelf_id.toString()) > 0;
    }
}
