package uz.arena.stadium.payment.repository

import uz.arena.stadium.payment.model.request.RequestModel
import uz.arena.stadium.payment.retrofit.ApiServis

class Repository(private val apiServis: ApiServis) {

    suspend fun getUserData(request: RequestModel) = apiServis.getData(request)

}