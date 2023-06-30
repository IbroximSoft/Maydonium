package uz.arena.stadium.payment.retrofit

import uz.arena.stadium.payment.repository.Repository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import uz.arena.stadium.payment.resource.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.arena.stadium.payment.model.request.RequestModel
import java.lang.Exception

class ViewModel(
    apiServis: ApiServis,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private val moveRepository = Repository(apiServis)
    val flow = MutableStateFlow<Resource>(Resource.Loading)

    fun getUserData(requestModel: RequestModel): StateFlow<Resource> {

        viewModelScope.launch {
            try {
                if (networkHelper.isNetworkConnected()) {
                    val response = moveRepository.getUserData(requestModel)
                    if (response.isSuccessful && response.body() != null) {
                        flow.emit(Resource.Success(response.body()!!))
                    } else {
                        flow.emit(Resource.Error("Sever Error !"))
                    }
                } else {
                    flow.emit(Resource.Error("Network no connection !"))
                }
            } catch (e: Exception) {
                flow.emit(Resource.Error(e.message.toString()))
            }
        }
        return flow
    }


}