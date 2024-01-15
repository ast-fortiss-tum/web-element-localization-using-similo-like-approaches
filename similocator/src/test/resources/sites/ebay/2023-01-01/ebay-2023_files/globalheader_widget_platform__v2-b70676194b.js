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

    /* Global Gadget Platform V2 - GlobalHeaderFrontEnd$41.0.0 */
    !function (e, n) {
        "object" == typeof exports && "undefined" != typeof module ? module.exports = n() : "function" == typeof define && define.amd ? define("GadgetPlatform", n()) : (e = e || self).GadgetPlatform = n()
    }(this, function () {
        "use strict";

        function i() {
            try {
                localStorage.setItem("ebay-font", n)
            } catch (e) {
            }
        }

        function e() {
            var e, n, t;
            (t = (t = o && o.load) && /Apple/.test(window.navigator.vendor) ? !((n = /AppleWebKit\/([0-9]+)(?:\.([0-9]+))(?:\.([0-9]+))/.exec(window.navigator.userAgent)) && parseInt(n[1], 10) < 603) : t) ? (o.load("1em Market Sans"), o.load("bold 1em Market Sans"), o.ready.then(i)) : (e = r, n = function () {
                var e = new FontFaceObserver("Market Sans"), n = new FontFaceObserver("Market Sans", {weight: "bold"});
                Promise.all([e.load(), n.load()]).then(i)
            }, (t = document.createElement("script")).type = "application/javascript", t.async = !0, t.onload = n, t.src = e, (e = document.getElementsByTagName("script")[0]).parentNode.insertBefore(t, e))
        }

        var o, n, r;

        function t(e, n) {
            if (null == e) throw new TypeError("Cannot convert first argument to object");
            for (var t = Object(e), i = 1; i < arguments.length; i++) {
                var o = arguments[i];
                if (null != o) for (var r = Object.keys(Object(o)), a = 0, d = r.length; a < d; a++) {
                    var s = r[a], c = Object.getOwnPropertyDescriptor(o, s);
                    void 0 !== c && c.enumerable && (t[s] = o[s])
                }
            }
            return t
        }

        o = document.fonts, n = "font-marketsans", r = "https://web.archive.org/web/20230901120849/https://ir.ebaystatic.com/cr/v/c1/vendor/fontfaceobserver.js", "fontDisplay" in document.documentElement.style || localStorage && localStorage.getItem("ebay-font") === n || window.addEventListener("load", function () {
            requestAnimationFrame ? requestAnimationFrame(e) : e()
        }), {
            assign: t, polyfill: function () {
                Object.assign || Object.defineProperty(Object, "assign", {
                    enumerable: !1,
                    configurable: !0,
                    writable: !0,
                    value: t
                })
            }
        }.polyfill();
        var a = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (e) {
            return typeof e
        } : function (e) {
            return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
        }, d = function (e, n) {
            if (!(e instanceof n)) throw new TypeError("Cannot call a class as a function")
        }, s = function (e, n, t) {
            return n && c(e.prototype, n), t && c(e, t), e
        };

        function c(e, n) {
            for (var t = 0; t < n.length; t++) {
                var i = n[t];
                i.enumerable = i.enumerable || !1, i.configurable = !0, "value" in i && (i.writable = !0), Object.defineProperty(e, i.key, i)
            }
        }

        var l = {DATA_MISSING: "dataMissing", TYPE_MISMATCH: "typeMismatch"}, u = void 0;

        function f(e) {
            return Array.isArray(e) ? "array" : null === e ? "null" : void 0 === e ? "undefined" : a(e)
        }

        function p(e) {
            return e = "string" == typeof e ? e.split(".").join(",").replace(/\[\d\]/g, function (e) {
                return e = "," + (e = e.replace(/[\[\]']+/g, ""))
            }).split(",") : e
        }

        function m(e, n) {
            return n.reduce(function (e, n) {
                return e && void 0 !== e[n] ? e[n] : void 0
            }, e)
        }

        function v(e, n, t, i) {
            var o = 3 < arguments.length && void 0 !== i ? i : "warn", r = void 0, a = m(e, n = p(n)), i = f(a),
                e = f(t);
            return "undefined" === e ? (t = "", e = "string") : "object" === e && function (e) {
                if (e) {
                    for (var n in e) if (e.hasOwnProperty(n)) return;
                    return 1
                }
            }(t) && (t = {__isEmpty: !0}), "undefined" !== i ? i !== e && (r = l.TYPE_MISMATCH, a = t) : (r = l.DATA_MISSING, a = t), u && o && r && (r = r, n = n, t = t, u[o = o] && u[o]("event: %s, path: %s, default: %s", r, n, t)), a
        }

        var g = {
            has: function (e, n) {
                return !("undefined" === (n = f(m(e, n = p(n)))) || "null" === n)
            }, get: function (e, n, t) {
                return v(e, n, t)
            }, need: function (e, n, t, i) {
                return v(e, n, t, i)
            }, setLogger: function (e) {
                u = e
            }
        };
        g.privates = {EVENT_TYPES: l};
        !function () {
            if ("undefined" != typeof Element) {
                var e = function () {
                    var e, n = document.createElement("div"), t = {
                        transition: "transitionend",
                        OTransition: "oTransitionEnd",
                        MozTransition: "transitionend",
                        WebkitTransition: "webkitTransitionEnd"
                    };
                    for (e in t) if (void 0 !== n.style[e]) return t[e]
                }()
            }
        }();
        if ("undefined" != typeof Element) for (var h = ["matches", "matchesSelector", "webkitMatchesSelector", "mozMatchesSelector", "msMatchesSelector", "oMatchesSelector"], w = 0; w < h.length; w++) {
            var y = h[w];
            if (Element.prototype[y]) {
                0;
                break
            }
        }
        var b = function (e) {
            var n = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : window;
            return "requestAnimationFrame" in n ? n.requestAnimationFrame(e) : n.setTimeout(e, 0)
        };

        function S(e, n) {
            var t = 2 < arguments.length && void 0 !== arguments[2] ? arguments[2] : function () {
            }, i = 3 < arguments.length && void 0 !== arguments[3] ? arguments[3] : document;
            d(this, S), this.doc = i, this.cb = t, this.js = e, this.jsSize = this.js && this.js.length, this.css = n, this.cssSize = this.css && this.css.length;
            i = (i.body || i.getElementsByTagName("head")[0]).childNodes;
            this.ref = i[i.length - 1]
        }

        function E(e) {
            var n = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : "", t = [], i = [];
            return Array.prototype.forEach.call(e, function (e) {
                "nb_script" === e.id ? (n && (e.id = n), t.push(e.innerHTML)) : i.push(e.innerHTML)
            }), t.concat(i)
        }

        function I() {
            var e = document.createElement("div");
            return e.id = j, e.style.display = "none", document.body.appendChild(e), e
        }

        var T = (s(S, [{
            key: "init", value: function () {
                this.js && this.css ? this.loadCSS(this.css) : this.cb()
            }
        }, {
            key: "loadJS", value: function (e) {
                var n, t = this;
                e && (Array.isArray(e) ? e.forEach(function (e) {
                    t.loadJS(e)
                }) : ((n = this.doc.createElement("script")).defer = !0, n.src = e, n.type = "text/javascript", e = function e() {
                    n.addEventListener && n.removeEventListener("load", e), t.jsSize--, t.jsSize <= 0 && t.cb()
                }, n.addEventListener("load", e), this.ref.parentNode.insertBefore(n, this.ref.nextSibling)))
            }
        }, {
            key: "loadCSS", value: function (e) {
                var n, t = this;
                e && (Array.isArray(e) ? e.forEach(function (e) {
                    t.loadCSS(e)
                }) : ((n = this.doc.createElement("link")).rel = "stylesheet", n.href = e, n.media = "temp", e = function e() {
                    n.addEventListener && n.removeEventListener("load", e), n.media = "all", t.cssSize--, t.cssSize <= 0 && t.loadJS(t.js)
                }, n.addEventListener("load", e), this.ref.parentNode.insertBefore(n, this.ref.nextSibling)))
            }
        }]), S), C = b, j = "gh_user", k = {
            initAndRenderWidgets: function (o) {
                var e = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : {}, n = e.hasEncodedHtml,
                    r = void 0 === n || n, t = e.js, n = e.css, e = e.jsInline, a = void 0 === e ? "" : e;
                new T(t, n, function () {
                    var e = document.getElementById(j) || I(), n = o.reduce(function (e, n) {
                        return e + (r ? decodeURI(n.html) : n.html)
                    }, "");
                    e.innerHTML += n;
                    var t = e.querySelectorAll("script"), i = E(t), n = o.map(function (e) {
                        return e.init
                    }), e = "";
                    a && (e = (t = "null" === (t = r ? decodeURI(a) : a) ? "" : t).slice(t.indexOf(">") + 1, t.lastIndexOf("<")).trim()), function (e) {
                        var n = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : document,
                            e = n.createTextNode(e), t = n.createElement("script");
                        t.setAttribute("data-type", "appended"), t.appendChild(e), C(function () {
                            return n.body.appendChild(t)
                        })
                    }(n.join(" ; ").concat(" ; ").concat(i).concat(" ; ").concat(e)), "function" == typeof window.markoDynamicInitComponents && window.markoDynamicInitComponents()
                }).init()
            }, initAndRenderOnDemandWidgets: function (e, n, t) {
                var i = "", o = document.createElement("div");
                o.setAttribute("data-type", "ondemand"), o.innerHTML += e, o.classList.add("on-demand-widgets-placeholder");
                e = o.querySelector(".gh-module-with-target"), e = e && e.dataset.targetSelector || "#widgets-placeholder", e = document.querySelector(e);
                e.appendChild(o), e.style.display = "block";
                o = o.querySelectorAll("script");
                o && 0 < o.length && (i = E(o, "nb_ondemand_script"));
                o = document.createElement("script");
                o.classList.add("on-demand-scripts"), o.type = "text/javascript", o.setAttribute("data-type", "on-demand-scripts"), o.appendChild(document.createTextNode(i)), document.body.appendChild(o), "function" == typeof window.markoInitComponents && (window.markoInitComponents(n), document.dispatchEvent(new CustomEvent("gadget-component-added", {detail: t})))
            }
        };
        k.privates = {HOLDER_ID: j, createHolder: I};

        function A(e) {
            "complete" === (1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : document).readyState ? e() : window.addEventListener("load", e)
        }

        var _ = g.need, O = k.initAndRenderWidgets, D = k.initAndRenderOnDemandWidgets, b = function (e) {
            var n = e.renderedComponents, t = e.html, i = e.content, o = e.js, r = e.css, a = e.jsInline,
                d = e.onDemand, s = e.parameters,
                e = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : O;
            1 === d ? D(t, n, s) : (window.widget_platform_renderedComponents = n, e([{
                html: t || i,
                init: ""
            }], {hasEncodedHtml: !1, js: o, css: r, jsInline: a}))
        }, k = function (e) {
            var n = window.widget_platform_ondemandUASDoneEventType;
            n && (document.dispatchEvent(new CustomEvent(n, {detail: _(e, "content.response.modules", {})})), delete window.widget_platform_ondemandUASDoneEventType)
        }, H = function (e) {
            var n = e.renderType, t = e.renderDelay, i = e.widgets,
                o = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : O,
                r = 2 < arguments.length && void 0 !== arguments[2] ? arguments[2] : document, e = i && i[0],
                a = e && e.js, d = e && e.css, s = e && e.jsInline;
            1 === n ? o(i, {js: a, css: d, jsInline: s}) : 2 === n ? A(function () {
                return o(i, {js: a, css: d, jsInline: s})
            }, r) : function (e, n) {
                var t = 2 < arguments.length && void 0 !== arguments[2] ? arguments[2] : document;
                A(function () {
                    window.setTimeout(n, e)
                }, t)
            }(t, function () {
                return o(i, {js: a, css: d, jsInline: s})
            }, r)
        };

        function M(e) {
            var n = e.widgetResponse, t = void 0 === n ? {} : n, i = e.onFallback, o = e.initializer,
                r = void 0 === o ? H : o;
            d(this, M);
            var a = t.widgets, n = t.queryParam, e = t.triggerFallBack, o = t.renderType, t = t.renderDelay;
            this.props = {
                widgets: a || [],
                queryParam: n || {},
                triggerFallBack: void 0 === e || e,
                onFallback: i,
                initializer: r,
                renderType: o,
                renderDelay: t
            }
        }

        function L(e, n) {
            if (e = function (e) {
                if (e instanceof ProgressEvent) {
                    e = e.target || e.currentTarget;
                    if (e instanceof XMLHttpRequest) return e.responseText
                }
            }(e)) try {
                var t = e && JSON.parse(e), i = t.rawResponse ? Y : J;
                t.parameters = n, i(t)
            } catch (e) {
                console.debug("Unable to parse Response", e)
            }
        }

        function P() {
            var e = K(0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : {});
            e && e.initializeAndTrigger()
        }

        function q(e) {
            !function () {
                var e = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : {};
                window && e.widgetPlatformOndemandUASDoneEventType && (window.widget_platform_ondemandUASDoneEventType = e.widgetPlatformOndemandUASDoneEventType), e.onDemand = 1, P(e)
            }(e && e.detail)
        }

        var R = (s(M, [{
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
            }]), M), x = g.has, N = g.get, F = "X_EBAY_C_CORRELATION_SESSION", G = "raptor.require",
            z = "trkCorrelationSessionInfo.getTrackingCorrelationSessionInfo", W = {
                readRaptorContext: function () {
                    if (x(window, G)) return N(window, G, function () {
                    })("ebay.context.Context")
                }, extractSessionInfo: function () {
                    if (x(window, z)) return N(window, z, function () {
                    })()
                }, addSessionInfoIntoPage: function (e) {
                    window.trkCorrelationSessionInfo = {}, window.trkCorrelationSessionInfo.getTrackingInfo = function () {
                        return JSON.parse(JSON.stringify(e))
                    }, window.trkCorrelationSessionInfo.getTrackingCorrelationSessionInfo = function () {
                        return e[F]
                    }
                }
            }, B = function () {
                var e = !1;
                return e = "serviceWorker" in navigator && "Notification" in window ? "default" === Notification.permission : e
            }, U = function (e) {
                var n = W.extractSessionInfo(), t = {}, i = null;
                if (n) return n;
                try {
                    i = W.readRaptorContext()
                } catch (e) {
                    i = null
                }
                i = i && i.pid;
                return i || (e = e || window, i = N(e, "GH.C.pageId", 0)), i ? (t[F] = "operationId=" + i, W.addSessionInfoIntoPage(t), t[F]) : null
            }, J = b, Y = k, X = function () {
                var e = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : window,
                    e = "undefined" !== a((n = e).GH) && n.GH && "function" == typeof n.GH.getWidgetDeliveryPlatform && n.GH.getWidgetDeliveryPlatform(),
                    n = e && e.base_path, e = e && e.new_buyer_acquistion;
                return n && e ? n + e : "/gh/useracquisition"
            }, K = function () {
                var n = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : {}, e = n.route || X(),
                    t = function () {
                        var e = 0 < arguments.length && void 0 !== arguments[0] ? arguments[0] : {},
                            n = {correlation: U(), show_optin_banner: B()},
                            t = (t = window.GH && window.GH.C && window.GH.C.xhrBaseUrl).replace(/^https?:\/\//, "");
                        return window.location.hostname !== t && (n.customReferer = encodeURIComponent(window.location.href)), Object.assign(n, e)
                    }(n), i = n.method || "GET", o = JSON.stringify(n.body) || "", r = {shouldRetry: !1, timeout: 1e4};
                if (n.route && (r.hasFullXhrUrl = !0), "HttpClient" in window) return new window.HttpClient(e, i, t, o, r, window).handlers({
                    onload: function (e) {
                        return L(e, n)
                    }, onerror: function (e) {
                        return function (e) {
                            var n = 1 < arguments.length && void 0 !== arguments[1] ? arguments[1] : {};
                            console.debug("useracquisition request has failed!", e);
                            var t = new CustomEvent("gadget-request-error", {detail: {err: e, parameters: n}});
                            document.dispatchEvent(t), n.rawResponse && Y(e)
                        }(e, n)
                    }
                })
            }, V = window.widget_platform, b = function () {
                document && document.addEventListener && document.addEventListener("widget-platform-ondemand-uas-request", q, !1);
                try {
                    new R({widgetResponse: V, onFallback: P}).init()
                } catch (e) {
                    console.debug("WidgetComponent init errored.")
                }
            }, k = {
                destroy: function () {
                    document.removeEventListener("widget-platform-ondemand-uas-request", q)
                }, init: b, v: 2
            };
        return void 0 !== window.GH && void 0 !== window.GH.C && b(), "undefined" != typeof window && "function" == typeof window.define && window.define.amd && (window.GadgetPlatform = k), k
    });
    /* Delivered by Global Header, ISPROD=true */


}
/*
     FILE ARCHIVED ON 12:08:49 Sep 01, 2023 AND RETRIEVED FROM THE
     INTERNET ARCHIVE ON 17:01:24 Nov 26, 2023.
     JAVASCRIPT APPENDED BY WAYBACK MACHINE, COPYRIGHT INTERNET ARCHIVE.

     ALL OTHER CONTENT MAY ALSO BE PROTECTED BY COPYRIGHT (17 U.S.C.
     SECTION 108(a)(3)).
*/
/*
playback timings (ms):
  captures_list: 1266.834
  exclusion.robots: 0.068
  exclusion.robots.policy: 0.057
  cdx.remote: 0.054
  esindex: 0.01
  LoadShardBlock: 275.707 (4)
  PetaboxLoader3.datanode: 239.728 (5)
  load_resource: 78.829
  PetaboxLoader3.resolve: 39.759
*/
