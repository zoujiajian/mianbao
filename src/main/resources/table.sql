CREATE TABLE IF NOT EXISTS `tb_user_info`(
	id int(5) auto_increment comment '用户id',
	user_name varchar(20) not null comment '用户名',
	user_picture varchar(30) not null comment '用户头像',
	user_password varchar(30) not null comment '用户密码',
	user_mail varchar(30) not null comment '用户邮箱',
	user_sex char(2) not null comment '用户性别',
	user_createTime timestamp not null default current_timestamp() comment '创建时间',
	primary key (id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户信息表'

CREATE TABLE IF NOT EXISTS `tb_scenic_spot`(
	id int(5) auto_increment comment'景点id',
	scenic_spot_name varchar(20) not null comment '景点名称',
	scenic_spot_info varchar(100) not null comment '景点简介',
	scenic_spot_picture varchar(100) not null comment '景点图片地址',
	scenic_createTime timestamp not null default current_timestamp() comment '创建时间',
	create_user int(5) not null comment '创建人',
	primary key(id),
	key(scenic_spot_name)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='景点表'

CREATE TABLE IF NOT EXISTS `tb_user_dynamic`(
	id int(5) auto_increment comment '动态id',
	user_id int(5) not null comment '用户id',
	dynamic_title varchar(20) not null comment '动态简介',
	dynamic_content varchar(500) not null comment '动态内容',
	dynamic_picture varchar(50) comment '动态图片',
	scenic_spot_ids varchar(20) not null comment '关联景点',
	createTime timestamp not null default current_timestamp() comment '创建时间',
	primary key(id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户个人动态'

CREATE TABLE IF NOT EXISTS `tb_dynamic_evaluate`(
	id int(10) auto_increment comment '评价id',
	dynamic_id int(5) not null comment '动态id',
	evaluate_content varchar(50) not null comment'评价内容',
	evaluate_user int(5) not null comment '评价人',
	create_time timestamp not null default current_timestamp() comment '创建时间',
	primary key(id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户个人动态评价'

CREATE TABLE IF NOT EXISTS `tb_evaluate_reply`(
    id int(10) auto_increment comment 'id',
    reply_id int(01) comment '回复目标id',
    evaluate int(10) not null comment'评价id',
    reply_content varchar(50) not null comment '回复内容',
    reply_user_id int(5) not null comment '回复人id',
    to_user_id int(5) not null comment '回复目标id',
    create_time timestamp not null default current_timestamp() comment '创建时间',
    primary key(id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='动态回复'

CREATE TABLE IF NOT EXISTS `tb_user_like`(
	id int(5) auto_increment comment '流水id',
	user_id int(5) not null comment '用户id',
	scenic_spot_id int(5) not null comment '景点id',
	create_time timestamp not null default current_timestamp() comment '创建时间',
	primary key(id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户收藏景点'

CREATE TABLE IF NOT EXISTS `tb_scenic_spot_evaluate`(
	id int(10) auto_increment comment '评价id',
	user_id int(5) not null comment '评价人',
	evaluate_content varchar(50) not null comment '评价内容',
	evaluate_picture varchar(50) not null comment '评价图片',
	create_time timestamp not null default current_timestamp() comment '创建时间',
	evaluate_star tinyint not null comment '星级',
	primary key(id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='景点评价'

CREATE TABLE IF NOT EXISTS `tb_scenic_spot_dynamic`(
	id int(10)  auto_increment comment '流水id',
	scenic_spot_id int(5) not null comment '景点id',
	dynamic_id int(5) not null comment '动态id',
	create_time timestamp not null default current_timestamp() comment '创建时间',
	primary key(id),
	key(scenic_spot_id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='景点相关动态'