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
            return {
                ...state,
                productsToOrder: state.productsToOrder.filter( p => p.id !== action.id)
            }
        default: return state;
    }
}

export default reducer;