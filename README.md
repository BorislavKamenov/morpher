## Morpher
Android library for animated transition between numbers using animated vector drawables and morphing.

![](http://i.giphy.com/3oriNOmApXPb3x6oVi.gif)

## How to use
Add Gradle dependency:

```gradle
dependencies {
   compile 'com.kamenov.morpher:morpher:1.0.0'
}
```

Then add Morpher in your layout:

```xml
<com.kamenov.android.morpher.Morpher
        android:id="@+id/morpher"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"/>
```

## Attributes

#### XML
* `app:duration` - transition duration in milliseconds (DEFAULT:  550)
* `app:strokeWidth` - stroke width for the digits (DEFAULT:  20)
* `app:zeroFiller` - count of leading zeroes (DEFAULT:  1)
* `app:textColor` - color for digits (DEFAULT:  BLACK)

#### Java
* `morpher.setNumber()` - transition to the digit   
* `morpher.setZeroFiller()` - count of leading zeroes
* `morpher.setStrokeWidth()` - stroke width for the digits
* `morpher.setDuration()` - transition duration in milliseconds
* `morpher.setTextColor()` - color for digits

For full example check `app` module.

