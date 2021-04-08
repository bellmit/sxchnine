import * as actionTypes from '../actions/actionTypes';

const initialState = {
    loading: false,
    error: '',
    paymentStatus: '',
    handledErrors: undefined,
    ordersHistory: [],
    trackOrder: [],
    trackOrderFound: undefined
};


const reducer = (state = initialState, action) => {
    switch (action.type) {
        case (actionTypes.ORDER_START):
            return {
                ...state,
                loading: action.loading
            };
        case (actionTypes.ORDER_SUCCESS):
            return {
                ...state,
                paymentStatus: action.paymentStatus.status
            };
        case (actionTypes.ORDER_FAIL):
            return {
                ...state,
                error: action.error
            };
        case (actionTypes.ORDER_ERROR_HANDLED):
            return {
                ...state,
                handledErrors: action.handledErrors
            };
        case (actionTypes.FETCH_ORDERS_HISTORY_SUCCESS):
            return {
                ...state,
                ordersHistory: action.ordersHistory
            };
        case (actionTypes.FETCH_ORDERS_HISTORY_FAIL):
            return {
                ...state,
                error: action.error
            };
        case (actionTypes.TRACK_ORDER_START):
            return {
                ...state,
                loading: action.loading
            };
        case (actionTypes.TRACK_ORDER_SUCCESS):
            return {
                ...state,
                trackOrder: action.trackOrder
            };
        case (actionTypes.TRACK_ORDER_FOUND):
            return {
                ...state,
                trackOrderFound: action.trackOrderFound
            };
        case (actionTypes.TRACK_ORDER_FAIL):
            return {
                ...state,
                error: action.error
            };
        default:
            return state;
    }
};

export default reducer;