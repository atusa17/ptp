package edu.msudenver.tsp.website.forms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreationForm {
    private int userID;
    private String username;
    private String password;
    private String confirmPassword;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String referrer; // optional
    private boolean agreedToTerms;
}
