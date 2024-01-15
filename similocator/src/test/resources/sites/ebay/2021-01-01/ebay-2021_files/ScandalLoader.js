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

    (function () {
        function r(e, n, t) {
            function o(i, f) {
                if (!n[i]) {
                    if (!e[i]) {
                        var c = "function" == typeof require && require;
                        if (!f && c) return c(i, !0);
                        if (u) return u(i, !0);
                        var a = new Error("Cannot find module '" + i + "'");
                        throw a.code = "MODULE_NOT_FOUND", a
                    }
                    var p = n[i] = {exports: {}};
                    e[i][0].call(p.exports, function (r) {
                        var n = e[i][1][r];
                        return o(n || r)
                    }, p, p.exports, r, e, n, t)
                }
                return n[i].exports
            }

            for (var u = "function" == typeof require && require, i = 0; i < t.length; i++) o(t[i]);
            return o
        }

        return r
    })()({
        1: [function (require, module, exports) {
            function loadScript(t, r) {
                if (t.urlToLoad) {
                    r = r || window;
                    var e = r.document.createElement("script");
                    e.async = "true", e.type = "text/javascript", e.src = t.urlToLoad, t.hash && (e.setAttribute("integrity", t.hash), e.setAttribute("crossorigin", "anonymous")), r.document.head.appendChild(e)
                }
            }

            function queueWithSideEffectOnFirstItem(t, r) {
                return r[0] ? t.call(this, r[0]) : r.push = function () {
                    return t.call(this, arguments[0]), Array.prototype.push.apply(this, arguments)
                }, r
            }

            function isHTTPS(t) {
                return t = t || window, "https:" === t.document.location.protocol
            }

            function getImplFromScandalAds(t, r) {
                if (r = r || isHTTPS, t) for (var e in t) if (t.hasOwnProperty(e)) {
                    var a = t[e];
                    if (a.providerParameters && a.providerParameters.scandalJS) {
                        var i = r.call() ? "https:" + a.providerParameters.scandalJS.url : "http:" + a.providerParameters.scandalJS.url,
                            o = a.providerParameters.scandalJS.hash;
                        return {urlToLoad: i, hash: o}
                    }
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
                        var d = getImplFromScandalAds(i);
                        d && d.urlToLoad && (e = !0, loadScript(d))
                    }
                }
            }(), window.scandalAds = window.scandalAds ? queueWithSideEffectOnFirstItem(window.loadImpl, window.scandalAds) : queueWithSideEffectOnFirstItem(window.loadImpl, []);
        }, {"./LibFuncs": 1}]
    }, {}, [2])


}
/*
     FILE ARCHIVED ON 11:57:25 Jan 01, 2021 AND RETRIEVED FROM THE
     INTERNET ARCHIVE ON 17:00:27 Nov 26, 2023.
     JAVASCRIPT APPENDED BY WAYBACK MACHINE, COPYRIGHT INTERNET ARCHIVE.

     ALL OTHER CONTENT MAY ALSO BE PROTECTED BY COPYRIGHT (17 U.S.C.
     SECTION 108(a)(3)).
*/
/*
playback timings (ms):
  exclusion.robots: 0.069
  exclusion.robots.policy: 0.057
  cdx.remote: 0.102
  esindex: 0.01
  LoadShardBlock: 437.339 (6)
  PetaboxLoader3.datanode: 171.25 (7)
  PetaboxLoader3.resolve: 133.544 (3)
  load_resource: 90.627
*/
