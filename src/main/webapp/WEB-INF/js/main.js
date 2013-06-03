$(document).ready(function(){
    bind_left_nav_click();
});

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
