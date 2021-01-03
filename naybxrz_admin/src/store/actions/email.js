import axios from "../../axios/axios";
import {store} from "../../index";
import * as actions from './actions';

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
}