import auth from '../../axios/auth';
import * as actionTypes from './actions';

export const authenticate = () => {
    return dispatch => {
        auth.get('/auth/authenticate', {
            headers: {
                'X-DOMAIN': process.env.REACT_APP_DOMAIN,
                'X-AUTHORITY': Math.round(new Date().getTime()/1000) + process.env.REACT_APP_AUTHORITY
            }
        })
            .then(response => {
                dispatch(authenticateSuccess(response.data.slice(0, response.data.length - process.env.REACT_APP_RATE)));
            })
            .catch(error => {
                dispatch(authenticateError(error));
            })
    }
}

export const authenticateSuccess = (data) => {
    return {
        type: actionTypes.AUTHENTICATION_SUCCESS,
        data: data
    };
}
export const authenticateError = (error) => {
    return {
        type: actionTypes.AUTHENTICATION_ERROR,
        error: error
    };
}

