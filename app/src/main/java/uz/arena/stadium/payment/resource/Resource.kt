package uz.arena.stadium.payment.resource

import uz.arena.stadium.payment.model.respose.GetDataResponse


sealed class Resource {

    object Loading : Resource()
    data class Success(val data: GetDataResponse) : Resource()
    data class Error(val message: String) : Resource()

}
