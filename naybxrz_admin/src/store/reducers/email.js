import * as actions from './../actions/actions';

const initialState = {
    subscriptionLoading: false,
    subscriptionUsersSuccess: false,
    subscriptionUsersFail: undefined,

    sendUpdatesToUsersLoading: false,
    sendUpdatesToUsersSuccess: false,
    sendUpdatesToUsersFail: undefined
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actions.SUBSCRIPTION_EMAIL_START:
            return {
                ...state,
                subscriptionLoading: action.subscriptionLoading
            };
        case actions.SUBSCRIPTION_EMAIL_SUCCESS:
            return {
                ...state,
                subscriptionUsersSuccess: action.subscriptionUsersSuccess
            };
        case actions.SUBSCRIPTION_EMAIL_FAIL:
            return {
                ...state,
                subscriptionUsersFail: action.subscriptionUsersFail
            };
        case actions.USER_UPDATES_START:
            return {
                ...state,
                sendUpdatesToUsersLoading: action.sendUpdatesToUsersLoading
            };
        case actions.USER_UPDATES_SUCCESS:
            return {
                ...state,
                sendUpdatesToUsersSuccess: action.sendUpdatesToUsersSuccess
            };
        case actions.USER_UPDATES_FAIL:
            return {
                ...state,
                sendUpdatesToUsersFail: action.sendUpdatesToUsersFail
            };
        default:
            return state;
    }
}

export default reducer;