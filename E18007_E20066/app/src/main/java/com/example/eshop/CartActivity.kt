package com.example.eshop

import android.view.View
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cart: Cart
    private lateinit var textViewTotalPrice: TextView
    private lateinit var buttonCheckout: Button
    private lateinit var emptyMessage: TextView

    // Locale Manager
    private lateinit var localeManager: LocaleManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Locale Manager
        localeManager = LocaleManager(this)

        // Set the language to the saved language on startup
        localeManager.language?.let { localeManager.setLocale(it) }

        setContentView(R.layout.activity_cart)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""  // Set an empty string to remove the title
            setDisplayHomeAsUpEnabled(false) // Hide the back arrow
        }

        // Initialize Cart object
        cart = Lists.cart

        // Initialize RecyclerView
        recyclerViewCart = findViewById(R.id.recyclerViewCart)
        recyclerViewCart.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(cart.getCartProducts(), cart, this)
        recyclerViewCart.adapter = cartAdapter

        // Initialize views
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice)
        buttonCheckout = findViewById(R.id.buttonCheckout)
        emptyMessage = findViewById(R.id.empty_cart)

        // Set visibility of empty message based on cart content
        checkCartEmpty()

        // Fetch cart products
        fetchCartProducts()

        // Update total price text
        updateTotalPrice()

        // Update checkout button text
        updateCheckoutButtonText()

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

            // Update checkout button text after language change
            updateCheckoutButtonText()

            // Update total price text after language change
            updateTotalPrice()

            // Restart activity to apply the new language
            recreate()
        }

        // Checkout Button
        buttonCheckout.setOnClickListener {
            if (cartAdapter.itemCount == 0) {
                Toast.makeText(this, getString(R.string.empty_cart), Toast.LENGTH_SHORT).show()
            } else {
                // Proceed with checkout logic
                cart.getCartProducts().forEach { (product, quantity) ->
                    repeat(quantity) {
                        Lists.boughtProducts.addProduct(product)
                    }
                }
                cart.getCartProducts().keys.forEach {
                    cart.removeFromCart(it)
                }
                cartAdapter.updateCart(cart.getCartProducts())
                updateTotalPrice()
                Toast.makeText(this, getString(R.string.checkout_message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchCartProducts() {
        val products = cart.getCartProducts()
        cartAdapter.updateCart(products)
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        val totalPrice = cart.getTotalCost()
        val totalPriceText = getString(R.string.total_price_label, totalPrice)
        textViewTotalPrice.text = totalPriceText
    }

    private fun updateCheckoutButtonText() {
        val checkoutText = if (localeManager.language == "en") "Checkout" else "Ολοκλήρωση αγοράς"
        buttonCheckout.text = checkoutText
    }

    private fun checkCartEmpty() {
        if (cartAdapter.isEmpty()) {
            recyclerViewCart.visibility = View.GONE
            emptyMessage.visibility = View.VISIBLE
        } else {
            recyclerViewCart.visibility = View.VISIBLE
            emptyMessage.visibility = View.GONE
        }
    }
}



