import * as actionTypes from './actionTypes';
import auth from '../../axios/auth';

export const authenticate = () => {
    return dispatch => {
        auth.get('/authenticate')
            .then(response => {
                dispatch(authenticateSuccess(response.data));
                localStorage.setItem("access_token", response.data["access_token"]);
                localStorage.setItem("refresh_token", response.data["refresh_token"]);
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

