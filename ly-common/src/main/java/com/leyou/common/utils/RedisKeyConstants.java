package com.leyou.common.utils;

public class RedisKeyConstants {

    public static final String GOODS_INFO_HASH = "GOODS_INFO_HASH:";

    public static final String GOODS_STOCK = "GOODS:STOCK:";

    public static final String GOODS_STOCK_LOCK = "GOODS_STOCK_LOCK:";

    public static final String UNIQUE_ID = "UNIQUE_ID:";


    public static final String CART_USERID = "CART:USERID:";

    public static final String TEMP_CART_ID = "TEMP_CART_ID:";


    //version1
    //(expire:24hours) - 可以实现24小时之内不能多次购买，（可以根据具体秒杀周期设置）
    public static final String SET_ORDER_FOR_USER = "SET_ORDER_FOR_USER:";

    //(expire:24hours)
    public static final String VALUE_OBJECT_ORDERCACHEDTO_ON_ORDERUNIQUEID = "VALUE_OBJECT_ORDERCACHEDTO_ON_ORDERUNIQUEID:";

    //(expire:24hours)
    public static final String SET_USER_FOR_PRODUCT_PURCHASED = "SET_USER_FOR_PRODUCT_PURCHASED:";

    //(expire:24hours)
    public static final String HASH_ONSALESTATUS_BY_PRODUCT_ON_USERID = "HASH_ONSALESTATUS_BY_PRODUCT_ON_USERID:";



    public static final String LIST_ONSALESTATUS_WAITINGLIST = "LIST_ONSALESTATUS_WAITINGLIST";


    //version2
    public static final String HASH_ONSALESTATUS = "HASH_ONSALESTATUS:";
}
