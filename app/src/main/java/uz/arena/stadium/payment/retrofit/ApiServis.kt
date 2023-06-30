package uz.arena.stadium.payment.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import uz.arena.stadium.payment.model.request.RequestModel
import uz.arena.stadium.payment.model.respose.GetDataResponse

interface ApiServis {

    @POST("api/v1")
    suspend fun getData(
        @Body body: RequestModel
    ):Response<GetDataResponse>
}

