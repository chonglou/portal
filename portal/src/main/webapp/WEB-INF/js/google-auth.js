function loginCallback(authResult){
    if (authResult['access_token']) {

    }
    else{
        console.log('存在错误：' + authResult['error']);
    }
}