package bookworm.domain;

import bookworm.data.GroupRepository;
import bookworm.models.Group;
import bookworm.models.Result;
import bookworm.models.ResultType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
class GroupService {

    private final GroupRepository REPOSITORY;

    public GroupService(GroupRepository groupRepository) {
        this.REPOSITORY = groupRepository;
    }

    public List<Group> findByName(String groupName) {
        return REPOSITORY.findByName(groupName);
    }

    public List<Group> findByOwner(String ownerName) {
        return REPOSITORY.findByOwner(ownerName);
    }

    public Group findByGroupId(UUID groupId) {
        return REPOSITORY.findByGroupId(groupId);
    }

    public Result<Object> addGroup(String name, String description, String ownerUsername) {
        Result<Object> result = validateFields(name, description, ownerUsername);

        if (!result.isSuccess()) {
            return result;
        }

        if (!REPOSITORY.add(name, description, ownerUsername)) {
            result.addMessage("Group add failed", ResultType.INVALID);
        }

        return result;
    }

    public Result<Object> update(Group group) {
        Result<Object> result = validateGroup(group);
        if (!result.isSuccess()) {
            return result;
        }

        if (!REPOSITORY.update(group)) {
            String msg = String.format("Group %s not found", group.getGROUP_ID().toString());
            result.addMessage(msg, ResultType.INVALID);
        }

        return result;
    }

    public boolean delete(UUID groupId) {
        Result<Object> result = new Result<>();
        validateUUID(groupId, result);

        if (!result.isSuccess()) {
            return false;
        }

        return REPOSITORY.delete(groupId);
    }

    public Result<Object> addGroupMember(String memberUsername, UUID groupId) {
        Result<Object> result = validateMember(memberUsername, groupId);

        if (!result.isSuccess()) {
            return result;
        }

        if (!REPOSITORY.addGroupMember(memberUsername, groupId)) {
            String msg = String.format("Member add to group %s failed", groupId);
            result.addMessage(msg, ResultType.INVALID);
        }

        return result;
    }

    public Result<Object> removeGroupMember(String memberUsername, UUID groupId) {
        Result<Object> result = validateMember(memberUsername, groupId);

        if (!result.isSuccess()) {
            return result;
        }

        if (!REPOSITORY.deleteGroupMember(memberUsername, groupId)) {
            String msg = String.format("Member remote from group %s failed", groupId);
            result.addMessage(msg, ResultType.INVALID);
        }

        return result;
    }

    private Result<Object> validateGroup(Group group) {
        Result<Object> result = new Result<>();
        if (group == null) {
            result.addMessage("Group cannot be null", ResultType.INVALID);
            return result;
        }

        if (group.getGROUP_ID() == null) {
            result.addMessage("Group ID is required", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(group.getName())) {
            result.addMessage("Group name is required", ResultType.INVALID);
        }

        if (group.getDescription() == null) {
            result.addMessage("Group description cannot be null", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(group.getOwner())) {
            result.addMessage("Group owner is required", ResultType.INVALID);
        }

        if (group.getGroupMembers() == null) {
            result.addMessage("Group members list is required", ResultType.INVALID);
        }

        if (group.getLibrary() == null) {
            result.addMessage("Group library is required", ResultType.INVALID);
        }

        return result;
    }

    private Result<Object> validateFields(String name, String description, String ownerUsername) {
        Result<Object> result = new Result<>();

        if (Validations.isNullOrBlank(name)) {
            result.addMessage("Group name is required", ResultType.INVALID);
        }

        if (description == null) {
            result.addMessage("Group description cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(ownerUsername)) {
            result.addMessage("Group onwer is required", ResultType.INVALID);
        }

        return result;
    }

    private Result<Object> validateMember(String memberUsername, UUID groupId) {
        Result<Object> result = new Result<>();

        if (Validations.isNullOrBlank(memberUsername)) {
            result.addMessage("Member username is required", ResultType.INVALID);
        }

        validateUUID(groupId, result);

        return result;
    }

    private void validateUUID(UUID value, Result<Object> result) {
        if (Validations.isNullOrBlank(value)) {
            result.addMessage("UUID cannot be null", ResultType.INVALID);
        }
    }
}