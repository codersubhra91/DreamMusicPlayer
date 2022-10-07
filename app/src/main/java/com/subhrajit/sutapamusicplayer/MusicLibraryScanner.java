package com.subhrajit.sutapamusicplayer;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class MusicLibraryScanner {
    public static ArrayList<SongModel> fetchAllSongs(Context context){

		// We use this thing to iterate through the results
		// of a SQLite database query.
		Cursor cursor;
        // First, before even touching the songs, we'll save all the
		// music genres (like "Rock", "Jazz" and such).
		// That's because Android doesn't allow getting a song genre
		// from the song file itself.
		//
		// To get the genres, we make queries to the system's SQLite
		// database. It involves genre IDs, music IDs and such.
        String songID=MediaStore.Audio.Media._ID;
        String GENRE_ID      = MediaStore.Audio.Genres._ID;
		String GENRE_NAME    = MediaStore.Audio.Genres.NAME;
        // Creating the map  "Genre IDs" -> "Genre Names"
		HashMap<String,String>genreIdToGenreNameMap = new HashMap<String, String>();
		// This is what we'll ask of the genres
		String[] genreColumns = {
				GENRE_ID,
				GENRE_NAME
		};
        Uri genreURI=MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
        cursor=context.getContentResolver().query(genreURI,genreColumns,null,null,null);

        // Iterating through the results and filling the map.
        if (cursor != null){
            while (cursor.moveToNext()){
                String id= cursor.getString(0);
                String name=cursor.getString(1);
                if (name==null && id==null){
                    name="<unknown>";
                    id="100000000000000001";
                }
                //Log.e("GenreID:"+ id,"GenreName: "+name);
                genreIdToGenreNameMap.put(id, name);
            }
            cursor.close();
        }
        // Map from Songs IDs to Genre IDs
        HashMap<String,String>songIdToGenreIdMap = new HashMap<String, String>();
        // For each genre, we'll query the databases to get
    	// all songs's IDs that have it as a genre.
    	for (String genreID : genreIdToGenreNameMap.keySet()) {
        	Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external",
        			                                                Long.parseLong(genreID));
        	cursor = context.getContentResolver().query(uri, new String[] { songID }, null, null, null);
        	// Iterating through the results, populating the map
        	for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

        		@SuppressLint("Range") long currentSongID = cursor.getLong(cursor.getColumnIndex(songID));
                //Log.e("SongID: "+currentSongID,"GenreID: "+genreID);
        		songIdToGenreIdMap.put(Long.toString(currentSongID), genreID);
        	}
        	cursor.close();
        }

        ArrayList<SongModel>tempAudioList=new ArrayList<>();
        Uri uri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[]projection={
                songID,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.SIZE
        };
        cursor=context.getContentResolver().query(uri,projection,MediaStore.Audio.Media.IS_MUSIC,null,null);
        if (cursor !=null){
            while (cursor.moveToNext()){
                String _ID=cursor.getString(0);
                String album=cursor.getString(1);
                String title=cursor.getString(2);
                String duration=cursor.getString(3);
                String path=cursor.getString(4);
                String artist=cursor.getString(5);
                String size=cursor.getString(6);
                //Log.e("PATH: "+path, "Album: "+album);
                // Using the previously created genre maps
				// to fill the current song genre.
				String currentGenreID   = songIdToGenreIdMap.get(_ID);
				String currentGenreName = genreIdToGenreNameMap.get(currentGenreID);

                if (currentGenreName==null || currentGenreName.equals("")){
                    currentGenreName="Unknown";
                }
                if(path==null){
                    path="NA";
                    title="NA";
                    artist="NA";
                    album="NA";
                    duration="NA";
                    currentGenreName="NA";
                    size="NA";
                }
                SongModel song = new SongModel (path,title,artist,size,currentGenreName,album,duration);
                tempAudioList.add(song);
            }
            cursor.close();
        }
        return tempAudioList;
    }
}
