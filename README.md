# ToDo List App

A note taking app, using the Android Architecture Component libraries (`Room`, `ViewModel`, `LiveData`), Fragments, a RecyclerView and Java. The data will be stored in an SQLite database and supports `CRUD` operations.Together, this whole structure constitues an `MVVM (Model-View-ViewModel)` architecture, which follows the single responsibility and separation of concerns principles.

## Content

- [Why Android Architecture?](#why-android-architecture-components)
- [How Android Architecture Components Work](#how-android-architecture-components-work)
- [Dependencies](#build-dependencies)
- [Demo](#demonstration)

## Why Android Architecture Components?

Android architecture components are a collection of libraries that help you design robust, testable, and maintainable apps. Start with classes for managing your UI component lifecycle and handling data persistence.

* Manage your app's lifecycle with ease. New `lifecycle-aware components` help you manage your activity and fragment lifecycles. Survive configuration changes, avoid memory leaks and easily load data into your UI.
* Use `LiveData` to build data objects that notify views when the underlying database changes.
* `ViewModel` Stores UI-related data that isn't destroyed on app rotations.
* `Room` is an a SQLite object mapping library. Use it to Avoid boilerplate code and easily convert SQLite table data to Java objects. Room provides compile time checks of SQLite statements and can return RxJava, Flowable and LiveData observables.

## How Android Architecture Components work?

![](/screenshots/android_components.png)

**Entity**: When working with Architecture Components, this is an annotated class that describes a database table.

**SQLite database**: On the device, data is stored in an SQLite database. For simplicity, additional storage options, such as a web server, are omitted. The Room persistence library creates and maintains this database for you.

**DAO**: Data access object. A mapping of SQL queries to functions. You used to have to define these painstakingly in your SQLiteOpenHelper class. When you use a DAO, you call the methods, and Room takes care of the rest.

**Room database**: Database layer on top of SQLite database that takes care of mundane tasks that you used to handle with an SQLiteOpenHelper. Database holder that serves as an access point to the underlying SQLite database. The Room database uses the DAO to issue queries to the SQLite database.

**Repository**: A class that you create, for example using the WordRepository class. You use the Repository for managing multiple data sources.

**ViewModel**: Provides data to the UI. Acts as a communication center between the Repository and the UI. Hides where the data originates from the UI. ViewModel instances survive configuration changes.

**LiveData**: A data holder class that can be observed. Always holds/caches latest version of data. Notifies its observers when the data has changed. LiveData is lifecycle aware. UI components just observe relevant data and don't stop or resume observation. LiveData automatically manages all of this since it's aware of the relevant lifecycle status changes while observing.

## Build Dependencies

Following dependencies is added to `buil.gradle(Module:app)`:

   1. Room Dependency

    dependencies {

    def room_version = "2.2.5"

    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"

    }

   2. Dependencies for Lifecycle, including LiveData and ViewModel.

     dependencies {

     def lifecycle_version = "2.2.0"

     <!---ViewModel -->
     implementation "androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version"
     <!--- LiveData -->
     implementation "androidx.lifecycle:lifecycle-livedata:$lifecycle_version"
     <!--- Saved state module for ViewModel -->
     implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
     <!--- Annotation processor -->
     annotationProcessor "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"

     }

# Demonstration

1. Adding a Category

A new category can be added under the navigation menu where by clicking the navigation icon opens the navigation menu and under the navigation menu by clicking Add Category item.

![](/screenshots/addcategory.gif)

2. Adding New Task

New task can be added under the Category fragment. In the navigation menu we can click any category added and a category fragment opens under the category fragment there is floating button to add the task. By clicking the button a view opens where the required information can be added to the field and by the clicking the icon with tick mark new task will be added.

![](/screenshots/addtask.gif)

3. Editing Category

Category can be edited from the category fragment. In the category fragment on top in the toolbar a pen like icon can be seen by clicking the icon the category can be edited.

![](/screenshots/editcategory.gif)

4. Delete Category

Category can be delete by following the same processs as of editing category. Here, in the view for editing category we just have to click on bin like icon.

![](/screenshots/deletecategory.gif)

5. Edit Task

Task can be edited from the home or category fragment. Just click on the task and the view for editing the task opens where we can edit the items in the view and save.

![](/screenshots/edittask.gif)

6. Delete Task

Task can be deleted from edit task view.

![](/screenshots/deletetask.gif)

7. Swipe to delete Task

Tasks can be easily deleted by simpling swiping the task right or left. The current task deleted with swipe can be restored simply by clicking the undo appeared as toast message.

![](/screenshots/swipetodelete.gif)
![](/screenshots/swipe_to_delete.gif)

8. Share Text

Description text from the task can be simply shared with messaging apps through use of implicit intent.

![](/screenshots/share_text.gif)

9. Horizontal Orientation

The app adjust in landscape orientation also.

![](/screenshots/horizontalOrientation.gif)