import axios from "axios";
import {ElMessage} from "element-plus"; //弹窗提示组件

const request = axios.create({
    baseURL: 'http://localhost:9090',
    timeout: 30000 //后台接口超时时间
})

//request拦截器
//可以自请求发送前对请求做一些处理
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
//配置请求头，从本地存储获取token
    let user = JSON.parse(localStorage.getItem('code_user') || '{}')
    config.headers['token'] = user.token
    return config
}, error => {
    return Promise.reject(error)
});

//response拦截器
//可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res //转化为Json，兼容服务器端返回的字符串数据
        }
        //添加返回code为401的处理
        if(res.code === '401'){
            ElMessage.error(res.msg)
        } else {
            return res;
        }
    },
    error => {
        if (error.response.status === 404){
            ElMessage.error('未找到请求接口')
        } else if (error.response.status === 500) {
            ElMessage.error('系统异常，请查看后端控制台报错')
        } else {
            console.error(error.message)
        }
    }
)
export default request //返回request对象