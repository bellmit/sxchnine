import * as actionTypes from './actionTypes';
import axios from '../../axios/axios';
import {store} from "../../index";

export const handleProductSuccess = (product) => {
    return {
        type: actionTypes.LOAD_PRODUCT_SUCCESS,
        product: product
    }
}

export const handleProductError = (error) => {
    return {
        type: actionTypes.LOAD_PRODUCT_FAIL,
        error: error
    }
}


export const startLoadingProduct = (loading) => {
    return {
        type: actionTypes.LOAD_PRODUCT_START,
        loading: loading
    }
}

export const loadProduct = (id, history) => {
    return dispatch => {
        dispatch(startLoadingProduct(true));
        axios.get('/product/id/'+id, {
            headers: {
                'Authorization': 'Bearer ' + store.getState().authentication.data.access_token
            }
        })
            .then(response => {
            dispatch(handleProductSuccess(response.data));
            dispatch(startLoadingProduct(false));
            history.push('/products/' + id);
        })
            .catch(error => {
            dispatch(handleProductError(error));
            dispatch(startLoadingProduct(false));

        });
    }
}