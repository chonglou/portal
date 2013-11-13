function getUserInfo() {
    //从页面收集OpenAPI必要的参数。get_user_info不需要输入参数，因此paras中没有参数
    var paras = {};

    //用JS SDK调用OpenAPI
    QC.api("get_user_info", paras)
        //指定接口访问成功的接收函数，s为成功返回Response对象
        .success(function (s) {
            //成功回调，通过s.data获取OpenAPI的返回数据
            alert("获取用户信息成功！当前用户昵称为：" + s.data.nickname);
        })
        //指定接口访问失败的接收函数，f为失败返回Response对象
        .error(function (f) {
            //失败回调
            alert("获取用户信息失败！");
        })
        //指定接口完成请求后的接收函数，c为完成请求返回Response对象
        .complete(function (c) {
            //完成请求回调
            alert("获取用户信息完成！");
        });
}

function checkQQLogin(){
    if(QC.Login.check()){
        QC.Login.getMe(function(openId, accessToken){
            QC.api("get_user_info", {}).success(function(user){
                new Ajax(
                    "/oauth/qq",
                    'POST',
                    {
                        token:accessToken,
                        id:openId,
                        name:user.data.nickname
                    },
                    function(){});
            });

        });
    }
}

function showQQLoginBar() {
    //调用QC.Login方法，指定btnId参数将按钮绑定在容器节点中
    QC.Login({
            //btnId：插入按钮的节点id，必选
            btnId: "qqLoginBar",
            //用户需要确认的scope授权项，可选，默认all
            scope: "all",
            //按钮尺寸，可用值[A_XL| A_L| A_M| A_S|  B_M| B_S| C_S]，可选，默认B_S
            size: "B_S"
        }, function (reqData, opts) {//登录成功

            //根据返回数据，更换按钮显示状态方法
            $("li#qqLoginBar").css("dropdown");
            $("li#personalBar").hide();
            var dom = document.getElementById(opts['btnId']);
            var _logoutTemplate = '<a href="#" class="dropdown-toggle" data-toggle="dropdown">' +
                '<img src="{figureurl}" class="navbar-icon" /> {nickname}' + '<b class="caret"></b></a>';
            _logoutTemplate +='<ul class="dropdown-menu">';
            _logoutTemplate +='<li><a href="/personal/self">用户中心</a></li>';
            _logoutTemplate +='<li class="divider"></li>';
            _logoutTemplate +='<li><a href="javascript:QC.Login.signOut();">退出登录</a></li>';
            _logoutTemplate +='<li class="divider"></li>';
            _logoutTemplate +='</ul>';

            dom && (dom.innerHTML = QC.String.format(_logoutTemplate, {
                nickname: QC.String.escHTML(reqData.nickname), //做xss过滤
                figureurl: reqData.figureurl_qq_1
            }));

        }, function (opts) {//注销成功
            alert('注销QQ登录成功');
            window.location.href="/personal/logout";
        }
    );
    /*
     QC.Login({
     btnId:"qqLoginBtn"
     });
     */
}