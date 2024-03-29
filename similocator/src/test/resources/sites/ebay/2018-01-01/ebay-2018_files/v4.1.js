var _____WB$wombat$assign$function_____ = function (name) {
    return (self._wb_wombat && self._wb_wombat.local_init && self._wb_wombat.local_init(name)) || self[name];
};
if (!self.__WB_pmw) {
    self.__WB_pmw = function (obj) {
        this.__WB_source = obj;
        return this;
    }
}
{
    let window = _____WB$wombat$assign$function_____("window");
    let self = _____WB$wombat$assign$function_____("self");
    let document = _____WB$wombat$assign$function_____("document");
    let location = _____WB$wombat$assign$function_____("location");
    let top = _____WB$wombat$assign$function_____("top");
    let parent = _____WB$wombat$assign$function_____("parent");
    let frames = _____WB$wombat$assign$function_____("frames");
    let opener = _____WB$wombat$assign$function_____("opener");

    var DFP = function (e) {
        DFP.TAGS_DELIMITER = "&";
        DFP.TAGS_PARAM_DELIMITER = "|";
        DFP.MOBILE_TAGS = "mobileTags";
        DFP.EQUALS = "=";
        var f = function (a, c) {
            this.tmxSrcURI = a;
            this.tmxSessionId = c
        };
        f.prototype.initTmxHandler = function () {
            var a = this;
            DFP.isNullOrEmpty(a.tmxSrcURI) ? a.tmxSessionId = "" : setTimeout(function () {
                var c = document.createElement("iframe");
                c.style.display = "none";
                c.tabIndex = "-1";
                c.height = "0";
                c.width = "0";
                c.src = a.tmxSrcURI;
                document.body.appendChild(c)
            }, 1)
        };
        var b = function (a) {
            b.HTML5_TAG_KEY = "kgv";
            b.DELIMITER =
                "|";
            this.newTagValue = a;
            this.storageMediaPresent = {};
            this.html5TagaData = {};
            this.HTML5_GET_METHOD_LIST = {
                getLocalStorage: this.getLocalStorage,
                getGlobalStorage: this.getGlobalStorage,
                getSessionStorage: this.getSessionStorage,
                getUserData: this.getUserData
            };
            this.HTML5_SET_METHOD_MAP = {
                getLocalStorage: this.setLocalStorage,
                getGlobalStorage: this.setGlobalStorage,
                getSessionStorage: this.setSessionStorage,
                getUserData: this.setUserData,
                handleDBStorage: this.setTagToDBStorage
            }
        };
        b.prototype.getHTML5Tag = function (a) {
            var c =
                null, d;
            for (d in this.HTML5_GET_METHOD_LIST) if (this.HTML5_GET_METHOD_LIST.hasOwnProperty(d) && (c = this.HTML5_GET_METHOD_LIST[d](a), !DFP.isNullOrEmpty(c))) {
                this.storageMediaPresent[d] = 1;
                break
            }
            return c
        };
        b.prototype.setHTML5Tag = function (a, c) {
            for (var d in this.HTML5_SET_METHOD_MAP) if (this.HTML5_SET_METHOD_MAP.hasOwnProperty(d) && 1 !== this.storageMediaPresent[d]) this.HTML5_SET_METHOD_MAP[d](b.HTML5_TAG_KEY, c)
        };
        b.prototype.getLocalStorage = function (a) {
            try {
                if (localStorage) return localStorage.getItem(a)
            } catch (c) {
            }
        };
        b.prototype.setLocalStorage = function (a, c) {
            try {
                localStorage && void 0 !== c && localStorage.setItem(a, c)
            } catch (d) {
            }
        };
        b.prototype.getSessionStorage = function (a) {
            try {
                if (sessionStorage) return sessionStorage.getItem(a)
            } catch (c) {
            }
        };
        b.prototype.setSessionStorage = function (a, c) {
            try {
                sessionStorage && void 0 !== c && sessionStorage.setItem(a, c)
            } catch (d) {
            }
        };
        b.prototype.getGlobalStorage = function (a) {
            if ("undefined" != typeof globalStorage) try {
                return globalStorage[getHost()][a]
            } catch (c) {
            }
        };
        b.prototype.setGlobalStorage = function (a,
                                                 c) {
            if ("undefined" != typeof globalStorage) try {
                void 0 !== c && (globalStorage[getHost()][a] = c)
            } catch (d) {
            }
        };
        b.prototype.getUserData = function (a) {
            try {
                var c = createUserElement();
                c.style.behavior = "url(#default#userData)";
                c.load(a);
                return c.getAttribute(a)
            } catch (d) {
            }
        };
        b.prototype.setUserData = function (a, c) {
            try {
                var d = createUserElement();
                d.style.behavior = "url(#default#userData)";
                d.setAttribute(a, c);
                d.save(a)
            } catch (b) {
            }
        };
        b.prototype.createUserElement = function () {
            var a;
            document.getElementById("userdataElement") ? a = document.getElementById("userdataElement") :
                (a = document.createElement("div"), a.style.visibility = "hidden", a.style.position = "absolute", a.setAttribute("id", "userdataElement"), document.body.appendChild(a));
            return a
        };
        b.prototype.handleDBStorage = function (a) {
            if (!window.openDatabase) return !1;
            if (a) {
                var c;
                try {
                    window.openDatabase("ebay", "1.0", "ebay database storage", 1048576).transaction(function (d) {
                        d.executeSql("SELECT value FROM kg WHERE key=?", [b.HTML5_TAG_KEY], function (d, b) {
                            b.rows.item(0) && (c = b.rows.item(0).value, a.handleSetTags(c))
                        }, function () {
                            a.handleSetTags(null)
                        })
                    })
                } catch (d) {
                    a.handleSetTags(null)
                }
                return !0
            }
        };
        b.prototype.getHost = function () {
            return window.location.host.replace("www.", "")
        };
        b.prototype.setTagToDBStorage = function (a, c) {
            try {
                window.openDatabase && window.openDatabase("ebay", "1.0", "ebay database storage", 1048576).transaction(function (d) {
                    d.executeSql("CREATE TABLE IF NOT EXISTS kg (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, key TEXT NOT NULL, value TEXT NOT NULL, UNIQUE (key))", [], function () {
                    }, function () {
                    });
                    d.executeSql("INSERT OR REPLACE INTO kg(key, value) VALUES(?, ?)", [a, c], function () {
                    }, function () {
                    })
                })
            } catch (d) {
            }
        };
        b.prototype.handleSetTags = function (a) {
            var c = !1, d = a;
            DFP.isNullOrEmpty(a) ? (c = !0, d = this.newTagValue) : this.storageMediaPresent.handleDBStorage = 1;
            this.setHTML5Tag(b.HTML5_TAG_KEY, d, c)
        };
        b.prototype.process = function () {
            var a;
            a = !0;
            a = this.getHTML5Tag(b.HTML5_TAG_KEY);
            if (DFP.isNullOrEmpty(a)) if (a = this.handleDBStorage(this), !1 === a) a = this.newTagValue; else return;
            this.setHTML5Tag(b.HTML5_TAG_KEY, a)
        };
        b.prototype.getTag = function () {
            var a = this.getHTML5Tag(b.HTML5_TAG_KEY), c = {};
            c.ht5 = encodeURIComponent(a);
            c.ht5new =
                a === this.newTagValue;
            return c
        };
        this.getAggregateResult = function (a) {
            var c = "", d;
            for (d in a) {
                var b;
                b = a[d];
                var e = "", f = void 0;
                for (f in b) e = e + DFP.TAGS_PARAM_DELIMITER + f + "=" + b[f];
                b = e.substring(1, e.length);
                !1 === DFP.isNullOrEmpty(b) && (c = c + DFP.TAGS_DELIMITER + b)
            }
            return c.substring(1, c.length)
        };
        var g = this;
        g.oContextObj = e;
        if (!e || !e.mobileTags || DFP.isNullOrEmpty(JSON.parse(e.mobileTags))) "true" === e.enableTMXTagging && setTimeout(function () {
                g.oDFPTMXHandler = new f(e.tmxDfpUrl, e.tmxSessionId);
                g.oDFPTMXHandler.initTmxHandler()
            },
            1), "true" === e.enableHTML5Tagging && setTimeout(function () {
            g.oHtmlStorageHandlerObj = new b(e.mid);
            g.oHtmlStorageHandlerObj.process()
        }, 1);
        DFP.isNullOrEmpty = function (a) {
            return !a || null === a || void 0 === a || "undefined" === a ? !0 : 0 === a.length
        };
        DFP.isInValidCharSet = function (a) {
            for (var c = 0; c < a.length; c++) {
                var b = a.charCodeAt(c);
                if (!(65 <= b && 91 > b || 97 <= b && 123 > b || 48 <= b && 58 > b)) if (b = a.charAt(c), !("+" == b || "*" == b || "/" == b || "=" == b)) return !0
            }
            return !1
        };
        return this
    };
    DFP.prototype.getFingerPrintDetails = function () {
        var e = {}, f = this.oContextObj;
        f.mobileTags && !DFP.isNullOrEmpty(JSON.parse(f.mobileTags)) ? e.tagInfo = DFP.MOBILE_TAGS + DFP.EQUALS + encodeURIComponent(f.mobileTags) : ("true" === f.enableFlashTagging && (this.oDfpFlash.processFlash(), e.flash = DFP.flashDFPData), "true" === f.enableSLTagging && (e.SilverLight = this.oDFPSL.getSLTag()), "true" === f.enableHTML5Tagging && (e.html5 = this.oHtmlStorageHandlerObj.getTag()), "true" === f.enableTMXTagging && (e.usid = {usid: this.oDFPTMXHandler.tmxSessionId}),
            e.tagInfo = encodeURIComponent(this.getAggregateResult(e)));
        return e
    };
    window.rInterval = function (e, f) {
        var b = Date.now, g = window.requestAnimationFrame || function (a) {
            window.setTimeout(a, 1E3 / 60)
        }, a = b(), c, d = function () {
            b() - a < f || (a += f, e());
            c || g(d)
        };
        g(d);
        return {
            clear: function () {
                c = 1
            }
        }
    };
    var dfpsvcAjaxCall;
    window.addEventListener("load", function () {
        var e = new DFP(globalDfpContext), f = 10;
        dfpsvcAjaxCall = window.rInterval(function () {
            f--;
            var b = e.getFingerPrintDetails(), b = b && b.tagInfo;
            "HttpClient" in window && (globalDfpContext.sessionId && "" !== globalDfpContext.dfpsvcProxy && "" !== globalDfpContext.sessionId && "" !== b) && (dfpsvcAjaxCall.clear(), b = JSON.stringify({
                sessionId: globalDfpContext.sessionId,
                fingerPrintData: b
            }), (new window.HttpClient(globalDfpContext.dfpsvcProxy, "POST", {}, b, {
                shouldRetry: !0,
                timeout: 5E3
            })).handlers({
                onload: function () {
                },
                onerror: function () {
                }
            }).initializeAndTrigger());
            0 === f && dfpsvcAjaxCall.clear()
        }, 500)
    });

}
/*
     FILE ARCHIVED ON 10:13:16 Sep 01, 2018 AND RETRIEVED FROM THE
     INTERNET ARCHIVE ON 16:59:26 Nov 26, 2023.
     JAVASCRIPT APPENDED BY WAYBACK MACHINE, COPYRIGHT INTERNET ARCHIVE.

     ALL OTHER CONTENT MAY ALSO BE PROTECTED BY COPYRIGHT (17 U.S.C.
     SECTION 108(a)(3)).
*/
/*
playback timings (ms):
  captures_list: 139.092
  exclusion.robots: 0.072
  exclusion.robots.policy: 0.06
  cdx.remote: 0.061
  esindex: 0.011
  LoadShardBlock: 112.229 (3)
  PetaboxLoader3.datanode: 95.464 (5)
  PetaboxLoader3.resolve: 256.75 (3)
  load_resource: 338.811 (2)
*/
