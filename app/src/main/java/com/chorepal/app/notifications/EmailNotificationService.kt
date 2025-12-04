package com.chorepal.app.notifications

import com.chorepal.app.data.models.Chore
import com.chorepal.app.data.models.User
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Email Notification Service
 * 
 * This service queues email notifications to be sent via Firebase Cloud Functions.
 * Requires Firebase Cloud Functions backend to be deployed.
 * 
 * See EMAIL_NOTIFICATIONS_SETUP.md for configuration instructions.
 */
class EmailNotificationService {
    
    private val firestore = FirebaseFirestore.getInstance()
    
    /**
     * Send email when chore is assigned to child
     */
    suspend fun sendChoreAssignedEmail(child: User, chore: Chore, parent: User) {
        val emailData = hashMapOf<String, Any>(
            "to" to child.email,
            "template" to "chore_assigned",
            "subject" to "New Chore Assigned: ${chore.title}",
            "data" to mapOf(
                "childName" to child.name,
                "parentName" to parent.name,
                "choreTitle" to chore.title,
                "choreDescription" to chore.description,
                "points" to chore.pointsValue,
                "choreType" to chore.choreType.name
            ),
            "timestamp" to System.currentTimeMillis()
        )
        
        queueEmail(emailData)
    }
    
    /**
     * Send email when chore is completed (to parent)
     */
    suspend fun sendChoreCompletedEmail(parent: User, chore: Chore, child: User) {
        val emailData = hashMapOf<String, Any>(
            "to" to parent.email,
            "template" to "chore_completed",
            "subject" to "${child.name} Completed a Chore!",
            "data" to mapOf(
                "parentName" to parent.name,
                "childName" to child.name,
                "choreTitle" to chore.title,
                "points" to chore.pointsValue,
                "hasPhoto" to (chore.imageProofUrl != null)
            ),
            "timestamp" to System.currentTimeMillis()
        )
        
        queueEmail(emailData)
    }
    
    /**
     * Send email when chore is approved (to child)
     */
    suspend fun sendChoreApprovedEmail(child: User, chore: Chore, parent: User) {
        val emailData = hashMapOf<String, Any>(
            "to" to child.email,
            "template" to "chore_approved",
            "subject" to "Chore Approved! You Earned ${chore.pointsValue} Points!",
            "data" to mapOf(
                "childName" to child.name,
                "parentName" to parent.name,
                "choreTitle" to chore.title,
                "pointsEarned" to chore.pointsValue,
                "totalPoints" to child.totalPoints
            ),
            "timestamp" to System.currentTimeMillis()
        )
        
        queueEmail(emailData)
    }
    
    /**
     * Send email when chore is rejected (to child)
     */
    suspend fun sendChoreRejectedEmail(child: User, chore: Chore, parent: User, reason: String?) {
        val emailData = hashMapOf<String, Any>(
            "to" to child.email,
            "template" to "chore_rejected",
            "subject" to "Chore Needs Revision: ${chore.title}",
            "data" to mapOf(
                "childName" to child.name,
                "parentName" to parent.name,
                "choreTitle" to chore.title,
                "reason" to (reason ?: "Please redo this chore")
            ),
            "timestamp" to System.currentTimeMillis()
        )
        
        queueEmail(emailData)
    }
    
    /**
     * Send email when points are redeemed
     */
    suspend fun sendPointsRedeemedEmail(child: User, points: Int, reason: String, parent: User) {
        val emailData = hashMapOf<String, Any>(
            "to" to child.email,
            "template" to "points_redeemed",
            "subject" to "Points Redeemed: $points Points Used",
            "data" to mapOf(
                "childName" to child.name,
                "parentName" to parent.name,
                "pointsRedeemed" to points,
                "reason" to reason,
                "remainingPoints" to child.totalPoints
            ),
            "timestamp" to System.currentTimeMillis()
        )
        
        queueEmail(emailData)
    }
    
    /**
     * Queue email to be sent by Cloud Function
     * The email document is added to Firestore, which triggers a Cloud Function
     */
    private fun queueEmail(emailData: HashMap<String, Any>) {
        try {
            firestore.collection("mail")
                .add(emailData)
                .addOnSuccessListener {
                    println("Email queued successfully")
                }
                .addOnFailureListener { e ->
                    println("Failed to queue email: ${e.message}")
                }
        } catch (e: Exception) {
            println("Error queueing email: ${e.message}")
        }
    }
}

