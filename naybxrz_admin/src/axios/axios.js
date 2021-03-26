import axios from 'axios';

const instance = axios.create({
    baseURL: 'https://api.naybxrz.com'
    //baseURL: 'http://localhost:9292'
});

//export let ordersUrl = 'http://localhost:8686/';
export let ordersUrl = 'http://order-service:8080/';

export default instance;