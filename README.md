# Popular-Movies-App
An Android app that allows users to discover the most popular movies playing along with movies categorized as Top-rated or Now Playing.
This app fetches its data from [The Movie Db API](https://www.themoviedb.org/)
## Overview
The app named as - KnowMovies display a list of movies which can be sorted based on options like: Now Playing, Popular or Top Rated.
The following are the *features of this app* :

* Fetches data from the Internet with theMovieDB API.
* Uses adapters and custom list layouts (used RecyclerView) to populate the list.
* The app has a feature of *unlimited scroll* that keeps displaying movies as the user scrolls (instead of the usual 20-movies-limit per API call)
* Allows users to view and play trailers
* Allows users to read reviews of a selected movie.
* The app also allows users to mark a movie as a favorite which is stored in the database (for Offline viewing)

## Libraries Used
The following are the list of libraries used in the app :
* **RecyclerView** and **CardView** for UI
* **GSON** for parsing JSON
* **Picasso** for Image Caching
* **Palette API** for generating a palette
* Data Persistence is achieved through **Room** (Used Architecture Components)
* **ViewModel** and **LiveData** to handle the Configuration changes and to observe data changes.

## Screenshots

![image](https://user-images.githubusercontent.com/39236351/50589008-b37c2e80-0eaa-11e9-82f8-c2d1b2c5b0bf.png)
![image](https://user-images.githubusercontent.com/39236351/50590306-669b5680-0eb0-11e9-84a0-f173b20b3586.png)
![image](https://user-images.githubusercontent.com/39236351/50590471-2ee0de80-0eb1-11e9-8952-260e3b88ee03.png)
![image](https://user-images.githubusercontent.com/39236351/50590425-f0e3ba80-0eb0-11e9-9a3f-8d80440d5ef3.png)
