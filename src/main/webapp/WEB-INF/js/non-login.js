function bind_personal_bar_click() {
    $("a[id^='personal_bar-']").each(function () {
        $(this).click(function () {
            clear_root_div();
            clear_message_div();
            new Ajax("/personal/" + $(this).attr("id").split('-')[1]);
        });
    });

}

$(document).ready(function () {
    //new Ajax("/personal/login");

});