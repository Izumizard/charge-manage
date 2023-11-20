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
