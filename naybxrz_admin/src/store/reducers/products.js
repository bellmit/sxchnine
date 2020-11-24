import * as actions from '../actions/actions';
import {productByIdPopup} from "../actions/products";

const initialState = {
    searchProductsLoading: false,
    searchProductsData: [],
    searchProductsError: '',
    productByIdLoading: false,
    productByIdData: '',
    productByIdError: '',
    productByIdPopup: false
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actions.SEARCH_PRODUCTS_START:
            return {
                ...state,
                searchProductsLoading: action.searchProductsLoading
            };
        case actions.SEARCH_PRODUCTS_SUCCESS:
            return {
                ...state,
                searchProductsData: action.searchProductsData
            };
        case actions.SEARCH_PRODUCTS_FAIL:
            return {
                ...state,
                searchProductsError: action.searchProductsError
            };
        case actions.PRODUCT_BY_ID_START:
            return {
                ...state,
                productByIdLoading: action.productByIdLoading
            };
        case actions.PRODUCT_BY_ID_SUCCESS:
            return {
                ...state,
                productByIdData: action.productByIdData
            };
        case actions.PRODUCT_BY_ID_FAIL:
            return {
                ...state,
                productByIdError: action.productByIdError
            };
        case actions.PRODUCT_BY_ID_POPUP:
            return {
                ...state,
                productByIdPopup: action.productByIdPopup
            }
        default:
            return state;
    }
}

export default reducer;