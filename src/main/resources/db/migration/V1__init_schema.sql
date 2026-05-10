create table tasks (
    id bigserial primary key,
    title varchar(255) not null,
    description text,
    completed boolean not null default false,
    priority varchar(20) not null,
    due_date timestamp,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

create table task_tags (
    task_id bigint not null,
    tag varchar(100) not null,
    constraint fk_task_tags_task_id
        foreign key (task_id) references tasks(id) on delete cascade
);

create table task_attachments (
    id bigserial primary key,
    task_id bigint not null,
    file_name varchar(255) not null,
    file_path varchar(1024) not null,
    uploaded_at timestamp not null default now(),
    constraint fk_task_attachments_task_id
        foreign key (task_id) references tasks(id) on delete cascade
);