import axios from 'axios';

const instance = axios.create({
    baseURL: 'https://api.naybxrz.com'
    //baseURL: 'http://localhost:9090'
    //baseURL: 'http://192.168.1.66:9090'
});

instance.CancelToken = axios.CancelToken;
instance.isCancel = axios.isCancel;

//instance.defaults.headers.common['Authorization'] = 'Bearer ' + store.getState().authentication.data.access_token //localStorage.getItem("access_token");

export default instance;