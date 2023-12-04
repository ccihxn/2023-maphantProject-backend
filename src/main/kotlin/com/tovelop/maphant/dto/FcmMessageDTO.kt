package com.tovelop.maphant.dto

data class FcmMessageDTO(
    val userId: Int,
    val title: String,
    val body: String,
    val etc: Map<String, String>? = null, // send more key-value (ex. chatting service - sender, message, etc...)*/
) {
    private var to: List<String>? = null // receiver's-fcm-token
    fun setTokens(tokens: List<String>) {
        this.to = tokens
    }

    fun getTokens() = this.to
}