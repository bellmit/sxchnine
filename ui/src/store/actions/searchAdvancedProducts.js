import * as actionTypes from '../actions/actionTypes';
import axios from '../../axios/axios';

export const searchAdvancedProducts = (gender, category, size) => {
    return dispatch => {
        dispatch(searchAdvancedStart(true));
        axios.get('/elastic/advancedSearch?gender='+ gender + '&category='+ category +'&size='+ size)
            .then(response => {
                dispatch(searchAdvancedSuccess(response.data));
                dispatch(searchAdvancedStart(false));
            }).catch(error => {
            dispatch(searchAdvancedStart(false));
            dispatch(searchAdvancedFail(error));
        })
    }
};

export const searchAdvancedStart = (loading) => {
    return {
        type: actionTypes.SEARCH_ADVANCED_PRODUCTS_START,
        loading: loading
    }
};

export const searchAdvancedSuccess = (products) => {
    return {
        type: actionTypes.SEARCH_ADVANCED_PRODUCTS_SUCCESS,
        products: products
    }
};

export const searchAdvancedFail = (error) => {
    return {
        type: actionTypes.SEARCH_ADVANCED_PRODUCTS_FAIL,
        error: error
    }
};