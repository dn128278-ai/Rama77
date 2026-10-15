# ProGuard / R8 Optimization Rules for Rama77 (com.rama77.app)
# Designed for robust release builds preserving high-fidelity UI assets, Lottie, Glide, and Retrofit 2

# === General AndroidX & Core ===
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,SourceFile,LineNumberTable
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**

# === RAMA77 Data Models & Serialization ===
# Preserve all data classes from being obfuscated or stripped by R8
-keep class com.rama77.app.data.models.** { *; }
-keepclassmembers class com.rama77.app.data.models.** {
    <fields>;
    <methods>;
}

# === Retrofit 2 & OkHttp 4 ===
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations

# === Google Gson Serialization ===
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !static !transient <methods>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
    @com.google.gson.annotations.Expose <fields>;
}

# === Airbnb Lottie Animation Library ===
# Preserve Lottie classes, composition loaders, and parser reflections
-keep class com.airbnb.lottie.** { *; }
-keep interface com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

# === Bumptech Glide Image Loader ===
# Preserve GlideModule, ModelLoaders, and Hardware Bitmap decoders
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl { *; }
-keepclassmembers class * extends com.bumptech.glide.module.AppGlideModule {
    <init>(...);
}
-keep public enum com.bumptech.glide.load.resource.bitmap.DownsampleStrategy$* {
    **[] $VALUES;
    public *;
}
-dontwarn com.bumptech.glide.**

# === Kotlin Coroutines & Flow ===
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler { *; }
-keepclassmembernames class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# === AndroidX Biometric & Security ===
-keep class androidx.biometric.** { *; }
-dontwarn androidx.biometric.**

# === Material Components & ViewBinding ===
-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
    public static ** inflate(...);
    public static ** bind(...);
    public ** getRoot();
}

# Preserve Native C++ Libraries (Uncompressed 4-byte aligned for APK stability)
-keepclasseswithmembernames class * {
    native <methods>;
}
