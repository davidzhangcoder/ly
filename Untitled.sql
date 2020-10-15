SELECT count(1) FROM leyou.tb_category;

SELECT * FROM `tb_category` WHERE id = (SELECT MAX(id) FROM tb_category);

select * from tb_category_brand;
select * from tb_category_brand where category_id is null;
delete from tb_category_brand where category_id is null;

select distinct brand_id from tb_category_brand;

select distinct brand_id from tb_category_brand where brand_id not in (select id from tb_brand);
select distinct category_id from tb_category_brand where category_id not in (select id from tb_category);
select * from tb_category_brand where brand_id in ('325407',
'325409',
'325405',
'325406',
'325404',
'325397',
'325408');
select * from tb_brand where id in ('325407',
'325409',
'325405',
'325406',
'325404',
'325397',
'325408'
);
select * from tb_category where id = 76;

delete from tb_category_brand where brand_id in ('325407',
'325409',
'325405',
'325406',
'325404',
'325397',
'325408');

commit;