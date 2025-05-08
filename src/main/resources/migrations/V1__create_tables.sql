create table users (
    id uuid primary key not null,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    sur_name varchar(50)
);

create table student (
    id uuid primary key not null,
    user_id uuid not null references users(id),
    group varchar(11) not null,
    faculty varchar(5) not null,
    course int not null
);

create table deputy_dean (
    id uuid primary key not null,
    user_id uuid not null references users(id),
    faculty varchar(5) not null,
    course int not null
);

create table day_schedule (
    id uuid primary key not null,
    deputy_dean_id uuid not null references deputy_dean(id),
    date timestamp not null,
    start_at_plan timestamp not null,
    end_at_plan timestamp not null,
    number_of_student_plan int,
    start_at_fact timestamp,
    end_at_fact timestamp,
    number_of_student_fact timestamp,
    purpose varchar(150) not null
);

create table petition (
    id uuid primary key not null,
    student_id uuid references student(id),
    deputy_dean_id uuid not null references deputy_dean(id),
    day_schedule_id uuid not null references day_schedule(id),
    created_at timestamp not null default now(),
    started_at timestamp,
    ended_at timestamp,
    purpose varchar(150) not null,
    status varchar(20) not null
);

create table authentication_info (
    user_id uuid primary key references users(id),
    chat_id bigint not null unique,
    mail varchar(100) not null unique
);

create table mail_approve (
    id uuid primary key not null,
    chat_id bigint not null,
    user_id uuid not null references users(id),
    mail varchar(100) not null,
    code varchar(10) not null
);

create table start_term_info (
    start_day timestamp primary key not null
)