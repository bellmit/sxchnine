import axios from 'axios';

axios.defaults.headers.common['Authorization'] = 'Basic Y2xpZW50OnNlY3JldA==';
axios.defaults.headers.common['Content-Type'] = 'application/x-www-form-urlencoded';
axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';


export const auth = axios.get('http://localhost:9090/api/authentication/oauth/token?grant_type=password&client_id=client&client_secret=secret&username=toto2%40gmail.com&password=toto1234&scope=all',
    {
        headers: {
            "Content-Type": 'application/x-www-form-urlencoded;',
        }
    }).then(response => {
        console.log("response receiverd ");
        console.log(response.data);
        localStorage.setItem("access_token", response.data["access_token"]);
        localStorage.setItem("refresh_token", response.data["refresh_token"]);
});

