drop table if exists celestial_events;
drop table if exists events_places_people;
drop table if exists events;
drop table if exists days;
drop table if exists weeks;
drop table if exists months;
drop table if exists moons;
drop table if exists suns;
drop table if exists inventory;
drop table if exists quests;
drop table if exists hooks_places_people;
drop table if exists hooks;
drop table if exists conditionals;
drop table if exists objectives_places_people_items_weapons;
drop table if exists outcomes_places_people_items_weapons;
drop table if exists places;
drop table if exists terrains;
drop table if exists cities;
drop table if exists settlement_types;
drop table if exists landmarks;
drop table if exists regions;
drop table if exists countries;
drop table if exists governments;
drop table if exists climates;
drop table if exists monsters;
drop table if exists outcomes;
drop table if exists rewards;
drop table if exists objectives;
drop table if exists continents;
drop table if exists place_types;
drop table if exists people_jobs;
drop table if exists people;
drop table if exists ability_scores;
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
	description varchar,
	season varchar,
	primary key(id)
);

create table weeks (
    id int generated always as identity,
    description varchar,
    week_number int not null,
    fk_month int not null,
    constraint fk_month foreign key(fk_month) references months(id),
    primary key(id)
);

create table days (
	id int generated always as identity,
	name varchar not null,
	description varchar,
	fk_week int not null,
	constraint fk_week foreign key(fk_week) references weeks(id),
	primary key(id)
);

create table moons (
	id int generated always as identity,
	name varchar not null,
	description varchar,
	primary key(id)
);

create table suns (
	id int generated always as identity,
	name varchar not null,
	description varchar,
	primary key(id)
);

create table celestial_events (
	id int generated always as identity,
	name varchar not null,
	description varchar,
	fk_moon int,
	fk_sun int,
	fk_month int,
	fk_week int,
	fk_day int,
	event_year int,
	constraint fk_moon foreign key(fk_moon) references moons(id),
	constraint fk_sun foreign key(fk_sun) references suns(id),
	constraint fk_month foreign key(fk_month) references months(id),
	constraint fk_week foreign key(fk_week) references weeks(id),
	constraint fk_day foreign key(fk_day) references days(id),
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
	fk_continent int,
	fk_government int,
	constraint fk_continent foreign key(fk_continent) references continents(id),
	constraint fk_government foreign key(fk_government) references governments(id),
	primary key(id)
);

create table place_types(
	id int generated always as identity,
	name varchar not null,
	description varchar,
	primary key(id)
);

create table regions (
    id int generated always as identity,
    name varchar not null,
    description varchar,
    fk_country int,
    fk_climate int,
    constraint fk_country foreign key(fk_country) references countries(id),
    constraint fk_climate foreign key(fk_climate) references climates(id),
    primary key(id)
);

create table cities (
	id int generated always as identity,
	name varchar not null,
	description varchar,
	fk_wealth int,
	fk_country int,
	fk_settlement int,
	fk_government int,
	fk_region int,
	constraint fk_wealth foreign key(fk_wealth) references wealth(id),
	constraint fk_country foreign key(fk_country) references countries(id),
	constraint fk_settlement foreign key(fk_settlement) references settlement_types(id),
	constraint fk_government foreign key(fk_government) references governments(id),
	constraint fk_region foreign key(fk_region) references regions(id),
	primary key(id)
);

create table landmarks (
	id int generated always as identity,
	name varchar not null,
	description varchar not null,
	fk_region int,
	constraint fk_region foreign key(fk_region) references regions(id),
	primary key(id)
);

create table places (
	id int generated always as identity,
	name varchar not null,
	description varchar,
	fk_place_type int,
    fk_terrain int,
    fk_country int,
    fk_city int,
    fk_region int,
	constraint fk_place_type foreign key(fk_place_type) references place_types(id),
	constraint fk_terrain foreign key(fk_terrain) references terrains(id),
	constraint fk_country foreign key(fk_country) references countries(id),
	constraint fk_city foreign key(fk_city) references cities(id),
	constraint fk_region foreign key(fk_region) references regions(id),
	primary key(id)
);

create table events (
	id int generated always as identity,
	name varchar not null,
	description varchar not null,
	event_year int,
	fk_month int,
	fk_week int,
	fk_day int,
	fk_city int,
    fk_continent int,
    fk_country int,
    constraint fk_month foreign key(fk_month) references months(id),
    constraint fk_week foreign key(fk_week) references weeks(id),
    constraint fk_day foreign key(fk_day) references days(id),
	constraint fk_city foreign key(fk_city) references cities(id),
	constraint fk_continent foreign key(fk_continent) references continents(id),
	constraint fk_country foreign key(fk_country) references countries(id),
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

create table ability_scores (
    id int generated always as identity,
    strength int not null,
    dexterity int not null,
    constitution int not null,
    intelligence int not null,
    wisdom int not null,
    charisma int not null,
    primary key(id)
);

create table people (
	id int generated always as identity,
	firstName varchar not null,
	lastName varchar,
	age int,
	title varchar,
	fk_race int,
    fk_wealth int,
    fk_ability_score int,
	isNPC bool not null,
	isEnemy bool not null,
	personality varchar,
	description varchar,
	notes varchar,
	constraint fk_race foreign key(fk_race) references races(id),
    constraint fk_wealth foreign key(fk_wealth) references wealth(id),
    constraint fk_ability_score foreign key(fk_ability_score) references ability_scores(id),
	primary key(id)
);

create table people_jobs (
	id int generated always as identity,
	fk_person int,
    fk_job int,
	constraint fk_person foreign key(fk_person) references people(id),
	constraint fk_job foreign key(fk_job) references jobs(id),
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
	fk_type int,
	constraint fk_type foreign key(fk_type) references item_types(id),
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
	fk_type int,
	constraint fk_type foreign key(fk_type) references weapon_types(id),
	isMagical bool,
	isCursed bool,
	notes varchar,
	primary key(id)
);

create table weapon_damages (
	id int generated always as identity,
	amount int,
	fk_type int,
	constraint fk_type foreign key(fk_type) references damage_types(id),
	primary key(id)
);

create table inventory (
	id int generated always as identity,
	fk_person int,
    fk_item int,
    fk_weapon int,
    fk_place int,
	constraint fk_person foreign key(fk_person) references people(id),
	constraint fk_item foreign key(fk_item) references items(id),
	constraint fk_weapon foreign key(fk_weapon) references weapons(id),
	constraint fk_place foreign key(fk_place) references places(id),
	primary key(id)
);

create table events_places_people (
	id int generated always as identity,
	fk_person int,
    fk_event int,
    fk_place int,
	constraint fk_person foreign key(fk_person) references people(id),
	constraint fk_event foreign key(fk_event) references events(id),
	constraint fk_place foreign key(fk_place) references places(id),
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
	fk_hook int,
    fk_person int,
    fk_place int,
	constraint fk_hook foreign key(fk_hook) references hooks(id),
	constraint fk_person foreign key(fk_person) references people(id),
	constraint fk_place foreign key(fk_place) references places(id),
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
	fk_objective int,
    fk_item int,
    fk_weapon int,
    fk_person int,
    fk_place int,
	constraint fk_objective foreign key(fk_objective) references objectives(id),
	constraint fk_item foreign key(fk_item) references items(id),
	constraint fk_weapon foreign key(fk_weapon) references weapons(id),
	constraint fk_person foreign key(fk_person) references people(id),
	constraint fk_place foreign key(fk_place) references places(id),
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
	fk_outcome int,
    fk_item int,
    fk_weapon int,
    fk_person int,
    fk_place int,
	constraint fk_outcome foreign key(fk_outcome) references outcomes(id),
	constraint fk_item foreign key(fk_item) references items(id),
	constraint fk_weapon foreign key(fk_weapon) references weapons(id),
	constraint fk_person foreign key(fk_person) references people(id),
	constraint fk_place foreign key(fk_place) references places(id),
	primary key(id)
);

create table rewards (
	id int generated always as identity,
	description varchar not null,
	notes varchar,
	goldValue numeric,
	silverValue numeric,
	copperValue numeric,
	fk_item int,
    fk_weapon int,
	constraint fk_item foreign key(fk_item) references items(id),
	constraint fk_weapon foreign key(fk_weapon) references weapons(id),
	primary key(id)
);

create table conditionals (
	id int generated always as identity,
	description varchar not null,
	notes varchar,
	wasObjectiveCompleted bool,
	fk_objectives_places_people_items_weapons int,
    fk_outcomes_places_people_items_weapons int,
    fk_reward int,
	constraint fk_objectives_places_people_items_weapons foreign key(fk_objectives_places_people_items_weapons) references objectives_places_people_items_weapons(id),
	constraint fk_outcomes_places_people_items_weapons foreign key(fk_outcomes_places_people_items_weapons) references outcomes_places_people_items_weapons(id),
	constraint fk_reward foreign key(fk_reward) references rewards(id),
	primary key(id)
);

create table quests (
	id int generated always as identity,
	description varchar not null,
	notes varchar,
	fk_hooks_places_people int,
    fk_conditional int,
    fk_reward int,
	constraint fk_hooks_places_people foreign key(fk_hooks_places_people) references hooks_places_people(id),
	constraint fk_conditional foreign key(fk_conditional) references conditionals(id),
	constraint fk_reward foreign key(fk_reward) references rewards(id),
	primary key(id)
);
