function indexViewModel(selector) {
    var self =this;
    self.selector = selector;
    self.binded = false;


    self.getMenuList = function (){
       /* $.post(url="/home/getMenuList").done(function(data){
            $(".top-nav1").html(data);
        }).fail(function(){
            alert("加载菜单失败!");
        })*/
    }

    /*KO数据绑定核心*/
    self.bindKo = function () {
        if (!self.binded) {
            ko.applyBindings(self, $(selector)[0]);
            self.binded = true;
        }
    };

    /*初始化加载*/
    self.init = function () {
        //加载表格
        self.getMenuList();
        //绑定KO
        self.bindKo();
    };
}
