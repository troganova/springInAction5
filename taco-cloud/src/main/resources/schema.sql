create table if not exists Ingredient (
    id varchar(4) default '1234' not null primary key,
    name varchar(25) not null,
    type varchar(10) not null
);
create table if not exists Taco (
    id identity,
    name varchar(50) not null,
    created_at timestamp not null
);
create table if not exists Taco_Ingredients (
    taco bigint not null,
    ingredient varchar(4) not null
);

alter table Taco_Ingredients add foreign key (taco) references Taco(id);
alter table Taco_Ingredients add foreign key (ingredient) references Ingredient(id);

create table if not exists Taco_Order (
    id identity,
    name varchar(50) not null,
    street varchar(50) not null,
    city varchar(50) not null,
    state varchar(2) not null,
    zip varchar(10) not null,
    ccNumber varchar(16) not null,
    ccExpiration varchar(5) not null,
    ccCVV varchar(3) not null,
    user_id bigint,
    placed_at timestamp not null
);
create table if not exists Taco_Order_Tacos (
    taco_order bigint not null,
    taco bigint not null
);
alter table Taco_Order_Tacos add foreign key (taco_order) references Taco_Order(id);
alter table Taco_Order_Tacos add foreign key (taco) references Taco(id);

create table if not exists Taco_User (
    id identity,
    username varchar(50),
    password varchar(50),
    fullname varchar(50),
    street varchar(50),
    city varchar(50),
    state varchar(50),
    zip varchar(50),
    phone_number varchar(255)
);

alter table Taco_Order add foreign key (user_id) references Taco_User(id);