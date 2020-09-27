import * as actionTypes from './actionTypes';
import axios from '../../axios/axios';


export const contact = (contact) => {
    return dispatch => {
        dispatch(contactStart(true));
        axios.post("/mail/contact", contact)
            .then(response => {
                dispatch(contactStart(false));
            })
            .catch(error => {
                dispatch(contactStart(false));
                dispatch(contactError(error));
            })
    }
}

export const contactStart = (start) => {
    return {
        type: actionTypes.CONTACT_START,
        loading: start
    }
}

export const contactError = (error) => {
    return {
        type: actionTypes.CONTACT_FAIL,
        error: error
    }
}