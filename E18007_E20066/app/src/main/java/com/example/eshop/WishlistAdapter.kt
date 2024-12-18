package com.example.eshop

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class WishlistAdapter(
    private var wishlistProducts: Set<Product>,
    private val wishlist: Wishlist,
    private val cart: Cart,
    private val context: Context
) : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.wishlist_item, parent, false)
        return WishlistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val productList = wishlistProducts.toList() // Convert set to list
        val product = productList[position]
        holder.bind(product)
        Glide.with(holder.productImage.context)
            .load(product.imageUrl)
            .into(holder.productImage)
    }

    override fun getItemCount(): Int {
        return wishlistProducts.size
    }

    fun updateWishlist(newWishlist: List<Product>) {
        wishlistProducts = newWishlist.toSet()
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean {
        return wishlistProducts.isEmpty()
    }

    inner class WishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        private val productNameTextView: TextView = itemView.findViewById(R.id.product_name)
        private val productPriceTextView: TextView = itemView.findViewById(R.id.product_price)
        private val removeFromWishlistButton: Button = itemView.findViewById(R.id.button_remove_from_wishlist)
        private val addToCartButton: Button = itemView.findViewById(R.id.button_add_to_cart)

        fun bind(product: Product) {
            productNameTextView.text = product.name
            productPriceTextView.text = itemView.context.getString(R.string.price_format, product.price)

            removeFromWishlistButton.setOnClickListener {
                wishlist.removeFromWishlist(product)
                notifyItemRemoved(adapterPosition)
                notifyItemRangeChanged(adapterPosition, itemCount)
                startWishlistActivity() // Restart Activity
            }

            addToCartButton.setOnClickListener {
                if (product.availability) {
                    cart.addToCart(product)
                    wishlist.removeFromWishlist(product)
                    notifyItemRemoved(adapterPosition)
                    notifyItemRangeChanged(adapterPosition, itemCount)
                    startWishlistActivity() // Restart Activity
                } else {
                    Toast.makeText(itemView.context, itemView.context.getString(R.string.product_not_available), Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun startWishlistActivity() {
            val intent = Intent(context, WishlistActivity::class.java)
            context.startActivity(intent)
        }
    }
}

