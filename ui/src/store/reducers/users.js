import * as actionTypes from '../actions/actionTypes';

const initialState = {
    loading: false,
    error: undefined,
    loginFailError: undefined,
    userAuthenticated: '',
    userAuth: '',
    status: '',
    userChangedPassword: false,
    errorChangedPassword: '',
    addedUser: false,

    subscribeUserLoading: false,
    subscribedUser: '',
    subscribeUserError: undefined,

    forgotPasswordLoading: false,
    forgotPasswordSuccess: '',
    forgotPasswordNotExistError: false,
    forgotPasswordError: undefined


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
        case actionTypes.ADDED_USER:
            return {
                ...state,
                addedUser: action.addedUser
            };
        case actionTypes.LOGIN_USER_START:
            return {
                ...state,
                loading: action.loading
            };
        case actionTypes.LOGIN_USER_FAIL:
            return {
                ...state,
                loginFailError: action.loginFailError
            };
        case actionTypes.LOGIN_USER_SUCCESS:
            return {
                ...state,
                userAuthenticated: action.user
            };

        case actionTypes.CHANGE_PASSWORD_USER_START:
            return {
                ...state,
                loading: action.loading
            };
        case actionTypes.CHANGE_PASSWORD_USER_FAIL:
            return {
                ...state,
                errorChangedPassword: action.errorChangedPassword
            };
        case actionTypes.CHANGE_PASSWORD_FAIL_INIT:
            return {
                ...state,
                errorChangedPassword: action.errorChangedPassword
            };
        case actionTypes.CHANGE_PASSWORD_USER_SUCCESS:
            return {
                ...state,
                userChangedPassword: action.userChangedPassword
            };
        case actionTypes.CHANGE_PASSWORD_SUCCESS_INIT:
            return {
                ...state,
                userChangedPassword: action.userChangedPassword
            };
        case actionTypes.SIGNOFF_USER_SUCESS:
            return {
                ...state,
                userAuthenticated: action.user
            };
        case actionTypes.SUBSCRIBE_USER_START:
            return {
                ...state,
                subscribeUserLoading: action.subscribeUserLoading
            };
        case actionTypes.SUBSCRIBE_USER_SUCCESS:
            return {
                ...state,
                subscribedUser: action.subscribeUserSuccess
            };
        case actionTypes.SUBSCRIBE_USER_ERROR:
            return {
                ...state,
                subscribeUserError: action.subscribeUserError
            };
        case actionTypes.FORGOT_PASSWORD_START:
            return {
                ...state,
                loading: action.forgotPasswordLoading
            };
        case actionTypes.FORGOT_PASSWORD_SUCCESS:
            return {
                ...state,
                forgotPasswordSuccess: action.forgotPasswordSuccess
            };
        case actionTypes.FORGOT_PASSWORD_NOT_EXIST:
            return {
                ...state,
                forgotPasswordNotExistError: action.forgotPasswordNotExistError
            };
        case actionTypes.FORGOT_PASSWORD_FAIL:
            return {
                ...state,
                forgotPasswordError: action.forgotPasswordError
            };
        default:
            return state;
    }
};

export default reducer;