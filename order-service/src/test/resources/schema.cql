
CREATE KEYSPACE IF NOT EXISTS test2 WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'} and durable_writes=true;

use test2;
create type address (address text, postalcode text, city text, province text, country text);
create type product (product_id text, product_name text, image text, product_brand text, product_size text, product_color text, product_qte int, unit_price decimal, store text);
create type payment_info (no_credit_card text, exp_date text, security_code int, last_name text, first_name text);
create table orders (order_id text, products list<frozen <product>>, product_name text, product_brand text, total decimal, user_email text, payment_info payment_info, user_address address, order_status text, order_time timestamp, payment_status text, payment_time timestamp, shipping_status text, shipping_time timestamp,tracking_number text, primary key ((user_email), order_status, order_time)) with clustering order by (order_status ASC, order_time DESC);
create table orders_by_orderId (order_id text, products list<frozen <product>>, product_name text, product_brand text, total decimal, user_email text, payment_info payment_info, user_address address, order_status text, order_time timestamp, payment_status text, payment_time timestamp, shipping_status text, shipping_time timestamp, tracking_number text, primary key ((order_id), user_email, order_time)) with clustering order by (user_email ASC, order_time DESC);
create table orders_status (order_id text, products list<frozen <product>>, product_name text, product_brand text, total decimal, user_email text, payment_info payment_info, user_address address, order_status text, order_time timestamp, payment_status text, payment_time timestamp, shipping_status text, shipping_time timestamp, tracking_number text, primary key ((order_status), order_time)) with clustering order by (order_time DESC);

