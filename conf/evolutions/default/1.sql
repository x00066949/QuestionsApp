# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  name                      varchar(255) not null,
  constraint pk_category primary key (name))
;

create table question (
  id                        bigint not null,
  category_name             varchar(255),
  question                  varchar(255),
  constraint pk_question primary key (id))
;

create sequence category_seq;

create sequence question_seq;

alter table question add constraint fk_question_category_1 foreign key (category_name) references category (name) on delete restrict on update restrict;
create index ix_question_category_1 on question (category_name);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists category;

drop table if exists question;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists category_seq;

drop sequence if exists question_seq;

