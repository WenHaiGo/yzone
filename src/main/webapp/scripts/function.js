//检查用户名是否存在
function checkItem(obj) {
    $.ajax({
        url: '/yzone/user/checkUserName',
        type: 'post',
        data: {
            username: obj.value
        },
        dataType: "text",
        success: function (data) {
            if (data == "yes") {
                $("#loginreminder").html("ok");
                $("#regreminder").html("sorry");
            }
            else {
                $("#loginreminder").html("sorry");
                $("#regreminder").html("ok");

            }
        },
    })
}

//登录判断
function checkLogin() {
    var username = $("input[name='username']").val();
    var password = $("input[name='password']").val();
    console.log(username)
    console.log(password)
    $.ajax({
        url: '/yzone/user/checkLogin',
        type: 'get',
        data: {
            username: username,
            password: password
        },
        dataType: 'text',
        success: function (data) {


            if (data == "yes") {

                location.href = "index.html";
            }
            else {
                $("#loginMsg").html("密码错误")
            }
        }
    })
}

//处理注册:
function register() {
    var username = $("input[name='username']").val();
    var password = $("input[name='password']").val();
    $.ajax({
        url: '/yzone/user/register',
        type: 'post',
        data: {
            username: username,
            password: password
        },
        dataType: 'text',
        success: function (data) {
            if (data == "yes") {
                location.href = "index.html";
            }
            else {
                alert("密码错误")
            }
        }
    })
}


//点击发表 触发弹出层
function deliver() {
    newspanel = $("#newspanel");
    newspanel.modal({
        show: true,//显示弹出层
        backdrop: 'static',//禁止位置关闭
        keyboard: false//关闭键盘事件
    });
}

//读取所有支持的语言
function  language() {
    $.ajax({
        url: '/yzone/news/language',
        type: 'post',
        dataType: 'json',
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                var language = $(".language");
                var options = '<option>' + data[i].completeName + '</option>';
                language.append(options)
            }
        }
    })
}

//输出翻译结果

function trans() {
    //分别获取翻译格式
    var originlanguage = $("#originlanguage").val();
    var translanguage = $("#translanguage").val();
    var origincontent = $("#origincontent").val();
    $.ajax({
        url: '/yzone/news/trans',
        type: 'post',
        data:{
            transLanguage:translanguage,
            originLanguage:originlanguage,
            originContent:origincontent
        },
        dataType: 'json',
        success: function (data) {
            $("#transcontent").val(data.trans_result[0].dst);/*TODO这里最好是传过来一个单纯的结果*/
        }
    })
}


//当用户登陆成功的时候显示用户头像
$(function () {
    var headPortait = $.cookie('headPortait');
    if(headPortait!=null)
    {

        $("#head").attr("src",headPortait);
        $("#head").show()
        $("#login").remove()
        $("#register").remove();

    }
})




/*负责模态框话题的操作*/
//智能补全
$(document).ready(function () {
    var objects = {};
    $("#topic-input").typeahead({
        source: function (query, process) { //query是输入框输入的文本内容, process是一个回调函数
           console.log(query)
            $.ajax({
                url: "/yzone/topic/pop",
                type: 'post',
                data: {
                    key: query
                },
                success: function (data) {
                    if (data.length == 0) {
                        console.log("没有找到可以自己添加")
                        $("#remind").show().delay(3000).hide(300);
                    }
                    else {
                        var results = [];
                        for (var i = 0; i < data.length; i++) {
                            // objects[data[i].name] = data[i].id;
                            results.push(data[i].topicName);
                        }
                        process(results)
                    }
                },

            });
        },
        afterSelect: function (item) {       //选择项之后的事件，item是当前选中的选项
            $(".topic-ready-name").html(item)
            var readytopic = '<div id="topic-ready" >\n' +
                '                                       <ul>\n' +
                '                                           <li>\n' +
                '                                               <span class="topic-ready-name">'+item+'</span>\n' +
                '                                           </li>\n' +
                '                                           <li>\n' +
                '                                               <!--TODO 这里不应该写样式代码.-->\n' +
                '                                               <span style="font-size: 40px;" onclick="removeTopicTag()">×</span>\n' +
                '                                           </li>\n' +
                '                                       </ul>\n' +
                '                                    </div>'


            $("#topic-input-out").append(readytopic);
            /*清空input*/
            $("#topic-input").val("");

        }
    })
});

/*去除topictag元素*/
function removeTopicTag() {
    $("#topic-ready").remove();
}





//发表新建的话题
function addTopic() {
    var topicName = $("#topicname").val();
    var topicDesc =$("#topicdesc").val()
    $.ajax({
        url: '/yzone/topic/add',
        type: 'post',
        data:{
            topicName:topicName,
            description:topicDesc
        },
        dataType: 'text',
        success:function(data){
            if(data=="no"){
                alert("11111发生错误")
            }
            if(data=="yes")
            {
                //隐藏添加话题的弹出层
                $("#addtopicpanel").modal('hide');
            }
        },
        error: function () {
            alert("发生错误")
        },
    })
}


//modal面板提交函数
function sendNews() {
    var alltopics = "";
    //先把所选topic的收集起来放在隐藏域中
    $(".topic-ready-name").each(function () {
        alltopics += this.innerHTML + ',';
    });
    $("[name=allChosenTopics]").val(alltopics);
    var formData = new FormData($("#uploadForm")[0]);//用form 表单直接 构造formData 对象; 就不需要下面的append 方法来为表单进行赋值了。
    $.ajax({
        url: '/yzone/news/send',
        type: 'post',
        data: formData,
        dataType: 'text',
        processData: false,  //必须false才会避开jQuery对 formdata 的默认处理
        contentType: false,  //必须false才会自动加上正确的Content-Type
        success: function (data) {
            if (data == "no") {
                alert("11111发生错误")
            }
            if (data == "yes") {
                //成功了之后直接跳到首页,首页会从数据库要数据,
                location.href = "/yzone";
            }
        },
        error: function () {
            alert("发生错误")
        },
    })
}