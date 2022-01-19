package bookworm.data;

import bookworm.data.mappers.GroupMapper;
import bookworm.models.Group;
import bookworm.models.Library;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class GroupJdbcTemplateRepository implements GroupRepository {

    private final JdbcTemplate jdbcTemplate;

    public GroupJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Group> findByName(String groupName) {
        final String sql = "select *"
                + " from `group`"
                + " where name = ?;";

        return jdbcTemplate.query(sql, new GroupMapper(), groupName);
    }

    @Override
    public List<Group> findByOwner(String ownerUsername) {
        final String sql = "select *"
                + " from `group`"
                + " where owner = ?;";

        return jdbcTemplate.query(sql, new GroupMapper(), ownerUsername);
    }

    @Override
    public Group findByGroupId(UUID groupId) {
        final String sql = "select *"
                + " from `group`"
                + " where group_id = ?;";

        return jdbcTemplate.query(sql, new GroupMapper(), groupId.toString()).stream()
                .findFirst().orElse(null);
    }

    @Override
    public boolean add(String name, String description, String ownerUsername) {
        final String sql = "insert into `group` (group_id, name, description, owner, library_id)"
                + " values (?,?,?,?,?);";

        return jdbcTemplate.update(sql,
                generateGroupId().toString(),
                name,
                description,
                ownerUsername,
                new Library().getLibraryId().toString()) > 0;
    }

    @Override
    public boolean update(Group group) {
        final String sql = "update `group` set"
                + " group_id = ?,"
                + " name = ?,"
                + " description = ?,"
                + " owner = ?,"
                + " library_id = ?"
                + " where group_id = ?;";

        return jdbcTemplate.update(sql,
                group.getGROUP_ID().toString(),
                group.getName(),
                group.getDescription(),
                group.getOwner(),
                group.getLibrary().getLibraryId().toString(),
                group.getGROUP_ID().toString()) > 0;
    }

    @Override
    public boolean delete(UUID groupId) {
        return jdbcTemplate.update("delete from `group` where group_id = ?;", groupId.toString()) > 0;
    }

    @Override
    public boolean addGroupMember(String memberUsername, UUID groupId) {
        final String sql = "insert into group_members (member_username, group_id)"
                + " values (?,?);";

        return jdbcTemplate.update(sql,
                memberUsername,
                groupId.toString()) > 0;
    }

    @Override
    public boolean deleteGroupMember(String memberUsername, UUID groupId) {
        return jdbcTemplate.update("delete from group_members where member_username = ? AND group_id = ?;", memberUsername, groupId.toString()) > 0;
    }

    private UUID generateGroupId() {
        return UUID.randomUUID();
    }
}
