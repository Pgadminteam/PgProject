-- JUnitTest시 확인하기 위해 db에 강제로 넣는 데이터
insert into T_Reply(id, title, content, writer_id, original_id, obj_type, board_id)
	values(seq4Reply_id.nextval, '무궁한 발전 기원', '많은 사람들에게 도움을...', 1, null, 'post', 1);

insert into T_Reply(id, title, content, writer_id, original_id, obj_type, board_id)
	values(seq4Reply_id.nextval, '직원 모집 공고', '열의에 가득찬 사람들 오세요', 1, null, 'post', 1);

insert into T_Reply(id, content, writer_id, original_id, obj_type)
	values(seq4Reply_id.nextval, 'findAllReply1Reply!', 1, 2, 'reply');
insert into T_Reply(id, content, writer_id, original_id, obj_type)
	values(seq4Reply_id.nextval, 'findAllReply2Reply!', 1, 2, 'reply');

insert into T_Reply(id, content, writer_id, original_id, obj_type)
	values(seq4Reply_id.nextval, 'findReplyWithReply test 대댓글', 1, 3, 'reply');
insert into T_Reply(id, content, writer_id, original_id, obj_type)
	values(seq4Reply_id.nextval, 'findReplyWithReply test 대댓글2', 1, 3, 'reply');
	