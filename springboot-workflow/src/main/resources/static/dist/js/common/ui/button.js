define(function(require,exports,module){require("plugin/jquery"),function($){"use strict";var s=function(t,e){this.$element=$(t),this.options=$.extend({},s.DEFAULTS,e),this.isLoading=!1};function n(i){return this.each(function(){var t=$(this),e=t.data("bs.button"),n="object"==typeof i&&i;e||t.data("bs.button",e=new s(this,n)),"toggle"==i?e.toggle():i&&e.setState(i)})}s.VERSION="3.4.0",s.DEFAULTS={loadingText:"loading..."},s.prototype.setState=function(t){var e="disabled",n=this.$element,i=n.is("input")?"val":"html",s=n.data();t+="Text",null==s.resetText&&n.data("resetText",n[i]()),setTimeout($.proxy(function(){n[i](null==s[t]?this.options[t]:s[t]),"loadingText"==t?(this.isLoading=!0,n.addClass(e).attr(e,e).prop(e,!0)):this.isLoading&&(this.isLoading=!1,n.removeClass(e).removeAttr(e).prop(e,!1))},this),0)},s.prototype.toggle=function(){var t=!0,e=this.$element.closest('[data-toggle="buttons"]');if(e.length){var n=this.$element.find("input");"radio"==n.prop("type")?(n.prop("checked")&&(t=!1),e.find(".active").removeClass("active"),this.$element.addClass("active")):"checkbox"==n.prop("type")&&(n.prop("checked")!==this.$element.hasClass("active")&&(t=!1),this.$element.toggleClass("active")),n.prop("checked",this.$element.hasClass("active")),t&&n.trigger("change")}else this.$element.attr("aria-pressed",!this.$element.hasClass("active")),this.$element.toggleClass("active")};var t=$.fn.button;$.fn.button=n,$.fn.button.Constructor=s,$.fn.button.noConflict=function(){return $.fn.button=t,this},$(document).on("click.bs.button.data-api",'[data-toggle^="button"]',function(t){var e=$(t.target).closest(".btn");n.call(e,"toggle"),$(t.target).is('input[type="radio"], input[type="checkbox"]')||(t.preventDefault(),e.is("input,button")?e.trigger("focus"):e.find("input:visible,button:visible").first().trigger("focus"))}).on("focus.bs.button.data-api blur.bs.button.data-api",'[data-toggle^="button"]',function(t){$(t.target).closest(".btn").toggleClass("focus",/^focus(in)?$/.test(t.type))})}(jQuery)});