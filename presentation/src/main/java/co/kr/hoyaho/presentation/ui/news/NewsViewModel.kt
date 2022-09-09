package co.kr.hoyaho.presentation.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.hoyaho.domain.NetworkResult
import co.kr.hoyaho.domain.model.News
import co.kr.hoyaho.domain.usecase.GetHeadLineNewsUseCase
import co.kr.hoyaho.presentation.ui.util.Event
import co.kr.hoyaho.presentation.ui.util.toErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val headLineNewsUseCase: GetHeadLineNewsUseCase
) : ViewModel() {

    private val _news: MutableLiveData<List<News>> = MutableLiveData()
    val news: LiveData<List<News>> get() = _news

    private val _showToast: MutableLiveData<Event<String>> = MutableLiveData()
    val showToast: LiveData<Event<String>> = _showToast

    init {
        fetchNews()
    }

    private fun fetchNews() {
        viewModelScope.launch {
            when (val result = headLineNewsUseCase.invoke()) {
                is NetworkResult.Success -> {
                    val news = result.data
                    _news.value = news
                }
                is NetworkResult.Error -> {
                    val msg = result.errorType.toErrorMessage()
                    _showToast.value = Event(msg)
                }
            }
        }
    }
}