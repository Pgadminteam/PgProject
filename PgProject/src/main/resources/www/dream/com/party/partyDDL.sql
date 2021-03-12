drop sequence seq4Party_id;

drop table T_Authority;
drop table T_Authority_Type;
drop table T_ContactPoint;
drop table T_Party;

create sequence seq4Party_id;

--id, name, birth_date, login_id, password, nickname
create table T_Party (
	id					numeric(22, 0) CONSTRAINT pk_party primary key,
	name				varchar2(100),
	birth_date			date,
	login_id			varchar2(100),
	password			varchar2(100),
	nickname			varchar2(100),
	--공통 관리 정보
	reg_date			date default sysdate,
	update_date			date default sysdate		
);
create index idx_party_loginid on T_Party(login_id);

--권한 계층관계를 포함하는 정보로 정의
create table T_Authority_Type(
	name				varchar2(100),
	super_auth_name		varchar2(100),
	primary key(name)		
);
insert into T_Authority_Type(name, super_auth_name)
	values('ROLE_ADMIN', null);
insert into T_Authority_Type(name, super_auth_name)
	values('ROLE_MEMBER', 'ROLE_ADMIN');

create table T_Authority(
	party_id			numeric(22, 0),
	auth_name			varchar2(100),
	primary key(party_id, auth_name)		
);


--Spring Security가 알아서 사용하는 테이블
create table persistent_logins(
	username 			varchar2(100),
	series				varchar2(64) primary key,
	token				varchar2(64),
	last_used			timestamp
);
