local limitedUriWithHashTag = tostring(KEYS[1]);

local limitedPeriod = tonumber(ARGV[1]);
local limitedQuantity = tonumber(ARGV[2]);
local timeStamp = tonumber(ARGV[3]);
-- local ip = tostring(ARGV[4]);
-- local keyHashTag = tostring(ARGV[4]);

-- 1. initial rpush limitedUri with timestamp, add expiration ( limitedPeriod , limitedTimeUnit )
-- 2. check if llen >= limitedQuantity

-- search_{192168110} , 10 10 10000


local uriIpValue = redis.call("exists" , limitedUriWithHashTag );

if( uriIpValue == 1 )
then
    local length = tonumber(redis.call("llen", limitedUriWithHashTag ));

    if( length < limitedQuantity )
    then
        redis.call("rpush" , limitedUriWithHashTag , timeStamp );
        return 1; -- return 1 means can access
    end

    local lastTimeStamp = tonumber(redis.call("lrange" , limitedUriWithHashTag , 0 , 0 )[1]);
    if( lastTimeStamp + limitedPeriod >= timeStamp)
    then
        return 0; -- return 0 means can NOT access
    end

-- it is not allowed to use multi and exec in lua
--    redis.call("multi");
    redis.call("lpop" , limitedUriWithHashTag );
    redis.call("rpush" , limitedUriWithHashTag , timeStamp );
--    redis.call("exec");
    return 1;-- return 1 means can access
end

redis.call("rpush" , limitedUriWithHashTag , timeStamp );
return 1; -- return 1 means can access