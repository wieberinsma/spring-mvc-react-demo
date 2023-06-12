package nl.han.rwd.srd.api.model;

import nl.han.rwd.srd.domain.user.validation.FieldUnique;

import javax.validation.constraints.NotBlank;

@FieldUnique(unique = "username", message = "Username already in use.")
public class RegistrationRequest
{
    private String firstName;

    private String lastname;

    private String username;

    private String password;

    @NotBlank(message = "First name must not be empty.")
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    @NotBlank(message = "Lastname must not be empty.")
    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    @NotBlank(message = "Username must not be empty.")
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @NotBlank(message = "Password must not be empty.")
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
