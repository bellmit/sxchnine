import axios from '../../axios/axios';
import * as actions from './actions';
import {store} from "../../index";
import {orderByIdPopup} from "./orders";

const setAxiosToken = () => {
    axios.defaults.headers.common['Authorization'] = 'Bearer ' + store.getState().authentication.data.access_token
};

const searchProductsStart = (loading) => {
    return {
        type: actions.SEARCH_PRODUCTS_START,
        searchProductsStart: loading
    }
};

const searchProductsSuccess = (response) => {
    return {
        type: actions.SEARCH_PRODUCTS_SUCCESS,
        searchProductsData: response
    }
};

const searchProductsFail = (error) => {
    return {
        type: actions.SEARCH_PRODUCTS_FAIL,
        searchProductsError: error
    }
};

export const searchProducts = (productId, productName, brand, sex) => {
    return dispatch => {
        setAxiosToken();
        dispatch(searchProductsStart(true));
        axios.get('/product/admin/searchProducts?id='+productId+'&name='+productName+'&brand='+brand+'&sex='+sex)
            .then(response => {
                dispatch(searchProductsSuccess(response.data));
                dispatch(searchProductsStart(false));
            })
            .catch(error => {
                dispatch(searchProductsStart(false));
                dispatch(searchProductsFail(error));
            })
    }
};

export const productByIdPopup = (open) => {
    return {
        type: actions.PRODUCT_BY_ID_POPUP,
        productByIdPopup: open
    }
};

const productByIdStart = (loading) => {
    return {
        type: actions.PRODUCT_BY_ID_START,
        productByIdStart: loading
    }
};

const productByIdSuccess = (response) => {
    return {
        type: actions.PRODUCT_BY_ID_SUCCESS,
        productByIdData: response
    }
};

const productByIdFail = (error) => {
    return {
        type: actions.PRODUCT_BY_ID_FAIL,
        productByIdError: error
    }
};

export const getProductById = (productId) => {
    return dispatch => {
        setAxiosToken();
        dispatch(productByIdStart(true));
        axios.get('/product/id/'+productId)
            .then(response => {
                dispatch(productByIdSuccess(response.data));
                dispatch(productByIdStart(false));
                dispatch(productByIdPopup(true));

            })
            .catch(error => {
                dispatch(productByIdStart(false));
                dispatch(productByIdFail(error));
            })
    }
}