drop table M_HT_Reply;
drop table T_Hash_Tag;

--id, super_id, word
create table T_Hash_Tag (
	id					numeric(22, 0) primary key,
	super_id			numeric(22, 0),
	word				varchar2(30)
);
create index idx_HT_word on T_Hash_Tag(word);

insert into T_Hash_Tag(id, super_id, word)
	values(1, null, 'IT');	--1
insert into T_Hash_Tag(id, super_id, word)
	values(2, 1, 'ProgramingLanguage');	--2
insert into T_Hash_Tag(id, super_id, word)
	values(3, 2, 'java');	--3
insert into T_Hash_Tag(id, super_id, word)
	values(4, 3, 'thread');	--4
insert into T_Hash_Tag(id, super_id, word)
	values(5, 3, 'lambda');	--5

--Top Down
SELECT *
  FROM T_Hash_Tag
 START WITH word = 'IT'
CONNECT BY PRIOR  id = super_id;

create table M_HT_Reply(
	hash_tag_id			numeric(22, 0),
	reply_id			numeric(22, 0),
	primary key(hash_tag_id, reply_id)		--검색어를 기준으로 게시글 찾기 성능 보장
);
create index idx_MHTR_Rply on M_HT_Reply(reply_id, hash_tag_id);	--게시글에 달려있는 HashTag 목록 조회

--검색 시 활용할 구문
select p.*
  from T_Reply p, M_HT_Reply m
 where m.hash_tag_id in (
	   SELECT id
	    FROM T_Hash_Tag
 	   START WITH word in ('IT', 'java')
	 CONNECT BY PRIOR  id = super_id)
   and m.reply_id = p.id;