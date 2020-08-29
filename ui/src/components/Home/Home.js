import React, {Component} from 'react';
import {connect} from 'react-redux';
import Aux from '../../hoc/Aux/Aux';
import Head from '../Head/Head';
import Middle from '../../containers/Middle/Middle';
import Footer from '../Footer/Footer';
import * as actionTypes from '../../store/actions/authentication';

class Home extends Component {

    componentDidMount(): void {
        this.props.authenticate();
    }

    render() {
        return (
            <Aux>
                <Head {...this.props}/>
                <Middle/>
                <Footer/>
            </Aux>
        );
    }
}

const mapDispatchToProps = dispatch => {
    return {
        authenticate: () => dispatch(actionTypes.authenticate())
    };
}

export default connect(null, mapDispatchToProps)(Home);