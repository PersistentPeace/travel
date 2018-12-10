$(function () {
    //这里异步请求将头部以及尾部的内容通过data请求过来. 将整个页面的内容作为要请求的
    //页面加载到客户端填充到data中, data中存放的就是header.html中所有的数据.
    //很多的页面需要相同的内容, 都可以通过相同的方式引入.
    $.get("header.html",function (data) {
        $("#header").html(data);
    });
    $.get("footer.html",function (data) {
        $("#footer").html(data);
    });
});