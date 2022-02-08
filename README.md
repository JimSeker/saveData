Save data Examples
===========
<b>eclipse/</b> has the example in the eclipse format (no longer updated).  otherwise all examples are using Android Studio.

<b>legacy/</b> these are examples that are no longer updated.  May still be useful, since they deal with android 2.3.3

<b>Preference Examples: </b>

<b>PerferenceDemo</b> uses the preference fragments to save user preference data.  Note, there are no support libraries used for fragment or preferences. Also, this is for API 16+, for older versions, see the legacy directory example.

<b>SupportPerferenceDemo</b> uses the androidx.prefernces.  It's similar to perferencedemo, using similar preferences and using live updates as well.

<b>Shared Preference Examples:</b>

<b>saveDataDemo</b> (java) shows an example of using the instance bundle,  the shared preference system to store data, and viewmodel for when the app is rotated, and for long term vs short term data storage.

<b>saveDataDemo_kt</b> (kotlin) shows an example of using the instance bundle,  the shared preference system to store data, and viewmodel for when the app is rotated, and for long term vs short term data storage.

<b>File system examples:</b>

<b>fileSystemDemo</b> shows how to read/write to local private and public directory. The external has been removed, since it won't work in API 30 (see legacy).  A new example will be needed for the sdcard, when I can find documentation that actually works (I looking at you google/android, poor documentation).

<b>fileSystemMediaStoreDemo</b> show you how to access the DCIM and pictures directory (easily changes to video).  This allows you to access the media on the SDcard, but any other file types.

<b>Sqlite Examples:</b> 

Note Room Examples are in the Architecture repo, https://github.com/JimSeker/Architecture

<b>ContentProDemo</b> shows how to create a simple context provider and how to access it
Also has an example of accessing the contacts provider.

<b>sqliteDBDemo</b> (java) an SupportSQLiteDatabase is created and used.  The data is displayed via a recyclerview.  While the code has
 insert, update, delete, and query.  Only the query and insert methods are called by this example.

<b>sqliteDBDemo_kt</b> (kotlin) an SupportSQLiteDatabase is created and used.  The data is displayed via a recyclerview.  While the code has
 insert, update, delete, and query.  Only the query and insert methods are called by this example.

<b>sqliteDBViewModelDemo</b> (java) uses a SupportSQLiteDatabase, enters data and displays it via a recyclerview.  It uses a ViewModel to update the display when data is added.  It also could delete and update data in the database, but the example does not actually use them.

<b>sqliteDBViewModelDemo_kt</b> (kotlin) uses a SupportSQLiteDatabase, enters data and displays it via a recyclerview.  It uses a ViewModel to update the display when data is added.  It also could delete and update data in the database, but the example does not actually use them.

<b>sqliteDemo</b> creates a supportSQLitedatabase and content provider

<b>sqliteDemo2</b> uses the content provider from sqlitedemo, also shows how to use a loader.

<b>sqliteDemo3</b> uses the content provider from sqlitedemo and recyclerview.  Uses a custom cursoradapter for it and a loader as well.

<b>sqliteDemo4</b> uses the content provider from sqlitedemo and recyclerview.  Uses a custom LiveData, plus a modelview to replace the deprecated Loaders.

<b>lvCursorDemo</b> show how to use cursoradapters with listview and explistview.

These are example code for University of Wyoming, Cosc 4730 Mobile Programming course and cosc 4735 Advance Mobile Programing course. 
All examples are for Android.
