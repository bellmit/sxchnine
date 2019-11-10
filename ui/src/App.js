import React from 'react';
import './App.css';
import Menu from './Menu/Menu';
import Head from './Head/Head';
import Middle from './Middle/Middle';
import Footer from './Footer/Footer';
import Contact from './Contact/Contact';
import Products from './Product/Products';
import ProductSlick from './Shopping/ProductSlick';
import Orders from "./Payment/Orders";
import Confirmation from './Confirmation/Confirmation';

function App() {
  return (
    <div className="App-Container">
        <Menu />
{/*        <Head />
        <Middle />
        <Footer />*/}
{/*
        <Products />
*/}


{/*
    <ProductSlick />
*/}

{/*
<Orders />
*/}

    <Confirmation/>


    </div>
  );
}

export default App;
