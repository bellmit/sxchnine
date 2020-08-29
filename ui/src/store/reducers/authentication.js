import * as actionTypes from '../actions/actionTypes';

const initialState = {
    data: '',
    error: ''
};

const reducer = (state = initialState, action) => {
    switch (action.type){
        case actionTypes.AUTHENTICATION_SUCCESS:
            return {
                ...state,
                data: action.data
            };
        case actionTypes.AUTHENTICATION_ERROR:
            return {
                ...state,
                error: action.error
            }
        default:
            return state;
    }
}

export default reducer;