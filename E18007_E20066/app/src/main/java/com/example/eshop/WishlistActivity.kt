package com.example.eshop

import android.view.View
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WishlistActivity : AppCompatActivity() {

    private lateinit var recyclerViewWishlist: RecyclerView
    private lateinit var wishlistAdapter: WishlistAdapter
    private lateinit var wishlist: Wishlist
    private lateinit var cart: Cart
    private lateinit var emptyMessage: TextView
    private lateinit var localeManager: LocaleManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)

        // Initialize Locale Manager
        localeManager = LocaleManager(this)

        // Set the language to the saved language on startup
        localeManager.language?.let { localeManager.setLocale(it) }

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Initialize objects
        wishlist = Lists.wishlist
        cart = Lists.cart

        // Initialize RecyclerView
        recyclerViewWishlist = findViewById(R.id.recyclerViewWishlist)
        recyclerViewWishlist.layoutManager = LinearLayoutManager(this)
        wishlistAdapter = WishlistAdapter(wishlist.getWishlistProducts(), wishlist, cart, this)
        recyclerViewWishlist.adapter = wishlistAdapter

        // Initialize views
        emptyMessage = findViewById(R.id.empty_wishlist)

        // Set visibility of empty message based on cart content
        checkWishlistEmpty()

        // Fetch cart products
        fetchWishlistProducts()

        // Set click listeners for bottom navigation buttons
        findViewById<Button>(R.id.navigation_products).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<Button>(R.id.navigation_cart).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        // Wishlist is current activity, so no action needed for this button
        findViewById<Button>(R.id.navigation_wishlist).setOnClickListener {
            startActivity(Intent(this, WishlistActivity::class.java))
        }

        findViewById<Button>(R.id.navigation_reviews).setOnClickListener {
            startActivity(Intent(this, ReviewActivity::class.java))
        }

        // Language Button
        val languageButton: Button = findViewById(R.id.language_button)
        languageButton.setOnClickListener {
            val newLanguage = if (localeManager.language == "en") "el" else "en"
            localeManager.language = newLanguage
            localeManager.setLocale(newLanguage)

            // Restart activity to apply the new language
            recreate()
        }
    }

    private fun fetchWishlistProducts() {
        val products = wishlist.getWishlistProducts().toList()
        wishlistAdapter.updateWishlist(products)
    }

    private fun checkWishlistEmpty() {
        if (wishlistAdapter.isEmpty()) {
            recyclerViewWishlist.visibility = View.GONE
            emptyMessage.visibility = View.VISIBLE
        } else {
            recyclerViewWishlist.visibility = View.VISIBLE
            emptyMessage.visibility = View.GONE
        }
    }
}



