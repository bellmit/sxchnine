import axios from 'axios';

const instance = axios.create({
    //baseURL: 'http://192.168.64.4:30382'
    baseURL: 'http://localhost:9090'
});

export default instance;