import axios from '../../axios/axios';
import * as actionTypes from './actionTypes';
import {store} from "../../index";

const setAxiosToken = () => {
    axios.defaults.headers.common['Authorization'] = 'Bearer ' + store.getState().authentication.data.access_token
};

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

export const addedUser = (addedUser) => {
    return {
        type: actionTypes.ADDED_USER,
        addedUser: addedUser
    }
};

export const saveUser = (userToAdd) => {
    return dispatch => {
        dispatch(saveUserStart(true));
        setAxiosToken();
        axios.post('/user/save', userToAdd)
            .then(response => {
                dispatch(saveUserAuth(userToAdd));
                dispatch(addedUser(true));
                dispatch(saveUserStart(false));
            })
            .catch(error => {
                dispatch(saveUserStart(false));
                dispatch(saveUserFail(error));
            });
    }
};

export const changePasswordUserStart = (loading) => {
    return {
        type: actionTypes.CHANGE_PASSWORD_USER_START,
        loading: loading
    }
};

export const changePasswordUserSuccessInit = (response) => {
    return {
        type: actionTypes.CHANGE_PASSWORD_SUCCESS_INIT,
        userChangedPassword: response
    }
};

export const changePasswordUserSuccess = (response) => {
    return {
        type: actionTypes.CHANGE_PASSWORD_USER_SUCCESS,
        userChangedPassword: response
    }
};

export const changePasswordUserFailInit = (error) => {
    return {
        type: actionTypes.CHANGE_PASSWORD_FAIL_INIT,
        errorChangedPassword: error
    }
};

export const changePasswordUserFail = (error) => {
    return {
        type: actionTypes.CHANGE_PASSWORD_USER_FAIL,
        errorChangedPassword: error
    }
};

export const changePassword = (email, oldPassword, newPassword, confirmNewPassword) => {
    return dispatch => {
        dispatch(changePasswordUserStart(true));
        setAxiosToken();
        axios.post('/user/changePassword?email=' + email + '&oldPassword=' + oldPassword + '&newPassword=' + newPassword + '&confirmNewPassword=' + confirmNewPassword)
            .then(response => {
                dispatch(changePasswordUserStart(false));
                if (response.data.id) {
                    console.log("SUCCESS");
                    console.log(response.data);
                    dispatch(changePasswordUserSuccess(true));
                    dispatch(changePasswordUserFailInit(undefined));

                } else {
                    console.log("ERROR");
                    console.log(response.data);
                    dispatch(changePasswordUserFail(response.data.error));
                    dispatch(changePasswordUserSuccessInit(undefined));
                }

                /* if (!response.data.error){
                     console.log("ERROR");
                     console.log(response.data);
                     dispatch(changePasswordUserFail(response.data.error));
                 } else {
                     console.log("SUCCESS");
                     console.log(response.data);
                     dispatch(changePasswordUserSuccess(true));
                 }*/
            })
            .catch(error => {
                dispatch(changePasswordUserStart(false));
                dispatch(changePasswordUserSuccess(false))
                dispatch(changePasswordUserFail(error));
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

export const loginUser = (email, password, history, order) => {
    return dispatch => {
        dispatch(loginUserStart(true));
        setAxiosToken();
        axios.post('/user/login?email=' + email + "&password=" + password)
            .then(response => {
                dispatch(loginUserStart(false));
                dispatch(loginUserSuccess(response.data))
                if (response.data !== '') {
                    if (order === true) {
                        history.push('/orders');
                    } else {
                        history.push('/userAccount');
                    }
                } else {
                    dispatch(loginUserFail("Sorry mate ! you are an unknown person :O - please check again :) "));
                }
            })
            .catch(error => {
                dispatch(loginUserFail(error.data));
                dispatch(loginUserStart(false));
            });
    }
};

const signOffUserSuccess = () => {
    return {
        type: actionTypes.SIGNOFF_USER_SUCESS,
        user: ''
    }
};

export const signOffUser = (history) => {
    return dispatch => {
        dispatch(signOffUserSuccess());
        history.push('/');
    }
}




