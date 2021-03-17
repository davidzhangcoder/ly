create table role
(
   id                   bigint not null auto_increment,
   name                 varchar(100) comment '名称',
   description          varchar(500) comment '描述',
   create_time          datetime comment '创建时间',
   status               int(1) default 1 comment '启用状态：0->禁用；1->启用',
   sort                 int default 0,
   primary key (id)
);

create table user_role_relation
(
   id                   bigint not null auto_increment,
   user_id             bigint,
   role_id              bigint,
   primary key (id)
);

create table menu
(
   id                   bigint not null auto_increment,
   parent_id            bigint comment '父级ID',
   create_time          datetime comment '创建时间',
   title                varchar(100) comment '菜单名称',
   level                int(4) comment '菜单级数',
   sort                 int(4) comment '菜单排序',
   name                 varchar(100) comment '前端名称',
   icon                 varchar(200) comment '前端图标',
   hidden               int(1) comment '前端隐藏',
   primary key (id)
);

create table permission
(
   id                   bigint not null auto_increment,
   category_id          bigint comment '资源分类ID',
   create_time          datetime comment '创建时间',
   name                 varchar(200) comment '资源名称',
   url                  varchar(200) comment '资源URL',
   description          varchar(500) comment '描述',
   primary key (id)
);

create table permission_category
(
   id                   bigint not null auto_increment,
   create_time          datetime comment '创建时间',
   name                 varchar(200) comment '分类名称',
   sort                 int(4) comment '排序',
   primary key (id)
);

create table role_menu_relation
(
   id                   bigint not null auto_increment,
   role_id              bigint comment '角色ID',
   menu_id              bigint comment '菜单ID',
   primary key (id)
);

create table role_permission_relation
(
   id                   bigint not null auto_increment,
   role_id              bigint comment '角色ID',
   permission_id          bigint comment '权限ID',
   primary key (id)
);

insert into role(id,name,description) values(1,'ROLE_ADMIN','管理员');
insert into role(id,name,description) values(2,'ROLE_USER','普通用户');

insert into user_role_relation(id,user_id,role_id) values(1,39,1);

insert into permission(id, name, description, url) values (1, 'home' , 'ROLE_HOME',  '/home' );
insert into permission(id, name, description, url) values (2, 'GetBrands' , 'GetBrands',  '/item-service/brand/getBrands' );
insert into permission(id, name, description, url) values (3, 'SaveBrand' , 'SaveBrand',  '/item-service/brand/persistBrand' );
insert into permission(id, name, description, url) values (4, 'GetCategoriesByPage' , 'GetCategoriesByPage',  '/item-service/category/getCategoriesByPage' );
insert into permission(id, name, description, url) values (5, 'SaveCategory' , 'SaveCategory',  '/item-service/category/persistCategory' );


insert into role_permission_relation(id,role_id,permission_id) values(1,1,1);