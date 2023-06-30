package uz.arena.stadium.payment.model.request

data class Data(
    val amount: Int = 0,
    val callback: String = "https://corp.com/success_callback",
    val callbackError: String = "https://corp.com/fail_url",
    val currency: String = "UZS",
    val hookUrl: String = "https://corp.com/payze_hook?authorization_token=token",
    val lang: String = "EN",
    val preauthorize: Boolean = false
)