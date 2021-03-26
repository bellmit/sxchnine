import React, {Component} from 'react';
import { Route, Redirect } from 'react-router-dom';
import {store} from "../../index";

const PrivateRoute = ({ component: Component, ...rest }) => (
    <Route {...rest} render={(props) => (
        store.getState().users.userAuthenticated !== ''
            ? <Component {...props} />
            : <Redirect to='/' />
    )} />
)

export default PrivateRoute;


