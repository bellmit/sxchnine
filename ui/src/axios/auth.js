import axios from 'axios';

const auth = axios.create({
    baseURL: 'https://api.naybxrz.com'
    //baseURL: 'http://localhost:9090'
    //baseURL: 'http://192.168.1.67:9090'
});

//auth.defaults.headers.common['Authorization'] = 'Basic YXBpLWdhdGV3YXktdXNlcjpwYXNzdzByZA==';

//auth.defaults.headers.common['Content-Type'] = 'application/x-www-form-urlencoded';
//auth.defaults.headers.post['Access-Control-Allow-Origin'] = '*';
//auth.defaults.headers.post['Access-Control-Allow-Headers'] = 'Origin, X-Requested-With, Content-Type, Accept, Authorization';

export default auth;
