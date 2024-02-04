delete from Taco_Order_Tacos;
delete from Taco_Ingredients;
delete from Taco;
delete from Taco_Order;
delete from Ingredient;
delete from Taco_User;

insert into Ingredient (id, name, type) values ('FLTO', 'Flour Tortilla', 'WRAP');
insert into Ingredient (id, name, type) values ('COTO', 'Corn Tortilla', 'WRAP');
insert into Ingredient (id, name, type) values ('GRBF', 'Ground Beef', 'PROTEIN');
insert into Ingredient (id, name, type) values ('CARN', 'Carnitas', 'PROTEIN');
insert into Ingredient (id, name, type) values ('TMTO', 'Diced Tomatoes', 'VEGGIES');
insert into Ingredient (id, name, type) values ('LETC', 'Lettuce', 'VEGGIES');
insert into Ingredient (id, name, type) values ('CHED', 'Cheddar', 'CHEESE');
insert into Ingredient (id, name, type) values ('JACK', 'Monterrey Jack', 'CHEESE');
insert into Ingredient (id, name, type) values ('SLSA', 'Salsa', 'SAUCE');
insert into Ingredient (id, name, type) values ('SRCR', 'Sour Cream', 'SAUCE');

insert into Taco_User(id, username, password, fullname, street, city, state, zip, phone_number) values (1, 'habuma', '$2a$10$QB3tCvm5sXDPLyyCrXRIXu4FURqxpYigI6LVDGW2UEebV92vHV26e', 'Craig Walls', '123 North Street', 'Cross Roads', 'TX', '76227', '123-123-1234');

insert into Taco (id, name, created_at) values (1, 'Carnivore', '2024-01-05');
insert into Taco (id, name, created_at) values (2, 'Bovine Bounty', '2024-01-06');
insert into Taco (id, name, created_at) values (3, 'Veg-Out', '2024-01-07');

insert into Taco_Ingredients  values (1, 'FLTO');
insert into Taco_Ingredients  values (1, 'GRBF');
insert into Taco_Ingredients  values (1, 'CARN');
insert into Taco_Ingredients  values (1, 'SRCR');
insert into Taco_Ingredients  values (1, 'SLSA');
insert into Taco_Ingredients  values (1, 'CHED');

insert into Taco_Ingredients  values (2, 'COTO');
insert into Taco_Ingredients  values (2, 'GRBF');
insert into Taco_Ingredients  values (2, 'CHED');
insert into Taco_Ingredients  values (2, 'JACK');
insert into Taco_Ingredients  values (2, 'SRCR');

insert into Taco_Ingredients  values (3, 'FLTO');
insert into Taco_Ingredients  values (3, 'COTO');
insert into Taco_Ingredients  values (3, 'TMTO');
insert into Taco_Ingredients  values (3, 'LETC');
insert into Taco_Ingredients  values (3, 'SLSA');


--4000001234567899 credit card

