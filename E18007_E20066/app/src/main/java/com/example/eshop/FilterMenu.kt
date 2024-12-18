package com.example.eshop

import android.content.Context
import android.widget.PopupMenu
import android.view.View
import com.example.eshop.MainActivity.FilterListener

class FilterMenu(
    private val context: Context,
    private val products: List<Product>,
    private val filterListener: FilterListener
) {

    fun showFilterPopupMenu(filterButton: View) {
        val popupMenu = PopupMenu(context, filterButton)
        popupMenu.inflate(R.menu.filter_menu)

        // Set click listener for filter
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.filter_accessories -> filterByCategory("Accessories")
                R.id.filter_clothing -> filterByCategory("Clothing")
                R.id.filter_footwear -> filterByCategory("Footwear")
                R.id.filter_price_below_50 -> filterByPrice(0, 49)
                R.id.filter_price_50_100 -> filterByPrice(50, 100)
                R.id.filter_price_above_100 -> filterByPrice(101, Int.MAX_VALUE)
                R.id.filter_rating_1 -> filterByRating(1.0, 1.9)
                R.id.filter_rating_2 -> filterByRating(2.0, 2.9)
                R.id.filter_rating_3 -> filterByRating(3.0, 3.9)
                R.id.filter_rating_4 -> filterByRating(4.0, 4.9)
                R.id.filter_rating_5 -> filterByRating(5.0, 5.0)
                else -> false
            }
            true
        }

        popupMenu.show()
    }

    private fun filterByCategory(category: String) {
        val filteredProducts = products.filter { product ->
            product.category == category
        }
        filterListener.updateFilteredProducts(filteredProducts)
    }

    private fun filterByPrice(minPrice: Int, maxPrice: Int) {
        val filteredProducts = products.filter { product ->
            product.price >= minPrice && product.price <= maxPrice
        }
        filterListener.updateFilteredProducts(filteredProducts)
    }

    private fun filterByRating(minRating: Double, maxRating: Double) {
        val filteredProducts = products.filter { product ->
            product.rating >= minRating && product.rating <= maxRating
        }
        filterListener.updateFilteredProducts(filteredProducts)
    }
}

