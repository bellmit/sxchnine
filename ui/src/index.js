import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter} from "react-router-dom";
import { Provider } from 'react-redux';
import { createStore, combineReducers } from "redux";
import './index.css';
import App from './App';
import productsReducer from '../src/store/reducers/products';
import * as serviceWorker from './serviceWorker';

const reducers = combineReducers ({
    products: productsReducer
})

const store = createStore(reducers)

const app = <Provider store={store}><BrowserRouter><App /></BrowserRouter></Provider>;

ReactDOM.render(app, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.register();
