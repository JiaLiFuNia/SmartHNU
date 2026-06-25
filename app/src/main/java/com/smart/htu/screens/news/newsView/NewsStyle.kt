package com.smart.htu.screens.news.newsView

/**
@author: Ashinch
@project: ReadYou
 */

object NewsStyle {

    val fontList = listOf("系统默认", "Serif", "Sans-serif", "Monospace")
    val fontMap = mapOf(
        0 to null,
        1 to "serif",
        2 to "sans-serif",
        3 to "monospace"
    )

    const val HORIZONTAL_MARGIN = 16

    private fun argbToCssColor(argb: Int): String = String.format("#%06X", 0xFFFFFF and argb)

    private fun applyFontFamily(
        fontFamily: String? = null
    ): String = when (fontFamily) {
        null -> "system-ui"
        else -> """
        --font-family: $fontFamily;
    """.trimIndent()
    }

    fun get(
        fontSize: Int,
        fontFamily: String?,
        lineHeight: Float,
        letterSpacing: Float,
        textMargin: Int,
        textColor: Int,
        textBold: Boolean,
        textAlign: String,
        boldTextColor: Int,
        subheadBold: Boolean,
        subheadUpperCase: Boolean,
        imgMargin: Int,
        imgBorderRadius: Int,
        imgDisplayMode: String,
        linkTextColor: Int,
        codeTextColor: Int,
        codeBgColor: Int,
        tableMargin: Int,
        selectionTextColor: Int,
        selectionBgColor: Int
    ): String = """
:root {
    ${applyFontFamily(fontFamily)}
    --font-size: ${fontSize}px;
    --line-height: ${lineHeight * 1.5f};
    --letter-spacing: ${letterSpacing}px;
    --text-margin: ${textMargin}px;
    --text-color: ${argbToCssColor(textColor)};
    --text-bold: ${if (textBold) "600" else "normal"};
    --text-align: ${textAlign};
    --bold-text-color: ${argbToCssColor(boldTextColor)};
    --link-text-color: ${argbToCssColor(linkTextColor)};
    --selection-text-color: ${argbToCssColor(selectionTextColor)};
    --selection-bg-color: ${argbToCssColor(selectionBgColor)};
    --subhead-bold: ${if (subheadBold) "600" else "normal"};
    --subhead-upper-case: ${if (subheadUpperCase) "uppercase" else "none"};
    --img-margin: ${imgMargin}px;
    --img-border-radius: ${imgBorderRadius}px;
    --content-padding: ;
    --bold-text-color: ;
    --image-caption-margin: ;
    --blockquote-margin: 20px;
    --blockquote-padding: ;
    --blockquote-bg-color: ;
    --blockquote-border-width: 3px;
    --blockquote-border-color: ${argbToCssColor(textColor)}33;
    --table-margin: ${tableMargin}px;
    --table-border-width: ;
    --table-border-color: ;
    --table-cell-padding: 0.2em;
    --table-alt-row-bg-color: ;
    --code-text-color: ${argbToCssColor(codeTextColor)};
    --code-bg-color: ${argbToCssColor(codeBgColor)};
    --code-scrollbar-color: ${argbToCssColor(codeTextColor)}22;
    --code-border-width: ;
    --code-border-color: ;
    --code-padding: ;
    --code-font-family: Menlo, Monospace, 'Courier New';
    --code-font-size: 0.9em;
    --pre-color: ;
}

.Signature {
    text-align: right !important;
    margin-top: 0.5em !important;
    margin-bottom: 0.5em !important;
}

article {
    padding: 0;
    margin: 0;
    margin-left: var(--text-margin) !important;
    margin-right: var(--text-margin) !important;
    font-family: var(--font-family) !important;
    font-size: var(--font-size) !important;
    font-weight: var(--text-bold) !important;
    color: var(--text-color) !important;
}

/* Page  */
body {
    margin: 0;
    padding: 0;
}

::selection {
    background-color: var(--selection-bg-color) !important;
    color: var(--selection-text-color) !important;
}

/* Heading  */
h1,
h2,
h3,
h4,
h5,
h6 {
    font-weight: var(--subhead-bold) !important;
    text-transform: var(--subhead-upper-case) !important;
    line-height: calc(min(1.2, var(--line-height))) !important;
    letter-spacing: var(--letter-spacing) !important;
    color: var(--bold-text-color) !important;
    text-align: var(--text-align) !important;
}

/* Paragraph */
p {
    max-width: 100% !important;
    word-wrap: break-word !important;
    overflow-wrap: break-word !important;
    line-height: var(--line-height) !important;
    letter-spacing: var(--letter-spacing) !important;
    text-align: var(--text-align) !important;
}

span {
    line-height: var(--line-height) !important;
    letter-spacing: var(--letter-spacing) !important;
    text-align: var(--text-align) !important;
}

/* Strong  */
strong,
b {
    font-weight: 600 !important;
    color: var(--bold-text-color) !important;
}

/* Link */
a,
a > strong {
    -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
    font-weight: 600 !important;
    color: var(--link-text-color) !important;
    text-decoration: none !important;
}
div > a {
    display: block;
    -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
    font-weight: 600 !important;
    color: var(--link-text-color);
    line-height: var(--line-height);
    letter-spacing: var(--letter-spacing) !important;
    text-align: var(--text-align) !important;
}

/* Image  */
iframe,
video,
embed,
object,
img {
    display: $imgDisplayMode;
    margin-top: 0.5em !important;
    margin-left: calc(0px - var(--text-margin) + var(--img-margin)) !important;
    margin-right: calc(0px - var(--text-margin) + var(--img-margin)) !important;
    max-width: calc(100% + 2 * var(--text-margin) - 2 * var(--img-margin)) !important;
    border-radius: var(--img-border-radius) !important;
}

img {
     height: auto !important;
}

img::after {
    width: 100px !important;
}

img.loaded {
    opacity: 1; /* 加载完成后设置透明度为1 */
}

img.thin {
    margin-top: 0.5em !important;
    margin-bottom: 0.5em !important;
    margin-left: unset !important;
    margin-right: unset !important;
    max-width: 100% !important;
}

p > img {
    margin-top: 0.5em !important;
    margin-bottom: 0.5em !important;
    margin-left: calc(0px - var(--text-margin) + var(--img-margin)) !important;
    margin-right: calc(0px - var(--text-margin) + var(--img-margin)) !important;
    max-width: calc(100% + 2 * var(--text-margin) - 2 * var(--img-margin)) !important;
    height: auto !important;
    border-radius: var(--img-border-radius) !important;
}

img + small {
    display: inline-block;
    line-height: calc(min(1.5, var(--line-height))) !important;
    letter-spacing: var(--letter-spacing) !important;
    margin-top: var(--image-caption-margin) !important;
    text-align: var(--text-align) !important;
}

/* List */
ul,
ol {
    padding-left: 0 !important;
    line-height: var(--line-height) !important;
    letter-spacing: var(--letter-spacing) !important;
    text-align: var(--text-align) !important;
}

li {
    line-height: var(--line-height) !important;
    letter-spacing: var(--letter-spacing) !important;
    margin-left: 1.5em !important;
    text-align: var(--text-align) !important;
}

/* Quote  */
blockquote {
    margin-left: 0.5em !important;
    padding-left: calc(0.9em) !important;
    background-color: var(--blockquote-bg-color) !important;
    border-left: var(--blockquote-border-width) solid var(--blockquote-border-color) !important;
    line-height: var(--line-height) !important;
    letter-spacing: var(--letter-spacing) !important;
    text-align: var(--text-align) !important;
}

blockquote blockquote {
    margin-right: 0 !important;
}

blockquote img {
    max-width: 100% !important;
    left: 0 !important;
}

/* Table  */
table {
    display: block !important;
    width: 100% !important;
    overflow-x: auto !important;
    -webkit-overflow-scrolling: touch;
    border-collapse: collapse !important;
    border: 1px solid rgb(140 140 140);
    letter-spacing: 1px;
    margin: 10px 0 !important;
}

table tbody {
    display: table !important;
    width: 100%;
}

table th,
table td {
    display: table-cell !important;
    border: 1px solid rgb(160 160 160);
    padding: 8px 4px !important;
    line-height: 1.4 !important;
    text-align: center !important;
    vertical-align: middle !important;
    font-size: 14px !important;
    min-width: 60px;
}

table td p {
    text-align: center !important;
    margin: 0 !important;
}

table tr {
    display: table-row !important;
}

table tr:nth-child(even) {
    background-color: var(--table-alt-row-bg-color) !important;
}

/* Code */
pre,
code {
    color: var(--code-text-color) !important;
    background-color: var(--code-bg-color) !important;
    border: 1 solid var(--code-text-color) !important;
    border-radius: 8px !important;
    padding: 2px 5px !important;
    margin: 2px !important;
    font-family: var(--code-font-family) !important;
    font-size: var(--code-font-size) !important;
}

pre {
    overflow: auto !important;
}

code {
    display: inline-block !important;
}

li code {
    white-space: pre-wrap !important;
    word-wrap: break-word !important;
    overflow-wrap: break-word !important;
    max-width: 100% !important;
}

pre::-webkit-scrollbar {
    height: 14px;
}

pre::-webkit-scrollbar-track {
    background-color: transparent;
}

pre::-webkit-scrollbar-thumb {
    background-color: var(--code-scrollbar-color);
    border-radius: 7px;
    background-clip: content-box;
    border: 5px solid transparent;
    border-left-width: 10px;
    border-right-width: 10px;
}

/* MISC */
figure {
    line-height: calc(min(1.5, var(--line-height))) !important;
    letter-spacing: var(--letter-spacing) !important;
    text-align: var(--text-align) !important;
    margin: 0 !important;
    font-size: 12px !important;
}

figure * {
    font-size: 1em !important;
}

figure p,
caption,
figcaption {
    font-size: 12px !important;
}

hr {
    border: 0 !important;
    height: 2px !important;
    background-color: var(--text-color) !important;
    opacity: 0.08 !important;
    border-radius: 2px;
}

/* Bionic Reading */
body {
    --br-boldness: 600;
}

[br-mode=on] br-bold *,
                         [br-mode=on] br-edge  {
    opacity: var(--fixation-edge-opacity,  100%);
}

[br-mode=on] br-bold:nth-of-type(n+1) [fixation-strength="1"] {
    display: inline;
    font-weight: var(--br-boldness);
    line-height: var(--br-line-height,  initial);
    text-decoration: var(--br-line-style) underline 2px;
    color: var(--bold-text-color) !important;
    text-underline-offset: 3px;
}

[br-mode=on] br-bold:nth-of-type(n+1) [fixation-strength="2"] {
    display: inline;
    font-weight: var(--br-boldness);
    line-height: var(--br-line-height, initial);
    text-decoration: var(--br-line-style) underline 2px;
    color: var(--bold-text-color) !important;
    text-underline-offset: 3px;
}

"""

}
