import * as actionTypes from '../actions/actionTypes';

const initialState = {
    loading: false,
    error: ''
}

const reducer = (state= initialState, action) =>{
    switch (action.type){
        case actionTypes.CONTACT_START:
            return {
                ... state,
                loading: action.loading
            };
        case actionTypes.CONTACT_FAIL:
            return {
                ...state,
                error: action.error
            };
        default:
            return state;
    }
};

export default reducer;