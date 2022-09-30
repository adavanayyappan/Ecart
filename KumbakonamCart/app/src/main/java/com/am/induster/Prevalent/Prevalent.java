package com.am.induster.Prevalent;


import com.am.induster.Model.Cart;
import com.am.induster.Model.Products;
import com.am.induster.Model.Users;

import java.util.ArrayList;

public class Prevalent
{
    public static Users currentOnlineUser;
    public static Products userProducts;
    public static Products cartProducts;
    public static ArrayList<Products> cartArray;
    public static ArrayList<String> cartQuantityArray;

    public static final String UserPhoneKey = "UserPhone";
    public static final String UserPasswordKey = "UserPassword";
}
