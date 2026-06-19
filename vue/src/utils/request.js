import axios from "axios";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const request = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 30000  // 后台接口超时时间
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let user = JSON.parse(localStorage.getItem("xm-user") || '{}')
    config.headers['token'] = user.token || ''
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
request.interceptors.response.use(
    response => {
        let res = response.data;
        if (res.code === '401') {
            ElMessage.error(res.msg);
            router.push('/login');
        }
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res;
        }
        return res;
    },
    error => {
        let res = error.response;
        if (res.status === 500 && res.data && res.data.msg) {
            // **如果后端返回了msg，则优先显示**
            ElMessage.error(res.data.msg);
        } else if (res.status === 404) {
            ElMessage.error('未找到请求接口');
        } else {
            ElMessage.error('系统异常，请查看后端控制台报错');
        }
        return Promise.reject(error);
    }
);


export default request
