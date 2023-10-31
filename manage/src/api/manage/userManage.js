import request from "@/utils/request";

/**
 * 批量删除用户接口
 * @param ids
 * @returns {*}
 */
export function deleteBatch(ids){
    return request({
        url: '/user/delete/batch',
        method: 'delete',
        data: ids
    })
}



/**
 * 删除用户接口
 * @param id
 * @returns {*}
 */
export function deleteUser(id){
    return request({
        url: '/user/delete/' + id,
        method: 'delete',
    })
}



/**
 * 保存编辑的用户信息或者新增用户接口
 * @param data
 * @returns {*}
 */
export function save(data) {
    const url = data.id ? '/user/update' : '/user/add';
    const method = data.id ? 'PUT' : 'POST';
    return request({
        url: url,
        method: method,
        data: data
    });
}



/**
 * 分页查询用户信息接口
 * @param pageNum
 * @param pageSize
 * @param username
 * @param name
 * @returns {*}
 */
export function loadUser(pageNum, pageSize, username, name) {
    return request({
        url: '/user/selectByPage',
        method:'get',
        params: {
            pageNum: pageNum,
            pageSize: pageSize,
            username: username,
            name: name
        }
    })
}
