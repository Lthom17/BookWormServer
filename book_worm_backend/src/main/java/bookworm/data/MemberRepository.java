package bookworm.data;

import bookworm.models.Member;

public interface MemberRepository {


    Member findByUsername(String username);

    Member add(Member member);

    void update(Member member);
}