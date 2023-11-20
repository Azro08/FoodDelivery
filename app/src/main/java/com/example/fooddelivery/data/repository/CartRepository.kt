package com.example.fooddelivery.data.repository

import android.util.Log
import com.example.fooddelivery.data.model.Cart
import com.example.fooddelivery.data.model.Food
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val cartCollection = firestore.collection("cart")
    private val userId = firebaseAuth.currentUser?.uid ?: ""

    suspend fun createUsersCart(uid: String) {
        try {
            val cartRef = cartCollection.document(uid)
            cartRef.set(Cart(uid, uid, emptyList())).await()
        } catch (e: FirebaseException) {
            Log.d("CartRepository", "Error creating cart: $e")
        }
    }

    suspend fun addFoodToCart(food: Food): String {
        return try {
            val cartRef = cartCollection.document(userId)

            // Get the current list of food
            val currentHomeworks =
                cartRef.get().await().toObject(Cart::class.java)?.foodList ?: emptyList()

            // Update the list of food
            val updatedFoodIds = currentHomeworks.toMutableList()
            updatedFoodIds.add(food)

            cartRef.update("foodList", updatedFoodIds).await()

            "Done"
        } catch (e: Exception) {
            e.message.toString()
        }

    }

    suspend fun getUsersCart(): List<Food> {
        return try {
            val documentSnapshot = cartCollection.document(userId).get().await()
            if (documentSnapshot.exists()) {
                val foods = documentSnapshot.toObject(Cart::class.java)?.foodList ?: emptyList()

                val foodList = mutableListOf<Food>()

                val foodsCollection = firestore.collection("food")

                for (foodItem in foods) {
                    val foodDocSnapshot = foodsCollection.document(userId).get().await()
                    if (foodDocSnapshot.exists()) {
                        val food = foodDocSnapshot.toObject(Food::class.java)
                        food?.let { foodList.add(it) }
                    }
                }

                foodList
            } else {
                emptyList()
            }
        } catch (e: FirebaseException) {
            emptyList()
        }
    }


    suspend fun deleteFoodFromCart(food: Food): String {
        return try {
            val cartRef = cartCollection.document(userId)
            cartRef.update("foodList", FieldValue.arrayRemove(food)).await()
            "Done"
        } catch (e: FirebaseException) {
            e.message.toString()
        }
    }

}