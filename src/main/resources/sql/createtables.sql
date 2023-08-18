drop table if exists calendar;
drop table if exists celestial_events;
drop table if exists months;
drop table if exists days;
drop table if exists moons;
drop table if exists suns;
drop table if exists inventory;
drop table if exists events_places_people;
drop table if exists quests;
drop table if exists hooks_places_people;
drop table if exists hooks;
drop table if exists conditionals;
drop table if exists objectives_places_people_items_weapons;
drop table if exists outcomes_places_people_items_weapons;
drop table if exists places;
drop table if exists terrains;
drop table if exists events;
drop table if exists cities;
drop table if exists settlement_types;
drop table if exists countries;
drop table if exists governments;
drop table if exists climates;
drop table if exists monsters;
drop table if exists outcomes;
drop table if exists rewards;
drop table if exists objectives;
drop table if exists landmarks;
drop table if exists continents;
drop table if exists place_types;
drop table if exists people_jobs;
drop table if exists people;
drop table if exists wealth;
drop table if exists races;
drop table if exists jobs;
drop table if exists items;
drop table if exists item_types;
drop table if exists weapons;
drop table if exists weapon_types;
drop table if exists weapon_damages;
drop table if exists damage_types;

create table months (
	id int generated always as identity,
	name varchar not null,
	primary key(id)
);

create table days (
	id int generated always as identity,
	name varchar not null,
	primary key(id)
);

create table moons (
	id int generated always as identity,
	name varchar not null,
	primary key(id)
);

create table suns (
	id int generated always as identity,
	name varchar not null,
	primary key(id)
);

create table celestial_events (
	id int generated always as identity,
	name varchar not null,
	constraint fk_moon foreign key(id) references moons(id),
	constraint fk_sun foreign key(id) references suns(id),
	primary key(id)
);

create table calendar (
	id int generated always as identity,
	year int not null,
	daysInYear int,
	daysInMonth int,
	daysInWeek int,
	constraint fk_month foreign key(id) references months(id),
	weekNum int,
	constraint fk_day foreign key(id) references days(id),
	constraint fk_celestial_events foreign key(id) references celestial_events(id),
	primary key(id)
);

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
	name varchar,
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
	constraint fk_wealth foreign key(id) references wealth(id),
	constraint fk_country foreign key(id) references countries(id),
	constraint fk_settlement foreign key(id) references settlement_types(id),
	constraint fk_government foreign key(id) references governments(id),
	primary key(id)
);

create table place_types(
	id int generated always as identity,
	name varchar not null,
	description varchar,
	primary key(id)
);

create table places (
	id int generated always as identity, 
	name varchar not null,
	description varchar,
	constraint fk_type foreign key(id) references place_types(id),
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
	is_exotic bool not null,
	primary key(id)
);

create table jobs (
	id int generated always as identity,
	name varchar not null,
	description varchar,
	primary key(id)
);

create table people (
	id int generated always as identity,
	firstName varchar not null,
	lastName varchar,
	age int,
	title varchar,
	constraint fk_race foreign key(id) references races(id),
	constraint fk_wealth foreign key(id) references wealth(id),
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
	goldValue numeric,
	silverValue numeric,
	copperValue numeric,
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
	goldValue numeric,
	silverValue numeric,
	copperValue numeric,
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
	constraint fk_event foreign key(id) references events(id),
	constraint fk_places foreign key(id) references places(id),
	primary key(id)
);

create table monsters (
	id int generated always as identity,
	name varchar not null,
	title varchar,
	isEnemy bool not null,
	personality varchar,
	description varchar,
	notes varchar,
	primary key(id)
);

create table hooks (
	id int generated always as identity,
	description varchar not null,
	notes varchar,
	primary key(id)
);

create table hooks_places_people (
	id int generated always as identity,
	constraint fk_hook foreign key(id) references hooks(id),
	constraint fk_person foreign key(id) references people(id),
	constraint fk_places foreign key(id) references places(id),
	primary key(id)
);

create table objectives (
	id int generated always as identity,
	description varchar not null,
	notes varchar,
	primary key(id)
);

create table objectives_places_people_items_weapons (
	id int generated always as identity,
	constraint fk_objective foreign key(id) references objectives(id),
	constraint fk_item foreign key(id) references items(id),
	constraint fk_weapon foreign key(id) references weapons(id),
	constraint fk_person foreign key(id) references people(id),
	constraint fk_places foreign key(id) references places(id),
	primary key(id)
);

create table outcomes (
	id int generated always as identity,
	description varchar not null,
	notes varchar,
	primary key(id)
);

create table outcomes_places_people_items_weapons (
	id int generated always as identity,
	constraint fk_outcome foreign key(id) references outcomes(id),
	constraint fk_item foreign key(id) references items(id),
	constraint fk_weapon foreign key(id) references weapons(id),
	constraint fk_person foreign key(id) references people(id),
	constraint fk_places foreign key(id) references places(id),
	primary key(id)
);

create table rewards (
	id int generated always as identity,
	description varchar not null,
	notes varchar,
	goldValue numeric,
	silverValue numeric,
	copperValue numeric,
	constraint fk_item foreign key(id) references items(id),
	constraint fk_weapon foreign key(id) references weapons(id),
	primary key(id)
);

create table conditionals (
	id int generated always as identity,
	description varchar not null,
	notes varchar,
	constraint fk_objectives_places_people_items_weapons foreign key(id) references objectives_places_people_items_weapons(id),
	wasObjectiveCompleted bool,
	constraint fk_outcomes_places_people_items_weapons foreign key(id) references outcomes_places_people_items_weapons(id),
	constraint fk_reward foreign key(id) references rewards(id),
	primary key(id)
);

create table quests (
	id int generated always as identity,
	description varchar not null,
	notes varchar,
	constraint fk_hooks_places_people foreign key(id) references hooks_places_people(id),
	constraint fk_conditional foreign key(id) references conditionals(id),
	constraint fk_reward foreign key(id) references rewards(id),
	primary key(id)
);
