package bookworm.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Member extends User {

    private static final String AUTHORITY_PREFIX = "ROLE_";

    BookUser bookUser;
    private Library library;
    private List<String> roles;


    public Member(BookUser bookUser, List<String> roles, boolean disabled) {

        super(bookUser.getUsername(), bookUser.getPassword(), !disabled, true, true, true, convertRolesToAuthorities(roles));

        this.bookUser = bookUser;
        this.library = new Library();
        this.roles = roles;
    }

    public Member(BookUser bookUser, Library library, List<String> roles, boolean disabled) {

        super(bookUser.getUsername(), bookUser.getPassword(), !disabled, true, true, true, convertRolesToAuthorities(roles));

        this.bookUser = bookUser;
        this.library = library;
        this.roles = roles;
    }

    public BookUser getBookUser() {
        return bookUser;
    }

    public void setBookUser(BookUser user) {
        this.bookUser = user;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }


    public static List<GrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
        for (String role : roles) {
            Assert.isTrue(!role.startsWith(AUTHORITY_PREFIX),
                    () ->
                            String.
                                    format("%s cannot start with %s (it is automatically added)",
                                            role, AUTHORITY_PREFIX));
            authorities.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + role));
        }
        return authorities;
    }

    public static List<String> convertAuthoritiesToRoles(Collection<GrantedAuthority> authorities) {
        return authorities.stream()
                .map(a -> a.getAuthority().substring(AUTHORITY_PREFIX.length()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Member member = (Member) o;
        return bookUser.equals(member.bookUser) && library.equals(member.library) && roles.equals(member.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bookUser, library, roles);
    }
}