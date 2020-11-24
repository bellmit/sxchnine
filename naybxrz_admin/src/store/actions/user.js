import axios from './../../axios/axios';
import {store} from "../../index";
import * as actions from './actions';

const setAxiosToken = () => {
    axios.defaults.headers.common['Authorization'] = 'Bearer ' + store.getState().authentication.data.access_token
};

export const authenticatedUserStart = (loading) => {
    return {
        type: actions.AUTHENTICATION_USER_START,
        loading: loading
    }
};

export const authenticatedUserError = (error) => {
    return {
        type: actions.AUTHENTICATION_USER_ERROR,
        error: error
    }
};

export const authenticatedUserSuccess = (user) => {
    return {
        type: actions.AUTHENTICATION_USER_SUCCESS,
        user: user
    }
};

export const authenticatedUserReset = (reset) => {
    return {
        type: actions.AUTHENTICATION_USER_RESET,
        reset: reset
    }
};

export const authenticatedUserFail = (fail) => {
    return {
        type: actions.AUTHENTICATION_USER_FAIL,
        fail: fail
    }
};

export const login = (email, password, history) => {
    return dispatch => {
        setAxiosToken();
        dispatch(authenticatedUserStart(true));
        axios.post('/user/loginAdmin?email=' + email + "&password=" + password)
            .then(response => {
                dispatch(authenticatedUserSuccess(response.data))
                dispatch(authenticatedUserStart(false));
                if (response.data !== ''){
                    history.push('/home');
                    dispatch(authenticatedUserReset(true));
                } else {
                    dispatch(authenticatedUserFail(true));
                }
            })
            .catch(error => {
                dispatch(authenticatedUserStart(false));
                dispatch(authenticatedUserError(error));
            })
    }
}