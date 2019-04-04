package edu.msudenver.tsp.website.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserCreationForm {
    @NotNull
    @NotEmpty
    private int userID;

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String confirmPassword;

    public Boolean checkPassword(final String passwordInput, final String passwordCheck) {
        if(!passwordInput.equals(passwordCheck)) {
            return false;
        }
        else return true;
    }

    @NotNull
    @NotEmpty
    private String emailAddress;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    private String referrer; // optional

    @NotNull
    @NotEmpty
    private boolean agreedToTerms;
}
