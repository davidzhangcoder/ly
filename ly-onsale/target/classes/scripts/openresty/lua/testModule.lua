--
-- Created by IntelliJ IDEA.
-- User: davidzhang
-- Date: 2020-11-11
-- Time: 1:20 a.m.
-- To change this template use File | Settings | File Templates.
--

-- 定义一个名为 testModule 的模块
local testModule = {};

-- 定义一个函数
function testModule:method1()
    return "aaaaa";
end

-- 定义一个函数
function testModule.method2(...) -- 或 function testModule.method2(self, ...)
    local temp = {...};
    ngx.say(temp);
    if temp ~= nil then
        for k, v in ipairs(temp) do
            ngx.say(k , ":" , v);
        end
    end

    local k = nil
    while next(temp,k) do
            ngx.say(next(temp, k))
            k = next(temp, k)
    end

end

--function method3()
--    return "method3";
--end

return testModule;
