import * as actionTypes from './actionTypes';
import axios from '../../axios/axios';
import {store} from '../../index';

export const fetchProduct = (pageNo, pageSize, sex) => {
    return dispatch => {
        dispatch(loadProductsStart(true));
        axios.get('/product/allBySex?pageNo='+pageNo+'&pageSize='+pageSize+'&sex='+sex, {
            headers: {
                'Authorization': 'Bearer ' + store.getState().authentication.data
            }
        })
            .then(response => {
                if (response.data.length === 0)
                    return;
                dispatch(loadProducts(response.data));
                dispatch(loadProductsStart(false));
            })
            .catch(error => {
            dispatch(loadProductsFail(error)) ;
            dispatch(loadProductsStart(false));

        })
    }
};


export const loadProductsStart = (loading) => {
    return {
        type: actionTypes.LOAD_PRODUCTS_START,
        loading: loading
    }
};

export const loadProducts = ( products ) => {
    return {
        type: actionTypes.LOAD_PRODUCTS_SUCCESS,
        products: products
    }
}


export const loadProductsFail = ( error ) => {
    return {
        type: actionTypes.LOAD_PRODUCTS_FAIL,
        error: error
    }
};

export const clearProducts = () => {
    return {
        type: actionTypes.CLEAR_PRODUCTS
    }
}


export const loadGenders = () => {
    return {
        type: actionTypes.LOAD_GENDER,
        gender: [
            {key: 'm', text: 'Male', value: 'm'},
            {key: 'f', text: 'Female', value: 'w'}
        ]
    }
}

export const loadTypes = () => {
    return {
        type: actionTypes.LOAD_TYPE,
        types: [
            {key: '1', text: 'Hoodie', value: 'hoodie'},
            {key: '2', text: 'T-Shirt', value: 'tshirt'},
            {key: '3', text: 'Sweatshirt', value: 'sweat'},
            {key: '4', text: 'Jacket', value: 'jacket'},
        ]
    }
}

export const loadSize = () => {
    return {
        type: actionTypes.LOAD_SIZE,
        size: [
            {key: '1', text: 'Small', value: 'S'},
            {key: '2', text: 'Medium', value: 'M'},
            {key: '3', text: 'Large', value: 'L'},
            {key: '4', text: 'XL', value: 'XL'},
        ]
    }
}