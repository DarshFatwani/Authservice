package org.example.service;

import org.example.entities.UserInfo;
import org.example.entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//UserDetails has methods that needs to be implemented and overriden
//Spring Security’s login mechanism always works with UserDetails, not your custom UserInfo entity directly.
/*Spring Security is a framework that handles authentication (who are you?) and authorization (what can you do?).
When a user logs into your app:
They enter a username and password.
Spring Security needs to fetch that user from the database.
It needs to know their roles/permissions.
But Spring Security doesn’t know about your custom UserInfo entity. It only understands a very specific format: UserDetails.
So we must adapt our entity into a format that Spring Security understands. That’s where CustomUserDetails comes in.*/
/*iSimilarly, Spring Security defines an interface:
public interface UserDetails {
    String getUsername();
    String getPassword();
    Collection<? extends GrantedAuthority> getAuthorities();
    boolean isAccountNonExpired();
    boolean isAccountNonLocked();
    boolean isCredentialsNonExpired();
    boolean isEnabled();
}

This is the contract.
If we make a class that implements UserDetails, then Spring can talk to it.*/

public class CustomUserDetails extends UserInfo
        implements UserDetails
{

    private String username;
    private String password;
    Collection<? extends GrantedAuthority> authorities;
/*GrantedAuthority is an interface, not an enum. It represents a single authority, like a role or a permission. Spring provides a simple implementation called SimpleGrantedAuthority, which just wraps a string such as ROLE_ADMIN or READ_REPORTS.
The actual role names are not predefined in Spring Security — we define them ourselves, either in the database or in configuration.
The only convention is that roles usually start with ROLE_
 */

    public CustomUserDetails(UserInfo byUsername) {
        this.username = byUsername.getUsername();
        this.password= byUsername.getPassword();
        List<GrantedAuthority> auths = new ArrayList<>();

        for(UserRole role : byUsername.getRoles()){
            auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
        }
        this.authorities = auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
