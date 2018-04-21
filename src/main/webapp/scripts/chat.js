//给侧边栏添加选中效果

$('body').on('click', '.conLeft li', function () {
    $(this).addClass('bg').siblings().removeClass('bg');
    var intername = $(this).children('.liRight').children('.intername').text();
    $('.headName').text(intername);
    $('.newsList').html('');
})

//处理发送
$('.sendBtn').on('click', function () {
    var message = $('#dope').val();
    $('#dope').val('');
    send(message);
    var str = '';
    str += '<li>' +
        '<div class="answerHead"><img src="img/6.jpg"/></div>' +
        '<div class="answers">' + message + '</div>' +
        '</li>';
    $('.newsList').append(str);
    $('.conLeft').find('li.bg').children('.liRight').children('.infor').text(message);
    $('.RightCont').scrollTop($('.RightCont')[0].scrollHeight);
})

function answers(obj) {
    alert(obj+"1212")
    var answer = "";
     answer += '<li>' +
        '<div class="nesHead"><img src="img/6.jpg"/></div>' +
        '<div class="news"><img class="jiao" src="img/20170926103645_03_02.jpg">' + obj + '</div>' +
        '</li>';
    $('.newsList').append(answer);
    $('.RightCont').scrollTop($('.RightCont')[0].scrollHeight);
}

/*
 * 鼠标滑过表情包
 */
$('.ExP').on('mouseenter', function () {
    $('.emjon').show();
})
$('.emjon').on('mouseleave', function () {
    $('.emjon').hide();
})
/*点击发送*/
$('.emjon li').on('click', function () {
    var imgSrc = $(this).children('img').attr('src');
    var str = "";
    str += '<li>' +
        '<div class="nesHead"><img src="img/6.jpg"/></div>' +
        '<div class="news"><img class="jiao" src="img/20170926103645_03_02.jpg"><img class="Expr" src="' + imgSrc + '"></div>' +
        '</li>';
    $('.newsList').append(str);
    $('.emjon').hide();
    $('.RightCont').scrollTop($('.RightCont')[0].scrollHeight);
})