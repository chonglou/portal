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

function show_tag_cloud(id){
    try {
        TagCanvas.Start(id+"Canvas",id+'Items',{
            textColour: '#ff0000',
            outlineColour: '#ff00ff',
            reverse: true,
            depth: 0.8,
            maxSpeed: 0.05
        });
    } catch(e) {
        $("div#"+id+'Container').hide();
    }
}
