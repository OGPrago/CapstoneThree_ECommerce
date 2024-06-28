# CapstoneThree E-Commerce

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [WIP/Bugs](#wipbugs)


## Project Overview

CapstoneThree E-Commerce is a full-stack simulated e-commerce application. 
It allows users to browse products, add items to a shopping cart, 
update profile info, 
and make purchases. Admin users can manage products, categories and view order details.

## Features

- User authentication and authorization
- Product listing and detail views
  - Search via:
    - category
    - price range
    - color
- Shopping cart functionality
- Order management
- Admin panel for product management

## Technologies Used

- **Backend:** Java, Spring Boot, MySQL
- **Frontend:** HTML, CSS, JavaScript
- **Database:** MySQL
- **Others:** Maven Folder Structure
- **Testing:** Postman

## WIP/Bugs

---

### Shopping Cart
Initially I had the ```getCart()``` working, but when I completed and implemented the other methods,
that function stopped working. The ```addToCart()``` was working a bit to an extent. It would work on Postman,
but not on the website; and would not show up in the cart, but would show up on the database. Also, encountered 505 errors
with ```updateItemQuantity()``` and ```clearCart()``` that I need to fix. I have narrowed it down to that it
seems the problems are coming from the controller class, and probably the annotations in them. 

### Update Profile
When testing using Postman, I was able to update profile information, however, when I would do it on the
website it would say it updated, but when checking the database it did not. 
    

