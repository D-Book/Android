package kr.hs.dgsw.dbook.namo;

import com.namo.epub.EpubSettings;

//import com.namo.pubtree.reader.viewer.R;
//import com.namo.viewer.BaseApplication;
//import com.namo.viewer.BaseApplication.ResourcesWrapper;
//import com.namo.viewer.model.Book;
//import com.namo.viewer.network.DataConst;
//import com.namo.viewer.storage.SPController;

public class DefaultViewerSettings extends EpubSettings {
//    public static final int THEME_ID_01 = 0;
//    public static final int THEME_ID_02 = 1;
//    public static final int THEME_ID_03 = 2;
//    public static final int THEME_ID_04 = 3;
//    public static final int THEME_ID_05 = 4;
//    public static final int THEME_ID_06 = 5;
//    public static final int THEME_ID_07 = 6;
//    public static final int THEME_ID_08 = 7;
//
//    /**
//     * #VW-LIB-100-01-011 Add by Chris (2018-05-09)
//     * Set Build Product Type
//     * TODO : Build Configuration
//     */
//    private static final BuildProductType PRODUCT_TYPE = BuildProductType.DEFAULT;
//    //	private static final BuildProductType PRODUCT_TYPE = BuildProductType.BLUENTREE;
//
//    /**
//     * #VW-LIB-101-01-022 Add by Chris (2018-05-28)
//     * Set DRM Type
//     * TODO : Build Configuration
//     */
//    private static final DrmType DRM_TYPE = DrmType.DEFAULT;
//    //	private static final DrmType DRM_TYPE = DrmType.NAMO_DRM;
//
//    /**
//     * #VW-LIB-100-01-012 Add by Chris (2018-05-09)
//     * Default Theme Setting
//     */
//    public static final int DEFAULT_THEME = PRODUCT_TYPE == BuildProductType.BLUENTREE ? THEME_ID_08 : THEME_ID_01;
//
//    private static final int DEFAULT_THUMBNAIL_SIZE = 1000;
//
//    // public boolean isPrePaginated;
//    private Context c;
//    private ResourcesWrapper r;
//    private SPController sp;
//    private BaseApplication app;
//
//    /**
//     * call from MainActivity
//     *
//     * @param app
//     */
//    public DefaultViewerSettings(BaseApplication app) {
//        this.app = app;
//        // this.isPrePaginated = isPrePaginated;
//        r = app.getResourcesWrapper();
//        c = app.getApplicationContext();
//
//        // #VW-LIB-100-01-011 Add by Chris (2018-05-09)
//        productType = PRODUCT_TYPE;
//
//        // #VW-LIB-101-01-022 Add by Chris (2018-05-28)
//        drmType = DRM_TYPE;
//
//        useCache = true;
//        scaleMode = ScaleMode.ONLY_PRE_PAGINATED;
//
//        touchMoveAreaPer = 10;
//
//        // 검색
//        searchResultLimitCount = 100;        // 검색어 결과 수 제한
//        searchResultNextTextLength = 50;    // 검색어 앞쪽 텍스트 길이
//        searchResultPrevTextLength = 50;    // 검색어 뒤쪽 텍스트 길이
//
//        thumbnailMode = ThumbnailMode.ON;
//        //		thumbnailLoadingDelay = 100;
//        thumbnailMaxWidth = DEFAULT_THUMBNAIL_SIZE;
//        thumbnailMaxHeight = DEFAULT_THUMBNAIL_SIZE;
//
//        sp = app.getSPController();
//
//        loadingMode = LoadingMode.SHOW_THUMBNAIL;
//        //		loadingDelay = 300;		// Loading delay time (Pre-Pagenated, Thumbnail)
//        //		pageLoadingDelay = 300;	// Loading delay time (Reflowable)
//        useMediaOverlay = true;
//
//        setPadding();
//
//        customCssPath = "custom.css";
//        initialStyle.fontFamily = "none";
//        initialStyle.fontSize = 1.0f;
//        initialStyle.lineHeight = 1.0f;
//        initialTheme = getTheme(sp.getEpubViewerTheme());
//
//        viewOption = getViewOption(sp.getViewOption());
//    }
//
//    public ViewOption getViewOption(int viewOption) {
//        switch (viewOption) {
//            case DataConst.TAG_VIEW_AUTO:
//                return ViewOption.AUTO;
//            case DataConst.TAG_VIEW_ONCE:
//                return ViewOption.ONCE;
//            case DataConst.TAG_VIEW_TWICE:
//                return ViewOption.TWICE;
//        }
//        return ViewOption.AUTO;
//    }
//
//    /**
//     * call from ViewerActivity
//     *
//     * @param app
//     * @param content
//     */
//    public DefaultViewerSettings(BaseApplication app, Book content) {
//        this.app = app;
//        // this.isPrePaginated = isPrePaginated;
//        r = app.getResourcesWrapper();
//        c = app.getApplicationContext();
//
//        // #VW-LIB-100-01-011 Add by Chris (2018-05-09)
//        productType = PRODUCT_TYPE;
//
//        // #VW-LIB-101-01-022 Add by Chris (2018-05-28)
//        drmType = DRM_TYPE;
//
//        useCache = true;
//        scaleMode = ScaleMode.ONLY_PRE_PAGINATED;
//
//        touchMoveAreaPer = 5;
//
//        searchResultLimitCount = 100;
//        searchResultNextTextLength = 50;
//        searchResultPrevTextLength = 50;
//
//        thumbnailMode = ThumbnailMode.ON;
//        //		thumbnailLoadingDelay = 100;
//        thumbnailMaxWidth = DEFAULT_THUMBNAIL_SIZE;
//        thumbnailMaxHeight = DEFAULT_THUMBNAIL_SIZE;
//
//        sp = app.getSPController();
//
//        loadingMode = LoadingMode.SHOW_THUMBNAIL;
//        useMediaOverlay = true;
//
//        setPadding();
//
//        customCssPath = "custom.css";
//        int index = content.getStyleFontFamily();
//
//        if (index == 0 && !Locale.getDefault().getLanguage().equalsIgnoreCase("ja"))
//            initialStyle.fontFamily = "none";
//        else
//            initialStyle.fontFamily = c.getResources().getStringArray(R.array.font_type_labels)[index];
//
//        initialStyle.fontSize = content.getStyleFontSize();
//        initialStyle.lineHeight = content.getStyleLineHeight();
//        initialTheme = getTheme(sp.getEpubViewerTheme());
//
//		viewOption = getViewOption(sp.getViewOption());
//    }
//
//    private void setPadding() {
//        padding = new Padding() {
//            @Override
//            public int getTop() {
//                return r.px(R.dimen.v_settings_paddingTop);
//            }
//
//            @Override
//            public int getOuter() {
//                return r.px(R.dimen.v_settings_paddingOuter);
//            }
//
//            @Override
//            public int getInner() {
//                return r.px(R.dimen.v_settings_paddingInner);
//            }
//
//            @Override
//            public int getBottom() {
//                return r.px(R.dimen.v_settings_paddingBottom);
//            }
//        };
//    }
//
//    public abstract class EpubViewerTheme implements Theme {
//        public abstract int getTextColor();
//
//        @Override
//        public Drawable getDividerDrawable(FrameType frameType) {
//            return null;
//        }
//
//        @Override
//        public Drawable getStartHandleDrawable(ViewDirection viewDirection) {
//            return r.d(c, viewDirection == ViewDirection.VERTICAL ? R.drawable.icon_selection01_vertical : R.drawable.icon_selection01);
//        }
//
//        @Override
//        public Drawable getEndHandleDrawable(ViewDirection viewDirection) {
//            return r.d(c, viewDirection == ViewDirection.VERTICAL ? R.drawable.icon_selection02_vertical : R.drawable.icon_selection02);
//        }
//
//        @Override
//        public int getSelectionColor() {
//            return c.getResources().getIntArray(R.array.annotation_colors)[ConstU.DEFAULT_ANNOTAION_COLOR];
//        }
//
//        @Override
//        public Drawable getNoteDrawable() {
//            return null;
//        }
//
//        @Override
//        public int getCfiColor() {
//            return c.getResources().getIntArray(R.array.annotation_colors)[ConstU.DEFAULT_ANNOTAION_COLOR];
//        }
//    }
//
//    public Theme getTheme(int id) {
//        switch (id) {
//            case THEME_ID_01:
//                return THEME01;
//            case THEME_ID_02:
//                return THEME02;
//            case THEME_ID_03:
//                return THEME03;
//            case THEME_ID_04:
//                return THEME04;
//            case THEME_ID_05:
//                return THEME05;
//            case THEME_ID_06:
//                return THEME06;
//            case THEME_ID_07:
//                return THEME07;
//            case THEME_ID_08:
//                return THEME08;
//            default:
//                return THEME01;
//        }
//    }
//
//    private final EpubViewerTheme THEME01 = new EpubViewerTheme() {
//
//        @Override
//        public int getTextColor() {
//            return r.c(c, R.color.viewer_font_color1);
//        }
//
//        @Override
//        public int getId() {
//            return THEME_ID_01;
//        }
//
//        @Override
//        public String getTextColorString() {
//            return "#3E3E3E";
//        }
//
//        @Override
//        public int getBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color1);
//        }
//
//        @Override
//        public int getPrePaginatedBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color1);
//        }
//
//        @Override
//        public int getBackgroundOutsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color1);
//        }
//
//        @Override
//        public Drawable getFrameDrawable(FrameType frameType) {
//            return null;
//        }
//    };
//
//    private final EpubViewerTheme THEME02 = new EpubViewerTheme() {
//        @Override
//        public int getId() {
//            return THEME_ID_02;
//        }
//
//        @Override
//        public int getTextColor() {
//            return r.c(c, R.color.viewer_font_color2);
//        }
//
//        @Override
//        public String getTextColorString() {
//            return "#4E3B28";
//        }
//
//        @Override
//        public int getBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color2);
//        }
//
//        @Override
//        public int getPrePaginatedBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color2);
//        }
//
//        @Override
//        public int getBackgroundOutsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color2);
//        }
//
//        @Override
//        public Drawable getFrameDrawable(FrameType frameType) {
//            return null;
//        }
//    };
//
//    private final EpubViewerTheme THEME03 = new EpubViewerTheme() {
//        @Override
//        public int getId() {
//            return THEME_ID_03;
//        }
//
//        @Override
//        public int getTextColor() {
//            return r.c(c, R.color.viewer_font_color3);
//        }
//
//        @Override
//        public String getTextColorString() {
//            return "#5B4A3A";
//        }
//
//        @Override
//        public int getBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color3);
//        }
//
//        @Override
//        public int getPrePaginatedBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color3);
//        }
//
//        @Override
//        public int getBackgroundOutsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color3);
//        }
//
//        @Override
//        public Drawable getFrameDrawable(FrameType frameType) {
//            return null;
//        }
//    };
//
//    private final EpubViewerTheme THEME04 = new EpubViewerTheme() {
//        @Override
//        public int getId() {
//            return THEME_ID_04;
//        }
//
//        @Override
//        public int getTextColor() {
//            return r.c(c, R.color.viewer_font_color4);
//        }
//
//        @Override
//        public String getTextColorString() {
//            return "#2B2B2A";
//        }
//
//        @Override
//        public int getBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color4);
//        }
//
//        @Override
//        public int getPrePaginatedBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color4);
//        }
//
//        @Override
//        public int getBackgroundOutsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color4);
//        }
//
//        @Override
//        public Drawable getFrameDrawable(FrameType frameType) {
//            return null;
//        }
//    };
//
//    private final EpubViewerTheme THEME05 = new EpubViewerTheme() {
//        @Override
//        public int getId() {
//            return THEME_ID_05;
//        }
//
//        @Override
//        public int getTextColor() {
//            return r.c(c, R.color.viewer_font_color5);
//        }
//
//        @Override
//        public String getTextColorString() {
//            return "#3E3E3E";
//        }
//
//        @Override
//        public int getBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color5);
//        }
//
//        @Override
//        public int getPrePaginatedBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color5);
//        }
//
//        @Override
//        public int getBackgroundOutsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color5);
//        }
//
//        @Override
//        public Drawable getFrameDrawable(FrameType frameType) {
//            return null;
//        }
//    };
//
//    private final EpubViewerTheme THEME06 = new EpubViewerTheme() {
//        @Override
//        public int getId() {
//            return THEME_ID_06;
//        }
//
//        @Override
//        public int getTextColor() {
//            return r.c(c, R.color.viewer_font_color6);
//        }
//
//        @Override
//        public String getTextColorString() {
//            return "#3E3E3E";
//        }
//
//        @Override
//        public int getBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color6);
//        }
//
//        @Override
//        public int getPrePaginatedBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color6);
//        }
//
//        @Override
//        public int getBackgroundOutsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color6);
//        }
//
//        @Override
//        public Drawable getFrameDrawable(FrameType frameType) {
//            return null;
//        }
//    };
//
//    private final EpubViewerTheme THEME07 = new EpubViewerTheme() {
//        @Override
//        public int getId() {
//            return THEME_ID_07;
//        }
//
//        @Override
//        public int getTextColor() {
//            return r.c(c, R.color.viewer_font_color7);
//        }
//
//        @Override
//        public String getTextColorString() {
//            return "#C7D9D6";
//        }
//
//        @Override
//        public int getBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color7);
//        }
//
//        @Override
//        public int getPrePaginatedBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color7);
//        }
//
//        @Override
//        public int getBackgroundOutsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color7);
//        }
//
//        @Override
//        public Drawable getFrameDrawable(FrameType frameType) {
//            return null;
//        }
//    };
//
//    private final EpubViewerTheme THEME08 = new EpubViewerTheme() {
//        @Override
//        public int getId() {
//            return THEME_ID_08;
//        }
//
//        @Override
//        public int getTextColor() {
//            return r.c(c, R.color.viewer_font_color8);
//        }
//
//        @Override
//        public String getTextColorString() {
//            return "#CECECB";
//        }
//
//        @Override
//        public int getBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color8);
//        }
//
//        @Override
//        public int getPrePaginatedBackgroundInsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color8);
//        }
//
//        @Override
//        public int getBackgroundOutsideFrameColor() {
//            return r.c(c, R.color.viewer_background_color8);
//        }
//
//        @Override
//        public Drawable getFrameDrawable(FrameType frameType) {
//            return null;
//        }
//    };
//
//    public String getCustomCSS(int index) {
//        if (index <= 0)
//            return null;
//
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("@font-face {\nfont-family:'");
//        sb.append(r.sa(R.array.font_type_labels)[index]);
//        sb.append("';\nsrc:url('file://");
//        sb.append(r.sa(R.array.font_type_values)[index]);
//        sb.append("');\n}\n");
//
//        return sb.toString();
//    }
}
