package com.example.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {

    @Query("SELECT COUNT(*) FROM notes")
    suspend fun countNotes(): Int

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getById(id: Long): NoteEntity?

    @Query("SELECT * FROM notes ORDER BY createdAt DESC, id DESC")
    fun pagingSource(): PagingSource<Int, NoteEntity>

    @Query(
        """
        SELECT * FROM notes
        ORDER BY createdAt DESC
        LIMIT :limit
        """
    )
    suspend fun firstPage(limit: Int): List<NoteEntity>

    @Query(
        """
        SELECT * FROM notes
        WHERE createdAt < :lastCreatedAt
        ORDER BY createdAt DESC
        LIMIT :limit
        """
    )
    suspend fun nextPage(
        lastCreatedAt: Long,
        limit: Int,
    ): List<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<NoteEntity>)

    @Update
    suspend fun update(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()
}
