package uz.arena.stadium.payment.model.request

data class RequestModel(
    val apiKey: String = "DEAC8B1444F240EABEA744A35E97F4A8",
    val apiSecret: String = "61465E8C055A4777B02AD35477141909",
    val data: Data = Data(),
    val method: String = "justPay"
)