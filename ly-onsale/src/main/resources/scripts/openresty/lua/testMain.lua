--
-- Created by IntelliJ IDEA.
-- User: davidzhang
-- Date: 2020-11-11
-- Time: 1:20 a.m.
-- To change this template use File | Settings | File Templates.
--

--变长参数
local function f(...)
    for k,v in ipairs({...}) do
        ngx.say(k,":",v)
    end
end


--4.测试 sharedData
--测试 ngx.shared
--local sharedData = ngx.shared.cache_productlist;
--
--count = count + 1
--ngx.say("global variable : ", count)
----local shared_data = ngx.shared.shared_data
--ngx.say(", shared memory : ", sharedData:get("abc"))
--sharedData:incr("abc",1)
--ngx.say(", shared memory : ", sharedData:get("abc"))
--ngx.say("worker Id : " , ngx.worker.id() )
--ngx.log(ngx.ERR,"aaaaa")
--print("bbbbb")


--3.测试 sharedData
--local sharedData = ngx.shared.cache_productlist;
--local i = sharedData:get("i");
--if not i then
--    --i= 1
--    --3、惰性赋值
--    sharedData:set("i", 1)
--    ngx.say("lazy set i ", i, "<br/>")
--end

--递增
--ngx.say("i=", sharedData:get("i"), "<br/>")
--i = sharedData:incr("i", 1)
----ngx.say("i=", i, "<br/>")

--1.测试Module
--1.1
local testModule1 = require "testModule"
ngx.say(testModule1.method2(111 , 222 , 333)); --或ngx.say(testModule1:method2(111 , 222 , 333))
--点号定义的，用点号来调用
--冒号定义的，用冒号来调用
--如function testModule.method2(...)，用testModule1.method2(111 , 222 , 333)来调用
--如function testModule:method2(...)，用testModule1:method2(111 , 222 , 333)来调用
--或function _M.new(self, width, height )，用local s1 = square:new(2, 3) － http://www.dahouduan.com/2017/12/07/lua-module-best-practice/

--1.2
--local b = "bbb";
--f( 111 , b , 333 );

--1.3
--ngx.say(testModule1.method3()) --如在testModule这么定义：function method3()，会报错：attempt to call field 'method3' (a nil value)，因为 method3()是作为全局变量
--ngx.say(method3()) --如在testModule这么定义：function method3()，正常运行，因为是调用全局变量，但会有警告：writing a global Lua variable ('method3') which may lead to race conditions between concurrent requests, so prefer the use of 'local' variables

--2.点号 冒号
--2.1
--girl = {money = 201 }
--boy = {money = 300 }
--function girl.goToMarket(boy1 ,someMoney)
--    boy1.money = boy1.money - someMoney
--end
--girl.goToMarket(boy ,100)
--ngx.say(boy.money)

--2.2
--obj = { x = 20 };
--obj1 = { x = 30 }
--function obj.fun1(self)
--    ngx.say(self.x)
--end
--obj.fun1(obj1); -- 其实self不用传入本身对象，只是指：如果要使用本身对象，必须手动传入


