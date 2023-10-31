import request from "@/utils/request";

/**
 * 用户登录接口
 * @param code
 * @param username
 * @param password
 * @returns {*}
 */
export function login({ code, username, password }) {
    return request({
        url: 'user/login',
        method: 'post',
        data: {
            code,
            username,
            password,
        }
    })
}



/**
 * 管理员信息获取接口
 * @returns {*}
 */

export function loadOfAdmin() {
    return request({
        url: '/user/listOfAdmin',
        method: 'get',
    })
}



/**
 * 发送邮箱验证码接口
 */
export function sendEmail(email, type) {
    return request({
        url: '/user/email/' + email + '/' + type,
        method: 'get'
    })
}



/**
 * 重置密码接口
 */
export function resetPwd({email, code}){
    return request({
        url: '/user/reset',
        method: 'put',
        data:{
            email,
            code,
        }
    })
}



/**
 * 用户注册接口
 */
export function  userRegister(user){
    return request({
        url:'/user/register',
        method: 'post',
        data: user,
    })
}
