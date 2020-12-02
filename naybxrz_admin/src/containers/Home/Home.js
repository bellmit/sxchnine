import React, {Component} from 'react';
import Aux from '../../adhoc/Aux/Aux';
import './Home.css';
import Dashboard from "../Dashboard/Dashboard";
import OrdersGridByMonth from "../Orders/OrdersGridByMonth";

class Home extends Component {

    render() {
        return (
            <Aux>
                <Dashboard />
                <OrdersGridByMonth {...this.props}/>
            </Aux>
        )
    };
}

export default Home;