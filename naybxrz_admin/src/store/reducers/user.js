import * as actions from '../actions/actions'

const initialState = {
    loading: false,
    userFail: false,
    authenticatedUser: '',
    userError: ''
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actions.AUTHENTICATION_USER_SUCCESS:
            return {
                ...state,
                authenticatedUser: action.user
            };
        case actions.AUTHENTICATION_USER_FAIL:
            return {
                ...state,
                userFail: action.fail
            };
        case actions.AUTHENTICATION_USER_START:
            return {
                ...state,
                loading: action.loading
            };
        case actions.AUTHENTICATION_USER_ERROR:
            return {
                ...state,
                userError: action.error
            };
        case actions.AUTHENTICATION_USER_RESET:
            return {
                ...state,
                userError: '',
                userFail: false
            };
        default:
            return state;
    }
}

export default reducer;