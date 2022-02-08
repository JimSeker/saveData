Save data Examples
===========
`eclipse/` has the example in the eclipse format (no longer updated).  otherwise all examples are using Android Studio.

`legacy/` these are examples that are no longer updated.  May still be useful, since they deal with android 2.3.3

---

**Preference Examples:**

`PerferenceDemo` uses the preference fragments to save user preference data.  Note, there are no support libraries used for fragment or preferences. Also, this is for API 16+, for older versions, see the legacy directory example.  This example will maintained but not updated. 

`SupportPerferenceDemo` (java) uses the androidx preferences fragments to save user preference data.  This is a translation of the PerferenceDemo to the newer androidx versions.  

`SupportPerferenceDemo_kt` (kotlin) uses the androidx preferences fragments to save user preference data.  This is a translation of the PerferenceDemo to the newer androidx versions.  

---

**Shared Preference Examples:**

`saveDataDemo` (java) shows an example of using the instance bundle,  the shared preference system to store data, and viewmodel for when the app is rotated, and for long term vs short term data storage.

`saveDataDemo_kt` (kotlin) shows an example of using the instance bundle,  the shared preference system to store data, and viewmodel for when the app is rotated, and for long term vs short term data storage.

`File system examples:`

`fileSystemDemo` shows how to read/write to local private and public directory. The external has been removed, since it won't work in API 30 (see legacy).  A new example will be needed for the sdcard, when I can find documentation that actually works (I looking at you google/android, poor documentation).

`fileSystemMediaStoreDemo` show you how to access the DCIM and pictures directory (easily changes to video).  This allows you to access the media on the SDcard, but any other file types.

---

**Sqlite Examples:** 

Note Room Examples are in the [Architecture repo](https://github.com/JimSeker/Architecture)

`ContentProDemo` shows how to create a simple context provider and how to access it
Also has an example of accessing the contacts provider.

`sqliteDBDemo` (java) an SupportSQLiteDatabase is created and used.  The data is displayed via a recyclerview.  While the code has
 insert, update, delete, and query.  Only the query and insert methods are called by this example.

`sqliteDBDemo_kt` (kotlin) an SupportSQLiteDatabase is created and used.  The data is displayed via a recyclerview.  While the code has
 insert, update, delete, and query.  Only the query and insert methods are called by this example.

`sqliteDBViewModelDemo` (java) uses a SupportSQLiteDatabase, enters data and displays it via a recyclerview.  It uses a ViewModel to update the display when data is added.  It also could delete and update data in the database, but the example does not actually use them.

`sqliteDBViewModelDemo_kt` (kotlin) uses a SupportSQLiteDatabase, enters data and displays it via a recyclerview.  It uses a ViewModel to update the display when data is added.  It also could delete and update data in the database, but the example does not actually use them.

`sqliteDemo` creates a supportSQLitedatabase and content provider

`sqliteDemo2` uses the content provider from sqlitedemo, also shows how to use a loader.

`sqliteDemo3` uses the content provider from sqlitedemo and recyclerview.  Uses a custom cursoradapter for it and a loader as well.

`sqliteDemo4` uses the content provider from sqlitedemo and recyclerview.  Uses a custom LiveData, plus a modelview to replace the deprecated Loaders.

`lvCursorDemo` show how to use cursoradapters with listview and explistview.

---

These are example code for University of Wyoming, Cosc 4730 Mobile Programming course and cosc 4735 Advance Mobile Programing course. 
All examples are for Android.
