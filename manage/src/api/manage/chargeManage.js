import request from "@/utils/request";

export function loadStation(pageNum, pageSize, station_name) {
    return request({
        url: '/station/selectByPage',
        method:'get',
        params: {
            pageNum: pageNum,
            pageSize: pageSize,
            station_name: station_name
        }
    })
}

export function  loadPort(pageNum, pageSize, station_name, port_status) {
    return request({
        url: '/port/selectByPage',
        method: 'get',
        params: {
            pageNum: pageNum,
            pageSize: pageSize,
            station_name: station_name,
            port_status: port_status,
        }
    })
}


export function updatePortStatus(id, newStatus) {
    return request({
        url: '/port/update',
        method: 'put',
        data: {
            id: id,
            port_status: newStatus
        }
    })
}


export function updateStationStatus(id, newStatus) {
    return request({
        url: '/station/update',
        method: 'put',
        data: {
            id: id,
            station_status: newStatus
        }
    })
}

/**
 * 删除站点接口
 * @param id
 * @returns {*}
 */
export function deleteStation(id){
    return request({
        url: '/station/delete/' + id,
        method: 'delete',
    })
}


/**
 * 批量删除电桩接口
 * @param ids
 * @returns {*}
 */
export function deleteStationBatch(ids){
    return request({
        url: '/station/delete/batch',
        method: 'delete',
        data: ids
    })
}


/**
 * 删除站点电桩接口
 * @param id
 * @returns {*}
 */
export function deletePort(id){
    return request({
        url: '/port/delete/' + id,
        method: 'delete',
    })
}


/**
 * 批量删除站点电桩接口
 * @param ids
 * @returns {*}
 */
export function deletePortBatch(ids){
    return request({
        url: '/port/delete/batch',
        method: 'delete',
        data: ids
    })
}


/**
 * 删除电价信息接口
 * @param id
 * @returns {*}
 */
export function priceDelete(id) {
    return request({
        url: '/electricity/delete/' + id,
        method: 'delete',
    })
}


/**
 * 批量删除电价信息接口
 * @param ids
 * @returns {*}
 */
export function deletePricesBatch(ids) {
    return request({
        url: '/electricity/delete/batch',
        method: 'delete',
        data: ids
    })
}


/**
 * 删除缩略图信息接口
 * @param id
 * @returns {*}
 */
export function pictureDelete(id) {
    return request({
        url: '/thumbnails/delete/' + id,
        method: 'delete',
    })
}


/**
 * 批量删除缩略图信息接口
 * @param ids
 * @returns {*}
 */
export function deletePicturesBatch(ids) {
    return request({
        url: '/thumbnails/delete/batch',
        method: 'delete',
        data: ids
    })
}


/**
 * 站点信息获取接口
 * @returns {*}
 */
export function loadOfStation() {
    return request({
        url: '/station/listOfStation',
        method: 'get',
    })
}


/**
 * 分页模糊查询电价信息接口
 * @param pageNum
 * @param pageSize
 * @param station_name
 * @returns {*}
 */
export function loadPrices(pageNum, pageSize, station_name) {
    return request({
        url: '/electricity/selectByPage',
        method: 'get',
        params: {
            pageNum: pageNum,
            pageSize: pageSize,
            station_name: station_name,
        }
    })
}

/**
 * 分页模糊查询缩略图信息接口
 * @param pageNum
 * @param pageSize
 * @param station_name
 * @returns {*}
 */
export function loadPicture(pageNum, pageSize, station_name) {
    return request({
        url: '/thumbnails/selectByPage',
        method: 'get',
        params: {
            pageNum: pageNum,
            pageSize: pageSize,
            station_name: station_name,
        }
    })
}


export function saveStation(data) {
    const url = data.id ? '/station/update' : '/station/add';
    const method = data.id ? 'PUT' : 'POST';
    return request({
        url: url,
        method: method,
        data: data
    })
}


export function savePort(data) {
    const url = data.id ? '/port/update' : '/port/add';
    const method = data.id ? 'PUT' : 'POST';
    return request({
        url: url,
        method: method,
        data: data
    })
}


export function savePrice(data) {
    const url = data.id ? '/electricity/update' : '/electricity/add';
    const method = data.id ? 'PUT' : 'POST';
    return request({
        url: url,
        method: method,
        data: data
    })
}


export function savePicture(data) {
    const url = data.id ? '/thumbnails/update' : '/thumbnails/add';
    const method = data.id ? 'PUT' : 'POST';
    return request({
        url: url,
        method: method,
        data: data
    })
}



