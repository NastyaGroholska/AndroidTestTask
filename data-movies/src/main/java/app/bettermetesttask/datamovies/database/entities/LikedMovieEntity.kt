package app.bettermetesttask.datamovies.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "LikedMovieEntry",
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("movie_id"),
            onDelete = ForeignKey.CASCADE
        )])
data class LikedMovieEntity(
    @PrimaryKey @ColumnInfo(name = "movie_id")
    val movieId: Int
)