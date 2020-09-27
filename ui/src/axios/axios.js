import axios from 'axios';

const instance = axios.create({
    //baseURL: 'http://192.168.64.4:30382'
    baseURL: 'http://localhost:9090'
});

instance.CancelToken = axios.CancelToken;
instance.isCancel = axios.isCancel;

instance.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem("access_token");

/*
axios.defaults.headers.post['Content-Type'] ='application/json;charset=utf-8';
*/
/*
axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';
*/

export default instance;