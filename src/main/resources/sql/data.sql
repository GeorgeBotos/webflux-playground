drop table if exists CUSTOMER_ORDER;
drop table if exists CUSTOMER;
drop table if exists PRODUCT;

create table CUSTOMER
(
	ID    int auto_increment primary key,
	NAME  varchar(100),
	EMAIL varchar(100)
);

create table PRODUCT
(
	ID          int auto_increment primary key,
	DESCRIPTION varchar(100),
	PRICE       int
);

create table CUSTOMER_ORDER
(
	ORDER_ID    uuid                     default random_uuid() primary key,
	CUSTOMER_ID int,
	PRODUCT_ID  int,
	AMOUNT      int,
	ORDER_DATE  timestamp with time zone default current_timestamp,
	foreign key (CUSTOMER_ID) references CUSTOMER (ID),
	foreign key (PRODUCT_ID) references PRODUCT (ID)
);

insert into CUSTOMER(NAME, EMAIL)
values ('sam', 'sam@gmail.com'),
	   ('mike', 'mike@gmail.com'),
	   ('jake', 'jake@gmail.com'),
	   ('emily', 'emily@example.com'),
	   ('sophia', 'sophia@example.com'),
	   ('liam', 'liam@example.com'),
	   ('olivia', 'olivia@example.com'),
	   ('noah', 'noah@example.com'),
	   ('ava', 'ava@example.com'),
	   ('ethan', 'ethan@example.com');

insert into PRODUCT(DESCRIPTION, PRICE)
values ('iphone 20', 1000),
	   ('iphone 18', 750),
	   ('ipad', 800),
	   ('mac pro', 3000),
	   ('apple watch', 400),
	   ('macbook air', 1200),
	   ('airpods pro', 250),
	   ('imac', 2000),
	   ('apple tv', 200),
	   ('homepod', 300);

-- Order 1: sam buys an iphone 20 & iphone 18
insert into CUSTOMER_ORDER (CUSTOMER_ID, PRODUCT_ID, AMOUNT, ORDER_DATE)
values (1, 1, 950, current_timestamp),
	   (1, 2, 850, current_timestamp);

-- Order 2: mike buys an iphone 20 and mac pro
insert into CUSTOMER_ORDER (CUSTOMER_ID, PRODUCT_ID, AMOUNT, ORDER_DATE)
values (2, 1, 975, current_timestamp),
	   (2, 4, 2999, current_timestamp);

-- Order 3: jake buys an iphone 18 & ipad
insert into CUSTOMER_ORDER (CUSTOMER_ID, PRODUCT_ID, AMOUNT, ORDER_DATE)
values (3, 2, 750, current_timestamp),
	   (3, 2, 775, current_timestamp);