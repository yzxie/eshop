use seckill;

create table seckill_number (
    product_id int not null comment '产品id',
    num   int not null comment '产品数量',
    primary key (product_id)
) ENGINE=InnoDB default charset=utf8 comment="秒杀产品数量";