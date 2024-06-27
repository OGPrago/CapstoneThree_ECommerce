package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("profile")
@PreAuthorize("isAuthenticated()")
@CrossOrigin
public class ProfileController
{
    private final ProfileDao profileDao;
    private final UserDao userDao;

    public ProfileController(ProfileDao profileDao, UserDao userDao)
    {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @GetMapping("")
    public Profile getProfile(Principal principal)
    {
        try
        {
            // Get the currently logged in username
            String userName = principal.getName();

            // Find the user by username
            User user = userDao.getByUserName(userName);

            if (user == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");

            // Use the profileDao to get the user's profile by user ID
            Profile profile = profileDao.getByUserId(user.getId());

            if (profile == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found.");

            return profile;
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
