# https://www.guardsquare.com/en/proguard/manual/usage
# https://stackoverflow.com/questions/35321742/android-proguard-most-aggressive-optimizations

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-keepattributes *Annotation*,SourceFile,LineNumberTable,EnclosingMethod,Signature,Exceptions,InnerClasses,RuntimeVisibleAnnotations,AnnotationDefault

-dontnote okhttp3.**
-dontwarn android.arch.**
-dontwarn android.databinding.**
-dontwarn android.support.**
-dontwarn autovalue.shaded.com.**
-dontwarn autovalue.shaded.org.apache.commons.**
-dontwarn ca.barrenechea.header-decor.**
-dontwarn com.airbnb.**
-dontwarn com.airbnb.android.**
-dontwarn com.crashlytics.**
-dontwarn com.crashlytics.sdk.android.**
-dontwarn com.fasterxml.jackson.**
-dontwarn com.futurice.project.models.pojo.**w
-dontwarn com.gabrielittner.auto.value.**
-dontwarn com.getkeepsafe.dexcount.**
-dontwarn com.github.akarnokd.**
-dontwarn com.github.barteksc.**
-dontwarn com.github.bumptech.glide.**
-dontwarn com.github.clans.**
-dontwarn com.github.gcacace.**
-dontwarn com.github.rubensousa.**
-dontwarn com.github.stairs.**
-dontwarn com.github.stfalcon.**
-dontwarn com.google.**
-dontwarn com.google.android.gms.**
-dontwarn com.google.auto.value.**
-dontwarn com.google.dagger.**
-dontwarn com.google.j2objc.annotations.Weak
-dontwarn com.hootsuite.android.**
-dontwarn com.hypertrack.**
-dontwarn com.instabug.library.**
-dontwarn com.itextpdf.text.**
-dontwarn com.jakewharton.rxbinding.**
-dontwarn com.jakewharton.timber.**
-dontwarn com.letv.sarrsdesktop.**
-dontwarn com.mixpanel.android.**
-dontwarn com.ogaclejapan.smarttablayout.**
-dontwarn com.ryanharter.auto.value.**
-dontwarn com.squareup.**
-dontwarn com.squareup.leakcanary.**
-dontwarn com.tbruyelle.rxpermissions.**
-dontwarn com.theartofdev.edmodo.**
-dontwarn com.weiwangcn.betterspinner.**
-dontwarn com.wonderkiln.**
-dontwarn com.yarolegovich.**
-dontwarn eu.inloop.**
-dontwarn io.reactivex.**
-dontwarn it.sephiroth.android.exif.**
-dontwarn java.lang.ClassValue
-dontwarn java.nio.file.*
-dontwarn java.util.concurrent.**
-dontwarn java8.util.**
-dontwarn javax.annotation.**
-dontwarn javax.lang.**
-dontwarn javax.naming.**
-dontwarn javax.servlet.**
-dontwarn javax.tools.**
-dontwarn javax.xml.**
-dontwarn javax.xml.stream.events.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.apache.log.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.tools.**
-dontwarn org.apache.velocity.**
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn org.java.lang.**
-dontwarn org.jdom.**
-dontwarn org.slf4j.**
-dontwarn pl.charmas.android.**
-dontwarn retrofit2.**
-dontwarn retrofit2.Platform$Java8
-dontwarn sun.misc.**
-dontwarn sun.misc.Unsafe
-dontwarn org.apache.**
-dontwarn org.w3c.dom.**
-dontwarn com.fasterxml.jackson.databind.**

-keep class com.crashlytics.** { *; }
-keep public class * extends java.lang.Exception
-keep class com.letv.sarrsdesktop.** { *; }
-keep class com.hypertrack.** { *; }
-keep class com.crashlytics.** { *; }
-keep class com.crashlytics.sdk.android.** { *; }
-keep class com.github.gcacace.** { *; }
-keep class ca.barrenechea.header-decor.** { *; }
-keep class com.github.stfalcon.** { *; }
-keep class pl.charmas.android.** { *; }
-keep class it.sephiroth.android.exif.** { *; }
-keep class com.ogaclejapan.smarttablayout.** { *; }
-keep class com.theartofdev.edmodo.** { *; }
-keep class com.hootsuite.android.** { *; }
-keep class com.weiwangcn.betterspinner.** { *; }
-keep class com.wonderkiln.** { *; }
-keep class com.jakewharton.timber.** { *; }
-keep class eu.inloop.** { *; }
-keep class com.github.clans.** { *; }
-keep class com.airbnb.android.** { *; }
-keep class com.github.akarnokd.** { *; }
-keep class com.yarolegovich.** { *; }
-keep class com.mixpanel.android.** { *; }
-keep class com.github.stairs.** { *; }
-keep class com.instabug.library.** { *; }
-keep class com.github.barteksc.** { *; }
-keep class com.tbruyelle.rxpermissions.** { *; }
-keep class com.squareup.leakcanary.** { *; }
-keep class com.airbnb.** { *; }
-keep class com.gabrielittner.auto.value.** { *; }
-keep class com.ryanharter.auto.value.** { *; }
-keep class com.google.dagger.** { *; }
-keep class com.squareup.** { *; }
-keep class com.github.rubensousa.** { *; }
-keep class com.jakewharton.rxbinding.** { *; }
-keep class com.getkeepsafe.dexcount.** { *; }
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }
-keep class android.databinding.** { *; }
-keep class com.airbnb.deeplinkdispatch.** { *; }
-keep class com.fasterxml.jackson.** { *; }
-keep class com.itextpdf.text.** { *; }
-keep class io.reactivex.** { *; }
-keep class java.util.concurrent.**
-keep class java8.util.** { *; }
-keep class okhttp3.** { *; }
-keep class retrofit2.** { *; }
-keep class sun.misc.Unsafe { *; }
-keep interface okhttp3.** { *; }
-keep interface org.parceler.Parcel
-keep class com.futurice.project.models.pojo.** { *; }
-keep class sun.misc.Unsafe { *; }
-keep class com.google.** { *; }
-keep class com.github.bumptech.glide.** { *; }
-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep interface android.support.v4.** { *; }
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.DialogFragment
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.google.vending.licensing.ILicensingService
-keep class android.arch.** { *; }
-keep class * implements android.arch.lifecycle.LifecycleObserver { <init>(...); }
-keep class * implements android.os.Parcelable { public static final android.os.Parcelable$Creator *; }
-keep class rx.internal.util.unsafe.** { *; }
-keep public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(...);
}
-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keep @com.fasterxml.jackson.annotation.JsonIgnoreProperties class * { *; }
-keep class com.fasterxml.** { *; }
-keep class org.codehaus.** { *; }
-keep class * extends java.util.ListResourceBundle {
    protected java.lang.Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}
-keep public class com.turvo.driver.model.** { *; }
-keep class com.fasterxml.jackson.databind.ObjectMapper {
    public <methods>;
    protected <methods>;
}
-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}

-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepnames class * implements android.os.Parcelable { public static final ** CREATOR; }

-keepclasseswithmembernames class * { native <methods>; }
-keepclasseswithmembernames class com.fasterxml.jackson.** { *; }

-keepclasseswithmembers class * { @retrofit2.http.* <methods>; }
-keepclasseswithmembers class * { @com.airbnb.deeplinkdispatch.DeepLink <methods>; }
-keepclasseswithmembers class * { public <init>(android.content.Context, android.util.AttributeSet); }
-keepclasseswithmembers class * { public <init>(android.content.Context, android.util.AttributeSet, int);}
-keepclasseswithmembers class com.fasterxml.jackson.**  { *; }

-keepclassmembernames class * { @com.google.android.gms.common.annotation.KeepName *; }
-keepclassmembernames class com.fasterxml.jackson.**  { *; }

-keepclassmembers class * extends android.app.Activity { public void *(android.view.View); }
-keepclassmembers class **.R$* { public static <fields>; }
-keepclassmembers class * implements android.arch.lifecycle.LifecycleObserver { <init>(...); }
-keepclassmembers class * extends android.arch.lifecycle.ViewModel { <init>(...); }
-keepclassmembers class android.arch.lifecycle.Lifecycle$State { *; }
-keepclassmembers class android.arch.lifecycle.Lifecycle$Event { *; }
-keepclassmembers class * { @android.arch.lifecycle.OnLifecycleEvent *; }
-keepclassmembers class * implements android.arch.lifecycle.LifecycleObserver { <init>(...); }
-keepclassmembers class android.arch.** { *; }
-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
}
-keepclassmembers public final enum com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility {
    public static final com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility *;
}
-keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
    public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }
-keepclassmembers class com.fasterxml.jackson.**  { *; }
-keepclassmembers class * {
     @com.fasterxml.jackson.annotation.JsonCreator *;
     @com.fasterxml.jackson.annotation.JsonProperty *;
}

-assumenosideeffects class android.util.Log {
 public static *** d(...);
 public static *** w(...);
 public static *** i(...);
 public static *** v(...);
}

-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**
-dontwarn org.hamcrest.**
-dontwarn com.squareup.javawriter.JavaWriter
-dontwarn org.mockito.**