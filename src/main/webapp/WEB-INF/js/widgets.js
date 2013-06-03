function Ajax(url, type, data, success, async) {
    var _init = function () {
        if (type == undefined) {
            type = "GET";
        }
        if (data == undefined) {
            data = [];
        }
        if (success == undefined) {
            success = function (result) {
                if (result.ok) {
                    switch (result.type) {
                        case "form":
                            new FormWindow(result);
                            break;
                        case "grid":
                            break;
                        case "list":
                            break;
                        default:
                            new MessageDialog("尚未支持");
                    }
                }
                else {
                    new MessageDialog(result.data);
                }
            }
        }
        if (async == undefined) {
            async = true;
        }
        $.ajax({
            url: url,
            type: type,
            data: data,
            success: success,
            async: async,
            dataType: "json",
            cache: false,
            error: function () {
                new MessageDialog("HTTP请求失败!");
            }
        });
    };
    _init();
}

function FormWindow(form) {
    var _form;
    var _label_td = function(id, label){
        return "<td class='form-label'><label for='fm-"+_form.id+"-"+id+"'>"+label+"：</label></td>";
    };
    var _input_id = function(id){
        return "fm-"+form.id+"-"+id;
    };
    var _hidden_td = function(id, value){
        return  "<td colspan='2'><input type='hidden' id='"+_input_id(id)+"' value='" + value + "'/></td>";
    };
    var _button = function(id,label, type){
        var btn = "<button id='"+_input_id(id)+"' class='btn ";
        if(type != undefined){
            btn += "btn-"+type;
        }
        btn+="'>"+label+"</button> ";
        return btn;
    };
    var _button_tr = function(buttons){
        var bg = "<tr><td colspan='2'><div class='btn-group'>";
        bg += _button("submit", "提交", "danger");
        bg += _button("reset", "重写", "info");
        for(var i in buttons){
            var btn = form.buttons[i];
            bg += _button(btn.id, btn.label, btn.type);
        }
        bg += "</div></td></tr>";
        return bg;
    };
    var _init = function () {
        _form = form;

        var content = "<fieldset><legend>"+form.title+"</legend>";
        content+="<form id='fm-" + form.id + "' method='" + form.method + "'  action='" + form.action + "'>";
        content +="<table class='form-table'>";
        content += "<tr>"+ _hidden_td("created", form.created)+"</tr>";
        for (var i in form.fields) {
            var field = form.fields[i];
            content += "<tr>";
            switch (field.type) {
                case "text":
                    content += _label_td(field.id, field.label);
                    content+="<td><input type='text' id='"+_input_id(field.id)+"' ";
                    if (field.value != undefined) {
                        content += "value='" + field.value + "' ";
                    }
                    if(field.readonly){
                        content += "readonly ";
                    }
                    content += " />";
                    if(field.required){
                        content += " *";
                    }
                    content += "</td>";
                    break;
                case "textarea":
                    content += _label_td(field.id, field.label);
                    content+="<td><textarea id='"+_input_id(field.id)+"' cols='"+field.cols+"' role='"+field.rows+"' ";

                    if(field.readonly){
                        content += "readonly ";
                    }
                    content += ">";
                    if (field.value != undefined) {
                        content += field.value;
                    }
                    content +="</textarea>";
                    if(field.required){
                        content += " *";
                    }
                    content += "</td>";
                    break;
                case "password":
                    content += _label_td(field.id, field.label);
                    content+="<td><input type='password' id='"+_input_id(field.id)+"' ";
                    if (field.value != undefined) {
                        content += "value='" + field.value + "' ";
                    }
                    content += " /> *";
                    content += "</td>";
                    break;
                default:
                    content += _hidden_td(field.id, field.value);
                    break;
            }
            content += "</tr>";
        }


        content += _button_tr(form.buttons);
        content += "</table></form></fieldset>";

        alert(content);
        $("div#gl_root").html(content);
    };

    _init();
}
function MessageDialog(messages, type) {
    var _init = function () {
        var name;
        switch (type) {
            case "error":
                name = "错误";
                break;
            case "success":
                name = "成功";
                break;
            case "info":
                name = "提示";
                break;
            default:
                type = "block";
                name = "警告";
                break;
        }
        if (type == undefined) {
            type = "error";
        }
        $("div#gl_message").html("<div class='alert alert-block'><button type='button' class='close' data-dismiss='alert'>&times;</button><h4>" +
            name + "：</h4>" + (messages instanceof Array ? messages.join("<br/>") : messages) + "</div>");

    };
    _init();
}
