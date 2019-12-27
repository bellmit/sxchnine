import * as actionTypes from '../actions/actionTypes';

const initialState = {
    products: [],
    types: [],
    size: [],
    gender: [],
    loading: false,
    error: ''
}


const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.LOAD_PRODUCTS_START:
            return {
                ...state,
                loading: action.loading
            };
        case actionTypes.LOAD_PRODUCTS_SUCCESS:
            return {
                ...state,
                products: action.products
            };
        case actionTypes.LOAD_PRODUCTS_FAIL:
            return {
                ...state,
                error: action.error
            };
        case actionTypes.LOAD_GENDER:
            return {
                ...state,
                gender: action.gender
            };
        case actionTypes.LOAD_TYPE:
            return {
                ...state,
                types: action.types
            };
        case actionTypes.LOAD_SIZE:
            return {
                ...state,
                size: action.size
            };
        default:
            return state;

    }
}

export default reducer;