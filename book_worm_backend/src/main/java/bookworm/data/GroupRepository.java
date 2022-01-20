package bookworm.data;

import bookworm.models.Group;

import java.util.List;
import java.util.UUID;

public interface GroupRepository {

    List<Group> findByName(String groupName);

    List<Group> findByOwner(String ownerUsername);

    Group findByGroupId(UUID groupId);

    boolean add(String name, String description, String ownerName);

    boolean update(Group group);

    boolean delete(UUID groupId);

    boolean addGroupMember(String memberUsername, UUID groupId);

    boolean deleteGroupMember(String memberUsername, UUID groupId);
}
