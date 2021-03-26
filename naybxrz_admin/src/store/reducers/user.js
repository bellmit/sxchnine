import * as actions from '../actions/actions'

const initialState = {
    loading: false,
    userFail: false,
    authenticatedUser: '',
    userError: '',

    usersLoading: false,
    usersData: undefined,
    usersError: undefined,
    usersNotFound: false,

    getUserLoading: false,
    getUserData: '',
    getUserError: undefined,
    getUserPopup: false,

    saveUserLoading: false,
    saveUserSuccess: '',
    saveUserError: undefined,

    subscribedUserLoading: false,
    subscribedUsersNumber: 0,
    subscribedUsersFail: undefined,

    getUsersLoading: false,
    getUsersFail: undefined,
    getUsersNumber: 0

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
        case actions.SEARCH_USERS_START:
            return {
                ...state,
                usersLoading: action.usersLoading
            };
        case actions.SEARCH_USERS_SUCCESS:
            return {
                ...state,
                usersData: action.usersData
            };
        case actions.SEARCH_USERS_FAIL:
            return {
                ...state,
                usersError: action.usersError
            };
        case actions.SEARCH_USERS_NOT_FOUND:
            return {
                ...state,
                usersNotFound: action.usersNotFound
            };
        case actions.GET_USER_START:
            return {
                ...state,
                getUserLoading: action.getUserLoading
            };
        case actions.GET_USER_SUCCESS:
            return {
                ...state,
                getUserData: action.getUserData
            };
        case actions.GET_USER_FAIL:
            return {
                ...state,
                getUserError: action.getUserError
            };
        case actions.GET_USERS_START:
            return {
                ...state,
                getUsersLoading: action.getUsersLoading
            };
        case actions.GET_USERS_SUCCESS:
            return {
                ...state,
                getUsersNumber: action.getUsersNumber
            };
        case actions.GET_USERS_FAIL:
            return {
                ...state,
                getUsersFail: action.getUsersFail
            };
        case actions.GET_USER_POPUP:
            return {
                ...state,
                getUserPopup: action.getUserPopup
            };
        case actions.SAVE_USER_START:
            return {
                ...state,
                saveUserLoading: action.saveUserLoading
            };
        case actions.SAVE_USER_FAIL:
            return {
                ...state,
                saveUserError: action.saveUserError
            };
        case actions.SAVE_USER_SUCCESS:
            return {
                ...state,
                saveUserSuccess: action.saveUserSuccess
            };
        case actions.USER_SUBSCRIPTION_START:
            return {
                ...state,
                subscribedUserLoading: action.subscribedUserLoading
            };
        case actions.USER_SUBSCRIPTION_SUCCESS:
            return {
                ...state,
                subscribedUsersNumber: action.subscribedUsersNumber
            };
        case actions.USER_SUBSCRIPTION_FAIL:
            return {
                ...state,
                subscribedUsersFail: action.subscribedUsersFail
            };
        default:
            return state;
    }
}

export default reducer;