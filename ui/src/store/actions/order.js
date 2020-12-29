import * as actionTypes from './actionTypes';
import axios from '../../axios/axios';
import {store} from "../../index";

const setAxiosToken = () => {
    axios.defaults.headers.common['Authorization'] = 'Bearer ' + store.getState().authentication.data.access_token
};

export const order = (productsToOrder, history) => {
    return dispatch => {
        dispatch(orderStart(true));
        history.push('/processing');
        window.localStorage.setItem("orderId", productsToOrder.orderKey.orderId);
        axios.post('/order/checkoutOrder', productsToOrder, {
            headers: {
                'Authorization': 'Bearer ' + store.getState().authentication.data.access_token
            }
        })
            .then(response => {
                if (response.data.status === 'CONFIRMED') {
                    dispatch(orderSuccess(response.data));
                    dispatch(orderStart(false));
                    history.replace('/confirmation/1');
                    window.localStorage.removeItem("orderId");
                } else if (response.data.status === 'REQUIRED_ACTION') {
                    dispatch(orderSuccess(response.data));
                    dispatch(orderStart(false));
                    window.location.replace(response.data.nextAction);
                } else if (response.data.status === 'WAITING'
                    || response.data.status === 'CHECKOUT_CALL_ERROR'
                    || response.data.status === 'CONFIRM_CALL_ERROR') {
                    dispatch(orderSuccess(response.data));
                    dispatch(orderStart(false));
                    history.replace('/confirmation/2');
                    window.localStorage.removeItem("orderId");
                } else {
                    dispatch(orderSuccess(response.data));
                    console.log(response.data);
                    if (response.data.errorReason.code === 'incorrect_number'
                        || response.data.errorReason.code === 'invalid_number'
                        || response.data.errorReason.code === 'expired_card'
                        || response.data.errorReason.code === 'incorrect_cvc'
                        || response.data.errorReason.code === 'invalid_cvc'
                        || response.data.errorReason.code === 'invalid_expiry_year') {
                        dispatch(orderStart(false));
                        dispatch(orderHandledError(response.data));
                        history.goBack();

                    } else {
                        dispatch(orderStart(false));
                        history.replace('/confirmation/0');
                        window.localStorage.removeItem("orderId");
                    }

                }
            })
            .catch(error => {
                dispatch(orderStart(false));
                dispatch(orderError(error));
                history.replace('/confirmation/0');
                window.localStorage.removeItem("orderId");
            })
    }
};

export const confirmOrder = (paymentIntentId, orderId, history) => {
    return dispatch => {
        setAxiosToken();
        dispatch(orderStart(true));
        axios.post('/order/confirmOrder?paymentIntentId=' + paymentIntentId + '&orderId=' + orderId)
            .then(response => {
                if (response.data.status === 'CONFIRMED') {
                    dispatch(orderSuccess(response.data));
                    dispatch(orderStart(false));
                    history.replace('/confirmation/1');
                    window.localStorage.removeItem("orderId");
                } else {
                    dispatch(orderSuccess(response.data));
                    history.replace('/confirmation/0');
                    dispatch(orderStart(false));
                    window.localStorage.removeItem("orderId");
                }
            })
            .catch(error => {
                dispatch(orderError(error));
                history.replace('/confirmation/0');
                dispatch(orderStart(false));
                window.localStorage.removeItem("orderId");
            });
    }
}


export const orderStart = (start) => {
    return {
        type: actionTypes.ORDER_START,
        loading: start
    }
};

export const orderSuccess = (paymentStatus) => {
    return {
        type: actionTypes.ORDER_SUCCESS,
        paymentStatus: paymentStatus
    }
};

export const orderError = (error) => {
    return {
        type: actionTypes.ORDER_FAIL,
        error: error
    }
};

export const orderHandledError = (response) => {
    return {
        type: actionTypes.ORDER_ERROR_HANDLED,
        handledErrors: response
    }
};


export const fetchOrdersHistory = (email) => {
    return dispatch => {
        axios.get('/order/userEmail/' + email, {
            headers: {
                'Authorization': 'Bearer ' + store.getState().authentication.data.access_token
            }
        })
            .then(response => {
                dispatch(fetchOrdersHistorySuccess(response.data));
            }).catch(error => {
            dispatch(fetchOrdersHistoryError(error))
        })
    }
};

export const fetchOrdersHistorySuccess = (orders) => {
    return {
        type: actionTypes.FETCH_ORDERS_HISTORY_SUCCESS,
        ordersHistory: orders
    }
};

export const fetchOrdersHistoryError = (error) => {
    return {
        type: actionTypes.FETCH_ORDERS_HISTORY_FAIL,
        error: error
    }
};

export const trackOrder = (orderId, email) => {
    return dispatch => {
        dispatch(trackOrderStart(true));
        axios.get('/order/trackOrder?orderId=' + orderId + '&email=' + email, {
            headers: {
                'Authorization': 'Bearer ' + store.getState().authentication.data.access_token
            }
        })
            .then(response => {
                dispatch(trackOrderSuccess(response.data));
                dispatch(trackOrderStart(false));
                if (response.data.length > 0) {
                    dispatch(trackOrderFound(true));
                } else {
                    dispatch(trackOrderFound(false));
                }
            })
            .catch(error => {
                dispatch(trackOrderStart(false));
                dispatch(trackOrderError(error));
            })
    }
};

export const trackOrderStart = (start) => {
    return {
        type: actionTypes.TRACK_ORDER_START,
        loading: start
    }
};

export const trackOrderSuccess = (orders) => {
    return {
        type: actionTypes.TRACK_ORDER_SUCCESS,
        trackOrder: orders
    }
};

export const trackOrderFound = (found) => {
    return {
        type: actionTypes.TRACK_ORDER_FOUND,
        trackOrderFound: found
    }
};

export const trackOrderError = (error) => {
    return {
        type: actionTypes.TRACK_ORDER_FAIL,
        error: error
    }
};


