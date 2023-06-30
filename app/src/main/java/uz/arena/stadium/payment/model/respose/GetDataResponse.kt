package uz.arena.stadium.payment.model.respose

data class GetDataResponse(
    val action: String,
    val createdDate: String,
    val headers: Headers,
    val id: Int,
    val request: Request,
    val response: Response,
    val status: String
)