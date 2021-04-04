import axios from 'axios';

const auth = axios.create({
    baseURL: process.env.REACT_APP_URL
});

export default auth;
