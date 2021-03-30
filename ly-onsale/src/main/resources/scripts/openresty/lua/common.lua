--
-- Created by IntelliJ IDEA.
-- User: davidzhang
-- Date: 2020-11-11
-- Time: 1:13 a.m.
-- To change this template use File | Settings | File Templates.
--

--"resty.rediscluster" loaded by init.lua
--local redis_cluster = require "resty.rediscluster"

-- 定义一个名为 common 的模块
local common = {};

local config = {
    dict_name = "redis_cluster_slot_locks",               --shared dictionary name for locks
    name = "testCluster",                   --rediscluster name
    serv_list = {                           --redis cluster node list(host and port),
        { ip = "192.168.1.2", port = 6379 },
        { ip = "192.168.1.2", port = 6380 },
        { ip = "192.168.1.2", port = 6381 },
        { ip = "192.168.1.2", port = 6389 },
        { ip = "192.168.1.2", port = 6390 },
        { ip = "192.168.1.2", port = 6391 }
    },
    keepalive_timeout = 60000,              --redis connection pool idle timeout
    keepalive_cons = 1000,                  --redis connection pool size
    connect_timeout = 1000,              --timeout while connecting
    max_redirection = 5,                    --maximum retry attempts for redirection
    max_connection_attempts = 1             --maximum retry attempts for connection
}

local onSaleServiceHost = "http://192.168.1.2:8081";
local onSaleProductServiceURL = "/goods/getOnSaleProduct";
local redisCacheExpireTime = 45; --45秒
local sharedCacheExpireTime = 30; --30秒

local function getRedisClusterConnection()
    local red_c = redis_cluster:new(config)
    return red_c;
end

local function httpGet(url, path)
    local httpClient=http.new()
    local resp, err = httpClient:request_uri(url, {
        method = "GET",
        path = path
    })

    return resp, err;
end

function common:setProductListToNginxCache(sharedData, key , value )
    sharedData:set(key, value , sharedCacheExpireTime )
end

function common:getProductListFromRedis()
    local redisConnection = getRedisClusterConnection();

    local v, err = redisConnection:get("OnSaleProductList")
    if err then
        ngx.log(ngx.ERR, "err: ", err)
    end

    return v;
end

function common:setProductListToRedis(productList)
    local redisConnection = getRedisClusterConnection();
    redisConnection:setex("OnSaleProductList", redisCacheExpireTime , productList);
    --setex is working, do not need to do set() and expire()
    --redisConnection:expire("OnSaleProductList", redisCacheExpireTime);
end

function common:getProductListFromService()
    local url = onSaleServiceHost;
    return httpGet(url, onSaleProductServiceURL);
end

return common;

