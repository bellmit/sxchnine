import axios from 'axios';

const auth = axios.create({
    baseURL: 'http://localhost:8081'
});

//auth.defaults.headers.common['Authorization'] = 'Basic YXBpLWdhdGV3YXktdXNlcjpwYXNzdzByZA==';

//auth.defaults.headers.common['Content-Type'] = 'application/x-www-form-urlencoded';
//auth.defaults.headers.post['Access-Control-Allow-Origin'] = '*';
//auth.defaults.headers.post['Access-Control-Allow-Headers'] = 'Origin, X-Requested-With, Content-Type, Accept, Authorization';

export default auth;
