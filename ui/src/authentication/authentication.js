/*
import axios from 'axios';

axios.defaults.headers.common['Authorization'] = 'Basic Y2xpZW50OnNlY3JldA==';
axios.defaults.headers.common['Content-Type'] = 'application/x-www-form-urlencoded';
axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';


export const auth = axios.get('http://localhost:8080/auth/realms/api-gateway/protocol/openid-connect/token?grant_type=client_credentials&client_id=api-gateway-client&client_secret=c8e6e1c0-4440-4d42-bda7-0ca48e8d6412&username=api-gateway-user&password=passw0rd&scope=all',
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

*/
