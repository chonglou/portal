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
                        case "redirect":
                            window.location.href = result.data[0];
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

function GridWindow(grid) {
    var _init=function(){
        //var content =
    };
    _init();
}

function FormWindow(form) {
    var _form;
    var _field = function (id, label, input) {
        if(label == undefined){
            return input;
        }
        var content = "<div class='control-group'>";
        content += "<label class='control-label' ";
        if (id != undefined) {
            content += " for='fm-" + _form.id + "-" + id + "'";
        }
        content += ">" + label + "：</label>";
        content += "<div class='controls'>";
        content += input;
        content += "</div>";
        content += "</div>";
        return content;
    };

    var _id = function (id) {
        return "fm-" + form.id + "-" + id;
    };
    var _hidden_field = function (id, value) {
        return  "<input type='hidden' id='" + _id(id) + "' value='" + value + "'/>";
    };

    var _button = function (id, label, type) {
        var btn = "<button type='button' id='" + _id(id) + "' class='btn ";
        if (type != undefined) {
            btn += "btn-" + type;
        }
        btn += "'>" + label + "</button> ";
        return btn;
    };
    var _button_group = function (buttons) {
        var content = "<div class='form-actions'>";
        content += _button("submit", "提交", "danger");
        content += _button("reset", "重写", "info");
        for (var i in buttons) {
            var btn = form.buttons[i];
            content += _button(btn.id, btn.label, btn.type);
        }
        content += "</div>";
        return content;
    };
    var _init = function () {
        _form = form;

        var content = "<form class='form-horizontal' method='" + form.method + "'  action='" + form.action + "'>";
        content += "<fieldset><legend>" + form.title + "</legend>";
        content+="<div id='"+_id("alert")+"'></div>";
        content += _hidden_field("created", form.created);
        for (var i in form.fields) {
            var field = form.fields[i];
            var input;
            switch (field.type) {
                case "text":
                    input = "<input class='input-xlarge focused' style='width: "+field.width+"px' type='text' id='" + _id(field.id) + "' ";
                    if (field.value != undefined) {
                        input += "value='" + field.value + "' ";
                    }
                    if (field.readonly) {
                        input += "readonly ";
                    }
                    input += " />";
                    if (field.required) {
                        input += " *";
                    }
                    break;
                case "textarea":
                    input = "<textarea id='" + _id(field.id) + "' style='width: "+field.width+"px;height: "+field.height+"px;' ";
                    if (field.readonly) {
                        input += "readonly ";
                    }
                    input += ">";
                    if (field.value != undefined) {
                        input += field.value;
                    }
                    input += "</textarea>";
                    if (field.required) {
                        input += " *";
                    }
                    break;
                case "select":
                    input = "<select style='width: "+field.width+"px' id='" + _id(field.id) + "' ";
                    if (field.readonly) {
                        input += "disabled "
                    }
                    input += ">";
                    for (var j in field.options) {
                        var item = field.options[j];

                        input += "<option value='" + item.value + "' ";
                        if (item.value == field.value) {
                            input += "selected='selected'"
                        }
                        input += ">" + item.label + "</option>";

                    }
                    input += "</select>";
                    break;
                case "password":
                    input = "<input type='password' id='" + _id(field.id) + "' ";
                    if (field.value != undefined) {
                        input += "value='" + field.value + "' ";
                    }
                    input += " /> *";
                    break;
                case "radio":
                    input = _hidden_field(field.id, field.value);
                    var k=1;
                    for (var j in field.options) {
                        var item = field.options[j];
                        input += "<input type='radio' name='" + _id(field.id) + "'  value='" + item.value + "' ";
                        if (item.value == field.value) {
                            input += "checked='true' ";
                        }
                        input += "/>" + item.label + " &nbsp;"

                        if(k%field.cols==0){
                            input += "<br/>"
                        }
                        k++;
                    }
                    break;

                case  "checkbox":
                    input = _hidden_field(field.id, field.value);
                    var k = 1;
                    for (var j in field.options) {
                        var item = field.options[j];
                        input += "<input type='checkbox' name='" + _id(field.id) + "' value='" + item.value + "' ";
                        if (item.selected) {
                            input += "checked='true' ";
                        }
                        input += "/>" + item.label + " &nbsp;";

                        if(k%field.cols==0){
                            input += "<br/>"
                        }
                        k++;
                    }
                    break;

                default:
                    input = _hidden_field(field.id, field.value);
                    break;
            }
            content += _field(field.id, field.label, input);
        }

        if (form.captcha) {
            var input = "<input type='text'  style='width: 80px;' id='" + _id("captcha") + "'/>* &nbsp;";
            input += "<img id='"+_id('captcha_img')+"' src='/captcha.jpg?_=" + Math.random() + "' alt='点击更换验证码'/>";
            content += _field("captcha", "验证码", input);

        }

        var reload_captcha = function(){
            $('img#'+_id('captcha_img')).attr("src", "/captcha.jpg?_="+Math.random());
        };

        content += _button_group(form.buttons);
        content += "</fieldset></form>";
        //alert(content);
        new HtmlDiv("fm-"+form.id, content);
        $('img#'+_id('captcha_img')).click(reload_captcha);
        $('button#'+_id("reset")).click(function(){
            for (var i in form.fields) {
                var field = form.fields[i];
                switch (field.type){
                    case "text":
                    case "password":
                    case "textarea":
                        $("input#"+_id(field.id)).val(field.value == undefined ? "":field.value);
                        break;
                    default:
                        break;
                }
            }
            if(form.captcha){
                $("input#"+_id("captcha")).val('');
                reload_captcha();
            }
        });

        $('button#'+_id("submit")).click(function(){
            var data = {};
            for (var i in form.fields) {
                var field = form.fields[i];
                switch (field.type){
                    case "text":
                    case "textarea":
                    case "password":
                        data[field.id] = $('input#'+_id(field.id)).val();
                    default:
                        break;
                }
            }
            if(form.captcha){
                data['captcha'] = $('input#'+_id('captcha')).val();
            }
            new Ajax(form.action, "POST", data);
            reload_captcha();
        });

    };

    _init();
}

function HtmlDiv(id, content){
    var _init = function(){
        var root ="div#"+id;
        if($(root).length == 0){
            $("div#gl_root").append("<div id='"+id+"'></div>");
        }
        $(root).html(content);
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
