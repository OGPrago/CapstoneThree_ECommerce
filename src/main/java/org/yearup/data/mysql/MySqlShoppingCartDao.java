package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;

import javax.sql.DataSource;
import java.security.Principal;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        return null;
    }

    @Override
    public ShoppingCart getCart(Principal principal) {
        return null;
    }

    @Override
    public ShoppingCartDao addToCart(Product product) {
        return null;
    }

    @Override
    public void updateItemQuantity(int productId, Product product) {

    }

    @Override
    public void deleteFromCart(int productId) {

    }
}
