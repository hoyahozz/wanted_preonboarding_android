package co.kr.hoyaho.data.remote.interactor

import co.kr.hoyaho.domain.NetworkError
import co.kr.hoyaho.domain.interactor.NetworkErrorHandler
import retrofit2.HttpException
import java.net.SocketTimeoutException

class NetworkErrorHandlerImpl : NetworkErrorHandler {
    override fun getError(exception: Throwable): NetworkError {
        return when (exception) {
            is SocketTimeoutException -> NetworkError.Timeout
            is HttpException -> {
                when (exception.code()) {
                    in 500..599 -> NetworkError.InternalServer
                    in 400..499 -> {
                        val code = exception.code()
                        NetworkError.BadRequest(code)
                    }
                    else -> NetworkError.Unknown
                }
            }
            else -> NetworkError.Unknown
        }
    }
}
