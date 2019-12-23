import * as actionTypes from '../actions/actionTypes';

const initialState = {
    product: null,
    loading: false,
    error: null
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.LOAD_PRODUCT_START:
            return {
                ...state,
                loading: action.loading
            }
        case actionTypes.LOAD_PRODUCT_SUCCESS:
            return {
                ...state,
                product: action.product
            }
        case actionTypes.LOAD_PRODUCT_FAIL:
            return  {
                ...state,
                error: action.error
            }
        default:
            return state;
    }

}

export default reducer;

