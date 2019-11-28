import React from 'react';
import { Route } from 'react-router-dom';
import './App.css';
import Menu from './components/Menu/Menu';
import Home from './components/Home/Home';
import Contact from './components/Contact/Contact';
import Products from './containers/Products/Products';
import ProductSlick from './containers/Shopping/ProductSlick';
import Orders from "./containers/Payment/Orders";
import Confirmation from './components/Confirmation/Confirmation';

function App() {
  return (
    <div className="App-Container">
        <Menu />
        <Route  path="/" exact component={Home}/>
        <Route  path="/men" exact component={Products}/>
        <Route  path="/products/:productId" exact component={ProductSlick}/>
{/*
        <Home />
*/}
{/*
        <Products />
*/}




{/*
    <ProductSlick />
*/}





{/*
<Orders />
*/}



{/*
    <Confirmation/>
*/}


    </div>
  );
}

export default App;
