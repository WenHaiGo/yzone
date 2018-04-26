function getAllUser() {
    //把菜单也读进去
    getMenu()
    $.ajax({
        url: '/yzone/user/all',
        type: 'post',
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                var item = '<tr>\n' +
                    '                        <td>' + i + '</td>\n' +
                    '                        <td>' + data[i].username + '</td>\n' +
                    '                        <td>' + data[i].sex + '</td>\n' +
                    '                        <td>' + data[i].address + '</td>\n' +
                    '                        <td>' + data[i].profession + '</td>\n' +
                    '                        <td>' + data[i].school + '</td>\n' +
                    '                        <td>1</td>\n' +
                    '                        <td>' + (new Date(data[i].createTime)).toLocaleString() + '</td>\n' +
                    '\n' +
                    '                        <td>\n' +
                    '                            <a href="user.html"><i class="icon-pencil"></i></a>\n' +
                    '                            <a href="#myModal" role="button" data-toggle="modal"><i class="icon-remove"></i></a>\n' +
                    '                        </td>\n' +
                    '                    </tr>'
                $("#allUser").append(item);
            }

        },
        error: function () {
            alert("发生错误")
        },
    })
}


function getMenu() {
    $.ajax({
        url: '/yzone/menu/all',
        type: 'post',
        dataType: "json",
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                var fatherMenu = '<a href="#' + data[i].url + '" class="nav-header" data-toggle="collapse"><i class="icon-briefcase"></i>' + data[i].name + '</a>';
                $("#sidebar-nav").append(fatherMenu)
                var temp = data[i].list;
                var scraMenu = '<ul id="' + data[i].url + '" class="nav nav-list collapse">\n' +
                    '</ul>';
                $("#sidebar-nav").append(scraMenu);
                for (var j = 0; j < temp.length; j++) {
                    var sonMenu = '<li>\n' +
                        '            <a href="' + temp[j].url + '">' + temp[j].name + '</a>\n' +
                        '        </li>\n';

                    $("#" + data[i].url).append(sonMenu);

                }

            }

        },
        error: function () {
            alert("hhahaha发生错误")
        },
    })
}


function getAllNews() {
    getMenu();
    $.ajax({
        url: '/yzone/news/manageAll',
        type: 'post',
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                var item = '<tr>\n' +
                    '                        <td>1</td>\n' +
                    '                        <td>' + data[i].originContent + '</td>\n' +
                    '                        <td>' + data[i].transContent + '</td>\n' +
                    '                        <td>我的补充</td>\n' +
                    '                        <td>' + data[i].topicId + '</td>\n' +
                    '                        <td>' + (new Date(data[i].createTime)).toLocaleString() + '</td>\n' +
                    '                        <td>123</td>\n' +
                    '                        <td>123</td>\n' +
                    '                        <td>123</td>\n' +
                    '\n' +
                    '                        <td>\n' +
                    '                            <a href="user.html"><i class="icon-pencil"></i></a>\n' +
                    '                            <a href="#myModal" role="button" data-toggle="modal"><i class="icon-remove"></i></a>\n' +
                    '                        </td>\n' +
                    '                    </tr>'
                $("#allNews").append(item);
            }

        },
        error: function () {
            alert("发生错误")
        },
    })
}
