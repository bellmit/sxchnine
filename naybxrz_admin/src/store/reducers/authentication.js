import * as actions from '../actions/actions';

const initialState = {
    data: '',
    error: ''
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actions.AUTHENTICATION_SUCCESS:
            return {
                ...state,
                data: action.data
            };
        case actions.AUTHENTICATION_ERROR:
            return {
                ...state,
                error: action.error
            };
        default:
            return state;
    }
}

export default reducer;