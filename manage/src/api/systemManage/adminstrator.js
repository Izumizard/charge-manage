import request from "@/utils/request";

/**
 * 更新用户信息接口
 * @param data
 * @returns {*}
 */
export function updateUser(data){
    return request({
        url: '/user/update',
        method: 'put',
        data: data
    });
}
