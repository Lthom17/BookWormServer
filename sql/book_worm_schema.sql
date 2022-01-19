drop database if exists book_worm;
create database book_worm;
use book_worm;


-- create tables and relationships

create table roles (
	role_id int primary key auto_increment,
    `name` varchar(25) not null
);

create table `member` (
	username varchar(100) primary key,
    email varchar(300) not null unique,
    first_name varchar(100),
    last_name varchar(100),
    library_id varchar(36) unique,
    password_hash varchar(2048) not null,
    disabled boolean not null default(0)
);

create table member_roles (
	role_id int not null,
    username varchar(100) not null,
    constraint pk_app_member_role
        primary key (role_id, username),
    constraint fk_app_member_role_role_id
        foreign key (role_id)
        references roles(role_id),
    constraint fk_app_member_role_username
        foreign key (username)
        references `member`(username)
    );

create table `group`  (
	group_id varchar(36) primary key,
    `name` varchar(100) not null,
    `description` tinytext not null,
    `owner` varchar(100) not null,
    library_id varchar(36) unique
);

create table group_members (
	member_username varchar(100),
    group_id varchar(36),
    constraint pk_group_members
        primary key (member_username, group_id),
    constraint fk_group_members_username
        foreign key (member_username)
        references `member`(username),
    constraint fk_group_members_group_id
        foreign key (group_id)
        references `group`(group_id)
);

create table bookshelf (
	bookshelf_id varchar(36) primary key,
    `name` varchar(250) not null,
    parent_id varchar(36),
    member_library_id varchar(36),
	group_library_id varchar(36),
    constraint fk_bookshelf_member_library_id
        foreign key (member_library_id)
        references `member`(library_id),
	constraint fk_bookshelf_group_library_id
		foreign key (group_library_id)
        references `group`(library_id)
);

create table book (
	isbn varchar(17) primary key,
    bookshelf_id varchar(36),
     constraint fk_book_bookshelf_id
        foreign key (bookshelf_id)
        references bookshelf(bookshelf_id)
);

insert into roles (`name`) values
    ('MEMBER'),
    ('GROUP_FOUNDER');


-- passwords are set to "P@ssw0rd!"
insert into `member` (username, email, first_name, last_name, library_id, password_hash, disabled)
    values
    ('JohnnyTest','john@smith.com','Johnny', 'Smith', '9f71448c-7326-11ec-90d6-0242ac120003', '$2a$12$Y61I/H1LH6RKqEzLX96k1.5pKFKKkSOUupcgJrdGn4DhHP5Mg/Uaa', 0),
    ('SallyJ23', 'sally@jones.com', 'Sally', 'Jones', '162a54ce-7327-11ec-90d6-0242ac120003', '$2a$12$Y61I/H1LH6RKqEzLX96k1.5pKFKKkSOUupcgJrdGn4DhHP5Mg/Uaa', 0),
	('Farmer123', 'bob@thefarm.com', 'Bob', 'Evans', 'ae5dcdb6-732c-11ec-90d6-0242ac120003', '$2a$12$Y61I/H1LH6RKqEzLX96k1.5pKFKKkSOUupcgJrdGn4DhHP5Mg/Uaa', 0), 
	('FriesNFrosties', 'wendy@wendys.com', 'Wendy', 'Thomas', 'ae5dd072-732c-11ec-90d6-0242ac120003', '$2a$12$Y61I/H1LH6RKqEzLX96k1.5pKFKKkSOUupcgJrdGn4DhHP5Mg/Uaa', 0);

insert into member_roles 
    values
    (2, 'JohnnyTest'),
    (2, 'SallyJ23'),
    (1, 'FriesNFrosties'),
    (1, 'Farmer123');
    

insert into `group` (group_id, `name`, `description`, `owner`, library_id)
values
('61526f94-7328-11ec-90d6-0242ac120003', 'The Ginger Elephants', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Mus mauris vitae ultricies leo integer.', 'JohnnyTest', '61526bb6-7328-11ec-90d6-0242ac120003'),
('61526a9e-7328-11ec-90d6-0242ac120003', 'The Scotland Saints',  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Mus mauris vitae ultricies leo integer.', 'SallyJ23', '615269a4-7328-11ec-90d6-0242ac120003');


insert into group_members
values
('JohnnyTest','61526f94-7328-11ec-90d6-0242ac120003'),
('Farmer123', '61526f94-7328-11ec-90d6-0242ac120003'), 
('SallyJ23',  '61526a9e-7328-11ec-90d6-0242ac120003'),
('FriesNFrosties', '61526a9e-7328-11ec-90d6-0242ac120003');


-- JohnnyTest member bookshelf
insert into bookshelf (bookshelf_id, `name`, parent_id, member_library_id)
values
('615272a0-7328-11ec-90d6-0242ac120003', 'Fantasy Worlds with Dragons','', '9f71448c-7326-11ec-90d6-0242ac120003'),
('6152719c-7328-11ec-90d6-0242ac120003', 'Outer Space Murder Mysteries', '',  '9f71448c-7326-11ec-90d6-0242ac120003');

-- bookshelf with a parent ('Fantasy Worlds with Dragons')
insert into bookshelf (bookshelf_id, `name`, parent_id, member_library_id)
values
('615270a2-7328-11ec-90d6-0242ac120003', 'Dragon Protagonist', '615272a0-7328-11ec-90d6-0242ac120003', '9f71448c-7326-11ec-90d6-0242ac120003');

-- bookshelf for The Scotland Saints group library
insert into bookshelf (bookshelf_id, `name`, parent_id, group_library_id)
values
('61526882-7328-11ec-90d6-0242ac120003','Castles of Scotland', '', '61526bb6-7328-11ec-90d6-0242ac120003');

-- bookshelf with a parent ('Castles of Scotland') for group
insert into bookshelf (bookshelf_id, `name`, parent_id, group_library_id)
values
('61526760-7328-11ec-90d6-0242ac120003','Ghost Stories of Scottish Castles', '61526882-7328-11ec-90d6-0242ac120003', '61526bb6-7328-11ec-90d6-0242ac120003');

insert into bookshelf (bookshelf_id, `name`, parent_id, group_library_id)
values
('615264fe-7328-11ec-90d6-0242ac120003', 'Combat Sports', '', '61526bb6-7328-11ec-90d6-0242ac120003');

-- bookshelves for 'The Ginger Elephants' 
insert into bookshelf (bookshelf_id, `name`, parent_id, group_library_id)
values
('dc95af08-6669-400f-a401-a3064cffc0b9', 'Brit Lit', '', '61526bb6-7328-11ec-90d6-0242ac120003'), 
('466e2102-62f2-4b68-ad97-37365b9d3c54', 'Civil War', '', '61526bb6-7328-11ec-90d6-0242ac120003'), 
('8fe0dc4c-4bf0-45b1-8349-33186543faff', 'British Playwrights', 'dc95af08-6669-400f-a401-a3064cffc0b9', '61526bb6-7328-11ec-90d6-0242ac120003');


-- JohnnyTest bookshelf 'Fantasy Worlds with Dragons'
insert into book (isbn, bookshelf_id)
values
('9780618134700', '615272a0-7328-11ec-90d6-0242ac120003'), -- The Hobbit
('9781507844823', '615272a0-7328-11ec-90d6-0242ac120003'), -- A Game of Thrones
('9780593044650', '615272a0-7328-11ec-90d6-0242ac120003'); -- Dragonflight

-- JohnnyTest bookshelf child 'Dragon Protagonist' of 'Fantasy Worlds wiith Dragons'
insert into book (isbn, bookshelf_id)
values
('9780545349208' ,'615270a2-7328-11ec-90d6-0242ac120003'), -- Wings of Fire: The Hidden Kingdom
('0439799295' ,'615270a2-7328-11ec-90d6-0242ac120003'); -- Dragon Keeper
 
 -- The Ginger Elephants bookshelves 
insert into book (isbn, bookshelf_id)
values
('9780425123348', 'dc95af08-6669-400f-a401-a3064cffc0b9'), -- A Christmas Carol
('9780140430479', 'dc95af08-6669-400f-a401-a3064cffc0b9'), -- Sense and Sensibility
('9780140707342', '8fe0dc4c-4bf0-45b1-8349-33186543faff'), -- Hamlet
('9780306810619', '466e2102-62f2-4b68-ad97-37365b9d3c54'); -- Personal Memoirs of U.S. Grant

	