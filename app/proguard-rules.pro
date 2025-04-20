# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-ignorewarnings

-keep class * {
    public private *;
}

#-keep class retrofit2.** { *; }
#-keep class okhttp3.** { *; }
#-keep class okhttp3.Cookie { *; }
#-keep class okhttp3.OkHttpClient { *; }
#-keep class com.google.gson.** { *; }
#-keep class com.google.gson.reflect.TypeToken { *; }
#
#-keep class * extends com.google.gson.reflect.TypeToken
#-keepclassmembers class * {
#    @com.google.gson.annotations.SerializedName <fields>;
#}
#
#-keepattributes Signature
#-keepattributes Exceptions