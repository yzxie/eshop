use eshop;

create table t_user (
    id varchar(32) comment '用户id，uuid',
    name varchar(32) comment '名字',
    email varchar(32) comment '邮箱',
    sex int(1) comment '性别',
    birthday date comment '生日',
    create_time datetime comment '创建时间',
    primary key (id)
) engine=InnoDB default charset=utf8 comment='用户';

create table t_product (
    id int not null auto_increment comment 'id',
    name varchar(256) not null comment '产品名称',
    description text comment '产品描述',
    status int not null comment '产品状态，如在售、下架等',
    price double not null comment '产品单价',
    owner_id varchar(32) not null comment '店家id',
    create_time datetime comment '创建时间',
    primary key (id),
    constraint `Id_owner_fk` foreign key (`owner_id`) references t_user(`id`)
) engine=InnoDB default charset=utf8 comment='产品';

create table t_product_quantity (
    id int not null auto_increment comment 'id',
    product_id int not null comment '产品id',
    num  int not null comment '产品数量',
    create_time datetime comment '创建时间',
    primary key (id),
    constraint `Id_product` foreign key (`product_id`) references t_product(`id`)
) ENGINE=InnoDB default charset=utf8 comment='产品库存';

create table t_order (
    id int not null auto_increment comment 'id',
    uuid varchar(32) not null comment 'uuid',
    user_id int not null comment '用户id',
    cost double not null comment '总金额',
    create_time datetime comment '创建时间',
    primary key (id),
    key (uuid)
) ENGINE=InnoDB default charset=utf8 comment='订单';

create table t_order_item (
    id int not null auto_increment comment 'id',
    order_uuid varchar(32) not null comment '所属订单id',
    product_id int not null comment '产品id',
    price double not null comment '产品单价',
    num int not null comment '产品数量',
    create_time datetime comment '创建时间',
    primary key (id),
    constraint `fk_order_uuid` foreign key (`order_uuid`)  references t_order(`uuid`)
) engine=InnoDB default charset=utf8 comment='订单项';

