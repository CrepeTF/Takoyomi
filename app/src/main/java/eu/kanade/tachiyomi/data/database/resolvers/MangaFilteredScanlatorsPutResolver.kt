package eu.kanade.tachiyomi.data.database.resolvers

import androidx.core.content.contentValuesOf
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver
import com.pushtorefresh.storio.sqlite.operations.put.PutResult
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery
import eu.kanade.tachiyomi.data.database.inTransactionReturn
import eu.kanade.tachiyomi.data.database.models.Manga
import eu.kanade.tachiyomi.data.database.tables.MangaTable

// [EXH]
class MangaFilteredScanlatorsPutResolver : PutResolver<Manga>() {

    override fun performPut(db: StorIOSQLite, manga: Manga) = db.inTransactionReturn {
        val updateQuery = mapToUpdateQuery(manga)
        val contentValues = mapToContentValues(manga)

        val numberOfRowsUpdated = db.lowLevel().update(updateQuery, contentValues)
        PutResult.newUpdateResult(numberOfRowsUpdated, updateQuery.table())
    }

    fun mapToUpdateQuery(manga: Manga) = UpdateQuery.builder()
        .table(MangaTable.TABLE)
        .where("${MangaTable.COL_ID} = ?")
        .whereArgs(manga.id)
        .build()

    fun mapToContentValues(manga: Manga) = contentValuesOf(
        MangaTable.COL_FILTERED_SCANLATORS to manga.filtered_scanlators,
    )
}
