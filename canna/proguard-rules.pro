# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /ANDROID_SDK_PATH/tools/proguard/proguard-android.txt
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

# Required for Square
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn retrofit.**

# Required for GSON
-keep class com.shopify.buy.model.** { *; }
-keep class com.shopify.canna.** {*; }
-keep class com.canna.** {*; }
-keep class com.facebook.** {*; }
-keep class com.android.** {*; }
-keep class com.jakewharton.** {*; }
-keep class com.squareup.** {*; }
-keep class com.jsibbold.** {*; }

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
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn retrofit2.Platform$Java8

# Mapbox
#-keep class com.mapbox.android.telemetry.**
#-keep class com.mapbox.android.core.location.**
-keep class android.arch.lifecycle.** { *; }
#-keep class com.mapbox.android.core.location.** { *; }
#-keep class com.mapbox.mapboxsdk.** { *; }

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
#-dontpreverify
-keepattributes *Annotation*
-verbose
-dontoptimize
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dontwarn android.support.v4.**
#-dontwarn com.mmi.**
-dontwarn com.facebook.internal.**
-dontwarn com.google.common.util.concurrent.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.logging.impl.**
-ignorewarnings

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class org.apache.commons.logging.**
-keep public class org.apache.commons.logging.impl.**
-keep public class com.mmi.navigation.NavigationFormatter{
<fields>;
    <methods>;
}
#-keep public class com.mmi.apis.NearbyResultWrapper {
#    <fields>;
#    <methods>;
#}
#
#-keep public class * extends com.mmi.navigation.Mapctivity{
#    public <fields>;
#}


-keeppackagenames org.jsoup.nodes
#-keep class com.mmi.apis.place.** {
#    <fields>;
#    <methods>;
#}
#
#
#-keep class com.mmi.apis.model.** {
#    <fields>;
#    <methods>;
#}
#-keep class  com.mmi.apis.utils.URLs {
#    <fields>;
#    <methods>;
#}
#
#-keep class com.mmi.apis.iface.** {
#    <fields>;
#    <methods>;
#}
#-keep class com.mmi.textinstructions.** {
#    <fields>;
#    <methods>;
#}
#-keep class com.mmi.apis.controller.** {
#    <fields>;
#    <methods>;
#}
#-keep class com.mmi.navigation.util.ManeuverInfo {
#    <fields>;
#    <methods>;
#}
#-keep class com.mmi.navigation.iface.** {
#    <fields>;
#    <methods>;
#}

#-dontwarn com.mmi.navigation.routing.RouteDirectionInfo
#
#-keep class com.mmi.navigation.routing.RouteDirectionInfo {
#   *;
#   private *** extraInfo*;
#}
#-keep class com.mmi.navigation.NavigationApplication{
#   public com.mmi.apis.model.NavigationPath getCalculatedRoute();
#   public com.mmi.navigation.NavigationLocationProvider getLocationProvider();
#   public com.mmi.navigation.NavLocation getSecondLocation();
#   public com.mmi.navigation.NavLocation getFirstLocation();
#   public com.mmi.apis.model.AdviseInfo getAdviseInfo();
#   public boolean isNavigating();
#   public com.mmi.navigation.NavLocation setCurrentLocation(com.mmi.navigation.NavLocation, boolean);
#   public void clearPreviousTrackData();
#   public void stopNavigation();
#   public void mute(boolean);
#   public boolean isMute();
#  # public List<com.mmi.navigation.routing.RouteDirectionInfo> getRouteDirections();
#}

#-keep class com.mmi.navigation.MapActivity{
#public <fields>;
#public <methods>;
#    protected abstract void onAfterMapReady(com.mapbox.mapboxsdk.maps.MapboxMap, com.mapbox.mapboxsdk.maps.MapView);
#}


#-keepnames class com.mmi.navigation.NavigationApplication

#-keep class com.mmi.navigation.MapmyIndiaNavigationHelper {
#    <fields>;
#    <methods>;
#}
#-keep class com.mmi.navigation.NavigationLocationProvider {
#    <fields>;
#    <methods>;
#}
#-keep class com.mmi.navigation.NavLocation {
#    <fields>;
#    <methods>;
#}
#-keep class com.mapbox.services.android.navigation.** {
#    *;
#}
#-keepnames class com.mmi.navigation.notifications.NavigationNotification
#
#-keep class com.mmi.navigation.notifications.NavigationNotification {
#    *;
#}



#-keep class com.mmi.navigation.TVSVolley {
#    <fields>;
#    <methods>;
#}
#-keep class com.mmi.navigation.gpx.GPXDataModel {
#    <fields>;
#    <methods>;
#}
#-keep class com.android.volley.mmi.toolbox.GsonRequest {
#    <fields>;
#    <methods>;
#}
#-keep class com.mmi.navigation.util.Connectivity {
#    <fields>;
#    <methods>;
#}
#-keep class com.mmi.navigation.util.GPSInfo {
#    <fields>;
#    <methods>;
#}
#-keep class com.mmi.navigation.routing.RouteCalculationResult {
#    <fields>;
#    <methods>;
#}
#
#-keep class com.mmi.beacon.traffic.db.ProbeLocation {
#    <fields>;
#    <methods>;
#}
#
#-keep class com.mmi.beacon.traffic.model.** {
#    <fields>;
#    <methods>;
#}

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}

-dontwarn kotlin.**

# Retrofit 2
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes *Annotation*,Signature
# Retain declared checked exceptions for use by a Proxy instance.
 -keepattributes Exceptions
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-dontwarn sun.misc.**
-dontwarn okio.**
-dontwarn okhttp3.**
-keep class retrofit.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
 @retrofit.http.* <methods>;
}

-keep class com.daimajia.** { *; }
-dontwarn com.daimajia.**
-keepnames class com.daimajia.**

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

#Proguard rules for mapmyindia sdk
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Retrofit 2
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes *Annotation*,Signature
# Retain declared checked exceptions for use by a Proxy instance.
 -keepattributes Exceptions
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-dontwarn sun.misc.**
-dontwarn okio.**
-dontwarn okhttp3.**
-keep class retrofit.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
 @retrofit.http.* <methods>;
}

# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\asheq\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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

# Retrofit
-keep class com.google.gson.** { *; }
-keep public class com.google.gson.** {public private protected *;}
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }
-keep class javax.xml.stream.** { *; }
-keep class retrofit.** { *; }
-keep class com.google.appengine.** { *; }
-keepattributes *Annotation*
-keepattributes Signature
-dontwarn com.squareup.okhttp.*
-dontwarn rx.**
-dontwarn javax.xml.stream.**
-dontwarn com.google.appengine.**
-dontwarn java.nio.file.**
-dontwarn org.codehaus.**



-dontwarn retrofit2.**
-dontwarn org.codehaus.mojo.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations

-keepattributes EnclosingMethod
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclasseswithmembers interface * {
    @retrofit2.* <methods>;
}
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions



# Add any classes the interact with gson
# the following line is for illustration purposes
-keep class com.example.asheq.zanis_postmans.ListAddressesActivity
-keep class com.example.asheq.zanis_postmans.ListOrderActivity
-keep class com.example.asheq.zanis_postmans.LoginActivity
-keep class com.example.asheq.zanis_postmans.SendReportsActivity
-keep class com.example.asheq.track.TrackLocationService
-keep class com.example.asheq.track.TrackLocationApplication
-keep class com.example.asheq.models.** { *; }



# Hide warnings about references to newer platforms in the library
-dontwarn android.support.v7.**
# don't process support library
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.MapActivity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
# To support Enum type of class members
-keepclassmembers enum * { *; }

-keep class com.activeandroid.** { *; }
-keep class com.activeandroid.**.** { *; }
-keep class * extends com.activeandroid.Model
-keep class * extends com.activeandroid.serializer.TypeSerializer

-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keep class com.mmi.services.account.** {
    <fields>;
    <methods>;
}
-keep class com.mmi.services.api.** {
    <fields>;
    <methods>;
}
-keep class com.mmi.services.utils.** {
    <fields>;
    <methods>;
}
-dontwarn kotlin.reflect.jvm.internal.**

-dontwarn javax.**
-dontwarn lombok.**
-dontwarn org.apache.**
-dontwarn com.squareup.**
-dontwarn com.sun.**
-dontwarn **retrofit**

-keep public class * extends androidx.recyclerview.widget.RecyclerView$LayoutManager {
    public <init>(android.content.Context, android.util.AttributeSet, int, int);
    public <init>();
}
-keepclassmembers class androidx.recyclerview.widget.RecyclerView {
    public void suppressLayout(boolean);
    public boolean isLayoutSuppressed();
}

# CoordinatorLayout resolves the behaviors of its child components with reflection.
-keep public class * extends androidx.coordinatorlayout.widget.CoordinatorLayout$Behavior {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>();
}

# Make sure we keep annotations for CoordinatorLayout's DefaultBehavior
-keepattributes RuntimeVisible*Annotation*

# keep setters in VectorDrawables so that animations can still work.
-keepclassmembers class android.support.graphics.drawable.VectorDrawableCompat$* {
   void set*(***);
   *** get*();
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-dontwarn rx.internal.util.unsafe.**
#-keepnames class rx.*

# Keep GSON stuff
-keep class sun.misc.Unsafe { *; }
-keep class sun.misc.Unsafe.** { *; }
-keep class com.google.gson.** { *; }

# Keep these for GSON and Jackson
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod

-keep class android.support.multidex.MultiDexApplication {
    <init>();
    void attachBaseContext(android.content.Context);
}

-keep public class * extends android.app.backup.BackupAgent {
    <init>();
}

-dontwarn androidx.annotation.**

# Retrofit
-keep class com.google.gson.** { *; }
-keep public class com.google.gson.** {public private protected *;}
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }
-keep class javax.xml.stream.** { *; }
-keep class retrofit.** { *; }
-keep class com.google.appengine.** { *; }
-keepattributes *Annotation*
-keepattributes Signature
-dontwarn com.squareup.okhttp.*
-dontwarn rx.**
-dontwarn javax.xml.stream.**
-dontwarn com.google.appengine.**
-dontwarn java.nio.file.**
-dontwarn org.codehaus.**


##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
# -keep class mypersonalclass.data.model.** { *; }

-keep class com.google.errorprone.annotations.** { *; }

# support constraint
-dontwarn android.support.constraint.**
-keep class android.support.constraint.** { *; }
-keep interface android.support.constraint.** { *; }
-keep public class android.support.constraint.R$* { *; }

-dontwarn org.jetbrains.annotations.**

-ignorewarnings

-keepattributes *Annotation*

-dontnote junit.framework.**
-dontnote junit.runner.**

-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**
-dontwarn org.hamcrest.**
-dontwarn com.squareup.javawriter.JavaWriter

-dontwarn org.mockito.**

-keepclassmembers,allowobfuscation class * extends androidx.lifecycle.ViewModel {
    <init>(androidx.lifecycle.SavedStateHandle);
}
-keepclassmembers,allowobfuscation class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application,androidx.lifecycle.SavedStateHandle);
}

-dontwarn kotlin.**

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-keep class android.support.design.widget.** { *; }
-keep interface android.support.design.widget.** { *; }
-dontwarn android.support.design.**
-keep public class * extends android.support.design.widget.CoordinatorLayout$Behavior {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# ServiceLoader support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Same story for the standard library's SafeContinuation that also uses AtomicReferenceFieldUpdater
-keepclassmembernames class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**



-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

-keep class com.google.firebase.quickstart.database.java.viewholder.** {
    *;
}

-keepclassmembers class com.google.firebase.quickstart.database.java.models.** {
    *;
}

-dontwarn com.appsflyer.**
-keep class com.appsflyer.** { *; }
-dontwarn com.google.firebase.appindexing.**
-dontwarn com.android.installreferrer.api.**

-keep class com.google.android.gms.internal.** { *; }

-dontwarn com.github.ome450901.SimpleRatingBar.**

-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

-keep class com.tooltip.TooltipActionView { *; }


-keep class com.logicbeanzs.uberpolylineanimation { *; }

-keep class cz.msebera.android.httpclient.** { *; }
-keep class com.loopj.android.http.** { *; }

-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepnames class com.facebook.FacebookActivity
-keepnames class com.facebook.CustomTabActivity

-keep class com.facebook.login.Login

-keep class me.relex.circleindicator.** {*;}
-dontwarn me.relex.circleindicator.**

### Reactive Network
-dontwarn com.github.pwittchen.reactivenetwork.library.ReactiveNetwork
-dontwarn io.reactivex.functions.Function
-dontwarn rx.internal.util.**
-dontwarn sun.misc.Unsafe

-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.** {*;}


-dontwarn com.mindorks.android.prdownloader.**
-keep class com.mindorks.android.prdownloader.** {*;}

-keepnames public class com.google.android.flexbox.FlexboxLayoutManager

# Keep custom model classes
-keep class com.google.firebase.example.fireeats.java.model.** { *; }
-keep class com.google.firebase.example.fireeats.kotlin.model.** { *; }

# https://github.com/firebase/FirebaseUI-Android/issues/1175
-dontwarn okio.**
-dontwarn retrofit2.Call
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-keep class android.support.v7.widget.RecyclerView { *; }

-keep class com.squareup.okhttp.** {*;}
-dontwarn com.squareup.okhttp.**

-keep class com.firebase.**{ *; }


## Some Common Proguard Rules
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,class/unboxing/enum
-ignorewarnings
-allowaccessmodification
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-repackageclasses

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclasseswithmembernames public class * { *; }

-keepclassmembers class **.R$* {
  public static <fields>;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.blusmart.rider.data.api.models.** { *; }
-keep class com.blusmart.rider.BluSmartApplication
-keep class com.blusmart.rider.blu_utils.AnalyticsUtils
-keep class com.blusmart.rider.common.MapAnimator {
    void set*(***);
    *** get*();
}
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepattributes JavascriptInterface
-keepattributes *Annotation*

-dontwarn com.razorpay.**
-keep class com.razorpay.** {*;}
-keep class com.razorpay.AnalyticsUtil{
    void set*(***);
    *** get*();
}
-optimizations !method/inlining/*

-keepclasseswithmembers class * {
  public void onPayment*(...);
}


-dontwarn com.google.android.gms.location.**
-keep class com.google.android.gms.location.** { *; }

-dontwarn com.moe.**
-dontwarn com.moengage.**
-keep class com.moe.** { *; }
-keep class com.moengage.** { *; }

-keep class com.delight.**  { *; }

-keep class com.readystatesoftware.chuck.internal.data.HttpTransaction { *; }
-keep class android.support.v7.widget.SearchView { *; }
-keep class com.shopify.canna { *; }
-keep class com.canna { *; }

# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep,allowobfuscation @interface com.facebook.soloader.DoNotOptimize

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Do not strip any method/class that is annotated with @DoNotOptimize
-keep @com.facebook.soloader.DoNotOptimize class *
-keepclassmembers class * {
    @com.facebook.soloader.DoNotOptimize *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

# Do not strip SoLoader class and init method
-keep public class com.facebook.soloader.SoLoader {
    public static void init(android.content.Context, int);
}

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**