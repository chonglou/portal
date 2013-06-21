$(document).ready(function(){
    bind_left_nav_click();
    bind_personal_bar_click();
});

function bind_personal_bar_click(){
    $("a[id^='personal_bar-']").each(function(){
        $(this).click(function(){
            clear_root_div();
            new Ajax("/personal/"+$(this).attr("id").split('-')[1]);
        });
    });
}

function clear_root_div(){
    $("div#gl_root").html("");
}

function bind_left_nav_click(){
    var id ="a[id^='left_nav-']";
    $(id).each(function(){
        $(this).click(function(){
            $(id).each(function(){
                $(this).parent().removeClass("active");
            });
            $(this).parent().addClass("active");
        });
    });
}
