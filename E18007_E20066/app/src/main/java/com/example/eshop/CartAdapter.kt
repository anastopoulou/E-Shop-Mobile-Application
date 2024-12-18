package com.example.eshop

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter(
    private var cartProducts: Map<Product, Int>,
    private val cart: Cart,
    private val context: Context
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = cartProducts.keys.toList()[position]
        val quantity = cartProducts[product] ?: 0
        holder.bind(product, quantity)
        Glide.with(holder.productImage.context)
            .load(product.imageUrl)
            .into(holder.productImage)
    }

    override fun getItemCount(): Int {
        return cartProducts.size
    }

    fun updateCart(newCartProducts: Map<Product, Int>) {
        cartProducts = newCartProducts
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean {
        return cartProducts.isEmpty()
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        private val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        private val productPriceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        private val increaseQuantityButton: Button = itemView.findViewById(R.id.increaseQuantityButton)
        private val decreaseQuantityButton: Button = itemView.findViewById(R.id.decreaseQuantityButton)
        private val removeFromCartButton: Button = itemView.findViewById(R.id.removeFromCartButton)

        fun bind(product: Product, quantity: Int) {
            productNameTextView.text = product.name
            productPriceTextView.text = itemView.context.getString(R.string.price_format, product.price * quantity)
            quantityTextView.text = quantity.toString()

            increaseQuantityButton.setOnClickListener {
                if (quantity < 10) {
                    cart.updateQuantity(product, quantity + 1)
                    quantityTextView.text = (quantity + 1).toString()
                    productPriceTextView.text = itemView.context.getString(R.string.price_format, product.price * (quantity + 1))
                    updateTotalPrice()
                    startCartActivity() // Restart Activity
                }
            }

            decreaseQuantityButton.setOnClickListener {
                if (quantity > 1) {
                    cart.updateQuantity(product, quantity - 1)
                    quantityTextView.text = (quantity - 1).toString()
                    productPriceTextView.text = itemView.context.getString(R.string.price_format, product.price * (quantity - 1))
                    updateTotalPrice()
                    startCartActivity() // Restart Activity
                }
            }

            removeFromCartButton.setOnClickListener {
                cart.removeFromCart(product)
                notifyItemRemoved(adapterPosition)
                notifyItemRangeChanged(adapterPosition, itemCount)
                updateTotalPrice()
                startCartActivity() // Restart Activity
            }
        }

        private fun updateTotalPrice() {
            var totalPrice = 0.0
            for ((product, quantity) in cartProducts) {
                totalPrice += product.price * quantity
            }
            val totalPriceTextView: TextView = (itemView.context as CartActivity).findViewById(R.id.textViewTotalPrice)
            val totalPriceText = itemView.context.getString(R.string.total_price_label, totalPrice)
            totalPriceTextView.text = totalPriceText
        }

        private fun startCartActivity() {
            val intent = Intent(context, CartActivity::class.java)
            context.startActivity(intent)
        }
    }
}