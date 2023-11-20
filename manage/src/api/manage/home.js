import request from "@/utils/request";

/**
 * 查询全部轮播图信息接口
 * @returns {*}
 */
export function swiper(){
    return request({
        url: '/swiper/selectAll',
        method: 'get'
    })
}

export function notice() {
    return request({
        url: '/notice/selectUserData',
        method: 'get'
    })
}
