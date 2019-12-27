import * as actionTypes from './actionTypes';
import axios from '../../axios/axios';

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
        axios.get('/id/'+id).then(response => {
            dispatch(handleProductSuccess(response.data));
            history.push('/products/' + id);
            dispatch(startLoadingProduct(false));
        }).catch(error => {
            dispatch(handleProductError(error));
        });
    }
}