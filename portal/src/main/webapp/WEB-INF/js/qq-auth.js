function showQQLoginBar() {

    QC.Login(
        {
            //插入按钮的节点id，必选
            btnId: "qqAuthBar",
            //用户需要确认的scope授权项，可选，默认all
            scope: "all",
            //按钮尺寸，可用值[A_XL| A_L| A_M| A_S|  B_M| B_S| C_S]，可选，默认B_S
            size: "B_S"
        },
        function (reqData, opts) {
            QC.Login.getMe(function (openId, accessToken) {
                new Ajax(
                    "/oauth/qq",
                    'POST',
                    {
                        token: accessToken,
                        id: openId,
                        name: reqData.nickname,
                        logo: reqData.figureurl_qq_1
                    },
                    function (result) {
                        if (result.ok) {
                            $("li#qqAuthBar").hide();
                            $("li#googleAuthBar").hide();
                            draw_person_bar(function () {
                                if (window.confirm("您确认要退出QQ登录么？")) {
                                    QC.Login.signOut();
                                    //root.show();
                                    window.location.href = "/personal/logout";
                                }
                            });
                        }
                    });
            });

        });

}
function showQQLoginBar1() {
    QC.Login(
        {
            //插入按钮的节点id，必选
            btnId: "qqAuthBar",
            //用户需要确认的scope授权项，可选，默认all
            scope: "all",
            //按钮尺寸，可用值[A_XL| A_L| A_M| A_S|  B_M| B_S| C_S]，可选，默认B_S
            size: "B_S"
        },
        function (reqData, opts) {
            //$("li#qqAuthBar").hide();


            /*
             QC.Login.getMe(function (openId, accessToken) {
             QC.api("get_user_info", {}).success(function (user) {
             new Ajax(
             "/oauth/qq",
             'POST',
             {
             token: accessToken,
             id: openId,
             name: user.data.nickname,
             logo: user.data.figureurl_qq_1
             },
             function (result) {
             if (result.ok) {
             $("li#qqAuthBar").hide();
             draw_person_bar();
             }
             });
             });

             });
             */

            /*
             $("li#qqLoginBar").hide();
             draw_person_bar();
             //checkQQLogin();
             var _temp ='欢迎你，<img src="{figureurl}" class="navbar-icon" /> {nickname}' + '<b class="caret"></b>';
             $("li#personalBar a.dropdown-toggle").html(QC.String.format(_temp, {
             nickname: QC.String.escHTML(reqData.nickname), //做xss过滤
             figureurl: reqData.figureurl_qq_1
             }));
             var logout =$("a#personal_bar-logout");
             logout.unbind();
             logout.click(function(){
             if (window.confirm("您确认要退出系统么？")) {
             QC.Login.signOut();
             window.location.href = "/personal/logout";
             }
             });
             */
        },
        function (opts) {
            //alert('注销QQ登录成功');
            window.location.href = "/personal/logout";
        }
    );
}