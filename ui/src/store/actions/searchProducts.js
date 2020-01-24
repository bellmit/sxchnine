import * as actionTypes from './actionTypes';
import axios from '../../axios/axios';

export const searchProducts = (event) => {
    return dispatch => {
        const CancelToken = axios.CancelToken;
        let cancel;
        dispatch(searchProductsStart(true));
        console.log(event);
        if (cancel !== undefined) {
            console.log('cancel request');
            cancel();
        }
        axios.get('/elastic/search/'+event, {
                cancelToken: new CancelToken(c => cancel = c)
            }).then(response => {
            console.log(response.data);
            dispatch(searchProductsSuccess(response.data));
            dispatch(searchProductsStart(false));
        }).catch(error => {
            if (axios.isCancel(error)) {
                console.log("shoud cancel");
                return;
            }
            dispatch(searchProductsFail(error));
            dispatch(searchProductsStart(false));
        })
        return () => cancel();
    }
};

export const searchProductsStart = (loading) => {
    return {
        type: actionTypes.SEARCH_PRODUCTS_START,
        loading: loading
    }
};

export const searchProductsSuccess = (products) => {
    return {
        type: actionTypes.SEARCH_PRODUCTS_SUCCESS,
        products: products
    }
};

export const searchProductsFail = (error) => {
    return {
        type: actionTypes.SEARCH_PRODUCTS_FAIL,
        error: error
    }
};