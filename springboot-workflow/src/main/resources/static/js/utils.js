/**
 * Created by kyleleeli on 2017/9/21.
 */
// dependency: string_format
format.extend(String.prototype, {
    escape: function(s) {
        return s.replace(/[&<>"'`]/g, function(c) {
            return '&#' + c.charCodeAt(0) + ';'
        })
    },
    upper: function(s) { return s.toUpperCase() }
});


/**
 * Jquery扩展方法
 */
jQuery.extend({
    notInArray:function (source,arr){//返回数组中 非当前数据
        var tmp = [];
        $.each(arr,function (i,n){
            if(source != n){
                tmp.push(n);
            }
        })
        return tmp;
    },
    selectAllChecked:function (tableSelector){//table全选反选操作
        if($(tableSelector+" thead td:eq(0) [type='checkbox']").is(":checked")){
            $(tableSelector+" tbody td [type='checkbox']").prop('checked',true);
        }else{
            $(tableSelector+" tbody td [type='checkbox']").prop('checked',false);
        }
    },
    checkedValue:function (tableSelector){//获取选中的checked的值
        var arr = [];
        $(tableSelector+" tbody td [type='checkbox']:checked").each(function (i,n){
            arr.push($(n).attr('data-value'));
        })
        return arr;
    },
    /** 文件下载 构造 form表单
     * {'url',[{roleIds:role},{source:source},{appId:appId}]}
     * @param url
     * @param fields
     */
    downForm:function (url,fields){
        var form = $("<form>");//定义一个form表单
        form.attr("style","display:none");
        form.attr("target","");
        form.attr("method","post");
        form.attr("action",url);
        fields.forEach(function (value,index,arr){
            var input=$("<input>");
            input.attr("type","hidden");
            input.attr("name",Object.keys(value)[0]);
            input.attr("value",value[Object.keys(value)[0]]);
            form.append(input);
        })
        $("body").append(form);//将表单放置在web中
        form.submit();//表单提交
    }

});
/**
 * Jquery扩展函数   form 序列化为JSOn对象
 */
jQuery.fn.extend({
    serializeObject:function (){
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [ o[this.name] ];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    }

})

