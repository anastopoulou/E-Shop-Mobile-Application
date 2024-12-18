package com.example.eshop

class Wishlist {
    private val wishlistProducts: MutableSet<Product> = mutableSetOf()

    fun addToWishlist(product: Product) {
        wishlistProducts.add(product)
    }

    fun removeFromWishlist(product: Product) {
        wishlistProducts.remove(product)
    }

    fun getWishlistProducts(): Set<Product> {
        return wishlistProducts.toSet()
    }

    fun hasProduct(product: Product): Boolean {
        return wishlistProducts.contains(product)
    }
}
