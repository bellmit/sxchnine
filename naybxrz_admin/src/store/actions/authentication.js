import auth from '../../axios/auth';
import * as actionTypes from './actions';

export const authenticate = () => {
    return dispatch => {
        auth.get('/auth/authenticate')
            .then(response => {
                dispatch(authenticateSuccess(response.data));
            })
            .catch(error => {
                dispatch(authenticateError(error));
            })
    }
}

export const authenticateSuccess = (data) => {
    return {
        type: actionTypes.AUTHENTICATION_SUCCESS,
        data: data
    };
}
export const authenticateError = (error) => {
    return {
        type: actionTypes.AUTHENTICATION_ERROR,
        error: error
    };
}

