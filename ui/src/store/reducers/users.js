import * as actionTypes from '../actions/actionTypes';

const initialState = {
    loading: false,
    error: '',
    users: '',
    userAuth: '',
    status: ''
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.ADD_USER_START:
            return {
                ...state,
                loading: action.loading
            };
        case actionTypes.ADD_USER_FAIL:
            return {
                ...state,
                error: action.error
            };
        case actionTypes.ADD_USER_AUTH:
            return {
                ...state,
                userAuth: action.user
            };
        case actionTypes.LOGIN_USER_START:
            return {
                ...state,
                loading: action.loading
            };
        case actionTypes.LOGIN_USER_FAIL:
            return {
                ...state,
                error: action.error
            };
        case actionTypes.LOGIN_USER_SUCCESS:
            return {
                ...state,
                status: action.status
            };

        default:
            return state;
    }
};

export default reducer;