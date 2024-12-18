package com.example.eshop

class Cart {
    private val cartProducts: MutableMap<Product, Int> = mutableMapOf()

    fun addToCart(product: Product, quantity: Int = 1) {
        if (quantity > 0) {
            cartProducts[product] = (cartProducts[product] ?: 0) + quantity
        }
    }

    fun removeFromCart(product: Product) {
        cartProducts.remove(product)
    }

    fun getCartProducts(): Map<Product, Int> {
        return cartProducts.toMap()
    }

    fun hasProduct(product: Product): Boolean {
        return cartProducts.containsKey(product)
    }

    fun getQuantity(product: Product): Int {
        return cartProducts[product] ?: 0
    }

    fun getTotalCost(): Double {
        return cartProducts.entries.sumOf { (product, quantity) -> product.price * quantity }
    }

    fun updateQuantity(product: Product, newQuantity: Int) {
        if (newQuantity > 0) {
            cartProducts[product] = newQuantity
        } else {
            removeFromCart(product)
        }
    }
}
