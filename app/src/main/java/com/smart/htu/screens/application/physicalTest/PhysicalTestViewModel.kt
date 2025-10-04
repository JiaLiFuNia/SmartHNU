package com.smart.htu.screens.application.physicalTest

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.htu.repo.DataStoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import javax.inject.Inject

data class PhysicalTestUiState(
    val physicalTestCode: String = "",
    val isCodeValidity: Boolean = false,
    val content: String? = null
)

@HiltViewModel
class PhysicalTestViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {
    private val _uiState = MutableStateFlow(PhysicalTestUiState())
    val uiState: StateFlow<PhysicalTestUiState> = _uiState.asStateFlow()

    private val physicalTestBaseUrl = "https://weimob.tzjkcs.com/"

    private val physicalTestCodeStateFlow = dataStoreRepo.observePhysicalTestCode()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            runBlocking {
                dataStoreRepo.observePhysicalTestCode().first()
            }
        )

    init {
        viewModelScope.launch {
            physicalTestCodeStateFlow.collect { value ->
                _uiState.update {
                    it.copy(
                        physicalTestCode = value,
                        isCodeValidity = value.isNotEmpty()
                    )
                }
            }
        }
        /*viewModelScope.launch {
            verifyCodeValidity()
        }*/
    }

    fun verifyCodeValidity() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = Jsoup.connect("${physicalTestBaseUrl}Home/QueryScore")
                .header("host", "weimob.tzjkcs.com")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0 Safari/537.36")
                .data("code", _uiState.value.physicalTestCode)
                .data("state", "OK")
                /*.cookie(
                    "CloudSportWCUser",
                    "377a22273656515c6e626f382b017e4e391a793b1c485341093f0714247a6e00746a20087e016d1a607a233e30535659213d6f6d71601a146e2b282f71021a086e746f343a4c411a767a6f7b71484a573a312334361a021a6e746f343c4d564c3e216f6d711a141a243d2c333a555f4d3e346f6d71504c4c3c2b77787c4c50513e3c3a2f7d4954572b3763343d175555232828397c4e51677f6a6261645c5a6d01112f61236a0f4b2f2d3c0000515a62070e1c20655660491e6c2f1b375b0076033f256007606b6c2d30020522515a7a7920150712497075043f7d07345f00542f217a3a1e68716b3519222d220f5f400e083f3b3d7f7a7003197f350b4a5b610e013464020a427563697e6571141a483e313b3e3f5d5f5d6e62160a7f1a4d562537233e371a02563934212a"
                )*/
                .execute()
            Log.e("TAG666 PhysicalTestVM", "verifyCodeValidity: ${_uiState.value.physicalTestCode}")
            Log.e("TAG666 PhysicalTestVM1", "verifyCodeValidity: ${response.statusCode()}")
            Log.e("TAG666 PhysicalTestVM1", "verifyCodeValidity: ${response.body()}")
            Log.e("TAG666 PhysicalTestVM1", "verifyCodeValidity: ${response.url()}")
            Log.e("TAG666 PhysicalTestVM1", "verifyCodeValidity: ${response.cookies()}")
            Log.e("TAG666 PhysicalTestVM1", "verifyCodeValidity: ${response.headers()}")

            _uiState.update {
                it.copy(
                    isCodeValidity = response.statusCode() == 200,
                    content = _uiState.value.physicalTestCode + response.body() + response.url()
                )
            }
        } catch (e: Exception) {
            Log.e("TAG666 PhysicalTestVM", "verifyCodeValidity: ${e.message}")
        }
    }

    fun savePhysicalTestCode(code: String) {
        viewModelScope.launch {
            dataStoreRepo.savePhysicalTestCode(code)
        }
    }

}