package org.jwt.userManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jwt.userManagement.enumeration.AccountStatus;
import org.jwt.userManagement.enumeration.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String username;
    @Lob
    private byte[] profileImage;
    @Transient
    private String profileImageInBase64;
    private String password;
    private Status status;
    private Integer authAttempts;
    private AccountStatus accountStatus;
    @ManyToOne(fetch = EAGER)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities
                = role.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getPermission())).collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s", role.getName())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountStatus == AccountStatus.NON_LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == Status.ACTIVE;
    }
}
