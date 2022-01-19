package bookworm.domain;

import bookworm.data.BookshelfRepository;
import bookworm.data.MemberRepository;
import bookworm.models.Library;
import bookworm.models.Member;
import bookworm.models.Result;
import bookworm.models.ResultType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class MemberService implements UserDetailsService {

    private BookshelfRepository bookshelfRepo;
    private MemberRepository memberRepository;
    private LibraryService libraryService;
    private final PasswordEncoder encoder;

    public MemberService(BookshelfRepository bookshelfRepo, MemberRepository memberRepository,
                         LibraryService libraryService,
                         PasswordEncoder encoder) {
        this.bookshelfRepo = bookshelfRepo;
        this.memberRepository = memberRepository;
        this.libraryService = libraryService;
        this.encoder = encoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username);

        if (member == null || !member.isEnabled()) {
            throw new UsernameNotFoundException(username + "not found");
        }

        return member;
    }

    public Result<Member> findByUsername(String username) {
        Result result = validate(username);

        if (!result.isSuccess()) {
            return result;
        }

        Member member = memberRepository.findByUsername(username);

        if (member == null) {
            result.addMessage("That username was not found in our database.", ResultType.NOT_FOUND);
            return result;
        }

        Library library = libraryService.buildLibrary(member.getLibrary().getLibraryId());

        member.setLibrary(library);

        result.setPayload(member);

        return result;

    }

    public Result<Member> add(Member member) {

        Result result = validate(member.getUsername());

        if (!result.isSuccess()) {
            return result;
        }
        result = validatePassword(member.getPassword());

        if (!result.isSuccess()) {
            return result;
        }

        result = validateEmail(member.getBookUser().getEmail());

        if (!result.isSuccess()) {
            return result;
        }

        String password_hash = encoder.encode(member.getPassword());
        member.getBookUser().setPassword(password_hash);
        Member memberToAdd = new Member(member.getBookUser(), member.getLibrary(), List.of("MEMBER"), false);

        result.setPayload(memberRepository.add(memberToAdd));

        return result;
    }


    private Result validate(String username) {
        Result result = new Result();
        if (username == null || username.isBlank()) {
            result.addMessage("username is required", ResultType.INVALID);
            return result;
        }

        if (username.length() > 50) {
            result.addMessage("username must be less than 50 characters", ResultType.INVALID);
            return result;
        }

        return result;
    }

    private Result validatePassword(String password) {

        Result result = new Result();
        if (password == null || password.length() < 8) {
            result.addMessage("password must be at least 8 characters", ResultType.INVALID);
            return result;
        }

        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        if (digits == 0 || letters == 0 || others == 0) {
            result.addMessage("password must contain a digit, a letter, and a non-digit/non-letter", ResultType.INVALID);
            return result;
        }

        return result;
    }

    private Result validateEmail(String email) {
        String regexPattern = "^(.+)@(\\S+)$";
        boolean matches = Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
        Result result = new Result();
        if (!matches) {
            result.addMessage("Invalid email address.", ResultType.INVALID);
        }
        return result;
    }

}