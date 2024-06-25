package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;

import java.security.Principal;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    ShoppingCart getCart(Principal principal);
    ShoppingCartDao addToCart(Product product);
    void updateItemQuantity(int productId, Product product);
    void deleteFromCart(int productId);
}
