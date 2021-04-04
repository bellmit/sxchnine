import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter} from "react-router-dom";
import {Provider} from 'react-redux';
import {persistStore, persistReducer} from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import {PersistGate} from 'redux-persist/integration/react';
import {encryptTransform} from 'redux-persist-transform-encrypt';
import {createStore, combineReducers, applyMiddleware, compose} from "redux";
import thunk from 'redux-thunk';
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import authentication from './store/reducers/authentication';
import user from './store/reducers/user';
import orders from './store/reducers/orders';
import products from './store/reducers/products';
import email from './store/reducers/email';

const reducers = combineReducers({
    authentication: authentication,
    user: user,
    orders: orders,
    products: products,
    email: email
});

const encryptor = encryptTransform({
    secretKey: process.env.REACT_APP_REDUX,
    onError: function (error) {
        // Handle the error.
    }
});

const persistConfig = { // configuration object for redux-persist
    key: 'root',
    storage: storage, // define which storage to use
    transforms: [encryptor]
};

const persistedReducer = persistReducer(persistConfig, reducers);// create a persisted reducer


const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

export const store = createStore(persistedReducer, composeEnhancers(applyMiddleware(thunk)));

export const persistor = persistStore(store);


const app = <Provider store={store}><BrowserRouter><PersistGate
    persistor={persistor}><App/></PersistGate></BrowserRouter></Provider>;

ReactDOM.render(app, document.getElementById('root'));


// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
