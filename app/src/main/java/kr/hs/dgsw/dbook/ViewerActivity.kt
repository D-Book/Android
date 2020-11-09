package kr.hs.dgsw.dbook

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.namo.epub.*
import com.namo.epub.model.EpubAnnotation
import com.namo.epub.model.PackageDocument
import com.namo.epub.model.SearchResult
import com.namo.epub.model.State
import com.namo.epub.model.attr.RenditionLayout
import kotlinx.android.synthetic.main.activity_viewer.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.hs.dgsw.dbook.local.DBookDatabase
import kr.hs.dgsw.dbook.local.dao.BookDao
import kr.hs.dgsw.dbook.namo.DefaultEpubDRM
import kr.hs.dgsw.dbook.namo.DefaultViewerSettings

const val EXTRA_BOOK_ID = "extra_book_id"

class ViewerActivity : AppCompatActivity(), EpubManager.Listener {
    lateinit var mEpubManager: EpubManager
    lateinit var mSettings: EpubSettings
    lateinit var epubProvider: EpubProvider
    lateinit var bookDao: BookDao
    var bookId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)
        bookDao = Room.databaseBuilder(this, DBookDatabase::class.java, "DBookDatabase")
                .build()
                .bookDao()
        bookId = intent.getStringExtra(EXTRA_BOOK_ID)
        if (bookId == null) {
            finish()
            return;
        }
        setEpubManager()
    }

    override fun onDestroy() {
        super.onDestroy()
        mEpubManager.destroy()
        epubProvider.destroy()
    }

    fun setEpubManager() {
        CoroutineScope(Dispatchers.IO).launch {
            bookDao.getBookById(bookId!!)?.let {
                launch(Dispatchers.Main) {
                    mSettings = DefaultViewerSettings()
                    val epubDRM = DefaultEpubDRM(it.book_file)
                    epubProvider = EpubFactory.createProvider(this@ViewerActivity, epubDRM, mSettings)
                    epubProvider.run()
                    mEpubManager = EpubFactory.createManager(this@ViewerActivity, bookId, it.book_file, epubProvider, mSettings)
                }
            }
        }
    }

    override fun onParseCompleted() {
        mEpubManager.open(layout_viewer, layout_video)
    }

    override fun onMessage(p0: EpubManager?, message: Int) {
    }

    override fun onException(p0: EpubManager?, p1: EpubException?) {
    }

    override fun onOpened(p0: EpubManager?) {
        mEpubManager.changePage()
    }

    override fun onPageChangeRequested(p0: EpubManager?, p1: PackageDocument.Spine.Itemref?, p2: State?): Boolean = false

    override fun onPageChanged(p0: EpubManager?, p1: Int, p2: Int, p3: Int, p4: RenditionLayout?) {
    }

    override fun onScaleChanged(p0: EpubManager?, p1: Float, p2: Float) {
    }

    override fun onPageCalculationStart(p0: EpubManager?) {
    }

    override fun onPageCalculationProgress(p0: EpubManager?, p1: Int, p2: Int) {
    }

    override fun onPageCalculationEnd(p0: EpubManager?, p1: Int, p2: Boolean) {
    }

    override fun onSearchStart(p0: EpubManager?) {
    }

    override fun onSearchProgress(p0: EpubManager?, p1: Int, p2: Int, p3: MutableList<SearchResult>?) {
    }

    override fun onSearchStopped(p0: EpubManager?, p1: Int, p2: Int, p3: MutableList<SearchResult>?) {
    }

    override fun onSearchEnd(p0: EpubManager?) {
    }

    override fun onFling(p0: EpubManager?, p1: MotionEvent?, p2: MotionEvent?, p3: Float, p4: Float): Boolean = false

    override fun onClick(p0: EpubManager?, p1: Int, p2: Int, p3: Int, p4: Int, p5: EpubSettings.AreaType?): Boolean {
        return when (p5) {
            EpubSettings.AreaType.CENTER -> {
                expand_layout_top.toggle(true)
                expand_layout_bottom.toggle(true)
                true
            }
            else -> false
        }
    }

    override fun onLinkClick(p0: EpubManager?, p1: String?, p2: Boolean) {
    }

    override fun onNoterefClick(p0: EpubManager?, p1: String?, p2: String?, p3: Rect?) {
    }

    override fun onAnnotationSelected(p0: EpubManager?, p1: EpubAnnotation?, p2: Rect?, p3: Rect?, p4: EpubSettings.ViewDirection?) {
    }

    override fun onSelectionCleared(p0: EpubManager?) {
    }
}