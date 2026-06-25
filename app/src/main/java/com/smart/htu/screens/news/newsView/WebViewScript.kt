package com.smart.htu.screens.news.newsView


/**
@author: Ashinch
@project: ReadYou
 */

object WebViewScript {

    fun get() = """
(function() {
    var imgs = document.getElementsByTagName('img');
    for(var i=0; i<imgs.length; i++) {
        imgs[i].onclick = function() {
            window.JavaScriptInterface.onImgTagClick(this.src);
            return false;
        }
    }
})()
"""
}
