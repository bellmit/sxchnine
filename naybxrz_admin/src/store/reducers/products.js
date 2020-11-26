import * as actions from '../actions/actions';

const initialState = {
    searchProductsLoading: false,
    searchProductsData: [],
    searchProductsError: undefined,
    productByIdLoading: false,
    productByIdData: '',
    productByIdError: '',
    productByIdPopup: false,
    saveProductLoading: false,
    saveProductError: undefined
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
            };
        case actions.SAVE_PRODUCT_START:
            return {
                ...state,
                saveProductLoading: action.saveProductLoading
            };
        case actions.SAVE_PRODUCT_FAIL:
            return {
                ...state,
                saveProductError: action.saveProductError
            };
        default:
            return state;
    }
}

export default reducer;