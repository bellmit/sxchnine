import * as actionTypes from './actionTypes';

export const addProductToOrder = (productToOrder) => {
    return {
        type: actionTypes.ADD_PRODUCT_TO_ORDER,
        productToOrder: productToOrder
    }
}

export const removeProductToOrder = (id) => {
    return {
        type: actionTypes.REMOVE_PRODUCT_TO_ORDER,
        id: id
    }
}

