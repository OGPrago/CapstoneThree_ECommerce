package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;

import java.security.Principal;
import java.util.List;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    List<ShoppingCart> getCart(Principal principal);
    void addToCart(Principal principal, Product product);
    void updateItemQuantity(Principal principal, int productId, int quantity);
    void clearCart(int userId);
}
