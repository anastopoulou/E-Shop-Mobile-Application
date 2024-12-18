package com.example.eshop

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(
    private var productList: List<Product>,
    private val updateCallback: (List<Product>) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var filteredList: List<Product> = productList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(itemView)
    }

    @SuppressLint("StringFormatMatches")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = filteredList[position]
        holder.productName.text = holder.itemView.context.getString(R.string.name, product.name)
        holder.productDescription.text = holder.itemView.context.getString(R.string.description, product.description)
        holder.productPrice.text = holder.itemView.context.getString(R.string.price, product.price)
        holder.productRating.text = holder.itemView.context.getString(R.string.rating, product.rating)
        holder.productDiscount.text = holder.itemView.context.getString(R.string.discount, product.discount)
        holder.productCategory.text = holder.itemView.context.getString(R.string.category, product.category)
        holder.productSpecifications.text = holder.itemView.context.getString(R.string.specifications, product.specifications)
        holder.productSize.text = holder.itemView.context.getString(R.string.size, product.size)
        holder.productColor.text = holder.itemView.context.getString(R.string.color, product.color)
        holder.productAvailability.text = if (product.availability) {
            holder.itemView.context.getString(R.string.availability_in_stock)
        } else {
            holder.itemView.context.getString(R.string.availability_out_of_stock)
        }
        Glide.with(holder.productImage.context)
            .load(product.imageUrl)
            .into(holder.productImage)

        holder.addToCartButton.setOnClickListener {
            // Add product to cart with quantity 1 (default)
            if (product.availability) {
                Lists.cart.addToCart(product)
                updateCallback(filteredList)
                Toast.makeText(holder.itemView.context, holder.itemView.context.getString(R.string.product_added_to_cart), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(holder.itemView.context, holder.itemView.context.getString(R.string.product_not_available), Toast.LENGTH_SHORT).show()
            }
        }

        holder.addToWishlistButton.setOnClickListener {
            Lists.wishlist.addToWishlist(product)
            updateCallback(filteredList)
            Toast.makeText(holder.itemView.context, holder.itemView.context.getString(R.string.product_added_to_wishlist), Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productDescription: TextView = itemView.findViewById(R.id.product_description)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val productRating: TextView = itemView.findViewById(R.id.product_rating)
        val productDiscount: TextView = itemView.findViewById(R.id.product_discount)
        val productCategory: TextView = itemView.findViewById(R.id.product_category)
        val productSpecifications: TextView = itemView.findViewById(R.id.product_specifications)
        val productSize: TextView = itemView.findViewById(R.id.product_size)
        val productColor: TextView = itemView.findViewById(R.id.product_color)
        val productAvailability: TextView = itemView.findViewById(R.id.product_availability)
        val addToCartButton: Button = itemView.findViewById(R.id.button_add_to_cart)
        val addToWishlistButton: Button = itemView.findViewById(R.id.button_add_to_wishlist)
    }

    fun updateProducts(newList: List<Product>) {
        filteredList = newList
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            productList
        } else {
            productList.filter { product ->
                product.name.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}
