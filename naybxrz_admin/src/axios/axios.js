import axios from 'axios';

const instance = axios.create({
    //baseURL: 'http://192.168.64.4:30382'
    baseURL: 'http://localhost:9292'
});

export let ordersUrl = 'http://localhost:8686/';

export default instance;