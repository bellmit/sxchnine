import axios from 'axios';

const auth = axios.create({
    baseURL: 'https://api.naybxrz.com'
    //baseURL: 'http://localhost:9292'
});

export default auth;
