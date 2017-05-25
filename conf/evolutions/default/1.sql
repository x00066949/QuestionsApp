# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_category primary key (id))
;

create table interview (
  id                        bigint not null,
  candidate_id              bigint,
  role_id                   bigint,
  num_questions             integer,
  interview_date            date,
  interview_rate            integer,
  constraint uq_interview_candidate_id unique (candidate_id),
  constraint pk_interview primary key (id))
;

create table question (
  id                        bigint not null,
  category_id               bigint,
  question                  varchar(255),
  points                    integer,
  repeats                   integer,
  is_difficult              boolean,
  constraint pk_question primary key (id))
;

create table question_rate (
  id                        bigint not null,
  interview_id              bigint,
  question_id               bigint,
  rate                      integer,
  comments                  varchar(255),
  constraint pk_question_rate primary key (id))
;

create table user (
  usertype                  varchar(31) not null,
  id                        bigint not null,
  name                      varchar(255),
  username                  varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (id))
;

create sequence category_seq;

create sequence interview_seq;

create sequence question_seq;

create sequence question_rate_seq;

create sequence user_seq;

alter table interview add constraint fk_interview_candidate_1 foreign key (candidate_id) references user (id) on delete restrict on update restrict;
create index ix_interview_candidate_1 on interview (candidate_id);
alter table interview add constraint fk_interview_role_2 foreign key (role_id) references category (id) on delete restrict on update restrict;
create index ix_interview_role_2 on interview (role_id);
alter table question add constraint fk_question_category_3 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_question_category_3 on question (category_id);
alter table question_rate add constraint fk_question_rate_interview_4 foreign key (interview_id) references interview (id) on delete restrict on update restrict;
create index ix_question_rate_interview_4 on question_rate (interview_id);
alter table question_rate add constraint fk_question_rate_question_5 foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_question_rate_question_5 on question_rate (question_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists category;

drop table if exists interview;

drop table if exists question;

drop table if exists question_rate;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists category_seq;

drop sequence if exists interview_seq;

drop sequence if exists question_seq;

drop sequence if exists question_rate_seq;

drop sequence if exists user_seq;

