package com.example.eshop

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ReviewAdapter(
    private var boughtProductsList: Set<Product>,
    private val boughtProducts: BoughtProducts,
    private val context: Context
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val productList = boughtProductsList.toList() // Convert set to list
        val product = productList[position]
        holder.bind(product)
    }

    fun updateBoughtProducts(newBoughtProducts: List<Product>) {
        boughtProductsList = newBoughtProducts.toSet()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return boughtProductsList.size
    }

    fun isEmpty(): Boolean {
        return boughtProductsList.isEmpty()
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.productName)
        val ratingSpinner: Spinner = itemView.findViewById(R.id.ratingSpinner)
        val commentEditText: EditText = itemView.findViewById(R.id.commentEditText)
        val reviewButton: Button = itemView.findViewById(R.id.reviewButton)

        fun bind(product: Product) {
            productName.text = product.name

            // Setup rating spinner with numbers 1 to 5
            val ratingAdapter = ArrayAdapter.createFromResource(itemView.context,
                R.array.rating_array, android.R.layout.simple_spinner_item)
            ratingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            ratingSpinner.adapter = ratingAdapter

            // Set a listener for the review button
            reviewButton.setOnClickListener {
                val rating = ratingSpinner.selectedItem.toString().toFloat()
                val comment = commentEditText.text.toString().trim()

                if (comment.isEmpty()) {
                    Toast.makeText(itemView.context, itemView.context.getString(R.string.empty_comment), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val database = FirebaseDatabase.getInstance().reference.child("reviews")

                // Find the next available reviewId starting from 1
                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var reviewId = 1
                        while (snapshot.hasChild(reviewId.toString())) {
                            reviewId++
                        }

                        // Now reviewId is the first available ID
                        val review = Review(reviewId, product.id, comment, rating)
                        database.child(reviewId.toString()).setValue(review)

                        // Mark product as reviewed in boughtProducts list
                        boughtProducts.removeProduct(product)

                        // Update product rating
                        val reviews = snapshot.children.mapNotNull { it.getValue(Review::class.java) }
                        val updatedRating = calculateAverageRating(product, reviews, review)
                        updateProductRatingInDatabase(product, updatedRating)

                        // Notify adapter of data set change
                        updateBoughtProducts(boughtProductsList.toList()) // Refresh the bought products list

                        Toast.makeText(itemView.context, itemView.context.getString(R.string.review_posted), Toast.LENGTH_SHORT).show()
                        startReviewActivity() // Restart Activity
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle errors
                    }
                })
            }
        }

        private fun calculateAverageRating(product: Product, reviews: List<Review>, review: Review): Float {
            val productReviews = reviews.filter { it.productId == product.id }
            val totalRating = productReviews.map { it.rating }.sum() + review.rating
            return totalRating / (productReviews.size + 1).toFloat()
        }

        private fun updateProductRatingInDatabase(product: Product, updatedRating: Float) {
            val database = FirebaseDatabase.getInstance().reference.child("products").child(product.id.toString())
            database.child("rating").setValue(updatedRating)
        }

        private fun startReviewActivity() {
            val intent = Intent(context, ReviewActivity::class.java)
            context.startActivity(intent)
        }
    }
}
