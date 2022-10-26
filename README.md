Save data Examples
===========
`eclipse/` has the example in the eclipse format (no longer updated).  otherwise all examples are using Android Studio.

`legacy/` these are examples that are no longer updated.

---

**Preference Examples:**

`PerferenceDemo` uses the preference fragments to save user preference data.  Note, there are no support libraries used for fragment or preferences. Also, this is for API 16+, for older versions, see the legacy directory example.  This example will maintained but not updated. 

`SupportPerferenceDemo` (java) uses the androidx preferences fragments to save user preference data.  This is a translation of the PerferenceDemo to the newer androidx versions.  

`SupportPerferenceDemo_kt` (kotlin) uses the androidx preferences fragments to save user preference data.  This is a translation of the PerferenceDemo to the newer androidx versions.  

---

**Shared Preference Examples:**

`saveDataDemo` (java) shows an example of using the instance bundle,  the shared preference system to store data, and viewmodel for when the app is rotated, and for long term vs short term data storage.

`saveDataDemo_kt` (kotlin) shows an example of using the instance bundle,  the shared preference system to store data, and viewmodel for when the app is rotated, and for long term vs short term data storage.

---

**File system examples:**

`fileSystemDemo` shows how to read/write to local private and public directory. The external has been removed, since it won't work in API 30 (see legacy).  A new example will be needed for the sdcard, when I can find documentation that actually works (I looking at you google/android, poor documentation).

`fileSystemMediaStoreDemo` show you how to access the DCIM and pictures directory (easily changes to video).  This allows you to access the media on the SDcard, but any other file types.

`fileSystemMediaStoreRecAudioDemo` This example uses the mediastore to record and play files on the music directory.  actually in a subdirectory, music/recording.  There is a picker for filenames and to choose which files to play.  Note, you will need to record at least one file in order to play something.

`ScreenShotDeetectorDemo`  This is a first attempt out detecting screenshots using a content observer.  It works in api32 pretty well and fails in api 33.  likely need to fix the permissions for api 33.  


---

**Sqlite Examples:** 

Note Room Examples are in the [Architecture repo](https://github.com/JimSeker/Architecture)

`sqliteDBDemo` (java) an SupportSQLiteDatabase is created and used.  The data is displayed via a recyclerview.  While the code has
 insert, update, delete, and query.  Only the query and insert methods are called by this example.

`sqliteDBDemo_kt` (kotlin) an SupportSQLiteDatabase is created and used.  The data is displayed via a recyclerview.  While the code has
 insert, update, delete, and query.  Only the query and insert methods are called by this example.

`sqliteDBViewModelDemo` (java) uses a SupportSQLiteDatabase, enters data and displays it via a recyclerview.  It uses a ViewModel to update the display when data is added.  It also could delete and update data in the database, but the example does not actually use them.

`sqliteDBViewModelDemo_kt` (kotlin) uses a SupportSQLiteDatabase, enters data and displays it via a recyclerview.  It uses a ViewModel to update the display when data is added.  It also could delete and update data in the database, but the example does not actually use them.

`lvCursorDemo` show how to use cursoradapters with listview and explistview.

---

**Content Provider Examples:** 

`ContentProDemo` shows how to create a simple context provider and how to access it.  Also has an example of accessing the contacts provider.

`ContentProSQliteDBDemo` shows how to create a content provider using a SQLite database.   displays in a listview, recyclerview and a spinner.

`ContentProviderRemoteDemo` connects to a "remote" content provider.  either the ContentProSQliteDBDemo or ContentProviderRoomDemo in [Architecture repo](https://github.com/JimSeker/Architecture).  It then will display the data in recyclerview and can add data via a FAB.

---

These are example code for University of Wyoming, Cosc 4730 Mobile Programming course and cosc 4735 Advance Mobile Programing course. 
All examples are for Android.
