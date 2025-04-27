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
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 保存混淆映射
-printmapping mapping.txt
-dontwarn sun.security.x509.X509Key
-ignorewarnings

-keep class java.security.** { *; }
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class okhttp3.Cookie { *; }
-keep class okhttp3.OkHttpClient { *; }
-keep public class org.jsoup.** { *; }
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep,allowobfuscation class com.google.gson.reflect.TypeToken
-keep,allowobfuscation class * extends com.google.gson.reflect.TypeToken

-keep class com.smart.htu.** { *; }

-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keepattributes Signature
-keepattributes Signature,InnerClasses,EnclosingMethod
-keepattributes Exceptions
-keepattributes *Annotation*