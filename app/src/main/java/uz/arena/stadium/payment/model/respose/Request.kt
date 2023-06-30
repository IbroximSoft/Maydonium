package uz.arena.stadium.payment.model.respose

data class Request(
    val amount: Double,
    val callback: String,
    val callbackError: String,
    val currency: String,
    val hookUrl: String,
    val lang: String,
    val preauthorize: Boolean
)