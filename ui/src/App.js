import React from 'react';
import { Route } from 'react-router-dom';
import './App.css';
import Menu from './containers/Menu/Menu';
import Logo from './components/Logo/Logo';
import Home from './components/Home/Home';
import Products from './containers/Products/Products';
import ProductSlick from './containers/Shopping/ProductSlick';
import Checkout from './containers/Checkout/Checkout';
import Orders from "./containers/Payment/Orders";
import Confirmation from './components/Confirmation/Confirmation';

function App() {
  return (
    <div className="App-Container">
        <Menu />
        <Logo />
        <Route  path="/" exact component={Home}/>
        <Route  path="/men" exact component={Products}/>
        <Route  path="/women" exact component={Products}/>
        <Route  path="/products/:productId" exact component={ProductSlick}/>
        <Route  path="/checkout" exact component={Checkout}/>
        <Route  path="/orders" exact component={Orders}/>
        <Route  path="/confirmation/:status" exact component={Confirmation} />

    </div>
  );
}

export default App;
