  
/*****************************************页面加载获取高度*********************************************************/
function TuneHeight(obj) {
    var subWeb = document.frames ? document.frames[obj.name].document : obj.contentDocument;
    if (obj != null && subWeb != null) {
        obj.style.height = (subWeb.body.scrollHeight) + "px";
    }
} 
function TuneParentHeight(obj) {
    var subWeb = parent.document.frames ? parent.document.frames[obj.name].document : obj.contentDocument;
    if (obj != null && subWeb != null) {
        obj.style.height = (subWeb.body.scrollHeight) + "px";
    }
}

function LoadHeight(id) {
    var obj = document.getElementById(id);
    var subWeb = document.frames ? document.frames[obj.name].document : obj.contentDocument;
    if (obj != null && subWeb != null) {
        return (subWeb.body.scrollHeight + 50)
    }
}


//进行Iframe的自动撑开,让所有父页面的Iframe都自动适应包含页高度  
function autoHeight() { 
    var doc = document,  
        p = window;  
    while(p = p.parent){  
        var frames = p.frames,  
            frame,  
            i = 0;  
        while(frame = frames[i++]){  
            if(frame.document == doc){
                frame.frameElement.style.height = frame.document.body.scrollHeight + "px";
                doc = p.document;
                break;  
            }  
        }  
        if(p == top){  
            break;  
        }
    }  
}

/************js获取参数*************************************/
function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}  
  

/****设为首页************************/
 function setHomepage(objurl){　 // 设为首页  
    if (document.all){   
        document.body.style.behavior = 'url(#default#homepage)';   
        document.body.setHomePage(objurl);   
    }else if (window.sidebar){   
        if (window.netscape){   
            try {   
                netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");   
            }catch (e) {   
                alert("该操作被浏览器驳回，如果想启用该功能，请在地址栏输入 about:config,然后该项 signed.applets.codebase_principal_support 值为true");   
            }   
        }   
        var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch);   
        prefs.setCharPref('browser.startup.homepage', objurl);   
    }   
}
/**********添加收藏夹*******************/
function addBookmark(url, title) {//添加收藏夹
    if (document.all) {
        window.external.AddFavorite(url, title);
    }
    else {
        window.sidebar.addPanel(title, url, "");
    }
}

//清空所有Cookie
function ClearCookie() { 
    $.cookie('queryParams', '', { expires: -1 });
    //$.cookie('Login_Key', '', { expires: -1 });
    //$.cookie('Login_Expiration', '', { expires: -1 });
    //$.cookie('Login_Version', '', { expires: -1 });
    //$.cookie('Login_StaffId', '', { expires: -1 });
    //$.cookie('Login_LoginName', '', { expires: -1 });
    //$.cookie('Login_ChineseName', '', { expires: -1 });
    //$.cookie('Login_DeptId', '', { expires: -1 });
    //$.cookie('Login_DeptName', '', { expires: -1 });
    //$.cookie('Login_Token', '', { expires: -1 });
}

/*******************************************日期函数******************************************************/
function getDateWeek()
{
    todayDate = new Date();
    date = todayDate.getDate();
    month= todayDate.getMonth() +1;
    year= todayDate.getYear();
    var dateweek = "";
    if(navigator.appName == "Netscape")
    {
        dateweek = dateweek + (1900+year) + "年" + month + "月" + date + "日 ";
    }
    if(navigator.appVersion.indexOf("MSIE") != -1)
    {
        dateweek = dateweek + year + "年" + month + "月" + date + "日 ";
    }
    switch(todayDate.getDay())
    {
    case 0:dateweek += "星期日";break;
    case 1:dateweek += "星期一";break;
    case 2:dateweek += "星期二";break;
    case 3:dateweek += "星期三";break;
    case 4:dateweek += "星期四";break;
    case 5:dateweek += "星期五";break;
    case 6:dateweek += "星期六";break;
    }
    return dateweek;
}
/***********获取当前时间*******************/
function getTimeString() {
    var mydate = new Date();
    var mytime = mydate.toLocaleTimeString(); //获取当前时间
    return mytime;
}

/****************全选和反选***********************/
function SelectAllById(obj,cid) {
    var frm=document.getElementById(cid).getElementsByTagName("input");
    if(frm){
        for (var i = 0; i < frm.length; i++) {
            if (frm[i].disabled == false) {
                frm[i].checked = obj.checked; 
            }
        }
    }    
}
/*******************实现复选框单选*****************************/
function check(e, id) {
    var objisall = document.getElementById(id).getElementsByTagName("input");
    for (var i = 0; i < objisall.length; i++) {
        if (objisall[i].type == "checkbox") {
            objisall[i].checked = false;
        }
    }
    e.checked = true;
}



/*********************树复选框实现单选**********************/
//单选
function SelectSingleNode(e) {
    e = e ? e : (window.event ? window.event : null);
    var obj = e.srcElement || e.target;
    if (obj.type == 'checkbox') {
        var checkBoxs = document.getElementsByTagName('INPUT');
        for (var i = 0; i < checkBoxs.length; i++) {
            if (checkBoxs[i].type == 'checkbox')
                checkBoxs[i].checked = false;
        }
        obj.checked = true;
    }
}



//多选
function SelectMultiNode(e) {
    e = e ? e : (window.event ? window.event : null);
    var obj = e.srcElement || e.target;
    if (obj.type == 'checkbox') {
        var childrenDivID = obj.id.replace('CheckBox', 'Nodes');
        var div = document.getElementById(childrenDivID);
        if (div == null) return;
        var checkBoxs = div.getElementsByTagName('INPUT');
        for (var i = 0; i < checkBoxs.length; i++) {
            if (checkBoxs[i].type == 'checkbox') {
                checkBoxs[i].checked = obj.checked;
            }
        }
    }
}


 
   
function goback(url) {
    window.location.href = encodeURI(url,"UTF-8");
}
function Getiframe(url) {
    window.parent.location.href = encodeURI(url, "UTF-8");
}
function back() {
    return window.history.back();
}
/*******过滤url的非法字符****************/
function stripscript(s) {
    var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）;—|{}【】‘；：”“'。，、？]")
    var rs = "";
    for (var i = 0; i < s.length; i++) {
        rs = rs + s.substr(i, 1).replace(pattern, '');
    }
    return rs;
}  

//*******全部替换************************************************
//str要替换的字符串，sptr要替换的字符，替换的新字符
function ReplaceAll(str, sptr, sptr1) {
    while (str.indexOf(sptr) >= 0) {
        str = str.replace(sptr, sptr1);
    }
    return str;
}

/**************获取光标的位置**********************/
(function ($, undefined) {
    $.fn.getCursorPosition = function () {
        var el = $(this).get(0);
        var pos = 0;
        if ('selectionStart' in el) {
            pos = el.selectionStart;
        } else if ('selection' in document) {
            el.focus();
            var Sel = document.selection.createRange();
            var SelLength = document.selection.createRange().text.length;
            Sel.moveStart('character', -el.value.length);
            pos = Sel.text.length - SelLength;
        }
        return pos;
    }
})(jQuery);

/************设置光标*******************/ 
$.fn.selectRange = function (start, end) {
    return this.each(function () {
        if (this.setSelectionRange) {
            //this.focus();
            this.setSelectionRange(start, end);
        } else if (this.createTextRange) {
            var range = this.createTextRange();
            range.collapse(true);
            range.moveEnd('character', end);
            range.moveStart('character', start);
            range.select();
        }
    });
};


function checkToInt(obj)
{
    var Pos = $(obj).getCursorPosition();
    //先把非数字的都替换掉，除了数字和.
    obj.value = obj.value.replace(/[^\d]/g, "");
    $(obj).selectRange(Pos, Pos);
}

function checkToIntAndEn(obj) {
    var Pos = $(obj).getCursorPosition();
    //先把非数字的都替换掉，除了数字和.
    obj.value = obj.value.replace(/[^A-Za-z\d]/g, "");
    $(obj).selectRange(Pos, Pos);
}
//判断数字型
function checkNum(obj) {
    var Pos = $(obj).getCursorPosition();
    //先把非数字的都替换掉，除了数字和.
    obj.value = obj.value.replace(/[^\d.]/g, "");
    //必须保证第一个为数字而不是.
    obj.value = obj.value.replace(/^\./g, "");
    //保证只有出现一个.而没有多个.
    obj.value = obj.value.replace(/\.{2,}/g, ".");
    var re = /([0-9]+\.[0-9]{2})[0-9]*/;
    //保证只能输入两位小数，第三位小数四舍五入
    var len = obj.value.length - obj.value.toString().indexOf(".") - 1;
    if (len > 2) {
        obj.value = obj.value.replace(re, "$1");
    }
    //保证.只出现一次，而不能出现两次以上
    obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");    
    $(obj).selectRange(Pos, Pos);
}

//判断数字（包含负数）
function checkDigit(obj) {
    var Pos = $(obj).getCursorPosition();
    //先把非数字的都替换掉，除了数字和. 
    obj.value = obj.value.replace(/[^\-?\d.]/g, "");
    //必须保证第一个为数字而不是.
    obj.value = obj.value.replace(/^\./g, "");
    //保证只有出现一个.而没有多个.
    obj.value = obj.value.replace(/\.{2,}/g, ".");
    //保证只能输入两位小数，第三位舍去
    var re = /([0-9]+\.[0-9]{2})[0-9]*/;
    var len = obj.value.length - obj.value.toString().indexOf(".") - 1;
    if (len > 2) {
        obj.value = obj.value.replace(re, "$1");
    }
    //保证.只出现一次，而不能出现两次以上
    obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
    obj.value = obj.value.replace("-", "$#$").replace(/\-/g, "").replace("$#$", "-");
    $(obj).selectRange(Pos, Pos);
}
 

/****************treeview选中下级的时候，上级也选上 ********************************/
function CheckChild(obj,IsParent) {
    var p = obj.parentNode.parentNode.parentNode.parentNode;
    if (p.nextSibling != null && p.nextSibling.tagName == "DIV") {
        var child = p.nextSibling;
        var inputs = child.getElementsByTagName("input");
        for (var i = 0, j = inputs.length; i < j; i++) {
            inputs[i].checked = obj.checked;
        }
    }
    var par = p.parentNode.getElementsByTagName("input");
    var ischk = true;
    if (!obj.ckecked) {
        ischk = false;
        for (var i = 0, j = par.length; i < j; i++) {
            if (par[i].checked) {
                ischk = true;
                break;
            }
        }
    }
    if (IsParent)
    {
      CheckParent(p, ischk);
    }
}
//选中上级
function CheckParent(obj, chk) {
    var p = obj.parentNode;
    var prev = p.previousSibling;
    if (prev != null && prev.tagName == "TABLE") {
        prev.getElementsByTagName("input")[0].checked = chk;
        if (!chk) {
            chk = false;
            var par = prev.parentNode.getElementsByTagName("input");
            for (var i = 0, j = par.length; i < j; i++) {
                if (par[i].checked) {
                    chk = true;
                    break;
                }
            }
        }
        CheckParent(prev, chk);
    }
} 
　　 

//全选或单选
function SelectedCheckBox(obj,gvlist,action) {
    $("#" + gvlist + " input[type='checkbox']").each(function() {
        if ($(this).attr("disabled") != "disabled") {
            if (action == "all") {
                $(this).attr("checked", obj.checked);
            }
            else if (action == "radio") {
            $(this).attr("checked", false);
            }
        }
    });
    if (action == "radio") {
        obj.checked = true;
    }
}
  

/**********使用JavaScript展开/折叠TreeView中所有节点*************************/

function expandAll(treeViewId) {
    var treeView = document.getElementById(treeViewId);
    var treeLinks = treeView.getElementsByTagName("a");
    var j = true;
    for (i = 0; i < treeLinks.length; i++) {
        if (treeLinks[i].firstChild.tagName == "IMG") {
            var node = treeLinks[i];
            var level = parseInt(treeLinks[i].id.substr(treeLinks[i].id.length - 1), 10);
            var childContainer = document.getElementById(treeLinks[i].id + "Nodes");

            if (j) {
                if (childContainer.style.display == "none")
                    TreeView_ToggleNode(eval(treeViewId + "_Data"), level, node, 'r', childContainer);
                j = false;
            }
            else {
                if (childContainer.style.display == "none")
                    TreeView_ToggleNode(eval(treeViewId + "_Data"), level, node, 'l', childContainer);
            }
        }
    }
}
function collapseAll(treeViewId) {
    var treeView = document.getElementById(treeViewId);
    var treeLinks = treeView.getElementsByTagName("a");
    var j = true;
    for (i = 0; i < treeLinks.length; i++) {
        if (treeLinks[i].firstChild.tagName == "IMG") {
            var node = treeLinks[i];
            var level = parseInt(treeLinks[i].id.substr(treeLinks[i].id.length - 1), 10);
            var childContainer = document.getElementById(treeLinks[i].id + "Nodes");

            if (j) {
                if (childContainer.style.display == "block")
                    TreeView_ToggleNode(eval(treeViewId + "_Data"), level, node, 'r', childContainer);
                j = false;
            }
            else {
                if (childContainer.style.display == "block")
                    TreeView_ToggleNode(eval(treeViewId + "_Data"), level, node, 'l', childContainer);
            }
        }
    }
}  
///异步请求
function ToServer(url, data, success, error) { 
    $.ajax({
        type: "Post",
        url: url,
        data: data,
       // contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: success,
        error: error
    });
}
 
//获取当前页面的名字
function GetCurrentPageName() { 
   return location.pathname.replace(/(.+)[\\/]/,"");
}


//键值对
function Dictionary() {
    this.data = new Array();

    this.put = function (key, value) {
        this.data[key] = value;
    };

    this.get = function (key) {
        return this.data[key];
    };

    this.remove = function (key) {
        this.data[key] = null;
    };

    this.isEmpty = function () {
        return this.data.length == 0;
    };

    this.size = function () {
        return this.data.length;
    };
    this.getData = function () {
        return this.data;
    };
}

Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份           
        "d+": this.getDate(), //日           
        "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时           
        "H+": this.getHours(), //小时           
        "m+": this.getMinutes(), //分           
        "s+": this.getSeconds(), //秒           
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度           
        "S": this.getMilliseconds() //毫秒           
    };
    var week = {
        "0": "/u65e5",
        "1": "/u4e00",
        "2": "/u4e8c",
        "3": "/u4e09",
        "4": "/u56db",
        "5": "/u4e94",
        "6": "/u516d"
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[this.getDay() + ""]);
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}

function formatDatebox(value, row) {
    if (value == null || value == '') {
        return '';
    }
    var pa = /.*\((.*)\)/;   //获取    /Date(时间戳) 括号中的字符串时间戳 的正则表达式
    var unixtime = value.match(pa)[1];  //通过正则表达式来获取到时间戳的字符串  
    var dt = new Date(parseInt(unixtime));//关键代码，将那个长字符串的日期值转换成正常的JS日期格式             
    return dt.format("yyyy-MM-dd"); //这里用到一个javascript的Date类型的拓展方法，这个是自己添加的拓展方法，在后面的步骤3定义  
}

function format_Date_box(value, row) {
    if (value == null || value == '') {
        return '';
    }
    var pa = /.*\((.*)\)/;   //获取    /Date(时间戳) 括号中的字符串时间戳 的正则表达式
    var unixtime = value.match(pa)[1];  //通过正则表达式来获取到时间戳的字符串  
    var dt = new Date(parseInt(unixtime));//关键代码，将那个长字符串的日期值转换成正常的JS日期格式             
    return dt.format("yyyy-MM-dd HH:mm:ss"); //这里用到一个javascript的Date类型的拓展方法，这个是自己添加的拓展方法，在后面的步骤3定义  
}

//bool类型转换
function ConvertToBoolean(flag) {

    if (flag == "True" || flag == "true") {
        return Boolean(flag);
    }
    else {
        return Boolean("");
    }
}

//文件下载
jQuery.download = function (url, data, method) {
    // 获取url和data
    if (url && data) {
        // data 是 string 或者 array/object
        data = typeof data == 'string' ? data : jQuery.param(data);
        // 把参数组装成 form的  input
        var inputs = '';
        jQuery.each(data.split('&'), function () {
            var pair = this.split('=');
            inputs += '<input type="hidden" name="' + pair[0] + '" value="' + pair[1] + '" />';
        });
        // request发送请求
        jQuery('<form action="' + url + '" method="' + (method || 'post') + '">' + inputs + '</form>')
        .appendTo('body').submit().remove();
    };
};

//左边补齐
function PadLeft(str, length) {
    return (Array(length).join('0') + str).slice(-length);
}
//右边补齐
function PadRight(str, length) {
    return (str+Array(length).join('0')).slice(-length);
}
//添加千分位
function toThousands(num) {
    return num == null ? "" : $.formatMoney(num, 2);
}
 

//html解码
function htmldecode(content) {
    var div = document.createElement('div');
    div.innerHTML = content;
    return div.innerText || div.textContent;
}



function uploadfile(imgType, directory, obj) { 
    $.ajaxFileUpload({
        url: '/sys/uploadfileImg', //你处理上传文件的服务端
        secureuri: false,
        fileElementId: 'uploadfile_' + imgType,
        dataType: 'json',
        data: { directory: directory },
        type: "post",
        async: false,
        beforeSend: function (xmlHttp) {
            xmlHttp.setRequestHeader("If-Modified-Since", "0");
            xmlHttp.setRequestHeader("Cache-Control", "no-cache");
        },
        success: function (data, status)  //服务器成功响应处理函数
        {
            if (data.Successed) {
                $("#img_" + imgType).attr("src", data.Data);
            }
            else {
                alert(data.Message);
            }
        },
        error: function (data, status, e)//服务器响应失败处理函数
        {
            alert(e);
        }
    });
}

function Signout() {
    if (confirm("您确认退出系统吗？")) {
        ToServer("/Common/Signout", null,
        function (data) {
            //goback($(this).attr("data-url"));
        },
        function (err) {
            var error = $.parseJSON(err.responseText);
            alert(error.Message);
        });
    }
    else {
        return false;
    }
}