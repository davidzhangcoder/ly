SELECT * FROM leyou1.tb_order;

select distinct cid from tb_spec_param;

select * from tb_category;

select * from tb_user;
-- delete from tb_user where id = 35;

select * from tb_spu;
select * from tb_sku;
select * from tb_sku where id = 26266538223;

select * from tb_stock;
select * from tb_stock where sku_id = 10781492357;
select * from tb_stock where sku_id in ( 10781492357 , 26266538223 );
-- update tb_stock set stock=99999 where sku_id = 10781492357;
select count(1) from tb_stock;
SELECT a.* , b.stock FROM tb_sku a , tb_stock b WHERE a.id in ( 10781492357 ) and a.id = b.sku_id;

desc tb_stock;
desc tb_order;
desc tb_order_detail;
desc tb_order_status;

select * from tb_order;
select count(1) from tb_order;
-- delete from tb_order where order_id not in (1099652732983312384,1099654463263735808);
select * from tb_order_detail;
select count(1) from tb_order_detail;
-- delete from tb_order_detail where order_id not in (1099652732983312384,1099654463263735808);
select * from tb_order_status;
select count(1) from tb_order_status;
-- delete from tb_order_status where order_id not in (1099652732983312384,1099654463263735808);

select * from undo_log;





