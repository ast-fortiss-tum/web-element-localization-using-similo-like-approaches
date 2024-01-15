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

    (function e(t, n, r) {
        function s(o, u) {
            if (!n[o]) {
                if (!t[o]) {
                    var a = typeof require == "function" && require;
                    if (!u && a) return a(o, !0);
                    if (i) return i(o, !0);
                    var f = new Error("Cannot find module '" + o + "'");
                    throw f.code = "MODULE_NOT_FOUND", f
                }
                var l = n[o] = {exports: {}};
                t[o][0].call(l.exports, function (e) {
                    var n = t[o][1][e];
                    return s(n ? n : e)
                }, l, l.exports, e, t, n, r)
            }
            return n[o].exports
        }

        var i = typeof require == "function" && require;
        for (var o = 0; o < r.length; o++) s(r[o]);
        return s
    })({
        1: [function (require, module, exports) {
            function loadScript(t, r) {
                if (t) {
                    r = r || window;
                    var e = r.document.createElement("script");
                    e.async = "true", e.type = "text/javascript", e.src = t, r.document.head.appendChild(e)
                }
            }

            function queueWithSideEffectOnFirstItem(t, r) {
                return r[0] ? t.call(this, r[0]) : r.push = function () {
                    return 0 === this.length && t.call(this, arguments[0]), Array.prototype.push.apply(this, arguments)
                }, r
            }

            function isHTTPS(t) {
                return t = t || window, "https:" === t.document.location.protocol
            }

            function getImplFromScandalAds(t, r) {
                if (r = r || isHTTPS, t) for (var e in t) if (t.hasOwnProperty(e)) {
                    var i = t[e];
                    if (i.providerParameters && i.providerParameters.scandalJS) return r.call() ? "https:" + i.providerParameters.scandalJS.url : "http:" + i.providerParameters.scandalJS.url
                }
                return null
            }

            module.exports = {
                loadScript: loadScript,
                queueWithSideEffectOnFirstItem: queueWithSideEffectOnFirstItem,
                isHTTPS: isHTTPS,
                getImplFromScandalAds: getImplFromScandalAds
            };
        }, {}], 2: [function (require, module, exports) {
            var loadScript = require("./LibFuncs").loadScript,
                queueWithSideEffectOnFirstItem = require("./LibFuncs").queueWithSideEffectOnFirstItem,
                getImplFromScandalAds = require("./LibFuncs").getImplFromScandalAds;
            window.loadImpl = function () {
                var e = !1;
                return function (i) {
                    if (!e) {
                        e = !0;
                        var d = getImplFromScandalAds(i);
                        loadScript(d)
                    }
                }
            }(), window.scandalAds = window.scandalAds ? queueWithSideEffectOnFirstItem(window.loadImpl, window.scandalAds) : queueWithSideEffectOnFirstItem(window.loadImpl, []);
        }, {"./LibFuncs": 1}]
    }, {}, [2])


//# sourceMappingURL=scl/js/ScandalLoader.js.map

}
/*
     FILE ARCHIVED ON 10:13:04 Sep 01, 2018 AND RETRIEVED FROM THE
     INTERNET ARCHIVE ON 16:59:26 Nov 26, 2023.
     JAVASCRIPT APPENDED BY WAYBACK MACHINE, COPYRIGHT INTERNET ARCHIVE.

     ALL OTHER CONTENT MAY ALSO BE PROTECTED BY COPYRIGHT (17 U.S.C.
     SECTION 108(a)(3)).
*/
/*
playback timings (ms):
  exclusion.robots: 0.141
  exclusion.robots.policy: 0.123
  cdx.remote: 0.169
  esindex: 0.016
  LoadShardBlock: 140.472 (6)
  PetaboxLoader3.datanode: 210.193 (8)
  load_resource: 320.271 (2)
  PetaboxLoader3.resolve: 194.316 (2)
*/
