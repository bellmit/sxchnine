import * as actions from './../actions/actions';

const initialState = {
    subscriptionLoading: false,
    subscriptionUsersSuccess: false,
    subscriptionUsersFail: undefined
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
        default:
            return state;
    }
}

export default reducer;