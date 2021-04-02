_M = {
    name2 = "shanhuhai"
}
local mt = { __index = _M , name1 = "yyyy" }
local person = setmetatable({name='11111'},mt)
--local person = setmetatable({},mt)
ngx.say(person.name1)
ngx.say(person.name2)
ngx.say(person['name2'])