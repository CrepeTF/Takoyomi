package eu.kanade.tachiyomi

import android.app.Application
import androidx.core.content.ContextCompat
import eu.kanade.tachiyomi.data.cache.ChapterCache
import eu.kanade.tachiyomi.data.cache.CoverCache
import eu.kanade.tachiyomi.data.database.DatabaseHelper
import eu.kanade.tachiyomi.data.download.DownloadManager
import eu.kanade.tachiyomi.data.library.CustomMangaManager
import eu.kanade.tachiyomi.data.preference.PreferencesHelper
import eu.kanade.tachiyomi.data.saver.ImageSaver
import eu.kanade.tachiyomi.data.track.TrackManager
import eu.kanade.tachiyomi.data.track.job.DelayedTrackingStore
import eu.kanade.tachiyomi.extension.ExtensionManager
import eu.kanade.tachiyomi.network.NetworkHelper
import eu.kanade.tachiyomi.source.SourceManager
import exh.eh.EHentaiUpdateHelper
import kotlinx.serialization.json.Json
import uy.kohesive.injekt.api.InjektModule
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingleton
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

class AppModule(val app: Application) : InjektModule {

    override fun InjektRegistrar.registerInjectables() {
        addSingleton(app)

        addSingletonFactory { Json { ignoreUnknownKeys = true } }

        addSingletonFactory { PreferencesHelper(app) }

        addSingletonFactory { DatabaseHelper(app) }

        addSingletonFactory { ChapterCache(app) }

        addSingletonFactory { CoverCache(app) }

        addSingletonFactory { NetworkHelper(app) }

        addSingletonFactory { SourceManager(app).also { get<ExtensionManager>().init(it) } }

        addSingletonFactory { ExtensionManager(app) }

        addSingletonFactory { DownloadManager(app) }

        addSingletonFactory { TrackManager(app) }

        addSingletonFactory { DelayedTrackingStore(app) }

        addSingletonFactory { ImageSaver(app) }

        // SY -->
        addSingletonFactory { CustomMangaManager(app) }

        addSingletonFactory { EHentaiUpdateHelper(app) }
        // SY <--

        // Asynchronously init expensive components for a faster cold start
        ContextCompat.getMainExecutor(app).execute {
            get<PreferencesHelper>()

            get<NetworkHelper>()

            get<SourceManager>()

            get<DatabaseHelper>()

            get<DownloadManager>()

            // SY -->
            get<CustomMangaManager>()
            // SY <--
        }
    }
}
