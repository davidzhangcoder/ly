redis_cluster = require "resty.rediscluster"
http = require("resty.http")

count = math.random(100);
--local sharedData = ngx.shared.cache_productlist;
--sharedData:set("abc", 1)