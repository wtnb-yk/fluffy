create table pets (
                        id UUID NOT NULL DEFAULT gen_random_uuid(),
                        name varchar(100) NOT NULL,
                        primary key (id)
);

insert into pets (name) values ('daifuku'), ('azuki');
