<p align="center">
    <a href="https://github.com/nerdloco/WeatherApp/ReadMe">
    <img src= "https://github.com/nerdloco/WeatherApp/blob/master/app/src/main/res/mipmap-xxhdpi/ic_launcher_foreground.png" alt="Weather App" width=250 height=250>
  </a>
  <h3 align="center">Weather App</h3>
</p>


### Table of contents

**1 [About the Project](#about-the-project)**<br>
**2 [General Preview](#general-preview)**<br>
**3 [Technical](#technical)**<br>
**4 [To Do List](#to-do-list)**<br>
**5 [Contributing](#contributing)**<br>
**6 [License](#license)**<br>
**7 [Versions](#versions)**<br>
**8 [Contact Information](#contact-information)**<br>

:point_down: :point_down: :point_down: :point_down: :point_down:

## General Preview
Screenshots

<img src="https://github.com/nerdloco/WeatherApp/blob/master/art/pic1.png" width="30%"> <img src="https://github.com/nerdloco/WeatherApp/blob/master/art/pic2.png" width="30%"> <img src="https://github.com/nerdloco/WeatherApp/blob/master/art/pic4.png" width="30%">
## Prequisite
<p>

Before running the project create a [firebase project](https://firebase.google.com/)  and replace **google-services.json** with yours in the `app` directory for a successful build and 
enable email optio.<br>Also please create an account on [WeatherMapApi] (https://openweathermap.org/api) and obtain an api key.

</p>

## About the App
<p>
An android app which consumes [WeatherMapAPi] (https://openweathermap.org/api) to display weather data of different cities.<br>It is built with clean architecture principles, Repository pattern & MVVM patterns as well as Architecture Components.<br>
Build system:[Gradle] (https://gradle.org/)
</p>
Star, share ... :wink:
## Technical

The Application is split into a three layer architecture:
- Presentation
- Domain
- Data

This provides better abstractions between framework implementations 
and the underlying business logic.It requires a number of classes to get 
things running but the pros outweigh the cons in terms of building an app 
that should scale.

The 3 layered architectural approach is majorly guided by clean architecture which provides
a clear separation of concerns with its Abstraction Principle.

The `domain` and `data` layers are java module libraries as the business 
logic does not rely on the Android frameworks concrete implementations.

#### Presentation

The application presentation layer contains the two Activities and 
and one ViewModel. The dependency injection in this case is handled by the App class.

The UI layer contains, the ForecastViewModel and the MainScreen Activity.

The ViewModel then receives data from the ForecastRepo and updates the 
LiveData being observed by the activity,the Activity then makes updates 
to the UI. The ViewModel uses RxJava, to: observe the observable data gotten from the api in a background thread,
then subscribe to the observable and get the results in the main thread. The results are then presented in form of live data to the MainScreen activity.
The MainScreen Activity observes the live data fetched from the internet and presents it to the user.

The UI utilises a **State pattern** by representing expected view states using The Resource class and Status enum.
This aids with delegating logic operations  to the Viewmodel easier.

#### Models/Domain

The domain layer contains domain model classes which represent the
data we will be handling across presentation and data layer.

In this case, we have two models i.e The ForecastResponse and The ForecastDataUi,each serving a particular purpose.
The ForecastReponse model maps the json data into a plain kotlin object , then it is converted to the ForecastDataUi model which serves the viewModel with data thus ensuring total separation.

#### Data

The Data layer using the **Repository Pattern**  will be able to 
provide data to the defined use cases which in this case is searching
for a city's weather data and viewing details of the city's data.

The repository class acts as the single data source, in which it provides remote data fetched from the api.
The class also mapes the json data gotten to their domain representations. 
Then the domain represetation is mapped into the ui reprsentation ,which in turn is fed to the viewModel as an Observable.
All this happend in an io thread.

### How to use
You can:

 a. Clone the project and compile it yourself in android studio(Most up to date)
 
 ### Running the project
**1. Required to run project:**

 - Use Android studio 3.* and later. It will be less messy.

         - Gradle version used: gradle 5.4.1

          - gradle plugin used 3.53

          - kotlin 1.3.61

          SDK version API 29

          Build tools 29.0.2

**2. Clone this repository:**

    `git clone https://github.com/nerdloco/WeatherApp.git`

**3. open Project in Android Studio**

**4. Build Project**

**5. Incase of an eror when building project, update your gradle version, Build Tools download**


#### Dependencies



 [Jetpack](https://developer.android.com/jetpack)🚀
  - [Viewmodel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Manage UI related data in a lifecycle conscious way 
  and act as a channel between use cases and ui
  - [Data Binding](https://developer.android.com/topic/libraries/data-binding) - support library that allows binding of UI components in  layouts to data sources,binds search results of forecast data to UI
- [Retrofit](https://square.github.io/retrofit/) - type safe http client 
and supports coroutines out of the box.  

- [okhttp-logging-interceptor](https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/README.md) - logs HTTP request and response data.
- [Material Design](https://material.io/develop/android/docs/getting-started/) - build awesome beautiful UIs.🔥🔥
- [Firebase](https://firebase.google.com/) - Used for authenticating the user .

-[Timber](https://github.com/JakeWharton/timber)- Used for logging.   
 
 
#### Permissions

**Internet Access **


## To Do List

:construction: :construction:
Add a better fix for the work manager in future.
Use Koin/ Dagger for dependency injection

## Contributing


I would love to have your help in making  **Weather App ** better.
The project is still very incomplete, but if there's an issue you'd like to see addressed sooner rather than later, let me know.

Before you contribute though read the contributing guide here: [CONTRIBUTING GUIDE]( https://github.com/nerdloco/WeatherApp/blob/master/contributing.md )

For any concerns, please open an [issue]https://github.com/nerdloco/WeatherApp/issues), or JUST, [fork the project and send a pull request](https://github.com/nerdloco/WeatherApp/pulls).

## License

### Assets

**Credits:**
- Logo is used from [FreePik](https://www.freepik.com/)

[![Open Source Love](https://badges.frapsoft.com/os/v2/open-source-200x33.png?v=103)](https://github.com/ellerbrock/open-source-badge/)
## Versions

Version 1.0  DATE 28/05/2020


## Contact Information
* Gmail : [gibsonruitiari@gmail.com]


<hr>

:bowtie: :v: :tropical_drink:
