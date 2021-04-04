import axios from 'axios';

const instance = axios.create({
    baseURL: process.env.REACT_APP_URL
});

export let ordersUrl = 'http://order-service:8080/';

export default instance;