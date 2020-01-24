import React, {Component} from 'react';
import Aux from '../../hoc/Aux/Aux';
import Head from '../Head/Head';
import Middle from '../../containers/Middle/Middle';
import Footer from '../Footer/Footer';
import * as auth from '../../authentication/authentication';

class Home extends Component {

    componentDidMount(): void {
        console.log(auth.auth);
    }

    render() {
        return (
            <Aux>
                <Head/>
                <Middle/>
                <Footer/>
            </Aux>
        );
    }
}

export default Home;