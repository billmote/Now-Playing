package com.androidfu.nowplaying.app.api;

import com.androidfu.nowplaying.app.BuildConfig;
import com.androidfu.nowplaying.app.model.movies.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import hugo.weaving.DebugLog;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public class RestClient {

    private ApiService apiService;

    public RestClient(String endpoint, boolean enableLogging) {

        Gson gsonBuilder = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                /*
                    Rotten Tomatoes returns an int when there's a valid value for runtime, but they
                    return an empty string when the value is null.  Because they return different
                    JSON primitive types we have to create a custom TypeAdapter for our Movie.class.
                 */
                .registerTypeAdapter(Movie.class, new JsonDeserializer<Movie>() {
                    @Override
                    public Movie deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
                        JsonObject movieObject = arg0.getAsJsonObject();
                        Gson gson = new Gson();
                        Movie movie = gson.fromJson(arg0, Movie.class);
                        int runtime;
                        try {
                            runtime = movieObject.get("runtime").getAsInt();
                        } catch (Exception e) {
                            runtime = 0;
                        }
                        movie.setMovieRuntime(runtime);
                        return movie;
                    }
                })
                .setDateFormat("yyyy-MM-dd")
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(BuildConfig.DEBUG && enableLogging ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setEndpoint(endpoint)
                .setConverter(new GsonConverter(gsonBuilder, "UTF-8"))
                .setRequestInterceptor(new SessionRequestInterceptor())
                .build();

        apiService = restAdapter.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }

}
