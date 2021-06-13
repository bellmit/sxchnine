import * as actionTypes from '../actions/actionTypes';

const initialState = {
    products: [],
    types: [],
    size: [],
    gender: [],
    loading: false,
    error: '',
    recommendedProducts: [],
    errorRecommendedProducts: ''
}


const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.LOAD_PRODUCTS_START:
            return {
                ...state,
                loading: action.loading
            };
        case actionTypes.LOAD_PRODUCTS_SUCCESS:
            let products = [...state.products];
            products = products.concat(action.products);
            return {
                ...state,
                products: products
            };
        case actionTypes.LOAD_RECOMMENDED_PRODUCTS_SUCCESS:
            return {
                ...state,
                recommendedProducts: action.recommendedProducts
            };
        case actionTypes.LOAD_PRODUCTS_FAIL:
            return {
                ...state,
                error: action.error
            };
        case actionTypes.CLEAR_PRODUCTS:
            let oldProducts = [...state.products];
            oldProducts = []
            return {
                ...state,
                products: oldProducts
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
        case actionTypes.SEARCH_PRODUCTS_SUCCESS:
            return {
                ...state,
                products: action.products
            };
        case actionTypes.SEARCH_PRODUCTS_START:
            return {
                ...state,
                loading: action.loading
            };
        case actionTypes.SEARCH_ADVANCED_PRODUCTS_START:
            return {
                ...state,
                loading: action.loading
            };
        case actionTypes.SEARCH_ADVANCED_PRODUCTS_SUCCESS:
            return {
                ...state,
                products: action.products
            };
        case actionTypes.HOME_SEARCH_ADVANCED_PRODUCTS_START:
            return {
                ...state,
                loading: action.loading
            };
        case actionTypes.HOME_SEARCH_ADVANCED_PRODUCTS_SUCCESS:
            return {
                ...state,
                products: action.products
            };
        default:
            return state;

    }
}

export default reducer;