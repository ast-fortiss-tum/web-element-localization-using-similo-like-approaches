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

    /* Global Widget Delivery Platform V2 - GlobalHeaderFrontEnd$29.0.0 */
    var WidgetPlatformV2 = function () {
        "use strict";

        function e(e, n) {
            if (null == e) throw new TypeError("Cannot convert first argument to object");
            for (var t = Object(e), i = 1; i < arguments.length; i++) {
                var r = arguments[i];
                if (null != r) for (var o = Object.keys(Object(r)), a = 0, d = o.length; a < d; a++) {
                    var s = o[a], c = Object.getOwnPropertyDescriptor(r, s);
                    void 0 !== c && c.enumerable && (t[s] = r[s])
                }
            }
            return t
        }

        !function () {
            var t = document.fonts, e = "font-marketsans",
                n = "https://web.archive.org/web/20210101115703/https://ir.ebaystatic.com/cr/v/c1/vendor/fontfaceobserver.js";

            function i() {
                try {
                    localStorage.setItem("ebay-font", e)
                } catch (e) {
                }
            }

            function r() {
                !function () {
                    var e = t && t.load;
                    if (e && /Apple/.test(window.navigator.vendor)) {
                        var n = /AppleWebKit\/([0-9]+)(?:\.([0-9]+))(?:\.([0-9]+))/.exec(window.navigator.userAgent);
                        e = !(n && parseInt(n[1], 10) < 603)
                    }
                    return e
                }() ? function (e, n) {
                    var t = document.createElement("script");
                    t.type = "application/javascript", t.async = !0, t.onload = n, t.src = e, document.documentElement.firstChild.appendChild(t)
                }(n, function () {
                    var e = new FontFaceObserver("Market Sans"),
                        n = new FontFaceObserver("Market Sans", {weight: "bold"});
                    Promise.all([e.load(), n.load()]).then(i)
                }) : (t.load("1em Market Sans"), t.load("bold 1em Market Sans"), t.ready.then(i))
            }

            "fontDisplay" in document.documentElement.style || localStorage && localStorage.getItem("ebay-font") === e || window.addEventListener("load", function () {
                requestAnimationFrame ? requestAnimationFrame(r) : r()
            })
        }(), {
            assign: e, polyfill: function () {
                Object.assign || Object.defineProperty(Object, "assign", {
                    enumerable: !1,
                    configurable: !0,
                    writable: !0,
                    value: e
                })
            }
        }.polyfill();
        var i = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (e) {
            return typeof e
        } : function (e) {
            return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
        }, f = function (e, n) {
            if (!(e instanceof n)) throw new TypeError("Cannot call a class as a function")
        }, n = function () {
            function i(e, n) {
                for (var t = 0; t < n.length; t++) {
                    var i = n[t];
                    i.enumerable = i.enumerable || !1, i.configurable = !0, "value" in i && (i.writable = !0), Object.defineProperty(e, i.key, i)
                }
            }

            return function (e, n, t) {
                return n && i(e.prototype, n), t && i(e, t), e
            }
        }(), s = {DATA_MISSING: "dataMissing", TYPE_MISMATCH: "typeMismatch"}, c = void 0;

        function l(e) {
            return Array.isArray(e) ? "array" : null === e ? "null" : void 0 === e ? "undefined" : i(e)
        }

        function u(e) {
            return "string" == typeof e && (e = e.split(".").join(",").replace(/\[\d\]/g, function (e) {
                return e = "," + (e = e.replace(/[\[\]']+/g, ""))
            }).split(",")), e
        }

        function v(e, n) {
            return n.reduce(function (e, n) {
                return e && void 0 !== e[n] ? e[n] : void 0
            }, e)
        }

        function r(e, n, t) {
            var i = 3 < arguments.length && void 0 !== arguments[3] ? arguments[3] : "warn", r = void 0,
                o = v(e, n = u(n)), a = l(o), d = l(t);
            return "undefined" === d ? (t = "", d = "string") : "object" === d && function (e) {
                if (e) {
                    for (var n in e) if (e.hasOwnProperty(n)) return !1;
                    return !0
                }
            }(t) && (t = {__isEmpty: !0}), "undefined" !== a ? a !== d && (r = s.TYPE_MISMATCH, o = t) : (r = s.DATA_MISSING, o = t), c && i && r && function (e, n, t, i) {
                c[i] && c[i]("event: %s, path: %s, default: %s", e, n, t)
            }(r, n, t, i), o
        }

        var t = {
            has: function (e, n) {
                var t = v(e, n = u(n)), i = l(t);
                return t = !("undefined" === i || "null" === i)
            }, get: function (e, n, t) {
                return r(e, n, t)
            }, need: function (e, n, t, i) {
                return r(e, n, t, i)
            }, setLogger: function (e) {
                c = e
            }
        }, o = {EVENT_TYPES: s};
        t.privates = o;
        !function () {
            if ("undefined" != typeof Element) {
                var e = function () {
                    var e = document.createElement("div"), n = {
                        transition: "transitionend",
                        OTransition: "oTransitionEnd",
                        MozTransition: "transitionend",
                        WebkitTransition: "webkitTransitionEnd"
                    };
                    for (var t in n) if (void 0 !== e.style[t]) return n[t]
                }()
            }
        }();
        if ("undefined" != typeof Element) for (var a = ["matches", "matchesSelector", "webkitMatchesSelector", "mozMatchesSelector", "msMatchesSelector", "oMatchesSelector"], d = 0; d < a.length; d++) {
            var p = a[d];
            if (Element.prototype[p]) {
                p;
                break
            }
        }
        var m = function (e) {
            var n = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : window;
            return "requestAnimationFrame" in n ? n.requestAnimationFrame(e) : n.setTimeout(e, 0)
        }, h = function () {
            function o(e, n) {
                var t = 2 < arguments.length && void 0 !== arguments[2] ? arguments[2] : function () {
                }, i = 3 < arguments.length && void 0 !== arguments[3] ? arguments[3] : document;
                f(this, o), this.doc = i, this.cb = t, this.js = e, this.jsSize = this.js && this.js.length, this.css = n, this.cssSize = this.css && this.css.length;
                var r = (i.body || i.getElementsByTagName("head")[0]).childNodes;
                this.ref = r[r.length - 1]
            }

            return n(o, [{
                key: "init", value: function () {
                    var e = this;
                    this.js && this.css ? this.loadJS(this.js, function () {
                        return e.loadCSS(e.css)
                    }) : this.cb()
                }
            }, {
                key: "loadJS", value: function (e, n) {
                    var t = this;
                    if (e) if (Array.isArray(e)) e.forEach(function (e) {
                        t.loadJS(e, n)
                    }); else {
                        var i = this.doc.createElement("script");
                        i.defer = !0, i.src = e, i.type = "text/javascript";
                        i.addEventListener("load", function e() {
                            i.addEventListener && i.removeEventListener("load", e), t.jsSize--, t.jsSize <= 0 && n()
                        }), this.ref.parentNode.insertBefore(i, this.ref.nextSibling)
                    }
                }
            }, {
                key: "loadCSS", value: function (e) {
                    var n = this;
                    if (e) if (Array.isArray(e)) e.forEach(function (e) {
                        n.loadCSS(e)
                    }); else {
                        var t = this.doc.createElement("link");
                        t.rel = "stylesheet", t.href = e, t.media = "temp";
                        t.addEventListener("load", function e() {
                            t.addEventListener && t.removeEventListener("load", e), t.media = "all", n.cssSize--, n.cssSize <= 0 && n.cb()
                        }), this.ref.parentNode.insertBefore(t, this.ref.nextSibling)
                    }
                }
            }]), o
        }(), g = m, y = "gh_user", w = function (e) {
            var n = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : "", t = [], i = [];
            return Array.prototype.forEach.call(e, function (e) {
                "nb_script" === e.id ? (n && (e.id = n), t.push(e.innerHTML)) : i.push(e.innerHTML)
            }), t.concat(i)
        }, S = function () {
            var e = document.createElement("div");
            return e.id = y, e.style.display = "none", document.body.appendChild(e), e
        }, b = {
            initAndRenderWidgets: function (a) {
                var e = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : {}, n = e.hasEncodedHtml,
                    d = void 0 === n || n, t = e.js, i = e.css, r = e.jsInline, s = void 0 === r ? "" : r;
                new h(t, i, function () {
                    var e = document.getElementById(y) || S(), n = a.reduce(function (e, n) {
                        return e + (d ? decodeURI(n.html) : n.html)
                    }, "");
                    e.innerHTML += n;
                    var t = e.querySelectorAll("script"), i = w(t), r = a.map(function (e) {
                        return e.init
                    }), o = "";
                    s && (o = function (e) {
                        return (e = "null" === e ? "" : e).slice(e.indexOf(">") + 1, e.lastIndexOf("<")).trim()
                    }(d ? decodeURI(s) : s)), function (e) {
                        var n = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : document,
                            t = n.createTextNode(e), i = n.createElement("script");
                        i.appendChild(t), g(function () {
                            return n.body.appendChild(i)
                        })
                    }(r.join(" ; ").concat(" ; ").concat(i).concat(" ; ").concat(o)), "function" == typeof window.markoDynamicInitComponents && window.markoDynamicInitComponents()
                }).init()
            }, initAndRenderOnDemandWidgets: function (e, n, t) {
                var i = "", r = document.createElement("div");
                r.innerHTML += e, r.classList.add("on-demand-widgets-placeholder");
                var o = r.querySelector(".gh-module-with-target"),
                    a = o && o.dataset.targetSelector || "#widgets-placeholder", d = document.querySelector(a);
                d.appendChild(r), d.style.display = "block";
                var s = r.querySelectorAll("script");
                s && 0 < s.length && (i = w(s, "nb_ondemand_script"));
                var c = document.createElement("script");
                c.classList.add("on-demand-scripts"), c.type = "text/javascript", c.appendChild(document.createTextNode(i)), document.body.appendChild(c), "function" == typeof window.markoInitComponents && (window.markoInitComponents(n), document.dispatchEvent(new CustomEvent("gadget-component-added", {detail: t})))
            }
        }, E = {HOLDER_ID: y, createHolder: S};
        b.privates = E;
        var I = t.need, T = b.initAndRenderWidgets, k = b.initAndRenderOnDemandWidgets, j = function (e) {
                "complete" === (1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : document).readyState ? e() : window.addEventListener("load", e)
            }, C = function (e) {
                var n = e.renderedComponents, t = e.html, i = e.content, r = e.js, o = e.css, a = e.jsInline,
                    d = e.onDemand, s = e.parameters,
                    c = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : T;
                1 === d ? k(t, n, s) : (window.widget_platform_renderedComponents = n, c([{
                    html: t || i,
                    init: ""
                }], {hasEncodedHtml: !1, js: r, css: o, jsInline: a}))
            }, A = function (e) {
                var n = window.widget_platform_ondemandUASDoneEventType;
                n && (document.dispatchEvent(new CustomEvent(n, {detail: I(e, "content.response.modules", {})})), delete window.widget_platform_ondemandUASDoneEventType)
            }, _ = function (e) {
                var n = e.renderType, t = e.renderDelay, i = e.widgets,
                    r = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : T,
                    o = 2 < arguments.length && void 0 !== arguments[2] ? arguments[2] : document, a = i && i[0],
                    d = a && a.js, s = a && a.css, c = a && a.jsInline;
                1 === n ? r(i, {js: d, css: s, jsInline: c}) : 2 === n ? j(function () {
                    return r(i, {js: d, css: s, jsInline: c})
                }, o) : function (e, n) {
                    var t = 2 < arguments.length && void 0 !== arguments[2] ? arguments[2] : document;
                    j(function () {
                        window.setTimeout(n, e)
                    }, t)
                }(t, function () {
                    return r(i, {js: d, css: s, jsInline: c})
                }, o)
            }, O = function () {
                function u(e) {
                    var n = e.widgetResponse, t = void 0 === n ? {} : n, i = e.onFallback, r = e.initializer,
                        o = void 0 === r ? _ : r;
                    f(this, u);
                    var a = t.widgets, d = t.queryParam, s = t.triggerFallBack, c = t.renderType, l = t.renderDelay;
                    this.props = {
                        widgets: a || [],
                        queryParam: d || {},
                        triggerFallBack: void 0 === s || s,
                        onFallback: i,
                        initializer: o,
                        renderType: c,
                        renderDelay: l
                    }
                }

                return n(u, [{
                    key: "initializeWidgets", value: function () {
                        var e = this.props, n = e.widgets;
                        (0, e.initializer)({widgets: n, renderType: e.renderType, renderDelay: e.renderDelay})
                    }
                }, {
                    key: "handleFallback", value: function () {
                        var e = this.props, n = e.queryParam;
                        (0, e.onFallback)(n)
                    }
                }, {
                    key: "init", value: function () {
                        this.props.triggerFallBack ? this.handleFallback() : this.initializeWidgets()
                    }
                }]), u
            }(), D = function () {
                var e = !1;
                return "serviceWorker" in navigator && "Notification" in window && (e = "default" === Notification.permission), e
            }, M = t.has, L = t.get, q = "X_EBAY_C_CORRELATION_SESSION", P = "raptor.require",
            F = "trkCorrelationSessionInfo.getTrackingCorrelationSessionInfo", H = {
                readRaptorContext: function () {
                    if (M(window, P)) return L(window, P, function () {
                    })("ebay.context.Context")
                }, extractSessionInfo: function () {
                    if (M(window, F)) return L(window, F, function () {
                    })()
                }, addSessionInfoIntoPage: function (e) {
                    window.trkCorrelationSessionInfo = {}, window.trkCorrelationSessionInfo.getTrackingInfo = function () {
                        return JSON.parse(JSON.stringify(e))
                    }, window.trkCorrelationSessionInfo.getTrackingCorrelationSessionInfo = function () {
                        return e[q]
                    }
                }
            }, R = D, N = function (e) {
                var n = H.extractSessionInfo(), t = {}, i = null;
                if (n) return n;
                try {
                    i = H.readRaptorContext()
                } catch (e) {
                    i = null
                }
                var r = i && i.pid;
                return r || (e = e || window, r = L(e, "GH.C.pageId", 0)), r ? (t[q] = "operationId=" + r, H.addSessionInfoIntoPage(t), t[q]) : null
            }, x = C, z = A, W = function () {
                var e = function (e) {
                        return "undefined" !== i(e.GH) && e.GH && "function" == typeof e.GH.getWidgetDeliveryPlatform && e.GH.getWidgetDeliveryPlatform()
                    }(0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : window), n = e && e.base_path,
                    t = e && e.new_buyer_acquistion;
                return n && t ? n + t : "/gh/useracquisition"
            }, U = function (e, n) {
                var t = function (e) {
                    if (e instanceof ProgressEvent) {
                        var n = e.target || e.currentTarget;
                        if (n instanceof XMLHttpRequest) return n.responseText
                    }
                }(e);
                if (t) try {
                    var i = t && JSON.parse(t), r = i.rawResponse ? z : x;
                    i.parameters = n, r(i)
                } catch (e) {
                    console.debug("Unable to parse Response", e)
                }
            }, B = function () {
                var n = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : {}, e = n.route || W(),
                    t = function () {
                        var e = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : {},
                            n = {correlation: N(), show_optin_banner: R()};
                        return Object.assign(n, e)
                    }(n), i = n.method || "GET", r = JSON.stringify(n.body) || "", o = {shouldRetry: !1, timeout: 1e4};
                if (n.route && (o.hasFullXhrUrl = !0), "HttpClient" in window) return new window.HttpClient(e, i, t, r, o, window).handlers({
                    onload: function (e) {
                        return U(e, n)
                    }, onerror: function (e) {
                        return function (e) {
                            var n = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : {};
                            console.debug("useracquisition request has failed!", e);
                            var t = new CustomEvent("gadget-request-error", {detail: {err: e, parameters: n}});
                            document.dispatchEvent(t), n.rawResponse && z(e)
                        }(e, n)
                    }
                })
            }, G = window.widget_platform, J = function () {
                var e = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : {};
                B(e).initializeAndTrigger()
            };
        document && document.addEventListener && document.addEventListener("widget-platform-ondemand-uas-request", function (e) {
            !function () {
                var e = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : {};
                window && e.widgetPlatformOndemandUASDoneEventType && (window.widget_platform_ondemandUASDoneEventType = e.widgetPlatformOndemandUASDoneEventType), e.onDemand = 1, J(e)
            }(e && e.detail)
        }, !1), new O({widgetResponse: G, onFallback: J}).init();
        return {}
    }();
    /* Delivered by Global Header, ISPROD=true */


}
/*
     FILE ARCHIVED ON 11:57:03 Jan 01, 2021 AND RETRIEVED FROM THE
     INTERNET ARCHIVE ON 17:00:38 Nov 26, 2023.
     JAVASCRIPT APPENDED BY WAYBACK MACHINE, COPYRIGHT INTERNET ARCHIVE.

     ALL OTHER CONTENT MAY ALSO BE PROTECTED BY COPYRIGHT (17 U.S.C.
     SECTION 108(a)(3)).
*/
/*
playback timings (ms):
  exclusion.robots: 0.073
  exclusion.robots.policy: 0.063
  cdx.remote: 0.098
  esindex: 0.009
  LoadShardBlock: 213.558 (6)
  PetaboxLoader3.datanode: 112.251 (8)
  PetaboxLoader3.resolve: 273.573 (3)
  load_resource: 246.484 (2)
*/
