package com.example.pokedex.network

sealed class Resource <T> (val data : T ? = null , val message : String ? = null) {

    class Success <T> (data: T ) : Resource <T>(data = data)
    class Loading <T> () : Resource <T> ()
    class Error <T> (message : String , data : T? = null) : Resource <T> (message = message)

}