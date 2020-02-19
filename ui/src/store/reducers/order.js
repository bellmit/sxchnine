import * as actionTypes from '../actions/actionTypes';

const initialState = {
    loading: false,
    error: '',
    paymentStatus: -1
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
                paymentStatus: action.paymentStatus
            };
        case (actionTypes.ORDER_FAIL):
            return {
                ...state,
                error: action.error
            };
        default:
            return state;
    }
}

export default reducer;