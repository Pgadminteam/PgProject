--db에 강제로 생성/삭제할 때 사용함
drop sequence seq4Board_id;
drop sequence seq4Reply_id;

drop table T_Reply;
drop table T_Board;

create sequence seq4Board_id;
create sequence seq4Reply_id;

--id, name
create table T_Board(
	id					numeric(22, 0) primary key,
	name				varchar2(100),
	--공통 관리 정보
	reg_date			date default sysdate,
	update_date			date default sysdate
);
insert into T_Board(id, name)
	values(seq4Board_id.nextval, 'bulletin');
insert into T_Board(id, name)
	values(seq4Board_id.nextval, 'notice');

--id, content, writer_id, original_id, obj_type, title, board_id, like_count, dislike_count, viewcnt, reg_date, update_date
create table T_Reply(
	id					numeric(22, 0) constraint pk_reply primary key,
	original_id			numeric(22, 0),
	content				varchar2(4000),
	writer_id			numeric(22, 0),
	obj_type			varchar2(100),	--reply, post
	--	post일 경우의 추가 정보
	title				varchar2(500),
	board_id			numeric(22, 0),
	viewcnt				numeric(22, 0),
	like_count 			smallInt default 0,
	dislike_count 		smallInt default 0,
	--공통 관리 정보
	reg_date			date default sysdate,
	update_date			date default sysdate
);
create index idx_writerOnRply on T_Reply(writer_id);
create index idx_orgOnRply on T_Reply(original_id);
create index idx_boardOnRply on T_Reply(board_id);
--좋아요 및 싫어요 개수
--alter table T_Reply add (like_count smallInt default 0);
--alter table T_Reply add (dislike_count smallInt default 0);

-- 이 아래는 컴포지트 패턴을 고성능으로 개발하고자 할 때 전형적인 개발 방법 예입니다. 
drop table THP_Reply;
create table THP_Reply(
	hierarchy_id		varchar2(4000) constraint pk_hp_reply primary key,
	content				varchar2(4000),
	--	post일 경우의 추가 정보
	title				varchar2(500)
);

--게시글
insert into THP_Reply(hierarchy_id, title, content)
	values('aaaa', 't1', 'c1');
insert into THP_Reply(hierarchy_id, title, content)
	values('aaab', 't2', 'c2');
--댓글
insert into THP_Reply(hierarchy_id, content)
	values('aaab-aaac', 'rc1');
insert into THP_Reply(hierarchy_id, content)
	values('aaab-aaac-aaad', 'rrc2');
insert into THP_Reply(hierarchy_id, content)
	values('aaab-aaac-aaad-aaae', 'rrrc3');
insert into THP_Reply(hierarchy_id, content)
	values('aaab-aaac-aaad-aaae-aaaf', 'rrrc3');
	
--특정 게시글('aaab')에 달린 모든 댓글 대댓글을 한번에 고성능으로 조회할 때 사용함
select *
  from THP_Reply
 where hierarchy_id like 'aaab%';
 
 
 
 
	