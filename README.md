Save data Examples
===========
<b>eclipse/</b> has the example in the eclipse format (no longer updated).  otherwise all examples are using Android Studio.

<b>legacy/</b> these are examples that are no longer updated.  May still be useful, since they deal with android 2.3.3

<b>PerferenceDemo</b> uses the preference fragments to save user preference data.  Note, there are no support libraries used for fragment or preferences. Also, this is for API 16+, for older versions, see the legacy directory example.

<b>SupportPerferenceDemo</b> uses the androidx.prefernces.  It's similar to perferencedemo, using similar preferences and using live updates as well.

<b>saveDataDemo</b> shows an example of using the instance bundle and the shared preference system to store data.

<b>ContentProDemo</b> shows how to create a simple context provider and how to access it
Also has an example of accessing the contacts provider.

<b>fileSystemDemo</b> shows how to read/write to local private and public directory. The external has been removed, since it won't work in API 30 (see legacy).  A new example will be needed for the sdcard, when I can find documentation that actually works (I looking at you google/android, poor documentation).

<b>sqliteDBDemo</b> an SupportSQLiteDatabase is created and used.  The data is displayed via a recyclerview.  While the code has
 insert, update, delete, and query.  Only the query and insert methods are called by this example.

<b>sqliteDemo</b> creates a supportSQLitedatabase and content provider

<b>sqliteDemo2</b> uses the content provider from sqlitedemo, also shows how to use a loader.

<b>sqliteDemo3</b> uses the content provider from sqlitedemo and recyclerview.  Uses a custom cursoradapter for it and a loader as well.

<b>sqliteDemo4</b> uses the content provider from sqlitedemo and recyclerview.  Uses a custom LiveData, plus a modelview to replace the deprecated Loaders.

<b>lvCursorDemo</b> show how to use cursoradapters with listview and explistview.

These are example code for University of Wyoming, Cosc 4730 Mobile Programming course.
All examples are for Android.
