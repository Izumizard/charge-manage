import request from "@/utils/request";

/**
 * 批量删除轮播图接口
 * @param ids
 * @returns {*}
 */
export function deleteBatchSwiper(ids) {
    return request({
        url: '/swiper/delete/batch',
        method: 'delete',
        data: ids
    })
}



/**
 * 更改轮播图展示状态接口
 * @param id
 * @param status
 * @returns {*}
 */
export function updateSwiperStatus(id, status) {
    return request({
        url: '/swiper/update',
        method: 'put',
        data: {
            id: id,
            status: status
        }
    })
}



/**
 * 删除轮播图接口
 * @param id
 * @returns {*}
 */
export function deleteSwiper(id) {
    return request({
        url: '/swiper/delete/' + id,
        method: 'delete',
    })
}



/**
 * 保存编辑的轮播图信息或者新增轮播图接口
 * @param data
 * @returns {*}
 */
export  function saveSwiper(data) {
    const url = data.id ? '/swiper/update' : '/swiper/add';
    const method = data.id ? 'PUT' : 'POST';
    return request({
        url: url,
        method: method,
        data: data
    });
}


/**
 * 分页查询轮播图信息接口
 * @param pageNum
 * @param pageSize
 * @param banner
 * @returns {*}
 */
export function loadSwiper(pageNum, pageSize, banner) {
    return request({
        url: '/swiper/selectByPage',
        method:'get',
        params: {
            pageNum: pageNum,
            pageSize: pageSize,
            banner: banner
        }
    })
}



// 通知管理接口

/**
 * 更改通知信息展示状态接口
 * @param id
 * @param open
 * @returns {*}
 */
export function updateOpen(id, open) {
    return request({
        url: '/notice/update',
        method: 'put',
        data: {
            id: id,
            open: open
        }
    })
}



/**
 * 批量删除通知信息接口
 * @param ids
 * @returns {*}
 */
export function deleteBatchNotice(ids) {
    return request({
        url: '/notice/delete/batch',
        method: 'delete',
        data: ids
    })
}



/**
 * 删除通知信息接口
 * @param id
 * @returns {*}
 */
export function deleteNotice(id) {
    return request({
        url: '/notice/delete/' + id,
        method: 'delete',
    })
}



/**
 * 保存编辑的轮播图信息或者新增轮播图接口
 * @param data
 * @returns {*}
 */
export  function saveNotice(data) {
    const url = data.id ? '/notice/update' : '/notice/add';
    const method = data.id ? 'PUT' : 'POST';
    return request({
        url: url,
        method: method,
        data: data
    });
}



/**
 * 分页查询通知信息接口
 * @param pageNumOne
 * @param pageSizeOne
 * @param title
 * @returns {*}
 */
export function loadNotice(pageNumOne, pageSizeOne, title) {
    return request({
        url: '/notice/selectByPage',
        method:'get',
        params: {
            pageNum: pageNumOne,
            pageSize: pageSizeOne,
            title: title
        }
    })
}
