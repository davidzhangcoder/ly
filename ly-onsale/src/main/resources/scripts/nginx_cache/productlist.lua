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
--在nginx中没有缓存
if not onSaleProductList then
    onSaleProductList = commonModule:getProductListFromRedis();
    ngx.log(ngx.NOTICE, "onSaleProductList from Redis: " , onSaleProductList)
    --在redis中没有缓存
    if onSaleProductList == ngx.null then
        --从数据库获取
        local resp, err = commonModule:getProductListFromService();
        if err ~= nil then
            ngx.log(ngx.ERR, "err: ", err)
        elseif resp ~= nil then
            onSaleProductList = resp.body;
            ngx.log(ngx.NOTICE, "onSaleProductList from DB: ", onSaleProductList)
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

ngx.log(ngx.NOTICE, "directly retrive from Nginx Cache, if not existed, retrieve from Redis Or DB: " , onSaleProductList)
if onSaleProductList then
    ngx.say(onSaleProductList)
else
    ngx.say("not found");
end

