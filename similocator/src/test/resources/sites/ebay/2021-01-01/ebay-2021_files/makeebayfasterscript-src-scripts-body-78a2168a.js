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

    !function () {
        function m(e) {
            try {
                return JSON.parse(e)
            } catch (e) {
                return {}
            }
        }

        function p(e, t) {
            if (e || (e = {}), !t) return e;
            for (var n in t) t.hasOwnProperty(n) && (e[n] = t[n]);
            return e
        }

        var t = {};
        t.unloadOptimization = function (e) {
            var t, n, r,
                o = (n = navigator.userAgent, r = n.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [], /trident/i.test(r[1]) ? "IE" : "Chrome" === r[1] && null !== (t = n.match(/\b(OPR|Edge)\/(\d+)/)) ? t.slice(1, 2).replace("OPR", "Opera") : (r = r[2] ? [r[1], r[2]] : [navigator.appName, navigator.appVersion, "-?"], null !== (t = n.match(/version\/(\d+)/i)) && r.splice(1, 1, t[1]), r[0]));
            e.browsers[o] && window.addEventListener("beforeunload", function () {
                for (var e = Array.prototype.slice.call(document.getElementsByTagName("iframe")), t = 0, n = e.length; t < n; t++) e[t].hasAttribute("data-lightweightiframe") || e[t].parentNode.removeChild(e[t])
            })
        }, t.bandwidthDetection = function (e) {
            var n, r, a = window.performance, o = window.localStorage;
            try {
                n = GH.vCJar || window["cookies-browser"] || "function" == typeof require && require("cookies-browser") || "undefined" != typeof raptor && raptor.require("ebay.cookies")
            } catch (e) {
                n = void 0
            }
            if (n && a && o) {
                var c, t = {
                    1: 100,
                    2: 250,
                    3: 500,
                    4: 750,
                    5: 1e3,
                    6: 2500,
                    7: 5e3,
                    8: 7500,
                    9: 1e4,
                    10: 15e3,
                    11: 2e4,
                    12: 5e4,
                    13: 1e5,
                    14: 2e5
                }, i = "__speed__", u = p({}, {
                    url: "https://web.archive.org/web/20210101115701/https://ir.ebaystatic.com/cr/v/c1/thirtysevens.jpg",
                    maxViews: 3,
                    imgSize: 37,
                    expiry: 3e5,
                    timeout: 200,
                    bweBuckets: t,
                    bwoBuckets: t
                }), d = function (e) {
                    o.setItem(i, JSON.stringify(e))
                }, s = function (e, t) {
                    if (null === e || e <= 0) return 0;
                    for (var n = 1; n <= 14; n++) if (e < t[n]) return n;
                    return 15
                }, l = function (e) {
                    var t = n.readCookie("dp1", "pbf");
                    n.writeCookielet("dp1", "pbf", n.setMulti(t, 119, 8, function (e) {
                        for (var t = 0, n = 0, r = 0; 0 < e || r < 8;) n = 1 & e, e >>= 1, t += 1 & n, t <<= 1, r++;
                        return t >>= 1
                    }(e)))
                }, f = function () {
                    var e, t,
                        n = (e = u.url, (t = document.createElement("a")).href = e, t.protocol = "https:" === window.location.protocol ? "https:" : "http:", t.href) + "?q=" + (new Date).getTime(),
                        r = new Image;
                    r.src = n, r.style.display = "hidden", r.onload = function () {
                        !function (e) {
                            var t = a.getEntriesByName(e);
                            if (t && 0 < t.length) {
                                var n = 8 * u.imgSize / (t[0].responseEnd - t[0].responseStart) * 1e3,
                                    r = 8 * u.imgSize / (t[0].responseEnd - t[0].requestStart) * 1e3,
                                    o = s(n, u.bweBuckets), i = s(r, u.bwoBuckets);
                                l((o << 4) + i), c.bwe = n, c.bwo = r, d(c)
                            }
                        }(n)
                    }, c.loadTime = (new Date).getTime(), c.viewCount = 0
                }, w = function (e, t) {
                    var n = function () {
                        var t;
                        try {
                            t = raptor.require("ebay.profiler.Profiler")
                        } catch (e) {
                            t = null
                        }
                        return t
                    }();
                    if (n && n.addParam && "undefined" != typeof oGaugeInfo) return n.addParam("bwe", e), void n.addParam("bwo", t);
                    var r = function () {
                        var t;
                        try {
                            t = require("raptor-pubsub")
                        } catch (e) {
                            t = null
                        }
                        return t
                    }();
                    r && r.channel("site-speed-ebay").emit("metricsData", {bwe: e, bwo: t})
                };
                r = e, window.addEventListener("load", function () {
                    var e, t, n;
                    "object" == typeof r && (p(u, r), !(c = m(o.getItem(i)) || {}).loadTime || (new Date).getTime() - c.loadTime >= u.expiry ? (navigator.connection && "unknown" !== navigator.connection.type && (navigator.connection.downlinkMax, navigator.connection.downlinkMax !== 1 / 0 && navigator.connection.downlinkMax), (e = null) ? (n = s(t = e, u.bweBuckets), l(n = (n << 4) + n), d({
                        bwe: t,
                        bwo: t,
                        loadTime: (new Date).getTime(),
                        viewCount: 0
                    })) : setTimeout(function () {
                        (window.requestIdleCallback || window.requestAnimationFrame || function (e) {
                            e()
                        })(f)
                    }, u.timeout)) : (c.viewCount = void 0 !== c.viewCount ? c.viewCount + 1 : 0, d(c)), c.bwe && c.bwo && w(c.bwe, c.bwo))
                })
            }
        };
        var e, n, r, o, i = (n = p({}, {
            modules: [],
            unloadOptimization: {browsers: {Firefox: !0, Chrome: !0, IE: !(e = "makeebayfaster_config"), Safari: !1}},
            bandwidthDetection: {
                url: "https://web.archive.org/web/20210101115701/https://ir.ebaystatic.com/cr/v/c1/thirtysevens.jpg",
                maxViews: 4,
                imgSize: 37,
                expiry: 3e5,
                timeout: 250
            }
        }), r = document.getElementById(e), (o = r && r.childNodes && r.childNodes.length && r.childNodes[0].nodeValue) && p(n, o = /^[\],:{}\s]*$/.test(o.replace(/\\["\\\/bfnrtu]/g, "@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, "]").replace(/(?:^|:|,)(?:\s*\[)+/g, "")) ? m(o) : {}), n);
        i.modules.forEach(function (e) {
            "bandwidthDetection" !== e && t[e](i[e])
        })
    }();

}
/*
     FILE ARCHIVED ON 11:57:01 Jan 01, 2021 AND RETRIEVED FROM THE
     INTERNET ARCHIVE ON 17:00:26 Nov 26, 2023.
     JAVASCRIPT APPENDED BY WAYBACK MACHINE, COPYRIGHT INTERNET ARCHIVE.

     ALL OTHER CONTENT MAY ALSO BE PROTECTED BY COPYRIGHT (17 U.S.C.
     SECTION 108(a)(3)).
*/
/*
playback timings (ms):
  exclusion.robots: 0.137
  exclusion.robots.policy: 0.121
  cdx.remote: 0.162
  esindex: 0.014
  LoadShardBlock: 68.972 (6)
  PetaboxLoader3.datanode: 113.262 (8)
  load_resource: 213.159 (2)
  PetaboxLoader3.resolve: 97.493 (2)
*/
