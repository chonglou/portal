function show_card_carousel(id) {

    $('div#'+id).carousel({
        interval: 2000
    });
    /*
    $("div#" + id).jcarousel({
        auto: 2,
        scroll: 1,
        visible: 1,
        initCallback: function (carousel) {
            $("img[id^='" + id + "-']").each(function () {
                $(this).click(function () {
                    window.open($(this).attr('id').split('-')[1]);
                });
            });
            carousel.buttonNext.bind('click', function () {
                carousel.startAuto(0);
            });
            carousel.buttonPrev.bind('click', function () {
                carousel.startAuto(0);
            });
            carousel.clip.hover(function () {
                carousel.stopAuto();
            }, function () {
                carousel.startAuto();
            });
        },
        wrap: 'circular'
    });
    */
}
function show_card_fall(id) {
    $('div#' + id).masonry({
        itemSelector: '.fall-item'
    });
}

function object2html(obj) {
    var context = "<ol class='list-group'>";
    for (var k in obj) {
        var v = obj[k];
        var s;
        switch (typeof v) {
            case "number":
            case "string":
                s = v;
                break;
            case "object":
                s += object2html(v);
                break;
            default :
                s = "未知类型：" + k;
                break;
        }

        context += "<li class='list-group-item'>" + k + "：" + s + "</li>";
    }
    context += "</ol>";
    return context;
}




function GridWindow(grid, parent) {
    var _grid_id;
    var _id = function (id) {
        return "grid-" + _grid_id + "-" + id;
    };
    var _c_id = function (id) {
        return id.split('-')[3];
    };
    var _init = function () {
        _grid_id = grid.id;
        var content = "<h4>" + grid.name;
        if (grid.add) {
            content += "[<button title='新增' id='" + _id("add") + "'>新增</button>]";
        }
        content += "</h4><hr/><table width='98%' align='center'><thead><tr class='grid-tr'>";

        for (var i in grid.cols) {
            var col = grid.cols[i];
            content += "<td";
            if (col.width != undefined) {
                content += " width='" + col.width + "'";
            }
            content += "><b>" + col.label + "</b></td>"
        }
        if (grid.action) {
            content += "<td>操作</td>";
        }
        content += "</tr></thead><tbody>";
        for (var i = 0; i < grid.items.length;) {
            if (grid.action) {
                if (i % (grid.cols.length + 1) == 0) {
                    content += "<tr class='grid-tr'>";
                }
            }
            else {

                if (i % grid.cols.length == 0) {
                    content += "<tr class='grid-tr'>";
                }
            }
            content += "<td>" + grid.items[i] + "</td>";
            i++;
            if (grid.action) {
                if ((i + 1) % (grid.cols.length + 1) == 0) {
                    content += "<td class='grid-td-opt'>";
                    if (grid.view) {
                        content += "<button title='查看' id='" + _id("view") + "-" + grid.items[i] + "'>查看</button>";
                    }
                    if (grid.edit) {
                        content += "<button title='编辑' id='" + _id("edit") + "-" + grid.items[i] + "'>编辑</button>";
                    }
                    if (grid.delete) {
                        content += "<button title='删除' id='" + _id("delete") + "-" + grid.items[i] + "'>删除</button>";
                    }
                    content += "</td>";
                    content += "</tr>";
                    i++;
                }
            }
            else {
                if (i % (grid.cols.length) == 0) {
                    content += "</tr>";
                }
            }
        }
        content += "</tbody></table>";

        new HtmlDiv("grid-" + grid.id, content, parent);

        if (grid.action != undefined) {
            if (grid.add) {
                var addBtn = $("button#" + _id("add"));
                addBtn.addClass("btn btn-primary btn-mini");
                addBtn.click(function () {
                    new Ajax(grid.action + "/add");
                });
            }
            if (grid.view) {

                $("button[id^='" + _id("view") + "']").each(function () {
                    $(this).addClass("btn btn-info btn-mini");
                    $(this).click(function () {
                        new Ajax(grid.action + "/" + _c_id($(this).attr("id")), "PUT");
                    });
                });
            }
            if (grid.edit) {
                $("button[id^='" + _id("edit") + "']").each(function () {
                    $(this).addClass("btn btn-warning btn-mini");
                    $(this).click(function () {
                        new Ajax(grid.action + "/" + _c_id($(this).attr("id")));
                    });
                });
            }
            if (grid.delete) {
                $("button[id^='" + _id("delete") + "']").each(function () {
                    $(this).addClass("btn btn-danger btn-mini");
                    $(this).click(function () {
                        new Ajax(grid.action + "/" + _c_id($(this).attr("id")), "DELETE");
                    });
                });
            }
        }
    };
    _init();
}




function showRecaptcha(element) {
    Recaptcha.create(
        gl_reCaptcha_key, element, {
            theme: "red",
            callback: Recaptcha.focus_response_field}
    );
}

Date.prototype.pattern = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    var week = {
        "0": "/u65e5",
        "1": "/u4e00",
        "2": "/u4e8c",
        "3": "/u4e09",
        "4": "/u56db",
        "5": "/u4e94",
        "6": "/u516d"
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[this.getDay() + ""]);
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}
