package com.example.eshop

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var languageButton: Button
    private lateinit var searchBar: EditText
    private lateinit var filterButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var navigationProducts: Button
    private lateinit var navigationCart: Button
    private lateinit var navigationWishlist: Button
    private lateinit var navigationReviews: Button

    // Firebase Analytics and Database references
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var database: DatabaseReference
    private lateinit var adapter: ProductAdapter
    private var productList: MutableList<Product> = mutableListOf()

    // Locale Manager
    private lateinit var localeManager: LocaleManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Locale Manager
        localeManager = LocaleManager(this)

        // Set the language to the saved language on startup
        localeManager.language?.let { localeManager.setLocale(it) }

        setContentView(R.layout.activity_main)

        // Initialize Firebase Analytics and Database
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        database = FirebaseDatabase.getInstance().reference.child("products")

        // Other view initialization
        toolbar = findViewById(R.id.toolbar)
        languageButton = findViewById(R.id.language_button)
        searchBar = findViewById(R.id.search_bar)
        filterButton = findViewById(R.id.filter_button)
        recyclerView = findViewById(R.id.recyclerView)
        navigationProducts = findViewById(R.id.navigation_products)
        navigationCart = findViewById(R.id.navigation_cart)
        navigationWishlist = findViewById(R.id.navigation_wishlist)
        navigationReviews = findViewById(R.id.navigation_reviews)

        // Initialize adapter, productList, and searchBarHelper
        adapter = ProductAdapter(productList) { filteredProducts ->
            adapter.updateProducts(filteredProducts)
        }

        // Set up search bar
        val searchBarHelper = SearchBar(adapter, productList)
        searchBarHelper.setupSearchBar(searchBar)

        // RecyclerView setup
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch products from database
        fetchProducts()

        // Filter Button
        filterButton.setOnClickListener {
            val filterMenu = FilterMenu(this@MainActivity, productList, object : FilterListener {
                override fun updateFilteredProducts(filteredProducts: List<Product>) {
                    adapter.updateProducts(filteredProducts)
                }
            })
            filterMenu.showFilterPopupMenu(filterButton)
            logEvent("Filter_Button_Clicked")
        }

        // Search Bar
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterRecyclerView(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Set click listeners for bottom navigation buttons
        navigationProducts.setOnClickListener {
            // Scroll to the top of the products list
            recyclerView.scrollToPosition(0)
        }

        navigationCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        navigationWishlist.setOnClickListener {
            startActivity(Intent(this, WishlistActivity::class.java))
        }

        navigationReviews.setOnClickListener {
            startActivity(Intent(this, ReviewActivity::class.java))
        }

        // Click listener for language button
        languageButton.setOnClickListener {
            val newLanguage = if (localeManager.language == "en") "el" else "en"
            localeManager.language = newLanguage
            localeManager.setLocale(newLanguage)

            // Restart activity to apply the new language
            recreate()
        }
    }

    private fun fetchProducts() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                productList.clear()  // Clear existing list to avoid duplicates
                for (productSnapshot in dataSnapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    if (product != null) {
                        productList.add(product)
                    }
                }
                // Notify the adapter of the data change
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Firebase", "loadProducts:onCancelled", databaseError.toException())
            }
        })
    }

    private fun filterRecyclerView(query: String) {
        adapter.filter(query)
    }

    private fun logEvent(event: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, event)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    // Define FilterListener interface
    interface FilterListener {
        fun updateFilteredProducts(filteredProducts: List<Product>)
    }
}
