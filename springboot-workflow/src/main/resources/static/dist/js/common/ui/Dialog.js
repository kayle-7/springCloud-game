define(function(require,exports,module){require("common/ui/Loading");var a="ui-dialog-",r=window.addEventListener?'<svg version="1.1" xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 200 200"><path d="M116.152,99.999l36.482-36.486c2.881-2.881,2.881-7.54,0-10.42 l-5.215-5.215c-2.871-2.881-7.539-2.881-10.42,0l-36.484,36.484L64.031,47.877c-2.881-2.881-7.549-2.881-10.43,0l-5.205,5.215 c-2.881,2.881-2.881,7.54,0,10.42l36.482,36.486l-36.482,36.482c-2.881,2.881-2.881,7.549,0,10.43l5.205,5.215 c2.881,2.871,7.549,2.871,10.43,0l36.484-36.488L137,152.126c2.881,2.871,7.549,2.871,10.42,0l5.215-5.215 c2.881-2.881,2.881-7.549,0-10.43L116.152,99.999z"/></svg>':"",l="WebkitAppearance"in document.documentElement.style||"undefined"!=typeof document.webkitHidden,t=function(t){var e={title:"",content:"",width:"auto",buttons:[],onShow:$.noop,onHide:$.noop,onRemove:$.noop},n=$.extend({},e,t||{}),i={};this.el=i,this.width=this.width,this.callback={show:n.onShow,hide:n.onHide,remove:n.onRemove},i.container=window.addEventListener?$('<dialog class="'+a+'container"></dialog>'):$('<div class="'+a+'container"></div>'),history.pushState&&i.container.get(0).addEventListener(l?"webkitAnimationEnd":"animationend",function(t){"dialog"==t.target.tagName.toLowerCase()&&this.classList.remove(a+"animation")}),i.dialog=$('<div class="ui-dialog"></div>').css("width",n.width),i.title=$('<div class="'+a+'title"></div>').html(n.title),i.close=$('<a href="javascript:" class="'+a+'close"></a>').html(r).click($.proxy(function(t){this[this.closeMode](),t.preventDefault()},this));var o=n.content;$.isFunction(o)&&(o=o()),o.size?this.closeMode="hide":this.closeMode="remove",i.body=$('<div class="'+a+'body"></div>')[o.size?"append":"html"](o),i.footer=$('<div class="'+a+'footer"></div>'),this.button(n.buttons),i.container.append(i.dialog.append(i.close).append(i.title).append(i.body).append(i.footer)),document.querySelector||i.container.append('<i class="'+a+'after"></i>');var s=$("."+a+"container");return s.size()?s.eq(0).before(i.container.css({zIndex:1*s.eq(0).css("z-index")+1})):(n.container||$(document.body)).append(i.container),this.display=!1,n.content&&this.show(),this};return t.prototype.button=function(t){var a=this.el,r=this;return a.footer.empty(),$.each(t,function(t,e){e=e||{};var n=t?e.type||"":e.type||"primary",i=t?e.value||"取消":e.value||"确定",o=e.events||{click:function(){r[r.closeMode]()}},s="btn";n&&(s=s+" "+s+"-"+n),a.footer.append(a["button"+t]=$('<a href="javascript:;" class="'+s+'">'+i+"</a>").bind(o))}),this},t.prototype.loading=function(){var t=this.el;return t&&(t.container.attr("class",[a+"container",a+"loading"].join(" ")),t.body.loading(),this.show()),this},t.prototype.unloading=function(t){var e=this.el;return e&&(e.container.removeClass(a+"loading"),e.body.unloading(t)),this},t.prototype.open=function(t,e){var n=this.el,i=$.extend({},{title:"",buttons:[]},e||{});return n&&t&&(n.container.attr("class",[a+"container"].join(" ")),n.title.html(i.title),n.body.html(t),this.button(i.buttons).show()),this},t.prototype.alert=function(t,e){var n=this.el,i=$.extend({},{title:"",type:"remind",buttons:[{}]},e||{});return i.buttons[0].type||"remind"==i.type||(i.buttons[0].type=i.type),n&&t&&(n.container.attr("class",[a+"container",a+"alert"].join(" ")),n.dialog.width("auto"),n.title.html(i.title),n.body.html('<div class="'+a+i.type+'">'+t+"</div>"),this.button(i.buttons).show()),this},t.prototype.confirm=function(t,e){var n=this.el,i=$.extend({},{title:"",type:"warning",buttons:[{type:"warning"},{}]},e||{});return i.buttons.length&&!i.buttons[0].type&&(i.buttons[0].type="warning"),n&&t&&(n.container.attr("class",[a+"container",a+"confirm"].join(" ")),n.dialog.width("auto"),n.title.html(i.title),n.body.html('<div class="'+a+i.type+'">'+t+"</div>"),this.button(i.buttons).show()),this},t.prototype.ajax=function(t,e){var i=this,n=(new Date).getTime(),o={dataType:"JSON",timeout:3e4,error:function(t,e){var n="";n="timeout"==e?"<p>主要是由于请求时间过长，数据没能成功加载，这一般是由于网速过慢导致，您可以稍后重试！</p>":"parsererror"==e?"<p>原因是请求的数据含有不规范的内容。一般出现这样的问题是开发人员没有考虑周全，欢迎向我们反馈此问题！</p>":"<p>一般是网络出现了异常，如断网；或是网络临时阻塞，您可以稍后重试！如依然反复出现此问题，欢迎向我们反馈！</p>",i.alert("<h6>尊敬的用户，很抱歉您刚才的操作没有成功！</h6>"+n,{type:"warning"}).unloading()}},s=$.extend({},o,t||{});if(!s.url)return this;var a=$.extend({},{title:"",content:function(t){return"string"==typeof t?t:"<i style=\"display:none\">看见我说明没使用'options.content'做JSON解析</i>"},buttons:[]},e||{}),r=s.success;return s.success=function(t){i.open(a.content(t),a),(new Date).getTime()-n<100?i.unloading(0):i.unloading(),$.isFunction(r)&&r.apply(this,arguments)},i.loading(),setTimeout(function(){$.ajax(s)},250),this},t.prototype.scroll=function(){var t=$("."+a+"container"),e=!1;if(t.each(function(){"block"==$(this).css("display")&&(e=!0)}),e){var n=17;1!=this.display&&"number"==typeof window.innerWidth&&(n=window.innerWidth-document.documentElement.clientWidth),document.documentElement.style.overflow="hidden",1!=this.display&&$(document.body).css("border-right",n+"px solid transparent")}else document.documentElement.style.overflow="",$(document.body).css("border-right","");return this},t.prototype.show=function(){var t=this.el.container;return t&&(t.css("display","block"),1!=this.display&&t.addClass(a+"animation"),this.scroll(),this.display=!0,$.isFunction(this.callback.show)&&this.callback.show.call(this,t)),this},t.prototype.hide=function(){var t=this.el.container;return t&&(t.hide(),this.scroll(),this.display=!1,$.isFunction(this.callback.hide)&&this.callback.hide.call(this,t)),this},t.prototype.remove=function(t){var e=this.el.container;return e&&(e.remove(),this.scroll(),this.display=!1,$.isFunction(this.callback.remove)&&this.callback.remove.call(this,e)),this},t});