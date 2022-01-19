package bookworm.domain;

import bookworm.data.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class GroupServiceTest {

    @Autowired
    GroupService service;

    @MockBean
    GroupRepository repository;
}
