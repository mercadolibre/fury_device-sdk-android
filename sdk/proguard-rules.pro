# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/nbarrios/Coding/xDKs/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

###################------ retrofit2 ------##########################
-dontwarn okhttp3.**

-dontnote com.android.org.conscrypt.SSLParametersImpl
-dontnote org.apache.harmony.xnet.provider.jsse.SSLParametersImpl
-dontnote dalvik.system.CloseGuard
-dontnote sun.security.ssl.SSLContextImpl

-keepnames class okhttp3.Response
-keepnames class okhttp3.HttpUrl
-keepnames class okhttp3.OkHttpClient

-keepattributes Signature
-keepattributes Exceptions

-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8

-keepnames class retrofit2.Response

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-dontwarn okio.**
###################------ retrofit2 ------##########################