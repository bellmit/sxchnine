import axios from "../../axios/axios";
import {store} from "../../index";
import * as actions from './actions';
import {USER_UPDATES_FAIL, USER_UPDATES_START, USER_UPDATES_SUCCESS} from "./actions";

const setAxiosToken = () => {
    axios.defaults.headers.common['Authorization'] = 'Bearer ' + store.getState().authentication.data.access_token
};


const subscriptionUsersStart = (loading) => {
    return {
        type: actions.SUBSCRIPTION_EMAIL_START,
        subscriptionLoading: loading
    }
};

export const subscriptionUsersSuccess = (response) => {
    return {
        type: actions.SUBSCRIPTION_EMAIL_SUCCESS,
        subscriptionUsersSuccess: response
    }
};

const subscriptionUsersFail = (error) => {
    return {
        type: actions.SUBSCRIPTION_EMAIL_FAIL,
        subscriptionUsersFail: error
    }
};

export const subscriptionUsers = () => {
    return dispatch => {
        setAxiosToken();
        dispatch(subscriptionUsersStart(true));
        axios.post('/mail/subscriptions', '')
            .then(response => {
                dispatch(subscriptionUsersStart(false));
                dispatch(subscriptionUsersSuccess(true));
            })
            .catch(error => {
                dispatch(subscriptionUsersStart(false));
                dispatch(subscriptionUsersSuccess(false));
                dispatch(subscriptionUsersFail(error));
            })
    }
};

const sendUpdatesToUsersStart = (loading) => {
    return {
        type: USER_UPDATES_START,
        sendUpdatesToUsersLoading: loading
    }
};

const sendUpdatesToUsersFail = (error) => {
    return {
        type: USER_UPDATES_FAIL,
        sendUpdatesToUsersFail: error
    }
};

export const sendUpdatesToUsersSuccess = (data) => {
    return {
        type: USER_UPDATES_SUCCESS,
        sendUpdatesToUsersSuccess: data
    }
};

export const sendUpdatesToUsers = () => {
    return dispatch => {
        setAxiosToken();
        dispatch(sendUpdatesToUsersStart(true));
        axios.post('/mail/updateUsers', '')
            .then(response => {
                dispatch(sendUpdatesToUsersStart(false));
                dispatch(sendUpdatesToUsersSuccess(true));
            })
            .catch(error => {
                dispatch(sendUpdatesToUsersStart(false));
                dispatch(sendUpdatesToUsersFail(error));
            })
    }
};
