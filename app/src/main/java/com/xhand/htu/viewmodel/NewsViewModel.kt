package com.xhand.htu.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.xhand.htu.model.entity.Category
import com.xhand.htu.model.entity.SwiperEntity

class NewsViewModel:ViewModel() {
    //首页标签
    val categories by mutableStateOf(
        listOf(
            Category("通知通告"),
            Category("活动通知"),
            Category("教务通知")
            )
    )

    var categoryIndex by mutableStateOf(0)
        private set

    fun updataCategoryIndex(index: Int){
        categoryIndex = index
    }

    //轮播图
    val swiperData = listOf(
        SwiperEntity("https://www.htu.edu.cn/_upload/article/images/22/06/de6a5af34a7e82b64a3a4b06a636/560bb4ea-3344-4399-b590-b8efb9bda062.jpg"),
        SwiperEntity("https://www.htu.edu.cn/_upload/article/images/95/3c/9a24a3da4a7a940bdc687b5a0613/15f627df-f56a-4b13-9427-d51a42c50242.png"),
        SwiperEntity("https://www.htu.edu.cn/_upload/article/images/a6/0f/ab2915e74d75818551218b6b2855/5b1b518a-f74d-448c-bb4a-0f77eb78884b.jpg")
    )
}

