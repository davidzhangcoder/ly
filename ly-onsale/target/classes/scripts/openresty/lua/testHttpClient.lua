--要请求的ip
url="http://192.168.1.2:8092"
-- 请求的路径
local path="/onsale/testMethod"
-- 发送请求
local http = require("resty.http")
local httpClient=http.new()
local resp, err = httpClient:request_uri(url, {
    method = "GET",
    path = path
})

-- 获取请求结果
if resp ~= nil then
    ngx.say(type(resp))
    ngx.say(resp.body)
    ngx.say(err)
end
--local value
--while next(resp,value) do
--    ngx.say( next(resp,value) )
--    value = next(resp,value)
--end


--ngx.say(resp.body)
--ngx.say(err)
httpClient:close()