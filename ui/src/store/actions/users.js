import axios from '../../axios/axios';
import  * as actionTypes from './actionTypes';

export const saveUserStart = (loading) => {
    return {
        type: actionTypes.ADD_USER_START,
        loading: loading
    }
};

export const saveUserFail = (error) => {
    return {
        type: actionTypes.ADD_USER_FAIL,
        error: error
    }
};

export const saveUserAuth = (userToAdd) => {
    return {
        type: actionTypes.ADD_USER_AUTH,
        user: userToAdd
    }
};

export const saveUser = (userToAdd) => {
    return dispatch => {
        dispatch(saveUserStart(true));
        axios.post('/user/save', userToAdd)
            .then(response => {
                dispatch(saveUserAuth(userToAdd));
                dispatch(saveUserStart(false));
            })
            .catch(error => {
                dispatch(saveUserStart(false));
                dispatch(saveUserFail(error));
            });
    }
};


export const loginUserStart = (loading) => {
    return {
        type: actionTypes.LOGIN_USER_START,
        loading: loading
    }
};

export const loginUserSuccess = (response) => {
    return {
        type: actionTypes.LOGIN_USER_SUCCESS,
        user: response
    }
};

export const loginUserFail = (error) => {
    return {
        type: actionTypes.LOGIN_USER_FAIL,
        error: error
    }
};

export const loginUser = (email, password, history) => {
    return dispatch => {
        dispatch(loginUserStart(true));
        axios.post('/user/login?email='+email+"&password="+password)
            .then(response => {
                dispatch(loginUserStart(false));
                dispatch(loginUserSuccess(response.data))
                if (response.data.email != null){
                    history.push('/orders')
                }
            })
            .catch(error => {
                dispatch(loginUserFail(error));
                dispatch(loginUserStart(false));
            });
    }
};

