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
                $("#loginMsg").html("dfsdfdsfsd密码错误")
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

//加载所有未读消息
function loadAllUnreadMessage() {
    var username = $.cookie("username")
    $.ajax({
        url: '/yzone/message/unread',
        type: 'post',
        data: {
            userName: username
        },
        success: function (data) {
            $("#chatBadge").html(data.length);

            for (var i = 0; i < data.length; i++) {
                var abstract = '\n' +
                    '                            <li>\n' +
                    '                                <h4><a href="/yzone/chat.html?name=' + data[i].userName + '">' + data[i].userName + '</a></h4>\n' +
                    '                                <span>' + data[i].send + '</span>\n' +
                    '                            </li>';
                $("#chatAbstract").append(abstract);
            }

        }
    })
}

//发送ajax到服务器去获取
function loadHotTopic() {
    $.ajax({
        url: '/yzone/topic/hot',
        type: 'post',
        dataType: 'json',
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                var topicItem = '<p>' + data[i] + '</p>';
                $("#hotTopic").append(topicItem);
            }
        }

    })
}

//读取所有支持的语言  因为onload不支持同时加载俩个方法,只好放在一起
function language() {

    loadHotTopic();
    //加载所有未处理消息
    loadAllUnreadMessage()
//加载所有news
    loadPageNews()
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
        data: {
            transLanguage: translanguage,
            originLanguage: originlanguage,
            originContent: origincontent
        },
        dataType: 'text',
        success: function (data) {
            $("#transcontent").val(data);
        }
    })
}


//当用户登陆成功的时候显示用户头像
$(function () {
    var headPortait = $.cookie('headPortait');
    if (headPortait != null) {

        $("#head").attr("src", headPortait);
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
                '                                               <span class="topic-ready-name">' + item + '</span>\n' +
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
    var topicDesc = $("#topicdesc").val()
    $.ajax({
        url: '/yzone/topic/add',
        type: 'post',
        data: {
            topicName: topicName,
            description: topicDesc
        },
        dataType: 'text',
        success: function (data) {
            if (data == "no") {
                alert("11111发生错误")
            }
            if (data == "yes") {
                //隐藏添加话题的弹出层
                $("#addtopicpanel").modal('hide');
            }
        },
        error: function () {
            alert("网络发生错误")
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
            alert("网络发生错误")
        },
    })
}


/*以下是对于首页消息的处理函数*/

/*删除自己发的一条消息*/
function deleteNews(obj) {
    var r = confirm("确定删除吗?");
    if (r == true) {
        $(obj).parent().parent().parent().remove();
        //去除前面的close
        var newsId = $(obj).attr("id").replace("close", "");

        $.ajax({
            url: '/yzone/news/delete',
            type: 'post',
            data: {
                newsId: newsId
            },
            dataType: 'text',
            success: function (data) {
                if (data == "yes") {
                    console.log("删除成功")
                }
                else {
                    alert("删除失败");
                }
            }
        })

    }

}

function showAll(tempObj) {
    /*var closeBtn = $($(obj).prev()).children("i");
    closeBtn.show();*/
    var tobj = $(tempObj).parent();
    //获取内容div
    var obj = $(tobj).prev().prev()

    var total_height = obj[0].scrollHeight; //文章总高度
    var show_height = 200; //定义原始显示高度
    if (total_height > show_height) {
        obj[0].style.height = total_height + 'px';
        $(tempObj).hide()
    }
}

/*  function showAll(obj) {
      var btn = $(obj).find("div:eq(0)").find("div:eq(0)");
      var total_height = obj.scrollHeight; //文章总高度
      var show_height = 300; //定义原始显示高度
      if (total_height > show_height) {
          btn.show();
          btn.click(function () {
              obj.style.height = total_height + 'px';
              btn.hide();
          })
      }
  }*/
/*
function hideNews(obj) {
    console.log(1)
    obj.style.height = 200 + 'px';
}*/


/*用于影藏搜索智能提示*/
function hideSearchRecom() {
    $("#searchRecom").hide()
}

//用于在导航页面点击我的跳转到个人页面
function navToPerson() {
    var username = $.cookie("username");
    location.href = "/yzone/mypage.html?username=" + username;
}


/*TODO 这里其实应该把弹出和赋值操作放在一起*/
function assignToModal() {
    var modalorigin = $("#origincontent").val();
    var modaltrans = $("#transcontent").val();
    $("#modalorigin").val(modalorigin);
    $("#modaltrans").val(modaltrans);
}


//处理主页news


function toPersonPage(userName) {
    $.ajax({
        url: '/yzone/user/person',
        type: 'post',
        data: {
            userName: userName
        },
        success: function (data) {
            if (data == "my") {
                location.href = "/yzone/mypage.html?username=" + userName;
            }
            else if (data = "other") {
                location.href = "/yzone/otherpage.html?username=" + userName;
            }
        },
        error: function () {
            /* alert("发生错误")*/
        },
    })
}

/*加载登陆之后页面的news流*/

//加载所有动态
/*
function loadAllNews() {
    $.ajax({
        url: '/yzone/news/all',
        type: 'post',
        dataType: 'json',
        success: function (data) {
            for (var i = data.length - 1; i >= 0; i--) {
                console.log(data[i].topicName)
                //处理是否有权利删除
                var closeId = "close" + data[i].newsId;
                //处理是否有赞过
                var likeId = "like" + data[i].newsId;
                //处理是否踩过
                var hateId = "hate" + data[i].newsId;
                var commentId = "comment" + data[i].newsId;
                var comment = data[i].userName + ',' + data[i].newsId;
                var news = '<div class="feed-list-item clearfix">\n' +
                    '\t\t\t\t\t\t\t<div class="feed-content"  >\n' +
                    '\t\t\t\t\t\t\t\t<div class="topic-source" >来自话题：' + data[i].topicName + '<a  style="margin-right: 600px"></a>\n' +
                    '                             <i onclick="deleteNews(this)" id=' + closeId + ' style="display: none"  class="fa fa-window-close icon-larger"></i></div>\n' +
                    '\t\t\t\t\t\t\t\t<div class="feed-info"  \n' +
                    '\t\t\t\t\t\t\t\t\t<a onclick="toPersonPage(\'' + data[i].userName + '\')" ><img src="' + data[i].headPortrait + '" class="portrait" /></a>\n' +
                    '\t\t\t\t\t\t\t\t\t<span class="nickname">' + data[i].userName + '</span>，\n' +
                    '\t\t\t\t\t\t\t\t\t<span class="userdesc">' + data[i].personSignature + '</span>\n' +
                    '\t\t\t\t\t\t\t\t\t<h3 class="ContentItem-title">' + data[i].originContent + '</h3>\n' +
                    '\t\t\t\t\t\t\t\t\t<span class="trans-content"  ><p>' + data[i].transContent + '</p>\n' +
                    '\t\t\t\t\t\t\t\t\t<img src="' + data[i].mediaUrl + '" />\n' +
                    '\t\t\t\t\t\t\t\t\t</span>\n' +
                    '\t\t\t\t\t\t\t\t\t\n' +
                    '\t\t\t\t\t\t\t\t\t\n' +
                    '\t\t\t\t\t\t\t\t</div>\n' + '<p></p>' +
                    '\t\t\t\t\t\t\t\t<div class="user-action"style="margin-top: 10px;" >\n' +
                    '\t\t\t\t\t\t\t\t\t<a  onclick="like(this)" id=' + likeId + ' class="fa fa-thumbs-o-up icon-5x"></a>\n' +
                    '\t\t\t\t\t\t\t\t\t&nbsp;&nbsp;&nbsp;\n' +
                    '\t\t\t\t\t\t\t\t\t<a onclick="hate(this)" id=' + hateId + ' class="fa fa-thumbs-o-down"></a>\n' +
                    '\t\t\t\t\t\t\t\t\t\t<a onclick="loadCommetById(' + data[i].newsId + ')" ><img src="img/chat.png" /><span >评论</span></a>\n' +
                    '\t\t\t\t\t\t\t\t\t\t<a><img src="img/chat.png" /><span>收藏</span></a>\n' +
                    '\t\t\t\t\t\t\t\t\t\t<a href="#"><img src="img/chat.png" /><span>举报</span></a>\n' + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
                    '<div  style="display: inline-block;" onclick="showAll(this)" hidden="hidden" class="button button-rounded button-tiny">展示全部</div>' +
                    '\t\t\t\t\t\t\t\t</div>\n' + '<p></p>' +
                    '\t\t\t\t\t\t\t</div>\n   <div id="' + commentId + '" style="margin-bottom: 20px;margin-left: 20px; " ></div>' +
                    '<input  placeholder="写下你的评论" style=" margin-left: 80px; margin-right: 10px;margin-bottom: 10px; width:600px;" type="text">' +
                    '<a onclick="sendComment(\'' + data[i].newsId + '\',\'' + data[i].userName + '\',this)">发表</a></div>' +
                    '\t\t\t\t\t\t</div>';
                $("#feed-list").append(news);
                //$('.feed-list').mouseover(showAll(document.getElementById(showAll())));
                //判断是否有删除的权利,如果有就显示删除按钮
                if (data[i].canDelete == true) {
                    $("#" + closeId).show();
                }
                //判断是否为这个news点击过赞或者踩
                if (data[i].like == true) {
                    $("#" + likeId).css("color", "red");
                }
                if (data[i].hate == true) {
                    $("#" + hateId).css("color", "red");
                }
            }
        },
        error: function () {

            alert("发生错误")
        },
    })
}
*/


function loadPageNews() {
    $.ajax({
        url: '/yzone/news/pageNews',
        type: 'post',
        data: {
            pageNo: $("#currentPage").val()
        },
        dataType: 'json',
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                console.log(data[i].topicName)
                //处理是否有权利删除
                var closeId = "close" + data[i].newsId;
                //处理是否有赞过
                var likeId = "like" + data[i].newsId;
                //处理是否踩过
                var hateId = "hate" + data[i].newsId;
                var commentId = "comment" + data[i].newsId;
                var comment = data[i].userName + ',' + data[i].newsId;
                var news = '<div class="feed-list-item clearfix">\n' +
                    '\t\t\t\t\t\t\t<div class="feed-content"  >\n' +
                    '\t\t\t\t\t\t\t\t<div class="topic-source" >来自话题：' + data[i].topicName + '<a  style="margin-right: 600px"></a>\n' +
                    '                             <i onclick="deleteNews(this)" id=' + closeId + ' style="display: none"  class="fa fa-window-close icon-larger"></i></div>\n' +
                    '\t\t\t\t\t\t\t\t<div class="feed-info"  \n' +
                    '\t\t\t\t\t\t\t\t\t<a onclick="toPersonPage(\'' + data[i].userName + '\')" ><img src="' + data[i].headPortrait + '" class="portrait" /></a>\n' +
                    '\t\t\t\t\t\t\t\t\t<span class="nickname">' + data[i].userName + '</span>，\n' +
                    '\t\t\t\t\t\t\t\t\t<span class="userdesc">' + data[i].personSignature + '</span>\n' +
                    '\t\t\t\t\t\t\t\t\t<h3 class="ContentItem-title">' + data[i].originContent + '</h3>\n' +
                    '\t\t\t\t\t\t\t\t\t<span class="trans-content"  ><p>' + data[i].transContent + '</p>\n' +
                    '\t\t\t\t\t\t\t\t\t<img src="' + data[i].mediaUrl + '" />\n' +
                    '\t\t\t\t\t\t\t\t\t</span>\n' +
                    '\t\t\t\t\t\t\t\t\t\n' +
                    '\t\t\t\t\t\t\t\t\t\n' +
                    '\t\t\t\t\t\t\t\t</div>\n' + '<p></p>' +
                    '\t\t\t\t\t\t\t\t<div class="user-action"style="margin-top: 10px;" >\n' +
                    '\t\t\t\t\t\t\t\t\t<a  onclick="like(this,\'' + data[i].userName + '\')" id=' + likeId + ' class="fa fa-thumbs-o-up icon-5x"></a>\n' +
                    '\t\t\t\t\t\t\t\t\t&nbsp;&nbsp;&nbsp;\n' +
                    '\t\t\t\t\t\t\t\t\t<a onclick="hate(this)" id=' + hateId + ' class="fa fa-thumbs-o-down"></a>\n' +
                    '\t\t\t\t\t\t\t\t\t\t<a onclick="loadCommetById(' + data[i].newsId + ')" ><img src="img/chat.png" /><span >评论</span></a>\n' +
                    '\t\t\t\t\t\t\t\t\t\t<a><img src="img/chat.png" /><span>收藏</span></a>\n' +
                    '\t\t\t\t\t\t\t\t\t\t<a href="#"><img src="img/chat.png" /><span>举报</span></a>\n' + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
                    '<div  style="display: inline-block;" onclick="showAll(this)" hidden="hidden" class="button button-rounded button-tiny">展示全部</div>' +
                    '\t\t\t\t\t\t\t\t</div>\n' + '<p></p>' +
                    '\t\t\t\t\t\t\t</div>\n   <div id="' + commentId + '" style="margin-bottom: 20px;margin-left: 20px; " ></div>' +
                    '<input  placeholder="写下你的评论" style=" margin-left: 80px; margin-right: 10px;margin-bottom: 10px; width:600px;" type="text">' +
                    '<a onclick="sendComment(\'' + data[i].newsId + '\',\'' + data[i].userName + '\',this)">发表</a></div>' +
                    '\t\t\t\t\t\t</div>';
                $("#feed-list").append(news);
                //$('.feed-list').mouseover(showAll(document.getElementById(showAll())));
                //判断是否有删除的权利,如果有就显示删除按钮
                if (data[i].canDelete == true) {
                    $("#" + closeId).show();
                }
                //判断是否为这个news点击过赞或者踩
                if (data[i].like == true) {
                    $("#" + likeId).css("color", "red");
                }
                if (data[i].hate == true) {
                    $("#" + hateId).css("color", "red");
                }
            }
            //到这里表示加载成功了,改变当前页数
            $("#currentPage").val(parseInt($("#currentPage").val()) + 1)
        },
        error: function () {

            /*  alert("发生错误")*/
        },
    })
}


//页面加载到尾部自动加载其他数据
$(window).scroll(
    function () {
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight == scrollHeight) {
            //自动加载下一页..
            if ($("#isSearch").val() == 1) {
                //加载普通页面
                loadPageNews()
            }
            else if ($("#isSearch").val() == 0) {
                //加载搜索页面
                showSearchResult();

            }

        }
    });


/*处理评论*/

//加载评论

function loadCommetById(newsId) {
    $(this).next("input").show()
    $.ajax({
        url: '/yzone/news/getComment',
        type: 'post',
        data: {
            newsId: newsId
        },
        dataType: "json",
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                var com = '<div><a>' + data[i].userName + '</a>:<span>' + data[i].content + '</span></div>';
                $("#comment" + newsId).append(com);
            }
        },
        //表示失败 则不要修改颜色
        error: function () {
            alert("加载失败")
        }
    })
}

//发表评论5ttr
function sendComment(newsId, userName, content) {
    $.ajax({
        url: '/yzone/news/comment',
        type: 'post',

        data: {
            userName: userName,
            newsId: newsId,
            content: $(content).prev().val()
        },
        success: function (data) {

            $(content).prev().val("");
        },
        //表示失败 则不要修改颜色
        error: function () {
            alert("评论失败")
        }
    })
}

/*点赞或者点踩来触发 TODO 其实这里的逻辑非常复杂,点击之后马上要出现效果,但是如果网络环境不好的话就再次把颜色退回去*/
function like(obj, userName) {
    if ($(obj).css("color") == "rgb(255, 0, 0)") {
        $(obj).css("color", "")
    }
    else {
        $(obj).css("color", "red")
    }
    $.ajax({
        url: '/yzone/news/like',
        type: 'post',
        data: {
            userName: userName,
            newsId: $(obj).attr("id").replace("like", "")
        },
        dataType: "json",
        //TODO 答辩完就来修改
        error: function () {

        }
    })
}

function hate(obj) {
    if ($(obj).css("color") == "rgb(255, 0, 0)") {
        $(obj).css("color", "")
    }
    else {
        $(obj).css("color", "red")
    }
    $.ajax({
        url: '/yzone/news/hate',
        type: 'post',
        data: {
            userName: "john",
            newsId: $(obj).attr("id").replace("hate", "")
        },
        dataType: "json",
        //TODO 答辩完就来修改
        success: function (data) {
            //表示失败 则不要修改颜色
            /*if(data==no)
            {

            }*/
        },
        //表示失败 则不要修改颜色
        error: function () {

        }
    })
}

//搜索下拉框推荐
function showSerachRecom() {
    $("#userrecom").children("div").remove();
    $("#topicrecom").children("div").remove();
    $("#contentrecom").children("div").remove();
    $("#searchRecom").show()
    /*清空上一次的*/

    var key = $("#searchtext").val();
    if (key != null && key != "") {
        $.ajax({
            url: '/yzone/search/recom',
            type: 'post',
            data: {
                key: key
            },
            dataType: 'json',
            success: function (data) {
                var user = data.user;
                if (user != null) {
                    for (var i = 0; i < user.length; i++) {
                        var userrecom = '<div  style="cursor: pointer;" onclick="toPersonPage(\'' + user[i].username + '\')">' + user[i].username + '</div>'
                        $("#userrecom").append(userrecom);
                    }
                }
                /*实现话题的提示*/
                var topic = data.topic;
                if (topic != null) {

                }

            }
        })
    }

}
