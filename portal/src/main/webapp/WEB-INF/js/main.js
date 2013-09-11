$(document).ready(function () {
    bind_left_nav_click();
    bind_personal_bar_click();
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

function show_tag_cloud(id) {
    $('ul#jcloud-' + id).jcloud({
        radius: 200,
        size: 30,
        step: 2,
        speed: 50,
        flats: 2,
        clock: 10,
        areal: 100,
        splitX: 100,
        splitY: 100,
        colors: ['#000000', '#DD2222', '#2267DD', '#2A872B', '#872A7B', '#CAC641']
    });
}
