CREATE TABLE IF NOT EXISTS products
(
    productId int primary key,
    title varchar(200),
    description varchar(200),
    price varchar(10),
    discount varchar(10),
    discounted_price varchar(10)

);