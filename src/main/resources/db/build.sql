create table t_admin
(
    admin_id    varchar(64) default '' not null
        primary key,
    username    varchar(128)           null,
    password    varchar(256)           null,
    is_use      int                    null,
    admin_role  int                    null,
    create_time datetime               null
)
    collate = utf8mb4_general_ci;

create table t_config
(
    config_id      int auto_increment
        primary key,
    config_content text   null,
    config_msg     text   null,
    config_type    int    null,
    create_time    bigint null,
    modify_time    bigint null,
    is_delete      int    null
)
    collate = utf8mb4_general_ci;

create table t_good
(
    good_id              varchar(64) default '' not null
        primary key,
    good_title           varchar(256)           null comment '标题',
    good_brief           text                   null comment '详情',
    scene_pic_url        text                   null,
    list_pic_url         text                   null,
    floor_price          decimal(12, 2)         null comment '底价',
    retail_price         decimal(12, 2)         null comment '真实价格',
    market_price         decimal(12, 2)         null comment '市场价格',
    good_number          int                    null comment '商品数量',
    video_url_vertical   text                   null,
    video_url_horizontal text                   null,
    photo_url1           text                   null,
    photo_url2           text                   null,
    photo_url3           text                   null,
    photo_url4           text                   null,
    photo_url5           text                   null,
    tag_list             varchar(256)           null,
    symbol_id            varchar(64)            null,
    is_new               int         default 0  null,
    is_chosen            int                    null,
    is_cheap             int                    null,
    like_num             int         default 0  null,
    score_all            decimal(12, 2)         null,
    create_time          bigint                 null,
    is_delete            int                    null,
    modify_time          bigint                 null
)
    collate = utf8mb4_general_ci;

create index t_good_is_cheap_index
    on t_good (is_cheap);

create index t_good_is_chosen_index
    on t_good (is_chosen);

create index t_good_is_new_index
    on t_good (is_new);

create table t_symbol
(
    symbol_id    varchar(64) default '' not null
        primary key,
    symbol_name  varchar(128)           null,
    sort_num     int                    null,
    symbol_title text                   null,
    symbol_desc  text                   null,
    is_popular   int                    null,
    create_time  bigint                 null,
    modify_time  bigint                 null,
    is_delete    int                    null comment '0 normal -1 del'
)
    collate = utf8mb4_general_ci;

create table t_user
(
    user_id     varchar(64) default '' not null
        primary key,
    open_id     varchar(128)           null,
    nick_name   varchar(256)           null,
    user_mobile varchar(32)            null,
    avatar_url  text                   null,
    union_id    varchar(128)           null,
    province    varchar(32)            null,
    city        varchar(32)            null,
    country     varchar(32)            null,
    gender      varchar(8)             null,
    language    varchar(64)            null,
    create_time bigint                 null,
    type_id     varchar(64)            null,
    father_id   varchar(64)            null,
    password    text                   null,
    is_delete   int                    null,
    modify_time bigint                 null
)
    collate = utf8mb4_general_ci;

