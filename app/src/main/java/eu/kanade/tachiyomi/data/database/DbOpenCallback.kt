package eu.kanade.tachiyomi.data.database

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import eu.kanade.tachiyomi.data.database.tables.CategoryTable
import eu.kanade.tachiyomi.data.database.tables.ChapterTable
import eu.kanade.tachiyomi.data.database.tables.HistoryTable
import eu.kanade.tachiyomi.data.database.tables.MangaCategoryTable
import eu.kanade.tachiyomi.data.database.tables.MangaTable
import eu.kanade.tachiyomi.data.database.tables.TrackTable
import exh.favorites.sql.tables.FavoriteEntryTable
import exh.merged.sql.tables.MergedTable
import exh.metadata.sql.tables.SearchMetadataTable
import exh.metadata.sql.tables.SearchTagTable
import exh.metadata.sql.tables.SearchTitleTable
import exh.savedsearches.tables.FeedSavedSearchTable
import exh.savedsearches.tables.SavedSearchTable

class DbOpenCallback : SupportSQLiteOpenHelper.Callback(DATABASE_VERSION) {

    companion object {
        /**
         * Name of the database file.
         */
        const val DATABASE_NAME = "tachiyomi.db"

        /**
         * Version of the database.
         */
        const val DATABASE_VERSION = /* SY --> */ 13 /* SY <-- */
    }

    override fun onCreate(db: SupportSQLiteDatabase) = with(db) {
        execSQL(MangaTable.createTableQuery)
        execSQL(ChapterTable.createTableQuery)
        execSQL(TrackTable.createTableQuery)
        execSQL(CategoryTable.createTableQuery)
        execSQL(MangaCategoryTable.createTableQuery)
        execSQL(HistoryTable.createTableQuery)
        // SY -->
        execSQL(SearchMetadataTable.createTableQuery)
        execSQL(SearchTagTable.createTableQuery)
        execSQL(SearchTitleTable.createTableQuery)
        execSQL(MergedTable.createTableQuery)
        execSQL(FavoriteEntryTable.createTableQuery)
        execSQL(SavedSearchTable.createTableQuery)
        execSQL(FeedSavedSearchTable.createTableQuery)
        // SY <--

        // DB indexes
        execSQL(MangaTable.createUrlIndexQuery)
        execSQL(MangaTable.createLibraryIndexQuery)
        execSQL(ChapterTable.createMangaIdIndexQuery)
        execSQL(ChapterTable.createUnreadChaptersIndexQuery)
        execSQL(HistoryTable.createChapterIdIndexQuery)
        // SY -->
        execSQL(SearchMetadataTable.createUploaderIndexQuery)
        execSQL(SearchMetadataTable.createIndexedExtraIndexQuery)
        execSQL(SearchTagTable.createMangaIdIndexQuery)
        execSQL(SearchTagTable.createNamespaceNameIndexQuery)
        execSQL(SearchTitleTable.createMangaIdIndexQuery)
        execSQL(SearchTitleTable.createTitleIndexQuery)
        execSQL(MergedTable.createIndexQuery)
        execSQL(FeedSavedSearchTable.createSavedSearchIdIndexQuery)
        // SY <--
    }

    override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL(MangaTable.addCoverLastModified)
        }
        if (oldVersion < 3) {
            db.execSQL(MangaTable.addDateAdded)
            db.execSQL(MangaTable.backfillDateAdded)
        }
        if (oldVersion < 4) {
            db.execSQL(MergedTable.dropTableQuery)
            db.execSQL(MergedTable.createTableQuery)
            db.execSQL(MergedTable.createIndexQuery)
        }
        /*if (oldVersion < 5) {
            db.execSQL(SimilarTable.createTableQuery)
            db.execSQL(SimilarTable.createMangaIdIndexQuery)
        }*/
        if (oldVersion < 6) {
            db.execSQL(MangaTable.addFilteredScanlators)
        }
        if (oldVersion < 7) {
            db.execSQL("DROP TABLE IF EXISTS manga_related")
        }
        if (oldVersion < 8) {
            db.execSQL(MangaTable.addNextUpdateCol)
        }
        if (oldVersion < 9) {
            db.execSQL(TrackTable.renameTableToTemp)
            db.execSQL(TrackTable.createTableQuery)
            db.execSQL(TrackTable.insertFromTempTable)
            db.execSQL(TrackTable.dropTempTable)
        }
        if (oldVersion < 10) {
            db.execSQL(ChapterTable.fixDateUploadIfNeeded)
        }
        if (oldVersion < 11) {
            db.execSQL(FavoriteEntryTable.createTableQuery)
        }
        if (oldVersion < 12) {
            db.execSQL(FavoriteEntryTable.fixTableQuery)
        }
        if (oldVersion < 13) {
            db.execSQL(SavedSearchTable.createTableQuery)
            db.execSQL(FeedSavedSearchTable.createTableQuery)
            db.execSQL(FeedSavedSearchTable.createSavedSearchIdIndexQuery)
        }
    }

    override fun onConfigure(db: SupportSQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }
}
