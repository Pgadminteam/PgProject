drop table T_Attach;

--owner_type, owner_id, uuid, upload_path, file_name, original_file_name, thumbnail_file_name, file_type
create table T_Attach (
	owner_type			varchar2(100),	--party, product, post
	owner_id			numeric(22, 0),
	uuid				varchar2(100),
	upload_path			varchar2(200),
	file_name			varchar2(500),
	original_file_name  varchar2(200),
	thumbnail_file_name	varchar2(500),
	file_type			varchar2(10),	--image, video, audio, others
	constraint pk_attach primary key(owner_type, owner_id, uuid)
);
