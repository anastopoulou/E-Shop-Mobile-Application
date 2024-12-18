package com.example.eshop

import android.view.View
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class ReviewActivity : AppCompatActivity() {

    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var boughtProducts: BoughtProducts
    private lateinit var emptyMessage: TextView

    // Locale Manager
    private lateinit var localeManager: LocaleManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Locale Manager
        localeManager = LocaleManager(this)

        // Set the language to the saved language on startup
        localeManager.language?.let { localeManager.setLocale(it) }

        setContentView(R.layout.activity_review)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Initialize object
        boughtProducts = Lists.boughtProducts

        reviewRecyclerView = findViewById(R.id.recyclerViewReviews)
        reviewRecyclerView.layoutManager = LinearLayoutManager(this)
        reviewAdapter = ReviewAdapter(boughtProducts.getBoughtProducts(), boughtProducts, this)
        reviewRecyclerView.adapter = reviewAdapter

        // Initialize views
        emptyMessage = findViewById(R.id.empty_review)

        // Set visibility of empty message based on cart content
        checkReviewEmpty()

        // Fetch products from Firebase
        fetchBoughtProducts()

        // Set click listeners for bottom navigation buttons
        findViewById<Button>(R.id.navigation_products).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<Button>(R.id.navigation_cart).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

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

    private fun fetchBoughtProducts() {
        val products = boughtProducts.getBoughtProducts().toList()
        reviewAdapter.updateBoughtProducts(products)
    }

    private fun checkReviewEmpty() {
        if (reviewAdapter.isEmpty()) {
            reviewRecyclerView.visibility = View.GONE
            emptyMessage.visibility = View.VISIBLE
        } else {
            reviewRecyclerView.visibility = View.VISIBLE
            emptyMessage.visibility = View.GONE
        }
    }
}
