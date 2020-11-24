import axios from '../../axios/axios';
import moment from 'moment';
import {store} from "../../index";
import * as actions from './actions';

const setAxiosToken = () => {
    axios.defaults.headers.common['Authorization'] = 'Bearer ' + store.getState().authentication.data.access_token
};

const ordersByMonthStart = (loading) => {
    return {
        type: actions.ORDERS_BY_MONTH_START,
        action: loading
    }
};

const ordersByMonthSuccess = (response) => {
    return {
        type: actions.ORDERS_BY_MONTH_SUCCESS,
        ordersByMonth: response
    }
};

const ordersByMonthError = (error) => {
    return {
        type: actions.ORDERS_BY_MONTH_FAIL,
        ordersByMonthError: error
    }
};

export const ordersByMonth = () => {
    return dispatch => {
        setAxiosToken();
        dispatch(ordersByMonthStart(true));
        axios.get('/order/lastOrders')
            .then(response => {
                dispatch(ordersByMonthSuccess(response.data));
                dispatch(ordersByMonthStart(false));
            })
            .catch(error => {
                dispatch(ordersByMonthError(error));
                dispatch(ordersByMonthStart(false));
            })
    }
}

const ordersNumberSuccess = (response) => {
    return {
        type: actions.ORDERS_NUMBERS_SUCCESS,
        ordersNumber: response
    }
};

const ordersNumberFail = (error) => {
    return {
        type: actions.ORDERS_NUMBERS_FAIL,
        ordersNumbersError: error
    }
};

export const getOrdersNumber = () => {
    return dispatch => {
        setAxiosToken();
        axios.get('/order/admin/ordersNumber')
            .then(response => {
                dispatch(ordersNumberSuccess(response.data));
            })
            .catch(error => {
                dispatch(ordersNumberFail(error));
            })
    }
};

export const orderByIdPopup = (open) => {
    return {
        type: actions.ORDER_BY_ID_POPUP,
        orderByIdPopup: open
    }
};

const orderByIdStart = (orderByIdLoading) => {
    return {
        type: actions.ORDER_BY_ID_START,
        orderByIdLoading: orderByIdLoading
    }
};

const orderByIdFail = (error) => {
    return {
        type: actions.ORDER_BY_ID_FAIL,
        orderByIdError: error
    }
};

const orderByIdSuccess = (response) => {
    return {
        type: actions.ORDER_BY_ID_SUCCESS,
        orderById: response
    }
};

export const orderById = (orderId) => {
    return dispatch => {
        setAxiosToken();
        dispatch(orderByIdStart(true));
        axios.get('/order/orderId/' + orderId)
            .then(response => {
                let order = response.data;
                if (order !== '') {
                    let shippingTime = order.shippingTime;
                    if (shippingTime !== null) {
                        order.shippingTime = moment(shippingTime).format("YYYY-MM-DD");
                    }
                    dispatch(orderByIdSuccess(order));
                    dispatch(orderByIdStart(false));
                    dispatch(orderByIdPopup(true));

                }
            })
            .catch(error => {
                dispatch(orderByIdFail(error));
                dispatch(orderByIdStart(false));
            })
    }
}

const saveOrderStart = (loading) => {
    return {
        type: actions.SAVE_ORDER_START,
        saveOrderLoading: loading
    }
};

const saveOrderSuccess = (response) => {
    return {
        type: actions.SAVE_ORDER_SUCCESS,
        saveOrderResponse: response
    }
};

const saveOrderFail = (error) => {
    return {
        type: actions.SAVE_ORDER_FAIL,
        saveOrderError: error
    }
};

export const saveOrder = (order) => {
    return dispatch => {
        setAxiosToken();
        dispatch(saveOrderStart(true));
        axios.post('/order/save', order)
            .then(response => {
                dispatch(saveOrderSuccess(response.data));
                dispatch(ordersByMonth());
                dispatch(getOrdersNumber());
                dispatch(orderByIdPopup(false));
                dispatch(saveOrderStart(false));
            })
            .catch(error => {
                dispatch(saveOrderFail(error));
                dispatch(saveOrderStart(false));
            });
    }
}

const searchOrdersStart = (loading) => {
    return {
        type: actions.SEARCH_ORDERS_START,
        searchOrdersLoading: loading
    }
};

const searchOrdersSuccess = (searchOrdersData) => {
    return {
        type: actions.SEARCH_ORDERS_SUCCESS,
        searchOrdersData: searchOrdersData
    }
};

const searchOrdersFail = (error) => {
    return {
        type: actions.SEARCH_ORDERS_FAIL,
        searchOrdersFail: error
    }
};

export const searchOrders = (orderId, email) => {
    return dispatch => {
        setAxiosToken();
        dispatch(searchOrdersStart(true));
        axios.get('/order/trackOrder?orderId=' + orderId + '&email=' + email)
            .then(response => {
                    let order = response.data;
                    if (order !== '') {
                        let shippingTime = order.shippingTime;
                        if (shippingTime !== null) {
                            order.shippingTime = moment(shippingTime).format("YYYY-MM-DD");
                        }
                        dispatch(searchOrdersSuccess(order));
                        dispatch(searchOrdersStart(false));
                    }
                }
            )
            .catch(error => {
                dispatch(searchOrdersFail(error));
                dispatch(searchOrdersStart(false));
            });
    }
}