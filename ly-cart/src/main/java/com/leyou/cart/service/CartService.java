package com.leyou.cart.service;

import com.leyou.cart.pojo.Cart;

import java.util.List;

public interface CartService {

    public void addCart(Cart cart);

    public List<Cart> queryCartList();
}
