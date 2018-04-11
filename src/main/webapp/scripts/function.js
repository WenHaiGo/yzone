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
            alert(data)
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
            alert(data)

            if (data == "yes") {
                alert(1)
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
            alert(data)

            if (data == "yes") {
                alert(1)
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