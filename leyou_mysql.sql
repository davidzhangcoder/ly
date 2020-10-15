SELECT * FROM leyou1.t_transactional_message_content;

SELECT * FROM leyou1.t_transactional_message;

SELECT * FROM leyou1.t_order;

SELECT * FROM leyou1.t_account1;
update leyou1.t_account1 set amount = 10000 where id =1;
delete from leyou1.t_account1 where id > 1;

SELECT * FROM leyou1.t_account2;
update leyou1.t_account2 set amount = 0 where id =2;

select * from undo_log;

-- delete from leyou1.t_transactional_message_content;
-- delete from leyou1.t_transactional_message;
-- delete from leyou1.t_order;

show processlist;

show variables like 'thread%';
show variables like '%pool%';
show variables like '%max_connections%';

show global status like 'Thread%';
show status like 'Threads%';

select version();

-- create table t_test(
-- id bigint(20),
-- name varchar(20)
-- );

-- drop table t_test;