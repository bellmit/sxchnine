import React from 'react';
import { Box, Image} from "@chakra-ui/core";
import './App.css';
import Menu from './Menu/Menu';
import Head from './Head/Head';
import Middle from './Middle/Middle';
import Footer from './Footer/Footer';
import Contact from './Contact/Contact';
import Products from './Product/Products';

function App() {
  return (
    <div className="App-Container">
        <Menu />
{/*        <Head />
        <Middle />
        <Footer />*/}


        <Products />




    </div>
  );
}

export default App;
