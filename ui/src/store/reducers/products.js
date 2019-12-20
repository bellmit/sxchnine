import * as actionTypes from '../actions/actionTypes';

const initialState = {
    products: [],
    types: [],
    size: [],
    gender: []
}


const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.LOAD_PRODUCTS:
            return {
                ...state,
                products: action.products
            }
        case actionTypes.LOAD_GENDER:
            return {
                ...state,
                gender: action.gender
            }
        case actionTypes.LOAD_TYPE:
            return {
                ...state,
                types: action.types
            }
        case actionTypes.LOAD_SIZE:
            return {
                ...state,
                size: action.size
            }
        default:
            return state;

    }
}

export default reducer;