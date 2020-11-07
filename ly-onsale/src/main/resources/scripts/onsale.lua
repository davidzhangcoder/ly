local userlistKey = tostring(KEYS[1]);
local stockKey = tostring(KEYS[2]);
local userid = tostring(ARGV[1]);
--test a
--redis.debug(userlistKey, productid, userid);

local stock = tonumber(redis.call("get",stockKey));

--has buy
if ( tonumber(redis.call('SISMEMBER',userlistKey,userid)) == 1 )
then
	return 2;--'only one purchased allowed'
end

--no stock setup
if( stock == false )
then
	return 3;--'stock is not configured'
end

if( stock<=0)
then
	return 4;--'sold out'
end

redis.call("sadd",userlistKey,userid);
redis.call("expire",userlistKey,24*60*60);

redis.call("decr",stockKey);

return 1;--'success'
