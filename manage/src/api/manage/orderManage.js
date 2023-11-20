import request from "@/utils/request";

/**
 * 批量删除充值订单接口
 * @param ids
 * @returns {*}
 */
export function deleteBatchRechargeRecords(ids){
    return request({
        url: '/rechargeRecords/delete/batch',
        method: 'delete',
        data: ids
    })
}



/**
 * 删除充值订单接口
 * @param id
 * @returns {*}
 */
export function deleteRechargeRecord(id){
    return request({
        url: '/rechargeRecords/delete/' + id,
        method: 'delete',
    })
}



/**
 * 分页查询充值订单信息接口
 * @param pageNum
 * @param pageSize
 * @param user_username
 * @returns {*}
 */
export function loadRechargeRecords(pageNum, pageSize, user_username) {
    return request({
        url: '/rechargeRecords/selectByPage',
        method:'get',
        params: {
            pageNum: pageNum,
            pageSize: pageSize,
            user_username: user_username
        }
    })
}
