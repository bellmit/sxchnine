import axios from 'axios';

const instance = axios.create({
    baseURL: process.env.REACT_APP_URL
});

instance.CancelToken = axios.CancelToken;
instance.isCancel = axios.isCancel;

//instance.defaults.headers.common['Authorization'] = 'Bearer ' + store.getState().authentication.data.access_token //localStorage.getItem("access_token");

export default instance;