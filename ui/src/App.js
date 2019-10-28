import React from 'react';
import './App.css';
import Menu from './Menu/Menu';
import Head from './Head/Head';
import Middle from './Middle/Middle';
import Footer from './Footer/Footer';
import Contact from './Contact/Contact';
import Products from './Product/Products';
import ProductSlick from './Shopping/ProductSlick';

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


    <ProductSlick />


    </div>
  );
}

export default App;
