package bookworm.data;

import bookworm.data.mappers.MemberMapper;
import bookworm.models.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public class MemberJdbcTemplateRepository implements MemberRepository {

    private JdbcTemplate jdbcTemplate;

    public MemberJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Member findByUsername(String username) {
        final String sql = "select m.username, email, first_name, last_name, password_hash, library_id, r.`name`, disabled " +
                "from `member` m " +
                "inner join member_roles as mr on mr.username = m.username " +
                "inner join roles as r on r.role_id = mr.role_id " +
                "where m.username = ?;";

        return (Member) jdbcTemplate.query(sql, new MemberMapper(), username).stream()
                .findFirst().orElse(null);
    }

    @Override
    public Member add(Member member) {
        final String sql = "insert into member (username, email, first_name, last_name, library_id, password_hash) values (?,?,?,?,?,?);";

        int rowsAffected = jdbcTemplate.update(sql,
                member.getUsername(),
                member.getBookUser().getEmail(),
                member.getBookUser().getFirst_name(),
                member.getBookUser().getLast_name(),
                member.getLibrary().getLibraryId().toString(),
                member.getPassword());

        if (rowsAffected <= 0) {
            return null;
        }

        updateRoles(member);
        return member;

    }

    @Override
    @Transactional
    public void update(Member member) {
        final String sql = "update member set "
                + "username = ?, "
                + "disabled = ? "
                + "where username = ?";

        jdbcTemplate.update(sql,
                member.getUsername(), !member.isEnabled(), member.getUsername());

        updateRoles(member);
    }


    private void updateRoles(Member member) {
        jdbcTemplate.update("delete from member_roles where username = ?;", member.getUsername());

        Collection<GrantedAuthority> authorities = member.getAuthorities();

        if (authorities == null) {
            return;
        }

        for (String role : Member.convertAuthoritiesToRoles(authorities)) {
            String sql = "insert into member_roles (role_id, username) "
                    + "select role_id, ? from roles where `name` = ?;";
            jdbcTemplate.update(sql, member.getUsername(), role);
        }
    }


    private List<String> getRolesByUsername(String username) {
        final String sql = "select r.name " +
                "from member_roles mr " +
                "inner join roles r on mr.role_id = r.role_id " +
                "inner join member m on mr.username = m.username " +
                "where mr.username = ?;";

        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("name"), username);
    }
}