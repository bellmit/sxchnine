import React from 'react';
import Aux from '../../hoc/Aux/Aux';
import Head from '../Head/Head';
import Middle from '../../containers/Middle/Middle';
import Footer from '../Footer/Footer';

const home = () => {
    return (
        <Aux>
            <Head />
            <Middle />
            <Footer />
        </Aux>
    );
}

export default home;