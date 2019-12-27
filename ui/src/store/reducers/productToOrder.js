import * as actionTypes from '../actions/actionTypes';

const initialState = {
    productsToOrder: []
}

const reducer = (state = initialState, action) => {
    switch(action.type) {
        case actionTypes.ADD_PRODUCT_TO_ORDER:
            return {
                ...state,
                productsToOrder: state.productsToOrder.concat(action.productToOrder)
            };
        case actionTypes.REMOVE_PRODUCT_TO_ORDER:
            let products = [...state.productsToOrder];
            products.splice(action.id, 1);
            return {
                ...state,
                productsToOrder: products
            }
        default: return state;
    }
}

export default reducer;