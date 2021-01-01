local testAccount = require "testAccount";

ngx.say("Start - testAccountMain");

local a = testAccount:new(7);
--a:deposit(100);

ngx.say(a.balance);