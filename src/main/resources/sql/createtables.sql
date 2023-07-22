drop table if exists terrains;
drop table if exists settlement_types;
drop table if exists governments;
drop table if exists climates;
drop table if exists wealth;
drop table if exists landmarks;
drop table if exists continents;
drop table if exists countries;
drop table if exists cities;
drop table if exists place_types;
drop table if exists places;
drop table if exists events;
drop table if exists races;
drop table if exists jobs;
drop table if exists people;
drop table if exists people_jobs;
drop table if exists item_types;
drop table if exists items;
drop table if exists weapon_types;
drop table if exists damage_types;
drop table if exists weapons;
drop table if exists weapon_damages;
drop table if exists inventory;
drop table if exists events_places_people;

create table terrains (
	id int generated always as identity, 
	name varchar not null,
	description varchar not null,
	primary key(id)
);

create table settlement_types (
	id int generated always as identity, 
	name varchar not null,
	description varchar not null,
	primary key(id)
);

create table governments (
	id int generated always as identity, 
	name varchar not null,
	description varchar not null,
	primary key(id)
);

create table climates (
	id int generated always as identity, 
	name varchar not null,
	description varchar not null,
	primary key(id)
);

create table wealth (
	id int generated always as identity, 
	name varchar not null,
	primary key(id)
);

create table landmarks (
	id int generated always as identity, 
	name varchar not null,
	description varchar not null,
	primary key(id)
);

create table continents (
	id int generated always as identity,
	description varchar,
	primary key(id)
);

create table countries (
	id int generated always as identity, 
	name varchar not null,
	description varchar,
	constraint fk_continent foreign key(id) references continents(id),
	constraint fk_government foreign key(id) references governments(id),
	primary key(id)
);

create table cities (
	id int generated always as identity, 
	name varchar not null,
	description varchar,
	constraint fk_country foreign key(id) references countries(id),
	constraint fk_settlement foreign key(id) references settlement_types(id),
	constraint fk_government foreign key(id) references governments(id),
	primary key(id)
);

create table place_types(
	id int generated always as identity,
	name varchar not null,
	description varchar,
);

create table places (
	id int generated always as identity, 
	name varchar not null,
	description varchar,
	constraint fk_type foreign key(id) references place_types(id) not null,
	constraint fk_terrain foreign key(id) references terrains(id),
	constraint fk_country foreign key(id) references countries(id),
	constraint fk_city foreign key(id) references cities(id),
	primary key(id)
);

create table events (
	id int generated always as identity,
	description varchar not null,
	year int,
	month varchar,
	day int,
	constraint fk_city foreign key(id) references cities(id),
	constraint fk_continent foreign key(id) references continents(id),
	constraint fk_country foreign key(id) references countries(id),
	primary key(id)
);

create table races (
	id int generated always as identity,
	name varchar not null,
	description varchar,
	isExotic bool not null,
	primary key(id)
);

create table jobs (
	id int generated always as identity,
	name varchar not null,
	description varchar
	primary key(id)
);

create table people (
	id int generated always as identity,
	firstName varchar not null,
	lastName varchar,
	age int,
	title varchar,
	constraint fk_race foreign key(id) references races(id),
	isNPC bool not null,
	isEnemy bool not null,
	personality varchar,
	description varchar,
	notes varchar,
	primary key(id)
);

create table people_jobs (
	id int generated always as identity,
	constraint fk_person foreign key(id) references people(id),
	constraint fk_job foreign key(id) references jobs(id),
	primary key(id)
);

create table item_types (
	id int generated always as identity,
	name varchar,
	description varchar,
	primary key(id)
);

create table items (
	id int generated always as identity,
	name varchar not null,
	description varchar,
	value numeric,
	weight numeric,
	constraint fk_type foreign key(id) references item_types(id),
	isMagical bool,
	isCursed bool,
	notes varchar,
	primary key(id)
);

create table weapon_types (
	id int generated always as identity,
	name varchar,
	description varchar,
	primary key(id)
);

create table damage_types (
	id int generated always as identity,
	name varchar,
	description varchar,
	primary key(id)
);

create table weapons (
	id int generated always as identity,
	name varchar not null,
	description varchar,
	value numeric,
	weight numeric,
	constraint fk_type foreign key(id) references weapon_types(id),
	isMagical bool,
	isCursed bool,
	notes varchar,
	primary key(id)
);

create table weapon_damages (
	id int generated always as identity,
	amount int,
	constraint fk_type foreign key(id) references damage_types(id),
	primary key(id)
);

create table inventory (
	id int generated always as identity,
	constraint fk_person foreign key(id) references people(id),
	constraint fk_item foreign key(id) references items(id),
	constraint fk_weapon foreign key(id) references weapons(id),
	constraint fk_place foreign key(id) references places(id),
	primary key(id)
);

create table events_places_people (
	id int generated always as identity,
	constraint fk_person foreign key(id) references people(id),
	constraint fk_places foreign key(id) references places(id),
	constraint fk_city foreign key(id) references cities(id),
	constraint fk_continent foreign key(id) references continents(id),
	constraint fk_country foreign key(id) references countries(id),
	primary key(id)
);
