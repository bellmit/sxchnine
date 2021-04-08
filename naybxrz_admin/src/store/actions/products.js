import axios from '../../axios/axios';
import * as actions from './actions';
import {store} from "../../index";
import {List} from "semantic-ui-react";
import React from "react";

const setAxiosToken = () => {
    axios.defaults.headers.common['Authorization'] = 'Bearer ' + store.getState().authentication.data
};

const searchProductsStart = (loading) => {
    return {
        type: actions.SEARCH_PRODUCTS_START,
        searchProductsLoading: loading
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
        axios.get('/product/admin/searchProducts?id=' + productId + '&name=' + productName + '&brand=' + brand + '&sex=' + sex)
            .then(response => {
                dispatch(searchProductsSuccess(response.data));
                dispatch(searchProductsStart(false));
                dispatch(searchProductsFail(undefined));
            })
            .catch(error => {
                dispatch(searchProductsStart(false));
                dispatch(searchProductsFail(error));
            })
    }
};

export const addProductClicked = (history) => {
    return dispatch => {
        dispatch(productByIdSuccess(undefined));
        dispatch(productByIdPopup(true));
        history.push('/product/new');
    }
};

export const closeProductModalAndRedirectBack = (history) => {
    return dispatch => {
        dispatch(productByIdPopup(false));
        history.goBack();
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

export const getProductById = (productId, history) => {
    return dispatch => {
        setAxiosToken();
        dispatch(productByIdStart(true));
        axios.get('/product/id/' + productId)
            .then(response => {
                dispatch(productByIdSuccess(response.data));
                dispatch(productByIdStart(false));
                dispatch(productByIdPopup(true));
                dispatch(productByIdFail(undefined));
                history.push('/product/' + productId);

            })
            .catch(error => {
                dispatch(productByIdStart(false));
                dispatch(productByIdFail(error));
            })
    }
};

const saveProductStart = (loading) => {
    return {
        type: actions.SAVE_PRODUCT_START,
        saveProductLoading: loading
    }
};

const saveProductFail = (error) => {
    return {
        type: actions.SAVE_PRODUCT_FAIL,
        saveProductError: error
    }
};

export const saveProduct = (product, history) => {
    return dispatch => {
        setAxiosToken();
        dispatch(saveProductStart(true));
        axios.post('/product/save', product)
            .then(response => {
                dispatch(saveProductStart(false));
                dispatch(productByIdPopup(false));
                history.goBack();

            })
            .catch(error => {
                dispatch(saveProductStart(false));
                dispatch(saveProductFail(error));
                history.goBack();
            })
    }
}

const bulkProductsStart = (loading) => {
    return {
        type: actions.BULK_PRODUCTS_START,
        bulkProductsLoading: loading
    }
};

const bulkProductsFail = (error) => {
    return {
        type: actions.BULK_PRODUCTS_FAIL,
        bulkProductsError: error
    }
};

const bulkProductsSuccess = (response) => {
    return {
        type: actions.BULK_PRODUCTS_SUCCESS,
        bulkProductsSuccess: response
    }
};

export const bulkProducts = (products) => {
    return dispatch => {
        setAxiosToken();
        dispatch(bulkProductsStart(true));
        axios.post('/product/bulk', products, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                dispatch(bulkProductsStart(false));
                dispatch(bulkProductsSuccess("success"));
                dispatch(bulkProductsFail(undefined));
            })
            .catch(error => {
                dispatch(bulkProductsStart(false));
                dispatch(bulkProductsFail(error));
                dispatch(bulkProductsSuccess(undefined));
            })
    }
}