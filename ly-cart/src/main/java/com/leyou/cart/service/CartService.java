package com.leyou.cart.service;

import com.leyou.cart.pojo.Cart;

import java.util.List;

public interface CartService {

    public void addCart(Cart cart);

    public String addCartWithoutLogin(Cart cart, String tempCartID);

    public List<Cart> queryCartList();
}
