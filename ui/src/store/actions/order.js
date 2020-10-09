import * as actionTypes from './actionTypes';
import axios from '../../axios/axios';


export const order = (productsToOrder, history) => {
    return dispatch => {
        dispatch(orderStart(true));
        axios.post('/order/checkoutOrder', productsToOrder)
            .then(response => {
                if (response.data.status === 'CONFIRMED'){
                    dispatch(orderSuccess(response.data));
                    dispatch(orderStart(false));
                    history.replace('/confirmation/1');
                } else if (response.data.status === 'REQUIRED_ACTION'){
                    dispatch(orderStart(false));
                    console.log("TODO: Handle required action");
                    window.location.replace(response.data.nextAction);
                } else {
                    dispatch(orderError(response.data));
                    history.replace('/confirmation/0');
                    dispatch(orderStart(false));
                }
                //this.props.history.replace('/confirmation/' + this.props.paymentStatus);

            })
            .catch(error => {
                dispatch(orderStart(false));
                dispatch(orderError(error))
            })
    }
};

export const confirmOrder = (payment_intentId) => {
    return dispatch => {
        dispatch(orderStart(true));

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


export const fetchOrdersHistory = (email) => {
    return dispatch => {
        axios.get('/order/userEmail/'+email)
            .then(response => {
            dispatch(fetchOrdersHistorySuccess(response.data));
            console.log(response.data);
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


