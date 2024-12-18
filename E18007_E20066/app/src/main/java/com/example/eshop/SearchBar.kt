package com.example.eshop

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class SearchBar(private val adapter: ProductAdapter, private val products: List<Product>) {

    fun setupSearchBar(searchBar: EditText) {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterBySearch(s.toString().trim())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterBySearch(query: String) {
        val filteredProducts = products.filter { product ->
            product.name.contains(query, ignoreCase = true)
        }
        adapter.updateProducts(filteredProducts)
    }
}