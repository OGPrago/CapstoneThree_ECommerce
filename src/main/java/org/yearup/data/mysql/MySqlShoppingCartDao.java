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

    public MySqlShoppingCartDao(DataSource dataSource, UserDao userDao)
    {
        super(dataSource);
        this.userDao = userDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId)
    {
        String sql = "SELECT sc.*, p.* FROM shopping_cart sc " +
                "JOIN products p ON sc.product_id = p.product_id WHERE sc.user_id = ?";
        ShoppingCart cart = new ShoppingCart();

        try (
             Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        )
        {
            statement.setInt(1, userId);

            try (
                    ResultSet resultSet = statement.executeQuery();
            )
            {
                while (resultSet.next())
                {
                    ShoppingCartItem item = mapRow(resultSet);
                    cart.add(item);
                }

            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return cart;
    }

    @Override
    public List<ShoppingCart> getCart(Principal principal)
    {
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int userId = user.getId();

        return (List<ShoppingCart>) getByUserId(userId);
    }

    @Override
    public void addToCart(Principal principal, Product product)
    {
        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + 1";

        String username = principal.getName();
        User user = userDao.getByUserName(username);
        int userId = user.getId();

        try (
             Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        )
        {
            ps.setInt(1, userId);
            ps.setInt(2, product.getProductId());
            ps.setInt(3, 1);

            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }


    @Override
    public void updateItemQuantity(Principal principal, int productId, int quantity)
    {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE product_id = ? AND user_id = ?";

        try (
             Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        )
        {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, getUserId(principal));

            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (
             Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        )
        {
            ps.setInt(1, userId);

            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
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

    private int getUserId(Principal principal)
    {
        String username = principal.getName();
        User user = userDao.getByUserName(username);
        return user.getId();
    }
}
