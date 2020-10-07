package com.leyou.cart.web;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "购物车相关接口", description = "提供购物车相关的 Rest API")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 新增购物车
     * @param cart
     * @return
     */
    @PostMapping(value = "/cart")
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
        cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/cartWithoutLogin")
    @ApiOperation(value = "未登录用户添加购物车",notes = "添加购物车")
    @ApiParam(name = "cart",required = true,value = "购物车对象，包含购物车　相关信息")
    @ApiResponse(code = 201, message = "未登录用户添加购物车成功")
    public ResponseEntity<Void> addCartWithoutLogin(@RequestBody Cart cart) {
        //获取全局唯一ID
        //以全局唯一ID为key,存入redis
        //返回全局唯一ID为cookie
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/cart/list")
    public ResponseEntity<List<Cart>> queryCartList() {
        return ResponseEntity.ok(cartService.queryCartList());
    }

}
