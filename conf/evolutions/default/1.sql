# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table candidate (
  id                        bigint not null,
  name                      varchar(255),
  role_id                   bigint,
  rate                      integer,
  constraint pk_candidate primary key (id))
;

create table category (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_category primary key (id))
;

create table question (
  id                        bigint not null,
  category_id               bigint,
  question                  varchar(255),
  constraint pk_question primary key (id))
;

create sequence candidate_seq;

create sequence category_seq;

create sequence question_seq;

alter table candidate add constraint fk_candidate_role_1 foreign key (role_id) references category (id) on delete restrict on update restrict;
create index ix_candidate_role_1 on candidate (role_id);
alter table question add constraint fk_question_category_2 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_question_category_2 on question (category_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists candidate;

drop table if exists category;

drop table if exists question;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists candidate_seq;

drop sequence if exists category_seq;

drop sequence if exists question_seq;

