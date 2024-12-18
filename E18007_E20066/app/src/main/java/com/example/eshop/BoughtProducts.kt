package com.example.eshop

class BoughtProducts {
    private val boughtProducts: MutableSet<Product> = mutableSetOf()

    fun addProduct(product: Product) {
        boughtProducts.add(product)
    }

    fun removeProduct(product: Product) {
        boughtProducts.remove(product)
    }

    fun getBoughtProducts(): Set<Product> {
        return boughtProducts.toSet()
    }

    fun hasProduct(product: Product): Boolean {
        return boughtProducts.contains(product)
    }
}