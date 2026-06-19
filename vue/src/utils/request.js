import axios from "axios";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const request = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 30000
})

const clearLogin = () => {
    localStorage.removeItem('xm-user')
    if (router.currentRoute.value.path !== '/login') {
        router.push('/login')
    }
}

request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    const user = JSON.parse(localStorage.getItem("xm-user") || '{}')
    if (user.token) {
        config.headers['token'] = user.token
    }
    return config
}, error => {
    return Promise.reject(error)
});

request.interceptors.response.use(
    response => {
        let res = response.data;
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res;
        }
        if (res && res.code === '401') {
            ElMessage.error(res.msg || '\u767b\u5f55\u5df2\u8fc7\u671f')
            clearLogin()
        } else if (res && res.code === '403') {
            ElMessage.error(res.msg || '\u65e0\u6743\u9650\u8bbf\u95ee')
        }
        return res;
    },
    error => {
        const res = error.response;
        if (!res) {
            ElMessage.error('\u7f51\u7edc\u5f02\u5e38\uff0c\u8bf7\u68c0\u67e5\u670d\u52a1\u662f\u5426\u542f\u52a8');
        } else if (res.status === 401) {
            ElMessage.error(res.data?.msg || '\u767b\u5f55\u5df2\u8fc7\u671f');
            clearLogin();
        } else if (res.status === 403) {
            ElMessage.error(res.data?.msg || '\u65e0\u6743\u9650\u8bbf\u95ee');
        } else if (res.status === 500 && res.data && res.data.msg) {
            ElMessage.error(res.data.msg);
        } else if (res.status === 404) {
            ElMessage.error('\u672a\u627e\u5230\u8bf7\u6c42\u63a5\u53e3');
        } else {
            ElMessage.error('\u7cfb\u7edf\u5f02\u5e38\uff0c\u8bf7\u67e5\u770b\u540e\u53f0\u63a7\u5236\u53f0\u62a5\u9519');
        }
        return Promise.reject(error);
    }
);

export default request
