function showQQLoginBar1() {
    QC.Login.signOut();
}
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
