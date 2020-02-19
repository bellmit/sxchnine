import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter} from "react-router-dom";
import { Provider } from 'react-redux';
import { createStore, combineReducers, applyMiddleware, compose } from "redux";
import  thunk from 'redux-thunk';
import './index.css';
import App from './App';
import productsReducer from '../src/store/reducers/products';
import productDetails from '../src/store/reducers/productDetails';
import productsToOrder from '../src/store/reducers/productToOrder';
import order from '../src/store/reducers/order';
import users from '../src/store/reducers/users';
import * as serviceWorker from './serviceWorker';

const reducers = combineReducers ({
    products: productsReducer,
    product: productDetails,
    productsToOrder: productsToOrder,
    order: order,
    users: users
});

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const store = createStore(reducers, composeEnhancers(applyMiddleware(thunk)));

const app = <Provider store={store}><BrowserRouter><App /></BrowserRouter></Provider>;

ReactDOM.render(app, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.register();
