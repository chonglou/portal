function Ajax(url, type, data, success, async, parent) {
    var _init = function () {
        if (type == undefined) {
            type = "GET";
        }
        if (data == undefined) {
            data = [];
        }
        if(parent == undefined){
            parent = "gl_root";
        }
        if (success == undefined) {
            success = function (result) {
                if (result.ok) {
                    switch (result.type) {
                        case "form":
                            new FormWindow(result, parent);
                            break;
                        case "grid":
                            new GridWindow(result, parent);
                            break;
                        case "redirect":
                            window.location.href = result.data[0];
                            break;
                        case "message":
                            new MessageDialog("操作成功", "success");
                            break;
                        default:
                            new MessageDialog("尚未支持");
                    }
                }
                else if (result.data) {
                    new MessageDialog(result.data);
                }
                else {
                    $("div#" + parent).html(result);
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
            //dataType: "json",
            cache: false,
            error: function () {
                new MessageDialog("HTTP请求失败!");
            }
        });
    };
    _init();
}

function FormWindow(form, parent) {
    var _form_id;
    var _field = function (id, label, input) {
        if (input == undefined) {
            return "<h4 class='glyphicon glyphicon-ok-sign'>" + label + "</h4>";
        }

        if (label == undefined) {
            return input;
        }
        var content = "<div class='form-group'><label ";
        if (id != undefined) {
            content += " for='fm-" + _form_id + "-" + id + "' ";
        }
        content += " class='col-lg-2 control-label'>" + label + "：</label>";
        content += "<div class='col-lg-10'>";
        content += input;
        content += "</div></div>";
        return content;
    };

    var _id = function (id) {
        return "fm-" + _form_id + "-" + id;
    };
    var _hidden_field = function (id, value) {
        var s = "<input type='hidden' id='" + _id(id) + "'";
        if (value != undefined) {
            s += "value='" + value + "'";
        }
        s += "/>";
        return   s;
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
        var content = "<div class='form-group'><div class='col-lg-2'></div>";
        content += "<div class='col-lg-10 btn-group'>";
        content += _button("submit", "提交", "danger");
        content += _button("reset", "重写", "info");
        for (var i in buttons) {
            var btn = form.buttons[i];
            content += _button(btn.id, btn.label, btn.type);
        }
        content += "</div></div>";
        return content;
    };
    var _init = function () {
        _form_id = form.id;

        var content = "<form class='form-horizontal' method='" + form.method + "'  action='" + form.action + "'>";
        content += "<fieldset><legend><h3 class='glyphicon glyphicon-save'>" + form.title + "</h3></legend>";
        content += _hidden_field("created", form.created);
        for (var i in form.fields) {
            var field = form.fields[i];
            var input;
            switch (field.type) {
                case "text":
                    input = "<input class='form-control' style='width: " + field.width + "px' type='text' id='" + _id(field.id) + "' ";
                    if (field.value != undefined) {
                        input += "value='" + field.value + "' ";
                    }
                    if (field.readonly) {
                        input += "readonly ";
                    }
                    input += " />";
                    if (field.required) {
                        field.label += "(*)";
                    }
                    break;
                case "textarea":

                    input = "<textarea ";
                    if (!field.html) {
                        input += "class='form-control'";
                    }
                    input += " id='" + _id(field.id) + "' style='width: " + field.width + "px;height: " + field.height + "px;' ";
                    if (field.readonly) {
                        input += "readonly ";
                    }
                    input += ">";
                    if (field.value != undefined) {
                        input += field.value;
                    }
                    input += "</textarea>";
                    if (field.required) {
                        field.label += "(*)";
                    }
                    break;
                case "splitter":
                    input = undefined;
                    break;
                case "select":
                    input = "<select style='width: "
                        + field.width
                        + "px;' class='form-control' id='"
                        + _id(field.id) + "' ";
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
                        input += ">" + item.label + " &nbsp; </option>";

                    }
                    input += "</select>";
                    break;
                case "password":
                    input = "<input style='width: " +
                        field.width +
                        "px' class='form-control' type='password' id='"
                        + _id(field.id) + "' ";
                    if (field.value != undefined) {
                        input += "value='" + field.value + "' ";
                    }
                    input += " />";
                    field.label += "(*)";
                    break;
                case "radio":
                    //input = _hidden_field(field.id, field.value);
                    input = "";
                    var k = 1;
                    for (var j in field.options) {
                        var item = field.options[j];
                        input += "<label class='radio-inline'><input class='form-control' type='radio' name='" + _id(field.id) + "'  value='" + item.value + "' ";
                        if (field.readonly) {
                            input += "disabled ";
                        }
                        if (item.value == field.value) {
                            input += "checked='true' ";
                        }
                        input += "/>" + item.label + "</label>";

                        if (k % field.cols == 0) {
                            input += "<br/>"
                        }
                        k++;
                    }
                    break;

                case  "checkbox":
                    //input = _hidden_field(field.id, field.value);
                    input = "";
                    var k = 1;
                    for (var j in field.options) {
                        var item = field.options[j];
                        input += "<label class='checkbox-inline'><input  type='checkbox' name='" + _id(field.id) + "' value='" + item.value + "' ";
                        if (item.selected) {
                            input += "checked='true' ";
                        }
                        input += "/>" + item.label + " </label>";

                        if (k % field.cols == 0) {
                            input += "<br/>"
                        }
                        k++;
                    }
                    break;
                case "agree":
                    //input = "<textarea disabled style='width: 400px;height: 100px' >" + field.text + "</textarea>";
                    //input += "<br/>"
                    input = field.text;
                    input += "<div class='checkbox'><label><input id='"
                        + _id(field.id)
                        + "' type='checkbox'>我同意</label></div>";
                    field.label += "(*)";
                    break;
                default:
                    input = _hidden_field(field.id, field.value);
                    break;
            }
            content += _field(field.id, field.label, input);
        }

        if (form.captcha) {
            var input = "<div class='form-group'>";
            input += "<label class='col-lg-2 control-label' for='" + _id('captcha') + "'>验证码(*)：</label>";
            switch (gl_captcha) {
                case "kaptcha":
                    input += "<div class='col-lg-2'><input class='form-control' type='text'  style='width: 80px;' id='"
                        + _id("captcha") + "'/></div>";
                    input += "<div class='col-lg-8'><img id='"
                        + _id('captcha_img')
                        + "' src='/captcha?_="
                        + Math.random()
                        + "' alt='点击更换验证码'/></div>";
                    break;
                case "reCaptcha":
                    input += "<div class='col-lg-10' id='" + _id('captcha') + "'></div>";
                    break;
            }
            input += "</div>";
            content += input;

        }

        var reload_captcha = function () {
            switch (gl_captcha) {
                case "kaptcha":
                    $('img#' + _id('captcha_img')).attr("src", "/captcha?_=" + Math.random());
                    break;
            }
        };

        content += _button_group(form.buttons);
        content += "</fieldset></form>";
        //console.log(content);
        new HtmlDiv("fm-" + form.id, content, parent);


        if (form.captcha) {
            switch (gl_captcha) {
                case "kaptcha":
                    $('img#' + _id('captcha_img')).click(reload_captcha);
                    break;
                case "reCaptcha":
                    showRecaptcha(_id("captcha"));
                    break;
            }
        }
        for (var i in form.fields) {
            var field = form.fields[i];
            if (field.type == "textarea" && field.html) {
                var editor = new UE.ui.Editor();
                editor.render(_id(field.id));
                //uParse(_id(field.id), {'liiconpath':'/ueditor/themes/ueditor-list/'});
                //UE.getEditor('myEditor')
            }
        }

        $('button#' + _id("reset")).click(function () {
            for (var i in form.fields) {
                var field = form.fields[i];
                switch (field.type) {
                    case "text":
                        $("input#" + _id(field.id)).val(field.value == undefined ? "" : field.value);
                        break;
                    case "password":
                        $("input#" + _id(field.id)).val("");
                        break;
                    case "textarea":
                        $("textarea#" + _id(field.id)).val(field.value == undefined ? "" : field.value);
                        break;
                    case "radio":
                        $("input:radio[name='" + _id(field.id) + "'][value='" + field.value + "']").prop('checked', true);
                        break;
                    case "select":
                        $("select#" + _id(field.id)).val(field.value == undefined ? "" : field.value);
                        break;
                    case "agree":
                        $("input#" + _id(field.id)).prop('checked', false);
                        break;
                    default:
                        break;
                }
            }
            if (form.captcha) {
                switch (gl_captcha) {
                    case "kaptcha":
                        $("input#" + _id("captcha")).val('');
                        reload_captcha();
                        break;
                    case "reCaptcha":
                        $("input#recaptcha_response_field").val('');
                        break;
                }
            }
        });

        $('button#' + _id("submit")).click(function () {
            var data = {};
            for (var i in form.fields) {
                var field = form.fields[i];
                switch (field.type) {
                    case "hidden":
                    case "text":
                        data[field.id] = $('input#' + _id(field.id)).val();
                        break;
                    case "password":
                        data[field.id] = $('input#' + _id(field.id)).val();
                        $('input#' + _id(field.id)).val('');
                        break;
                    case "textarea":
                        data[field.id] = $('textarea#' + _id(field.id)).val();
                        break;
                    case "radio":
                        data[field.id] = $("input[name='" + _id(field.id) + "']:checked").val();
                        break;
                    case "select":
                        data[field.id] = $('select#' + _id(field.id)).val();
                        break;
                    case "agree":
                        if ($("input#" + _id(field.id)).is(':checked')) {
                            data[field.id] = true;
                        }
                        break;
                    case "checkbox":
                        var vv = [];
                        $("input[name='" + _id(field.id) + "']:checked").each(function () {
                            vv.push($(this).val());
                        });
                        data[field.id] = vv.join("-");
                        break;
                    default:
                        break;
                }
            }
            if (form.captcha) {
                switch (gl_captcha) {
                    case "kaptcha":
                        data['captcha'] = $('input#' + _id('captcha')).val();
                        break;
                    case "reCaptcha":
                        data['challenge'] = $('input#recaptcha_challenge_field').val();
                        data['captcha'] = $('input#recaptcha_response_field').val();
                        break;
                }
            }
            new Ajax(form.action, "POST", data, undefined, !form.captcha);
            reload_captcha();
        });

    };

    _init();
}


function HtmlDiv(id, content, parent) {
    var _init = function () {
        var root = "div#" + id;
        if ($(root).length == 0) {
            $("div#" + parent).append("<div id='" + id + "'></div>");
        }
        $(root).html(content);
    };
    _init();
}

function MessageDialog(messages, type, okFun) {
    var _init = function () {
        if (type == undefined) {
            type = "info";
        }
        var name;
        switch (type) {
            case "warning":
                name = "警告";
                break;
            case "success":
                name = "成功";
                break;
            case "danger":
                name = "错误";
                break;
            default:
                type = "info";
                name = "提示";
                break;
        }
        var content = "<div id='gl_modal' class='modal fade'><div class='modal-dialog'><div class='modal-content'>";
        content += "<div class='modal-header alert alert-" + type + "'><button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button><h4 class='modal-title'>";
        content += name + "</h4></div>";
        content += "<div class='modal-body'><p>";
        content += (messages instanceof Array ? messages.join("<br/>") : messages);
        content += "&hellip;</p></div>";
        content += "<div class='modal-footer'>";
        if (okFun == undefined) {
            content += "<button id='gl_modal_ok' type='button' class='btn btn-primary' data-dismiss='modal'>确定</button>";
        }
        else {
            content += "<button id='gl_modal_cancel' type='button' class='btn btn-default' data-dismiss='modal'>取消</button>";
            content += "<button  id='gl_modal_ok' type='button' class='btn btn-primary'>确定</button>";
        }

        content += "</div></div></div></div>";
        $("div#gl_message").html(content);
        if (okFun != undefined) {
            $("button#gl_modal_ok").click(okFun);
        }
        $('div#gl_modal').modal({backdrop: false});
    };
    _init();
}

function MessageDialog1(messages, type) {
    var _init = function () {
        var name;
        switch (type) {
            case "warning":
                name = "错误";
                break;
            case "success":
                name = "成功";
                break;
            case "info":
                name = "提示";
                break;
            default:
                type = "danger";
                name = "警告";
                break;
        }
        if (type == undefined) {
            type = "error";
        }
        $("div#gl_message").html("<div class='alert alert-" + type + "'><button type='button' class='close' data-dismiss='alert'>&times;</button><h4>" +
            name + "[" + (new Date()).pattern("yyyy-MM-dd hh:mm:ss.S") + "]：</h4>" + (messages instanceof Array ? messages.join("<br/>") : messages) + "</div>");

    };
    _init();
}
