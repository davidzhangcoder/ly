local userlist = tostring(KEYS[1]);
local productid = tostring(KEYS[2]);
local userid = tostring(ARGV[1]);
--test a
redis.debug(userlist, productid, userid);

local stock = tonumber(redis.call("get","stock:"..productid));

--has buy
if ( tonumber(redis.call('SISMEMBER',userlist,userid)) == 1 )
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

redis.call("sadd",userlist,userid);

redis.call("decr","stock:"..productid);

return 1;--'success'
