package com.example.eshop

data class Review(
    var reviewId: Int = 0,
    var productId: Int = 0,
    var comment: String = "",
    var rating: Float = 0f
) {
    // No-argument constructor for Firebase
    constructor() : this(0, 0, "", 0f)
}