import * as actionTypes from './actionTypes';
import axios from '../../axios/axios';


export const order = (productsToOrder) => {
    return dispatch => {
        dispatch(orderStart(true));
        axios.post('/order/checkoutOrder', productsToOrder)
            .then(response => {
                dispatch(orderSuccess(response.data));
                dispatch(orderStart(false));
            })
            .catch(error => {
                dispatch(orderStart(false));
                dispatch(orderError(error))
            })
    }
};


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