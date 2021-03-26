import axios from 'axios';

const auth = axios.create({
    //baseURL: 'http://192.168.64.4:30922'
    baseURL: 'http://localhost:9292'
});

export default auth;
