package com.smart.htu.screens.news.newsView

/**
@author: Ashinch
@project: ReadYou
 */

object NewsHTML {

    const val HTML: String = """
<!DOCTYPE html>
<html dir="auto">
<head>
    <meta name="viewport" content="initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no, width=device-width, viewport-fit=cover" />
    <meta content="text/html; charset=utf-8" http-equiv="content-type"/>
    <style type="text/css">
        %s
    </style>
    <base href="%s" />
    <script src="https://cdn.jsdelivr.net/npm/pangu@4.0.7/dist/browser/pangu.min.js" defer></script>
</head>
<body>
<main>
    <article id="article"> 
        %s
    </article>
</main>
<script>
    %s
    document.addEventListener('DOMContentLoaded', function () { pangu.spacingElementById('article'); });  
</script>
</body>
</html>
"""

}
