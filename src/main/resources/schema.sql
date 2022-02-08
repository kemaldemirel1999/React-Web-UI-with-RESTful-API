

CREATE TABLE URL(
    url varchar(200) not null,
    spider_id int not null,
    id  int PRIMARY KEY not null,
    result varchar(max ) not null
);