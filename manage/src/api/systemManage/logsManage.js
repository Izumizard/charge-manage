import request from "@/utils/request";



/**
 * 批量删除系统日志接口
 * @param ids
 * @returns {*}
 */
export function deleteBatch(ids){
    return request({
        url: '/logs/delete/batch',
        method: 'delete',
        data: ids
    })
}



/**
 * 删除系统日志接口
 * @param id
 * @returns {*}
 */
export function deleteLogs(id){
    return request({
        url: '/logs/delete/' + id,
        method: 'delete',
    })
}



/**
 * 分页查询用户信息接口
 * @param pageNum
 * @param pageSize
 * @param operation
 * @returns {*}
 */
export function loadLogs(pageNum, pageSize, operation) {
    return request({
        url: '/logs/selectByPage',
        method:'get',
        params: {
            pageNum: pageNum,
            pageSize: pageSize,
            operation: operation
        }
    })
}
