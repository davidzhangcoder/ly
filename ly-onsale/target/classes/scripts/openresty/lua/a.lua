--1
--local a
--ngx.say('111112')
--if a == nil then
--    ngx.say('a is false')
--end

--2
--local a = {a1 = "a1", b = "b", c = "c", d = "d" }
--ngx.say(a[2]);
--ngx.say(table.maxn(a))
--local value
--while next(a, value) do
--    ngx.say(next(a, value))
--    value = next(a, value)
--end

--3
--local tabletest = {1,2,3,4,5,nil,nil,6,nil,nil,7,nil,8,9};--{ [1] = "a1" , [2] = "b" , nil , [3] = "c" }
----ngx.say(tabletest[2]);
----ngx.say(table.maxn(tabletest))
--ngx.say(#tabletest)
--for k, v in pairs(tabletest) do
--    ngx.say(k , ":" , v)
--end
--
--ngx.say("~~~~~~~");
--local k,v = next(tabletest);
--ngx.say(k)
--local k1 = next(tabletest,nil);
--ngx.say(k1)

--4
local var = ngx.var
ngx.say("type ngx.var : ", type(ngx.var), "<br/>")
ngx.say("ngx.var : ", ngx.var, "<br/>")
ngx.say("ngx.var.a : ", ngx.var.a, "<br/>")
ngx.say("ngx.var.b : ", ngx.var.b, "<br/>")
ngx.say("var.a : ", var.a, "<br/>")
ngx.say("var.b : ", var.b, "<br/>")
ngx.say("ngx.var[1] : ", ngx.var[1], "<br/>")
ngx.say("ngx.var[2] : ", ngx.var[2], "<br/>")
ngx.say("ngx.var[a] : ", var["a"], "<br/>")
ngx.say("ngx.var[b] : ", var["b"], "<br/>")
ngx.var.b = 2;

--5 － redis
--local function close_redis(red)
--    if not red then
--        return
--    end
--    local ok, err = red:close()
--    if not ok then
--        ngx.say("close redis error : ", err)
--    end
--end
--
--local redis = require("resty.redis")
--
----创建实例
--local red = redis:new()
----设置超时（毫秒）
--red:set_timeout(1000)
----建立连接
--local ip = "192.168.1.2"
--local port = 6379
--local ok, err = red:connect(ip, port)
--if not ok then
--    ngx.say("connect to redis error : ", err)
--    return close_redis(red)
--end
--
----调用API获取数据
--local resp, err = red:get("key1")
--ngx.say("msg : ", resp)
--ngx.say("err : ", err)
--if not resp then
--    ngx.say("get msg error : ", err)
--    return close_reedis(red)
--end
----得到的数据为空处理
--if resp == ngx.null then
--    resp = ''  --比如默认值
--end
--ngx.say("msg : ", resp)

--6.redis cluster
--local config = {
--    dict_name = "redis_cluster_slot_locks",               --shared dictionary name for locks
--    name = "testCluster",                   --rediscluster name
--    serv_list = {                           --redis cluster node list(host and port),
--        { ip = "192.168.1.2", port = 6379 },
--        { ip = "192.168.1.2", port = 6380 },
--        { ip = "192.168.1.2", port = 6381 },
--        { ip = "192.168.1.2", port = 6389 },
--        { ip = "192.168.1.2", port = 6390 },
--        { ip = "192.168.1.2", port = 6391 }
--    },
--    keepalive_timeout = 60000,              --redis connection pool idle timeout
--    keepalive_cons = 1000,                  --redis connection pool size
--    connect_timeout = 1000,              --timeout while connecting
--    max_redirection = 5,                    --maximum retry attempts for redirection
--    max_connection_attempts = 1             --maximum retry attempts for connection
--}
--
--local redis_cluster = require "resty.rediscluster"
--local red_c = redis_cluster:new(config)
--
----local v, err = red_c:hgetall("hash_test")
--local v, err = red_c:hget("hash_test","hkey1")
--if err then
--    ngx.log(ngx.ERR, "err: ", err)
--else
--    ngx.say(v)
-- end

--7. cjson
local json = require("cjson")

local dogs = { d1 = "dog1" , d2 = "dog2" }
local cats = { c1 = "cat1" , c2 = "cat2" };
local animal = { [1] = dogs , keyofcat = cats };
ngx.say("value --> ", animal["keyofcat"].c1);
ngx.say("value --> ", animal.keyofcat.c1);
ngx.say("value --> ", animal[1].d1);
--ngx.say("value --> ", animal) --error
ngx.say("value --> ", json.encode( animal ))
--ngx.say("value --> ", json.encode({dogs={}}))
