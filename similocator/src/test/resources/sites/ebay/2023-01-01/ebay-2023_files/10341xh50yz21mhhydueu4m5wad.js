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

    function TaaSDynamic() {
        var a = window["cookies-browser"] || raptor.require("ebay.cookies");
        this.readCookie = function (c, b) {
            var d = a.readCookie(c, b);
            return d
        };
        this.writeCookielet = function (d, c, e, b, f) {
            a.writeCookielet(d, c, e, b, f)
        };
        this.readMultiLineCookie = function (c, b) {
            a.readMultiLineCookie(c, b)
        };
        this.writeMultiLineCookie = function (d, b, e, f, c) {
            a.writeMultiLineCookie(d, b, e, f, c)
        };
        this.writeCookie = function (b, c, d) {
            a.writeCookie(b, c, d)
        }
    }

    var TaaSDynamicObj = new TaaSDynamic();

    function attachTrackingListener(b, a) {
        window.jQuery ? $(document).on(b, a) : document.addEventListener(b, a, false)
    }

    window.triggerTracking = function (c, b) {
        var a = document.createEvent("CustomEvent");
        a.initCustomEvent(c, false, false, b);
        document.dispatchEvent(a)
    };
    var $trk = null;
    (function () {
        function b(g) {
            for (var e = 1; e < arguments.length; e++) {
                var f = arguments[e];
                for (var d in f) {
                    if (Object.prototype.hasOwnProperty.call(f, d)) {
                        g[d] = f[d]
                    }
                }
            }
            return g
        }

        function c(e) {
            var d = $trk = this;
            b(d, d.config = e);
            d.image = document.createElement("img");
            d.image.style.display = "none";
            if (d.rover.sync) {
                d.image.attr("src", d.rover.sync)
            }
            d.originalPSI = d.currentPSI = e.psi;
            attachTrackingListener("click", d.onBody);
            attachTrackingListener("mousedown", d.onMouse);
            attachTrackingListener("rover", d.onRover);
            attachTrackingListener("track", d.onTrack);
            attachTrackingListener("pulsar", d.onPulsar);
            attachTrackingListener("TRACKING_UPDATE_PSI", d.trackingUpdatePSI);
            attachTrackingListener("ADD_LAYER_PSI", d.addLayerPSI);
            attachTrackingListener("CLEAR_LAYER_PSI", d.clearLayerPSI)
        }

        b(c.prototype, {
            codes: ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "-", "_", "!"],
            sizes: {p: 4, c: 1, m: 3, l: 3},
            start: {p: 1, c: 5, m: 6, l: 9},
            target: function (d) {
                return (d.tagName) ? d : d.target
            },
            attrib: function (e, d) {
                return e.getAttribute ? e.getAttribute(d) : null
            },
            valid: function (d, e) {
                return (e && (e.indexOf(".ebay.") > 0)) ? d : null
            },
            trackable: function (f) {
                var e = $trk, d = f.tagName;
                if (d.match(/^a$|area/i)) {
                    return e.valid(d, d.href)
                } else {
                    if (d.match(/input/) && d.type.match(/submit/i) && d.form) {
                        return e.valid(d, form.action)
                    } else {
                        return null
                    }
                }
            },
            click: function (e) {
                var d = $trk, g = d.target(e);
                for (var f = null; (g && g.tagName); g = g.parentNode) {
                    f = f || d.trackable(g);
                    if (d.attrib(g, "_sp")) {
                        return d.clickElement(g, f)
                    }
                }
                if (d.pid) {
                    d.track(d.pid)
                }
            },
            clickElement: function (e, f) {
                var d = $trk, g = d.attrib(e, "_sp");
                d.track(g.split(";")[0], f ? d.attrib(f, "_l") : null)
            },
            track: function (o, g) {
                var p = $trk, h = o.split(".");
                if (!o.match(/p\d+/)) {
                    h.push(p.pid)
                }
                if (g) {
                    h.push(g)
                }
                var e = TaaSDynamicObj.readCookie("ds2", "sotr");
                var k = p.chars((e && (e.charAt(0) == "b")) ? e : "bzzzzzzzzzzz");
                for (var m = 0, j = h.length; (m < j); m++) {
                    var i = h[m].match(/([pcml])(\d+)/);
                    if (i == null) {
                        continue
                    }
                    var n = i[1], l = i[2];
                    var r = p.sizes[n], d = p.start[n];
                    var f = p.chars(p.encode(l, r));
                    for (var q = 0; (q < r); q++) {
                        k[d + q] = f[q]
                    }
                }
                var m = 0, j = k.length, e = "";
                while (m < j) {
                    e = e.concat(k[m++])
                }
                TaaSDynamicObj.writeCookielet("ds2", "sotr", e)
            },
            chars: function (f) {
                var e = 0, d = f.length, g = [];
                while (e < d) {
                    g.push(f.charAt(e++))
                }
                return g
            },
            encode: function (e, f) {
                for (var d = $trk.codes, g = ""; (e >= 64); e = e / 64 | 0) {
                    g = d[e % 64] + g
                }
                g = ((e >= 0) ? d[e] : "") + g;
                return g.concat("zzzz").substring(0, f)
            },
            onBody: function (d) {
                $trk.click(d)
            },
            onMouse: function (d) {
                TaaSDynamicObj.writeCookielet("ebay", "psi", $trk.currentPSI)
            },
            onTrack: function (e, d) {
                var f = d.trksid;
                if (f) {
                    $trk.track(f, d.trklid)
                }
            },
            onRover: function (j, g) {
                if (j.detail != undefined && j.detail != null) {
                    g = j.detail
                } else {
                    if (!g || typeof g !== "object") {
                        return
                    }
                }
                var f = $trk;
                var i = g.imp;
                var e = f.rover.uri + (i ? f.rover.imp : f.rover.clk);
                var d;
                var h;
                delete g.imp;
                if (i) {
                    h = a(i, "&", true)
                }
                if (g) {
                    g.ts = Date.now();
                    d = a(g, "&", true)
                }
                if (d && d.length > 0) {
                    e += (e.indexOf("?") < 0 ? "?" : "&") + "trknvp=" + d
                }
                if (h && h.length > 0) {
                    e += (e.indexOf("?") < 0 ? "?" : "&") + "imp=" + h
                }
                f.image.src = e
            },
            onPulsar: function (h, k, e) {
                if (typeof _plsUBTTQ !== "undefined") {
                    if ((typeof k === "undefined" || k == null) && h.detail != null) {
                        if (Array.isArray(h.detail)) {
                            k = h.detail[0];
                            e = h.detail[1]
                        } else {
                            k = h.detail
                        }
                    }
                    if (typeof k !== "undefined" && k != null) {
                        var g = (JSON.parse(JSON.stringify(k)));
                        var f = g.eventProperty;
                        if (typeof e !== "undefined" && e != null) {
                            var i = (JSON.parse(JSON.stringify(e)));
                            if (typeof f !== "undefined" && f != null) {
                                f.actionKind = i.actionKind
                            }
                        }
                        if (g.eventFamily != null && g.eventAction != null) {
                            if (g.eventAction == "CLIENT_PAGE_VIEW" && typeof f.ciid === "undefined" && $trk != null) {
                                var d = $trk;
                                var j = d.currentPSI;
                                if (j.startsWith("A")) {
                                    j = j.substring(1)
                                }
                                f.ciid = j
                            }
                            _plsUBTTQ.push(["customEvts", g.eventFamily, g.eventAction, f])
                        }
                    }
                }
            },
            trackingUpdatePSI: function (e, d) {
                if (e.detail != undefined && e.detail != null) {
                    d = e.detail
                }
                if (d && d.psi) {
                    self.currentPSI = d.psi
                }
                if (d && d.callback && typeof (d.callback) == "function") {
                    d.callback.call(self)
                }
            },
            addLayerPSI: function (e, d) {
                if (e.detail != undefined && e.detail != null) {
                    d = e.detail
                }
                if (d && d.psi) {
                    self.currentPSI = self.currentPSI + d.psi
                }
            },
            clearLayerPSI: function (d) {
                self.currentPSI = self.originalPSI
            }
        });
        window.TaaSTrackingCore = c;

        function a(f, e, i) {
            if (f && typeof f === "object") {
                var d = [];
                Object.keys(f).forEach(function h(j) {
                    d.push(j + "=" + f[j])
                });
                var g = d.join(e || ",");
                return i ? encodeURIComponent(g) : g
            } else {
                return i ? encodeURIComponent(f) : f
            }
        }
    })();
    var idmapRoverURL;

    function TaaSIdMapTracker() {
        this.roverService = function (a) {
            if (window.location.protocol == "https:" || !a || TaaSDynamicObj.readCookie("dp1", "idm")) {
                return
            }
            idmapRoverURL = a;
            window.jQuery ? $(window).on("load", this.sendRequest) : window.addEventListener("load", this.sendRequest, false)
        };
        this.sendRequest = function () {
            if (idmapRoverURL.indexOf("?") > -1) {
                idmapRoverURL += "&"
            } else {
                idmapRoverURL += "?"
            }
            idmapRoverURL += "cb=TaaSIdMapTrackerObj.handleResponse";
            idmapRoverURL = encodeURI(idmapRoverURL) + "&cache=" + Math.random();
            var a = document.createElement("script");
            a.setAttribute("src", idmapRoverURL);
            a.setAttribute("type", "text/javascript");
            document.body.appendChild(a)
        };
        this.setCookieExpiration = function (a) {
            if (typeof a == "number" && a > 0) {
                TaaSDynamicObj.writeCookielet("dp1", "idm", "1", a / 86400, "")
            }
        };
        this.handleResponse = function (c) {
            if (c && c.length > 1) {
                var b = c.length - 1;
                for (var a = 0; a < b; a++) {
                    this.createImage(c[a])
                }
                this.setCookieExpiration(c[b])
            }
        };
        this.createImage = function (a) {
            if (a && a.length > 1) {
                var b = document.createElement("IMG");
                b.width = "1";
                b.height = "1";
                b.src = a;
                b.alt = "";
                document.body.appendChild(b)
            }
        }
    }

    var tracking = tracking || (function () {
        return {}
    })();
    tracking.roversync = (tracking.roversync && QUnit === undefined) || (function () {
        var d = "1.3";
        var a = 2;
        var f;
        var q = Date.now();
        var g;
        var j;
        var v;
        var p = window.location.hostname;
        var u;
        var o;
        var b;
        var e;
        var c;

        function h(y) {
            var z;
            if (window.XMLHttpRequest) {
                z = new window.XMLHttpRequest()
            } else {
                z = new ActiveXObject("Microsoft.XMLHTTP")
            }
            z.withCredentials = false;
            z.open("GET", y, true);
            z.send("")
        }

        function w(y) {
            if (y == null || y === undefined) {
                return false
            }
            return ((y.naturalWidth == 1 && y.naturalHeight == 1) || (y.width == 1 && y.height == 1))
        }

        function x(z) {
            if (z == null) {
                return null
            }
            var A = encodeURIComponent;
            var B = "host=" + z.hostname + "&ifm=" + z.imgFindMethod + "&hasImg =" + z.hasImg + "&hasNons=" + (z.nonsession != null) + "&hasNpii=" + (z.npii != null) + "&hasSess=" + (z.s != null) + "&nonSess=" + A(z.nonsession) + "&npii=" + A(z.npii) + "&ris=" + z.roversyncImgStatus + "&s=" + A(z.s) + "&winLoc=" + A(z.winLoc);
            var y = "/?_trkparms=" + A(B);
            return y
        }

        function t(F) {
            var D = F + "=";
            var A = document.cookie.split(";");
            for (var C = 0; C < A.length; C++) {
                var E = A[C];
                E = E.trim();
                var B = E.indexOf(D);
                if (B == 0) {
                    var z = E.indexOf("=");
                    var y = E.substring(z + 1);
                    return y
                }
            }
            return null
        }

        function n() {
            var y = document.getElementById("roversyncimg");
            if (y != null) {
                return {img: y, method: "id"}
            }
            var B = document.body.lastChild;
            var A = new RegExp("^(http|https)://rover.*.ebay.*/roversync/*", "i");
            for (var z = 0; z <= 40 && B != null; z++) {
                if (B != null) {
                    if (B.src != null && A.test(B.src)) {
                        y = B;
                        return {img: y, method: "sibling"}
                    }
                    B = B.previousSibling
                }
            }
            return {img: y, method: null}
        }

        function r() {
            return {
                err: f,
                hasImg: v,
                handEndTime: g,
                handStartTime: j,
                handElapsedMs: (g - j),
                hostname: p,
                imgFindMethod: u,
                roversyncImgStatus: o,
                sampling: b,
                startTime: q,
                url1: e,
                url2: c
            }
        }

        function m() {
            return d
        }

        function k() {
            try {
                j = Date.now();
                var B = n();
                u = B.method;
                v = (B.img != null);
                if (v) {
                    e = B.img.src
                }
                var A = t("npii");
                o = w(B.img);
                if ((!v && A != null) || (v && o && A != null)) {
                    g = Date.now();
                    return
                }
                var y = {
                    hostname: p,
                    hasImg: v,
                    imgFindMethod: u,
                    nonsession: t("nonsession"),
                    npii: A,
                    roversyncImgStatus: o,
                    s: t("s"),
                    winLoc: window.location
                };
                c = x(y);
                h(c);
                g = Date.now()
            } catch (z) {
                f = z
            }
        }

        function s() {
            if (!l()) {
                b = false;
                return
            }
            b = true;
            if (window.addEventListener) {
                window.addEventListener("load", k, false)
            } else {
                if (window.attachEvent) {
                    window.attachEvent("onload", k)
                }
            }
        }

        function l() {
            var y = 100;
            var z = t("roversyncSampling");
            if (z != null && !isNaN(z)) {
                y = Number(z)
            } else {
                if (tracking._RSRandomSamplingPct != null && !isNaN(tracking._RSRandomSamplingPct)) {
                    y = tracking._RSRandomSamplingPct
                } else {
                    y = a
                }
            }
            return Math.random() < (y / 100)
        }

        function i() {
            console.log("err=" + f + (f != null ? " err.description=" + f.description + " err.message=" + f.message + " err.name=" + f.name + " err.number=" + f.number : "") + " hasImg=" + v + " handEndTime=" + g + " handStartTime=" + j + " handElapsedMs=" + (g - j) + " hostname=" + p + " imgFindMethod=" + u + " roversyncImgStatus=" + o + " sampling=" + b + " startTime=" + q + " url1=" + e + " url2=" + c)
        }

        s();
        return {getVersion: m, logStatus: i}
    })();

}
/*
     FILE ARCHIVED ON 11:55:00 Sep 01, 2023 AND RETRIEVED FROM THE
     INTERNET ARCHIVE ON 17:01:10 Nov 26, 2023.
     JAVASCRIPT APPENDED BY WAYBACK MACHINE, COPYRIGHT INTERNET ARCHIVE.

     ALL OTHER CONTENT MAY ALSO BE PROTECTED BY COPYRIGHT (17 U.S.C.
     SECTION 108(a)(3)).
*/
/*
playback timings (ms):
  captures_list: 902.095
  exclusion.robots: 0.069
  exclusion.robots.policy: 0.06
  cdx.remote: 0.059
  esindex: 0.01
  LoadShardBlock: 260.468 (3)
  PetaboxLoader3.datanode: 140.059 (4)
  load_resource: 262.385
  PetaboxLoader3.resolve: 181.645
*/
