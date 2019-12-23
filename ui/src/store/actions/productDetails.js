import * as actionTypes from './actionTypes';

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
    console.log('im here to dispatch actions ' + id);
        const fetchProduct = {
            id: 1,
            name: 'Classic retro - 90s',
            brand: 'Nike',
            logo: '',
            price: 90,
            images: [
                {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
            ]
        }

        const error = {
            code: 404,
            reason: 'Not found'
        }
    return dispatch => {
        console.log('im here to dispatch actions inside' + id);
        dispatch(startLoadingProduct(true));
        dispatch(handleProductSuccess(fetchProduct));
        history.push('/products/' + id);
        dispatch(startLoadingProduct(false));

        /*        setTimeout(() => {
                    // Yay! Can invoke sync or async actions with `dispatch`
                    dispatch(handleProductSuccess(fetchProduct));
                    dispatch(startLoadingProduct(false));

                }, 5000);*/
        dispatch(handleProductError(error));
    }
}