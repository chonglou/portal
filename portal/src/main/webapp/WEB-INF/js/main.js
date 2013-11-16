$(document).ready(function () {
    bind_left_nav_click();
});


function clear_root_div() {
    $("div#gl_root").html("");
}
function clear_message_div() {
    $("div#gl_message").html("");
}

function bind_left_nav_click() {
    var id = "a[id^='left_nav-']";
    $(id).each(function () {
        $(this).click(function () {
            $(id).each(function () {
                $(this).parent().removeClass("active");
                $(this).parent().parent().parent().removeClass("active");
            });
            $(this).parent().addClass("active");
            $(this).parent().parent().parent().addClass("active");
            clear_root_div();
            clear_message_div();
            new Ajax($(this).attr('id').split('-')[1]);
        });
    });
}

function draw_person_bar(fn_logout){
    new Ajax("/personal/bar", undefined, undefined, function(result){
        var title = '登录/注册';
        var items=[];
        for(var k in result.items){
            var v = result.items[k];
            items.push('<li><a href="#" id="'+v.url+'">'+ v.name+'</a></li>');
        }
        $("li#personalBar ul.dropdown-menu").html(items.join('<li class="divider"></li>'));

        if(result.ok){
            title = '欢迎你，';
            if(result.logo){
                title+='<img alt="" class="navbar-icon" src="'+result.logo+'"/> ';
            }
            title+=result.name+'。';


            $("li#qqAuthBar").hide();
            $("li#googleAuthBar").hide();

            $("li#personalBar ul.dropdown-menu a").each(function () {
                var id = $(this).attr("id");
                if(id.indexOf('logout')>=0){
                    if(fn_logout == undefined){
                        fn_logout = function(){
                            if (window.confirm("您确认要退出系统么？")) {
                                window.location.href = id;
                            }
                        };
                    }
                    $(this).click(fn_logout);
                }
                else{
                    $(this).click(function () {
                        window.location.href = id;
                    });
                }


            });
        }
        else{
            $("li#personalBar ul.dropdown-menu a").each(function () {
                $(this).click(function () {
                    clear_root_div();
                    clear_message_div();
                    //alert($(this).attr("id"));
                    new Ajax($(this).attr("id"));
                });
            });
        }

        $("li#personalBar a.dropdown-toggle").html(title+'<b class="caret"></b>');
    });
}
