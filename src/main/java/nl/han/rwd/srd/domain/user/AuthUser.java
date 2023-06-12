package nl.han.rwd.srd.domain.user;

import java.util.HashSet;
import java.util.Set;

public class AuthUser
{
    private String username;

    //TODO: Magic Strings
    private Set<String> roles = new HashSet<>();

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Set<String> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<String> roles)
    {
        this.roles = roles;
    }
}
