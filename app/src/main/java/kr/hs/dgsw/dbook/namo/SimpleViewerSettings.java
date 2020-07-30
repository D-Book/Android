package kr.hs.dgsw.dbook.namo;

import com.namo.epub.EpubSettings;

public class SimpleViewerSettings extends EpubSettings {

	private static final int DEFAULT_THUMBNAIL_SIZE = 1000;

	public SimpleViewerSettings() {

		scaleMode = ScaleMode.ONLY_PRE_PAGINATED;

		thumbnailMode = ThumbnailMode.ON;
		thumbnailMaxWidth = DEFAULT_THUMBNAIL_SIZE;
		thumbnailMaxHeight = DEFAULT_THUMBNAIL_SIZE;

		loadingMode = LoadingMode.SHOW_THUMBNAIL;

		customCssPath = "custom.css";
	}
}
