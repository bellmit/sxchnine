import axios from 'axios';

const instance = axios.create({
    baseURL: 'http://localhost:53101'
});

instance.defaults.headers.common['AUTHORIZATION'] = 'TOKEN';

export default instance;