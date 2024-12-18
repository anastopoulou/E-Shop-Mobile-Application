package com.example.eshop

data class Product(
    val id: Int = 0,
    val name: String = "",
    var imageUrl: String = "",
    val description: String = "",
    val price: Double = 0.0,
    var discount: Double = 0.0,
    var rating: Float = 0.0f,
    val category: String = "",
    val specifications: String = "",
    val size: String = "",
    val color: String = "",
    var availability: Boolean = false
) {
    // No-argument constructor required by Firebase
    constructor() : this(0, "", "", "", 0.0, 0.0, 0.0f, "", "", "", "", false)
}
