package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("cart")
@CrossOrigin
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;
    private ShoppingCartItem shoppingCartItem = new ShoppingCartItem();

    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao)
    {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }


    // each method in this controller requires a Principal object as a parameter
    @GetMapping("")
    public ShoppingCart getCart(Principal principal)
    {
        try {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shoppingcartDao to get all items in the cart and return the cart
            return shoppingCartDao.getByUserId(userId);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping("products/{productId}")
    public void addToCart(@PathVariable int productId, Principal principal)
    {
        try
        {
            Product product = productDao.getById(productId);

            if (product == null)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
            }
            shoppingCartDao.addToCart(principal, product);
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.", ex);
        }
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping("products/{productId}")
    public void updateItemQuantity(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal)
    {
        try
        {
            shoppingCartDao.updateItemQuantity(principal, productId, item.getQuantity());
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.", ex);
        }
    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    @DeleteMapping("")
    public void clearCart(int userId)
    {
        try
        {
            shoppingCartDao.clearCart(userId);
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.", ex);
        }
    }
}
