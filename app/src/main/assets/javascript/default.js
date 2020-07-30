var w = window;
w["namo"] = {};

w["navigator"]["epubReadingSystem"] = {
	"SUPPORTED_FEATURE_REFLOWABLE" : [ "keyboard-events", "keyboard-events",
			"touch-events", "spine-scripting" ],
	"SUPPORTED_FEATURE_PRE_PAGINATED" : [ "dom-manipulation", "layout-changes",
			"keyboard-events", "touch-events", "spine-scripting" ],
	"name" : "Namo Epub Library",
	"version" : "0.0.0",
	"layoutStyle" : "paginated",
	"hasFeature" : function(featureName) {
		var features = w["namo"]["isReflowable"] ? w["navigator"]["epubReadingSystem"]["SUPPORTED_FEATURE_REFLOWABLE"]
				: w["navigator"]["epubReadingSystem"]["SUPPORTED_FEATURE_PRE_PAGINATED"];
		return features["indexOf"](featureName) >= 0;
	}
};

/* constants */
w["namo"]["BRIDGE"] = "namo_bridge";
w["namo"]["DIRECTION_LTR"] = 0;
w["namo"]["DIRECTION_RTL"] = 1;
w["namo"]["DIRECTION_VERTICAL"] = 2;
w["namo"]["HIT_TEST_THRESHOLD"] = 5;
w["namo"]["TAG"] = {
	"BLOCK" : [ "div", "section", "article" ],
	"CLICK" : [ "a", "area", "audio", "button", "datalist", "embed", "iframe",
			"input", "keygen", "label", "object", "option", "select",
			"summary", "textarea", "video" ],
    "HASHREF" : [ "a" ],
	"EVENT" : [ w, w["document"], w["HTMLElement"]["prototype"] ],
	"MEDIA" : [ "audio", "video" ],
	"TEXT" : [ "html", "body", "div", "section", "li", "td", "th", "p", "span",
			"h1", "h2", "h3", "h4", "h5", "h6" ],
	"TEXT_INPUT_TYPE" : [ "email", "number", "password", "search", "tel",
			"text", "url" ]
};
w["namo"]["REQUIRED_NAMESPACE"] = [ "http://www.w3.org/1999/xhtml",
		"http://www.w3.org/2000/svg" ];
w["namo"]["CORE_MEDIA_TYPE"] = [ "application/vnd.ms-opentype",
		"application/font-woff", "image/gif", "image/jpeg", "image/png",
		"image/svg+xml", "application/xhtml+xml", "application/x-dtbncx+xml",
		"application/smil+xml", "application/pls+xml", "audio/mpeg",
		"audio/mp4", "text/css", "text/javascript" ];

if (w["SVGElement"])
	w["namo"]["TAG"]["EVENT"]["push"](w["SVGElement"]["prototype"]);

/* variables */
w["namo"]["loaded"] = false;
w["namo"]["sdkInt"] = 0;
w["namo"]["hashCode"] = -1;
w["namo"]["debug"] = false;
w["namo"]["mimeType"] = "application/xhtml+xml";
w["namo"]["useAudioAutoplay"] = false;
w["namo"]["useVideoAutoplay"] = false;
w["namo"]["system"] = {
	"width" : 0,
	"height" : 0
};
w["namo"]["viewport"] = {
	"width" : 0,
	"height" : 0
};
w["namo"]["bindings"] = {};
w["namo"]["element"] = {
	"dummy" : null,
	"media" : [],
	"audio" : {},
	"video" : {},
	"focusedText" : null
};
w["namo"]["range"] = {
	"left" : null,
	"right" : null
};
w["namo"]["isReflowable"] = true;
w["namo"]["isTwoPageMode"] = false; // 웹뷰 하나에 두 페이지를 표시할 것인지
w["namo"]["viewDirection"] = w["namo"]["DIRECTION_LTR"];
w["namo"]["columnDirection"] = w["namo"]["DIRECTION_LTR"];
w["namo"]["columnGap"] = 0;
w["namo"]["columnWidth"] = 0;
w["namo"]["columnHeight"] = 0;
w["namo"]["columnRatio"] = 1;
w["namo"]["totalPage"] = 0;
w["namo"]["currentPage"] = -1;
w["namo"]["initialStyle"] = null;
w["namo"]["initialTheme"] = null;
w["namo"]["initialTextInputs"] = [];
w["namo"]["mediaOverlay"] = [];
w["namo"]["mediaOverlayActiveClass"] = null;
w["namo"]["mediaOverlayPlaybackActiveClass"] = null;
w["namo"]["currentMediaOverlayFragment"] = null;
w["namo"]["currentEmbeddedMOFragment"] = null;
w["namo"]["isLeftSpread"]; // default is undefined
w["namo"]["htmlContentTypeByFileExtension"] = false;

// 20160503 over lollipop version webview issue (ver 50.0.2661.86)
w["namo"]["currentScrollXOffset"] = -1;
w["namo"]["currentScrollYOffset"] = -1;

//EpubWebView와 EpubResearchView의 요청을 구분해서 처리하기 위한 변수
w["namo"]["referer"] = "web";

/* apis */
w["namo"]["api"] = {
	"changePage" : function(page) {
		w["namo"]["callback"]({
			"callbackName" : "onApiCalled",
			"functionName" : "changePage",
			"page" : page
		});
	},
	"changeToPrevPage" : function() {
		w["namo"]["callback"]({
			"calllbackName" : "onApiCalled",
			"functionName" : "changeToPrevPage"
		});
	},
	"changeToNextPage" : function() {
		w["namo"]["callback"]({
			"callbackName" : "onApiCalled",
			"functionName" : "changeToNextPage"
		});
	}
};

/* functions */
w["namo"]["executeFunction"] = function(params) {
	var result = w["namo"][params["functionName"]](params);

	if (result && result != null) {
		if (w["namo"]["sdkInt"] >= 19) {
			if (w["namo"]["debug"])
				w["console"]["log"](w["JSON"]["stringify"](result));

			return result;
		} else {
			w["namo"]["callback"](result);
		}
	}
};

w["namo"]["callback"] = function(result) {
	if (!result)
		return;

	var resultStr = w["JSON"]["stringify"](result);

	if (w["namo"]["debug"])
		w["console"]["log"](resultStr);

	w[w["namo"]["BRIDGE"]]["onCallback"](resultStr);
};
w["namo"]["addEventListener"] = function(type, listener, useCapture, custom) {

    console.log(" :: addEventListener :: ");

	this["namo_addEventListener"](type, listener, useCapture);

	if (!custom)
		w["namo"]["addToNamoEvents"](this, type, listener);
};
w["namo"]["addToNamoEvents"] = function(element, type, listener) {

    console.log(" :: addToNamoEvents :: ");

	if (!element["namo_events"])
		element["namo_events"] = {};
	if (!element["namo_events"][type])
		element["namo_events"][type] = [];

	element["namo_events"][type]["push"](listener);
};

w["namo"]["hasEventListener"] = function(target, event) {

	if (target["hasAttribute"]
	    && target["hasAttribute"]("on" + event))
		return true;

	if (target["namo_events"]
	    && target["namo_events"][event]
	    && target["namo_events"][event]["length"] > 0)
		return true;

	return false;
};

w["namo"]["hasClickEventListener"] = function(target) {
	return w["namo"]["hasEventListener"](target, "click")
			|| (w["namo"]["hasEventListener"](target, "touchdown") && w["namo"]["hasEventListener"] (target, "touchend"))
			|| (w["namo"]["hasEventListener"](target, "mousedown") && w["namo"]["hasEventListener"] (target, "mouseup"));
};
w["namo"]["hasScrollEventListener"] = function(target) {
	return w["namo"]["hasEventListener"](target, "touchstart")
			|| w["namo"]["hasEventListener"](target, "touchdown")
			|| w["namo"]["hasEventListener"](target, "touchmove")
			|| w["namo"]["hasEventListener"](target, "mousedown")
			|| w["namo"]["hasEventListener"](target, "mousemove");
};

function namo_removeOnloadAttribute() {
    var onloadTargets = [ window, window["document"], window["document"]["documentElement"] ];
    if (window["document"]["body"])
        onloadTargets["push"](window["document"]["body"]);
    var imgs = document["getElementsByTagName"]("img");
    for ( var i = 0, len = imgs["length"]; i < len; i++)
        onloadTargets["push"](imgs[i]);

    for ( var i = 0, len = onloadTargets["length"]; i < len; i++) {
        var target = onloadTargets[i];
        if (target["onload"]) {
            window["namo_onload"]["push"](target);
            w["namo"]["addToNamoEvents"](target, "load", target["onload"]);
            target["onload"] = "";
        }
    }
}

w["namo"]["onDomContentLoaded"] = function(event) {
    console.log(" :: onDomContentLoaded :: ");

    //ResearchView에서 호출된 경우는 onLoad script가 수행되지 않도록 한다.
    //왜냐하면, thumbnail은 페이지의 가장 최초 상태로 생성되어야 하며, 그렇지 않으면, 페이지 로딩 시, 최초 상태가 아닌 상태(thumbnail)이 먼저 보여졌다가
    //이후 최초 상태가 다시 보여지면서 의도하지 않은 형태로 보여질 수 가 있다.
    if ( w["namo"]["referer"] == "research") {
        namo_removeOnloadAttribute();
        console.log("[createall] research view. so remove onload event.");
    }

	w["namo"]["processFallbacks"]();
	w["namo"]["processEpubSwitch"]();
	w["namo"]["processEpubTrigger"]();
	w["namo"]["processMedia"]();
	w["namo"]["processInput"]();
	w["namo"]["processNoteref"]();
};




w["namo"]["onClick"] = function(event) {

    console.log(" :: onClick :: ");

	var target = event.target;
	if (!target)
		return true;

	// 20170210 noteref popup window
	// if (w["namo"]["TAG"]["HASHREF"]["indexOf"]
	// (target["nodeName"]["toLowerCase"]()) >= 0
	// || target["getAttribute"]("epub:type") == "noteref") {
	// event["preventDefault"]();
	// event["stopPropagation"]();
	//
	// return false;
	// }
	var element = target;
	while (element) {
		var elementName = element["nodeName"]["toLowerCase"]();
		if (w["namo"]["TAG"]["HASHREF"]["indexOf"](elementName) >= 0
				|| element["getAttribute"]("epub:type") == "noteref"
				|| element["getAttribute"]("data-epub-type") == "noteref"
				|| element["getAttribute"]("data-epub:type") == "noteref") {
			event["preventDefault"]();
			event["stopPropagation"]();

			return false;
		}
		element = element["parentElement"];
	}
};
w["namo"]["finishLoad"] = function(params) {

    console.log(" :: finishLoad :: ");

	if (w["namo"]["isLoaded"])
		return;
	w["namo"]["isLoaded"] = true;

	// params
	var checksum = params["checksum"];

	w["namo"]["processBodyStyle"](checksum);
	var result = {
		"callbackName" : "onLoaded",
		"checksum" : checksum,
		"pageCount" : 1,
		"viewDirection" : w["namo"]["viewDirection"]
	};
	w["namo"]["calculatePageCount"]();
	if (w["namo"]["isReflowable"])
		result["pageCount"] = w["namo"]["totalPage"];

	w["namo"]["calculateMediaOverlay"]();
	result["mediaOverlay"] = w["namo"]["mediaOverlay"];

	return result;
};
w["namo"]["processNoteref"] = function() {

    console.log(" :: processNoteref :: ");

	var matchingElements = [];
	var allElements = w["document"]["getElementsByTagName"]("*");
	for (var i = 0, n = allElements["length"]; i < n; i++) {
		var elem = allElements[i];
		var href = elem["getAttribute"]("data-pubtree-href")
				|| elem["getAttribute"]("data-href")
				|| elem["getAttribute"]("href");
		var isNoteref = elem["getAttribute"]("epub:type") == "noteref"
				|| elem["getAttribute"]("data-epub-type") == "noteref"
				|| elem["getAttribute"]("data-epub-epub:type") == "noteref";
		if (href && isNoteref) {
			if (href["indexOf"]("#") == 0) {
				var noteref = w["document"]["getElementById"](href["substring"](1));
				if (noteref && noteref["nodeName"]["toLowerCase"]() == "aside" && noteref["getAttribute"]("epub:type") == "footnote")
					noteref["style"]["display"] = "none";
			}
		}
	}
};
w["namo"]["processBodyStyle"] = function(checksum) {

    console.log(" :: processBodyStyle :: ");

	w["namo"]["processVerticalStyle"]();
	w["namo"]["processRtlStyle"]();

//	w["document"]["documentElement"]["style"]["margin"] = "0px";
//	w["document"]["documentElement"]["style"]["padding"] = "0px";

	if (w["document"]["body"]) {
		w["document"]["body"]["style"]["margin"] = "0px";
		w["document"]["body"]["style"]["-webkit-tap-highlight-color"] = "rgba(0,0,0,0)";
	}

	if (w["namo"]["isReflowable"]) {
		w["namo"]["columnRatio"] = w["namo"]["columnWidth"] / w["namo"]["columnHeight"];

		w["namo"]["changeStyle"]({
			"checksum" : checksum,
			"fontSize" : w["namo"]["initialStyle"]["fontSize"],
			"fontFamily" : w["namo"]["initialStyle"]["fontFamily"],
			"lineHeight" : w["namo"]["initialStyle"]["lineHeight"],
			"initialize" : true
		});
		w["namo"]["changeTheme"]({
			"checksum" : checksum,
			"textColor" : w["namo"]["initialTheme"]["textColor"],
			"initialize" : true
		});

		if (w["document"]["body"]) {
		//EpubProviderImpl에서 css를 통해 설정
//		    w["document"]["body"]["style"]["backgroundColor"] = "transparent";
//            w["document"]["body"]["style"]["width"] = w["namo"]["columnWidth"] + "px";
//            w["document"]["body"]["style"]["height"] = w["namo"]["columnHeight"] + "px";
//            w["document"]["body"]["style"]["max-width"] = "none";
//            w["document"]["body"]["style"]["max-height"] = "none";
//            w["document"]["body"]["style"]["-webkit-column-gap"] = w["namo"]["columnGap"] + "px";
//            w["document"]["body"]["style"]["-webkit-column-width"] = w["namo"]["columnWidth"] + "px";

			w["document"]["body"]["style"]["padding"] = "0px";
			w["document"]["body"]["style"]["textAlign"] = "justify";

			w["namo"]["element"]["dummy"] = w["document"]["createElement"] ("div");
			w["namo"]["element"]["dummy"]["style"]["-webkit-column-break-before"] = "always";
			w["namo"]["element"]["dummy"]["style"]["width"] = w["namo"]["columnWidth"] + "px";
			w["namo"]["element"]["dummy"]["style"]["height"] = w["namo"]["columnHeight"] + "px";
			w["namo"]["element"]["dummy"]["style"]["margin"] = "0px";
			w["namo"]["element"]["dummy"]["style"]["backgroundColor"] = "transparent";
      		w["document"]["body"]["appendChild"](w["namo"]["element"]["dummy"]);

            w["namo"]["processMediaResize"]("img");
            w["namo"]["processMediaResize"]("video");
            w["namo"]["processDivResize"]();
            w["namo"]["processTableResize"]();
            w["namo"]["processImageResize"]();

            if(window.getComputedStyle(window.document.documentElement).getPropertyValue("-webkit-writing-mode") == "vertical-rl" ||
                window.getComputedStyle(window.document.body).getPropertyValue("-webkit-writing-mode") == "vertical-rl")
                w["namo"]["processRubyAdjust"]();
		}
		console.log(" :: processBodyStyle :: columnGap : " + window.namo.columnGap);
	}

	console.log("[createall] body's style = " + document.body.style.cssText);
};


w["namo"]["processImageResize"] = function() {
    console.log("[createall]namo.columnWidth = " + w["namo"]["columnWidth"]);
	var imgWidth = w["namo"]["columnWidth"] * 0.90;
	var imgHeight = w["namo"]["columnHeight"] * 0.90;
    console.log("[createall]imgWidth = " + imgWidth);
	var objects = w["document"]["getElementsByTagName"]("img");

	for (var i = 0, len = objects["length"]; i < len; i++) {
        //style attribute가 존재할 경우
		if (objects[i]["hasAttribute"]("style")) {
			var object = objects[i]["style"];
			console.log("[createall] object's style = " + object.cssText);

			if (object["width"] != null && object["width"]["indexOf"]("px") > 0) {
				var imgW = object["width"]["split"]("px");

				// if (imgW[0] > w["namo"]["columnWidth"]) {
				if (imgW[0] > imgWidth) {
					object["width"] = imgWidth + "px";

					var imgR = imgWidth / imgW[0];
					if (object["height"] != null && object["height"]["indexOf"]("px") > 0) {
						var imgH = object["height"]["split"]("px");
						object["height"] = (imgH[0] * imgR) + "px";
					}
				}
			}
			else {
    			objects[i]["setAttribute"]("style", "max-width : " + imgWidth + "px !important; max-height : " + imgHeight + "px !important;");
			}
		}
		else {
			var object = objects[i];

			if (object["hasAttribute"]("width") && object["getAttribute"]("width")["indexOf"]("px") > 0) {
				var imgW = object["getAttribute"]("width")["split"]("px");

                    console.log("[createall]else imgW = " + imgW + ", imgWidth = " + imgWidth);
				if (imgW[0] > imgWidth) {
					object["setAttribute"]("width", imgWidth + "px");

					var imgR = imgWidth / imgW[0];
					if (object["hasAttribute"]("height") && object["getAttribute"]("height")["indexOf"]("px") > 0) {
						var imgH = object["getAttribute"]("height")["split"]("px");
						object["setAttribute"]("height", (imgH[0] * imgR) + "px");
					}
				}
			}
			else {
    			object["setAttribute"]("style", "max-width : " + imgWidth + "px !important; height : auto !important;");
			}
		}

        //display속성이 block이 되면 정렬의 문제가 발생하여 처리하지 않음.
//        //display 속성이 없는 경우, 기본적으로 inline이 되어 이미지가 붙어나오고 잘려 보이는 경우가 있어, 부모 node의 display 속성을 따르도록 처리
//        if (objects[i].style["display"] == null || objects[i].style["display"] == "") {
//            objects[i].style["display"] = "inherit";
//        }
	}
};


w["namo"]["processMediaResize"] = function(type) {

    console.log(" :: processMediaResize :: ");

    var maxWidth = w["namo"]["columnWidth"];// * 0.90;
    var maxHeight = w["namo"]["columnHeight"];// * 0.90;

	var objects = w["document"]["getElementsByTagName"](type);
	for (var i = 0, len = objects["length"]; i < len; i++) {

        var object = objects[i];

	    // #1. Style 속성이 있는 경우
		if (object["hasAttribute"]("style")) {
			var styleObject = object["style"];

            // #1-1. Style width 0px 이상인 경우
			if (styleObject["width"] != null && styleObject["width"]["indexOf"]("px") > 0) {

                // Width가 columnWidth 보다 큰 경우
				var imgW = styleObject["width"]["split"]("px");
				if (imgW[0] > maxWidth) {
					styleObject["width"] = maxWidth + "px";

					var imgR = maxWidth / imgW[0];
					if (styleObject["height"] != null && styleObject["height"]["indexOf"]("px") > 0) {
						var imgH = styleObject["height"]["split"]("px");
						styleObject["height"] = (imgH[0] * imgR) + "px";
					}
				}
                // Height이 columnHeight 보다 큰 경우
				var imgH = styleObject["height"]["split"]("px");
				if (imgH[0] > maxHeight) {
                    styleObject["height"] = maxHeight + "px";

                    var imgR = maxHeight / imgH[0];
                    if (styleObject["width"] != null && styleObject["width"]["indexOf"]("px") > 0) {
                        var imgW = styleObject["width"]["split"]("px");
                        styleObject["width"] = (imgW[0] * imgR) + "px";
                    }
                }
			// #1-2. Style width 속성이 없는 경우
			} else {
                if(object.clientWidth > maxWidth || object.clientHeight > maxHeight){
                    var widthRatio = object.clientWidth/maxWidth;
                    var heightRatio = object.clientHeight/maxHeight;

                    if(widthRatio > heightRatio){
                      object.style.width = maxWidth + "px";
                      object.style.height = "auto";
                    } else {
                      object.style.width = "auto";
                      object.style.height = maxHeight + "px";
                    }
                }
          }
		// #2. Style 속성이 없는 경우
		} else {
            // #2-1. Width 속성이 있는 경우
			if (object["hasAttribute"]("width") && object["getAttribute"]("width")["indexOf"]("px") > 0) {
				var imgW = object["getAttribute"]("width")["split"]("px");

				if (imgW[0] > maxWidth) {
					object["setAttribute"]("width", maxWidth + "px");

					var imgR = maxWidth / imgW[0];
					if (object["hasAttribute"]("height") && object["getAttribute"]("height")["indexOf"] ("px") > 0) {
						var imgH = object["getAttribute"]("height")["split"] ("px");
						object["setAttribute"]("height", (imgH[0] * imgR) + "px");
					}
				}
            // #2-2. Width 속성이 없는 경우
            } else {
                if(object.clientWidth > maxWidth || object.clientHeight > maxHeight){
                    var widthRatio = object.clientWidth/maxWidth;
                    var heightRatio = object.clientHeight/maxHeight;

                    if(widthRatio > heightRatio){
                        object.style.width = maxWidth + "px";
                        object.style.height = "auto";
                    } else {
                        object.style.width = "auto";
                        object.style.height = maxHeight + "px";
                    }
                }
            }
		}
	}
};

w["namo"]["processDivResize"] = function() {

    console.log(" :: processDivResize :: ");

	var imgWidth = w["namo"]["columnWidth"] * 0.85;
	var objects = w["document"]["getElementsByTagName"]("div");
	for (var i = 0, len = objects["length"]; i < len; i++) {
		if (objects[i]["hasAttribute"]("style")) {
			var object = objects[i]["style"];

			if (object["width"] != null && object["width"]["indexOf"]("px") > 0) {
				var imgW = object["width"]["split"]("px");

				if (imgW[0] > imgWidth) {
					object["width"] = imgWidth + "px";
					var imgR = imgWidth / imgW[0];
				}
			}
		} else {
			var object = objects[i];

			if (object["hasAttribute"]("width")
					&& object["getAttribute"]("width")["indexOf"]("px") > 0) {
				var imgW = object["getAttribute"]("width")["split"]("px");

				if (imgW[0] > imgWidth) {
					object["setAttribute"]("width", imgWidth + "px");
					var imgR = imgWidth / imgW[0];
				}
			}

			object["setAttribute"]("style", "width : auto !important; height : auto !important;");
		}
	}
};

w["namo"]["processTableResize"] = function() {

	var imgWidth = w["namo"]["columnWidth"];
	var objects = w["document"]["getElementsByTagName"]("table");
	for (var i = 0, len = objects["length"]; i < len; i++) {
		if (objects[i]["hasAttribute"]("style")) {
			var object = objects[i]["style"];

			if (object["width"] != null && object["width"]["indexOf"]("px") > 0) {
				var imgW = object["width"]["split"]("px");

				if (imgW[0] > imgWidth) {
					object["width"] = imgWidth + "px";
				}
			}
		} else {
			var object = objects[i];

			if (object["hasAttribute"]("width")
					&& object["getAttribute"]("width")["indexOf"]("px") > 0) {
				var imgW = object["getAttribute"]("width")["split"]("px");

				if (imgW[0] > imgWidth) {
					object["setAttribute"]("width", imgWidth + "px");
				}
			}

			object["setAttribute"]("style", "width : auto !important; height : auto !important;");
		}
	}
};

w["namo"]["processRubyAdjust"] = function() {

    console.log(" :: processRubyAdjust :: ");

	var columnWidth = w["namo"]["columnWidth"];
	var columnHeight = w["namo"]["columnHeight"];
	var objects = w["document"]["getElementsByTagName"]("rt");

	for (var i = 0, len = objects.length; i < len; i++) {
	    var object = objects[i];

        if(object.getBoundingClientRect().left == 0){

            var parent = object.parentElement;

            if( parent.tagName.toLowerCase() == "ruby"){
                parent = parent.parentElement;

                var padding = 1;

                while(object.getBoundingClientRect().left == 0){
                    parent.style.paddingRight = padding + "em";
                    padding++;
                    if(padding == 5)
                        break;
                }
            } else {
                continue;
            }
        }
	}
}

w["namo"]["processFallbacks"] = function() {

    console.log(" :: processFallbacks :: ");

	var objects = w["document"]["getElementsByTagName"]("object");
	for (var len = objects["length"], i = len - 1; i >= 0; i--) {
		var object = objects[i];
		var mediaType = object["getAttribute"]("type");
		if (mediaType && w["namo"]["bindings"][mediaType]) {
			var a = w["document"]["createElement"]("a");
			a["href"] = object["getAttribute"]("data");
			a["innerHTML"] = "press to show plugin";
			a["namo_bindings_media-type"] = mediaType;
			a["namo_bindings_data"] = object["getAttribute"]("data");

			var paramTemp = [];
			var params = object["getElementsByTagName"]("param");
			for (var j = 0, len2 = params["length"]; j < len2; j++) {
				var param = params[j];
				paramTemp["push"](param["getAttribute"]("name") + "="
						+ param["getAttribute"]("value"));
			}
			a["namo_bindings_params"] = paramTemp["length"] > 0 ? paramTemp["join"]
					("&")
					: "";

			object["parentNode"]["replaceChild"](a, object);
		} else if (mediaType
				&& w["namo"]["CORE_MEDIA_TYPE"]["indexOf"](mediaType) < 0) {
			object["removeAttribute"]("type");
		}
	}

	var embeds = w["document"]["getElementsByTagName"]("embed");
	for (var i = 0, len = embeds["length"]; i < len; i++) {
		var embed = embeds[i];
		var mediaType = embed["getAttribute"]("type");
		if (mediaType && w["namo"]["CORE_MEDIA_TYPE"]["indexOf"](mediaType) < 0)
			embed["removeAttribute"]("type");
	}
};

w["namo"]["processEpubSwitch"] = function() {

    console.log(" :: processEpubSwitch :: ");

	var switchTemp = w["document"]["createElement"]("div");
	var switches = w["namo"]["getElementsByTagNameNS"]("switch", "epub");
	while (switches[0]) {
		var _switch = switches[0];
		_switch["parentNode"]["replaceChild"](switchTemp, _switch);

		var cases = w["namo"]["getElementsByTagNameNS"]
				("case", "epub", _switch);
		var choice = null;
		for (var i = 0, len = cases["length"]; i < len; i++) {
			var _case = cases[i];
			if (w["namo"]["REQUIRED_NAMESPACE"]["indexOf"]
					(_case["getAttribute"]("required-namespace")) >= 0) {
				choice = _case;
				break;
			}
		}

		if (!choice) {
			var _default = w["namo"]["getElementsByTagNameNS"]("default",
					"epub", _switch);
			if (_default["length"] > 0)
				choice = _default[0];
		}

		if (choice)
			while (choice["childNodes"][0])
				switchTemp["parentNode"]["insertBefore"](
						choice["childNodes"][0], switchTemp);

		switchTemp["parentNode"]["removeChild"](switchTemp);
	}
};
w["namo"]["processEpubTrigger"] = function() {

    console.log(" :: processEpubTrigger :: ");

	var triggerFunction = function(action, ref) {
		return function(event) {
			switch (action) {
			case "show":
				ref["style"]["visibility"] = "visible";
				break;
			case "hide":
				ref["style"]["visibility"] = "hidden";
				break;
			case "play":
				ref["play"]();
				break;
			case "pause":
				ref["pause"]();
				break;
			case "resume":
				ref["play"]();
				break;
			case "mute":
				ref["muted"] = true;
				break;
			case "unmute":
				ref["muted"] = false;
				break;
			}
		}
	}

	var triggers = w["namo"]["getElementsByTagNameNS"]("trigger", "epub");
	while (triggers[0]) {
		var trigger = triggers[0];
		var action = trigger["getAttribute"]("action");
		var ref = w["document"]["getElementById"](trigger["getAttribute"]
				("ref"));
		var event = trigger["getAttribute"]("ev:event");
		var observer = w["document"]["getElementById"](trigger["getAttribute"]
				("ev:observer"));

		if (observer && ref)
			observer["addEventListener"](event, triggerFunction(action, ref),
					false);

		var child = trigger["firstChild"];
		while (child) {
			trigger["parentNode"]["appendChild"](child);
			child = trigger["firstChild"];
		}

		trigger["parentNode"]["removeChild"](trigger);
	}
};

w["namo"]["processMedia"] = function() {

    console.log(" :: processMedia :: ");

	w["namo"]["element"]["media"] = w["document"]["querySelectorAll"](w["namo"]["TAG"]["MEDIA"]);

	var videoIndex = 0;
	for (var i = 0, len = w["namo"]["element"]["media"]["length"]; i < len; i++) {
		var media = w["namo"]["element"]["media"][i];
		if (media["nodeName"]["toLowerCase"]() == "video") {
            media["setAttribute"]("controlslist", "nodownload");

			media["namo_index"] = videoIndex++;
			media["addEventListener"]("play", function(e) {
				w["namo"]["callback"]({
					"callbackName" : "onVideoStateChanged",
					"index" : e["target"]["namo_index"],
					"state" : "play"
				});
			}, false, true);
			media["addEventListener"]("pause", function(e) {
				w["namo"]["callback"]({
					"callbackName" : "onVideoStateChanged",
					"index" : e["target"]["namo_index"],
					"state" : "pause"
				});
			}, false, true);
		}

		if (media["hasAttribute"]("autoplay")) {
			media["removeAttribute"]("autoplay");
			media["namo_autoplay"] = true;
		}

		// #VW-LIB-101-01-023 Add by Chris (2018-07-17)
        if (media["hasAttribute"]("preload")) {
            media["removeAttribute"]("preload");
        }
	}

    // MediaOverlay
	for (var i = 0, len = w["namo"]["mediaOverlay"]["length"]; i < len; i++) {
		var mediaOverlay = w["namo"]["mediaOverlay"][i];
		if (mediaOverlay["fragment"]) {
			var element = w["document"]["getElementById"]
					(mediaOverlay["fragment"]);
			if (element) {
				if (w["namo"]["TAG"]["MEDIA"]["indexOf"] (element["nodeName"]["toLowerCase"]()) >= 0) {
					element["addEventListener"]("play", w["namo"]["onMediaOverlayPlay"], false, true);
					element["addEventListener"]("pause", w["namo"]["onMediaOverlayPause"], false, true);
					element["addEventListener"]("ended", w["namo"]["onMediaOverlayEnded"], false, true);
				}

				mediaOverlay["epub:type"] = element["getAttribute"]("epub:type");
			}
		}
	}
};

w["namo"]["pauseMedia"] = function(params) {

    console.log(" :: pauseMedia :: ");

	if (w["namo"]["isReflowable"]) {
		if (!params["onlyPauseVideo"]) {
			for ( var i in w["namo"]["element"]["audio"])
				for (var audios = w["namo"]["element"]["audio"][i], j = 0, len2 = audios["length"]; j < len2; j++) {
					var audio = audios[j];
					if (!audio["paused"]) {
						audio["currentTime"] = 0;
						audio["pause"]();
					}
				}
		}

		var videos = w["namo"]["element"]["video"][w["namo"]["currentPage"]] || [];
		if (w["namo"]["isTwoPageMode"] && (w["namo"]["viewDirection"] == w["namo"]["DIRECTION_LTR"])) {
			var temp = w["namo"]["element"]["video"][w["namo"]["currentPage"] + 1];
			if (temp)
				videos = videos["concat"](temp);
		}
		for (var i = 0, len = videos["length"]; i < len; i++) {
			var video = videos[i];
			if (!video["paused"]) {
				video["currentTime"] = 0;
				video["pause"]();
			}
		}
	} else {
		for (var i = 0, len = w["namo"]["element"]["media"]["length"]; i < len; i++) {
			var media = w["namo"]["element"]["media"][i];
			if (params["onlyPauseVideo"]
					&& media["nodeName"]["toLowerCase"]() == "audio")
				continue;

			if (!media["paused"]) {
				media["currentTime"] = 0;
				media["pause"]();
			}
		}
	}
};
w["namo"]["playMedia"] = function() {

    console.log(" :: playMedia :: ");

	if (w["namo"]["isReflowable"]) {
		if (w["namo"]["useAudioAutoplay"]) {
			for ( var i in w["namo"]["element"]["audio"])
				for (var audios = w["namo"]["element"]["audio"][i], j = 0, len2 = audios["length"]; j < len2; j++) {
					var audio = audios[j];
					if (audio["namo_autoplay"])
						audio["play"]();
				}
		}

		if (w["namo"]["useVideoAutoplay"]) {
			var videos = w["namo"]["element"]["video"][w["namo"]["currentPage"]]
					|| [];
			for (var i = 0, len = videos["length"]; i < len; i++) {
				var video = videos[i];
				if (video["namo_autoplay"] && w["namo"]["sdkInt"] < 19)
					video["play"]();
			}
		}
	} else {
		for (var i = 0, len = w["namo"]["element"]["media"]["length"]; i < len; i++) {
			var media = w["namo"]["element"]["media"][i];
			if (media["namo_autoplay"]) {
				if (w["namo"]["useAudioAutoplay"]
						&& media["nodeName"]["toLowerCase"]() == "audio")
					media["play"]();
				else if (w["namo"]["useVideoAutoplay"]
						&& media["nodeName"]["toLowerCase"]() == "video")
					media["play"]();
			}
		}
	}
};
w["namo"]["processInput"] = function() {

    console.log(" :: processInput :: ");

	for (var i = 0, len = w["namo"]["initialTextInputs"]["length"]; i < len; i++) {
		var textInput = w["namo"]["initialTextInputs"][i];
		var input = w["document"]["getElementById"](textInput["id"]);
		if (input)
			input["value"] = textInput["value"];
	}
};
w["namo"]["processRtlStyle"] = function() {

    console.log(" :: processRtlStyle :: ");
    console.log(" :: processRtlStyle :: innerWidth : " + w["innerWidth"]);

	if ((w["document"]["body"] && w["getComputedStyle"](w["document"]["body"])["direction"] == "rtl")
			|| w["getComputedStyle"](w["document"]["documentElement"])["direction"] == "rtl") {
		w["namo"]["viewDirection"] = w["namo"]["DIRECTION_RTL"];
		w["document"]["documentElement"]["style"]["direction"] = "ltr";
		if (w["document"]["body"]) {
			w["document"]["body"]["style"]["direction"] = "ltr";

			var children = w["document"]["body"]["children"];
			for (var i = 0; i < children["length"]; i++)
				if (children[i]["style"])
					children[i]["style"]["direction"] = "rtl";
		}
	}
};
w["namo"]["processVerticalStyle"] = function() {

    console.log(" :: processVerticalStyle :: ");

    //document.documentElement.style["writing-mode"] = "vertical-rl";

	var styleSheets = w["document"]["styleSheets"];
	for (var i = 0, len = styleSheets["length"]; i < len; i++) {
		if (styleSheets[i]["ownerNode"]["rel"] == "stylesheet" && styleSheets[i]["ownerNode"]["className"] == "vertical") {
			w["namo"]["viewDirection"] = w["namo"]["DIRECTION_VERTICAL"];

			if (w["document"]["body"]) {
				w["document"]["body"]["style"]["-webkit-writing-mode"] = "vertical-rl";
			}
			break;
		}
	}
};
w["namo"]["calculateObjectPage"] = function(obj) {

    console.log(" :: calculateObjectPage :: ");

	if (!obj || !w["namo"]["isReflowable"] || !w["document"]["body"])
		return 1;

	var getRects = function(o) {
		var result = [];
		if (!o)
			return result;

		if (o["nodeType"] == w["Node"]["TEXT_NODE"]) {
			var range = w["document"]["createRange"]();
			range["selectNode"](o);
			o = range;
		}

		if (o["getClientRects"]) {
			var clientRects = o["getClientRects"]();
			if (clientRects["length"] > 0)
				result["push"](clientRects[0]);
			if (clientRects["length"] > 1)
				result["push"](clientRects[1]);
		} else if (o[0]) {
			result["push"](o[0]);
			if (o[1])
				result["push"](o[1]);
		} else {
			result["push"](o);
		}

		return result;
	};

	var rects = getRects(obj);
	if (obj["nodeType"]) {
		if (rects["length"] == 0
				&& obj["nodeType"] == w["Node"]["ELEMENT_NODE"]
				&& obj["nextSibling"]) {
			rects = getRects(obj["nextSibling"]);
			if (rects["length"] > 0)
				obj = obj["nextSibling"];
		}
		if (rects["length"] == 0
				&& obj["nodeType"] == w["Node"]["ELEMENT_NODE"]
				&& obj["prevSibling"]) {
			rects = getRects(obj["prevSibling"]);
			if (rects["length"] > 0)
				obj = obj["prevSibling"];
		}
		if (rects["length"] == 0
				&& obj["nodeType"] == w["Node"]["ELEMENT_NODE"]
				&& obj["parentElement"]) {
			rects = getRects(obj["parentElement"]);
			if (rects["length"] > 0)
				obj = obj["parentElement"];
		}

		if (obj["childNodes"] && obj["childNodes"]["length"] > 0) {
			var range = w["document"]["createRange"]();
			range["setStart"](obj, 0);
			range["setEnd"](obj, obj["childNodes"]["length"]);
			rects = rects["concat"](getRects(range));
		}
	}

	if (rects["length"] == 0)
		return 1;

	var containerRect = w["document"]["documentElement"]["getBoundingClientRect"] ();
	var pages = [];

	for (var i = 0, len = rects["length"]; i < len; i++) {
		var rect = rects[i];
		var page = 1;
		switch (w["namo"]["columnDirection"]) {
            case w["namo"]["DIRECTION_VERTICAL"]:
                page = w["Math"]["ceil"]((1 + rect["top"] - containerRect["top"]) / (w["namo"]["columnHeight"] + w["namo"]["columnGap"]));
                break;
            case w["namo"]["DIRECTION_RTL"]:
                page = w["Math"]["ceil"]((1 + containerRect["left"] - rect["left"]) / (w["namo"]["columnWidth"] + w["namo"]["columnGap"]));
                break;
            default:
                page = w["Math"]["ceil"]((1 + rect["left"] - containerRect["left"]) / (w["namo"]["columnWidth"] + w["namo"]["columnGap"]));
		}

		if (page <= 0)
			page = 1;

		pages["push"](page);
	}

	return w["Math"]["min"]["apply"](null, pages);
};
w["namo"]["calculateCfiPage"] = function(cfi) {
	if (!cfi || !cfi["start"])
		return 1;

	if (w["namo"]["isReflowable"]) {
		var startCfi = w["CFI"]["parse"](cfi["start"]);
		var endCfi = null;
		if (cfi["end"])
			endCfi = w["CFI"]["parse"](cfi["end"]);

		var cfiRects = w["CFI"]["getRects"](startCfi, endCfi);
		if (cfiRects["rects"]["length"] > 0)
			return w["namo"]["calculateObjectPage"](cfiRects["rects"]);
	}

	return 1;
};
w["namo"]["calculateCfiPages"] = function(params) {
	// params
	var checksum = params["checksum"];
	var cfis = params["cfis"];

	var pages = [];
	for (var i = 0; i < cfis["length"]; i++)
		pages["push"](w["namo"]["calculateCfiPage"](cfis[i]));

	return {
		"callbackName" : "onCfiPagesCalculated",
		"checksum" : checksum,
		"pages" : pages
	};
};

w["namo"]["calculateFragmentPages"] = function(params) {
	// params
	var checksum = params["checksum"];
	var fragments = params["fragments"];
	var pages = {};
	for ( var epubType in fragments) {
		pages[epubType] = [];
		for (var i = 0; i < fragments[epubType]["length"]; i++) {
			var fragment = fragments[epubType][i];
			if (fragment) {
				page = w["namo"]["calculateObjectPage"]
						(w["document"]["getElementById"](fragment));
			} else
				page = 1;
			pages[epubType]["push"](page);
		}
	}

	return {
		"callbackName" : "onFragmentPagesCalculated",
		"checksum" : checksum,
		"pages" : pages
	};
};
w["namo"]["calculatePageCount"] = function() {

    console.log(" :: calculatePageCount :: ");

	if (w["namo"]["isReflowable"]) {
		if (w["namo"]["element"]["dummy"]) {
			var containerRect = w["document"]["documentElement"]["getBoundingClientRect"] ();
			var dummyRect = w["namo"]["element"]["dummy"]["getBoundingClientRect"] ();

			var left = dummyRect["left"] - containerRect["left"];
			var top = dummyRect["top"] - containerRect["top"];

			if (top >= w["namo"]["columnHeight"])
				w["namo"]["columnDirection"] = w["namo"]["DIRECTION_VERTICAL"];
			else if (left < 0)
				w["namo"]["columnDirection"] = w["namo"]["DIRECTION_RTL"];
			else
				w["namo"]["columnDirection"] = w["namo"]["DIRECTION_LTR"];

            console.log(" :: calculatePageCount :: columnDirection : " + w["namo"]["columnDirection"] + " (LTR : 0, RTL : 1, VERTICAL : 2)");

			switch (w["namo"]["columnDirection"]) {
                case w["namo"]["DIRECTION_VERTICAL"]:
                    w["namo"]["totalPage"] = w["Math"]["ceil"](top / (w["namo"]["columnHeight"] + w["namo"]["columnGap"]));
                    break;
                case w["namo"]["DIRECTION_RTL"]:
                    w["namo"]["totalPage"] = w["Math"]["ceil"](-left / (w["namo"]["columnWidth"] + w["namo"]["columnGap"]));
                    break;
                default:
                    w["namo"]["totalPage"] = w["Math"]["ceil"](left / (w["namo"]["columnWidth"] + w["namo"]["columnGap"]));
			}
		}

		// 20160503 over lollipop version webview issue (ver 50.0.2661.86)
		if (w["namo"]["sdkInt"] > 19 && w["namo"]["isReflowable"]) {
			if (left >= 0 && top < 0)
				w["namo"]["totalPage"] = 1;
		}

		if (w["namo"]["totalPage"] <= 0)
			w["namo"]["totalPage"] = 1;

		for (var i = 0, len = w["namo"]["element"]["media"]["length"]; i < len; i++) {
			var media = w["namo"]["element"]["media"][i];
			var mediaArray = media["nodeName"]["toLowerCase"]() == "audio" ? w["namo"]["element"]["audio"] : w["namo"]["element"]["video"];
			var mediaPage = w["namo"]["calculateObjectPage"](media);

			if (w["namo"]["isTwoPageMode"] && (w["namo"]["viewDirection"] == w["namo"]["DIRECTION_LTR"]))
				mediaPage = 2 * ((mediaPage - 1) / 2) + 1;

			if (mediaArray[mediaPage])
				mediaArray[mediaPage]["push"](media);
			else
				mediaArray[mediaPage] = [ media ];
		}
	}
};
w["namo"]["calculateMediaOverlay"] = function() {
	for (var i = 0, len = w["namo"]["mediaOverlay"]["length"]; i < len; i++) {
		var mediaOverlay = w["namo"]["mediaOverlay"][i];
		if (mediaOverlay["fragment"]) {
			var element = w["document"]["getElementById"]
					(mediaOverlay["fragment"]);
			if (element)
				mediaOverlay["page"] = w["namo"]["calculateObjectPage"]
						(element);
		}
	}
};
w["namo"]["playMediaOverlay"] = function(params) {
	var element = w["document"]["getElementById"](params["fragment"]);

	if (w["namo"]["mediaOverlayPlaybackActiveClass"]
			&& w["document"]["documentElement"]["classList"])
		w["document"]["documentElement"]["classList"]["add"]
				(w["namo"]["mediaOverlayPlaybackActiveClass"]);

	if (w["namo"]["mediaOverlayActiveClass"]
			&& w["document"]["documentElement"]["classList"]) {
		var removeElement = w["document"]["getElementById"]
				(w["namo"]["currentMediaOverlayFragment"]);
		if (removeElement)
			removeElement["classList"]["remove"]
					(w["namo"]["mediaOverlayActiveClass"]);
		if (element)
			element["classList"]["add"](w["namo"]["mediaOverlayActiveClass"]);
	}
	w["namo"]["currentMediaOverlayFragment"] = params["fragment"];

	if (w["namo"]["TAG"]["MEDIA"]["indexOf"](element["nodeName"]["toLowerCase"]
			()) >= 0) {
		if (w["namo"]["currentMediaOverlayFragment"] != w["namo"]["currentEmbeddedMOFragment"])
			element["currentTime"] = 0;
		element["play"]();
		element["justEnded"] = false;
		w["namo"]["currentEmbeddedMOFragment"] = w["namo"]["currentMediaOverlayFragment"];
	} else {
		w["namo"]["currentEmbeddedMOFragment"] = null;
	}

};
w["namo"]["pauseMediaOverlay"] = function() {
	if (w["document"]["documentElement"]["classList"]) {
		w["document"]["documentElement"]["classList"]["remove"]
				(w["namo"]["mediaOverlayPlaybackActiveClass"]);
		for (var i = 0, len = w["namo"]["mediaOverlay"]["length"]; i < len; i++) {
			var fragment = w["namo"]["mediaOverlay"][i]["fragment"];
			var element = w["document"]["getElementById"](fragment);
			if (element) {
				element["classList"]["remove"]
						(w["namo"]["mediaOverlayActiveClass"]);
				if (element
						&& w["namo"]["TAG"]["MEDIA"]["indexOf"]
								(element["nodeName"]["toLowerCase"]()) >= 0) {
					element["pause"]();
				}
			}
		}
	}

	w["namo"]["currentMediaOverlayFragment"] = null;
};
w["namo"]["onMediaOverlayPlay"] = function() {
	w["namo"]["callback"]({
		"callbackName" : "onMediaOverlayStateChanged",
		"state" : "play",
		"fragment" : w["namo"]["currentMediaOverlayFragment"]
	});
};
w["namo"]["onMediaOverlayPause"] = function() {
	var element = w["document"]["getElementById"]
			(w["namo"]["currentMediaOverlayFragment"]);
	if (element && !element["justEnded"])
		w["namo"]["callback"]({
			"callbackName" : "onMediaOverlayStateChanged",
			"state" : "pause",
			"fragment" : w["namo"]["currentMediaOverlayFragment"]
		});
};
w["namo"]["onMediaOverlayEnded"] = function() {
	var element = w["document"]["getElementById"]
			(w["namo"]["currentMediaOverlayFragment"]);
	if (element) {
		element["currentTime"] = 0;
		element["justEnded"] = true;
	}
	w["namo"]["callback"]({
		"callbackName" : "onMediaOverlayStateChanged",
		"state" : "ended",
		"fragment" : w["namo"]["currentMediaOverlayFragment"]
	});
};
w["namo"]["insertStyle"] = function(params) {

    console.log(" :: insertStyle :: ");

	var checksum = params["checksum"];
	var s = w["document"]["createElement"]("style");
	s["type"] = "text/css";
	// adding to head must precede setting style.
	w["document"]["getElementsByTagName"]("head")[0]["appendChild"](s);
	s["innerHTML"] = params["inlineCssStyle"];
}
w["namo"]["changeStyle"] = function(params) {

//    console.log(" :: changeStyle :: ");

	// params
	var checksum = params["checksum"];
	var fontFamily = params["fontFamily"];
	var fontSize = params["fontSize"];
	var lineHeight = params["lineHeight"];
	var initialize = params["initialize"];

	var defaultFontFamily = fontFamily == "none";
	var elements = w["document"]["querySelectorAll"](w["namo"]["TAG"]["TEXT"]);

//    console.log(" :: changeStyle :: initialize : " + initialize);

	if (initialize) {
		for (var i = 0, len = elements["length"]; i < len; i++) {
			var element = elements[i];
			var computedStyle = w["getComputedStyle"](element, null);

			element["namo_fontSize"] = w["parseInt"](computedStyle["fontSize"]);
			element["namo_fontFamily"] = computedStyle["fontFamily"];
//			console.log(" :: changeStyle :: fontFamily : " + computedStyle["fontFamily"]);
			element["namo_lineHeight"] = computedStyle["lineHeight"] == "normal" ? "normal"
					: w["parseInt"](computedStyle["lineHeight"]);
		}
	}

	for (var i = 0, len = elements["length"]; i < len; i++) {
		var element = elements[i];

		element["style"]["fontSize"] = w["parseInt"](element["namo_fontSize"] * fontSize) + "px";
		element["style"]["fontFamily"] = defaultFontFamily ? element["namo_fontFamily"] : fontFamily;
		element["style"]["lineHeight"] = element["namo_lineHeight"] == "normal" ? 1.6 * lineHeight * fontSize
				: (w["parseInt"](element["namo_lineHeight"] * lineHeight * fontSize) + "px");
	}

	if (!initialize) {
		w["namo"]["calculatePageCount"]();

		return {
			"callbackName" : "onStyleChanged",
			"checksum" : checksum,
			"pageCount" : w["namo"]["totalPage"]
		};
	}
};
w["namo"]["changeTheme"] = function(params) {

    console.log(" :: changeTheme :: ");

	// params
	var checksum = params["checksum"];
	var textColor = params["textColor"];
	var initialize = params["initialize"];

	var defaultColor = "none" == textColor;
	w["document"]["body"]["style"]["color"] = defaultColor ? element["namo_color"]
			: textColor;

	if (!initialize)
		return {
			"callbackName" : "onThemeChanged",
			"checksum" : checksum
		};
};
w["namo"]["hitTest"] = function(params) {
	// params
	var percentageX = params["percentageX"];
	var percentageY = params["percentageY"];

//	console.log(" :: hitTest :: percentageX = " + percentageX + ", percentageY = " + percentageY);

	var result = {
		"callbackName" : "onHitTested",
		"nodeName" : null,
		"attributes" : {},
		"hasClickable" : false,
		"windowHasClickable" : false,
		"hasLink" : false,
		"hasScroll" : false,
		"windowHasScroll" : false,
		"isTextInput" : false,
		"rect" : {
			"left" : 0,
			"top" : 0,
			"right" : 0,
			"bottom" : 0
		},
		"mediaOverlay" : null
	};
	var target = w["namo"]["getHitTestTarget"](w["namo"]["toX"](percentageX), w["namo"]["toY"](percentageY));

	if (!target) {
		return result;
	}

	result["nodeName"] = target["nodeName"]["toLowerCase"]();

//	console.log(" :: hitTest :: nodeName = " + result["nodeName"]);

	var computedStyle = null;

	if (target["getAttribute"]("epub:type") == "noteref"
			|| target["getAttribute"]("data-epub-type") == "noteref"
			|| target["getAttribute"]("data-epub:type") == "noteref") {
		// 141105 check epub:type="noteref" for all elements

		var href = target["getAttribute"]("data-pubtree-href")
				|| target["getAttribute"]("data-href")
				|| target["getAttribute"]("href");
		result["attributes"]["href"] = href;
		if (target["getAttribute"]("data-href") != null) {
			result["nodeName"] = "namo_href";
			result["attributes"]["noteref"] = "noteref";
		} else
			result["nodeName"] = "namo_noteref";
		if (href["indexOf"]("#") == 0) {
			var note = w["document"]["getElementById"](href["substring"](1))["cloneNode"]
					(true);
			note["style"]["removeProperty"]("display");
			result["attributes"]["noteref"] = note["outerHTML"];
		}
		result["rect"] = w["namo"]["toPercentageRect"]
				(target["getBoundingClientRect"]());
	}
	/**
	 * 20160315 # 15585 [Android][Trac1108] 도형에 하이퍼 링크로 외부 사이트의 MP4 파일 연결 fail
	 */
	else if (target["hasAttribute"]("data-pubtree-href")
			|| target["hasAttribute"]("data-href")) {
		var href = target["getAttribute"]("data-pubtree-href")
				|| target["getAttribute"]("data-href")
				|| target["getAttribute"]("href");
		result["nodeName"] = "namo_href";
		result["attributes"]["href"] = href;
	} else {
		if (result["nodeName"] == "a") {
			var mediaType = target["namo_bindings_media-type"];
			if (mediaType) {
				result["nodeName"] = "namo_bindings";
				result["attributes"]["mediaType"] = mediaType;
				result["attributes"]["handler"] = w["namo"]["bindings"][mediaType];
				result["attributes"]["data"] = target["namo_bindings_data"];
				result["attributes"]["params"] = target["namo_bindings_params"];
			}
		} else if (result["nodeName"] == "img") {
			result["attributes"]["src"] = target["getAttribute"]("src");
		} else if (result["nodeName"] == "textarea"
				|| result["nodeName"] == "input") {
			result["hasClickable"] = true;
			result["hasScroll"] = true;
			result["isTextInput"] = result["nodeName"] == "textarea"
					|| (result["nodeName"] == "input" && (w["namo"]["TAG"]["TEXT_INPUT_TYPE"]["indexOf"]
							(target["getAttribute"]("type")) >= 0))

			if (result["isTextInput"]) {
				w["namo"]["element"]["focusedText"] = target;
				if (target["hasAttribute"]("id"))
					result["attributes"]["textInputId"] = target["getAttribute"]
							("id");
				result["rect"] = w["namo"]["toPercentageRect"]
						(target["getBoundingClientRect"]());
				computedStyle = w["getComputedStyle"](target, null);
				result["attributes"]["font-size"] = computedStyle["font-size"];
				result["attributes"]["text"] = target["value"];
				result["attributes"]["type"] = target["getAttribute"]("type");
			}
		}
	}
	var element = target;

	while (element) {
		var elementName = element["nodeName"]["toLowerCase"]();

		computedStyle = w["getComputedStyle"](element, null);

		result["hasClickable"] = result["hasClickable"]
				|| w["namo"]["hasClickEventListener"](element);

		if (!result["hasClickable"]
				&& w["namo"]["TAG"]["HASHREF"]["indexOf"](elementName) >= 0) {
			result["attributes"]["href"] = element["getAttribute"]("href");

			if (element["getAttribute"]("epub:type") == "noteref"
					|| element["getAttribute"]("data-epub-type") == "noteref"
					|| element["getAttribute"]("data-epub:type") == "noteref") {
				if (element["getAttribute"]("data-href") != null) {
					result["nodeName"] = "namo_href";
					result["attributes"]["noteref"] = "noteref";
				} else
					result["nodeName"] = "namo_noteref";

				result["hasClickable"] = false;
				result["hasLink"] = false;
			} else {
				result["hasLink"] = true;
				result["hasClickable"] = true;
			}
		}

		/**
		 * 20160315 # 15585 [Android][Trac1108] 도형에 하이퍼 링크로 외부 사이트의 MP4 파일 연결
		 * fail
		 */
		result["hasClickable"] = result["nodeName"] != "namo_href"
				&& (result["hasClickable"] || (w["namo"]["TAG"]["CLICK"]["indexOf"]
						(elementName) >= 0));

		result["hasClickable"] = result["nodeName"] != "namo_noteref"
				&& (result["hasClickable"] || (w["namo"]["TAG"]["CLICK"]["indexOf"]
						(elementName) >= 0));

		result["hasScroll"] = result["hasScroll"]
				|| w["namo"]["hasScrollEventListener"](element);

		/**
		 * 20160415 # 15712 [Android] [Trac1136] Android에서 스크롤있는 경우, 페이지 이동문제
		 */
		// if (w["namo"]["isReflowable"]) {
		result["hasScroll"] = result["hasScroll"]
				|| ((computedStyle["overflow-x"] == "scroll" || computedStyle["overflow-x"] == "auto") && (element["scrollWidth"] > element["clientWidth"]));
		result["hasScroll"] = result["hasScroll"]
				|| ((computedStyle["overflow-y"] == "scroll" || computedStyle["overflow-y"] == "auto") && (element["scrollHeight"] > element["clientHeight"]));
		// }

		if (!result["mediaOverlay"]) {
			var id = element["getAttribute"]("id");
			if (id) {
				for (var i = 0, len = w["namo"]["mediaOverlay"]["length"]; i < len; i++) {
					var fragment = w["namo"]["mediaOverlay"][i]["fragment"];
					if (id == fragment
							&& w["namo"]["TAG"]["MEDIA"]["indexOf"]
									(element["nodeName"]["toLowerCase"]()) < 0) {
						result["mediaOverlay"] = fragment;
						break;
					}
				}
			}
		}

		element = element["parentElement"];
	}

	result["windowHasClickable"] = w["namo"]["hasClickEventListener"]
			(w["document"])
			|| w["namo"]["hasClickEventListener"](w);
	result["windowHasScroll"] = w["namo"]["hasScrollEventListener"]
			(w["document"])
			|| w["namo"]["hasScrollEventListener"](w);
	return result;
};
w["namo"]["getHitTestTarget"] = function(x, y) {

	var target = w["document"]["elementFromPoint"](x, y);
	if (target && w["namo"]["TAG"]["CLICK"]["indexOf"](target["nodeName"]["toLowerCase"]()) >= 0)
		return target;

	for (var i = -1; i <= 1; i++) {
		for (var j = -1; j <= 1; j++) {
			if (i == 0 && j == 0)
				continue;

			var element = w["document"]["elementFromPoint"](x + i
					* w["namo"]["HIT_TEST_THRESHOLD"], y + j
					* w["namo"]["HIT_TEST_THRESHOLD"]);
			if (element
					&& w["namo"]["TAG"]["CLICK"]["indexOf"]
							(element["nodeName"]["toLowerCase"]()) >= 0)
				return element;
		}
	}

	return target;
};

w["namo"]["changePage"] = function(params) {

    console.log(" :: changePage :: ");

	// params
	var checksum = params["checksum"];
	var page = params["page"];

	var result = {
		"callbackName" : "onPageChanged",
		"checksum" : checksum,
		"page" : 1,
		"pageCountshowing" : 1,
		"videoRects" : [],
		"columnDirection" : 1
	};

	if (w["namo"]["isReflowable"]) {

        console.log(" :: changePage :: columnDirection : " + w["namo"]["columnDirection"] + " (0:DIRECTION_LTR, 1:DIRECTION_RTL, 2:DIRECTION_VERTICAL)");

        //scrollTo(x좌표, y좌표)
        //vertical일 경우, 다단이 세로로 생기므로 y좌표를 옮기고
        //horizontal일 때는  RTL/LTR에 따라 왼쪽 혹은 오른쪽으로 옮김
		switch (w["namo"]["columnDirection"]) {
    		case w["namo"]["DIRECTION_VERTICAL"]:
	    		w["scrollTo"](0, (page - 1) * (w["namo"]["columnHeight"] + w["namo"]["columnGap"]));
			    break;
            case w["namo"]["DIRECTION_RTL"]:
                w["scrollTo"](-(page - 1) * (w["namo"]["columnWidth"] + w["namo"]["columnGap"]), 0);
                break;
            default:
                w["scrollTo"]((page - 1) * (w["namo"]["columnWidth"] + w["namo"]["columnGap"]), 0);
                break;
		}

		result["columnDirection"] = w["namo"]["columnDirection"];

		var currentVideos = w["namo"]["element"]["video"][w["namo"]["currentPage"]] || [];
		if (w["namo"]["isTwoPageMode"] && (w["namo"]["viewDirection"] == w["namo"]["DIRECTION_LTR"])) {
			var temp = w["namo"]["element"]["video"][w["namo"]["currentPage"] + 1];
			if (temp)
				currentVideos = currentVideos["concat"](temp);
		}
		for (var i = 0, len = currentVideos["length"]; i < len; i++) {
			var currentVideo = currentVideos[i];
			currentVideo["pause"]();
			try {
				currentVideo["currentTime"] = 0;
			} catch (e) {
			}
		}

		var nextVideos = w["namo"]["element"]["video"][page] || [];
		if (w["namo"]["isTwoPageMode"] && (w["namo"]["viewDirection"] == w["namo"]["DIRECTION_LTR"])) {
			var temp = w["namo"]["element"]["video"][page + 1];
			if (temp)
				nextVideos = nextVideos["concat"](temp);
		}
		w["namo"]["currentPage"] = page;

		for (var i = 0, len = nextVideos["length"]; i < len; i++) {
			var nextVideo = nextVideos[i];
			if (nextVideo["namo_autoplay"])
				result["videoRects"]["push"](w["namo"]["toPercentageRect"]
						(nextVideo["getBoundingClientRect"]()));
		}

		result["page"] = page;

		console.log(" :: changePage :: isTwoPageMode : " + w["namo"]["isTwoPageMode"]);
		console.log(" :: changePage :: page : " + page);
		console.log(" :: changePage :: totalPage : " + w["namo"]["totalPage"]);

		if (w["namo"]["isTwoPageMode"])
			result["pageCountShowing"] = (w["namo"]["isTwoPageMode"] && page != w["namo"]["totalPage"]) ? 2 : 1;
		else
			result["pageCountShowing"] = page > w["namo"]["totalPage"] ? 0 : 1;
	} else {
		for (var i = 0, len = w["namo"]["element"]["media"]["length"]; i < len; i++) {
			var media = w["namo"]["element"]["media"][i];
			if (media["namo_autoplay"]
					&& media["nodeName"]["toLowerCase"]() == "video")
				result["videoRects"]["push"](w["namo"]["toPercentageRect"]
						(media["getBoundingClientRect"]()));
		}
	}
	return result;
};

w["namo"]["setInputText"] = function(params) {
	// params
	var text = w["unescape"](params["text"]);

	if (w["namo"]["element"]["focusedText"])
		w["namo"]["element"]["focusedText"]["value"] = text;
};
w["namo"]["search"] = function(params) {
	var checksum = params["checksum"];
	var keyword = w["unescape"](params["keyword"]);
	var prevTextLength = w["Math"]["max"](params["prevTextLength"], 1);
	var nextTextLength = w["Math"]["max"](params["nextTextLength"], 1);

	var node = w["document"]["body"];
	var upwards = false;
	var matches = [];
	var temp = [];
	var prevText = "";
	while (node) {
		if (node["nodeType"] == w["Node"]["TEXT_NODE"]) {
			var nodeValue = node["nodeValue"];
			if (nodeValue != "") {
				for (var i = 0, len = nodeValue["length"]; i < len; i++) {
					var c = nodeValue["charAt"](i);

					var j = 0;
					while (j < temp["length"]) {
						var t = temp[j];
						if (t["matched"]) {
							t["nextText"] += c;
							if (t["nextText"]["length"] >= nextTextLength) {
								matches["push"](t);
								temp["splice"](j, 1);
								continue;
							}
						} else if (c["toLowerCase"]() == keyword["charAt"]
								(t["index"])["toLowerCase"]()) {
							t["keyword"] += c;
							t["index"] += 1;

							if (t["index"] == keyword["length"]) {
								t["range"]["setEnd"](node, i + 1);
								t["matched"] = true;
							}
						} else {
							temp["splice"](j, 1);
							continue;
						}
						j++;
					}

					if (c["toLowerCase"]() == keyword["charAt"](0)["toLowerCase"]
							()) {
						var t = {
							"range" : w["document"]["createRange"](),
							"index" : 1,
							"matched" : keyword["length"] == 1,
							"keyword" : c,
							"prevText" : prevText,
							"nextText" : ""
						};

						t["range"]["setStart"](node, i);
						if (t["matched"])
							t["range"]["setEnd"](node, i + 1);

						temp["push"](t);
					}

					if (prevText["length"] >= prevTextLength)
						prevText = prevText["substring"](1);
					prevText += c;
				}
			}
		}

		if (node["nodeType"] == w["Node"]["ELEMENT_NODE"]) {
			if (!upwards) {
				var display = w["getComputedStyle"](node)["display"];

				if (!/inline/i["test"](display)) {
					for (var i = 0, len = temp["length"]; i < len; i++) {
						var t = temp[i];
						if (t["matched"])
							matches["push"](t);
					}
					temp = [];
					prevText = "";
				}

				var rect = node["getBoundingClientRect"]();
				if (rect["width"] != 0 && rect["height"] != 0
						&& node["hasChildNodes"]()) {
					upwards = false;
					node = node["firstChild"];
					continue;
				}
			}
		}
		upwards = false;

		if (node["nextSibling"]) {
			node = node["nextSibling"];
			continue;
		}

		if (node["parentNode"]) {
			upwards = true;
			node = node["parentNode"];
			continue;
		} else {
			break;
		}
	}

	if (temp["length"] > 0)
		for (var i = 0, len = temp["length"]; i < len; i++) {
			var t = temp[i];
			if (t["matched"])
				matches["push"](t);
		}

	var results = [];
	for (var i = 0, len = matches["length"]; i < len; i++) {
		var t = matches[i];

		var cfi = window.CFI;
        cfi.start = window.CFI.generate(t.range.startContainer, t.range.startOffset);
        cfi.end = window.CFI.generate(t.range.endContainer, t.range.endOffset);

		results["push"]({
			"keyword" : t["keyword"],
			"prevText" : t["prevText"]["replace"](/(^\s*)/g, "")["replace"](/(\s+$)/g, " "),
			"nextText" : t["nextText"]["replace"](/(^\s+)/g, " ")["replace"](/(\s*$)/g, ""),
            "startCfi" : cfi.start,
            "endCfi" : cfi.end,
            // #22. 검색결과에 페이지 정보 넘겨주도록 변경 Add by Chris (2018-09-20)
            "page" : window.namo.calculateCfiPage(cfi)
		});
	}

	return {
		"callbackName" : "onSearched",
		"checksum" : checksum,
		"results" : results
	};
};
w["namo"]["startSelection"] = function(params) {

    console.log(" :: startSelection :: ");

	// params
	var percentageX = params["percentageX"];
	var percentageY = params["percentageY"];

	var x = w["namo"]["toX"](percentageX), y = w["namo"]["toY"](percentageY);
	var range = w["document"]["caretRangeFromPoint"](x, y);
	if (!range) {
		w["namo"]["clearSelection"]();
		return;
	}
	if (range["startContainer"]["nodeType"] == w["Node"]["TEXT_NODE"])
		try {
			range["expand"]("word");
		} catch (e) {
			if (range["endOffset"] < range["startContainer"]["length"])
				range["setEnd"](range["endContainer"], range["endOffset"] + 1);
			else
				range["setStart"](range["startContainer"],
						range["startOffset"] - 1);
		}

	w["namo"]["range"]["start"] = range["cloneRange"]();
	w["namo"]["range"]["start"]["collapse"](true);
	w["namo"]["range"]["end"] = range["cloneRange"]();
	w["namo"]["range"]["end"]["collapse"](false);

	w["namo"]["confirmSelection"]();
};
w["namo"]["changeSelectionBounds"] = function(params) {

    console.log(" :: changeSelectionBounds :: ");

	// params
	var start = params["start"];
	var percentageX = params["percentageX"];
	var percentageY = params["percentageY"];

	if (percentageX < 0)
		percentageX = 0;
	else if (percentageX > 1)
		percentageX = 1;

	if (percentageY < 0)
		percentageY = 0;
	else if (percentageY > 1)
		percentageY = 1;

	var x = w["namo"]["toX"](percentageX), y = w["namo"]["toY"](percentageY);
	var range = w["document"]["caretRangeFromPoint"](x, y);
	if (range
			&& w["namo"]["range"][start ? "start" : "end"]["compareBoundaryPoints"]
					(w["Range"][start ? "START_TO_START" : "END_TO_END"], range) != 0) {
		w["namo"]["range"][start ? "start" : "end"] = range;
		w["namo"]["changeSelection"]({
			"confirm" : false
		});
	}
};
w["namo"]["changeSelection"] = function(params) {

    console.log(" :: changeSelection :: ");

	var confirm = params["confirm"];

	var start = w["namo"]["range"]["start"];
	var end = w["namo"]["range"]["end"];
	var result = {
		"callbackName" : "onSelectionChanged",
		"rects" : []
	};

	if (!start || !end) {
		if (confirm)
			w["namo"]["clearSelection"]();
		else
			w["namo"]["callback"](result);

		return;
	}

	if (start["compareBoundaryPoints"](w["Range"]["END_TO_START"], end) > 0) {
		var temp = start;
		start = end;
		end = temp;
	}

	var range = w["document"]["createRange"]();
	range["setStart"](start["startContainer"], start["startOffset"]);
	range["setEnd"](end["endContainer"], end["endOffset"]);

	var ranges = w["namo"]["getTextRanges"](range);
	for (var i = 0, len = ranges["length"]; i < len; i++) {
		var rects = ranges[i]["rects"];
		for (var j = 0, len2 = rects["length"]; j < len2; j++)
			result["rects"]["push"](rects[j]);
	}
	if (confirm) {
		if (result["rects"]["length"] == 0) {
			w["namo"]["clearSelection"]();

			return;
		} else {
			w["namo"]["range"]["start"] = start = ranges[0]["range"]["cloneRange"] ();
			w["namo"]["range"]["start"]["collapse"](true);
			w["namo"]["range"]["end"] = end = ranges[ranges["length"] - 1]["range"]["cloneRange"] ();
			w["namo"]["range"]["end"]["collapse"](false);

			result["startCfi"] = w["CFI"]["generate"](start["startContainer"], start["startOffset"]);
			result["endCfi"] = w["CFI"]["generate"](end["endContainer"], end["endOffset"]);

			var texts = [];
			for (var i = 0, len = ranges["length"]; i < len; i++)
				texts["push"](ranges[i]["range"]["toString"]()["replace"](/(^\s*)|(\s*$)/gi, ""));
			result["selectedText"] = texts["join"](" ");
		}
	}
	w["namo"]["callback"](result);
};

w["namo"]["confirmSelection"] = function() {

    console.log(" :: confirmSelection :: ");

	w["namo"]["changeSelection"]({
		"confirm" : true
	});
};

w["namo"]["clearSelection"] = function() {

    console.log(" :: clearSelection :: ");

	w["namo"]["range"]["start"] = null;
	w["namo"]["range"]["end"] = null;
	w["namo"]["callback"]({
		"callbackName" : "onSelectionCleared"
	});
};

w["namo"]["calculateAnnotations"] = function(params) {

    console.log(" :: calculateAnnotations :: ");

	// params
	var checksum = params["checksum"];
	var annotations = params["annotations"];
	var result = {
		"callbackName" : "onAnnotationCalculated",
		"checksum" : checksum,
		"annotations" : []
	};

	for (var i = 0, len = annotations["length"]; i < len; i++) {
		var annotation = annotations[i];
		var o = {
			"type" : annotation["type"],
			"visible" : false,
			"rects" : []
		};
		result["annotations"]["push"](o);

		var startCfi = w["CFI"]["parse"](annotation["startCfi"]);
		var endCfi = null;
		if (annotation["endCfi"])
			endCfi = w["CFI"]["parse"](annotation["endCfi"]);
		var cfiRects = w["CFI"]["getRects"](startCfi, endCfi);

		if (annotation["key"])
			o["key"] = annotation["key"];

		if (annotation["type"] == 2) {
			for (var j = 0, len2 = cfiRects["rects"]["length"]; j < len2; j++) {
				var rect = w["namo"]["intersectRect"](cfiRects["rects"][j]);
				if (rect["left"] == 0 && rect["top"] == 0 && rect["right"] == 0
						&& rect["bottom"] == 0)
					continue;

				o["rects"]["push"](w["namo"]["toPercentageRect"](rect));
			}
		} else if (cfiRects["range"]) {
			var ranges = w["namo"]["getTextRanges"](cfiRects["range"]);
			for (var j = 0, len2 = ranges["length"]; j < len2; j++) {
				var rects = ranges[j]["rects"];
				for (var k = 0, len3 = rects["length"]; k < len3; k++)
					o["rects"]["push"](rects[k]);
			}
		}
		o["visible"] = o["rects"]["length"] > 0;
		if (o["visible"] && annotation["type"] == 2 && startCfi["node"] && startCfi["temporalOffset"] >= 0) {
			var count = 0;
			startCfi["node"]["currentTime"] = 0;
			var interval = w["setInterval"]
					(
							function() {
								if (count++ > 30
										|| startCfi["node"]["currentTime"] != 0) {
									startCfi["node"]["currentTime"] = startCfi["temporalOffset"];
									w["clearInterval"](interval);
								}
							}, 100);
			startCfi["node"]["play"]();
		}
	}
	return result;
};

w["namo"]["getTextRanges"] = function(range) {

    console.log(" :: getTextRanges :: ");

	var ranges = [];
	if (!range || range["collapsed"])
		return ranges;

	var startNode = range["startContainer"];
	if (startNode["nodeType"] == w["Node"]["ELEMENT_NODE"]
			&& startNode["hasChildNodes"]())
		startNode = startNode["childNodes"][range["startOffset"]];
	var endNode = range["endContainer"];
	if (endNode["nodeType"] == w["Node"]["ELEMENT_NODE"]
			&& endNode["hasChildNodes"]())
		endNode = endNode["childNodes"][range["endOffset"]];

	var node = startNode;
	var upwards = false;
	var ruby = null;
	var rubyRect = null, rubyParentRect = null;
	while (node) {
		if (node["nodeType"] == w["Node"]["TEXT_NODE"]
				&& node["nodeValue"]["replace"]
						(/(^\s*)|(\s*$)|(&nbsp;)*/gi, "") != "") {
			var currentRange = w["document"]["createRange"]();
			currentRange["selectNode"](node);

			if (node == range["startContainer"])
				currentRange["setStart"](node, range["startOffset"]);
			if (node == range["endContainer"])
				currentRange["setEnd"](node, range["endOffset"]);

			// 20160503 over lollipop version webview issue (ver 50.0.2661.86)
			var cLeft = 0;
			var cRight = w["innerWidth"];
			var cTop = 0;
			var cBottom = w["innerHeight"];

			var cXOffset = w["pageXOffset"];
			var cYOffset = w["pageYOffset"];

			if (currentRange["compareBoundaryPoints"](
					w["Range"]["START_TO_START"], range) >= 0
					&& currentRange["compareBoundaryPoints"](
							w["Range"]["END_TO_END"], range) <= 0) {

				var rects = [];
				var clientRects = currentRange["getClientRects"]();
				for (var i = 0, len = clientRects["length"]; i < len; i++) {
					var clientRect = clientRects[i];
					var rect = {
						"left" : 0,
						"top" : 0,
						"right" : 0,
						"bottom" : 0
					};

					if (ruby
							&& (rubyRect["left"] < cLeft
									|| rubyRect["right"] > cRight
									|| rubyRect["top"] < cTop || rubyRect["bottom"] > cBottom))
						continue;

					if (ruby) {
						rect["left"] = rubyParentRect["left"]
								+ rubyParentRect["right"] - clientRect["right"];
						rect["right"] = rubyParentRect["left"]
								+ rubyParentRect["right"] - clientRect["left"];

						if (clientRect["top"] > cBottom
								|| clientRect["bottom"] < cTop) {
							if (rubyParentRect["right"] > cRight) {
								rect["left"] -= w["innerWidth"];
								rect["right"] -= w["innerWidth"];
							} else if (rubyParentRect["left"] < cLeft) {
								rect["left"] += w["innerWidth"];
								rect["right"] += w["innerWidth"];
							}
						}
					} else {
						rect["left"] = clientRect["left"];
						rect["right"] = clientRect["right"];
					}

					rect["top"] = clientRect["top"];
					rect["bottom"] = clientRect["bottom"];

					if (ruby)
						if (clientRect["top"] > cBottom) {
							rect["top"] -= w["innerHeight"];
							rect["bottom"] -= w["innerHeight"];
						} else if (clientRect["bottom"] < cTop) {
							rect["top"] += w["innerHeight"];
							rect["bottom"] += w["innerHeight"];
						}

					if (rect["left"] < cLeft || rect["right"] > cRight
							|| rect["top"] < cTop || rect["bottom"] > cBottom)
						continue;

					rects["push"](w["namo"]["toPercentageRect"](rect));
				}

				ranges["push"]({
					"range" : currentRange,
					"rects" : rects
				});
			}
		}

		if (node == endNode)
			break;

		if (node["nodeType"] == w["Node"]["ELEMENT_NODE"]) {
			if (!upwards && node["hasChildNodes"]()) {
				if (w["namo"]["sdkInt"] < 19
						&& w["namo"]["viewDirection"] == w["namo"]["DIRECTION_VERTICAL"]
						&& node["nodeName"]["toLowerCase"]() == "ruby") {
					ruby = node;
					rubyRect = ruby["getBoundingClientRect"]();

					var rubyParent = ruby["parentElement"];
					while (rubyParent) {
						if (!/inline/i["test"](w["getComputedStyle"]
								(rubyParent)["display"]))
							break;

						rubyParent = rubyParent["parentElement"];
					}

					if (rubyParent) {
						rubyParentRect = rubyParent["getBoundingClientRect"]();
					} else {
						ruby = null;
						rubyRect = null;
					}
				}
				upwards = false;
				node = node["firstChild"];
				continue;
			} else if (node == ruby) {
				ruby = null;
				rubyRect = null;
				rubyParentRect = null;
			}
		}
		upwards = false;

		if (node["nextSibling"]) {
			node = node["nextSibling"];
			continue;
		}

		if (node["parentNode"]) {
			upwards = true;
			node = node["parentNode"];
			continue;
		} else {
			break;
		}
	}

	return ranges;
};
w["namo"]["toPercentageX"] = function(x) {

    console.log(" :: toPercentageX :: x = " + x + ", innerWidth = " + w["innerWidth"] + ", return = " + x / w["innerWidth"]);

	return x / w["innerWidth"];
};
w["namo"]["toPercentageY"] = function(y) {

    console.log(" :: toPercentageY :: y = " + y + ", innerHeight = " + w["innerHeight"] + ", return = " + y / w["innerHeight"]);

	return y / w["innerHeight"];
};
w["namo"]["intersectRect"] = function(rect) {

    console.log(" :: intersectRect :: ");

	var sortFunction = function(a, b) {
		return a - b;
	};

	// #15808 20160524 over lollipop version webview issue (ver 50.0.2661.86)
	var cLeft = rect["left"];
	var cRight = rect["right"];
	var cTop = rect["top"];
	var cBottom = rect["bottom"];
	if (w["namo"]["sdkInt"] > 19 && w["namo"]["isReflowable"]) {
		cLeft = rect["left"] % w["innerWidth"];
		cRight = rect["right"] % w["innerWidth"];
		cTop = rect["top"] % w["innerHeight"];
		cBottom = rect["bottom"] % w["innerHeight"];
	}

	//var x = [ rect["left"], rect["right"], 0, window["innerWidth"] ];
	//var y = [ rect["top"], rect["bottom"], 0, window["innerHeight"] ];
	var x = [ cLeft, cRight, 0, w["innerWidth"] ];
	var y = [ cTop, cBottom, 0, w["innerHeight"] ];

	x["sort"](sortFunction);
	y["sort"](sortFunction);

	return {
		"left" : x[1],
		"top" : y[1],
		"right" : x[2],
		"bottom" : y[2]
	};
};
w["namo"]["toPercentageRect"] = function(rect) {

    console.log(" :: toPercentageRect :: ");

	return {
		"left" : w["namo"]["toPercentageX"](rect["left"]),
		"top" : w["namo"]["toPercentageY"](rect["top"]),
		"right" : w["namo"]["toPercentageX"](rect["right"]),
		"bottom" : w["namo"]["toPercentageY"](rect["bottom"])
	};
};
w["namo"]["toX"] = function(percentageX) {
    return w["Math"]["round"](percentageX * w["innerWidth"]);
};

w["namo"]["toY"] = function(percentageY) {
	return w["Math"]["round"](percentageY * w["innerHeight"]);
};

w["namo"]["getElementsByTagNameNS"] = function(tagName, namespace, target) {
	if (!target)
		target = w["document"];

	var namespaceUri = "";
	if (namespace == "epub")
		namespaceUri = "http://www.idpf.org/2007/ops";

	if (w["namo"]["mimeType"] == "application/xhtml+xml")
		return target["getElementsByTagNameNS"](namespaceUri, tagName);
	else
		return target["getElementsByTagName"](namespace + ":" + tagName);
};

/* epub cfi */
w["CFI"] = {};
w["CFI"]["generate"] = function(container, offset) {

    console.log(" :: CFI : generate :: ");

	var cfi = "";
	if (container["nodeType"] == w["Node"]["TEXT_NODE"]) {
		cfi = "/" + w["CFI"]["getIndexOfNode"](container) + ":" + offset;
		container = container["parentNode"];
	}

	while (container && container != w["document"]["documentElement"]) {
		var idTag = ""
		if (container["hasAttribute"]("id"))
			idTag = "[" + container["getAttribute"]("id") + "]";
		cfi = "/" + w["CFI"]["getIndexOfNode"](container) + idTag + cfi;
		container = container["parentNode"];
	}

	return cfi;
};
w["CFI"]["parse"] = function(cfi) {

    console.log(" :: CFI : parse :: ");

	var result = {
		"node" : null,
		"characterOffset" : -1,
		"temporalOffset" : -1,
		"spatialOffsetX" : -1,
		"spatialOffsetY" : -1
	};

	if (!cfi)
		return result;

	result["node"] = w["document"]["documentElement"];

	var steps = cfi["split"]("/");
	for (var i = 1, len = steps["length"]; i < len; i++) {
		if (!result["node"])
			break;

		var step = steps[i];
		var assertion = null;
		if (step["indexOf"]("[") >= 0 && step["indexOf"]("]") >= 0) {
			assertion = step["substring"](step["indexOf"]("[") + 1, step["lastIndexOf"]("]"));
			step = step["replace"](/\^(\[|\])/, "")["replace"](/\[[^\]]*\]/, "");
		}

		var index = -1;
		step = step["replace"](/^(\d+)/, function(str, s1) {
			index = w["parseInt"](s1);

			return "";
		});

		if (index == -1)
			break;

		result["node"] = w["CFI"]["getNodeFromIndex"](result["node"], index, assertion);

		if (i == len - 1 && result["node"]) {
			step = step["replace"](/^:(\d+)/, function(str, s1) {
				if (result["node"]["nodeType"] == w["Node"]["TEXT_NODE"])
					result["characterOffset"] = w["parseInt"](s1);

				return "";
			});

			step = step["replace"](/~(\d+\.?\d*)/, function(str, s1) {
				if (w["namo"]["TAG"]["MEDIA"]["indexOf"](result["node"]["nodeName"]["toLowerCase"]()) >= 0)
					result["temporalOffset"] = w["parseFloat"](s1);
				return "";
			});

			step = step["replace"](/^@(\d+\.?\d*):(\d+\.?\d*)/, function(str, s1, s2) {
				if (result["node"]["nodeType"] == w["Node"]["ELEMENT_NODE"]) {
					result["spatialOffsetX"] = w["parseFloat"](s1);
					result["spatialOffsetY"] = w["parseFloat"](s2);
				}
				return "";
			});
		}
	}

	return result;
};
w["CFI"]["getRects"] = function(startCfi, endCfi) {

    console.log(" :: CFI : getRects :: ");

	var result = {
		"range" : null,
		"rects" : []
	};

	if (!startCfi["node"])
		return result;

	var getPos = function(cfi) {
		var r = cfi["node"]["getBoundingClientRect"]();
		return {
			"x" : r["left"] + r["width"] * cfi["spatialOffsetX"] / 100,
			"y" : r["top"] + r["height"] * cfi["spatialOffsetY"] / 100
		};
	};

	if (startCfi["spatialOffsetX"] >= 0 && startCfi["spatialOffsetY"] >= 0) {
		var startPos = getPos(startCfi);
		var r = {
			"left" : startPos["x"],
			"top" : startPos["y"],
			"right" : startPos["x"],
			"bottom" : startPos["y"]
		};

		if (endCfi && endCfi["node"] && startCfi["node"] == endCfi["node"]
				&& endCfi["spatialOffsetX"] && endCfi["spatialOffsetY"]) {
			var endPos = getPos(endCfi);
			r["right"] = endPos["x"];
			r["bottom"] = endPos["y"];
		}

		result["rects"]["push"](r);

		return result;
	}

	var range = w["document"]["createRange"]();
	if (startCfi["characterOffset"] >= 0) {
		range["setStart"](startCfi["node"], startCfi["characterOffset"]);
		range["setEnd"](startCfi["node"], startCfi["characterOffset"]);
	} else {
		range["selectNode"](startCfi["node"]);
	}

	if (endCfi && endCfi["node"]) {
		if (endCfi["characterOffset"] >= 0) {
			range["setEnd"](endCfi["node"], endCfi["characterOffset"]);
		} else {
			var endRange = w["document"]["createRange"]();
			endRange["selectNode"](endCfi["node"]);
			range["setEnd"](endRange["endContainer"], endRange["endOffset"]);
		}
	}

	if (range["getClientRects"]()["length"] == 0)
		range["expand"]("word");

	result["range"] = range;
	result["rects"] = range["getClientRects"]();

	return result;
};
w["CFI"]["getIndexOfNode"] = function(node) {

    console.log(" :: CFI : getIndexOfNode :: ");

	var cur = node["parentNode"]["firstChild"];
	var index = 0;
	if (cur) {
		if (cur["nodeType"] == w["Node"]["TEXT_NODE"])
			index = 1;
		else if (cur["nodeType"] == w["Node"]["ELEMENT_NODE"])
			index = 2;

		while (cur && cur != node && cur["nextSibling"]) {
			if (cur["nextSibling"]["nodeType"] == cur["nodeType"])
				index += 2;
			else
				index += 1;
			cur = cur["nextSibling"];
		}
	}

	return index;
};
w["CFI"]["getNodeFromIndex"] = function(parent, index, assertion) {
	if (!parent || parent["nodeType"] == w["Node"]["TEXT_NODE"])
		return null;

	var cur = parent["firstChild"];

	if(cur == null)
	    return null;

	var curIndex = 0;
	if (cur["nodeType"] == w["Node"]["TEXT_NODE"])
		curIndex = 1;
	else if (cur["nodeType"] == w["Node"]["ELEMENT_NODE"])
		curIndex = 2;

	while (curIndex <= index && cur["nextSibling"]) {
		if (index == curIndex)
			break;

		if (cur["nextSibling"]["nodeType"] == cur["nodeType"])
			curIndex += 2;
		else
			curIndex += 1;

		cur = cur["nextSibling"];
	}

	if (assertion && index % 2 == 0
			&& (!cur || cur["getAttribute"]("id") != assertion))
		cur = w["document"]["getElementById"](assertion);

	return cur;
};

w["namo"]["startTTS"] = function(params) {
	var result = {
		"callbackName" : "onTTSChanged",
		"selectedText" : ""
	};
	// w["TTS"]["column_size"] = w["namo"]["columnWidth"] +
	// w["namo"]["columnGap"];
	var startPage = params["startPage"];
	var width = w["namo"]["columnWidth"];
	if (w["namo"]["isTwoPageMode"] && (w["namo"]["viewDirection"] == w["namo"]["DIRECTION_LTR"]))
		width = width * 2 + w["namo"]["columnGap"];
	w["TTS"]["column_size"] = width;
	var selectedText = w["TTS"]["play"](startPage, true);
	result["selectedText"] = selectedText;
	return result;
};
w["namo"]["nextTTS"] = function() {
	var result = {
		"callbackName" : "onTTSChanged",
		"selectedText" : ""
	};
	var selectedText = w["TTS"]["getReturnTextBackup"]();
	result["selectedText"] = selectedText;
	return result;
}
w["namo"]["stopTTS"] = function() {
	w["TTS"]["stop"]();
	w["TTS"]["stopClear"]();
}
w["namo"]["pauseTTS"] = function() {
	w["TTS"]["stop"]();
}
w["namo"]["setTTSData"] = function(params) {
	w["TTS"]["setData"](params);
}
/* epub TTS */
w["TTS"] = {};
/* variables */
w["TTS"]["tags"] = "";
w["TTS"]["start"] = false;
w["TTS"]["first_sentence"] = true;
w["TTS"]["sentence"] = 0;
w["TTS"]["stopped"] = false;
w["TTS"]["i"] = 0;
w["TTS"]["start_point"] = 0;
w["TTS"]["column_size"] = 0;
w["TTS"]["start_idx"] = 0;
w["TTS"]["tmp"] = {
	"use" : false,
	"xx" : 0,
	"now" : 0,
	"total" : 0,
	"break_sentence" : 0,
	"break_xx" : 0,
	"break_xx_end" : 0
};
w["TTS"]["tags_start"] = 0;
w["TTS"]["rangeOffset"] = "";
w["TTS"]["returnTextBackup"] = "";
w["TTS"]["setDatas"] = false;
w["TTS"]["stop"] = function() {
	w["TTS"]["i"] = 0;
	w["TTS"]["first_sentence"] = true;
	w["TTS"]["stopped"] = true;
};
w["TTS"]["stopClear"] = function() {
	w["TTS"]["start"] = false;
	w["TTS"]["first_sentence"] = true;
	w["TTS"]["sentence"] = 0;
	w["TTS"]["highlightRangeClear"]();
	w["TTS"]["selClear"]();
};
w["TTS"]["clear"] = function() {
	var _id = w["document"]["getElementsByTagName"]("body");
	var tags = _id[0]["getElementsByTagName"]("*");
	for (var i = 0; i < tags["length"]; i++) {
		tags[i]["style"]["backgroundColor"] = null;
		tags[i]["style"]["color"] = null;
	}
};
w["TTS"]["selClear"] = function() {
	if (w["getSelection"]) {
		if (w["getSelection"]()["empty"]) {
			w["getSelection"]()["empty"]();
		} else if (w["getSelection"]()["removeAllRanges"]) {
			w["getSelection"]()["removeAllRanges"]();
		}
	} else if (w["document"]["selection"]) {
		w["document"]["selection"]["empty"]();
	}
};
w["TTS"]["marking"] = function(i) {
	var _id = w["document"]["getElementsByTagName"]("body");
	var tags = _id[0]["getElementsByTagName"]("*");
	return tags[i]["innerText"];
};
w["TTS"]["getStartStatus"] = function() {
	return w["TTS"]["start"] ? "true" : "false";
};
w["TTS"]["setData"] = function(data) {
	w["TTS"]["column_size"] = w["namo"]["columnWidth"];
	// w["TTS"]["column_size"] = data["column_size"];
	w["TTS"]["xStart"] = {
		"sentence" : data["sentence"],
		"sentenceMode" : data["sentenceMode"]
	};
	w["TTS"]["sentence"] = data["sentence"];
	w["TTS"]["tmp"]["xx"] = w["parseInt"](data["tt_xx"]);
	w["TTS"]["tmp"]["use"] = data["tt_use"];
	w["TTS"]["tmp"]["now"] = w["parseInt"](data["tt_now"]);
	w["TTS"]["tmp"]["total"] = w["parseInt"](data["tt_total"]);
	if (data["tt_break_xx_end"] != 0) {
		w["TTS"]["tmp"]["use"] = true;
	}
	if (w["TTS"]["tmp"]["xx"] != 0 || data["sentence"] != 0) {
		w["TTS"]["start"] = true;
	} else {
		w["TTS"]["start"] = false;
	}
};
w["TTS"]["break_marking"] = function(sentence, idx) {
	var _id = w["document"]["getElementsByTagName"]("body");
	var tags = _id[0]["getElementsByTagName"]("*");
	var tags_list = [];
	var scrollLeft = w["document"]["body"]["scrollLeft"];
	w["TTS"]["selClear"]();
	w["TTS"]["highlightRangeClear"]();
	tags = w["TTS"]["setTags"](tags);
	var tmp = w["TTS"]["checkSpecialChars"](tags);
	if ((tmp["length"] - 1) < idx) {
		return "_BREAK_SENTENCE_ERROR_";
	}
	var start_point = idx != 0 ? 1 : 0;
	for (var i = 0; i < idx; i++) {
		start_point += tmp[i]["length"];
	}
	w["TTS"]["setSelectionRange"](tags[sentence], start_point, start_point
			+ tmp[idx]["length"]);
	w["TTS"]["highlightRange"]();
	w["TTS"]["selClear"]();
	return "_BREAK_SENTECNE_MARKING_";
};
w["TTS"]["getReturnTextBackup"] = function() {
	return w["TTS"]["returnTextBackup"] && w["TTS"]["returnTextBackup"] != "" ? w["TTS"]["returnTextBackup"]["trim"]
			()
			: "";
};
w["TTS"]["play"] = function(mode, returnTextEnable) {
	var _id = w["document"]["getElementsByTagName"]("body");
	var tags = _id[0]["getElementsByTagName"]("*");
	var scrollLeft = w["document"]["body"]["scrollLeft"];
	var pageMoved = false;
	var tmp = "";
	var tmpBackup = "";
	var sentenceMode = "";
	var returnText = "";
	// 시작 지점 셋팅
	if (w["TTS"]["start"] != true) {
		w["TTS"]["selClear"]();
		w["TTS"]["highlightRangeClear"]();
		w["TTS"]["tags"] = tags = w["TTS"]["setTags"](tags);
		tmp = w["TTS"]["setStartPoint"](tags);
	} else {
		w["TTS"]["selClear"]();
		w["TTS"]["highlightRangeClear"]();
		w["TTS"]["tags"] = tags = w["TTS"]["setTags"](tags);
	}
	// 셀렉션 및 리턴 텍스트 셋팅
	w["TTS"]["highlightRangeClear"]();
	if (tags[w["TTS"]["sentence"]]) {
		tmp = w["TTS"]["checkSpecialChars"](tags);
		w["TTS"]["tmp"]["total"] = tmp != null ? tmp["length"] : 0;
		if (tmp && tmp != null && tmp["length"] > 1) {
			sentenceMode = "group";
			if (w["TTS"]["tmp"]["use"] != true) {
				w["TTS"]["tmp"]["use"] = true;
				w["TTS"]["tmp"]["now"] = 0;
			}
			var start_point = w["TTS"]["tmp"]["now"] != 0 ? 1 : 0;
			for (var i = 0; i < w["TTS"]["tmp"]["now"]; i++) {
				start_point += tmp[i]["length"];
			}
			w["TTS"]["setSelectionRange"](tags[w["TTS"]["sentence"]],
					start_point,
					(start_point + tmp[w["TTS"]["tmp"]["now"]]["length"]));
			tmpBackup = {
				"sentence" : w["TTS"]["sentence"],
				"xx" : start_point,
				"xx_end" : (start_point + tmp[w["TTS"]["tmp"]["now"]]["length"]),
				"now" : w["TTS"]["tmp"]["now"],
				"total" : w["TTS"]["tmp"]["total"]
			};
			returnText = tmp[w["TTS"]["tmp"]["now"]];
			if (w["TTS"]["tmp"]["now"] == (w["TTS"]["tmp"]["total"] - 1)) {
				w["TTS"]["tmp"]["xx"] = 0;
				w["TTS"]["tmp"]["use"] = false;
				w["TTS"]["tmp"]["now"] = 0;
				w["TTS"]["tmp"]["total"] = 0;
				w["TTS"]["sentence"]++;
			} else {
				w["TTS"]["tmp"]["xx"] += tmp[w["TTS"]["tmp"]["now"]]["length"];
				w["TTS"]["tmp"]["now"]++;
			}
		} else {
			sentenceMode = "single";
			w["TTS"]["setSelectionRange"](tags[w["TTS"]["sentence"]], 0,
					tags[w["TTS"]["sentence"]]["textContent"]["length"]);
			w["TTS"]["tmp"]["xx"] = 0;
			w["TTS"]["tmp"]["use"] = false;
			w["TTS"]["tmp"]["now"] = 0;
			w["TTS"]["tmp"]["total"] = 0;
			tmpBackup = {
				"sentence" : w["TTS"]["sentence"],
				"xx" : w["TTS"]["tmp"]["xx"],
				"xx_end" : tags[w["TTS"]["sentence"]]["textContent"]["length"],
				"now" : w["TTS"]["tmp"]["now"],
				"total" : w["TTS"]["tmp"]["total"]
			};
			returnText = tags[w["TTS"]["sentence"]]["textContent"];
			w["TTS"]["sentence"]++;
		}
		var rangeOffset = w["TTS"]["getSelectionOffset"]();
		var xx_start = tmpBackup["xx"];
		var xx_end = tmpBackup["xx_end"];
		var txt_filter = "";
		var string_slice = false;
		var break_point = 0;
		if (w["TTS"]["column_size"] != 0) {
			for (n = xx_start; n < xx_end; n++) {
				w["TTS"]["setSelectionRange"](tags[tmpBackup["sentence"]], n,
						n + 1);
				var charRangeOffset = w["TTS"]["getSelectionOffset"]();
				if (charRangeOffset["left"] >= w["TTS"]["column_size"]) {
					string_slice = true;
					break_point = n;
					break;
				} else
					txt_filter += tags[tmpBackup["sentence"]]["textContent"]["substr"]
							(xx_start, 1);
				xx_start++;
			}
			if (break_point != 0) {
				pageMoved = true;
			}
			w["TTS"]["setSelectionRange"](tags[tmpBackup["sentence"]],
					tmpBackup["xx"], tmpBackup["xx_end"]);
		}
		if (pageMoved != true) {
			if (sentenceMode != "group") {
				// 문장이 하나밖에 없는 태그일 경우.... left값과 scrollLeft값 비교
				if (w["TTS"]["column_size"] != 0 && tags[w["TTS"]["sentence"]]) {
					if (tags[w["TTS"]["sentence"]]["tagName"]
							&& tags[w["TTS"]["sentence"]]["tagName"] != "undefined") {
						var offsetData = null;
						if (tags[w["TTS"]["sentence"]]) {
							w["TTS"]["setSelectionRange"](
									tags[w["TTS"]["sentence"]], 0, 1);
							offsetData = w["TTS"]["getSelectionOffset"]();
							offsetData = offsetData["left"];
							if (w["TTS"]["column_size"] <= offsetData)
								pageMoved = true;
						}
					}
				}
				w["TTS"]["setSelectionRange"](tags[tmpBackup["sentence"]], 0,
						tags[tmpBackup["sentence"]]["textContent"]["length"]);
			} else {
				// 문장이 여러개인 태그일 경우... 다음 문장의 left 값과 비교한다.
				if (tags[w["TTS"]["sentence"]]) {
					if (tmp[w["TTS"]["tmp"]["now"]]) {
						w["TTS"]["setSelectionRange"]
								(
										tags[w["TTS"]["sentence"]],
										w["TTS"]["tmp"]["xx"],
										(w["parseInt"](w["TTS"]["tmp"]["xx"]) + w["parseInt"]
												(tmp[w["TTS"]["tmp"]["now"]]["length"])));
						var nextRangeOffset = w["TTS"]["getSelectionOffset"]();
						if (w["TTS"]["column_size"] != 0
								&& nextRangeOffset["left"] >= w["TTS"]["column_size"])
							pageMoved = true;
					}
				}
				w["TTS"]["setSelectionRange"](tags[tmpBackup["sentence"]],
						tmpBackup["xx"],
						(w["parseInt"](tmpBackup["xx"]) + w["parseInt"]
								(tmp[tmpBackup["now"]]["length"])));
			}
		}
		w["TTS"]["highlightRange"]();
		w["TTS"]["selClear"]();
		if (pageMoved) {
			w["TTS"]["stop"]();
			if (returnTextEnable) {
				w["TTS"]["returnTextBackup"] = returnText;
				return "_STOP_:" + w["TTS"]["sentence"] + ":" + sentenceMode
						+ ":" + w["TTS"]["tmp"]["xx"] + ":"
						+ w["TTS"]["tmp"]["use"] + ":" + w["TTS"]["tmp"]["now"]
						+ ":" + w["TTS"]["tmp"]["total"] + ":"
						+ tmpBackup["sentence"] + ":" + tmpBackup["now"] + ":"
						+ tmpBackup["total"] + ":" + tmpBackup["xx"] + ":"
						+ break_point;
			}
		}
		w["TTS"]["rangeOffset"] = rangeOffset;
		if (returnTextEnable) {
			return returnText["trim"]();
		}
	} else {
		if (returnTextEnable) {
			w["TTS"]["returnTextBackup"] = returnText;
			return "_THE_END_";
		}
	}
};
w["TTS"]["setTags"] = function(tags) {
	var tags_list = [];
	var passTag = [ "br", "img", "hr", "video", "audio", "canvas" ];
	var checkLoop = 0;
	for (;;) {
		for (var i = 0; i < tags["length"]; i++) {
			if (passTag["indexOf"](tags[i]["tagName"]
					&& tags[i]["tagName"]["toLowerCase"]()) == -1) {
				if (tags[i]["parentElement"]["tagName"]["toLowerCase"]() == "body"
						&& checkLoop == 0 || checkLoop > 0) {
					tags_list["push"](tags[i]);
				}
			}
		}
		if (tags_list["length"] < 2) {
			if (tags_list["length"] > 0) {
				tags = tags_list[0]["childNodes"];
				tags_list = [];
			} else {
				tags_list = [];
				break;
			}
		} else
			break;
		checkLoop++;
	}
	/* display none remove */
	var tag_total = tags_list["length"];
	for (var t = 0; t < tag_total; t++) {
		if (w["TTS"]["getElementStyleDisplay"](tags_list[t]) == "none") {
			tags_list["splice"](t, 1);
			t--;
			tag_total--;
		}
	}
	/* ul ol checker */
	var t = 0;
	var listTag = [ "ul", "ol" ];
	for (;;) {
		if (tags_list[t]) {
			if (tags_list[t]["tagName"]
					&& listTag["indexOf"](tags_list[t]["tagName"]
							&& tags_list[t]["tagName"]["toLowerCase"]()) != -1) {
				var dummy = [];
				for (var a = 0; a < t; a++)
					dummy["push"](tags_list[a]);
				for (var l = 0; l < tags_list[t]["childNodes"]["length"]; l++)
					dummy["push"](tags_list[t]["childNodes"][l]);
				for (var b = t + 1; b < tags_list["length"]; b++)
					dummy["push"](tags_list[b]);
				tags_list = dummy;
			}
		} else
			break;
		t++;
	}
	var tag_count = 0;
	var new_tags_list = [];
	var passTagList = [ "b", "i", "u", "del", "strike", "strong", "em", "sub",
			"sup", "span" ];
	for (;;) {
		if (tags_list[tag_count] && tags_list[tag_count] != "undefined") {
			var ch = tags_list[tag_count]["childNodes"];
			if (ch["length"] != "undefined" && ch["length"] > 0) {
				var xTagList = w["TTS"]["getTagChilds"](tags_list[tag_count],
						ch);
				for (var x = 0; x < xTagList["length"]; x++)
					new_tags_list["push"](xTagList[x]);
			} else
				new_tags_list["push"](tags_list[tag_count]);
		} else
			break;
		tag_count++;
	}
	tags_list = new_tags_list;
	var tags_total = tags_list["length"];
	for (var t = 0; t < tags_total; t++) {
		if (tags_list[t]["textContent"]["trim"]() == "") {
			tags_list["splice"](t, 1);
			t--;
			tags_total--;
		}
	}
	return tags_list;
};
w["TTS"]["getElementStyleDisplay"] = function(element) {
	var display = "";
	if (element["currentStyle"]) {
		display = element["currentStyle"]["display"];
	} else {
		try {
			if (w["getComputedStyle"](element, null))
				display = w["getComputedStyle"](element, null)["display"];
		} catch (e) {
		}
	}
	return display;
};
w["TTS"]["getTagChilds"] = function(tags, ch) {
	var passTagList = [ "b", "i", "u", "del", "strike", "strong", "em", "sub",
			"sup", "span" ];
	var returnTagList = [];
	var ele_exists = true;
	for (var c = 0; c < ch["length"]; c++) {
		if (ch[c]["tagName"]
				&& passTagList["indexOf"](ch[c]["tagName"]["toLowerCase"]()) != -1) {
			ele_exists = false;
		}
	}
	if (ele_exists) {
		for (var j = 0; j < ch["length"]; j++) {
			var new_ch = ch[j]["childNodes"];
			if (new_ch["length"] > 0) {
				var xTagList = w["TTS"]["getTagChilds"](ch[j], new_ch);
				for (var x = 0; x < xTagList["length"]; x++) {
					returnTagList["push"](xTagList[x]);
				}
			} else {
				returnTagList["push"](ch[j]);
			}
		}
	} else {
		returnTagList["push"](tags);
	}
	return returnTagList;
};
w["TTS"]["setStartPoint"] = function(tags) {
	var scrollLeft = w["document"]["body"]["scrollLeft"];
	w["TTS"]["start"] = true;
	for (var i = 0; i < tags["length"]; i++) {
		var txtOffsetLeft = 0;
		var tmp = tags[i]["textContent"]["match"]
				(/([^\r\n][^!?\.\r\n]+[\w!?\.]+)/g);
		if (tmp && tmp != null && tmp["length"] > 1) {
			var tt = {
				"xx" : 0,
				"now" : 0
			};
			var breaking = false;
			for (var j = 0; j < tmp["length"]; j++) {
				if (tmp[j]["trim"]() != "") {
					var xt = 0;
					for (var k = 0; k < j; k++) {
						xt += tmp[k]["length"];
					}
					w["TTS"]["setSelectionRange"](tags[i], xt, xt
							+ tmp[j]["length"]);
					var offset = w["TTS"]["getSelectionOffset"]();
					if (offset["left"] == null || offset["left"] < 0) {
						continue;
					} else {
						w["TTS"]["sentence"] = i;
						w["TTS"]["tmp"]["xx"] = xt;
						w["TTS"]["tmp"]["now"] = j;
						w["TTS"]["tmp"]["total"] = tmp["length"];
						w["TTS"]["tmp"]["use"] = true;
						breaking = true;
						break;
					}
				} else {
					continue;
				}
			}
			if (!tmp[tt["now"]]) {
				w["TTS"]["sentence"]++;
				w["TTS"]["tmp"]["xx"] = 0;
				w["TTS"]["tmp"]["now"] = 0;
			}
			if (breaking) {
				break;
			}
		} else {
			if (tags[i]["textContent"]["trim"]() != "") {
				w["TTS"]["setSelectionRange"](tags[i], 0,
						tags[i]["textContent"]["length"]);
				var offset = w["TTS"]["getSelectionOffset"]();
				if (offset["left"] == null || offset["left"] < 0) {
					continue;
				} else {
					w["TTS"]["sentence"] = i;
					w["TTS"]["tmp"]["xx"] = 0;
					w["TTS"]["tmp"]["now"] = 0;
					break;
				}
			} else {
				continue;
			}
		}
	}
};
w["TTS"]["checkSpecialChars"] = function(tags) {
	var url_regex = /(http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w-.\/?%&=]*)?)/gi;
	var email_regex = /([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\.[a-zA-Z0-9._-]+)/gi;
	var url_checker = tags[w["TTS"]["sentence"]]["textContent"]["match"]
			(url_regex);
	var email_checker = tags[w["TTS"]["sentence"]]["textContent"]["match"]
			(email_regex);
	var tmp = "";
	// URL이 태그 내 문자열에 포함 && 이메일은 없음
	if (url_checker && email_checker == null) {
		var stext = tags[w["TTS"]["sentence"]]["textContent"];
		var url = stext["match"](url_regex);
		var url_data = [];
		var dummy = [];
		var s_pointer = 0;
		for (var ui = 0; ui < url["length"]; ui++) {
			var start_idx = stext["indexOf"](url[ui]);
			var urlLength = url[ui]["length"];
			url_data["push"]({
				"start_idx" : start_idx,
				"txtLength" : urlLength
			});
		}
		for (var ud = 0; ud < url_data["length"]; ud++) {
			if (url_data[ud]["start_idx"] != s_pointer) {
				var f_str = stext["substr"](s_pointer,
						(url_data[ud]["start_idx"] - s_pointer))["match"]
						(/([^\r\n][^!?\.\r\n]+[\w!?\.]+)/g);
				if (f_str && f_str != null && f_str["length"] > 1) {
					for (var t0 = 0; t0 < f_str["length"]; t0++) {
						if (t0 != (f_str["length"] - 1)) {
							if ((!w["isNaN"](f_str[t0]["substr"](
									(f_str[t0]["length"] - 2), 1)) && f_str[t0]["substr"]
									((f_str[t0]["length"] - 2), 1)["trim"]() != "")
									&& (!w["isNaN"](f_str[t0 + 1]["substr"](0,
											1)) && f_str[t0 + 1]["substr"]
											(0, 1)["trim"]() != "")) {
								if (!f_str[t0 + 2]) {
									f_str[t0] += f_str[t0 + 1];
									f_str["splice"]((t0 + 1), 1);
									t0--;
								} else {
									f_str[t0] += f_str[t0 + 1];
									f_str[t0] += f_str[t0 + 2];
									f_str["splice"]((t0 + 1), 2);
									t0 -= 2;
								}
							}
						}
					}
				}
				if (f_str != null) {
					for (var fi = 0; fi < f_str["length"]; fi++)
						dummy["push"](f_str[fi]);
				}
				s_pointer = url_data[ud]["start_idx"];
			}
			dummy["push"](stext["substr"](url_data[ud]["start_idx"],
					url_data[ud]["txtLength"]));
			s_pointer = url_data[ud]["start_idx"] + url_data[ud]["txtLength"]
		}
		if (s_pointer != stext["length"]) {
			var f_str = stext["substr"](s_pointer, stext["length"])["match"]
					(/([^\r\n][^!?\.\r\n]+[\w!?\.]+)/g);
			if (f_str && f_str != null && f_str["length"] > 1) {
				for (var t0 = 0; t0 < f_str["length"]; t0++) {
					if (t0 != (f_str["length"] - 1)) {
						// 둘다 숫자일 때
						if ((!w["isNaN"](f_str[t0]["substr"](
								(f_str[t0]["length"] - 2), 1)) && f_str[t0]["substr"]
								((f_str[t0]["length"] - 2), 1)["trim"]() != "")
								&& (!w["isNaN"](f_str[t0 + 1]["substr"](0, 1)) && f_str[t0 + 1]["substr"]
										(0, 1)["trim"]() != "")) {
							if (!f_str[t0 + 2]) {
								f_str[t0] += f_str[t0 + 1];
								f_str["splice"]((t0 + 1), 1);
								t0--;
							} else {
								f_str[t0] += f_str[t0 + 1];
								f_str[t0] += f_str[t0 + 2];
								f_str["splice"]((t0 + 1), 2);
								t0 -= 2;
							}
						}
					}
				}
			}
			if (f_str != null) {
				for (var fi = 0; fi < f_str["length"]; fi++)
					dummy["push"](f_str[fi]);
			}
		}
		tmp = dummy;
	} else if (url_checker == null && email_checker) { // 이메일이 태그 내 문자열에 포함
		// && URL은 없음
		var stext = tags[w["TTS"]["sentence"]]["textContent"];
		var email = stext["match"](email_regex);
		var email_data = [];
		var dummy = [];
		var s_pointer = 0;
		for (var ui = 0; ui < email["length"]; ui++) {
			var start_idx = stext["indexOf"](email[ui]);
			var emailLength = email[ui]["length"];
			email_data["push"]({
				"start_idx" : start_idx,
				"txtLength" : emailLength
			});
		}
		for (var ud = 0; ud < email_data["length"]; ud++) {
			if (email_data[ud]["start_idx"] != s_pointer) {
				var f_str = stext["substr"](s_pointer,
						(email_data[ud]["start_idx"] - s_pointer))["match"]
						(/([^\r\n][^!?\.\r\n]+[\w!?\.]+)/g);
				if (f_str && f_str != null && f_str["length"] > 1) {
					for (var t0 = 0; t0 < f_str["length"]; t0++) {
						if (t0 != (f_str["length"] - 1)) {
							// 둘다 숫자일 때
							if ((!w["isNaN"](f_str[t0]["substr"](
									(f_str[t0]["length"] - 2), 1)) && f_str[t0]["substr"]
									((f_str[t0]["length"] - 2), 1)["trim"]() != "")
									&& (!w["isNaN"](f_str[t0 + 1]["substr"](0,
											1)) && f_str[t0 + 1]["substr"]
											(0, 1)["trim"]() != "")) {
								if (!f_str[t0 + 2]) {
									f_str[t0] += f_str[t0 + 1];
									f_str["splice"]((t0 + 1), 1);
									t0--;
								} else {
									f_str[t0] += f_str[t0 + 1];
									f_str[t0] += f_str[t0 + 2];
									f_str["splice"]((t0 + 1), 2);
									t0 -= 2;
								}
							}
						}
					}
				}
				if (f_str != null) {
					for (var fi = 0; fi < f_str["length"]; fi++)
						dummy["push"](f_str[fi]);
				}
				s_pointer = email_data[ud]["start_idx"];
			}
			dummy["push"](stext["substr"](email_data[ud]["start_idx"],
					email_data[ud]["txtLength"]));
			s_pointer = email_data[ud]["start_idx"]
					+ email_data[ud]["txtLength"];
		}
		if (s_pointer != stext["length"]) {
			var f_str = stext["substr"](s_pointer, stext["length"])["match"]
					(/([^\r\n][^!?\.\r\n]+[\w!?\.]+)/g);
			if (f_str && f_str != null && f_str["length"] > 1) {
				for (var t0 = 0; t0 < f_str["length"]; t0++) {
					if (t0 != (f_str["length"] - 1)) {
						// 둘다 숫자일 때
						if ((!w["isNaN"](f_str[t0]["substr"](
								(f_str[t0]["length"] - 2), 1)) && f_str[t0]["substr"]
								((f_str[t0]["length"] - 2), 1)["trim"]() != "")
								&& (!w["isNaN"](f_str[t0 + 1]["substr"](0, 1)) && f_str[t0 + 1]["substr"]
										(0, 1)["trim"]() != "")) {
							if (!f_str[t0 + 2]) {
								f_str[t0] += f_str[t0 + 1];
								f_str["splice"]((t0 + 1), 1);
								t0--;
							} else {
								f_str[t0] += f_str[t0 + 1];
								f_str[t0] += f_str[t0 + 2];
								f_str["splice"]((t0 + 1), 2);
								t0 -= 2;
							}
						}
					}
				}
			}
			if (f_str != null) {
				for (var fi = 0; fi < f_str["length"]; fi++)
					dummy["push"](f_str[fi]);
			}
		}
		tmp = dummy;
	} else if (url_checker && email_checker) {
		var stext = tags[w["TTS"]["sentence"]]["textContent"];
		var url = stext["match"](url_regex);
		var email = stext["match"](email_regex);
		var urlemail_data = [];
		var dummy = [];
		var s_pointer = 0;
		for (var ui = 0; ui < url["length"]; ui++) {
			var start_idx = stext["indexOf"](url[ui]);
			var urlLength = url[ui]["length"];
			urlemail_data["push"]({
				"start_idx" : start_idx,
				"txtLength" : urlLength
			});
		}
		for (var ui = 0; ui < email["length"]; ui++) {
			var start_idx = stext["indexOf"](email[ui]);
			var urlLength = email[ui]["length"];
			urlemail_data["push"]({
				"start_idx" : start_idx,
				"txtLength" : urlLength
			});
		}
		for (var ud = 0; ud < urlemail_data["length"]; ud++) {
			if (urlemail_data[ud]["start_idx"] != s_pointer) {
				var f_str = stext["substr"](s_pointer,
						(urlemail_data[ud]["start_idx"] - s_pointer))["match"]
						(/([^\r\n][^!?\.\r\n]+[\w!?\.]+)/g);
				if (f_str && f_str != null && f_str["length"] > 1) {
					for (var t0 = 0; t0 < f_str["length"]; t0++) {
						if (t0 != (f_str["length"] - 1)) {
							// 둘다 숫자일 때
							if ((!w["isNaN"](f_str[t0]["substr"](
									(f_str[t0]["length"] - 2), 1)) && f_str[t0]["substr"]
									((f_str[t0]["length"] - 2), 1)["trim"]() != "")
									&& (!w["isNaN"](f_str[t0 + 1]["substr"](0,
											1)) && f_str[t0 + 1]["substr"]
											(0, 1)["trim"]() != "")) {
								if (!f_str[t0 + 2]) {
									f_str[t0] += f_str[t0 + 1];
									f_str["splice"]((t0 + 1), 1);
									t0--;
								} else {
									f_str[t0] += f_str[t0 + 1];
									f_str[t0] += f_str[t0 + 2];
									f_str["splice"]((t0 + 1), 2);
									t0 -= 2;
								}
							}
						}
					}
				}
				for (var fi = 0; fi < f_str["length"]; fi++)
					dummy["push"](f_str[fi]);
				s_pointer = urlemail_data[ud]["start_idx"];
			}
			dummy["push"](stext["substr"](urlemail_data[ud]["start_idx"],
					urlemail_data[ud]["txtLength"]));
			s_pointer = urlemail_data[ud]["start_idx"]
					+ urlemail_data[ud]["txtLength"];
		}
		if (s_pointer != stext["length"]) {
			var f_str = stext["substr"](s_pointer, stext["length"])["match"]
					(/([^\r\n][^!?\.\r\n]+[\w!?\.]+)/g);
			if (f_str && f_str != null && f_str["length"] > 1) {
				for (var t0 = 0; t0 < f_str["length"]; t0++) {
					if (t0 != (f_str["length"] - 1)) {
						// 둘다 숫자일 때
						if ((!w["isNaN"](f_str[t0]["substr"](
								(f_str[t0]["length"] - 2), 1)) && f_str[t0]["substr"]
								((f_str[t0]["length"] - 2), 1)["trim"]() != "")
								&& (!w["isNaN"](f_str[t0 + 1]["substr"](0, 1)) && f_str[t0 + 1]["substr"]
										(0, 1)["trim"]() != "")) {
							if (!f_str[t0 + 2]) {
								f_str[t0] += f_str[t0 + 1];
								f_str["splice"]((t0 + 1), 1);
								t0--;
							} else {
								f_str[t0] += f_str[t0 + 1];
								f_str[t0] += f_str[t0 + 2];
								f_str["splice"]((t0 + 1), 2);
								t0 -= 2;
							}
						}
					}
				}
			}
			for (var fi = 0; fi < f_str["length"]; fi++)
				dummy["push"](f_str[fi]);
		}
		tmp = dummy;
	} else {
		tmp = tags[w["TTS"]["sentence"]]["textContent"]["match"]
				(/([^\r\n][^!?\.\r\n]+[\w!?\.]+)/g);
		if (tmp && tmp != null && tmp["length"] > 1) {
			for (var t0; t0 < tmp["length"]; t0++) {
				if (t0 != (tmp["length"] - 1)) {
					// 둘다 숫자일 때
					if ((!w["isNaN"](tmp[t0]["substr"]((tmp[t0]["length"] - 2),
							1)) && tmp[t0]["substr"]
							((tmp[t0]["length"] - 2), 1)["trim"]() != "")
							&& (!w["isNaN"](tmp[t0 + 1]["substr"](0, 1)) && tmp[t0 + 1]["substr"]
									(0, 1)["trim"]() != "")) {
						if (!f_str[t0 + 2]) {
							f_str[t0] += f_str[t0 + 1];
							f_str["splice"]((t0 + 1), 1);
							t0--;
						} else {
							f_str[t0] += f_str[t0 + 1];
							f_str[t0] += f_str[t0 + 2];
							f_str["splice"]((t0 + 1), 2);
							t0 -= 2;
						}
					}
				}
			}
		}
	}
	return tmp;
};
w["TTS"]["getStyle"] = function(oElm, strCssRule) {
	var strValue = "";
	if (w["document"]["defaultView"]
			&& w["document"]["defaultView"]["getComputedStyle"]) {
		strValue = w["document"]["defaultView"]["getComputedStyle"](oElm, "")["getPropertyValue"]
				(strCssRule);
	} else if (oElm["currentStyle"]) {
		strCssRule = strCssRule["replace"](/\-(\w)/g, function(strMatch, p1) {
			return p1["toUpperCase"]();
		});
		strValue = oElm["currentStyle"][strCssRule];
	}
	return strValue;
};
w["TTS"]["getNowTagsOffsetLeft"] = function() {
	var tags = w["TTS"]["tags"];
	var str = "";
	if (tags) {
		var of1 = tags[w["TTS"]["sentence"]] ? tags[w["TTS"]["sentence"]]["offsetLeft"]
				: "unknown";
		var of2 = tags[w["TTS"]["sentence"] - 1] ? tags[w["TTS"]["sentence"] - 1]["offsetLeft"]
				: "unknown";
		str = of1 + " / " + of2;
	} else {
		str = "unknown";
	}
	return str;
};
w["TTS"]["getTextNodesIn"] = function(node) {
	var textNodes = [];
	if (node["nodeType"] == 3) {
		textNodes["push"](node);
	} else {
		var children = node["childNodes"];
		for (var i = 0, len = children["length"]; i < len; ++i) {
			textNodes["push"]["apply"](textNodes, w["TTS"]["getTextNodesIn"]
					(children[i]));
		}
	}
	return textNodes;
};
w["TTS"]["setSelectionRange"] = function(el, start, end) {
	w["TTS"]["highlightRangeClear"]();
	if (el["textContent"]["trim"]() == "")
		return;
	if (w["document"]["createRange"] && w["getSelection"]) {
		var range = w["document"]["createRange"]();
		range["selectNodeContents"](el);
		var textNodes = w["TTS"]["getTextNodesIn"](el);
		var foundStart = false;
		var charCount = 0;
		var endCharCount;
		for (var i = 0, textNode; textNode = textNodes[i++];) {
			endCharCount = charCount + textNode["length"];
			if (!foundStart
					&& start >= charCount
					&& (start < endCharCount || (start == endCharCount && i <= textNodes["length"]))) {
				range["setStart"](textNode, start - charCount);
				foundStart = true;
			}
			if (foundStart && end <= endCharCount) {
				range["setEnd"](textNode, end - charCount);
				break;
			}
			charCount = endCharCount;
		}
		var sel = w["getSelection"]();
		sel["removeAllRanges"]();
		sel["addRange"](range);
	} else if (w["document"]["selection"]
			&& w["document"]["body"]["createTextRange"]) {
		var textRange = w["document"]["body"]["createTextRange"]();
		textRange["moveToElementText"](el);
		textRange["collapse"](true);
		textRange["moveEnd"]("character", end);
		textRange["moveStart"]("character", start);
		textRange["select"]();
	} else {
		var range = w["document"]["createRange"]();
		range["selectNodeContents"](el);
		var textNodes = w["TTS"]["getTextNodesIn"](el);
		var foundStart = false;
		var charCount = 0, endCharCount;
		for (var i = 0, textNode; textNode = textNodes[i++];) {
			endCharCount = charCount + textNode["length"];
			if (!foundStart
					&& start >= charCount
					&& (start < endCharCount || (start == endCharCount && i <= textNodes["length"]))) {
				range["setStart"](textNode, start - charCount);
				foundStart = true;
			}
			if (foundStart && end <= endCharCount) {
				range["setEnd"](textNode, end - charCount);
				break;
			}
			charCount = endCharCount;
		}
		var sel = w["document"]["getSelection"]();
		sel["removeAllRanges"]();
		sel["addRange"](range);
	}
};
w["TTS"]["makeEditableAndHighlight"] = function(colour) {
	sel = w["getSelection"]();
	if (sel["rangeCount"] && sel["getRangeAt"]) {
		range = sel["getRangeAt"](0);
	}
	w["document"]["designMode"] = "on";
	if (range) {
		sel["removeAllRanges"]();
		sel["addRange"](range);
	}
	if (!w["document"]["execCommand"]("HiliteColor", false, colour)) {
		w["document"]["execCommand"]("BackColor", false, colour);
	}
	w["document"]["designMode"] = "off";
};
w["TTS"]["highlightRangeClear"] = function() {
	var dc = w["document"]["getElementById"]("namoViewerTTSHighLight");
	if (dc != null) {
		dc["setAttribute"]("style",
				"background-color: transparent !important; display: inline;");
		dc["setAttribute"]("id", "namoViewerTTSHighLightOld");
	}
	var dc2 = w["document"]["getElementById"]("namoViewerTTSHighLightOld");
	if (dc2 != null) {
		dc2["outerHTML"] = dc2["innerHTML"];
	}
};
w["TTS"]["highlightRange"] = function() {
	var sel = w["getSelection"]() ? w["getSelection"]()
			: w["document"]["getSelection"]();
	var userSelection = sel["getRangeAt"](0);
	var newNode = w["document"]["createElement"]("span");
	newNode["setAttribute"]("style",
			"background-color: #33ccff !important; display: inline;");
	newNode["setAttribute"]("id", "namoViewerTTSHighLight");
	newNode["appendChild"](userSelection["extractContents"]());
	userSelection["insertNode"](newNode);
};
w["TTS"]["highlight"] = function(colour) {
	var range, sel;
	if (w["getSelection"]) {
		try {
			if (!w["document"]["execCommand"]("BackColor", false, colour)) {
				w["TTS"]["makeEditableAndHighlight"](colour);
			}
		} catch (ex) {
			w["TTS"]["makeEditableAndHighlight"](colour);
		}
	} else if (w["document"]["selection"]
			&& w["document"]["selection"]["createRange"]) {
		range = w["document"]["selection"]["createRange"]();
		range["execCommand"]("BackColor", false, colour);
	}
};
w["TTS"]["selectAndHighlightRange"] = function(id, start, end) {
	w["TTS"]["setSelectionRange"](w["document"]["getElementById"](id), start,
			end);
	w["TTS"]["highlight"]("blue");
};
w["TTS"]["simulateClick"] = function(el) {
	var evt = w["document"]["createEvent"]("MouseEvents");
	evt["initMouseEvent"]("click", true, true, window, 1, 0, 0, 0, 0, false,
			false, false, false, 0, null);
	el["dispatchEvent"](evt);
};
w["TTS"]["getSelectionOffset"] = function() {
	var left = null, right = null, top = null, bottom = null, width = null;
	if (w["getSelection"]
			&& w["document"]["createRange"]
			&& typeof w["document"]["createRange"]()["getBoundingClientRect"] != "undefined") {
		var sel = w["getSelection"]();
		if (sel["rangeCount"] > 0) {
			var rect = sel["getRangeAt"](0)["getBoundingClientRect"]();
			if (rect != null) {
				left = rect["left"];
				right = rect["right"];
				top = rect["top"];
				bottom = rect["bottom"];
				width = rect["width"];
			} else {
				left = null;
				right = null;
				top = null;
				bottom = null;
				width = null;
			}
		}
	} else if (w["document"]["selection"]
			&& w["document"]["selection"]["type"] != "Control") {
		var textRange = w["document"]["selection"]["createRange"]();
		if (textRange != null) {
			left = textRange["boundingLeft"];
			right = textRange["boundingRight"];
			top = textRange["boundingTop"];
			bottom = textRange["boundingBottom"];
			width = textRange["boundingWidth"];
		} else {
			left = null;
			right = null;
			top = null;
			bottom = null;
			width = null;
		}
	}
	//if (w["namo"]["sdkInt"] > 19)
	//	left = left - w["namo"]["currentScrollXOffset"];

	return {
		"left" : left,
		"right" : right,
		"top" : top,
		"bottom" : bottom,
		"width" : width
	};
};
(function() {
	for (var i = 0, len = w["namo"]["TAG"]["EVENT"]["length"]; i < len; i++) {
		var target = w["namo"]["TAG"]["EVENT"][i];
		target["namo_addEventListener"] = target["addEventListener"];
		target["addEventListener"] = w["namo"]["addEventListener"];
	}

	w["addEventListener"]("click", w["namo"]["onClick"], true, true);
})();

(function() {
	w["addEventListener"]("DOMContentLoaded", w["namo"]["onDomContentLoaded"], false, true);
})();
