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
import authenticationReducer from '../src/store/reducers/authentication';
import productsReducer from '../src/store/reducers/products';
import productDetails from '../src/store/reducers/productDetails';
import productsToOrder from '../src/store/reducers/productToOrder';
import order from '../src/store/reducers/order';
import users from '../src/store/reducers/users';
import contact from '../src/store/reducers/contact';
import * as serviceWorker from './serviceWorker';
import ScrollToTop from './components/Scroll/ScrollToTop.js'


const reducers = combineReducers({
    authentication: authenticationReducer,
    products: productsReducer,
    product: productDetails,
    productsToOrder: productsToOrder,
    order: order,
    users: users,
    contact: contact
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


const app = <Provider store={store}><BrowserRouter><ScrollToTop><PersistGate
    persistor={persistor}><App/></PersistGate></ScrollToTop></BrowserRouter></Provider>;


ReactDOM.render(app, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
/*serviceWorker.register({
    onUpdate: async registration => {
        // We want to run this code only if we detect a new service worker is
        // waiting to be activated.
        // Details about it: https://developers.google.com/web/fundamentals/primers/service-workers/lifecycle
        if (registration && registration.waiting) {
            await registration.unregister();
            // Makes Workbox call skipWaiting()
            registration.waiting.postMessage({ type: 'SKIP_WAITING' });
            // Once the service worker is unregistered, we can reload the page to let
            // the browser download a fresh copy of our app (invalidating the cache)
            window.location.reload();
        }
    },
});*/