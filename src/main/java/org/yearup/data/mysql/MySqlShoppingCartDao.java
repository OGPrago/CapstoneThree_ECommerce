package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao
{
    UserDao userDao;
    public MySqlShoppingCartDao(DataSource dataSource, UserDao userDao) {
        super(dataSource);
        this.userDao = userDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId)
    {
        String sql = "SELECT sc.*, p.* FROM shopping_cart sc " +
                "JOIN products p ON sc.product_id = p.product_id WHERE sc.user_id = ?";
        ShoppingCart cart = new ShoppingCart();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ShoppingCartItem item = mapRow(resultSet);
                cart.add(item);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cart;
    }

    @Override
    public List<ShoppingCart> getCart(Principal principal) {
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int userId = user.getId();

        return (List<ShoppingCart>) getByUserId(userId);
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

    private ShoppingCartItem mapRow(ResultSet resultSet) throws SQLException
    {
        int productId = resultSet.getInt("product_id");
        int quantity = resultSet.getInt("quantity");
        BigDecimal discountPercent = resultSet.getBigDecimal("discount_percent");

        Product product = new Product();
        product.setProductId(productId);
        product.setName(resultSet.getString("name"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setCategoryId(resultSet.getInt("category_id"));
        product.setDescription(resultSet.getString("description"));
        product.setColor(resultSet.getString("color"));
        product.setStock(resultSet.getInt("stock"));
        product.setImageUrl(resultSet.getString("image_url"));
        product.setFeatured(resultSet.getBoolean("featured"));

        ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setDiscountPercent(discountPercent);

        return item;
    }
}
