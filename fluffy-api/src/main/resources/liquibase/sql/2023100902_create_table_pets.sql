create table owners (
                        id UUID NOT NULL DEFAULT gen_random_uuid(),
                        name varchar(100) NOT NULL,
                        primary key (id)
);

insert into owners (name) values ('Mike'), ('Ann');
