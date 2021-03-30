--
-- Created by IntelliJ IDEA.
-- User: davidzhang
-- Date: 2020-11-13
-- Time: 2:31 a.m.
-- To change this template use File | Settings | File Templates.
--

local commonModule = require "common"

local sharedData = ngx.shared.cache_productlist;

local onSaleProductList =sharedData:get("onSaleProuctList")
ngx.log(ngx.NOTICE, "onSaleProductList from Nginx Cache: " , onSaleProductList ~= nil)
--在nginx中没有缓存
if not onSaleProductList then
    onSaleProductList = commonModule:getProductListFromRedis();
    ngx.log(ngx.NOTICE, "onSaleProductList from Redis: " , onSaleProductList ~= ngx.null)
    --在redis中没有缓存
    if onSaleProductList == ngx.null then
        --从数据库获取
        local resp, err = commonModule:getProductListFromService();
        if err ~= nil then
            ngx.log(ngx.ERR, "err: ", err)
        elseif resp ~= nil then
            onSaleProductList = resp.body;
            ngx.log(ngx.NOTICE, "onSaleProductList from DB: ", onSaleProductList ~= null)
            --重设置到Redis
            commonModule:setProductListToRedis(onSaleProductList);
            --重设置到sharedData
            commonModule:setProductListToNginxCache(sharedData, "onSaleProuctList", onSaleProductList);
            --sharedData:set("onSaleProuctList", onSaleProductList , commonModule.sharedCacheExpireTime )
        end
    else
        --重设置到sharedData
        commonModule:setProductListToNginxCache(sharedData, "onSaleProuctList", onSaleProductList);
        --sharedData:set("onSaleProuctList", onSaleProductList , commonModule.sharedCacheExpireTime )
    end
end

--ngx.log(ngx.NOTICE, "directly retrive from Nginx Cache, if not existed, retrieve from Redis Or DB: " , onSaleProductList ~= null )
if onSaleProductList then
    --ngx.say(onSaleProductList)

    local json = require("cjson");
    local onSaleProductListDecoded = json.decode(onSaleProductList);

--    ngx.say(type(onSaleProductList))
--    ngx.say(type(onSaleProductListDecoded))
--    ngx.say(type(onSaleProductListDecoded[1]))
--    ngx.say(table.getn(onSaleProductListDecoded))
--    ngx.say(onSaleProductListDecoded[1].id)
--    for k  in pairs(onSaleProductListDecoded[1]) do
--        local temp = onSaleProductListDecoded[1];
--        if k == 'images' then
--            ngx.say( temp[k] )
--        end
--        ngx.say( k )
--    end
--    ngx.say("-------")
--    for i = 1, table.getn(onSaleProductListDecoded) do
--        local temp = onSaleProductListDecoded[i];
--        ngx.say( temp.id )
--        ngx.say( temp.spuId )
--        ngx.say( temp.title )
--        ngx.say( temp.images )
--        ngx.say( temp.price )
--        ngx.say( temp.enable )
--        ngx.say( temp.stock )
--    end
--    ngx.say("-------")
--    local b =  {a = onSaleProductList[1]};
--    ngx.log( b )


    local data = { productListData = onSaleProductListDecoded };
    --local data = { productListData = onSaleProductList };
    local template = require("resty.template")
    -- 渲染，第一个参数是要渲染的html页面，第二个是渲染数据
    template.render("productlist.html", data)
else
    ngx.say("not found");
end

--json, template, lock, JVM Cahce (Spring Cache)
--元表，对象，变量的共享范围

