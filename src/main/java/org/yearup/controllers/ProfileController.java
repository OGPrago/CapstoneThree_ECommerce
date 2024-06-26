package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

@RestController
@RequestMapping("profile")
@PreAuthorize("isAuthenticated()")
public class ProfileController
{
    private ProfileDao profileDao;

    public ProfileController(ProfileDao profileDao)
    {
        this.profileDao = profileDao;
    }

    @GetMapping
    public Profile getByUserId(@PathVariable int id)
    {
        try
        {
            var product = profileDao.getByUserId(id);

            if (product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            return product;
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
