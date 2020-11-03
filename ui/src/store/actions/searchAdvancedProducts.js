import * as actionTypes from '../actions/actionTypes';
import axios from '../../axios/axios';
import {store} from "../../index";

export const searchAdvancedProducts = (gender, category, size) => {
    return dispatch => {
        dispatch(searchAdvancedStart(true));
        axios.get('/elastic/advancedSearch?gender='+ gender + '&category='+ category +'&size='+ size,{
            headers: {
                'Authorization': 'Bearer ' + store.getState().authentication.data.access_token
            }
        })
            .then(response => {
                dispatch(searchAdvancedSuccess(response.data));
                dispatch(searchAdvancedStart(false));
            })
            .catch(error => {
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


export const homeSearchProducts = (gender, category, size, history) => {
    return dispatch => {
        dispatch(homeSearchStart(true));
        axios.get('/elastic/advancedSearch?gender='+ gender + '&category='+ category +'&size='+ size,{
            headers: {
                'Authorization': 'Bearer ' + store.getState().authentication.data.access_token
            }
        })
            .then(response => {
                dispatch(homeSearchSuccess(response.data));
                dispatch(homeSearchStart(false));
                if (gender === 'W')
                    history.push('/women');
                else if (gender === 'M')
                    history.push('/men');
            }).catch(error => {
            dispatch(homeSearchStart(false));
            dispatch(homeSearchFail(error));
        })
    }
};

export const homeSearchStart = (loading) => {
    return {
        type: actionTypes.HOME_SEARCH_ADVANCED_PRODUCTS_START,
        loading: loading
    }
};

export const homeSearchSuccess = (products) => {
    return {
        type: actionTypes.HOME_SEARCH_ADVANCED_PRODUCTS_SUCCESS,
        products: products
    }
};

export const homeSearchFail = (error) => {
    return {
        type: actionTypes.HOME_SEARCH_ADVANCED_PRODUCTS_FAIL,
        error: error
    }
};